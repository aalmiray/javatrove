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

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kordamp.javatrove.chat02.client.AppModule;
import org.kordamp.javatrove.chat02.client.ChatClient;
import org.kordamp.javatrove.chat02.client.view.AppView;
import org.kordamp.javatrove.chat02.server.ChatServer;

import static org.kordamp.javatrove.chat02.StyledTextAreaMatchers.containsText;
import static org.kordamp.javatrove.chat02.WindowMatchers.isShowing;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;

/**
 * @author Andres Almiray
 */
@RunWith(FunctionalTestRunner.class)
public class FullTest {
    @ClassRule
    public static FunctionalTestRule testfx = new FunctionalTestRule(context -> {
        AppView view = context.getClientInjector().getInstance(AppView.class);
        context.getStage().setScene(view.createScene());
        context.getStage().sizeToScene();
        context.getStage().setResizable(false);
        context.getStage().show();
    });

    private ChatServer chatServer;
    private ChatClient client;

    @Test
    public void _01_verify_initial_state() {
        // expect:
        verifyThat("#loginButton", isDisabled());
        verifyThat("#logoutButton", isDisabled());
        verifyThat("#sendButton", isDisabled());
    }

    @Test
    public void _02_login_without_server_results_in_error() {
        //when:
        testfx.clickOn("#name").write("Alice");

        // then:
        verifyThat("#loginButton", isEnabled());

        // when:
        testfx.clickOn("#loginButton");

        // then:
        pause(1000);
        testfx.waitUntil(testfx.window("Error"), isShowing(), 5);
        testfx.clickOn("OK");
    }

    @Test
    public void _03_login_with_server_running() throws Exception {
        // given:
        chatServer = testfx.getServerInjector().getInstance(ChatServer.class);
        chatServer.start();

        // when:
        testfx.clickOn("#loginButton");

        // then:
        verifyThat("#loginButton", isDisabled());
        verifyThat("#logoutButton", isEnabled());
        testfx.waitFor("#messages", containsText("Alice connected."), 200);
    }

    @Test
    public void _04_send_a_message() {
        // when:
        testfx.clickOn("#message").write("hello");
        testfx.clickOn("#sendButton");

        // then:
        testfx.waitFor("#messages", containsText("Alice> hello"), 200);
    }

    @Test
    public void _05_another_client_connects() {
        // given:
        Injector injector = Guice.createInjector(new AppModule());
        client = injector.getInstance(ChatClient.class);

        // when:
        client.login("localhost", ChatUtil.SERVER_PORT, "Bob");

        // then:
        testfx.waitFor("#messages", containsText("Alice> hello"), 200);

        // when:
        client.send("Bob", "howdy!");

        // then:
        testfx.waitFor("#messages", containsText("Bob> howdy!"), 200);

        // when:
        client.logout("Bob");

        // then:
        testfx.waitFor("#messages", containsText("Bob disconnected"), 200);
    }

    private static void pause(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
