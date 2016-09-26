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
package org.kordamp.javatrove.example02.impl;

import org.jdeferred.DeferredCallable;
import org.jdeferred.DeferredFutureTask;
import org.jdeferred.DeferredManager;
import org.kordamp.javatrove.example02.model.Repository;
import org.kordamp.javatrove.example02.service.Github;
import org.kordamp.javatrove.example02.service.GithubAPI;
import org.kordamp.javatrove.example02.util.CancellablePromise;
import org.kordamp.javatrove.example02.util.DefaultCancellablePromise;
import org.kordamp.javatrove.example02.util.Links;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Andres Almiray
 */
public class GithubImpl implements Github {
    private static final Logger LOG = LoggerFactory.getLogger(GithubImpl.class);

    @Inject private GithubAPI api;
    @Inject private DeferredManager deferredManager;

    @Override
    public CancellablePromise<Collection<Repository>, Throwable, Repository> repositories(final String organization, final int limit) {
        DeferredFutureTask<Collection<Repository>, Repository> futureTask = new DeferredFutureTask<>(new DeferredCallable<Collection<Repository>, Repository>() {
            @Override
            public Collection<Repository> call() throws Exception {
                int l = limit <= 0 ? Integer.MAX_VALUE : limit;
                LOG.info("Querying /orgs/{}/repos; limit={}", organization, l);
                Response<List<Repository>> response = api.repositories(organization).execute();
                return processResponse(response, l, new ArrayList<>());
            }

            private Collection<Repository> processPage(String nextPageUrl, int limit, List<Repository> accumulator) throws IOException {
                LOG.info("Querying {}; limit={}, count={}", nextPageUrl, limit, accumulator.size());
                Response<List<Repository>> response = api.repositoriesPaginated(nextPageUrl).execute();
                return processResponse(response, limit, accumulator);
            }

            private Collection<Repository> processResponse(Response<List<Repository>> response, int limit, List<Repository> accumulator) throws IOException {
                if (response.isSuccessful()) {
                    int offset = accumulator.size();
                    for (int i = 0; i < response.body().size(); i++) {
                        if (i + offset == limit) {
                            return accumulator;
                        }

                        Repository repository = response.body().get(i);
                        notify(repository);
                        accumulator.add(repository);
                    }

                    Links links = Links.of(response.headers().get("Link"));
                    if (links.hasNext()) {
                        return processPage(links.next(), limit, accumulator);
                    }
                    return accumulator;
                }
                throw new HttpResponseException(response.code(), response.message());
            }
        });

        return new DefaultCancellablePromise<>(deferredManager.when(futureTask), futureTask);
    }
}