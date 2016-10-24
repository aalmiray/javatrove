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
package org.kordamp.javatrove.example07.server.impl;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.kordamp.javatrove.example07.server.ChatHandler;
import org.kordamp.javatrove.example07.server.ChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.ScheduledExecutorService;

import static org.kordamp.javatrove.example07.ChatUtil.SERVER_PORT_KEY;

/**
 * @author Andres Almiray
 */
public class ChatServerImpl implements ChatServer {
    private static final Logger LOG = LoggerFactory.getLogger(ChatServer.class);

    @Inject
    @Named(SERVER_PORT_KEY)
    private int port;

    @Inject private ChatHandler chatHandler;
    @Inject private ScheduledExecutorService scheduledExecutorService;

    private Server server;

    @Override
    public void start() throws Exception {
        LOG.info("Configuring server on port " + port);
        server = NettyServerBuilder.forPort(port)
            .addService(chatHandler)
            .build();
        LOG.info("Starting server");
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                ChatServerImpl.this.stop();
            }
        });
        blockUntilShutdown();
    }

    public void stop() {
        if (server != null) {
            LOG.info("Stopping server");
            server.shutdown();
            scheduledExecutorService.shutdownNow();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
