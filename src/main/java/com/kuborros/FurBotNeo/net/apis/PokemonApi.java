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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
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
public class PokemonApi {
    
private List<String> urls = new ArrayList<>();
private String url;
    private Logger LOG = LoggerFactory.getLogger("ImageBoardApi");

public PokemonApi(String url){
    this.url = url;
}

public List<String> PokeXml() throws IllegalArgumentException {
    try {


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
			urls.add(eElement.getElementsByTagName("file_url").item(0).getTextContent());
                }
        }
       
       for (int i = 0; i < urls.size(); i++) {
            if (urls.get(i).contains(".webm")){
              urls.remove(i);
            }   
        }
       return urls;
    } catch (IOException | ParserConfigurationException | DOMException | SAXException ex) {
       LOG.error(ex.getLocalizedMessage());
       return null;
    }
}
}
