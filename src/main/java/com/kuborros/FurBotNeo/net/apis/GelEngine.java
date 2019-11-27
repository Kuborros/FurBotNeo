
package com.kuborros.FurBotNeo.net.apis;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GelEngine {


    private final List<String> urls = new ArrayList<>(100);
    private final String url;
    private final Logger LOG = LoggerFactory.getLogger("ImageBoardApi");
    private final OkHttpClient client = new OkHttpClient();

    public GelEngine(String baseUrl) {
        this.url = baseUrl;
    }

    public List<String> getImageSetRandom() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException, NoImgException {
        Request randomRq = new Request.Builder()
                .url(url)
                .header("User-Agent", "FurryBotNeo/1.0")
                .addHeader("Accept", "application/json")
                .build();
        return getGelPicSet(randomRq);
    }

    public List<String> getImageSetTags(String tags) throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException, NoImgException {
        Request tagRq = new Request.Builder()
                .url(url + "&tags=" + tags.replaceAll(" ", "+"))
                .header("User-Agent", "FurryBotNeo/1.0")
                .addHeader("Accept", "application/json")
                .build();
        return getGelPicSet(tagRq);
    }


    private List<String> getGelPicSet(Request rq) throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException, NoImgException {
        try (Response response = client.newCall(rq).execute()) {
        String tempUrl;

            if (!response.isSuccessful()) throw new IOException("Received error response code: " + response);
            InputSource source = new InputSource(Objects.requireNonNull(response.body()).byteStream());
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(source);

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
           LOG.error("Error occurred while retrieving images: ", ex);
          throw ex;
       }
    }
}