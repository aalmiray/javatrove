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
package org.kordamp.javatrove.example01.impl;

import org.jdeferred.DeferredManager;
import org.jdeferred.Promise;
import org.kordamp.javatrove.example01.model.Repository;
import org.kordamp.javatrove.example01.service.Github;
import org.kordamp.javatrove.example01.service.GithubAPI;
import retrofit.Response;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

/**
 * @author Andres Almiray
 */
public class GithubImpl implements Github {
    @Inject private GithubAPI api;
    @Inject private DeferredManager deferredManager;

    @Override
    public Promise<Collection<Repository>, Throwable, Void> repositories(final String organization) {
        return deferredManager.when(() -> {
            Response<List<Repository>> response = api.repositories(organization).execute();
            if (response.isSuccess()) { return response.body(); }
            throw new IllegalStateException(response.message());
        });
    }
}