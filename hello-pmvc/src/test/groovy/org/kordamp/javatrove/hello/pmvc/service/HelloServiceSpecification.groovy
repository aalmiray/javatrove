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
package org.kordamp.javatrove.hello.pmvc.service

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Andres Almiray
 */
@Unroll
class HelloServiceSpecification extends Specification {
    def "Invoking HelloService.sayHello('#input') yields '#output'"() {
        given: "an instance of HelloService"
        HelloService service = new DefaultHelloService()

        when: "the sayHello method is invoked with '#input'"
        String result = service.sayHello(input)

        then: "the result should be equal to '#output'"
        result == output

        where:
        input  | output
        ''     | 'Howdy stranger!'
        'Test' | 'Hello Test'
    }
}
