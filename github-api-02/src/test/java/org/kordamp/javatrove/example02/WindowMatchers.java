package org.kordamp.javatrove.example02;

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