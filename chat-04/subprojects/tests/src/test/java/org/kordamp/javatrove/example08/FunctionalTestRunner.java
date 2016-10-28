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
package org.kordamp.javatrove.example08;

import org.junit.internal.runners.statements.Fail;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static org.junit.runner.Description.createTestDescription;

/**
 * @author Andres Almiray
 */
public class FunctionalTestRunner extends BlockJUnit4ClassRunner {
    private FunctionalTestRule failureCollector;

    private static class FailureListener extends RunListener {
        private final FunctionalTestRule failureCollector;

        private FailureListener(FunctionalTestRule failureCollector) {
            this.failureCollector = failureCollector;
        }

        @Override
        public void testFailure(Failure failure) throws Exception {
            failureCollector.setFailures(true);
        }
    }

    public FunctionalTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> methods = new ArrayList<>(super.computeTestMethods());
        Collections.sort(methods, (a, b) -> a.getName().compareTo(b.getName()));
        return unmodifiableList(methods);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        try {
            resolveFailureCollectorRule(method);
            notifier.addFirstListener(new FailureListener(failureCollector));
        } catch (Exception e) {
            notifier.fireTestFailure(new Failure(createTestDescription(method.getDeclaringClass(), method.getName()), e));
        }
        super.runChild(method, notifier);
    }

    @Override
    protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
        try {
            resolveFailureCollectorRule(method);
        } catch (Exception e) {
            return new Fail(e);
        }
        return super.withBefores(method, target, statement);
    }

    @Override
    protected boolean isIgnored(FrameworkMethod child) {
        if (super.isIgnored(child)) {
            return true;
        }

        try {
            resolveFailureCollectorRule(child);
            return failureCollector.hasFailures();
        } catch (Exception e) {
            return true;
        }
    }

    private void resolveFailureCollectorRule(FrameworkMethod child) throws NoSuchFieldException, IllegalAccessException {
        if (failureCollector == null) {
            for (Field field : child.getDeclaringClass().getFields()) {
                if (FunctionalTestRule.class.isAssignableFrom(field.getType())) {
                    failureCollector = (FunctionalTestRule) field.get(null);
                    return;
                }
            }
            throw new IllegalStateException("Class " + child.getDeclaringClass().getName() + " does not define a field of type " + FunctionalTestRule.class.getName());
        }
    }
}
