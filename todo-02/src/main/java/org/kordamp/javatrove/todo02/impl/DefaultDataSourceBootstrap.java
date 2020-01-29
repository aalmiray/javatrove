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
package org.kordamp.javatrove.todo02.impl;

import org.kordamp.javatrove.todo02.database.DataSourceBootstrap;
import org.kordamp.javatrove.todo02.database.TodoRepository;
import org.kordamp.javatrove.todo02.model.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * @author Andres Almiray
 */
public class DefaultDataSourceBootstrap implements DataSourceBootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDataSourceBootstrap.class);

    @Inject private DataSource dataSource;
    @Inject private TodoRepository todoRepository;

    @Override
    public void init() {
        initSchema();
        initData();
    }

    protected void initSchema() {
        URL url = DataSourceBootstrap.class.getClassLoader().getResource("org/kordamp/javatrove/todo02/schema.ddl");

        try {
            Connection connection = dataSource.getConnection();
            try (Scanner sc = new Scanner(url.openStream()); Statement statement = connection.createStatement()) {
                sc.useDelimiter(";");
                while (sc.hasNext()) {
                    String line = sc.next().trim();
                    statement.execute(line);
                }
            } catch (IOException | SQLException e) {
                LOG.error("An error occurred when reading schema DDL from " + url, e);
            }
        } catch (SQLException e) {
            LOG.error("An error occurred when reading schema DDL from " + url, e);
        }
    }

    protected void initData() {
        todoRepository.save(Todo.builder().description("Add Javadoc").build());
    }
}
