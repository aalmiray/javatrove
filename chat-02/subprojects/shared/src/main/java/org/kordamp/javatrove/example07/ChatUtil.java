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
package org.kordamp.javatrove.example07;

import javax.annotation.Nonnull;
import java.security.MessageDigest;

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

            String result = "";
            for (byte b : bytes) {
                result += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
