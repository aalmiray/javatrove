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
package org.kordamp.javatrove.example07.server.impl;

import com.esotericsoftware.kryonet.Server;
import lombok.Data;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kordamp.javatrove.example07.Command;
import org.kordamp.javatrove.example07.server.ServerCommandDispatcher;
import org.kordamp.javatrove.example07.server.ServerCommandHandler;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Inject;

import static com.google.inject.name.Names.named;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Andres Almiray
 */
@RunWith(JukitoRunner.class)
@UseModules({ServerCommandDispatcherImplTest.TestModule.class})
public class ServerCommandDispatcherImplTest {
    @Inject private ServerCommandDispatcher dispatcher;

    @Test
    public void dispatcherHandlesACommand() {
        // given:
        ACommand acommand = new ACommand();
        BCommand bcommand = new BCommand();

        // when:
        dispatcher.dispatch(null, null, acommand);

        // then:
        assertThat(acommand.isInvoked(), equalTo(true));

        // when:
        dispatcher.dispatch(null, null, bcommand);

        // then:
        assertThat(bcommand.isInvoked(), equalTo(false));
    }

    public static class ACommandHandler implements ServerCommandHandler {
        @Override
        public <C extends Command> boolean supports(C command) {
            return command instanceof ACommand;
        }

        @Override
        public <C extends Command> void handle(Server server, NamedConnection connection, C command) {
            ((ACommand) command).setInvoked(true);
        }
    }

    @Data
    public static class ACommand implements Command {
        private boolean invoked;
    }

    @Data
    public static class BCommand implements Command {
        private boolean invoked;
    }

    public static class TestModule extends ExtAnnotationsModule {
        public TestModule() {
            super(TestModule.class.getPackage().getName());
        }

        @Override
        protected void configure() {
            super.configure();
            bind(ServerCommandDispatcher.class)
                .to(ServerCommandDispatcherImpl.class);
            bind(ServerCommandHandler.class)
                .annotatedWith(named("A"))
                .to(ACommandHandler.class);
        }
    }
}
