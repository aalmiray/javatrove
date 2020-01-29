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
package org.kordamp.javatrove.todo02;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import org.kordamp.javatrove.todo02.database.DataSourceBootstrap;
import org.kordamp.javatrove.todo02.database.Sql2oHandler;
import org.kordamp.javatrove.todo02.database.TodoRepository;
import org.kordamp.javatrove.todo02.impl.DefaultDataSourceBootstrap;
import org.kordamp.javatrove.todo02.impl.DefaultSql2oHandler;
import org.kordamp.javatrove.todo02.impl.Sql2oTodoRepository;
import org.kordamp.javatrove.todo02.module.DataSourceProvider;
import org.kordamp.javatrove.todo02.module.ObjectMapperProvider;
import org.kordamp.javatrove.todo02.module.QuirksProvider;
import org.kordamp.javatrove.todo02.module.Sql2oProvider;
import org.kordamp.javatrove.todo02.routes.Route;
import org.kordamp.javatrove.todo02.servlet.DefaultCreateTodoRoute;
import org.kordamp.javatrove.todo02.servlet.DefaultListTodosRoute;
import org.sql2o.Sql2o;
import org.sql2o.quirks.Quirks;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.sql.DataSource;

import static com.google.inject.name.Names.named;

/**
 * @author Andres Almiray
 */
public class AppModule extends ExtAnnotationsModule {
    public AppModule() {
        super(AppModule.class.getPackage().getName());
    }

    @Override
    protected final void configure() {
        super.configure();
        bindObjectMapper();
        bindDataSource();
        bindQuirks();
        bindSql2o();
        bindSql2oHandler();
        bindTodoRepository();
        bindDataSourceBootstrap();
        bindListTodosRoute();
        bindCreateTodoRoute();
    }

    protected void bindObjectMapper() {
        bind(ObjectMapper.class)
            .toProvider(ObjectMapperProvider.class)
            .in(Singleton.class);
    }

    protected void bindDataSource() {
        bind(DataSource.class)
            .toProvider(DataSourceProvider.class)
            .in(Singleton.class);
    }

    protected void bindQuirks() {
        bind(Quirks.class)
            .toProvider(QuirksProvider.class)
            .in(Singleton.class);
    }

    protected void bindSql2o() {
        bind(Sql2o.class)
            .toProvider(Sql2oProvider.class)
            .in(Singleton.class);
    }

    protected void bindSql2oHandler() {
        bind(Sql2oHandler.class)
            .to(DefaultSql2oHandler.class)
            .in(Singleton.class);
    }

    protected void bindTodoRepository() {
        bind(TodoRepository.class)
            .to(Sql2oTodoRepository.class)
            .in(Singleton.class);
    }

    protected void bindDataSourceBootstrap() {
        bind(DataSourceBootstrap.class)
            .to(DefaultDataSourceBootstrap.class)
            .in(Singleton.class);
    }

    protected void bindListTodosRoute() {
        bind(Route.class)
            .annotatedWith(named(Constants.LIST_TODOS_ROUTE))
            .to(DefaultListTodosRoute.class)
            .in(Singleton.class);
    }

    protected void bindCreateTodoRoute() {
        bind(Route.class)
            .annotatedWith(named(Constants.CREATE_TODO_ROUTE))
            .to(DefaultCreateTodoRoute.class)
            .in(Singleton.class);
    }
}
