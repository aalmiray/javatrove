package org.kordamp.javatrove.example02.util;

import org.jdeferred.Promise;

/**
 * @author Andres Almiray
 */
public interface CancellablePromise<D, F, P> extends Promise<D, F, P> {
    void cancel();
}
