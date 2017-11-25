
package com.kuborros.FurBotNeo.net.apis;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Kuborros
 */
public class GelEngine {
    

    List<String> urls = new ArrayList<>();    
    List<String> result = new ArrayList<>();
    private int ur;  
    private String url;
    Logger LOG = LoggerFactory.getLogger("ImageBoardApi");    
    
    public GelEngine (String url) {
       this.url = url;         
    }
   
    public String getGelPic() throws WebmPostException, IllegalArgumentException, JSONException{

       try {     

        Random rand = new Random();
        
        URL u = new URL(url);
        URLConnection UC = u.openConnection();
        UC.setRequestProperty ( "User-agent", "DiscordBot/1.0");
        InputStream r = UC.getInputStream();
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(r);

	doc.getDocumentElement().normalize();


	NodeList nList = doc.getElementsByTagName("post");


	for (int temp = 0; temp < nList.getLength(); temp++) {

		Node nNode = nList.item(temp);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
			urls.add(eElement.getAttribute("file_url"));
                }
        }
        for (int i = 0; i < urls.size(); i++) {
            if (urls.get(i).contains(".webm")){
              urls.remove(i);
            }   
        }
        /*
        if (urls.isEmpty()){
            throw new WebmPostException();
        }
        */
        ur = rand.nextInt(urls.size());
        
        if (urls.get(ur).startsWith("https:")) {
        return urls.get(ur);    
        }
        else return "https:" + urls.get(ur);
       }
       catch (IOException | ParserConfigurationException | SAXException  ex) {
          LOG.error(ex.getLocalizedMessage()); 
          return null; 
       }  
    }
}