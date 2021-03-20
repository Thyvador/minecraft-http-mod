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

    public static final int PORT = 8001;
    private final Logger logger = LogManager.getLogger();

    public Server() throws IOException {
        logger.debug(String.format("Server starting on port %d", PORT));
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/potions", new PotionHandler());
        server.createContext("/weather", new WeatherHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();

        logger.debug(String.format("Server started on port %d", PORT));
    }
}
