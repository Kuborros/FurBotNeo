/*
 * The MIT License
 *
 * Copyright 2017 Kuborros.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.kuborros.FurBotNeo.net.apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Kuborros
 */
public class Dan2Api {

    private String url;
    Logger LOG = LoggerFactory.getLogger("ImageBoardApi");
    List<String> results = new ArrayList<>();

    public Dan2Api(String url) {
        this.url = url;
    }

    public List<String> getDanPic() throws JSONException, IOException {

        try {

            URL u = new URL(url);
            URLConnection UC = u.openConnection();
            UC.setRequestProperty("User-agent", "DiscordBot/1.0");
            InputStream r = UC.getInputStream();

            String str;
            try (Scanner scan = new Scanner(r)) {
                str = new String();
                while (scan.hasNext()) {
                    str += scan.nextLine();
                }
            }

            JSONArray arr = new JSONArray(str);

            int i = 0;
            while (i < arr.length()) {
                JSONObject obj = arr.getJSONObject(i);
                String picUrl = obj.getString("file_url");
                results.add("https://danbooru.donmai.us" + picUrl);
                i++;
            }

            return results;
        } catch (Exception ex) {
            LOG.error(ex.getLocalizedMessage());
            throw ex;
        }
    }
}

    
    


