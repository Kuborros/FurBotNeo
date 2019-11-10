package com.kuborros.FurBotNeo.utils.audio.invidious;

import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class InvidiousAudioSearch {
    private final Logger LOG = LoggerFactory.getLogger("Invidious URL Json");
    private String apiUrl; //Example URL: https://www.invidio.us/api/v1/search/?q=gaben&fields=videoId,title&pretty=1

    InvidiousAudioSearch(String apiURL) {
        this.apiUrl = apiURL + "search/"; // https://www.invidio.us/api/v1/search/
    }

    public String getIdBySearch(AudioReference reference) {
        String ref;
        try {
            ref = getStreamId(new URL(
                    apiUrl + "?q=" + reference.identifier.replaceFirst("ytsearch: ", "").replaceAll(" ", "%20") + "&fields=videoId,title&pretty=1")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return ref;
    }

    private String getStreamId(URL u) {

        String id;
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

            JSONArray response = new JSONArray(str.toString());
            id = response.getJSONObject(0).getString("videoId");

            return id;
        } catch (Exception e) {
            LOG.error("Error has occured: ", e);
            return "";
        }
    }
}
