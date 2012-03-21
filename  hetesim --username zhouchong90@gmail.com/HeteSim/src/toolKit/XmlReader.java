package toolKit;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.soap.Node;

/**
 * Create,Load xml file;
 * @author liugang
 *
 */
public class XmlReader {
	
	private String filePath;
	
	public XmlReader(String XMLFilePath)
	{
		filePath = XMLFilePath;
	}
	
	/**
	 * create xml file;
	 * @param FilePath
	 */
	public void createXml()
	{
		Document document = DocumentHelper.createDocument();

		Element heteSimElement = document.addElement("HeteSim");
		Element nameElement = heteSimElement.addElement("name");
		nameElement.setText("AuthorNet");

		Element filePathElement = heteSimElement.addElement("filePath");
		filePathElement.setText("C:/HeteSim");
		
		Element typeElement = heteSimElement.addElement("type");
		typeElement.setText("txt");
		Element entitiesElement = heteSimElement.addElement("entities");
		
		Element entityAElement = entitiesElement.addElement("entity");
		Element nameAElement = entityAElement.addElement("name");
		nameAElement.setText("Author");
		Element abbrAElement = entityAElement.addElement("abbr");
		abbrAElement.setText("A");
		Element relatedAElement = entityAElement.addElement("relatedEntities");
		relatedAElement.setText("???");
		
		Element entityCElement = entitiesElement.addElement("entity");
		Element nameCElement = entityCElement.addElement("name");
		nameCElement.setText("Conference");
		Element abbrCElement = entityCElement.addElement("abbr");
		abbrCElement.setText("C");
		Element relatedCElement = entityCElement.addElement("relatedEntities");
		relatedCElement.setText("???");		
		
		Element entityLElement = entitiesElement.addElement("entity");
		Element nameLElement = entityLElement.addElement("name");
		nameLElement.setText("Lable");
		Element abbrLElement = entityLElement.addElement("abbr");
		abbrLElement.setText("L");
		Element relatedLElement = entityLElement.addElement("relatedEntities");
		relatedLElement.setText("???");				

		Element entityPElement = entitiesElement.addElement("entity");
		Element namePElement = entityPElement.addElement("name");
		namePElement.setText("Paper");
		Element abbrPElement = entityPElement.addElement("abbr");
		abbrPElement.setText("P");
		Element relatedPElement = entityPElement.addElement("relatedEntities");
		relatedPElement.setText("???");

		Element entityTElement = entitiesElement.addElement("entity");
		Element nameTElement = entityTElement.addElement("name");
		nameTElement.setText("Term");
		Element abbrTElement = entityTElement.addElement("abbr");
		abbrTElement.setText("T");
		Element relatedTElement = entityTElement.addElement("relatedEntities");
		relatedTElement.setText("???");		

		Element relationsElement = heteSimElement.addElement("relations");
		
		Element relationPAElement = relationsElement.addElement("relation");
		Element namePAElement = relationPAElement.addElement("name");
		namePAElement.setText("P-A");
		
		Element relationPCElement = relationsElement.addElement("relation");
		Element namePCElement = relationPCElement.addElement("name");
		namePCElement.setText("P-C");
		
		Element relationPLElement = relationsElement.addElement("relation");
		Element namePLElement = relationPLElement.addElement("name");
		namePLElement.setText("P-L");	
		
		Element relationPTElement = relationsElement.addElement("relation");
		Element namePTElement = relationPTElement.addElement("name");
		namePTElement.setText("P-T");
		
		try {
			XMLWriter output = new XMLWriter(new FileWriter(new File(
					filePath)));
			output.write(document);
			output.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * load xml file from a filePath
	 * @param filePath
	 * @return
	 * @throws MalformedURLException
	 * @throws DocumentException
	 */
	private Document readXml()	throws MalformedURLException,DocumentException
	{
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(filePath));
		return document;
	}

	/**
	 * get the name of all relations
	 * @param filePath
	 * @return
	 * @throws MalformedURLException
	 * @throws DocumentException
	 */
	public ArrayList<String> getRelations() throws MalformedURLException, DocumentException
	{
		ArrayList<String> relationList = new ArrayList<String>();
		Document document = readXml();
		Element root = document.getRootElement();
		
		for(Iterator i = root.elementIterator() ; i.hasNext(); ) 
		{
			Element element = (Element) i.next();
			// do something
			if(element.getName().equals("relations"))
			{
				for(Iterator j = element.elementIterator();j.hasNext();)
				{
					Element ele = (Element) j.next();
					Iterator k = ele.elementIterator();
					Element el = (Element) k.next();
//					System.out.println(el.getText());
					relationList.add(el.getText());
				}
			}
		}
		return relationList;
	}
	/**
	 * get the name(abbr) of all the entity
	 * @param filePath
	 * @return
	 * @throws MalformedURLException
	 * @throws DocumentException
	 */
	public ArrayList<String> getEntities() throws MalformedURLException, DocumentException
	{
		ArrayList<String> entityList = new ArrayList<String>();
		Document document = readXml();
		Element root = document.getRootElement();

		for(Iterator i = root.elementIterator() ; i.hasNext(); ) 
		{
			Element element = (Element) i.next();
			// do something
			if(element.getName().equals("entities"))
			{
				for(Iterator j = element.elementIterator();j.hasNext();)
				{
					Element ele = (Element) j.next();
					for(Iterator k = ele.elementIterator();k.hasNext();)
					{
						Element el = (Element) k.next();
						if(el.getName().equals("abbr"))
							//System.out.println(el.getText());
							entityList.add(el.getText());
					}
				}
			}
		}
		return entityList; 
	}
	/**
	 * get the name of abbr,if not exist,return "null"
	 * @param abbr
	 * @return
	 */
	public String getAbbrName(char abbr) throws MalformedURLException, DocumentException
	{
		ArrayList<String> entityNameList = new ArrayList<String>();
		String name = "null";
		Document document = readXml();
		Element root = document.getRootElement();

		for(Iterator i = root.elementIterator() ; i.hasNext(); ) 
		{
			Element element = (Element) i.next();
			if(element.getName().equals("entities"))
			{
				for(Iterator j = element.elementIterator();j.hasNext();)
				{
					Element ele = (Element) j.next();
					for(Iterator k = ele.elementIterator();k.hasNext();)
					{
						Element el = (Element) k.next();
						if(el.getName().equals("name"))
							entityNameList.add(el.getText());
					}
				}
			}
		}
		for(String eachName : entityNameList)
			if(eachName.charAt(0) == abbr)
				name = eachName;				
		return name;
	}
	public String getFilePath() throws MalformedURLException, DocumentException
	{
		ArrayList<String> entityList = new ArrayList<String>();
		Document document = readXml();
		Element root = document.getRootElement();	
		for(Iterator i = root.elementIterator() ; i.hasNext(); ) 
		{
			Element element = (Element) i.next();
			if(element.getName().equals("filePath"))
				return element.getText();
		}
		return null;		
	}
	public String getFileType() throws MalformedURLException, DocumentException
	{
		ArrayList<String> entityList = new ArrayList<String>();
		Document document = readXml();
		Element root = document.getRootElement();	
		for(Iterator i = root.elementIterator() ; i.hasNext(); ) 
		{
			Element element = (Element) i.next();
			if(element.getName().equals("type"))
				return element.getText();
		}
		return null;
	}
	/**
	 * test
	 * @param args
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException, DocumentException
	{
		XmlReader heteSim = new XmlReader("C:/AuthorDataConf.xml");
		ArrayList<String> list = new ArrayList<String>();
		heteSim.createXml();
//		heteSim.parserXml("/home/liugang/HeteSim.xml");
//		heteSim.getRelation("/home/liugang/HeteSim.xml");
//		list = heteSim.getEntity("/home/liugang/HeteSim.xml");
//		System.out.println(heteSim.getAbbrName('B',"/home/liugang/HeteSim.xml"));
		System.out.println(heteSim.getFilePath());
		System.out.println(heteSim.getFileType());
		System.out.println("Done");
	}
}






