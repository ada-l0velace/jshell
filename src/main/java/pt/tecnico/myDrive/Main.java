package pt.tecnico.myDrive;

import pt.tecnico.myDrive.domain.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;
import org.jdom2.input.SAXBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.io.IOException;


import pt.tecnico.myDrive.service.factory.Factory.FileType;

public class Main {

    private static final Logger logger = LogManager.getRootLogger();

    public static void main(String [] args) {
        try {
            init();
            for (String s: args) xmlScan(new java.io.File(s));
            //setup();
            //xmlPrint();
            //debug();
        } finally {
            // ensure an orderly shutdown
            FenixFramework.shutdown();
        }
    }

    @Atomic
    public static void init() { // empty phonebook
        logger.trace("Init lol: " + FenixFramework.getDomainRoot());
        Manager.getInstance();

     //   logger.trace(Manager.getInstance().getSuperuser().getFileByPath("/home") + "<----------------------------");

    }

    /**
     * Exports the Manager to a xml file
     */
    @Atomic
    public static void xmlPrint() {
        logger.trace("xmlPrint: " + FenixFramework.getDomainRoot());
        Document doc = Manager.getInstance().exportXml();
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());


        try { xmlOutput.output(doc, new PrintStream(System.out));
        } catch (IOException e) { System.out.println(e); }
    }

    /**
     * Debug Xml
     */
    @Atomic
    public static void debug() {
        logger.trace("xmlPrint: " + FenixFramework.getDomainRoot());
        Document doc = Manager.getInstance().xmlExport();
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());


        try { xmlOutput.output(doc, new PrintStream(System.out));
        } catch (IOException e) { System.out.println(e); }
    }

    @Atomic
    public static void setup() {
        logger.trace("Setup: " + FenixFramework.getDomainRoot());
        Manager m = Manager.getInstance();
        //m.createFile(FileType.PLAINFILE, m.createSession("root","***"), "name", "content");
        m.createGuestSession();
    }

    @Atomic
    public static void shutDown() {
        logger.trace("ShutDown: " + FenixFramework.getDomainRoot());
        Manager m = Manager.getInstance();
        m.removeAllSessions();
        FenixFramework.shutdown();
    }



    public static void appTest(String [] args) {}

    /**
     * Scans the xml file and imports to database.
     * @param file (Java.io.File) represents a file. 
     */
    @Atomic
    public static void xmlScan(java.io.File file) {
        Manager m = Manager.getInstance();
        
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document)builder.build(file);
            m.importXml(document.getRootElement());
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

}
