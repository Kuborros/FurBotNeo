
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
public class DanApi {

    private final String url;
    private final Logger LOG = LoggerFactory.getLogger("ImageBoardApi");
    private final List<String> results = new ArrayList<>();

    public DanApi(String url) {
        this.url = url;
    }

    public List<String> getDanPic() throws IOException, NoImgException {

        try {

            URL u = new URL(url);
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

            JSONArray arr = new JSONArray(str.toString());

            int i = 0;
            while (i < arr.length()) {
                JSONObject obj = arr.getJSONObject(i);
                try {
                    String picUrl = obj.getString("file_url");
                    results.add("https://danbooru.donmai.us" + picUrl);
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
            LOG.error(ex.getLocalizedMessage());
            throw ex;
        }
    }
}

    
    


