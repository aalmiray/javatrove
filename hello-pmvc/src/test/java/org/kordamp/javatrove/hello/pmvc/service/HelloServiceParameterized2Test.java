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
package org.kordamp.javatrove.hello.pmvc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Andres Almiray
 */
@RunWith(Parameterized.class)
public class HelloServiceParameterized2Test {
    @Parameterized.Parameters(name = "Invoking HelloService.sayHello(''{0}'') yields ''{1}''")
    public static Collection<Object[]> data() {
        return Arrays.asList(
            new String[]{"", "Howdy stranger!"},
            new String[]{"Test", "Hello Test"}
        );
    }

    @Parameterized.Parameter(0)
    public String input;

    @Parameterized.Parameter(1)
    public String output;

    @Test
    public void sayHello() {
        // given:
        HelloService service = new DefaultHelloService();
        // expect:
        assertThat(service.sayHello(input), equalTo(output));
    }
}
