
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
public class E621Api {

    private final String url;
    private final Logger LOG = LoggerFactory.getLogger("ImageBoardApi");
    private final List<String> result = new ArrayList<>();

public E621Api(String url){
    this.url = url;
}

    public List<String> getImageSetRandom() throws IOException, NoImgException {
        URL u = new URL(url + "order:random+-flash+-webm&limit=100");
        return getFurryPicSet(u);
    }

    public List<String> getImageSetTags(String tags) throws IOException, NoImgException {
        URL u = new URL(url + "&tags=" + tags.replaceAll(" ", "+") + "+order:random&limit=100");
        return getFurryPicSet(u);
    }

    private List<String> getFurryPicSet(URL u) throws IOException, NoImgException {
     
        try {

            URLConnection UC = u.openConnection();
            UC.setRequestProperty ( "User-agent", "DiscordBot/1.0");
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
                try {
                    JSONObject obj = arr.getJSONObject(i);
                    result.add(obj.getString("file_url"));
                } catch (JSONException e) {
                    LOG.debug("Picture was missing its file_url");
                }
                i++;
            }
            if (result.isEmpty()) {
                throw new NoImgException();
            }

            return result;
        } catch (IOException ex) {
            LOG.error("Error occured while retreiving images: ", ex);
            throw ex;
        }
    
    } 

}
