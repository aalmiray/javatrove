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
package org.kordamp.javatrove.todo01.spark;

import org.kordamp.javatrove.todo01.routes.CreateTodoRoute;
import org.kordamp.javatrove.todo01.routes.ListTodosRoute;
import org.kordamp.javatrove.todo01.routes.Routes;
import spark.template.mustache.MustacheTemplateEngine;

import javax.inject.Inject;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * @author Andres Almiray
 */
public class DefaultRoutes implements Routes {
    @Inject private ListTodosRoute listTodosRoute;
    @Inject private CreateTodoRoute createTodoRoute;

    @Override
    public void init() {
        get("/todos", listTodosRoute);
        post("/todos", createTodoRoute);
    }
}
