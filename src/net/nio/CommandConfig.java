package net.nio;

import interfaces.IAction;
import interfaces.IController;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.core.ResultManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class CommandConfig {
	private static CommandConfig actionConfig = null;

	public static CommandConfig gets() {
		if (null == actionConfig) {
			actionConfig = new CommandConfig();
		}
		return actionConfig;
	}
	
	//解析所有模块的
	public void load()
	{
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder db = null;
	    ServerHandler handler = ServerHandler.getInstances();
		try {
			db = dbf.newDocumentBuilder();
			Document document = db.parse("res/config.xml");
			NodeList dogList = document.getElementsByTagName("item");
			for (int i = 0; i < dogList.getLength(); i++)  
            { 
				//自己
				Element elem  = (Element) dogList.item(i); 
				//添加一个模块
				Element parent= (Element) elem.getParentNode();
				int type = Integer.parseInt(parent.getAttribute("type"));
				String name = parent.getAttribute("name");
				if(!handler.hasController(type)) handler.addMoule(type,(IController) create(name));
				//
				NodeList itemList = elem.getChildNodes();
				int cmd = Integer.parseInt(elem.getAttribute("cmd"));
				for(int j = 0;j<itemList.getLength();j++)
				{
					if(itemList.item(j).getNodeName()=="action"){
						handler.addAction(type, cmd, (IAction) create(itemList.item(j).getTextContent()));
					}
					if(itemList.item(j).getNodeName()=="result"){
						ResultManager.register(cmd, itemList.item(j).getTextContent());
					}
				}
            }
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	//---------
	public static Object create(String className)
	{
		Class<?> actionClass = null;
		Object action = null;
		try {
			actionClass = Class.forName(className);
			action = actionClass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return action;
	}
	
	//ends
}
