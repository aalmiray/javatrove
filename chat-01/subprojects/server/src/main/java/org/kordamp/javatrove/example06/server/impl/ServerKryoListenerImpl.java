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
package org.kordamp.javatrove.example06.server.impl;

import com.esotericsoftware.kryonet.Connection;
import org.kordamp.javatrove.example06.Command;
import org.kordamp.javatrove.example06.LogoutCommand;
import org.kordamp.javatrove.example06.server.ServerCommandDispatcher;

import javax.inject.Inject;

/**
 * @author Andres Almiray
 */
public class ServerKryoListenerImpl extends ServerKryoListener {
    @Inject
    private ServerCommandDispatcher serverCommandDispatcher;

    @Override
    public void disconnected(Connection connection) {
        NamedConnection namedConnection = (NamedConnection) connection;
        serverCommandDispatcher.dispatch(server, namedConnection, LogoutCommand.builder()
            .name(namedConnection.getName())
            .build());
    }

    @Override
    public final void received(Connection connection, Object object) {
        if (!(object instanceof Command)) {
            return;
        }
        serverCommandDispatcher.dispatch(server, (NamedConnection) connection, (Command) object);
    }
}
