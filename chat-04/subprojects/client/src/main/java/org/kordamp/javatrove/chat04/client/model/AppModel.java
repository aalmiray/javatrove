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
package org.kordamp.javatrove.chat04.client.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.javatrove.chat04.client.ChatClient;

import java.util.Optional;

import static javafx.collections.FXCollections.observableArrayList;
import static org.kordamp.javatrove.chat04.ChatUtil.SERVER_PORT;

/**
 * @author Andres Almiray
 */
public class AppModel {
    @Getter
    private final ObservableList<String> messages = observableArrayList();

    private StringProperty server;
    private IntegerProperty port;
    private StringProperty name;
    private StringProperty message;
    private BooleanProperty connected;

    @Setter
    private ChatClient client;

    public Optional<ChatClient> getClient() {
        return Optional.ofNullable(client);
    }

    public StringProperty serverProperty() {
        if (server == null) {
            server = new SimpleStringProperty(this, "server", "localhost");
        }
        return server;
    }

    public String getServer() {
        return serverProperty().get();
    }

    public void setServer(String server) {
        serverProperty().set(server);
    }

    public IntegerProperty portProperty() {
        if (port == null) {
            port = new SimpleIntegerProperty(this, "port", SERVER_PORT);
        }
        return port;
    }

    public int getPort() {
        return portProperty().get();
    }

    public void setPort(int port) {
        portProperty().set(port);
    }

    public StringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty(this, "name");
        }
        return name;
    }

    public String getName() {
        return nameProperty().get();
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public StringProperty messageProperty() {
        if (message == null) {
            message = new SimpleStringProperty(this, "message");
        }
        return message;
    }

    public String getMessage() {
        return messageProperty().get();
    }

    public void setMessage(String message) {
        messageProperty().set(message);
    }

    public BooleanProperty connectedProperty() {
        if (connected == null) {
            connected = new SimpleBooleanProperty(this, "connected");
        }
        return connected;
    }

    public boolean isConnected() {
        return connectedProperty().get();
    }

    public void setConnected(boolean connected) {
        connectedProperty().set(connected);
    }
}
