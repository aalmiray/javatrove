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
package org.kordamp.javatrove.example02.util;

import org.jdeferred.DeferredFutureTask;
import org.jdeferred.Promise;

import java.util.concurrent.CancellationException;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class DefaultCancellablePromise<D, F, P> extends DelegatingPromise<D, F, P> implements CancellablePromise<D, F, P> {
    private final DeferredFutureTask<D, P> futureTask;

    public DefaultCancellablePromise(Promise<D, F, P> delegate, DeferredFutureTask<D, P> futureTask) {
        super(delegate);
        this.futureTask = requireNonNull(futureTask, "Argument 'futureTask' must not be null");
    }

    @Override
    public void cancel() {
        try {
            futureTask.cancel(true);
        } catch (CancellationException expected) {
            // OK
        }
    }
}
