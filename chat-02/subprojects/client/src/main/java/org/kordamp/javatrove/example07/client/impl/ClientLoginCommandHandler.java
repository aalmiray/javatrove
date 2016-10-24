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
package org.kordamp.javatrove.example07.client.impl;

import org.kordamp.javatrove.example07.Command;
import org.kordamp.javatrove.example07.LoginCommand;
import org.kordamp.javatrove.example07.client.ClientCommandHandler;
import org.kordamp.javatrove.example07.client.model.AppModel;

import javax.inject.Inject;

/**
 * @author Andres Almiray
 */
public class ClientLoginCommandHandler implements ClientCommandHandler {
    public static final String NAME = "_LOGIN_";

    @Inject private AppModel model;

    @Override
    public <C extends Command> boolean supports(C command) {
        return command instanceof LoginCommand;
    }

    @Override
    public <C extends Command> void handle(C command) {
        LoginCommand loginCommand = (LoginCommand) command;
        model.getMessages().add(loginCommand.getName() + " connected.");
    }
}
