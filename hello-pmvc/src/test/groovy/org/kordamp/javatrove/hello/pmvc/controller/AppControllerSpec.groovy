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
        1 * service.sayHello('Test') >> 'Hello Test'

        when: "The input is set to 'Test'"
        model.input = 'Test'

        and: "The sayHello action is invoked on the controller"
        controller.sayHello()

        then: "The output should be 'Hello Test'"
        model.output == 'Hello Test'
    }

    static class TestModule extends AppModule {
        private final MockFactory mockFactory = new DetachedMockFactory()

        @Override
        protected void bindHelloService() {
            bind(HelloService).toInstance(mockFactory.Mock(HelloService))
        }
    }
}
