/*
 * Copyright 2016 Andres Almiray
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
package org.kordamp.javatrove.example03;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kordamp.javatrove.example03.impl.GithubAPIProvider;
import org.kordamp.javatrove.example03.impl.GithubImpl;
import org.kordamp.javatrove.example03.impl.ObjectMapperProvider;
import org.kordamp.javatrove.example03.model.AppModel;
import org.kordamp.javatrove.example03.service.Github;
import org.kordamp.javatrove.example03.service.GithubAPI;
import org.kordamp.javatrove.example03.util.ApplicationEventBus;
import org.kordamp.javatrove.example03.util.ApplicationEventHandler;
import org.kordamp.javatrove.example03.view.AppView;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Singleton;

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
        bindGithubApiUrl();
        bindGithub();
        bindObjectMapper();
        bindGithubAPI();
        bindApplicationEventBus();
        bindApplicationEventHandler();
        bindAppController();
        bindAppModel();
        bindAppView();
    }

    protected void bindGithubApiUrl() {
        bindConstant()
            .annotatedWith(named(GithubAPI.GITHUB_API_URL_KEY))
            .to("https://api.github.com");
    }

    protected void bindGithub() {
        bind(Github.class)
            .to(GithubImpl.class)
            .in(Singleton.class);
    }

    protected void bindObjectMapper() {
        bind(ObjectMapper.class)
            .toProvider(ObjectMapperProvider.class)
            .in(Singleton.class);
    }

    protected void bindGithubAPI() {
        bind(GithubAPI.class)
            .toProvider(GithubAPIProvider.class)
            .in(Singleton.class);
    }

    protected void bindApplicationEventBus() {
        bind(ApplicationEventBus.class)
            .in(Singleton.class);
    }

    protected void bindApplicationEventHandler() {
        bind(ApplicationEventHandler.class)
            .asEagerSingleton();
    }

    protected void bindAppController() {
        bind(AppModel.class).in(Singleton.class);
    }

    protected void bindAppModel() {
        bind(AppModel.class).in(Singleton.class);
    }

    protected void bindAppView() {
        bind(AppView.class).in(Singleton.class);
    }
}
