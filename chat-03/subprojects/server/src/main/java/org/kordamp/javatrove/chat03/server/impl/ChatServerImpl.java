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
package org.kordamp.javatrove.chat03.server.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kordamp.javatrove.chat03.ChatUtil;
import org.kordamp.javatrove.chat03.Command;
import org.kordamp.javatrove.chat03.server.ChatServer;
import org.kordamp.javatrove.chat03.server.ServerCommandDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Andres Almiray
 */
public class ChatServerImpl implements ChatServer {
    private static final Logger LOG = LoggerFactory.getLogger(ChatServerImpl.class);
    private static final String TCP_PREFIX = "tcp://*:";

    @Inject
    @Named(ChatUtil.SERVER_PORT_KEY)
    private int port;

    @Inject private ScheduledExecutorService scheduledExecutorService;
    @Inject private ObjectMapper objectMapper;
    @Inject private ServerCommandDispatcher serverCommandDispatcher;

    private ZMQ.Socket server;
    private ZMQ.Socket publisher;
    private ZMQ.Context context;

    @Override
    public void start() {
        context = ZMQ.context(Runtime.getRuntime().availableProcessors());
        server = context.socket(ZMQ.REP);
        publisher = context.socket(ZMQ.PUB);
        publisher.setLinger(5000);
        publisher.setSndHWM(0);

        server.bind(TCP_PREFIX + port);
        publisher.bind(TCP_PREFIX + (port + 1));

        scheduledExecutorService.submit(this::handleConnection);
    }

    private void handleConnection() {
        while (!Thread.currentThread().isInterrupted()) {
            byte[] bytes = server.recv();
            try {
                Command command = objectMapper.readValue(bytes, Command.class);
                LOG.info("received " + command);
                serverCommandDispatcher.dispatch(publisher, command);
            } catch (IOException e) {
                LOG.error("Unexpected error", e);
            }
            server.send("", 0);
        }
    }

    @Override
    public void stop() {
        server.close();
        publisher.close();
        context.term();
    }
}
