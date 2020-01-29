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
package org.kordamp.javatrove.chat02;

import javax.annotation.Nonnull;
import java.security.MessageDigest;

import static org.kordamp.javatrove.chat02.Command.Type.DISCONNECT;
import static org.kordamp.javatrove.chat02.Command.Type.LOGIN;
import static org.kordamp.javatrove.chat02.Command.Type.LOGOUT;
import static org.kordamp.javatrove.chat02.Command.Type.MESSAGE;

/**
 * @author Andres Almiray
 */
public class ChatUtil {
    public static final String SERVER_PORT_KEY = "_SERVER_PORT_";
    public static final int SERVER_PORT = 54555;
    public static final String NAME_SEPARATOR = ">";

    @Nonnull
    public static String toSHA1(@Nonnull String content) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(content.getBytes());

            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return result.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static Command loginCommand(String name) {
        return Command.builder()
            .type(LOGIN)
            .payload(name)
            .build();
    }

    public static Command logoutCommand(String name) {
        return Command.builder()
            .type(LOGOUT)
            .payload(name)
            .build();
    }

    public static Command messageCommand(String message) {
        return Command.builder()
            .type(MESSAGE)
            .payload(message)
            .build();
    }

    public static Command disconnectCommand() {
        return Command.builder()
            .type(DISCONNECT)
            .build();
    }
}
