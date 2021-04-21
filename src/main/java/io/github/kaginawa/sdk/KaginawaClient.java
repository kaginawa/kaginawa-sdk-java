/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kaginawa.sdk;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.JsonbException;
import javax.json.bind.config.PropertyNamingStrategy;
import javax.json.bind.config.PropertyVisibilityStrategy;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * API client for the Kaginawa Server.
 *
 * @since 0.0.1
 */
public class KaginawaClient {
    private static final String NODE_RESOURCE = "/nodes";
    private static final String SERVER_RESOURCE = "/servers";
    private final String endpoint;
    private final String apiKey;
    private final HttpClient httpClient;
    private final HttpResponse.BodyHandler<String> stringHandler;
    private final Jsonb jsonb;

    /**
     * Constructs a {@link KaginawaClient}.
     *
     * @param endpoint endpoint url (http://foo or https://foo)
     * @param apiKey   api key with ADMIN role
     * @throws NullPointerException     if the given parameter is {@code null}
     * @throws IllegalArgumentException if the given parameter is empty or invalid
     */
    public KaginawaClient(String endpoint, String apiKey) {
        this(endpoint, apiKey, HttpClient.newHttpClient());
    }

    /**
     * Constructs a {@link KaginawaClient} with proxy configuration.
     *
     * @param endpoint      endpoint url (http://foo or https://foo)
     * @param apiKey        api key with ADMIN role
     * @param proxySelector IllegalArgumentException if the given parameter is {@code null}, empty or invalid
     * @throws NullPointerException     if the given parameter is {@code null}
     * @throws IllegalArgumentException if the given parameter is empty or invalid
     */
    public KaginawaClient(String endpoint, String apiKey, ProxySelector proxySelector) {
        this(endpoint, apiKey, HttpClient.newBuilder().proxy(proxySelector).build());
    }

