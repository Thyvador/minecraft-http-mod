package com.thyvador.mod.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

public class PostHandler implements HttpHandler {

    private static final String POST = "POST";
    private final Logger logger = LogManager.getLogger();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue = null;
        if (POST.equals(httpExchange.getRequestMethod())) {

            requestParamValue = handlePostRequest(httpExchange);

        }


        handleResponse(httpExchange, requestParamValue);
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();

        String htmlResponse = "";
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String handlePostRequest(HttpExchange httpExchange) {
        logger.debug("POST " + httpExchange.getRequestURI() + " " + httpExchange.getRequestBody());
        return "";
    }
}
