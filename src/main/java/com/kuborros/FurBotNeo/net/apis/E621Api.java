
/*
 * Copyright © 2020 Kuborros (kuborros@users.noreply.github.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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


public class E621Api {

    private final String url;
    private final Logger LOG = LoggerFactory.getLogger("ImageBoardApi");
    private final List<String> result = new ArrayList<>(100);
    private final OkHttpClient client = new OkHttpClient();

    public E621Api(String url) {
        this.url = url;
    }

    public List<String> getImageSetRandom() throws IOException, NoImgException {
        Request randomRq = new Request.Builder()
                .url(url + "order:random+-flash+-webm&limit=100")
                .header("User-Agent", "FurryBotNeo/1.0")
                .addHeader("Accept", "application/json")
                .build();
        return getFurryPicSet(randomRq);
    }

    public List<String> getImageSetTags(String tags) throws IOException, NoImgException {
        Request tagRq = new Request.Builder()
                .url(url + "&tags=" + tags.replaceAll(" ", "+") + "+order:random&limit=100")
                .header("User-Agent", "FurryBotNeo/1.0")
                .addHeader("Accept", "application/json")
                .build();
        return getFurryPicSet(tagRq);
    }

    private List<String> getFurryPicSet(Request rq) throws IOException, NoImgException {

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
            JSONObject content = new JSONObject(str.toString());
            JSONArray arr = content.getJSONArray("posts");
            int i = 0;
            while (i < arr.length()) {
                try {
                    JSONObject obj = arr.getJSONObject(i);
                    JSONObject file = obj.getJSONObject("file");
                    result.add(file.getString("url"));
                } catch (JSONException e) {
                    LOG.debug("Picture was missing it's url");
                }
                i++;
            }
            if (result.isEmpty()) {
                throw new NoImgException();
            }

            return result;
        } catch (IOException ex) {
            LOG.error("Error occurred while retrieving images: ", ex);
            throw ex;
        }

    }

}
