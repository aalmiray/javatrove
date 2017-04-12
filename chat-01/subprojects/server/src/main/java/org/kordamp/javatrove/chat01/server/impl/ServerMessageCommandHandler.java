/*
 * Copyright 2016-2017 Andres Almiray
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
package org.kordamp.javatrove.chat01.server.impl;

import com.esotericsoftware.kryonet.Server;
import org.kordamp.javatrove.chat01.Command;
import org.kordamp.javatrove.chat01.server.ServerCommandHandler;

import static org.kordamp.javatrove.chat01.Command.Type.MESSAGE;

/**
 * @author Andres Almiray
 */
public class ServerMessageCommandHandler implements ServerCommandHandler {
    public static final String NAME = "_MESSAGE_";

    @Override
    public boolean supports(Command.Type commandType) {
        return commandType == MESSAGE;
    }

    @Override
    public void handle(Server server, NamedConnection connection, Command command) {
        server.sendToAllTCP(command);
    }
}
