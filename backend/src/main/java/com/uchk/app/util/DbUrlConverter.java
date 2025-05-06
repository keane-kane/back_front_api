package com.uchk.app.util;

import java.net.URI;
import java.net.URISyntaxException;

public class DbUrlConverter {
    public static String convertToJdbcUrl(String databaseUrl) {
        try {
            if (databaseUrl == null) {
                return null;
            }
            
            URI uri = new URI(databaseUrl);
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            
            // Get query parameters
            String query = uri.getQuery();
            String queryParams = "";
            if (query != null && !query.isEmpty()) {
                queryParams = "?" + query;
            }
            
            return String.format("jdbc:postgresql://%s:%d/%s%s", 
                host, 
                port != -1 ? port : 5432, 
                path, 
                queryParams);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to parse DATABASE_URL", e);
        }
    }
}