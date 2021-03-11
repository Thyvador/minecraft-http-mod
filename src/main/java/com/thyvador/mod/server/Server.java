package com.thyvador.mod.server;

import com.sun.net.httpserver.HttpServer;
import com.thyvador.mod.server.handlers.PotionHandler;
import com.thyvador.mod.server.handlers.WeatherHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    private final Logger logger = LogManager.getLogger();

    public Server() throws IOException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        server.createContext("/potions", new PotionHandler());
        server.createContext("/weather", new WeatherHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();

        logger.debug("Server started on port 8001");
    }
}
