package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.*;

import server.RequestParser.RequestInfo;
import servlets.Servlet;

public class MyHTTPServer extends Thread implements HTTPServer {

    private final int port;
    private final ExecutorService threadPool;
    private volatile boolean stop = false;
    private final Map<String, Servlet> getMap = new HashMap<>();
    private final Map<String, Servlet> postMap = new HashMap<>();
    private final Map<String, Servlet> deleteMap = new HashMap<>();

    public MyHTTPServer(int port, int nThreads) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(nThreads);
    }

    @Override
    public void addServlet(String httpCommand, String uri, Servlet s) {
        if (httpCommand.equalsIgnoreCase("GET")) getMap.put(uri, s);
        else if (httpCommand.equalsIgnoreCase("POST")) postMap.put(uri, s);
        else if (httpCommand.equalsIgnoreCase("DELETE")) deleteMap.put(uri, s);
    }

    @Override
    public void removeServlet(String httpCommand, String uri) {
        if (httpCommand.equalsIgnoreCase("GET")) getMap.remove(uri);
        else if (httpCommand.equalsIgnoreCase("POST")) postMap.remove(uri);
        else if (httpCommand.equalsIgnoreCase("DELETE")) deleteMap.remove(uri);
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            server.setSoTimeout(1000);

            while (!stop) {
                try {
                    Socket client = server.accept();
                    threadPool.execute(() -> handleClient(client));
                } catch (SocketTimeoutException e) {}
            }
        } catch (IOException e) {
            if (!stop) e.printStackTrace();
        }
    }

    private void handleClient(Socket client) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStream out = client.getOutputStream();
            RequestInfo ri = RequestParser.parseRequest(in);

            if (ri != null) {
                Servlet s = findBestServlet(ri.getHttpCommand(), ri.getUri());
                if (s != null) {
                    s.handle(ri, out);
                }
            }
            out.flush();
            client.close();
        } catch (IOException e) {}
    }

    private Servlet findBestServlet(String command, String uri) {
        Map<String, Servlet> map;
        if (command.equalsIgnoreCase("GET")) map = getMap;
        else if (command.equalsIgnoreCase("POST")) map = postMap;
        else if (command.equalsIgnoreCase("DELETE")) map = deleteMap;
        else return null;

        String bestMatch = "";
        Servlet selected = null;
        for (String registeredUri : map.keySet()) {
            if (uri.startsWith(registeredUri)) {
                if (registeredUri.length() > bestMatch.length()) {
                    bestMatch = registeredUri;
                    selected = map.get(registeredUri);
                }
            }
        }
        return selected;
    }

    @Override
    public void close() {
        stop = true;
        threadPool.shutdown();
    }
}