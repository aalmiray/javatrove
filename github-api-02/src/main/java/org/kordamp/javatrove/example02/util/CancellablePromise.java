package org.kordamp.javatrove.example02.util;

import org.jdeferred.Promise;

/**
 * @author Andres Almiray
 */
public interface CancellablePromise<D, P> extends Promise<D, Throwable, P> {
    void cancel();
}
