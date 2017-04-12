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
package org.kordamp.javatrove.example03.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

/**
 * @author Andres Almiray
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository implements Comparable<Repository> {
    private String name;
    private String description;
    @Setter(onMethod = @__({@JsonProperty("full_name")}))
    private String fullName;
    @Setter(onMethod = @__({@JsonProperty("html_url")}))
    private String htmlUrl;

    @Builder
    public static Repository build(String name, String fullName, String description, String htmlUrl) {
        Repository repository = new Repository();
        repository.name = name;
        repository.fullName = fullName;
        repository.description = description;
        repository.htmlUrl = htmlUrl;
        return repository;
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public int compareTo(Repository other) {
        if (other == null) { return 1; }
        return name.compareTo(other.name);
    }
}