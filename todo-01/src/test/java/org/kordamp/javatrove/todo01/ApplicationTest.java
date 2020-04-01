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
package org.kordamp.javatrove.todo01;

import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Andres Almiray
 */
@RunWith(JukitoRunner.class)
@UseModules({AppModule.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest {
    @Inject private Application application;

    @Before
    public void setup() {
        application.start();
    }

    @After
    public void cleanup() {
        application.stop();
    }

    @Test
    public void _01_initial_data_is_loaded() {
        given().port(8080).
        when().
            get("/todos").
        then().
            body("todos.description", equalTo(asList("Add Javadoc")));
    }

    @Test
    public void _02_create_a_todo_item() {
        given().port(8080).
            queryParam("description","test").
        when().
            post("/todos").
        then().
            body("todos.description", equalTo(asList("Add Javadoc", "test")));
    }
}
