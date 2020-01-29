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

import org.jdeferred.AlwaysCallback;
import org.jdeferred.DoneCallback;
import org.jdeferred.DoneFilter;
import org.jdeferred.DonePipe;
import org.jdeferred.FailCallback;
import org.jdeferred.FailFilter;
import org.jdeferred.FailPipe;
import org.jdeferred.ProgressCallback;
import org.jdeferred.ProgressFilter;
import org.jdeferred.ProgressPipe;
import org.jdeferred.Promise;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public abstract class DelegatingPromise<D, F, P> implements Promise<D, F, P> {
    private final Promise<D, F, P> delegate;

    public DelegatingPromise(Promise<D, F, P> delegate) {
        this.delegate = requireNonNull(delegate, "Argument 'delegate' must not be null");
    }

    protected Promise<D, F, P> getDelegate() {
        return delegate;
    }

    @Override
    public State state() {
        return getDelegate().state();
    }

    @Override
    public boolean isPending() {
        return getDelegate().isPending();
    }

    @Override
    public boolean isResolved() {
        return getDelegate().isResolved();
    }

    @Override
    public boolean isRejected() {
        return getDelegate().isRejected();
    }

    @Override
    public Promise<D, F, P> then(DoneCallback<D> doneCallback) {
        return getDelegate().then(doneCallback);
    }

    @Override
    public Promise<D, F, P> then(DoneCallback<D> doneCallback, FailCallback<F> failCallback) {
        return getDelegate().then(doneCallback, failCallback);
    }

    @Override
    public Promise<D, F, P> then(DoneCallback<D> doneCallback, FailCallback<F> failCallback, ProgressCallback<P> progressCallback) {
        return getDelegate().then(doneCallback, failCallback, progressCallback);
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DoneFilter<D, D_OUT> doneFilter) {
        return getDelegate().then(doneFilter);
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DoneFilter<D, D_OUT> doneFilter, FailFilter<F, F_OUT> failFilter) {
        return getDelegate().then(doneFilter, failFilter);
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DoneFilter<D, D_OUT> doneFilter, FailFilter<F, F_OUT> failFilter, ProgressFilter<P, P_OUT> progressFilter) {
        return getDelegate().then(doneFilter, failFilter, progressFilter);
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DonePipe<D, D_OUT, F_OUT, P_OUT> donePipe) {
        return getDelegate().then(donePipe);
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DonePipe<D, D_OUT, F_OUT, P_OUT> donePipe, FailPipe<F, D_OUT, F_OUT, P_OUT> failPipe) {
        return getDelegate().then(donePipe, failPipe);
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DonePipe<D, D_OUT, F_OUT, P_OUT> donePipe, FailPipe<F, D_OUT, F_OUT, P_OUT> failPipe, ProgressPipe<P, D_OUT, F_OUT, P_OUT> progressPipe) {
        return getDelegate().then(donePipe, failPipe, progressPipe);
    }

    @Override
    public Promise<D, F, P> done(DoneCallback<D> callback) {
        return getDelegate().done(callback);
    }

    @Override
    public Promise<D, F, P> fail(FailCallback<F> callback) {
        return getDelegate().fail(callback);
    }

    @Override
    public Promise<D, F, P> always(AlwaysCallback<D, F> callback) {
        return getDelegate().always(callback);
    }

    @Override
    public Promise<D, F, P> progress(ProgressCallback<P> callback) {
        return getDelegate().progress(callback);
    }

    @Override
    public void waitSafely() throws InterruptedException {
        getDelegate().waitSafely();
    }

    @Override
    public void waitSafely(long timeout) throws InterruptedException {
        getDelegate().waitSafely(timeout);
    }
}
