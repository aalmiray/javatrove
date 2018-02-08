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
package org.kordamp.javatrove.chat04;

import javafx.stage.Window;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;

import static org.testfx.matcher.base.GeneralMatchers.baseMatcher;

/**
 * TestFX matchers for {@code javafx.stage.Window}.
 *
 * @author Andres Almiray
 */
@Unstable(reason = "needs more tests")
public class WindowMatchers {
    private WindowMatchers() {
        // intentionally private and blank
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    /**
     * A matcher checking if a window is currently showing.
     *
     * @return the matcher
     */
    @Factory
    public static Matcher<Window> isShowing() {
        return baseMatcher("Window is showing", window -> isShowing(window));
    }

    /**
     * A matcher checking if a window is currently not showing.
     *
     * @return the matcher
     */
    @Factory
    public static Matcher<Window> isNotShowing() {
        return baseMatcher("Window is not showing", window -> !isShowing(window));
    }

    /**
     * A matcher checking if a window currently has the focus.
     *
     * @return the matcher
     */
    @Factory
    public static Matcher<Window> isFocused() {
        return baseMatcher("Window is focused", window -> isFocused(window));
    }

    /**
     * A matcher checking if a window is currently not focused.
     *
     * @return the matcher
     */
    @Factory
    public static Matcher<Window> isNotFocused() {
        return baseMatcher("Window is not focused", window -> !isFocused(window));
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static boolean isShowing(Window window) {
        return window.isShowing();
    }

    private static boolean isFocused(Window window) {
        return window.isFocused();
    }
}