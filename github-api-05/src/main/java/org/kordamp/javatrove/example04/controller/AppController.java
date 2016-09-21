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
package org.kordamp.javatrove.example04.controller;

import org.kordamp.javatrove.example04.model.AppModel;
import org.kordamp.javatrove.example04.model.Repository;
import org.kordamp.javatrove.example04.service.Github;
import org.kordamp.javatrove.example04.util.ApplicationEventBus;
import org.kordamp.javatrove.example04.util.ThrowableEvent;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.inject.Inject;
import java.time.Duration;

import static org.kordamp.javatrove.example04.model.State.READY;
import static org.kordamp.javatrove.example04.model.State.RUNNING;

/**
 * @author Andres Almiray
 */
public class AppController {
    @Inject private AppModel model;
    @Inject private Github github;
    @Inject private ApplicationEventBus eventBus;

    public void load() {
        Flux<Repository> flux = github.repositories(model.getOrganization());
        if (model.getLimit() > 0) {
            flux = flux.take(model.getLimit());
        }

        model.setCancellation(flux.timeout(Duration.ofSeconds(10L))
            .doOnSubscribe(subscription -> model.setState(RUNNING))
            .doOnTerminate(() -> model.setState(READY))
            .doOnError(throwable -> eventBus.publishAsync(new ThrowableEvent(throwable)))
            .subscribeOn(Schedulers.parallel())
            .subscribe(model.getRepositories()::add));
    }

    public void cancel() {
        if (model.getCancellation() != null) {
            model.getCancellation().dispose();
            model.setState(READY);
        }
    }
}
