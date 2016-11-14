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
package org.kordamp.javatrove.chat02.server;

import org.kordamp.javatrove.chat02.server.impl.ChatHandlerImpl;
import org.kordamp.javatrove.chat02.server.impl.ChatServerImpl;
import org.kordamp.javatrove.chat02.server.impl.ScheduledExecutorServiceProvider;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

import static com.google.inject.name.Names.named;
import static org.kordamp.javatrove.chat02.ChatUtil.SERVER_PORT;
import static org.kordamp.javatrove.chat02.ChatUtil.SERVER_PORT_KEY;

/**
 * @author Andres Almiray
 */
public class ServerModule extends ExtAnnotationsModule {
    public ServerModule() {
        super(ServerModule.class.getPackage().getName());
    }

    @Override
    protected final void configure() {
        super.configure();
        bindServerPort();
        bindChatServer();
        bindChatHandler();
        bindExecutorService();
    }

    protected void bindServerPort() {
        bindConstant()
            .annotatedWith(named(SERVER_PORT_KEY))
            .to(SERVER_PORT);
    }

    protected void bindChatServer() {
        bind(ChatServer.class)
            .to(ChatServerImpl.class)
            .in(Singleton.class);
    }

    protected void bindChatHandler() {
        bind(ChatHandler.class)
            .to(ChatHandlerImpl.class)
            .in(Singleton.class);
    }

    protected void bindExecutorService() {
        bind(ScheduledExecutorService.class)
            .toProvider(ScheduledExecutorServiceProvider.class)
            .in(Singleton.class);
    }
}
