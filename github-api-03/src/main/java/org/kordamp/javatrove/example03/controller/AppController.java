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
package org.kordamp.javatrove.example03.controller;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.kordamp.javatrove.example03.model.AppModel;
import org.kordamp.javatrove.example03.model.Repository;
import org.kordamp.javatrove.example03.service.Github;
import org.kordamp.javatrove.example03.util.ApplicationEventBus;
import org.kordamp.javatrove.example03.util.ThrowableEvent;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

import static org.kordamp.javatrove.example03.model.State.READY;
import static org.kordamp.javatrove.example03.model.State.RUNNING;

/**
 * @author Andres Almiray
 */
public class AppController {
    @Inject private AppModel model;
    @Inject private Github github;
    @Inject private ApplicationEventBus eventBus;

    public void load() {
        Observable<Repository> observable = github.repositories(model.getOrganization());
        if (model.getLimit() > 0) {
            observable = observable.take(model.getLimit());
        }

        model.setDisposable(observable
            .timeout(10, TimeUnit.SECONDS)
            .doOnSubscribe(disposable -> model.setState(RUNNING))
            .doOnTerminate(() -> model.setState(READY))
            .doOnError(throwable -> eventBus.publishAsync(new ThrowableEvent(throwable)))
            .subscribeOn(Schedulers.io())
            .subscribe(model.getRepositories()::add));
    }

    public void cancel() {
        if (model.getDisposable() != null) {
            model.getDisposable().dispose();
            model.setState(READY);
        }
    }
}