    /**
     * Constructs a {@link KaginawaClient} with custom {@link HttpClient} designed for unit testing.
     *
     * @param endpoint   endpoint url (http://foo or https://foo)
     * @param apiKey     api key with ADMIN role
     * @param httpClient an {@link HttpClient} object
     * @throws NullPointerException     if the given parameter is {@code null}
     * @throws IllegalArgumentException if the given parameter is empty or invalid
     */
    KaginawaClient(String endpoint, String apiKey, HttpClient httpClient) {
        Objects.requireNonNull(endpoint, "endpoint is required");
        Objects.requireNonNull(apiKey, "apiKey is required");
        Objects.requireNonNull(httpClient, "httpClient is required");
        if (endpoint.isEmpty()) {
            throw new IllegalArgumentException("endpoint is empty");
        }
        if (!endpoint.startsWith("http://") && !endpoint.startsWith("https://")) {
            throw new IllegalArgumentException("not an http or https endpoint: " + endpoint);
        }
        if (apiKey.isEmpty()) {
            throw new IllegalArgumentException("apiKey is empty");
        }
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.httpClient = httpClient;
        this.stringHandler = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);
        var config = new JsonbConfig()
                .withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES)
                .withPropertyVisibilityStrategy(new PropertyVisibilityStrategy() {
                    @Override
                    public boolean isVisible(Field field) {
                        return true;
                    }

                    @Override
                    public boolean isVisible(Method method) {
                        return false;
                    }
                });
        this.jsonb = JsonbBuilder.create(config);
    }

    /**
     * Returns an endpoint URL.
     *
     * @return endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Returns an API key.
     *
     * @return api key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Returns a {@link HttpResponse.BodyHandler} for decoding {@link String} responses.
     * This method is package-private due to designed for unit testing.
     *
     * @return handler
     */
    HttpResponse.BodyHandler<String> getStringHandler() {
        return stringHandler;
    }

    /**
     * Retrieves a list of Kaginawa nodes filtered by recently reported.
     * <p>
     * List of {@link Report} fields to retrieve:
     * </p>
     * <ul>
     *     <li>{@code id}</li>
     *     <li>{@code customId}</li>
     *     <li>{@code serverTime}</li>
     *     <li>{@code success}</li>
     * </ul>
     *
     * @param thresholdMin freshness threshold from last received time as minutes, 0 for unlimited
     * @return list of newest reports
     * @throws KaginawaServerException if the network or data error occurs
     */
    public List<Report> listAliveNodes(int thresholdMin) throws KaginawaServerException {
        var url = endpoint + NODE_RESOURCE + "?projection=id";
        if (thresholdMin > 0) {
            url += "&minutes=" + thresholdMin;
        }
        var body = getStringResponse(url);
        try {
            return jsonb.fromJson(body, typeOfReportList());
        } catch (JsonbException e) {
            throw new KaginawaServerException("failed to decode nodes response: " + body, e);
        }
    }

    /**
     * Retrieves a list of Kaginawa nodes filtered by given custom ID.
     * <p>
     * This operation retrieves all {@link Report} fields.
     * </p>
     *
     * @param customId custom ID
     * @return list of newest reports
     * @throws NullPointerException     if the given parameter is {@code null}
     * @throws IllegalArgumentException if the given parameter is empty
     * @throws KaginawaServerException  if the network or data error occurs
     */
    public List<Report> listNodesByCustomId(String customId) throws KaginawaServerException {
        Objects.requireNonNull(customId, "customId is required");
        if (customId.isEmpty()) {
            throw new IllegalArgumentException("customId is empty");
        }
        var url = endpoint + NODE_RESOURCE + "?custom-id=" + customId;
        var body = getStringResponse(url);
        try {
            return jsonb.fromJson(body, typeOfReportList());
        } catch (JsonbException e) {
            throw new KaginawaServerException("failed to decode nodes response: " + body, e);
        }
    }

    /**
     * Retrieves a single Kaginawa node.
     *
     * @param id ID, commonly MAC address
     * @return a report
     * @throws NullPointerException     if the given parameter is {@code null}
     * @throws IllegalArgumentException if the given parameter is empty
     * @throws KaginawaServerException  if the network or data error occurs (incl. not found)
     */
    public Report findNodeById(String id) throws KaginawaServerException {
        Objects.requireNonNull(id, "id is required");
        if (id.isEmpty()) {
            throw new IllegalArgumentException("id is empty");
        }
        var url = endpoint + NODE_RESOURCE + "/" + id.toLowerCase();
        var body = getStringResponse(url);
        try {
            return jsonb.fromJson(body, Report.class);
        } catch (JsonbException e) {
            throw new KaginawaServerException("failed to decode nodes response: " + body, e);
        }
    }

    /**
     * Executes a command.
     *
     * @param id         target ID, commonly MAC address
     * @param command    command
     * @param user       login user
     * @param key        (optional) key content of the login user
     * @param password   (optional) password of the login user
     * @param timeoutSec (optional) timeout in seconds, set 0 to default
     * @return result of given command
     * @throws NullPointerException     if the given non-optional parameter is {@code null}
     * @throws IllegalArgumentException if the given non-optional parameter is empty
     * @throws KaginawaServerException  if the network or data error occurs
     */
    public String command(String id, String command, String user, String key, String password, int timeoutSec)
            throws KaginawaServerException {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(command, "command is required");
        Objects.requireNonNull(user, "user is required");
        if (id.isEmpty()) {
            throw new IllegalArgumentException("id is empty");
        }
        if (command.isEmpty()) {
            throw new IllegalArgumentException("command is empty");
        }
        if (user.isEmpty()) {
            throw new IllegalArgumentException("user is empty");
        }
        var param = "command=" + command + "&user=" + user;
        if (key != null && !key.isEmpty()) {
            param += "&key=" + key;
        }
        if (password != null && !password.isEmpty()) {
            param += "&password=" + password;
        }
        if (timeoutSec > 0) {
            param += "&timeout=" + timeoutSec;
        }
        var url = endpoint + NODE_RESOURCE + "/" + id.toLowerCase() + "/command";
        var request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .header("Authorization", "token " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(param))
                .build();
        return retrieveStringResponse(request);
    }

    /**
     * Retrieves past received data of given ID.
     * <p>
     * List of {@link Report} fields to retrieve:
     * </p>
     * <ul>
     *     <li>{@code id}</li>
     *     <li>{@code customId}</li>
     *     <li>{@code serverTime}</li>
     *     <li>{@code success}</li>
     *     <li>{@code sequence}</li>
     *     <li>{@code rttMillis}</li>
     *     <li>{@code uploadKbps}</li>
     *     <li>{@code downloadKbps}</li>
     * </ul>
     *
     * @param id    ID
     * @param begin begin time, 0 for unlimited
     * @param end   end time, 0 for unlimited
     * @return list of past received reports
     * @throws NullPointerException     if the given parameter is {@code null}
     * @throws IllegalArgumentException if the given parameter is empty
     * @throws KaginawaServerException  if the network or data error occurs
     */
    public List<Report> listHistories(String id, long begin, long end) throws KaginawaServerException {
        Objects.requireNonNull(id, "id is required");
        if (id.isEmpty()) {
            throw new IllegalArgumentException("id is empty");
        }
        var url = endpoint + NODE_RESOURCE + "/" + id + "/histories?projection=measurement";
        if (begin > 0) {
            url += "&begin=" + begin;
        }
        if (end > 0) {
            url += "&end=" + end;
        }
        var body = getStringResponse(url);
        try {
            return jsonb.fromJson(body, typeOfReportList());
        } catch (JsonbException e) {
            throw new KaginawaServerException("failed to decode histories response: " + body, e);
        }
    }

    /**
     * Retrieves a SSH server information by given hostname.
     *
     * @param hostname hostname
     * @return SSH server information
     * @throws NullPointerException     if the given parameter is {@code null}
     * @throws IllegalArgumentException if the given parameter is empty
     * @throws KaginawaServerException  if the network or data error occurs
     */
    public SshServer findSshServerByHostname(String hostname) throws KaginawaServerException {
        Objects.requireNonNull(hostname, "hostname is required");
        if (hostname.isEmpty()) {
            throw new IllegalArgumentException("hostname is empty");
        }
        var url = endpoint + SERVER_RESOURCE + "/" + hostname;
        var body = getStringResponse(url);
        try {
            return jsonb.fromJson(body, SshServer.class);
        } catch (JsonbException e) {
            throw new KaginawaServerException("failed to decode servers response: " + body, e);
        }
    }

    private Type typeOfReportList() {
        return new ArrayList<Report>() {
        }.getClass().getGenericSuperclass();
    }

    private String getStringResponse(String url) throws KaginawaServerException {
        var request = HttpRequest.newBuilder(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "token " + apiKey)
                .build();
        return retrieveStringResponse(request);
    }

    private String retrieveStringResponse(HttpRequest request) throws KaginawaServerException {
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, stringHandler);
        } catch (IOException e) {
            throw new KaginawaServerException("failed to connect kaginawa server: " + endpoint, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KaginawaServerException("request interrupted", e);
        }
        if (response.statusCode() != 200) {
            var msg = "HTTP " + response.statusCode() + " " + response.body();
            throw new KaginawaServerException(msg, response.statusCode());
        }
        return response.body();
    }
}
