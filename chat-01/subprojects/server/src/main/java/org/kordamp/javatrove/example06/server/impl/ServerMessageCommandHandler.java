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

import com.esotericsoftware.kryonet.Server;
import org.kordamp.javatrove.example06.Command;
import org.kordamp.javatrove.example06.MessageCommand;
import org.kordamp.javatrove.example06.server.ServerCommandHandler;

import static org.kordamp.javatrove.example06.ChatUtil.NAME_SEPARATOR;

/**
 * @author Andres Almiray
 */
public class ServerMessageCommandHandler implements ServerCommandHandler {
    public static final String NAME = "_MESSAGE_";

    @Override
    public <C extends Command> boolean supports(C command) {
        return command instanceof MessageCommand;
    }

    @Override
    public <C extends Command> void handle(Server server, NamedConnection connection, C command) {
        MessageCommand messageCommand = (MessageCommand) command;
        MessageCommand update = MessageCommand.builder()
            .message(connection.getName() + NAME_SEPARATOR + " " + messageCommand.getMessage())
            .build();
        server.sendToAllTCP(update);
    }
}
