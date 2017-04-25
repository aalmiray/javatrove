/*
 * Copyright 2016-2017 Andres Almiray
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
package org.kordamp.javatrove.todo02;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.gradle.archive.importer.embedded.EmbeddedGradleImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Andres Almiray
 */
@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest {
    @Deployment
    public static WebArchive createDeployment() throws Exception {
        File rootDir = new File(System.getProperty("user.dir"));
        File file = new File(rootDir, "gradle.properties");
        Properties props = new Properties();
        props.load(new FileInputStream(file));

        File[] files = Maven.resolver()
            .resolve("junit:junit:" + props.getProperty("junitVersion"),
                "org.hamcrest:hamcrest-library:" + props.getProperty("hamcrestVersion"),
                "io.rest-assured:rest-assured:" + props.getProperty("restassuredVersion"))
            .withTransitivity()
            .asFile();

        WebArchive war = ShrinkWrap.create(EmbeddedGradleImporter.class, "application.war")
            .forThisProjectDirectory()
            .importBuildOutput()
            .as(WebArchive.class)
            .addAsWebInfResource("context.xml")
            .setWebXML(new File(rootDir, "src/main/webapp/WEB-INF/web.xml"))
            .addAsLibraries(files);
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void _01_initial_data_is_loaded() {
        given().port(8888).
        when().
            get("/application/todos").
        then().
            body("todos.description", equalTo(asList("Add Javadoc")));
    }

    @Test
    public void _02_create_a_todo_item() {
        given().port(8888).
            queryParam("description", "test").
        when().
            post("/application/todos").
        then().
            body("todos.description", equalTo(asList("Add Javadoc", "test")));
    }
}