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
package org.kordamp.javatrove.chat04.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kordamp.javatrove.chat04.ChatChannelInitializer;
import org.kordamp.javatrove.chat04.ChatHandler;
import org.kordamp.javatrove.chat04.ChatUtil;
import org.kordamp.javatrove.chat04.CommandDecoder;
import org.kordamp.javatrove.chat04.CommandEncoder;
import org.kordamp.javatrove.chat04.impl.ChatChannelInitializerImpl;
import org.kordamp.javatrove.chat04.impl.CommandDecoderImpl;
import org.kordamp.javatrove.chat04.impl.CommandEncoderImpl;
import org.kordamp.javatrove.chat04.impl.ObjectMapperProvider;
import org.kordamp.javatrove.chat04.server.impl.ChatServerImpl;
import org.kordamp.javatrove.chat04.server.impl.ServerChatHandlerImpl;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Singleton;

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
        bindChannelInitializer();
        bindChannelHandler();
        bindCommandEncoder();
        bindCommandDecoder();
        bindObjectMapper();
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

    protected void bindCommandEncoder() {
        bind(CommandEncoder.class)
            .to(CommandEncoderImpl.class);
    }

    protected void bindCommandDecoder() {
        bind(CommandDecoder.class)
            .to(CommandDecoderImpl.class);
    }

    protected void bindChannelInitializer() {
        bind(ChatChannelInitializer.class)
            .to(ChatChannelInitializerImpl.class);
    }

    protected void bindChannelHandler() {
        bind(ChatHandler.class)
            .to(ServerChatHandlerImpl.class);
    }

    protected void bindObjectMapper() {
        bind(ObjectMapper.class)
            .toProvider(ObjectMapperProvider.class)
            .in(Singleton.class);
    }
}
