/*
 * Copyright 2016 Andres Almiray
 *
 * This file is part of Java Trove Examples
 *
 * Java Trove Examples is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java Trove Examples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Java Trove Examples. If not, see <http://www.gnu.org/licenses/>.
 */
package org.kordamp.javatrove.example02.controller;

import lombok.Getter;
import net.engio.mbassy.listener.Handler;
import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kordamp.javatrove.example02.TestHelper;
import org.kordamp.javatrove.example02.model.AppModel;
import org.kordamp.javatrove.example02.model.Repository;
import org.kordamp.javatrove.example02.model.State;
import org.kordamp.javatrove.example02.service.Github;
import org.kordamp.javatrove.example02.util.ApplicationEventBus;
import org.kordamp.javatrove.example02.util.CancellablePromise;
import org.kordamp.javatrove.example02.util.DelegatingPromise;
import org.kordamp.javatrove.example02.util.ThrowableEvent;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Andres Almiray
 */
@RunWith(JukitoRunner.class)
public class AppControllerTest {
    private static final String ORGANIZATION = "foo";

    @Inject private AppController controller;
    @Inject private AppModel model;
    @Inject private ApplicationEventBus eventBus;
    @Inject private ExecutorService executorService;

    public static class Module extends JukitoModule {
        protected void configureTest() {
            bind(ExecutorService.class)
                .toProvider(() -> Executors.newFixedThreadPool(2));
        }
    }

    @After
    public void cleanup() {
        executorService.shutdownNow();
    }

    @Test
    public void happyPath(Github github) {
        // given:
        List<Repository> repositories = TestHelper.createSampleRepositories().subList(0, 5);
        Deferred<Collection<Repository>, Throwable, Repository> promise = new DeferredObject<>();
        when(github.repositories(ORGANIZATION, 5)).thenAnswer(invocation -> new CancellablePromiseStub<>(promise));

        // when:
        model.setOrganization(ORGANIZATION);
        model.setLimit(5);
        controller.loadRepositories();
        executorService.submit(() -> {
            repositories.forEach(promise::notify);
            promise.resolve(repositories);
        });
        await().timeout(2, SECONDS).until(model::getState, equalTo(State.READY));

        // then:
        assertThat(model.getRepositories(), hasSize(5));
        assertThat(model.getRepositories(), equalTo(repositories));
        verify(github, only()).repositories(ORGANIZATION, 5);
    }

    @Test
    public void failurePath(Github github) {
        // given:
        ApplicationEventHandlerStub eventHandler = new ApplicationEventHandlerStub();
        eventBus.subscribe(eventHandler);
        Throwable exception = new RuntimeException("boom");
        Promise<Collection<Repository>, Throwable, Repository> promise = new DeferredObject<Collection<Repository>, Throwable, Repository>().reject(exception);
        when(github.repositories(ORGANIZATION, 5)).thenAnswer(invocation -> new CancellablePromiseStub<>(promise));

        // when:
        model.setOrganization(ORGANIZATION);
        model.setLimit(5);
        controller.loadRepositories();
        await().timeout(2, SECONDS).until(eventHandler::getEvent, notNullValue());

        // then:
        assertThat(model.getRepositories(), hasSize(0));
        assertThat(eventHandler.getEvent().getThrowable(), equalTo(exception));
        verify(github, only()).repositories(ORGANIZATION, 5);
    }

    @Test
    public void cancelPath(Github github) {
        // given:
        List<Repository> repositories = TestHelper.createSampleRepositories();
        Deferred<Collection<Repository>, Throwable, Repository> promise = new DeferredObject<>();
        CancellablePromiseStub<Collection<Repository>, Repository> stub = new CancellablePromiseStub<>(promise);
        when(github.repositories(ORGANIZATION, 10)).thenAnswer(invocation -> stub);

        // when:
        model.setOrganization(ORGANIZATION);
        model.setLimit(10);
        controller.loadRepositories();
        executorService.submit(() -> {
            repositories.forEach(repository -> {
                if (!stub.getCancelled().get()) {
                    promise.notify(repository);
                    pause(200, MILLISECONDS);
                }
            });
            promise.resolve(repositories);
        });
        executorService.submit(() -> {
            pause(600, MILLISECONDS);
            controller.cancel();
        });

        await().atLeast(500, MILLISECONDS)
            .timeout(5, SECONDS)
            .until(model::getState, equalTo(State.READY));

        // then:
        assertThat(model.getRepositories().size(), greaterThanOrEqualTo(0));
        assertThat(model.getRepositories().size(), lessThan(10));
        verify(github, only()).repositories(ORGANIZATION, 10);
    }

    private void pause(long duration, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(duration));
        } catch (InterruptedException ignored) {
            // ok
        }
    }

    private static class ApplicationEventHandlerStub {
        @Getter
        private ThrowableEvent event;

        @Handler
        public void handleThrowable(ThrowableEvent event) {
            this.event = event;
        }
    }

    private static class CancellablePromiseStub<D, P> extends DelegatingPromise<D, Throwable, P> implements CancellablePromise<D, P> {
        @Getter
        private final AtomicBoolean cancelled = new AtomicBoolean(false);

        public CancellablePromiseStub(Promise<D, Throwable, P> delegate) {
            super(delegate);
        }

        @Override
        public void cancel() {
            cancelled.set(true);
        }
    }
}
