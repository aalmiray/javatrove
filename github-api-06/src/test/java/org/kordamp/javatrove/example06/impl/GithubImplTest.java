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
package org.kordamp.javatrove.example06.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kordamp.javatrove.example06.AppModule;
import org.kordamp.javatrove.example06.model.Repository;
import org.kordamp.javatrove.example06.service.Github;
import org.kordamp.javatrove.example06.service.GithubAPI;

import javax.inject.Inject;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.google.inject.name.Names.named;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.kordamp.javatrove.example06.TestHelper.createSampleRepositories;
import static org.kordamp.javatrove.example06.TestHelper.repositoriesAsJSON;

/**
 * @author Andres Almiray
 */
@RunWith(JukitoRunner.class)
@UseModules({GithubImplTest.AppTestModule.class})
public class GithubImplTest {
    private static final String ORGANIZATION = "foo";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8080);

    @Inject private ObjectMapper objectMapper;
    @Inject private Github github;


    @Test
    public void happyPath() throws Exception {
        // given:
        Collection<Repository> repositories = createSampleRepositories();
        stubFor(get(urlEqualTo("/orgs/" + ORGANIZATION + "/repos"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/json")
                .withBody(repositoriesAsJSON(repositories, objectMapper))));

        // when:
        CompletableFuture<Collection<Repository>> promise = github.repositories(ORGANIZATION);
        await().timeout(2, SECONDS).until(promise::isDone, equalTo(true));

        // then:
        promise.thenAccept(result -> assertThat(result, equalTo(repositories)));
        verify(getRequestedFor(urlEqualTo("/orgs/" + ORGANIZATION + "/repos")));
    }

    @Test
    public void failurePath() {
        // given:
        stubFor(get(urlEqualTo("/orgs/" + ORGANIZATION + "/repos"))
            .willReturn(aResponse()
                .withStatus(500)
                .withStatusMessage("Internal Error")));

        // when:
        CompletableFuture<Collection<Repository>> promise = github.repositories(ORGANIZATION);
        await().timeout(2, SECONDS).until(promise::isCompletedExceptionally, equalTo(true));

        // then:
        promise.exceptionally(throwable -> {
            assertThat(throwable.getMessage(), equalTo("Internal Error"));
            return null;
        });
        verify(getRequestedFor(urlEqualTo("/orgs/" + ORGANIZATION + "/repos")));
    }

    public static class AppTestModule extends AppModule {
        @Override
        protected void bindGithubApiUrl() {
            bindConstant()
                .annotatedWith(named(GithubAPI.GITHUB_API_URL_KEY))
                .to("http://localhost:8080");
        }
    }
}
