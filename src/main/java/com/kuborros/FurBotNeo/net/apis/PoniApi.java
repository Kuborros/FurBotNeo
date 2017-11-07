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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kuborros
 */
public class PoniApi {
    
    private String url;
    Logger LOG = LoggerFactory.getLogger("ImageBoardApi");    
    
    public PoniApi(String url) {
        this.url = url;
    }
    
public String getPoniPic() throws JSONException, WebmPostException{

     
        try {
            
            URL u = new URL(url);
            URLConnection UC = u.openConnection();
            UC.setRequestProperty ( "User-agent", "DiscordBot/1.0");
            InputStream r = UC.getInputStream();
            
            String str;
            try (Scanner scan = new Scanner(r)) {
                str = new String();
                while (scan.hasNext()) {
                    str += scan.nextLine();
                }
            }
            JSONObject obj = new JSONObject(str);
            JSONObject res = obj.getJSONArray("search").getJSONObject(0);
            JSONObject loc = res.getJSONObject("representations");
            
            String ext = res.getString("original_format");
            String picUrl = loc.getString("full");
            
            if (ext.equals("webm")){
                throw new WebmPostException();
            }
            return "https:" + picUrl;
        } catch (IOException ex) {
            LOG.error(ex.getLocalizedMessage());
            return null;
        }
    
    }
    
    
}
