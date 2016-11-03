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
package org.kordamp.javatrove.example04.impl;

import io.reactivex.Observable;
import org.kordamp.javatrove.example04.model.Repository;
import org.kordamp.javatrove.example04.service.Github;
import org.kordamp.javatrove.example04.service.GithubAPI;
import org.kordamp.javatrove.example04.util.Links;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
@Component
public class GithubImpl implements Github {
    private static final Logger LOG = LoggerFactory.getLogger(GithubImpl.class);

    @Inject private GithubAPI api;

    @Override
    public Observable<Repository> repositories(String organization) {
        requireNonNull(organization, "Argument 'organization' must not be blank");

        return paginatedObservable(
            () -> {
                LOG.info("Querying /orgs/{}/repos", organization);
                return api.repositories(organization);
            },
            (Links links) -> {
                String next = links.next();
                LOG.info("Querying {}", next);
                return api.repositoriesPaginate(next);
            });
    }

    private static <T> Observable<T> paginatedObservable(FirstPageSupplier<T> firstPage, NextPageSupplier<T> nextPage) {
        requireNonNull(firstPage, "Argument 'firstPage' must not be null");
        requireNonNull(nextPage, "Argument 'nextPage' must not be null");

        return processPage(nextPage, firstPage.get());
    }

    private static <T> Observable<T> processPage(NextPageSupplier<T> supplier, Observable<Response<List<T>>> items) {
        return items.flatMap(response -> {
            if (response.isSuccessful()) {
                Links links = Links.of(response.headers().get("Link"));
                Observable<T> currentPage = Observable.fromIterable(response.body());
                if (links.hasNext()) {
                    return currentPage.concatWith(processPage(supplier, supplier.get(links)));
                }
                return currentPage;
            }
            return Observable.error(new HttpResponseException(response.code(), response.message()));
        });
    }

    private interface FirstPageSupplier<T> {
        Observable<Response<List<T>>> get();
    }

    private interface NextPageSupplier<T> {
        Observable<Response<List<T>>> get(Links links);
    }
}