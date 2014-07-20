package me.bahadir.scramba;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

public class WordFactory extends DefaultHandler{

	public static class FactoryWord {
		public String word;
		public String frequency;
		public List<String> meanings = new LinkedList<>();
	}

	private Context context;
	private List<FactoryWord> words = new ArrayList<>(1500);
	
	private FactoryWord tempWord;
	private String tempVal;
	private int elementCount = 0;
	
	public WordFactory(Context context) {
		this.context = context;
		read();
		Log.i("baha","Read word count: " + words.size() + " of elements " + elementCount);
	}
	
	public void read() {
		elementCount = 0;
		try {
            // create a XMLReader from SAXParser
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            // create a SAXXMLHandler

            // store handler in XMLReader
            xmlReader.setContentHandler(this);
            // the process starts
            xmlReader.parse(new InputSource(context.getResources().getAssets().open("words.xml")));
            // get the `Employee list`
            
 
        } catch (Exception ex) {
            Log.e("XML", "SAXXMLParser: parse() failed\n" + ex.getMessage());
        }
		
		
		
	}
	
	public FactoryWord getRandomWord() {
		return words.get(Util.randInt(0, words.size()-1));
	}
	
	public List<FactoryWord> getWords() {
		return words;
	}

	// Event Handlers
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        // reset
    	elementCount++;
        tempVal = "";
        if (qName.equalsIgnoreCase("word")) {
            // create a new instance of employee
            tempWord = new FactoryWord();
            tempWord.word = attributes.getValue("word");
            tempWord.frequency = attributes.getValue("frequency");
            words.add(tempWord);
        }
    }
 
    
    
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        tempVal = new String(ch, start, length);
    }
 
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equalsIgnoreCase("meaning")) {
            // add it to the list
            tempWord.meanings.add(tempVal);
        } 
    }
	
	
	
}
