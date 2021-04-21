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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SshServerTests {
    @Test
    public void testBuilder() {
        var builder = SshServer.Builder.newBuilder();
        assertNotNull(builder.host("example.com"));
        assertNotNull(builder.port(10001));
        assertNotNull(builder.user("root"));
        assertNotNull(builder.key("line1\nline2\nline3"));
        assertNotNull(builder.password("password"));
        var sshServer = builder.build();
        assertNotNull(sshServer);
        assertEquals("example.com", sshServer.getHost());
        assertEquals(10001, sshServer.getPort());
        assertEquals("root", sshServer.getUser());
        assertEquals("line1\nline2\nline3", sshServer.getKey());
        assertEquals("password", sshServer.getPassword());
    }

    @Test
    public void testBuilder_ISE() {
        var builder = SshServer.Builder.newBuilder();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void testBuilder_NPE() {
        var builder = SshServer.Builder.newBuilder();
        assertThrows(NullPointerException.class, () -> builder.host(null));
        assertThrows(NullPointerException.class, () -> builder.user(null));
        assertThrows(NullPointerException.class, () -> builder.key(null));
        assertThrows(NullPointerException.class, () -> builder.password(null));
    }

    @Test
    public void testBuilder_IAE() {
        var builder = SshServer.Builder.newBuilder();
        assertThrows(IllegalArgumentException.class, () -> builder.host(""));
        assertThrows(IllegalArgumentException.class, () -> builder.port(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.port(65536));
        assertThrows(IllegalArgumentException.class, () -> builder.user(""));
        assertThrows(IllegalArgumentException.class, () -> builder.key(""));
        assertThrows(IllegalArgumentException.class, () -> builder.password(""));
    }
}
