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
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.kordamp.javatrove.example06.KryoUtil;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Andres Almiray
 */
public class ServerProvider implements Provider<Server> {
    @Inject
    private ServerKryoListener serverKryoListener;

    @Override
    public Server get() {
        Server server = new Server() {
            @Override
            protected Connection newConnection() {
                return new NamedConnection();
            }
        };
        KryoUtil.registerClasses(server);
        serverKryoListener.setServer(server);
        server.addListener(serverKryoListener);
        return server;
    }
}
