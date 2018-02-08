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
package org.kordamp.javatrove.hello.pmvc.service;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Andres Almiray
 */
public class HelloServiceScenarioTest extends SimpleScenarioTest<HelloServiceScenarioTest.TestSteps> {
    @Test
    public void default_greeting_is_returned_when_blank_input_is_given() {
        given().an_instance_of_the_service();

        when().the_sayHello_method_is_invoked_with_$s("");

        then().the_result_is_equal_to_$s("Howdy stranger!");
    }

    @Test
    public void greeting_is_returned_when_input_is_given() {
        given().an_instance_of_the_service();

        when().the_sayHello_method_is_invoked_with_$s("Test");

        then().the_result_is_equal_to_$s("Hello Test");
    }

    public static class TestSteps extends Stage<TestSteps> {
        private HelloService service;
        private String result;

        public void an_instance_of_the_service() {
            service = new DefaultHelloService();
        }

        public TestSteps the_sayHello_method_is_invoked_with_$s(String input) {
            result = service.sayHello(input);
            return this;
        }

        public void the_result_is_equal_to_$s(String output) {
            assertThat(result, equalTo(output));
        }
    }
}
