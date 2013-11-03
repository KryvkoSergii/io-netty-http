package io.netty.http;
 
 
import java.io.File;
import java.util.HashMap;
import java.util.Map;
 
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
 
public class XMLWriterReader {
/** Class is used to read configuration of mySql server, like user name, password, 
 * table name, data base name.
 * Configuration file must be in root directory /Config.xml  
 * **/	
 
private static String dataDaseXMLFileName;
	// HashMap what contains parameters to establish mySql connection 
 private Map list = new HashMap();
	
	public XMLWriterReader(String fileAddress)
	//	set config file address
	{
		this.dataDaseXMLFileName = fileAddress;	
	}
	
 
	public Map getParameters() {
		File file = new File(dataDaseXMLFileName);
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse(file);
			
			// gets root element, <configuration> in file
			Element rootElement = doc.getDocumentElement();
			
			// gets list of nodes, such as <server>
			NodeList childrenList = rootElement.getElementsByTagName("server");
			
			//			gets attributes from nodes
			for (int i=0; i<childrenList.getLength();i++) 
			{
				Node child = childrenList.item(i);
				
				// catch and throw devider except node
				if (child instanceof Element)
				{
				
					Element childElement = (Element) child;
					// get user name
					list.put("user", childElement.getElementsByTagName("user").item(0).getTextContent());
					// get password
					list.put("password", childElement.getElementsByTagName("password").item(0).getTextContent());
					// get data base name					
					list.put("dataBase",childElement.getElementsByTagName("database").item(0).getTextContent());
					// get table name
					list.put("table", childElement.getElementsByTagName("table").item(0).getTextContent());
 
				}
			}
		return list;
		} 
		catch (Throwable e)
		{
			e.printStackTrace();
			return null;
		}
}
	
	
 
}
