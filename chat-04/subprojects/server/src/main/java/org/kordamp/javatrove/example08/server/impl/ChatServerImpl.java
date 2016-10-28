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
package org.kordamp.javatrove.example08.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.kordamp.javatrove.example08.ChatChannelInitializer;
import org.kordamp.javatrove.example08.ChatUtil;
import org.kordamp.javatrove.example08.server.ChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andres Almiray
 */
public class ChatServerImpl implements ChatServer {
    private static final Logger LOG = LoggerFactory.getLogger(ChatServerImpl.class);

    @Inject
    @Named(ChatUtil.SERVER_PORT_KEY)
    private int port;

    @Inject private ChatChannelInitializer channelInitializer;

    private EventLoopGroup rootGroup;
    private EventLoopGroup workGroup;

    @Override
    public void start() {
        rootGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                .group(rootGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitializer);
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new IllegalStateException("Unexpected error", e);
        }
    }

    @Override
    public void stop() {
        rootGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
