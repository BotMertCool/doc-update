package me.botmert.bot.sheet;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.botmert.bot.DiscordBot;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SheetHandler {
    public void downloadSheetsJson() {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        String url = "http://localhost:4000/1vW-nFbnR02F9BEnNPe5NBejHRGPt0QEGOYXLSePsC1k/0";

        try {
            Request request = new Request.Builder()
                .url(url)
                .build();
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }

            String responseBody = Objects.requireNonNull(response.body()).string();

            Object json = objectMapper.readValue(responseBody, Object.class);

            objectMapper.writeValue(new File("sheets.json"), json);

            DiscordBot.getInstance().getLogger().info("Downloaded sheets.json");
        } catch (IOException e) {
            DiscordBot.getInstance().getLogger().error("Error downloading sheet json", e);
        }
    }

}