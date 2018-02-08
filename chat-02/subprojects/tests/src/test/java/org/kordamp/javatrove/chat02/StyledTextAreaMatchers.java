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
package org.kordamp.javatrove.chat02;

import javafx.scene.Node;
import org.fxmisc.richtext.StyledTextArea;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;

import java.util.Objects;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * @author Andres Almiray
 */
public class StyledTextAreaMatchers {
    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static Matcher<Node> hasText(String string) {
        String descriptionText = "has text \"" + string + "\"";
        return typeSafeMatcher(StyledTextArea.class, descriptionText, node -> hasText(node, string));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static Matcher<Node> hasText(Matcher<String> matcher) {
        String descriptionText = "has " + matcher.toString();
        return typeSafeMatcher(StyledTextArea.class, descriptionText, node -> hasText(node, matcher));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static Matcher<Node> containsText(String string) {
        String descriptionText = "contains text \"" + string + "\"";
        return typeSafeMatcher(StyledTextArea.class, descriptionText, node -> containsText(node, string));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static Matcher<Node> containsText(Matcher<String> matcher) {
        String descriptionText = "contains " + matcher.toString();
        return typeSafeMatcher(StyledTextArea.class, descriptionText, node -> containsText(node, matcher));
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static boolean hasText(StyledTextArea text,
                                   String string) {
        return Objects.equals(string, lookupText(text));
    }

    private static boolean hasText(StyledTextArea text,
                                   Matcher<String> matcher) {
        return matcher.matches(lookupText(text));
    }

    private static boolean containsText(StyledTextArea text,
                                        String string) {
        return lookupText(text).contains(string);
    }

    private static boolean containsText(StyledTextArea text,
                                        Matcher<String> matcher) {
        return matcher.matches(lookupText(text));
    }

    private static String lookupText(StyledTextArea text) {
        return text.getText();
    }
}
