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

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Andres Almiray
 */
public class HelloServiceSteps {
    private HelloService service;
    private String result;

    @Given("an instance of the service")
    public void an_instance_of_the_service() {
        service = new DefaultHelloService();
    }

    @When("the sayHello method is invoked without input")
    public void the_sayHello_method_is_invoked_without_input() {
        result = service.sayHello("");
    }

    @When("the sayHello method is invoked with $input")
    public void the_sayHello_method_is_invoked_with(String input) {
        result = service.sayHello(input);
    }

    @Then("the result is equal to $output")
    public void the_result_is_equal_to_$s(String output) {
        assertThat(result, equalTo(output));
    }
}
