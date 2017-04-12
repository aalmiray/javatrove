/*
 * Copyright 2016-2017 Andres Almiray
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
package org.kordamp.javatrove.chat03.server;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;

/**
 * @author Andres Almiray
 */
public class Main {
    @Inject
    private ChatServer server;

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new ServerModule());
        Main main = new Main();
        injector.injectMembers(main);
        main.run();
    }

    private Main() {

    }

    public void run() {
        server.start();
    }
}
