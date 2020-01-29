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
package org.kordamp.javatrove.example05.service;

import org.kordamp.javatrove.example05.model.Repository;
import reactor.core.publisher.Flux;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

import java.util.List;

/**
 * @author Andres Almiray
 */
public interface GithubAPI {
    String GITHUB_API_URL_KEY = "GITHUB_API_URL";

    @GET("/orgs/{organization}/repos")
    Flux<Response<List<Repository>>> repositories(@Path("organization") String organization);

    @GET
    Flux<Response<List<Repository>>> repositoriesPaginate(@Url String url);
}
