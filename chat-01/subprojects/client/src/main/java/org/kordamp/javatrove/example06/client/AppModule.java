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
package org.kordamp.javatrove.example06.client;

import com.esotericsoftware.kryonet.Client;
import org.jdeferred.DeferredManager;
import org.kordamp.javatrove.example06.client.controller.AppController;
import org.kordamp.javatrove.example06.client.impl.ClientCommandDispatcherImpl;
import org.kordamp.javatrove.example06.client.impl.ClientDisconnectCommandHandler;
import org.kordamp.javatrove.example06.client.impl.ClientKryoListener;
import org.kordamp.javatrove.example06.client.impl.ClientKryoListenerImpl;
import org.kordamp.javatrove.example06.client.impl.ClientLoginCommandHandler;
import org.kordamp.javatrove.example06.client.impl.ClientLogoutCommandHandler;
import org.kordamp.javatrove.example06.client.impl.ClientMessageCommandHandler;
import org.kordamp.javatrove.example06.client.impl.ClientProvider;
import org.kordamp.javatrove.example06.client.impl.DeferredManagerProvider;
import org.kordamp.javatrove.example06.client.model.AppModel;
import org.kordamp.javatrove.example06.client.util.ApplicationEventBus;
import org.kordamp.javatrove.example06.client.util.ApplicationEventHandler;
import org.kordamp.javatrove.example06.client.view.AppView;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.inject.name.Names.named;

/**
 * @author Andres Almiray
 */
public class AppModule extends ExtAnnotationsModule {
    public AppModule() {
        super(AppModule.class.getPackage().getName());
    }

    @Override
    protected final void configure() {
        super.configure();
        bindExecutorService();
        bindClient();
        bindClientListener();
        bindCommandDispatcher();
        bindLoginCommandHandler();
        bindLogoutCommandHandler();
        bindMessageCommandHandler();
        bindDisconnectCommandHandler();
        bindDeferredManager();
        bindApplicationEventBus();
        bindApplicationEventHandler();
        bindAppController();
        bindAppModel();
        bindAppView();
    }

    protected void bindExecutorService() {
        bind(ExecutorService.class)
            .toInstance(Executors.newFixedThreadPool(1));
    }

    protected void bindClient() {
        bind(Client.class)
            .toProvider(ClientProvider.class);
    }

    protected void bindClientListener() {
        bind(ClientKryoListener.class)
            .to(ClientKryoListenerImpl.class)
            .in(Singleton.class);
    }

    protected void bindCommandDispatcher() {
        bind(ClientCommandDispatcher.class)
            .to(ClientCommandDispatcherImpl.class)
            .in(Singleton.class);
    }

    protected void bindLoginCommandHandler() {
        bind(ClientCommandHandler.class)
            .annotatedWith(named(ClientLoginCommandHandler.NAME))
            .to(ClientLoginCommandHandler.class)
            .in(Singleton.class);
    }

    protected void bindLogoutCommandHandler() {
        bind(ClientCommandHandler.class)
            .annotatedWith(named(ClientLogoutCommandHandler.NAME))
            .to(ClientLogoutCommandHandler.class)
            .in(Singleton.class);
    }

    protected void bindMessageCommandHandler() {
        bind(ClientCommandHandler.class)
            .annotatedWith(named(ClientMessageCommandHandler.NAME))
            .to(ClientMessageCommandHandler.class)
            .in(Singleton.class);
    }

    protected void bindDisconnectCommandHandler() {
        bind(ClientCommandHandler.class)
            .annotatedWith(named(ClientDisconnectCommandHandler.NAME))
            .to(ClientDisconnectCommandHandler.class)
            .in(Singleton.class);
    }

    protected void bindDeferredManager() {
        bind(DeferredManager.class)
            .toProvider(DeferredManagerProvider.class)
            .in(Singleton.class);
    }

    protected void bindApplicationEventBus() {
        bind(ApplicationEventBus.class)
            .in(Singleton.class);
    }

    protected void bindApplicationEventHandler() {
        bind(ApplicationEventHandler.class)
            .asEagerSingleton();
    }

    protected void bindAppController() {
        bind(AppController.class).in(Singleton.class);
    }

    protected void bindAppModel() {
        bind(AppModel.class).in(Singleton.class);
    }

    protected void bindAppView() {
        bind(AppView.class).in(Singleton.class);
    }
}
