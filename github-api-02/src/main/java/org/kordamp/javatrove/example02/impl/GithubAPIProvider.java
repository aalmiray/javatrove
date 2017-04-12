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
package org.kordamp.javatrove.example02.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kordamp.javatrove.example02.service.GithubAPI;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author Andres Almiray
 */
public class GithubAPIProvider implements Provider<GithubAPI> {
    @Inject
    @Named(GithubAPI.GITHUB_API_URL_KEY)
    private String githubApiUrl;

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public GithubAPI get() {
        return new Retrofit.Builder()
            .baseUrl(githubApiUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
            .create(GithubAPI.class);
    }
}