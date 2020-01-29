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
package org.kordamp.javatrove.chat01.client.impl;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import org.kordamp.javatrove.chat01.Command;
import org.kordamp.javatrove.chat01.client.ClientCommandHandler;
import org.kordamp.javatrove.chat01.client.util.ApplicationEventBus;
import org.kordamp.javatrove.chat01.client.util.ThrowableEvent;

import javax.inject.Inject;

import static org.kordamp.javatrove.chat01.Command.Type.ERROR;

/**
 * @author Andres Almiray
 */
public class ClientErrorCommandHandler implements ClientCommandHandler {
    public static final String NAME = "_ERROR_";

    @Inject private ApplicationEventBus eventBus;

    @Override
    public boolean supports(Command.Type commandType) {
        return commandType == ERROR;
    }

    @Override
    public void handle(Client client, Connection connection, Command command) {
        eventBus.publishAsync(new ThrowableEvent(new Throwable(command.getPayload())));
    }
}
