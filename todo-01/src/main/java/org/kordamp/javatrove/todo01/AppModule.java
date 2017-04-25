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
package org.kordamp.javatrove.todo01;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import org.kordamp.javatrove.todo01.database.DataSourceBootstrap;
import org.kordamp.javatrove.todo01.database.Sql2oHandler;
import org.kordamp.javatrove.todo01.database.TodoRepository;
import org.kordamp.javatrove.todo01.impl.DefaultDataSourceBootstrap;
import org.kordamp.javatrove.todo01.impl.DefaultSql2oHandler;
import org.kordamp.javatrove.todo01.impl.Sql2oTodoRepository;
import org.kordamp.javatrove.todo01.module.DataSourceProvider;
import org.kordamp.javatrove.todo01.module.ObjectMapperProvider;
import org.kordamp.javatrove.todo01.module.QuirksProvider;
import org.kordamp.javatrove.todo01.module.Sql2oProvider;
import org.kordamp.javatrove.todo01.routes.CreateTodoRoute;
import org.kordamp.javatrove.todo01.routes.ListTodosRoute;
import org.kordamp.javatrove.todo01.routes.Routes;
import org.kordamp.javatrove.todo01.spark.DefaultCreateTodoRoute;
import org.kordamp.javatrove.todo01.spark.DefaultListTodosRoute;
import org.kordamp.javatrove.todo01.spark.DefaultRoutes;
import org.sql2o.Sql2o;
import org.sql2o.quirks.Quirks;
import ru.vyarus.guice.ext.ExtAnnotationsModule;
import spark.template.mustache.MustacheTemplateEngine;

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
        bindApplication();
        bindObjectMapper();
        bindMustacheTemplateEngine();
        bindRoutes();
        bindListTodosRoute();
        bindCreateTodoRoute();
        bindDataSourceProperties();
        bindDataSource();
        bindQuirks();
        bindSql2o();
        bindSql2oHandler();
        bindTodoRepository();
        bindDataSourceBootstrap();
    }

    protected void bindApplication() {
        bind(Application.class)
            .in(Singleton.class);
    }

    protected void bindObjectMapper() {
        bind(ObjectMapper.class)
            .toProvider(ObjectMapperProvider.class)
            .in(Singleton.class);
    }

    protected void bindMustacheTemplateEngine() {
        bind(MustacheTemplateEngine.class)
            .in(Singleton.class);
    }

    protected void bindRoutes() {
        bind(Routes.class)
            .to(DefaultRoutes.class)
            .in(Singleton.class);
    }

    protected void bindListTodosRoute() {
        bind(ListTodosRoute.class)
            .to(DefaultListTodosRoute.class)
            .in(Singleton.class);
    }

    protected void bindCreateTodoRoute() {
        bind(CreateTodoRoute.class)
            .to(DefaultCreateTodoRoute.class)
            .in(Singleton.class);
    }

    protected void bindDataSourceProperties() {
        bindConstant()
            .annotatedWith(named(Constants.DATASOURCE_CONFIG_FILE))
            .to("/org/kordamp/javatrove/todo01/datasource.properties");
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
}
