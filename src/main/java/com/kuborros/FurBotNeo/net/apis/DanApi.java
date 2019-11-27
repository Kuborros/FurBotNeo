
package com.kuborros.FurBotNeo.net.apis;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class DanApi {

    private final String url;
    private final Logger LOG = LoggerFactory.getLogger("ImageBoardApi");
    private final List<String> results = new ArrayList<>(100);
    private final OkHttpClient client = new OkHttpClient();

    public DanApi(String url) {
        this.url = url;
    }

    public List<String> getImageSetRandom() throws IOException, NoImgException {
        Request randomRq = new Request.Builder()
                .url(url)
                .header("User-Agent", "FurryBotNeo/1.0")
                .addHeader("Accept", "application/json")
                .build();
        return getDanPicSet(randomRq);
    }

    public List<String> getImageSetTags(String tags) throws IOException, NoImgException {
        Request tagRq = new Request.Builder()
                .url(url + "&tags=" + tags.replaceAll(" ", "+"))
                .header("User-Agent", "FurryBotNeo/1.0")
                .addHeader("Accept", "application/json")
                .build();
        return getDanPicSet(tagRq);
    }

    private List<String> getDanPicSet(Request rq) throws IOException, NoImgException {

        try (Response response = client.newCall(rq).execute()) {

            if (!response.isSuccessful()) throw new IOException("Received error response code: " + response);
            InputStream r = Objects.requireNonNull(response.body()).byteStream();

            StringBuilder str;
            try (Scanner scan = new Scanner(r)) {
                str = new StringBuilder();
                while (scan.hasNext()) {
                    str.append(scan.nextLine());
                }
            }

            JSONArray arr = new JSONArray(str.toString());

            int i = 0;
            while (i < arr.length()) {
                JSONObject obj = arr.getJSONObject(i);
                try {
                    String picUrl = obj.getString("file_url");
                    results.add(picUrl);
                } catch (JSONException e) {
                    LOG.debug("Picture was missing its file_url");
                }
                i++;
            }
            if (results.isEmpty()) {
                throw new NoImgException();
            }
            return results;
        } catch (IOException ex) {
            LOG.error("Error occurred while retrieving images: ", ex);
            throw ex;
        }
    }
}

    
    


