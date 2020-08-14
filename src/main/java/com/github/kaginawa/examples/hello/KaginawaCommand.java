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

import java.util.Scanner;

/**
 * Basic command executing example of the Kaginawa SDK for Java.
 */
public class KaginawaCommand {
    public static void main(String[] args) {
        // build client
        var scanner = new Scanner(System.in);
        System.out.print("endpoint > ");
        var endpoint = scanner.nextLine();
        System.out.print("api key > ");
        var apiKey = scanner.nextLine();
        if (!endpoint.startsWith("http://") && !endpoint.startsWith("https://")) {
            endpoint = "https://" + endpoint;
        }
        var client = new KaginawaClient(endpoint, apiKey);

        // collect target information
        System.out.print("target id > ");
        var id = scanner.nextLine();
        try {
            var report = client.findNodeById(id);
            System.out.println(report.getId() + " " + report.getHostname());
        } catch (KaginawaServerException e) {
            System.err.println(e.toString());
            return;
        }

        System.out.print("user > ");
        var user = scanner.nextLine();
        System.out.print("password > ");
        var password = scanner.nextLine();

        // command prompt
        while (true) {
            System.out.print("command (type \"exit\" to exit) > ");
            var command = scanner.nextLine();
            if (command.equals("exit") || command.equals("quit")) {
                break;
            }
            try {
                var result = client.command(id, command, user, null, password, 0);
                System.out.println(result);
            } catch (KaginawaServerException e) {
                System.err.println(e.toString());
            }
        }
        scanner.close();
    }
}
