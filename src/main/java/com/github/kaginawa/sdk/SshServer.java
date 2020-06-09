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
package com.github.kaginawa.sdk;

import java.util.Objects;

/**
 * SSH server's connection attributes.
 * <p>
 * All fields are marked as protected (not private) for reflective access.
 * </p>
 *
 * @since 0.0.1
 */
public class SshServer {
    /**
     * @see Builder#newBuilder()
     */
    protected SshServer() {
    }

    protected String host = "";

    protected int port;

    protected String user = "";

    protected String key = "";

    protected String password = "";

    /**
     * Returns the host name.
     *
     * @return host name
     */
    public String getHost() {
        return host;
    }

    /**
     * Returns the port number.
     *
     * @return port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the user name.
     *
     * @return user name
     */
    public String getUser() {
        return user;
    }

    /**
     * Returns the key.
     *
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Builds a {@link Report}.
     */
    public static class Builder {
        private final SshServer o = new SshServer();

        /**
         * @see #newBuilder()
         */
        private Builder() {
        }

        /**
         * Creates a new {@link Builder} for building a {@link SshServer}.
         *
         * @return a {@link Builder}
         */
        public static Builder newBuilder() {
            return new Builder();
        }

        /**
         * Sets a host name.
         *
         * @param host host name
         * @return this builder
         * @throws NullPointerException     if the host is {@code null}
         * @throws IllegalArgumentException if the host is empty
         */
        public Builder host(String host) {
            Objects.requireNonNull(host, "host is required");
            if (host.isEmpty()) {
                throw new IllegalArgumentException("host is empty");
            }
            o.host = host;
            return this;
        }

        /**
         * Sets a port number.
         *
         * @param port TCP port number
         * @return this builder
         * @throws IllegalArgumentException if the port is out of range
         */
        public Builder port(int port) {
            if (port < 0 || port > 65535) {
                throw new IllegalArgumentException("out of range: " + port);
            }
            o.port = port;
            return this;
        }

        /**
         * Sets a login user name.
         *
         * @param user user name
         * @return this builder
         * @throws NullPointerException     if the user is {@code null}
         * @throws IllegalArgumentException if the user is empty
         */
        public Builder user(String user) {
            Objects.requireNonNull(user, "user is required");
            if (user.isEmpty()) {
                throw new IllegalArgumentException("user is empty");
            }
            o.user = user;
            return this;
        }

        /**
         * Sets a key.
         *
         * @param key key
         * @return this builder
         * @throws NullPointerException     if the key is {@code null}
         * @throws IllegalArgumentException if the key is empty
         */
        public Builder key(String key) {
            Objects.requireNonNull(key, "key is required");
            if (key.isEmpty()) {
                throw new IllegalArgumentException("key is empty");
            }
            o.key = key;
            return this;
        }

        /**
         * Sets a password.
         *
         * @param password password
         * @return this builder
         * @throws NullPointerException     if the password is {@code null}
         * @throws IllegalArgumentException if the password is empty
         */
        public Builder password(String password) {
            Objects.requireNonNull(password, "password is required");
            if (password.isEmpty()) {
                throw new IllegalArgumentException("password is empty");
            }
            o.password = password;
            return this;
        }

        /**
         * Returns a new {@link SshServer} built from the current state of this builder.
         *
         * @return a new {@link SshServer}
         * @throws IllegalStateException if the required parameter(s) are not set
         */
        public SshServer build() {
            if (o.host == null || o.host.isEmpty()) {
                throw new IllegalStateException("host is not set yet");
            }
            return o;
        }
    }
}
