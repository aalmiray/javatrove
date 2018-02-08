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
package org.kordamp.javatrove.hello.pmvc.controller;

import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kordamp.javatrove.hello.pmvc.model.AppModel;
import org.kordamp.javatrove.hello.pmvc.service.HelloService;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Andres Almiray
 */
@RunWith(JukitoRunner.class)
public class AppControllerTest {
    @Inject private AppController controller;
    @Inject private AppModel model;

    @Test
    public void happyPath(HelloService service) {
        // given:
        String input = "Test";
        String output = "Hello Test";
        when(service.sayHello(input)).thenReturn(output);

        // when:
        model.setInput(input);
        controller.sayHello();

        // then:
        assertThat(model.getOutput(), equalTo(output));
        verify(service, only()).sayHello(input);
    }
}
