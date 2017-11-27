package org.kordamp.javatrove.hello.pmvc.controller

import org.kordamp.javatrove.hello.pmvc.AppModule
import org.kordamp.javatrove.hello.pmvc.model.AppModel
import org.kordamp.javatrove.hello.pmvc.service.HelloService
import spock.guice.UseModules
import spock.lang.Specification
import spock.mock.DetachedMockFactory
import spock.mock.MockFactory

import javax.inject.Inject

/**
 * @author Andres Almiray
 */
@UseModules(TestModule)
class AppControllerSpec extends Specification {

    @Inject private AppController controller
    @Inject private AppModel model
    @Inject private HelloService service

    def happyPath() {
        given: "The HelloService mock is configured to return 'Hello \$input'"
        service.sayHello('Test') >> 'Hello Test'

        when: "The input is set to 'Test'"
        model.input = 'Test'

        and: "The sayHello action is invoked on the controller"
        controller.sayHello()

        then: "The output should be 'Hello Test'"
        model.output == ' Hello Test'
    }

    static class TestModule extends AppModule {
        private final MockFactory mockFactory = new DetachedMockFactory()

        @Override
        protected void bindHelloService() {
            bind(HelloService).toInstance(mockFactory.Mock(HelloService))
        }
    }
}
