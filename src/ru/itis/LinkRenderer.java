package ru.itis;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.sun.security.ntlm.Server;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import java.util.Date;

public class LinkRenderer {
    private static final int PORT = 8080;
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);

        server.createContext("/", exchange -> {
            addHeaders(exchange.getResponseHeaders());
            byte[] body = createBody().getBytes();
            exchange.sendResponseHeaders(200, body.length);
            try(OutputStream bodyStream = exchange.getResponseBody()) {
                bodyStream.write(body);
                bodyStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        server.setExecutor(Executors.newFixedThreadPool(15));
        server.start();
        System.out.println("Server started on port: " + PORT);
    }


    private static void addHeaders(Headers headers) {
        headers.add("Content-Type", "text/html");
        headers.add("Server", "localhost");
        headers.add("Date", new Date().toString());
    }

    private static String createBody() {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body><a href='https://www.vk.com'>vkontakte.ru</a></body></html>");
        return builder.toString();
    }
}