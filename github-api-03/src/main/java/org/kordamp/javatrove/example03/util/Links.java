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
package org.kordamp.javatrove.example03.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.kordamp.javatrove.example03.util.StringUtils.isBlank;

/**
 * @author Andres Almiray
 */
public class Links {
    private static final Pattern REL_PATTERN = Pattern.compile("rel=\"(.*)\"");

    private String first;
    private String next;
    private String prev;
    private String last;

    public static Links of(String input) {
        return new Links(input);
    }

    private Links(String input) {
        if (!isBlank(input)) {
            for (String s : input.split(",")) {
                String[] parts = s.split(";");
                Matcher matcher = REL_PATTERN.matcher(parts[1].trim());
                if (matcher.matches()) {
                    switch (matcher.group(1).toLowerCase()) {
                        case "first":
                            first = normalize(parts[0]);
                            break;
                        case "next":
                            next = normalize(parts[0]);
                            break;
                        case "prev":
                            prev = normalize(parts[0]);
                            break;
                        case "last":
                            last = normalize(parts[0]);
                            break;
                    }
                }
            }
        }
    }

    private String normalize(String url) {
        url = url.trim();
        if (url.startsWith("<") && url.endsWith(">")) {
            url = url.substring(1, url.length() - 1);
        }
        return url;
    }

    public boolean hasFirst() {
        return !isBlank(first);
    }

    public boolean hasNext() {
        return !isBlank(next);
    }

    public boolean hasPrev() {
        return !isBlank(prev);
    }

    public boolean hasLast() {
        return !isBlank(last);
    }

    public String first() {
        return first;
    }

    public String next() {
        return next;
    }

    public String prev() {
        return prev;
    }

    public String last() {
        return last;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Links{");
        sb.append("first='").append(first).append('\'');
        sb.append(", next='").append(next).append('\'');
        sb.append(", prev='").append(prev).append('\'');
        sb.append(", last='").append(last).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
