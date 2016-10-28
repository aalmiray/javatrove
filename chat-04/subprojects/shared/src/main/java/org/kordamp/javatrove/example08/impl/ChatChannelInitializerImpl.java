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
package org.kordamp.javatrove.example08.impl;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.kordamp.javatrove.example08.ChatChannelInitializer;
import org.kordamp.javatrove.example08.ChatHandler;
import org.kordamp.javatrove.example08.CommandDecoder;
import org.kordamp.javatrove.example08.CommandEncoder;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Andres Almiray
 */
@ChannelHandler.Sharable
public class ChatChannelInitializerImpl extends ChannelInitializer<SocketChannel> implements ChatChannelInitializer {
    @Inject private Provider<CommandEncoder> commandEncoderProvider;
    @Inject private Provider<CommandDecoder> commandDecoderProvider;
    @Inject private Provider<ChatHandler> chatHandlerProvider;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
            .addLast("decoder", commandDecoderProvider.get())
            .addLast("encoder", commandEncoderProvider.get())
            .addLast("handler", chatHandlerProvider.get());
    }
}
