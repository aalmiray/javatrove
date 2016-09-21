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
package org.kordamp.javatrove.example04;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.retrofit2.adapter.reactor.ReactorCallAdapterFactory;
import org.kordamp.javatrove.example04.controller.AppController;
import org.kordamp.javatrove.example04.impl.GithubImpl;
import org.kordamp.javatrove.example04.model.AppModel;
import org.kordamp.javatrove.example04.model.Repository;
import org.kordamp.javatrove.example04.service.Github;
import org.kordamp.javatrove.example04.service.GithubAPI;
import org.kordamp.javatrove.example04.util.ApplicationEventBus;
import org.kordamp.javatrove.example04.util.ApplicationEventHandler;
import org.kordamp.javatrove.example04.view.AppView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.inject.Named;

/**
 * @author Andres Almiray
 */
@Configuration
public class AppConfig {
    @Bean
    @Named(GithubAPI.GITHUB_API_URL_KEY)
    public String githubApiUrl() {
        return "https://api.github.com";
    }

    @Bean
    public Github github() {
        return new GithubImpl();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(Repository.class);
        return objectMapper;
    }

    @Bean
    public GithubAPI githubAPI(@Named(GithubAPI.GITHUB_API_URL_KEY) String githubApiUrl, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
            .baseUrl(githubApiUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(ReactorCallAdapterFactory.create())
            .build()
            .create(GithubAPI.class);
    }

    @Bean
    public ApplicationEventBus eventBus() {
        return new ApplicationEventBus();
    }

    @Bean
    public ApplicationEventHandler applicationEventHandler() {
        return new ApplicationEventHandler();
    }

    @Bean
    public AppController controller() {
        return new AppController();
    }

    @Bean
    public AppModel model() {
        return new AppModel();
    }

    @Bean
    public AppView view() {
        return new AppView();
    }
}
