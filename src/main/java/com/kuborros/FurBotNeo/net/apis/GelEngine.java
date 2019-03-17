
package com.kuborros.FurBotNeo.net.apis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kuborros
 */
public class GelEngine {


    private final List<String> urls = new ArrayList<>();
    private final String url;
    private final Logger LOG = LoggerFactory.getLogger("ImageBoardApi");
    
    public GelEngine (String url) {
       this.url = url;         
    }

    public List<String> getGelPic() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException, NoImgException {

       try {     

        String tempUrl;
        
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
            tempUrl = eElement.getAttribute("file_url");
			urls.add(tempUrl.startsWith("https:") ? tempUrl : "https:" + tempUrl);
                }
        }
           urls.removeIf(s -> s.contains(".webm"));

           if (urls.isEmpty()) {
               throw new NoImgException();
           }
        return urls;
       }
       catch (IOException | ParserConfigurationException | SAXException  ex) {
          LOG.error(ex.getLocalizedMessage()); 
          throw ex;
       }
    }
}