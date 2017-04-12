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
package org.kordamp.javatrove.chat03.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kordamp.javatrove.chat03.ChatUtil;
import org.kordamp.javatrove.chat03.impl.ObjectMapperProvider;
import org.kordamp.javatrove.chat03.server.impl.ChatServerImpl;
import org.kordamp.javatrove.chat03.server.impl.ScheduledExecutorServiceProvider;
import org.kordamp.javatrove.chat03.server.impl.ServerCommandDispatcherImpl;
import org.kordamp.javatrove.chat03.server.impl.ServerLoginCommandHandler;
import org.kordamp.javatrove.chat03.server.impl.ServerLogoutCommandHandler;
import org.kordamp.javatrove.chat03.server.impl.ServerMessageCommandHandler;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

import static com.google.inject.name.Names.named;

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
        bindObjectMapper();
        bindExecutorService();
        bindCommandDispatcher();
        bindLoginCommandHandler();
        bindLogoutCommandHandler();
        bindMessageCommandHandler();
    }

    protected void bindServerPort() {
        bindConstant()
            .annotatedWith(named(ChatUtil.SERVER_PORT_KEY))
            .to(ChatUtil.SERVER_PORT);
    }

    protected void bindChatServer() {
        bind(ChatServer.class)
            .to(ChatServerImpl.class)
            .in(Singleton.class);
    }

    protected void bindObjectMapper() {
        bind(ObjectMapper.class)
            .toProvider(ObjectMapperProvider.class)
            .in(Singleton.class);
    }

    protected void bindExecutorService() {
        bind(ScheduledExecutorService.class)
            .toProvider(ScheduledExecutorServiceProvider.class)
            .in(Singleton.class);
    }

    protected void bindCommandDispatcher() {
        bind(ServerCommandDispatcher.class)
            .to(ServerCommandDispatcherImpl.class)
            .in(Singleton.class);
    }

    protected void bindLoginCommandHandler() {
        bind(ServerCommandHandler.class)
            .annotatedWith(named(ServerLoginCommandHandler.NAME))
            .to(ServerLoginCommandHandler.class)
            .in(Singleton.class);
    }

    protected void bindLogoutCommandHandler() {
        bind(ServerCommandHandler.class)
            .annotatedWith(named(ServerLogoutCommandHandler.NAME))
            .to(ServerLogoutCommandHandler.class)
            .in(Singleton.class);
    }

    protected void bindMessageCommandHandler() {
        bind(ServerCommandHandler.class)
            .annotatedWith(named(ServerMessageCommandHandler.NAME))
            .to(ServerMessageCommandHandler.class)
            .in(Singleton.class);
    }
}
