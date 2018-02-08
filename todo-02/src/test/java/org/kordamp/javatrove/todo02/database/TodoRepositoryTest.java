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
package org.kordamp.javatrove.todo02.database;

import com.google.inject.Singleton;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kordamp.javatrove.todo02.AppModule;
import org.kordamp.javatrove.todo02.impl.DefaultDataSourceBootstrap;
import org.kordamp.javatrove.todo02.model.Todo;

import javax.inject.Inject;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Andres Almiray
 */
@RunWith(JukitoRunner.class)
@UseModules({TodoRepositoryTest.AppTestModule.class})
public class TodoRepositoryTest {
    @Inject private DataSourceBootstrap dataSourceBootstrap;
    @Inject private TodoRepository todoRepository;

    @Test
    public void repositoryOperations() {
        // given:
        dataSourceBootstrap.init();

        // expect:
        assertThat(todoRepository.findAll(), empty());
        assertThat(todoRepository.findById(1L), nullValue());

        // given:
        Todo todo1 = Todo.builder().description("todo1").build();
        Todo todo2 = Todo.builder().description("todo2").build();

        // when:
        todoRepository.save(todo1);
        Collection<Todo> all = todoRepository.findAll();
        // then:
        assertThat(todo1.getId(), notNullValue());
        assertThat(all, contains(todo1));

        // when:
        todoRepository.save(todo2);
        all = todoRepository.findAll();
        // then:
        assertThat(todo2.getId(), notNullValue());
        assertThat(all, contains(todo1, todo2));
        // when:
        todoRepository.delete(todo2);
        all = todoRepository.findAll();
        // then:
        assertThat(all, contains(todo1));

        // when:
        todo1.setDone(true);
        todoRepository.save(todo1);
        Todo other = todoRepository.findById(todo1.getId());
        assertThat(other.isDone(), equalTo(true));

        // given:
        assertThat(todoRepository.findAll().size(), equalTo(1));
        // when:
        todoRepository.clear();
        // then:
        assertThat(todoRepository.findAll().size(), equalTo(0));
    }

    public static class AppTestModule extends AppModule {
        @Override
        protected void bindDataSourceBootstrap() {
            bind(DataSourceBootstrap.class)
                .to(TestDataSourceBootstrap.class)
                .in(Singleton.class);
        }
    }

    private static class TestDataSourceBootstrap extends DefaultDataSourceBootstrap {
        @Override
        protected void initData() {
            // empty
        }
    }
}
