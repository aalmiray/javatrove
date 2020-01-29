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
package org.kordamp.javatrove.hello.pmvc;

import org.kordamp.javatrove.hello.pmvc.controller.AppController;
import org.kordamp.javatrove.hello.pmvc.model.AppModel;
import org.kordamp.javatrove.hello.pmvc.service.DefaultHelloService;
import org.kordamp.javatrove.hello.pmvc.service.HelloService;
import org.kordamp.javatrove.hello.pmvc.view.AppView;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Singleton;

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
        bindHelloService();
        bindAppController();
        bindAppModel();
        bindAppView();
    }

    protected void bindHelloService() {
        bind(HelloService.class)
            .to(DefaultHelloService.class)
            .in(Singleton.class);
    }

    protected void bindAppController() {
        bind(AppController.class).in(Singleton.class);
    }

    protected void bindAppModel() {
        bind(AppModel.class).in(Singleton.class);
    }

    protected void bindAppView() {
        bind(AppView.class).in(Singleton.class);
    }
}
