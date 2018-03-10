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
package org.kordamp.javatrove.chat01;

import lombok.Builder;
import lombok.Data;

/**
 * @author Andres Almiray
 */
@Data
public class Command {
    public enum Type {
        LOGIN,
        LOGOUT,
        MESSAGE,
        DISCONNECT,
        ERROR
    }

    private Type type;
    private String payload;

    @Builder
    public static Command create(Type type, String payload) {
        Command cmd = new Command();
        cmd.setType(type);
        cmd.setPayload(payload);
        return cmd;
    }
}