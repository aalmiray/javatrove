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
package org.kordamp.javatrove.todo01.impl;

import org.kordamp.javatrove.todo01.database.Sql2oHandler;
import org.kordamp.javatrove.todo01.database.TodoRepository;
import org.kordamp.javatrove.todo01.model.Todo;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author Andres Almiray
 */
public class Sql2oTodoRepository implements TodoRepository {
    @Inject private Sql2oHandler sql2oHandler;

    @Override
    public Todo findById(Long id) {
        return sql2oHandler.withSql2o(sql2o -> sql2o.withConnection((connection, argument) -> {
            return connection.createQuery("SELECT * FROM todos WHERE id = :id")
                .addParameter("id", id)
                .executeAndFetchFirst(Todo.class);
        }));
    }

    @Override
    public void save(Todo todo) {
        if (todo.isNotPersisted()) {
            insert(todo);
        } else {
            update(todo);
        }
    }

    private void insert(final Todo todo) {
        sql2oHandler.withSql2o(sql2o -> {
            sql2o.withConnection((connection, argument) -> {
                String sql = "INSERT INTO todos(description, done) VALUES (:description, false)";

                Object key = connection.createQuery(sql)
                    .bind(todo)
                    .executeUpdate()
                    .getKey();
                todo.setId(((Number) key).longValue());
            });
            return todo;
        });
    }

    private void update(final Todo todo) {
        sql2oHandler.withSql2o(sql2o -> {
            sql2o.withConnection((connection, argument) -> {
                String sql = "UPDATE todos SET " +
                    "done = :done " +
                    "WHERE id = :id";

                connection.createQuery(sql)
                    .bind(todo)
                    .executeUpdate();
            });
            return todo;
        });
    }

    @Override
    public void delete(final Todo todo) {
        sql2oHandler.withSql2o(sql2o -> {
            sql2o.withConnection((connection, argument) -> {
                connection.createQuery("DELETE FROM todos WHERE id = :id")
                    .addParameter("id", todo.getId())
                    .executeUpdate();
            });
            return todo;
        });
    }

    @Override
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public Collection<Todo> findAll() {
        return sql2oHandler.withSql2o(sql2o -> (Collection<Todo>) sql2o.withConnection((connection, argument) -> {
            return connection.createQuery("SELECT * FROM todos").executeAndFetch(Todo.class);
        }));
    }

    @Override
    public void clear() {
        sql2oHandler.withSql2o(sql2o -> {
            sql2o.withConnection((connection, argument) -> {
                connection.createQuery("DELETE FROM todos").executeUpdate();
            });
            return null;
        });
    }
}
