package soundboard;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.input.SAXBuilder;
/**
 *
 * @author Babyc
 */

public class Show {
    private Element showElement;
    private Document xmlShowDocument;
    private List<Element> listOfCues;
    private File showFile;
    private SAXBuilder saxBuilder;
    private ArrayList<Cue> cueList;
    public Show()
    {
        saxBuilder = new SAXBuilder(); 
        cueList = new ArrayList<Cue>();
    }
    
    public Show(String directory)
    {
        saxBuilder = new SAXBuilder(); 
        cueList = new ArrayList<Cue>();
    }
    public void createShow(String showName, String directory){
       //create show document with name
       System.out.println(directory);
       try {
        showElement = new Element("show");
        showElement.setAttribute(new Attribute("name", showName));
        xmlShowDocument = new Document(showElement);
        
        Element showCueList = new Element("cues");
        
        xmlShowDocument.getRootElement().addContent(showCueList);

         XMLOutputter xmlOutput = new XMLOutputter();

         // display xml
         xmlOutput.setFormat(Format.getPrettyFormat());
         listOfCues = xmlShowDocument.getRootElement().getChild("cues").getChildren();
        try { 
            xmlOutput.output(xmlShowDocument, System.out);
            showFile = new File(directory + "/" + showName + ".xml");
            xmlOutput.output(xmlShowDocument, new FileOutputStream(showFile));
            
        } catch (IOException ex) {
            Logger.getLogger(Show.class.getName()).log(Level.SEVERE, null, ex);
        }
       } catch (NullPointerException ex){
           System.out.println("Nothing entered");
       }
    }
    
    public boolean openShow(File loadFile) throws JDOMException
    {
        try {
            xmlShowDocument = saxBuilder.build(loadFile); 
            if(xmlShowDocument.hasRootElement()){
                if(xmlShowDocument.getRootElement().getChild("cues") != null){
                  
                    listOfCues = xmlShowDocument.getRootElement().getChild("cues").getChildren();
                    for(int i = 0; i < listOfCues.size(); i++)
                    {
                        
                        String cueName = listOfCues.get(i).getAttributeValue("name");
                        System.out.println(i);
                        File cueFile = new File(listOfCues.get(i).getAttributeValue("filePath"));
                        int volume = Integer.parseInt(listOfCues.get(i).getAttributeValue("volume"));
                        Cue currentCue = new Cue(cueName, cueFile, -1, volume);
                        cueList.add(currentCue);
                    }
                    showFile = loadFile;
                    System.out.println("Show file loaded");
                    return true;
                }
                return false;
            }
    
        } catch (IOException ex) {
            Logger.getLogger(Show.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NullPointerException ex){
            System.out.println("Nothing Entered");
            return false;
        }
        
        return false;
    }
    
    public void addCue(Cue newCue)
    {
        Element cue = new Element("cue" + Integer.toString(listOfCues.size()));
        cue.setAttribute(new Attribute("name", newCue.getName()));
        cue.setAttribute(new Attribute("filePath", newCue.getFilePath()));
        cue.setAttribute(new Attribute("length", Double.toString(newCue.getLength())));
        cue.setAttribute(new Attribute("volume", String.valueOf(newCue.getVolume())));
        listOfCues.add(cue);
        cueList.add(newCue);
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try { 
            xmlOutput.output(xmlShowDocument, System.out);
            xmlOutput.output(xmlShowDocument, new FileOutputStream(showFile));
        } catch (IOException ex) {
            Logger.getLogger(Show.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editCueVolume(int index, int volume)
    {
        Element cue = listOfCues.get(index);
        cue.setAttribute("volume", String.valueOf(volume));
        
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try { 
            xmlOutput.output(xmlShowDocument, System.out);
            xmlOutput.output(xmlShowDocument, new FileOutputStream(showFile));
        } catch (IOException ex) {
            Logger.getLogger(Show.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editCueName(int index, String newName)
    {
        Element cue = listOfCues.get(index);
        cue.setAttribute("name", String.valueOf(newName));
        
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try { 
            xmlOutput.output(xmlShowDocument, System.out);
            xmlOutput.output(xmlShowDocument, new FileOutputStream(showFile));
        } catch (IOException ex) {
            Logger.getLogger(Show.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addCue(Cue newCue, int index)
    {
        Element cue = new Element("cue" + Integer.toString(listOfCues.size()));
        cue.setAttribute(new Attribute("name", newCue.getName()));
        cue.setAttribute(new Attribute("filePath", newCue.getFilePath()));
        cue.setAttribute(new Attribute("length", Double.toString(newCue.getLength())));
        cue.setAttribute(new Attribute("volume", String.valueOf(newCue.getVolume())));
        cue.addContent("/n");
        listOfCues.add(index, cue);
        cueList.add(index, newCue);
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try { 
            xmlOutput.output(xmlShowDocument, System.out);
            xmlOutput.output(xmlShowDocument, new FileOutputStream(showFile));
        } catch (IOException ex) {
            Logger.getLogger(Show.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.updateCueList();
    }
    
    public DefaultTableModel getCueList()
    {
        DefaultTableModel returnList = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        returnList.addColumn("Cues");
        returnList.addColumn("Length");
        for(int i = 0; i < listOfCues.size(); i++)
        {
            DecimalFormat df = new DecimalFormat("#.##");
            String[] rowData = {listOfCues.get(i).getAttributeValue("name"), df.format(Double.parseDouble(listOfCues.get(i).getAttributeValue("length")))};
            returnList.addRow(rowData);
        }
        return returnList;
        
   
    }

    private void updateCueList()
    {
        cueList.clear();
        listOfCues = xmlShowDocument.getRootElement().getChild("cues").getChildren();
            for(int i = 0; i < listOfCues.size(); i++)
            {
                String cueName = listOfCues.get(i).getAttributeValue("name");
                System.out.println(i);
                File cueFile = new File(listOfCues.get(i).getAttributeValue("filePath"));
                int volume = Integer.parseInt(listOfCues.get(i).getAttributeValue("volume"));
                Cue currentCue = new Cue(cueName, cueFile, -1, volume);
                cueList.add(currentCue);
            }
         
    }
    
    public void removeCue(int index)
    {
        listOfCues.remove(listOfCues.get(index));
        updateCueList();
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try { 
            xmlOutput.output(xmlShowDocument, System.out);
        } catch (IOException ex) {
            Logger.getLogger(Show.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public Cue getCueAt(int index)
    {    
        return cueList.get(index);
    }
    
    public void stopAll()
    {
        for(int i = 0;i < this.size(); i++)
        {
            cueList.get(i).stop();
        }
    }
    
    public int size()
    {
        return cueList.size();
    }
}
