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
package org.kordamp.javatrove.todo02.servlet;

import org.kordamp.javatrove.todo02.database.TodoRepository;
import org.kordamp.javatrove.todo02.model.Todos;
import org.kordamp.javatrove.todo02.routes.ListTodosRoute;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andres Almiray
 */
public class DefaultListTodosRoute extends AbstractRoute implements ListTodosRoute {
    @Inject protected TodoRepository todoRepository;

    @Override
    protected ModelAndView doHandle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Todos todos = new Todos();
        todos.getTodos().addAll(todoRepository.findAll());
        return new ModelAndView(todos, "todos.mustache");
    }
}
