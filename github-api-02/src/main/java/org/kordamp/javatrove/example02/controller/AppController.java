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

import org.kordamp.javatrove.example02.model.AppModel;
import org.kordamp.javatrove.example02.service.Github;
import org.kordamp.javatrove.example02.util.ApplicationEventBus;
import org.kordamp.javatrove.example02.util.ThrowableEvent;

import javax.inject.Inject;
import java.util.concurrent.CancellationException;

import static org.kordamp.javatrove.example02.model.State.READY;
import static org.kordamp.javatrove.example02.model.State.RUNNING;

/**
 * @author Andres Almiray
 */
public class AppController {
    @Inject private AppModel model;
    @Inject private Github github;
    @Inject private ApplicationEventBus eventBus;

    public void loadRepositories() {
        model.setState(RUNNING);
        model.setPromise(github.repositories(model.getOrganization(), model.getLimit()));
        model.getPromise()
            .progress(model.getRepositories()::add)
            .fail(throwable -> {
                if (!(throwable instanceof CancellationException)) {
                    eventBus.publishAsync(new ThrowableEvent(throwable));
                }
            })
            .always((state, resolved, rejected) -> model.setState(READY));
    }

    public void cancel() {
        if (model.getPromise() != null) {
            model.getPromise().cancel();
            model.setPromise(null);
        }
    }
}
