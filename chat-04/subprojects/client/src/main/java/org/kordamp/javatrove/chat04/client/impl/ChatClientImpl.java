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
package org.kordamp.javatrove.chat04.client.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.kordamp.javatrove.chat04.ChatChannelInitializer;
import org.kordamp.javatrove.chat04.client.ChatClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.kordamp.javatrove.chat04.ChatUtil.NAME_SEPARATOR;
import static org.kordamp.javatrove.chat04.ChatUtil.loginCommand;
import static org.kordamp.javatrove.chat04.ChatUtil.logoutCommand;
import static org.kordamp.javatrove.chat04.ChatUtil.messageCommand;

/**
 * @author Andres Almiray
 */
public class ChatClientImpl implements ChatClient {
    private static final Logger LOG = LoggerFactory.getLogger(ChatClientImpl.class);

    @Inject private ChatChannelInitializer channelInitializer;

    private Channel channel;
    private EventLoopGroup group;

    @Override
    public void login(String server, int port, String name) {
        group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(channelInitializer);
            channel = bootstrap.connect(server, port)
                .sync()
                .channel();
            channel.writeAndFlush(loginCommand(name));
        } catch (Exception e) {
            LOG.error("Unexpected error", e);
            terminate();
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void logout(String name) {
        channel.writeAndFlush(logoutCommand(name));
        terminate();
    }

    @Override
    public void send(String name, String message) {
        channel.writeAndFlush(messageCommand(name + NAME_SEPARATOR + " " + message));
    }

    private void terminate() {
        if (channel != null) {
            channel.close();
            channel = null;
        }
        if (group != null) {
            group.shutdownGracefully();
            group = null;
        }
    }
}
