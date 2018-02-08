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
package org.kordamp.javatrove.example06.impl;

import org.kordamp.javatrove.example06.model.Repository;
import org.kordamp.javatrove.example06.service.Github;
import org.kordamp.javatrove.example06.service.GithubAPI;
import retrofit2.Response;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * @author Andres Almiray
 */
public class GithubImpl implements Github {
    @Inject private GithubAPI api;
    @Inject private ExecutorService executorService;

    @Override
    public CompletableFuture<Collection<Repository>> repositories(final String organization) {
        Supplier<Collection<Repository>> supplier = () -> {
            try {
                Response<List<Repository>> response = api.repositories(organization).execute();
                if (response.isSuccessful()) { return response.body(); }
                throw new IllegalStateException(response.message());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        };
        return CompletableFuture.supplyAsync(supplier, executorService);
    }
}