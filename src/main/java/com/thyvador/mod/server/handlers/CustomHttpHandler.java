package com.thyvador.mod.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;

import static com.thyvador.mod.server.handlers.HttpVerbs.GET;
import static com.thyvador.mod.server.handlers.HttpVerbs.POST;

public interface CustomHttpHandler extends HttpHandler {

    @Override
    default void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (GET.equals(httpExchange.getRequestMethod())) {
                handleGetRequest(httpExchange);
            } else if (POST.equals(httpExchange.getRequestMethod())) {
                handlePostRequest(httpExchange);
                handleMethodNotAllowed(httpExchange);
            }
        } catch (IOException e) {
            handleInternalError(httpExchange, e);
        }
    }

    default void handleInternalError(HttpExchange httpExchange, Exception e) throws IOException {
        handleResponse(httpExchange, HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }


    default void handleResponse(HttpExchange httpExchange, int status, String body) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(status, body.length());
        outputStream.write(body.getBytes());
        outputStream.flush();
        outputStream.close();
    }


    default void handleMethodNotAllowed(HttpExchange httpExchange) throws IOException {
        handleResponse(httpExchange, HttpStatus.SC_METHOD_NOT_ALLOWED, "");
    }

    void handlePostRequest(HttpExchange httpExchange) throws IOException;

    void handleGetRequest(HttpExchange httpExchange) throws IOException;
}
