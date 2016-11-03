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
package org.kordamp.javatrove.example03.model;

import io.reactivex.disposables.Disposable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import static javafx.collections.FXCollections.observableArrayList;
import static org.kordamp.javatrove.example03.model.State.READY;
import static org.kordamp.javatrove.example03.model.State.RUNNING;

/**
 * @author Andres Almiray
 */
public class AppModel {
    private final ObservableList<Repository> repositories = observableArrayList();
    private StringProperty organization;
    private ObjectProperty<State> state;
    private IntegerProperty limit;

    @Getter
    @Setter
    private Disposable disposable;

    public AppModel() {
        stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == RUNNING) {
                repositories.clear();
            }
        });
    }

    public ObservableList<Repository> getRepositories() {
        return repositories;
    }

    public StringProperty organizationProperty() {
        if (organization == null) {
            organization = new SimpleStringProperty(this, "organization");
        }
        return organization;
    }

    public String getOrganization() {
        return organizationProperty().get();
    }

    public void setOrganization(String organization) {
        organizationProperty().set(organization);
    }

    public int getLimit() {
        return limitProperty().get();
    }

    public IntegerProperty limitProperty() {
        if (limit == null) {
            limit = new SimpleIntegerProperty(this, "limit");
        }
        return limit;
    }

    public void setLimit(int limit) {
        limitProperty().set(limit);
    }

    public State getState() {
        return stateProperty().get();
    }

    public ObjectProperty<State> stateProperty() {
        if (state == null) {
            state = new SimpleObjectProperty<>(this, "state", READY);
        }
        return state;
    }

    public void setState(State state) {
        stateProperty().set(state);
    }
}
