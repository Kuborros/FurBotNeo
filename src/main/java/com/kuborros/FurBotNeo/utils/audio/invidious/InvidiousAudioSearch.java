package com.kuborros.FurBotNeo.utils.audio.invidious;

import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

class InvidiousAudioSearch {
    private final Logger LOG = LoggerFactory.getLogger("Invidious URL Json");
    private final String apiUrl; //Example URL: https://www.invidio.us/api/v1/search/?q=gaben&fields=videoId,title&pretty=1
    private final OkHttpClient client = new OkHttpClient();

    InvidiousAudioSearch(String apiURL) {
        this.apiUrl = apiURL + "search/"; // https://www.invidio.us/api/v1/search/
    }

    String getIdBySearch(AudioReference reference) {
        String ref;
        try {
            Request request = new Request.Builder()
                    .url(apiUrl + "?q=" + reference.identifier.replaceFirst("ytsearch: ", "").replaceAll(" ", "%20") + "&fields=videoId,title&pretty=1")
                    .header("User-Agent", "DiscordBot/1.0")
                    .addHeader("Accept", "application/json")
                    .build();
            ref = getStreamId(request);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return ref;
    }

    private String getStreamId(Request rq) {

        String id;
        try (Response resp = client.newCall(rq).execute()) {

            if (!resp.isSuccessful()) throw new IOException("Received error response code: " + resp);
            InputStream r = Objects.requireNonNull(resp.body()).byteStream();

            StringBuilder str;
            try (Scanner scan = new Scanner(r)) {
                str = new StringBuilder();
                while (scan.hasNext()) {
                    str.append(scan.nextLine());
                }
            }

            JSONArray response = new JSONArray(str.toString());
            id = response.getJSONObject(0).getString("videoId");

            return id;
        } catch (Exception e) {
            LOG.error("Error has occurred: ", e);
            return "";
        }
    }
}
