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
