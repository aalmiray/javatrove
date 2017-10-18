package org.kordamp.javatrove.hello.pmvc.service

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Andres Almiray
 */
@Unroll
class HelloServiceSpecification extends Specification {
    def "Invoking HelloService.sayHello('#input') yields '#output'"() {
        given:
        HelloService service = new DefaultHelloService()

        expect:
        output == service.sayHello(input)

        where:
        input  | output
        ''     | 'Howdy stranger!'
        'Test' | 'Hello Test'
    }
}
