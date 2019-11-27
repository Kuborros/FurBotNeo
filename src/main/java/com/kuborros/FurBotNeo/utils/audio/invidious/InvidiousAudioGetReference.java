package com.kuborros.FurBotNeo.utils.audio.invidious;

import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

//Example URL: https://www.invidio.us/api/v1/videos/yjbEeVC1yxY?fields=videoId,title,description,author,formatStreams&pretty=1

class InvidiousAudioGetReference {

    private final Logger LOG = LoggerFactory.getLogger("Invidious URL Json");
    private final String apiUrl;
    private final OkHttpClient client = new OkHttpClient();

    InvidiousAudioGetReference(String apiURL) {
        this.apiUrl = apiURL + "videos/"; // https://www.invidio.us/api/v1/videos/
    }

    AudioReference getUrlById(String id) {
        String stream, title;
        try {
            Request request = new Request.Builder()
                    .url(apiUrl + id + "?fields=videoId,title,description,author,formatStreams&pretty=1")
                    .header("User-Agent", "DiscordBot/1.0")
                    .addHeader("Accept", "application/json")
                    .build();
            List<String> ref = getStreamURL(request);
            assert ref != null;
            stream = ref.get(0);
            title = ref.get(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new AudioReference(stream, title);
    }

    AudioReference getUrlById(AudioReference reference) {
        String stream, title;

        String id = StringUtils.substringBefore(StringUtils.substringAfter(reference.identifier, "?v="), "&");
        try {
            Request request = new Request.Builder()
                    .url(apiUrl + id + "?fields=videoId,title,description,author,formatStreams&pretty=1")
                    .header("User-Agent", "DiscordBot/1.0")
                    .addHeader("Accept", "application/json")
                    .build();
            List<String> ref = getStreamURL(request);
            assert ref != null;
            stream = ref.get(0);
            title = ref.get(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new AudioReference(stream, title);
    }

    private ArrayList<String> getStreamURL(Request rq) {

        String title, author, url;
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

            JSONObject response = new JSONObject(str.toString());
            title = response.getString("title");
            author = response.getString("author");
            JSONArray formats = response.getJSONArray("formatStreams");
            url = formats.getJSONObject(0).getString("url");

            ArrayList<String> result = new ArrayList<>(2);
            result.add(url);
            result.add(title + "@" + author);
            return result;
        } catch (Exception e) {
            LOG.error("Error has occurred: ", e);
            return null;
        }
    }
}
