package org.kordamp.javatrove.example01.controller

import org.jdeferred.Promise
import org.jdeferred.impl.DeferredObject
import org.kordamp.javatrove.example01.AppModule
import org.kordamp.javatrove.example01.TestHelper
import org.kordamp.javatrove.example01.model.AppModel
import org.kordamp.javatrove.example01.model.Repository
import org.kordamp.javatrove.example01.service.Github
import org.kordamp.javatrove.example01.util.ApplicationEventBus
import spock.guice.UseModules
import spock.lang.Specification

import javax.inject.Inject

/**
 * @author Andres Almiray
 */
@UseModules(AppModule)
class AppControllerSpec extends Specification {
    private static final String ORGANIZATION = 'foo'

    @Inject private AppController controller
    @Inject private AppModel model
    @Inject private ApplicationEventBus eventBus


    def happyPath() {
        given:
        Collection<Repository> repositories = TestHelper.createSampleRepositories()
        Promise<Collection<Repository>, Throwable, Void> promise = new DeferredObject<Collection<Repository>, Throwable, Void>().resolve(repositories)
        controller.@github = Stub(Github) {
            _(ORGANIZATION) >> promise
        }

        when:
        model.organization = ORGANIZATION
        controller.loadRepositories()

        then:
        model.repositories.size() == 3
        model.repositories == repositories
    }
}
