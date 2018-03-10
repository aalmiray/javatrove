/*
 * Copyright 2016-2018 Andres Almiray
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

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.kordamp.javatrove.chat03.Command;
import org.kordamp.javatrove.chat03.server.CommandExecutionException;
import org.kordamp.javatrove.chat03.server.ServerCommandDispatcher;
import org.kordamp.javatrove.chat03.server.ServerCommandHandler;
import org.zeromq.ZMQ;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andres Almiray
 */
public class ServerCommandDispatcherImpl implements ServerCommandDispatcher {
    @Inject private Injector injector;

    private final Set<ServerCommandHandler> commandHandlers = new LinkedHashSet<>();

    @PostConstruct
    private void init() {
        List<Binding<ServerCommandHandler>> bindings = injector.findBindingsByType(TypeLiteral.get(ServerCommandHandler.class));
        for (Binding<ServerCommandHandler> binding : bindings) {
            Key<ServerCommandHandler> key = binding.getKey();
            commandHandlers.add(injector.getInstance(key));
        }
    }

    @Override
    public void dispatch(ZMQ.Socket publisher, Command command) throws CommandExecutionException {
        for (ServerCommandHandler handler : commandHandlers) {
            if (handler.supports(command.getType())) {
                try {
                    handler.handle(publisher, command);
                } catch (Exception e) {
                    throw new CommandExecutionException(e);
                }
            }
        }
    }
}
