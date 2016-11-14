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
package org.kordamp.javatrove.chat04.client.impl;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.kordamp.javatrove.chat04.Command;
import org.kordamp.javatrove.chat04.client.ClientCommandDispatcher;
import org.kordamp.javatrove.chat04.client.ClientCommandHandler;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andres Almiray
 */
public class ClientCommandDispatcherImpl implements ClientCommandDispatcher {
    @Inject
    private Injector injector;

    private final Set<ClientCommandHandler> commandHandlers = new LinkedHashSet<>();

    @PostConstruct
    private void init() {
        List<Binding<ClientCommandHandler>> bindings = injector.findBindingsByType(TypeLiteral.get(ClientCommandHandler.class));
        for (Binding<ClientCommandHandler> binding : bindings) {
            Key<ClientCommandHandler> key = binding.getKey();
            commandHandlers.add(injector.getInstance(key));
        }
    }

    @Override
    public void dispatch(Command command) {
        commandHandlers.stream()
            .filter(handler -> handler.supports(command.getType()))
            .forEach(handler -> handler.handle(command));
    }
}
