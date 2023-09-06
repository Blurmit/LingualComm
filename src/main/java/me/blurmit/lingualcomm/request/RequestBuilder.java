package me.blurmit.lingualcomm.request;

import com.google.gson.JsonObject;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@NoArgsConstructor(staticName = "create")
public class RequestBuilder implements IRequest {

    private URL url;
    private RequestType type;
    private Map<String, String> headers;
    private JsonObject data;

    public RequestBuilder setURL(URL url) {
        this.url = url;

        return this;
    }

    public RequestBuilder setURL(String url) {
        try {
            this.url = new URL(url);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to set request URL", e);
        }

        return this;
    }

    public URL getURL() {
        return url;
    }

    public RequestBuilder setType(RequestType type) {
        this.type = type;

        return this;
    }

    public RequestType getType() {
        return type;
    }

    public RequestBuilder setRequestHeader(String header, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }

        headers.put(header, value);

        return this;
    }

    public RequestBuilder setUserAgent(String userAgent) {
        return setRequestHeader("User-Agent", userAgent);
    }

    public RequestBuilder setContentType(String contentType) {
        return setRequestHeader("Content-Type", contentType);
    }

    public RequestBuilder setAuthorization(String authorization) {
        return setRequestHeader("Authorization", authorization);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public RequestBuilder addRequestData(String key, String value) {
        if (data == null) {
            data = new JsonObject();
        }

        data.addProperty(key, value);

        return this;
    }

    public RequestBuilder addRequestData(String key, Number value) {
        if (data == null) {
            data = new JsonObject();
        }

        data.addProperty(key, value);

        return this;
    }

    public RequestBuilder addRequestData(String key, Boolean value) {
        if (data == null) {
            data = new JsonObject();
        }

        data.addProperty(key, value);

        return this;
    }

    public RequestBuilder addRequestData(String key, Character value) {
        if (data == null) {
            data = new JsonObject();
        }

        data.addProperty(key, value);

        return this;
    }

    public String getData() {
        return data.toString();
    }

    public JsonObject getRawData() {
        return data;
    }

    public RequestBuilder addURLData(String key, String value) {
        try {
            key = URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
            value = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception ignored) {}

        StringBuilder rawURL = new StringBuilder(url.toString());

        if (url.getQuery() == null) {
            rawURL.append("?");
        }

        if (url.getQuery() != null && !url.getQuery().endsWith("&")) {
            rawURL.append("&");
        }

        rawURL.append(key).append("=").append(value);

        return setURL(rawURL.toString());
    }

    public Map<String, String> getURLData() {
        Map<String, String> data = new HashMap<>();
        String query = url.getQuery();
        String[] parts = new String[] { query };

        if (query.contains("&")) {
            parts = query.split("&");
        }

        for (String part : parts) {
            String[] partData = part.split("=");
            String key = partData[0];
            String value = partData[1];

            data.put(key, value);
        }

        return data;
    }

    public String send() {
        return Request.create(this).send();
    }

}
