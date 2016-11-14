/*
 * Copyright 2016 Andres Almiray
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
package org.kordamp.javatrove.chat03;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.stage.Stage;
import lombok.Data;
import lombok.Getter;
import org.hamcrest.Matcher;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.kordamp.javatrove.chat03.client.AppModule;
import org.kordamp.javatrove.chat03.server.ServerModule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationAdapter;
import org.testfx.framework.junit.ApplicationFixture;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Andres Almiray
 */
public class FunctionalTestRule extends FxRobot implements TestRule, ApplicationFixture {
    @Getter
    private Injector clientInjector;
    @Getter
    private Injector serverInjector;
    private boolean failures;

    private final Consumer<ClientContext> start;

    @Data
    public static class ClientContext {
        private final Injector clientInjector;
        private final Stage stage;
    }

    public FunctionalTestRule(Consumer<ClientContext> start) {
        this.start = start;
    }

    public boolean hasFailures() {
        return failures;
    }

    public void setFailures(boolean failures) {
        this.failures = failures;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                setup();
                try {
                    base.evaluate();
                } finally {
                    cleanup();
                }
            }
        };
    }

    private void setup() throws Exception {
        clientInjector = Guice.createInjector(new AppModule());
        serverInjector = Guice.createInjector(new ServerModule());

        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(() -> new ApplicationAdapter(this));
    }

    private void cleanup() throws Exception {
        FxToolkit.cleanupApplication(new ApplicationAdapter(this));
    }

    @Override
    public void init() throws Exception {
        // do nothing
    }

    @Override
    public void start(Stage stage) throws Exception {
        start.accept(new ClientContext(clientInjector, stage));
    }

    @Override
    public void stop() throws Exception {
        // do nothing
    }

    public <T> void waitFor(final String query, final Matcher<T> condition,
                            int timeoutInSeconds) {
        awaitCondition(() -> condition.matches(lookup(query).query()), timeoutInSeconds);
    }

    public <T> void waitUntil(final String query, final String message,
                              final Matcher<T> condition,
                              int timeoutInSeconds) {
        awaitCondition(message, () -> condition.matches(lookup(query).query()), timeoutInSeconds);
    }

    public <T> void waitUntil(final T target, final Matcher<T> condition,
                              int timeoutInSeconds) {
        awaitCondition(() -> condition.matches(target), timeoutInSeconds);
    }

    public <T> void waitUntil(final T target, final String message,
                              final Matcher<T> condition,
                              int timeoutInSeconds) {
        awaitCondition(message, () -> condition.matches(target), timeoutInSeconds);
    }

    private void awaitCondition(Callable<Boolean> condition, int timeoutInSeconds) {
        awaitCondition(null, condition, timeoutInSeconds);
    }

    private void awaitCondition(String message, Callable<Boolean> condition, int timeoutInSeconds) {
        try {
            WaitForAsyncUtils.waitFor(timeoutInSeconds, TimeUnit.SECONDS, condition);
        } catch (Exception exception) {
            throw new RuntimeException(message, exception);
        }
    }
}
