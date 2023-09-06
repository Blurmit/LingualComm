package me.blurmit.lingualcomm.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;

@AllArgsConstructor(staticName = "create")
public class Request implements IRequest {

    private final RequestBuilder requestBuilder;

    public static RequestBuilder create(RequestType type) {
        return RequestBuilder.create().setURL("https://www.example.com/").setType(type);
    }

    public static RequestBuilder create(String url) {
        return RequestBuilder.create().setURL(url).setType(RequestType.GET);
    }

    public String send() {
        try {
            URL url = requestBuilder.getURL();
            RequestType type = requestBuilder.getType();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(type.toString());
            connection.setDoOutput(true);

            for (Map.Entry<String, String> header : requestBuilder.getHeaders().entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }

            JsonObject data = requestBuilder.getRawData();
            if (data != null) {
                byte[] dataBytes = data.toString().getBytes(StandardCharsets.UTF_8);
                int length = dataBytes.length;

                connection.setFixedLengthStreamingMode(length);
                connection.connect();

                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(dataBytes);
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String input;

            while ((input = reader.readLine()) != null) {
                content.append(input);
            }

            reader.close();
            connection.disconnect();

            return content.toString();
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to send HTTP request", e);

            JsonObject translations = new JsonObject();
            JsonArray translationsData = new JsonArray();
            JsonObject dataObject = new JsonObject();

            dataObject.addProperty("detected_source_language", "EN");
            dataObject.add("text", JsonNull.INSTANCE);

            translationsData.add(dataObject);

            translations.add("translations", translationsData);

            return translations.toString();
        }
    }

}
