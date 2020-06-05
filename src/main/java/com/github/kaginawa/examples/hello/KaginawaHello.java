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
package com.github.kaginawa.examples.hello;

import com.github.kaginawa.sdk.KaginawaClient;
import com.github.kaginawa.sdk.KaginawaServerException;
import com.github.kaginawa.sdk.Report;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Basic example of the Kaginawa SDK for Java.
 */
public class KaginawaHello {
    private static final String USAGE = "Usage: KaginawaHello -e <ENDPOINT> -k <API_KEY>";

    public static void main(String[] args) {
        // get endpoint and api key from command-line arguments
        var endpoint = "";
        var apiKey = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-e") && i + 1 < args.length) {
                endpoint = args[i + 1];
            } else if (args[i].equals("-k") && i + 1 < args.length) {
                apiKey = args[i + 1];
            }
        }
        if (endpoint.isEmpty() || apiKey.isEmpty()) {
            System.out.println(USAGE);
            System.exit(2);
            return;
        }

        // create a client object
        var client = new KaginawaClient(endpoint, apiKey);

        // retrieve list of alive (recently reported) nodes
        List<Report> aliveNodes;
        try {
            aliveNodes = client.listAliveNodes(5);
        } catch (KaginawaServerException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return;
        }

        // print the result
        System.out.println(aliveNodes.size() + " alive node(s) detected.");
        var formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        aliveNodes.forEach(n -> {
            var t = n.getServerTime().format(formatter);
            System.out.println(t + " " + n.getId() + " " + n.getCustomId());
        });
    }
}
