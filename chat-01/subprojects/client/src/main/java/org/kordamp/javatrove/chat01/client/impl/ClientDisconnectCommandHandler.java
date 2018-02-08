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
package org.kordamp.javatrove.chat01.client.impl;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import org.kordamp.javatrove.chat01.Command;
import org.kordamp.javatrove.chat01.client.ClientCommandHandler;
import org.kordamp.javatrove.chat01.client.model.AppModel;

import javax.inject.Inject;
import java.io.IOException;

import static org.kordamp.javatrove.chat01.Command.Type.DISCONNECT;

/**
 * @author Andres Almiray
 */
public class ClientDisconnectCommandHandler implements ClientCommandHandler {
    public static final String NAME = "_DISCONNECT";

    @Inject private AppModel model;

    @Override
    public boolean supports(Command.Type commandType) {
        return commandType == DISCONNECT;
    }

    @Override
    public void handle(final Client client, Connection connection, Command command) {
        model.getClient().ifPresent(c -> {
            if (c == client) {
                model.getMessages().add("Server " + model.getServer() + ":" + model.getPort() + " is no longer available.");
                client.stop();
                try {
                    client.dispose();
                } catch (IOException ignored) {
                    // OK
                }
                model.setClient(null);
                model.setConnected(false);
                model.getMessages().clear();
            }
        });
    }
}
