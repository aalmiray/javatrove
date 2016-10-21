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
package org.kordamp.javatrove.example06.client.controller;

import com.esotericsoftware.kryonet.Client;
import com.google.inject.Injector;
import org.jdeferred.DeferredManager;
import org.kordamp.javatrove.example06.LoginCommand;
import org.kordamp.javatrove.example06.LogoutCommand;
import org.kordamp.javatrove.example06.MessageCommand;
import org.kordamp.javatrove.example06.client.model.AppModel;
import org.kordamp.javatrove.example06.client.util.ApplicationEventBus;
import org.kordamp.javatrove.example06.client.util.ThrowableEvent;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Andres Almiray
 */
public class AppController {
    @Inject private AppModel model;
    @Inject private DeferredManager deferredManager;
    @Inject private ApplicationEventBus eventBus;

    @Inject
    private Injector injector;

    private Client client;

    public void login() {
        deferredManager.when(() -> {
            try {
                client = injector.getInstance(Client.class);
                client.connect(5000, model.getServer(), model.getPort());
                client.sendTCP(LoginCommand.builder().name(model.getName()).build());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }).fail(this::handleException)
            .then((Void result) -> model.setConnected(true));
    }

    public void logout() {
        deferredManager.when(() -> {
            try {
                client.sendTCP(LogoutCommand.builder().name(model.getName()).build());
                client.stop();
                client.dispose();
                client = null;
                model.getMessages().clear();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }).fail(this::handleException)
            .then((Void result) -> model.setConnected(false));
    }

    public void send() {
        deferredManager.when(() -> {
            try {
                String message = model.getMessage();
                model.setMessage("");
                client.sendTCP(MessageCommand.builder().message(message).build());
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }).fail(this::handleException);
    }

    private void handleException(Throwable throwable) {
        eventBus.publishAsync(new ThrowableEvent(throwable));
    }
}
