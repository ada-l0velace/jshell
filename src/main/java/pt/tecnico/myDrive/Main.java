package pt.tecnico.myDrive;
import pt.tecnico.myDrive.domain.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import java.io.PrintStream;
import java.io.IOException;
import org.jdom2.output.Format;
import org.joda.time.DateTime;


public class Main {

    // FenixFramework will try automatic initialization when first accessed
    public static void main(String [] args) {
        try {
            applicationCodeGoesHere();
        } finally {
            // ensure an orderly shutdown
            FenixFramework.shutdown();
        }
    }

    public static void applicationCodeGoesHere() {
        someTransaction();
    }

    @Atomic
    public static void someTransaction() {
        //Manager m = Manager.getInstance();
        User u = new User("Super User","root","rootroot", (short)1);
        User u1 = new User("Daniel", "Poodle", "GangnamStyle", (short)1);
        //m.createUser(u);
        //m.getUser(u).exportXml();
        Element element = new Element("mydrive");
        Document doc = new Document(element);	
        File f = new File(u);
        element.addContent(f.exportXml());
        System.out.println(f.exportXml());		
        element.addContent(u.exportXml());
        System.out.println(u.exportXml());		
        element.addContent(u1.exportXml());
        System.out.println(u1.exportXml());
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        try { xmlOutput.output(doc, new PrintStream(System.out));
        } catch (IOException e) { System.out.println(e); }
        System.out.println("FenixFramework's root object is: " + FenixFramework.getDomainRoot());
				
    }
}
