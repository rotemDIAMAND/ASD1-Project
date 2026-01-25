package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParser {

    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();
        if (firstLine == null || firstLine.isEmpty()) {
            return null;
        }
        String line;
        int contentLength = 0;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.toLowerCase().startsWith("content-length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }
        Map<String, String> parameters = new HashMap<>();
        while (reader.ready()) {
            line = reader.readLine();
            if (line == null || line.isEmpty()) continue;
            if (line.contains("=")) {
                String[] pairs = line.split("&");
                for (String p : pairs) {
                    String[] kv = p.split("=");
                    if (kv.length == 2) {
                        parameters.put(kv[0], kv[1]);
                    }
                }
            } else {
                StringBuilder contentBuilder = new StringBuilder();
                contentBuilder.append(line).append("\n");
                while (reader.ready()) {
                    contentBuilder.append((char) reader.read());
                }
                return createRequestInfo(firstLine, parameters, contentBuilder.toString().getBytes());
            }
        }
        return createRequestInfo(firstLine, parameters, null);
    }
    private static RequestInfo createRequestInfo(String firstLine, Map<String, String> parameters, byte[] content) {
        String[] parts = firstLine.split(" ");
        String httpCommand = parts[0];
        String uri = parts[1];
        String path = uri;
        if (uri.contains("?")) {
            path = uri.substring(0, uri.indexOf("?"));
            String queryString = uri.substring(uri.indexOf("?") + 1);
            String[] pairs = queryString.split("&");
            for (String p : pairs) {
                String[] kv = p.split("=");
                if (kv.length == 2) {
                    parameters.put(kv[0], kv[1]);
                }
            }
        }
        String[] temp = path.split("/");
        List<String> list = new ArrayList<>();
        for (String s : temp) {
            if (!s.isEmpty()) {
                list.add(s);
            }
        }
        String[] uriSegments = list.toArray(new String[0]);
        return new RequestInfo(httpCommand, uri, uriSegments, parameters, content);
    }

    public static class RequestInfo {
        private final String httpCommand;
        private final String uri;
        private final String[] uriSegments;
        private final Map<String, String> parameters;
        private final byte[] content;

        public RequestInfo(String httpCommand, String uri, String[] uriSegments, Map<String, String> parameters, byte[] content) {
            this.httpCommand = httpCommand;
            this.uri = uri;
            this.uriSegments = uriSegments;
            this.parameters = parameters;
            this.content = content;
        }

        public String getHttpCommand() {
            return httpCommand;
        }

        public String getUri() {
            return uri;
        }

        public String[] getUriSegments() {
            return uriSegments;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public byte[] getContent() {
            return content;
        }
    }
}
