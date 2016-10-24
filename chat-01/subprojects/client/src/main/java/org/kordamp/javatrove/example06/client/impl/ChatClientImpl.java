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

import com.esotericsoftware.kryonet.Client;
import org.kordamp.javatrove.example06.Command;
import org.kordamp.javatrove.example06.client.ChatClient;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Andres Almiray
 */
public class ChatClientImpl implements ChatClient {
    @Inject
    private Client client;

    @Override
    public void connect(int timeout, String server, int port) {
        try {
            client.connect(timeout, server, port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void disconnect() {
        try {
            client.stop();
            client.dispose();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public <T extends Command> void send(T command) {
        client.sendTCP(command);
    }
}
