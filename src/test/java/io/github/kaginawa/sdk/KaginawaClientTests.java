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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KaginawaClientTests {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> stringResponse;

    private String readFile(String filename) {
        try {
            return Files.readString(Paths.get(getClass().getResource("/" + filename).toURI()));
        } catch (IOException | URISyntaxException | NullPointerException e) {
            throw new RuntimeException("unable to read test file: " + filename, e);
        }
    }

    @Test
    public void testKaginawaClient() {
        var client = new KaginawaClient("https://example.com", "12345");
        assertEquals("https://example.com", client.getEndpoint());
        assertEquals("12345", client.getApiKey());
        var proxiedClient = new KaginawaClient("http://example.com", "12345", ProxySelector.getDefault());
        assertEquals("http://example.com", proxiedClient.getEndpoint());
        assertEquals("12345", proxiedClient.getApiKey());
    }

    @Test
    public void testKaginawaClient_NPE() {
        assertThrows(NullPointerException.class, () -> new KaginawaClient(null, null));
        assertThrows(NullPointerException.class, () -> new KaginawaClient(null, "12345"));
        assertThrows(NullPointerException.class, () -> new KaginawaClient("http://example.com", null));
    }

    @Test
    public void testKaginawaClient_IAE() {
        assertThrows(IllegalArgumentException.class, () -> new KaginawaClient("", "12345"));
        assertThrows(IllegalArgumentException.class, () -> new KaginawaClient("foo", "12345"));
        assertThrows(IllegalArgumentException.class, () -> new KaginawaClient("https://example.com", ""));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 3}) // nodes_alive_0.json and nodes_alive_3.json
    public void testListAliveNodes(int nOfResponse) {
        var client = new KaginawaClient("http://example.com", "12345", httpClient);
        try {
            when(stringResponse.body()).thenReturn(readFile("nodes_alive_" + nOfResponse + ".json"));
            when(stringResponse.statusCode()).thenReturn(200);
            when(httpClient.send(any(), eq(client.getStringHandler()))).thenReturn(stringResponse);
        } catch (IOException | InterruptedException e) {
            fail(e);
        }
        List<Report> list = null;
        try {
            list = client.listAliveNodes(5);
        } catch (KaginawaServerException e) {
            fail(e);
        }
        assertNotNull(list);
        assertEquals(nOfResponse, list.size());
        list.forEach(n -> {
            assertNotNull(n.getId());
            assertTrue(n.isSuccess());
            assertTrue(n.getCustomId().startsWith("pi"));
            assertEquals(1591263924, n.getServerTimeAsLong());
            assertEquals("", n.getLocalIpV4());
            assertEquals("", n.getDiskLabel());
            assertNotNull(n.getUsbDevices());
            assertTrue(n.getUsbDevices().isEmpty());
        });
    }

    @Test
    public void testListAliveNodes_404() {
        var client = new KaginawaClient("http://example.com", "12345", httpClient);
        try {
            when(stringResponse.statusCode()).thenReturn(404);
            when(httpClient.send(any(), eq(client.getStringHandler()))).thenReturn(stringResponse);
        } catch (IOException | InterruptedException e) {
            fail(e);
        }
        try {
            client.listAliveNodes(5);
            fail("no expected exceptions are thrown");
        } catch (KaginawaServerException e) {
            assertEquals(404, e.getHttpStatus());
        }
    }

    @Test
    public void testListAliveNodes_KSE() {
        var client = new KaginawaClient("http://example.com", "12345", httpClient);
        try {
            when(stringResponse.body()).thenReturn(readFile("not_a_json.txt"));
            when(stringResponse.statusCode()).thenReturn(200);
            when(httpClient.send(any(), eq(client.getStringHandler()))).thenReturn(stringResponse);
        } catch (IOException | InterruptedException e) {
            fail(e);
        }
        assertThrows(KaginawaServerException.class, () -> client.listAliveNodes(5));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 3}) // nodes_cid_0.json and nodes_cid_3.json
    public void testListNodesByCustomId(int nOfResponse) {
        var client = new KaginawaClient("http://example.com", "12345", httpClient);
        try {
            when(stringResponse.body()).thenReturn(readFile("nodes_cid_" + nOfResponse + ".json"));
            when(stringResponse.statusCode()).thenReturn(200);
            when(httpClient.send(any(), eq(client.getStringHandler()))).thenReturn(stringResponse);
        } catch (IOException | InterruptedException e) {
            fail(e);
        }
        List<Report> list = null;
        try {
            list = client.listNodesByCustomId("test-rpi");
        } catch (KaginawaServerException e) {
            fail(e);
        }
        assertNotNull(list);
        assertEquals(nOfResponse, list.size());
        list.forEach(n -> {
            assertNotNull(n.getId());
            assertTrue(n.isSuccess());
            assertEquals("test-rpi", n.getCustomId());
            assertEquals("test-rpi.local", n.getHostname());
            assertTrue(n.getServerTimeAsLong() > 0);
        });
    }

    @Test
    public void testListNodesByCustomId_NPE() {
        var client = new KaginawaClient("http://example.com", "12345");
        assertThrows(NullPointerException.class, () -> client.listNodesByCustomId(null));
    }

    @Test
    public void testListNodesByCustomId_IAE() {
        var client = new KaginawaClient("http://example.com", "12345");
        assertThrows(IllegalArgumentException.class, () -> client.listNodesByCustomId(""));
    }

    @Test
    public void testFindNodeById() {
        var client = new KaginawaClient("http://example.com", "12345", httpClient);
        try {
            when(stringResponse.body()).thenReturn(readFile("node.json"));
            when(stringResponse.statusCode()).thenReturn(200);
            when(httpClient.send(any(), eq(client.getStringHandler()))).thenReturn(stringResponse);
        } catch (IOException | InterruptedException e) {
            fail(e);
        }
        Report report = null;
        try {
            report = client.findNodeById("b8:27:eb:73:90:9f");
        } catch (KaginawaServerException e) {
            fail(e);
        }
        assertNotNull(report);
        assertNotNull(report.getId());
        assertTrue(report.isSuccess());
        assertEquals("test-rpi", report.getCustomId());
        assertEquals("test-rpi.local", report.getHostname());
        assertTrue(report.getServerTimeAsLong() > 0);
    }

    @Test
    public void testFindNodeById_KSE() {
        var client = new KaginawaClient("http://example.com", "12345", httpClient);
        try {
            when(stringResponse.body()).thenReturn("");
            when(stringResponse.statusCode()).thenReturn(404);
            when(httpClient.send(any(), eq(client.getStringHandler()))).thenReturn(stringResponse);
        } catch (IOException | InterruptedException e) {
            fail(e);
        }
        assertThrows(KaginawaServerException.class, () -> client.findNodeById("foo"));
    }

    @Test
    public void testFindNodeById_IAE() {
        var client = new KaginawaClient("http://example.com", "12345");
        assertThrows(IllegalArgumentException.class, () -> client.findNodeById(""));
    }

    @Test
    public void testCommand() {
        var client = new KaginawaClient("http://example.com", "12345", httpClient);
        try {
            when(stringResponse.body()).thenReturn("OK");
            when(stringResponse.statusCode()).thenReturn(200);
            when(httpClient.send(any(), eq(client.getStringHandler()))).thenReturn(stringResponse);
        } catch (IOException | InterruptedException e) {
            fail(e);
        }
        String resp = null;
        try {
            resp = client.command("b8:27:eb:73:90:9f", "echo \"OK\"", "user", "key", "pass", 10);
        } catch (KaginawaServerException e) {
            fail(e);
        }
        assertNotNull(resp);
        assertEquals("OK", resp);
    }

    @Test
    public void testCommand_IAE() {
        var client = new KaginawaClient("http://example.com", "12345");
        assertThrows(IllegalArgumentException.class, () -> client.command("", "cmd", "user", null, "pass", 0));
        assertThrows(IllegalArgumentException.class, () -> client.command("id", "", "user", null, "pass", 0));
        assertThrows(IllegalArgumentException.class, () -> client.command("id", "cmd", "", null, "pass", 0));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 9}) // histories_0.json and histories_9.json
    public void testListHistories(int nOfResponse) {
        var client = new KaginawaClient("http://example.com", "12345", httpClient);
        try {
            when(stringResponse.body()).thenReturn(readFile("histories_" + nOfResponse + ".json"));
            when(stringResponse.statusCode()).thenReturn(200);
            when(httpClient.send(any(), eq(client.getStringHandler()))).thenReturn(stringResponse);
        } catch (IOException | InterruptedException e) {
            fail(e);
        }
        List<Report> list = null;
        try {
            list = client.listHistories("test-rpi", 1591361000, 1591362600);
        } catch (KaginawaServerException e) {
            fail(e);
        }
        assertNotNull(list);
        assertEquals(nOfResponse, list.size());
        list.forEach(n -> {
            assertNotNull(n.getId());
            assertTrue(n.isSuccess());
            assertEquals("test-rpi", n.getCustomId());
            assertEquals("test-rpi.local", n.getHostname());
            assertTrue(n.getServerTimeAsLong() > 0);
            assertTrue(n.getSequence() > 0);
        });
    }

    @Test
    public void testListHistories_NPE() {
        var client = new KaginawaClient("http://example.com", "12345");
        assertThrows(NullPointerException.class, () -> client.listHistories(null, 0, 0));
    }

    @Test
    public void testListHistories_IAE() {
        var client = new KaginawaClient("http://example.com", "12345");
        assertThrows(IllegalArgumentException.class, () -> client.listHistories("", 0, 0));
    }

    @Test
    public void testFindSshServerByHostname() {
        var client = new KaginawaClient("http://example.com", "12345", httpClient);
        try {
            when(stringResponse.body()).thenReturn(readFile("server.json"));
            when(stringResponse.statusCode()).thenReturn(200);
            when(httpClient.send(any(), eq(client.getStringHandler()))).thenReturn(stringResponse);
        } catch (IOException | InterruptedException e) {
            fail(e);
        }
        SshServer server = null;
        try {
            server = client.findSshServerByHostname("example.com");
        } catch (KaginawaServerException e) {
            fail(e);
        }
        assertNotNull(server);
        assertEquals("example.com", server.getHost());
        assertEquals("kaginawa", server.getUser());
        assertEquals(22, server.getPort());
        assertTrue(server.getKey().startsWith("-----BEGIN RSA PRIVATE KEY-----"));
        assertTrue(server.getKey().endsWith("-----END RSA PRIVATE KEY-----"));
    }

    @Test
    public void testFindSshServerByHostname_NPE() {
        var client = new KaginawaClient("http://example.com", "12345");
        assertThrows(NullPointerException.class, () -> client.findSshServerByHostname(null));
    }

    @Test
    public void testFindSshServerByHostname_IAE() {
        var client = new KaginawaClient("http://example.com", "12345");
        assertThrows(IllegalArgumentException.class, () -> client.findSshServerByHostname(""));
    }

    @Test
    public void testFindSshServerByHostname_KSE() {
        var client = new KaginawaClient("http://example.com", "12345", httpClient);
        try {
            when(stringResponse.body()).thenReturn(readFile("not_a_json.txt"));
            when(stringResponse.statusCode()).thenReturn(200);
            when(httpClient.send(any(), eq(client.getStringHandler()))).thenReturn(stringResponse);
        } catch (IOException | InterruptedException e) {
            fail(e);
        }
        assertThrows(KaginawaServerException.class, () -> client.findSshServerByHostname("example.com"));
    }
}
