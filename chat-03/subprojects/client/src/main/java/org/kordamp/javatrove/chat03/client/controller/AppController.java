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
package org.kordamp.javatrove.chat03.client.controller;

import com.google.inject.Injector;
import org.jdeferred.DeferredManager;
import org.kordamp.javatrove.chat03.client.ChatClient;
import org.kordamp.javatrove.chat03.client.model.AppModel;
import org.kordamp.javatrove.chat03.client.util.ApplicationEventBus;
import org.kordamp.javatrove.chat03.client.util.ThrowableEvent;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author Andres Almiray
 */
public class AppController {
    @Inject private AppModel model;
    @Inject private DeferredManager deferredManager;
    @Inject private ApplicationEventBus eventBus;
    @Inject private Injector injector;

    public void login() {
        deferredManager.when(() -> {
            ChatClient client = injector.getInstance(ChatClient.class);
            client.login(model.getServer(), model.getPort(), model.getName());
            model.setClient(client);
        }).fail(this::handleException)
            .then((Void result) -> model.setConnected(true));
    }

    public void logout() {
        deferredManager.when(() -> {
            Optional<ChatClient> client = model.getClient();
            model.setClient(null);
            client.ifPresent(c -> c.logout(model.getName()));
        }).fail(this::handleException)
            .always((state, result, rejected) -> model.cleanup(true));
    }

    public void send() {
        deferredManager.when(() -> {
            String message = model.getMessage();
            model.setMessage("");
            model.getClient().ifPresent(c -> c.send(model.getName(), message));
        }).fail(this::handleException);
    }

    private void handleException(Throwable throwable) {
        model.cleanup(false);
        eventBus.publishAsync(new ThrowableEvent(throwable));
    }
}
