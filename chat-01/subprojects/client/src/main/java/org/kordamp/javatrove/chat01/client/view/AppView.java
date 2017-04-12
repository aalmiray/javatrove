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
package org.kordamp.javatrove.chat01.client.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.kordamp.javatrove.chat01.client.controller.AppController;
import org.kordamp.javatrove.chat01.client.model.AppModel;
import org.reactfx.EventStreams;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

import static java.util.Objects.requireNonNull;
import static org.kordamp.javatrove.chat01.ChatUtil.NAME_SEPARATOR;
import static org.kordamp.javatrove.chat01.ChatUtil.SERVER_PORT;

/**
 * @author Andres Almiray
 */
public class AppView {
    @Inject private AppController controller;
    @Inject private AppModel model;

    @FXML private TextField server;
    @FXML private TextField port;
    @FXML private TextField name;
    @FXML private TextField message;
    @FXML private Button loginButton;
    @FXML private Button logoutButton;
    @FXML private Button sendButton;
    @FXML private VBox placeholder;

    private StyleClassedTextArea content;

    public Scene createScene() {
        String basename = getClass().getPackage().getName().replace('.', '/') + "/app";
        URL fxml = getClass().getClassLoader().getResource(basename + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxml);
        fxmlLoader.setControllerFactory(param -> AppView.this);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        content = new StyleClassedTextArea();
        content.setId("messages");
        content.setEditable(false);

        ObservableList<String> messages = createJavaFXThreadProxyList(model.getMessages());
        EventStreams.changesOf(messages).subscribe(this::handleMessages);

        VirtualizedScrollPane<StyleClassedTextArea> scrollPane = new VirtualizedScrollPane<>(content);
        placeholder.getChildren().add(scrollPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        server.textProperty().bindBidirectional(model.serverProperty());
        Bindings.bindBidirectional(port.textProperty(), model.portProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number number) {
                return number.toString();
            }

            @Override
            public Number fromString(String string) {
                try {
                    int i = Integer.parseInt(string);
                    if (i < 0 || i > 65535) {
                        throw new IllegalStateException();
                    }
                    return i;
                } catch (Exception e) {
                    port.setText("" + SERVER_PORT);
                    return SERVER_PORT;
                }
            }
        });

        server.disableProperty().bind(model.connectedProperty());
        port.disableProperty().bind(model.connectedProperty());
        name.disableProperty().bind(model.connectedProperty());
        message.disableProperty().bind(Bindings.not(model.connectedProperty()));

        name.textProperty().bindBidirectional(model.nameProperty());
        message.textProperty().bindBidirectional(model.messageProperty());

        loginButton.disableProperty().bind(EasyBind.combine(model.serverProperty(), model.nameProperty(), model.connectedProperty(),
            (server1, name1, connected) -> connected || isBlank(server1) || isBlank(name1)));

        logoutButton.disableProperty().bind(Bindings.not(model.connectedProperty()));

        sendButton.disableProperty().bind(EasyBind.combine(model.connectedProperty(), model.messageProperty(),
            (connected, message1) -> !connected || isBlank(message1)));

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(basename + ".css", "bootstrapfx.css");
        return scene;
    }

    private void handleMessages(ListChangeListener.Change<? extends String> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(this::handleMessage);
            }
        }
    }

    private void handleMessage(String message) {
        int from = content.getText().length();
        int semicolon = message.indexOf(NAME_SEPARATOR);
        content.appendText(message + "\n");
        if (semicolon != -1) {
            content.setStyleClass(from, from + semicolon + 1, "content--name");
        }
    }

    public void login(ActionEvent ignored) {
        controller.login();
    }

    public void logout(ActionEvent ignored) {
        controller.logout();
    }

    public void send(ActionEvent ignored) {
        controller.send();
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    private static <E> ObservableList<E> createJavaFXThreadProxyList(ObservableList<E> source) {
        requireNonNull(source, "Argument 'source' must not be null");
        return new JavaFXThreadProxyObservableList<>(source);
    }

    private static class JavaFXThreadProxyObservableList<E> extends TransformationList<E, E> {
        protected JavaFXThreadProxyObservableList(ObservableList<E> source) {
            super(source);
        }

        @Override
        protected void sourceChanged(ListChangeListener.Change<? extends E> c) {
            if (Platform.isFxApplicationThread()) {
                fireChange(c);
            } else {
                Platform.runLater(() -> fireChange(c));
            }
        }

        @Override
        public int getSourceIndex(int index) {
            return index;
        }

        @Override
        public E get(int index) {
            return getSource().get(index);
        }

        @Override
        public int size() {
            return getSource().size();
        }
    }
}
