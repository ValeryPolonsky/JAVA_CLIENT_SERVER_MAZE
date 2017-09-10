package clientModel;

import java.io.File;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import clientPresenter.Properties;
 
 
/**
 * Class that saves and loads properties from the .xml file
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class SaveAndLoadProperties {
 
    public void saveGameProperties(String viewSetup,String maxMazeSIze,String algorithm) 
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
           
            Element rootElement = doc.createElementNS("gameData","gameData");
          
            doc.appendChild(rootElement);
 
            rootElement.appendChild(getGameData(doc, "1", viewSetup, maxMazeSIze, algorithm));
 
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
           
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
 
            //write to console or file
            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(new File("properties.xml"));
 
            //write data
            transformer.transform(source, console);
            transformer.transform(source, file);
            System.out.println("DONE");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Properties loadGameProperties()
    {
    	Properties dfp=new Properties();
    	 try {

    			File fXmlFile = new File("properties.xml");
    			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    			Document doc = dBuilder.parse(fXmlFile);
    					
    			//optional, but recommended
    			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    			doc.getDocumentElement().normalize();

    		
    			NodeList nList = doc.getElementsByTagName("Element");
    					
    			for (int temp = 0; temp < nList.getLength(); temp++) {

    				Node nNode = nList.item(temp);
    						   				    						
    				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

    					Element eElement = (Element) nNode;
    					
    					dfp.setViewSetup(eElement.getElementsByTagName("viewSetup").item(0).getTextContent());
    					dfp.setMaxMazeSize(eElement.getElementsByTagName("maxMazeSize").item(0).getTextContent());
    					dfp.setDefaultAlgorithm(eElement.getElementsByTagName("defaultAlgorithm").item(0).getTextContent());
    				}
    			}
    			
    		    } catch (Exception e) {
    			e.printStackTrace();
    		    }
    	 return dfp;
    }
 
 
    private static Node getGameData(Document doc, String id, String viewSetup, String maxMazeSize, String defaultAlgorithm)
    {
        Element element = doc.createElement("Element");
        element.setAttribute("id", id);
        element.appendChild(getGameDataElements(doc, element, "viewSetup", viewSetup));
        element.appendChild(getGameDataElements(doc, element, "maxMazeSize", maxMazeSize));
        element.appendChild(getGameDataElements(doc, element, "defaultAlgorithm", defaultAlgorithm));
 
        return element;
    }
 
    //utility method to create text node
    private static Node getGameDataElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
 
}
