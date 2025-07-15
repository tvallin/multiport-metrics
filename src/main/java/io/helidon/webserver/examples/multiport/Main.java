/*
 * Copyright (c) 2021, 2025 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.webserver.examples.multiport;

import java.util.List;

import io.helidon.logging.common.LogConfig;
import io.helidon.metrics.api.Counter;
import io.helidon.metrics.api.Metrics;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.WebServerConfig;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.observe.ObserveFeature;

/**
 * The application main class.
 */
public final class Main {

    private static Counter counter;

    /**
     * Cannot be instantiated.
     */
    private Main() {
    }

    /**
     * Application main entry point.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        // load logging configuration
        LogConfig.configureRuntime();
        counter = Metrics.globalRegistry()
                .getOrCreate(Counter.builder("myCounter").description("Counts GET hello endpoint"));
        String observeSocketKey = "observe";

        ObserveFeature observeFeature = ObserveFeature.builder()
                .sockets(List.of(observeSocketKey))
                .build();

        WebServer.builder()
                .host("localhost")
                .port(8080)
                .routing(routing -> routing.get("/hello", (req, res) -> {
                    counter.increment();
                    res.send("Hello!");
                }))
                .addFeature(observeFeature)
                .putSocket(observeSocketKey, socket -> socket.port(8083))
                .build()
                .start();

        System.out.println("WEB server is up! http://localhost:8080");
    }
}
