package com.jc.xmlsplitter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// https://www.programcreek.com/2011/08/java-parse-xml-by-using-stax/


public class StaxXMLSplitter {

	public static void main(String[] args) throws FileNotFoundException,
	XMLStreamException, FactoryConfigurationError, ParserConfigurationException, TransformerException {
	// First create a new XMLInputFactory
	XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	
	
	// Stream input file
	InputStream in = new FileInputStream("input.xml");
	
	// Create Event Reader for event-driven looping
	XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
	int count = 0;
	
	StringBuilder sb = new StringBuilder();
	// step thru each event
	while (eventReader.hasNext()) {
		
		// capture the event type
		XMLEvent event = eventReader.nextEvent();
		//System.out.println("event type: " + event.getEventType() + " event: " + event);
		
		// check if event is opening element tag
		if (event.isStartElement()) {
			
			// convert event to start element
			StartElement startElement = event.asStartElement();
	
			// check if target element name matches
	
			if (startElement.getName().getLocalPart().equals("Child")) {
				sb.append(startElement);
				//System.out.println("--start of Child");
				
			
			}
			
			if (startElement.getName().getLocalPart().equals("Name")) {
				sb.append(startElement);
				// capture the next event
				event = eventReader.nextEvent();
				if(event.isCharacters()) {
					//System.out.println("Name: "+ event.asCharacters().getData());
					sb.append(event);
				}
			}
			
		
		}
		
		// check if event is closing element tag
		if (event.isEndElement()) {
			EndElement endElement = event.asEndElement();
			if (endElement.getName().getLocalPart() == "Name") {
				sb.append(endElement);
			
			}
		}
	
		// check if event is closing element tag
		if (event.isEndElement()) {
			EndElement endElement = event.asEndElement();
			if (endElement.getName().getLocalPart() == "Child") {
				sb.append(endElement);
				//System.out.println("--end of a Child\n");
				System.out.println("sb: " + sb.toString());
				StringToXML(sb, count);
				sb.setLength(0);
				count++;
			}
		}
		
		
		
		}
		 
	}
	
	public static void StringToXML(StringBuilder sb, int count) throws ParserConfigurationException, TransformerException {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		DocumentBuilder builder;  
		builder = factory.newDocumentBuilder();  
	    try {
			Document document = builder.parse(new InputSource(new StringReader(sb.toString())));
			document.setXmlStandalone(true);
			
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File("file"+count+".xml"));
			
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		
			transformer.transform(source, result);
			
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
