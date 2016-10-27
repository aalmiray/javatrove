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
package org.kordamp.javatrove.example06.client.impl;

import com.esotericsoftware.kryonet.Connection;
import org.kordamp.javatrove.example06.Command;
import org.kordamp.javatrove.example06.client.ClientCommandDispatcher;
import org.kordamp.javatrove.example06.client.model.AppModel;

import javax.inject.Inject;

import static org.kordamp.javatrove.example06.ChatUtil.disconnectCommand;
import static org.kordamp.javatrove.example06.ChatUtil.loginCommand;

/**
 * @author Andres Almiray
 */
public class ClientKryoListenerImpl extends ClientKryoListener {
    @Inject private ClientCommandDispatcher clientCommandDispatcher;
    @Inject private AppModel model;

    @Override
    public void connected(Connection connection) {
        clientCommandDispatcher.dispatch(client, connection, loginCommand(model.getName()));
    }

    @Override
    public final void received(Connection connection, Object object) {
        if (!(object instanceof Command)) {
            return;
        }
        clientCommandDispatcher.dispatch(client, connection, (Command) object);
    }

    @Override
    public void disconnected(Connection connection) {
        clientCommandDispatcher.dispatch(client, connection, disconnectCommand());
    }
}
