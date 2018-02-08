/*
 * Copyright 2016-2018 Andres Almiray
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
package org.kordamp.javatrove.example03.controller;

import io.reactivex.Observable;
import lombok.Getter;
import net.engio.mbassy.listener.Handler;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kordamp.javatrove.example03.model.AppModel;
import org.kordamp.javatrove.example03.model.Repository;
import org.kordamp.javatrove.example03.model.State;
import org.kordamp.javatrove.example03.service.Github;
import org.kordamp.javatrove.example03.util.ApplicationEventBus;
import org.kordamp.javatrove.example03.util.ThrowableEvent;

import javax.inject.Inject;
import java.util.Collection;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.kordamp.javatrove.example03.TestHelper.createSampleRepositories;
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

    public static class Module extends JukitoModule {
        protected void configureTest() {
            bind(AppController.class);
        }
    }

    @Test
    public void happyPath(Github github) {
        // given:
        Collection<Repository> repositories = createSampleRepositories();
        when(github.repositories(ORGANIZATION)).thenReturn(Observable.fromIterable(repositories));

        // when:
        model.setOrganization(ORGANIZATION);
        controller.load();
        await().timeout(2, SECONDS).until(model::getState, equalTo(State.READY));

        // then:
        assertThat(model.getRepositories(), hasSize(10));
        assertThat(model.getRepositories(), equalTo(repositories));
        verify(github, only()).repositories(ORGANIZATION);
    }

    @Test
    public void failurePath(Github github) {
        // given:
        ApplicationEventHandlerStub eventHandler = new ApplicationEventHandlerStub();
        eventBus.subscribe(eventHandler);
        RuntimeException exception = new RuntimeException("boom");
        when(github.repositories(ORGANIZATION)).thenReturn(Observable.error(exception));

        // when:
        model.setOrganization(ORGANIZATION);
        controller.load();
        await().timeout(2, SECONDS).until(model::getState, equalTo(State.READY));

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
