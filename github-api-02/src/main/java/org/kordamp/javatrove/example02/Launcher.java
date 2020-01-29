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
package org.kordamp.javatrove.example02;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import org.kordamp.javatrove.example02.view.AppView;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

/**
 * @author Andres Almiray
 */
public class Launcher extends Application {
    @Inject private ExecutorService executorService;
    @Inject private AppView view;

    @Override
    public void init() throws Exception {
        Injector injector = Guice.createInjector(new AppModule());
        injector.injectMembers(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(view.createScene());
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        executorService.shutdownNow();
    }

    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
        launch(Launcher.class, args);
    }
}
