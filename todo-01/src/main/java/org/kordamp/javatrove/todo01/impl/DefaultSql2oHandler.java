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
package org.kordamp.javatrove.todo01.impl;

import org.kordamp.javatrove.todo01.database.Sql2oCallback;
import org.kordamp.javatrove.todo01.database.Sql2oHandler;
import org.kordamp.javatrove.todo01.exceptions.RuntimeSql2oException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class DefaultSql2oHandler implements Sql2oHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSql2oHandler.class);
    private static final String ERROR_CALLBACK_NULL = "Argument 'callback' must not be null";

    @Inject private Sql2o sql2o;

    @Override
    public <R> R withSql2o(Sql2oCallback<R> callback) throws RuntimeSql2oException {
        requireNonNull(callback, ERROR_CALLBACK_NULL);
        try {
            LOG.debug("Executing sql2o statements");
            return callback.handle(sql2o);
        } catch (Exception e) {
            throw new RuntimeSql2oException(e);
        }
    }
}
