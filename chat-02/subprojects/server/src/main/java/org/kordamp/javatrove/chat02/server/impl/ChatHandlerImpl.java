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
package org.kordamp.javatrove.chat02.server.impl;

import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.Data;
import org.kordamp.javatrove.chat02.grpc.ChatGrpc;
import org.kordamp.javatrove.chat02.grpc.Login;
import org.kordamp.javatrove.chat02.grpc.Logout;
import org.kordamp.javatrove.chat02.grpc.Message;
import org.kordamp.javatrove.chat02.grpc.Response;
import org.kordamp.javatrove.chat02.server.ChatHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Andres Almiray
 */
public class ChatHandlerImpl extends ChatGrpc.ChatImplBase implements ChatHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ChatHandlerImpl.class);

    @Inject private ScheduledExecutorService scheduledExecutorService;

    private final Map<String, ClientContext> clients = new ConcurrentHashMap<>();
    private final List<ClientContext> failures = new CopyOnWriteArrayList<>();

    @PostConstruct
    private void init() {
        scheduledExecutorService.scheduleAtFixedRate(this::cleanupClients, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    private void cleanupClients() {
        List<ClientContext> clients = new ArrayList<>(failures);
        failures.clear();
        List<Response> responses = new ArrayList<>();
        clients.forEach(c -> responses.add(Response.newBuilder()
            .setType(Response.Type.LOGOUT)
            .setPayload(c.getName())
            .build()));
        dowWithObservers(o -> responses.forEach(o::onNext));
    }

    @Override
    public void login(Login request, StreamObserver<Response> responseObserver) {
        clients.put(request.getId(), new ClientContext(request.getId(), request.getName(), responseObserver));
        Response response = Response.newBuilder()
            .setType(Response.Type.LOGIN)
            .setPayload(request.getName())
            .build();
        dowWithObservers(observer -> observer.onNext(response));
    }

    @Override
    public void logout(Logout request, StreamObserver<Empty> responseObserver) {
        doWithObserver(responseObserver, observer -> {
            Response response = Response.newBuilder()
                .setType(Response.Type.LOGOUT)
                .setPayload(request.getName())
                .build();
            dowWithObservers(o -> o.onNext(response));
            clients.remove(request.getId()).getObserver().onCompleted();
            observer.onNext(Empty.getDefaultInstance());
        });
    }

    @Override
    public void send(Message request, StreamObserver<Empty> responseObserver) {
        doWithObserver(responseObserver, observer -> {
            Response response = Response.newBuilder()
                .setType(Response.Type.MESSAGE)
                .setPayload(request.getMessage())
                .build();
            dowWithObservers(o -> o.onNext(response));
            observer.onNext(Empty.getDefaultInstance());
        });
    }

    private <T> void doWithObserver(@Nonnull StreamObserver<T> observer, @Nonnull Consumer<StreamObserver<T>> consumer) {
        try {
            consumer.accept(observer);
            observer.onCompleted();
        } catch (Exception e) {
            LOG.error("Unexpected error", e);
            observer.onError(e);
        }
    }

    private void dowWithObservers(@Nonnull Consumer<StreamObserver<Response>> consumer) {
        clients.forEach((key, context) -> {
            try {
                consumer.accept(context.getObserver());
            } catch (StatusRuntimeException ignored) {
                failures.add(context);
                clients.remove(key);
            }
        });
    }

    @Data
    private static class ClientContext {
        private final String id;
        private final String name;
        private final StreamObserver<Response> observer;
    }
}