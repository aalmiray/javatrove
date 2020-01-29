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
package org.kordamp.javatrove.todo01.module;

import org.sql2o.Sql2o;
import org.sql2o.quirks.Quirks;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

/**
 * @author Andres Almiray
 */
public class Sql2oProvider implements Provider<Sql2o> {
    @Inject private DataSource dataSource;
    @Inject private Quirks quirks;

    @Override
    public Sql2o get() {
        return new Sql2o(dataSource, quirks);
    }
}
