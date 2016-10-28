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
package org.kordamp.javatrove.example08.client.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.kordamp.javatrove.example08.ChatHandler;
import org.kordamp.javatrove.example08.Command;
import org.kordamp.javatrove.example08.client.ClientCommandDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * @author Andres Almiray
 */
public class ClientChatHandlerImpl extends ChannelInboundHandlerAdapter implements ChatHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ClientChatHandlerImpl.class);

    @Inject private ClientCommandDispatcher commandDispatcher;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            LOG.info("Received " + msg);
            commandDispatcher.dispatch((Command) msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
