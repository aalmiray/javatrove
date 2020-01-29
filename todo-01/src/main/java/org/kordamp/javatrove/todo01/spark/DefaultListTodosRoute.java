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
package org.kordamp.javatrove.todo01.spark;

import org.kordamp.javatrove.todo01.database.TodoRepository;
import org.kordamp.javatrove.todo01.model.Todos;
import org.kordamp.javatrove.todo01.routes.ListTodosRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.inject.Inject;

/**
 * @author Andres Almiray
 */
public class DefaultListTodosRoute extends AbstractRoute implements ListTodosRoute {
    @Inject protected TodoRepository todoRepository;

    @Override
    protected ModelAndView doHandle(Request request, Response response) throws Exception {
        Todos todos = new Todos();
        todos.getTodos().addAll(todoRepository.findAll());
        return new ModelAndView(todos, "todos.mustache");
    }
}
