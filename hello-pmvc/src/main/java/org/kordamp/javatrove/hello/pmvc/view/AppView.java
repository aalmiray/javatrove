/*
 * Copyright 2016-2020 Andres Almiray
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
package org.kordamp.javatrove.hello.pmvc.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.kordamp.javatrove.hello.pmvc.controller.AppController;
import org.kordamp.javatrove.hello.pmvc.model.AppModel;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

/**
 * @author Andres Almiray
 */
public class AppView {
    @Inject private AppController controller;
    @Inject private AppModel model;

    @FXML private TextField input;
    @FXML private Label output;

    public Scene createScene() {
        String basename = getClass().getPackage().getName().replace('.', '/') + "/app";
        URL fxml = getClass().getClassLoader().getResource(basename + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxml);
        fxmlLoader.setControllerFactory(param -> AppView.this);

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        model.inputProperty().bind(input.textProperty());
        model.outputProperty().addListener((v, o, n) -> Platform.runLater(() -> output.setText(n)));

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(basename + ".css", "org/kordamp/bootstrapfx/org/kordamp/bootstrapfx/bootstrapfx.css");
        return scene;
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public void sayHello(ActionEvent ignored) {
        controller.sayHello();
    }
}
