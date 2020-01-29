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
package org.kordamp.javatrove.chat01.server.impl;

import com.esotericsoftware.kryonet.Server;
import org.kordamp.javatrove.chat01.ChatUtil;
import org.kordamp.javatrove.chat01.server.ChatServer;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

/**
 * @author Andres Almiray
 */
public class ChatServerImpl implements ChatServer {
    @Named(ChatUtil.SERVER_PORT_KEY)
    @Inject private int port;

    @Inject private Server server;

    @Override
    public void start() {
        try {
            server.bind(port);
            server.start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void stop() {
        server.stop();
    }
}
