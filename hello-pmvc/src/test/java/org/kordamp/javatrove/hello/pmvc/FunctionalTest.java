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
package org.kordamp.javatrove.hello.pmvc;

import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.kordamp.javatrove.hello.pmvc.view.AppView;
import org.testfx.framework.junit.ApplicationRule;

import javax.inject.Inject;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

/**
 * @author Andres Almiray
 */
@RunWith(JukitoRunner.class)
@UseModules({AppModule.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FunctionalTest {
    private static final String NAME = "foo";

    @Inject private AppView view;

    @Rule
    public ApplicationRule testfx = new ApplicationRule(stage -> {
        stage.setScene(view.createScene());
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    });

    @Test
    public void _01_empty_name() {
        // when:
        testfx.clickOn("#sayHelloButton");

        // then:
        verifyThat("#output", hasText("Howdy stranger!"));
    }

    @Test
    public void _02_defined_name() {
        // when:
        testfx.clickOn("#input")
            .eraseText(NAME.length())
            .write(NAME);
        testfx.clickOn("#sayHelloButton");

        // then:
        verifyThat("#output", hasText("Hello " + NAME));
    }
}
