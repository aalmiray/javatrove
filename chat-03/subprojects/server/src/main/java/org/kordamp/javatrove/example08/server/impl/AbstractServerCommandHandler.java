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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kordamp.javatrove.example08.Command;
import org.kordamp.javatrove.example08.server.ServerCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import javax.inject.Inject;

/**
 * @author Andres Almiray
 */
public abstract class AbstractServerCommandHandler implements ServerCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractServerCommandHandler.class);

    @Inject protected ObjectMapper objectMapper;

    @Override
    public void handle(ZMQ.Socket publisher, Command command) {
        try {
            LOG.info("publishing " + command);
            publisher.send(objectMapper.writeValueAsBytes(command));
        } catch (JsonProcessingException e) {
            LOG.error("Unexpected error", e);
        }
    }
}
