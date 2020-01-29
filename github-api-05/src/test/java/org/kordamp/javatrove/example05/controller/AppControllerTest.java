/*
 * Copyright 2016-2020 Andres Almiray
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
package org.kordamp.javatrove.example05.controller;

import lombok.Getter;
import net.engio.mbassy.listener.Handler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kordamp.javatrove.example05.AppConfig;
import org.kordamp.javatrove.example05.model.AppModel;
import org.kordamp.javatrove.example05.model.Repository;
import org.kordamp.javatrove.example05.model.State;
import org.kordamp.javatrove.example05.service.Github;
import org.kordamp.javatrove.example05.util.ApplicationEventHandler;
import org.kordamp.javatrove.example05.util.ThrowableEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;

import javax.inject.Inject;
import java.util.Collection;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.kordamp.javatrove.example05.TestHelper.createSampleRepositories;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Andres Almiray
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppControllerTest.Config.class)
public class AppControllerTest {
    private static final String ORGANIZATION = "foo";

    @Inject private Github github;
    @Inject private AppController controller;
    @Inject private AppModel model;
    @Inject private ApplicationEventHandler applicationEventHandler;

    @Test
    public void happyPath() {
        // given:
        Collection<Repository> repositories = createSampleRepositories();
        when(github.repositories(ORGANIZATION)).thenReturn(Flux.fromIterable(repositories));

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
    public void failurePath() {
        // given:
        RuntimeException exception = new RuntimeException("boom");
        when(github.repositories(ORGANIZATION)).thenReturn(Flux.error(exception));

        // when:
        model.setOrganization(ORGANIZATION);
        controller.load();
        await().timeout(2, SECONDS).until(model::getState, equalTo(State.READY));

        // then:
        assertThat(model.getRepositories(), hasSize(0));
        assertThat(((ApplicationEventHandlerStub) applicationEventHandler).getEvent().getThrowable(), equalTo(exception));
        // verify(github, only()).repositories(ORGANIZATION);
    }

    public static class ApplicationEventHandlerStub extends ApplicationEventHandler {
        @Getter
        private ThrowableEvent event;

        @Handler
        @Override
        public void handleThrowable(ThrowableEvent event) {
            this.event = event;
        }
    }

    @Configuration
    @ComponentScan("org.kordamp.javatrove.example05")
    static class Config extends AppConfig {
        @Bean
        public Github github() {
            return mock(Github.class);
        }

        @Bean
        public ApplicationEventHandler applicationEventHandler() {
            return new ApplicationEventHandlerStub();
        }
    }
}
