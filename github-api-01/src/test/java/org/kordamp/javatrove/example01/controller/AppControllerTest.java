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
package org.kordamp.javatrove.example01.controller;

import lombok.Getter;
import net.engio.mbassy.listener.Handler;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kordamp.javatrove.example01.TestHelper;
import org.kordamp.javatrove.example01.model.AppModel;
import org.kordamp.javatrove.example01.model.Repository;
import org.kordamp.javatrove.example01.service.Github;
import org.kordamp.javatrove.example01.util.ApplicationEventBus;
import org.kordamp.javatrove.example01.util.ThrowableEvent;

import javax.inject.Inject;
import java.util.Collection;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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

    @Test
    public void happyPath(Github github) {
        // given:
        Collection<Repository> repositories = TestHelper.createSampleRepositories();
        Promise<Collection<Repository>, Throwable, Void> promise = new DeferredObject<Collection<Repository>, Throwable, Void>().resolve(repositories);
        when(github.repositories(ORGANIZATION)).thenReturn(promise);

        // when:
        model.setOrganization(ORGANIZATION);
        controller.loadRepositories();

        // then:
        assertThat(model.getRepositories(), hasSize(3));
        assertThat(model.getRepositories(), equalTo(repositories));
        verify(github, only()).repositories(ORGANIZATION);
    }

    @Test
    public void failurePath(Github github) {
        // given:
        ApplicationEventHandlerStub eventHandler = new ApplicationEventHandlerStub();
        eventBus.subscribe(eventHandler);
        Throwable exception = new RuntimeException("boom");
        Promise<Collection<Repository>, Throwable, Void> promise = new DeferredObject<Collection<Repository>, Throwable, Void>().reject(exception);
        when(github.repositories(ORGANIZATION)).thenReturn(promise);

        // when:
        model.setOrganization(ORGANIZATION);
        controller.loadRepositories();
        await().timeout(2, SECONDS).until(eventHandler::getEvent, notNullValue());

        // then:
        assertThat(model.getRepositories(), hasSize(0));
        assertThat(eventHandler.getEvent().getThrowable(), equalTo(exception));
        verify(github, only()).repositories(ORGANIZATION);
    }

    public static class ApplicationEventHandlerStub {
        @Getter
        private ThrowableEvent event;

        @Handler
        public void handleThrowable(ThrowableEvent event) {
            this.event = event;
        }
    }
}
