package com.kuborros.FurBotNeo.utils.audio.invidious;

import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Example URL: https://www.invidio.us/api/v1/videos/yjbEeVC1yxY?fields=videoId,title,description,author,formatStreams&pretty=1

public class InvidiousAudioGetReference {

    private final Logger LOG = LoggerFactory.getLogger("Invidious URL Json");
    private String apiUrl;

    InvidiousAudioGetReference(String apiURL) {
        this.apiUrl = apiURL + "videos/"; // https://www.invidio.us/api/v1/videos/
    }

    public AudioReference getUrlById(String id) {
        String stream, title;
        try {
            List<String> ref = getStreamURL(new URL(
                    apiUrl + id + "?fields=videoId,title,description,author,formatStreams&pretty=1")
            );
            assert ref != null;
            stream = ref.get(0);
            title = ref.get(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new AudioReference(stream, title);
    }

    public AudioReference getUrlById(AudioReference reference) {
        String stream, title;

        String id = StringUtils.substringBefore(StringUtils.substringAfter(reference.identifier, "?v="), "&");
        try {
            List<String> ref = getStreamURL(new URL(
                    apiUrl + id + "?fields=videoId,title,description,author,formatStreams&pretty=1")
            );
            assert ref != null;
            stream = ref.get(0);
            title = ref.get(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new AudioReference(stream, title);
    }

    private ArrayList<String> getStreamURL(URL u) {

        String title, author, url;
        try {

            URLConnection UC = u.openConnection();
            UC.setRequestProperty("User-agent", "DiscordBot/1.0");
            InputStream r = UC.getInputStream();

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
            LOG.error("Error has occured: ", e);
            return null;
        }
    }
}
