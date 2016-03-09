package pt.tecnico.myDrive;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import java.io.PrintStream;
import java.io.IOException;
import org.jdom2.output.Format;


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
        //User u = new User("Super User","root","rootroot", (short)1);
        //m.createUser(u);
        //m.getUser(u).exportXml();
        //Element element = new Element("mydrive");
        //Document doc = new Document(element);
        //for (User p: m.getUserSet())
        //element.addContent(u.exportXml());
        //System.out.println(u.exportXml());
        // XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        // try { xmlOutput.output(doc, new PrintStream(System.out));
        // } catch (IOException e) { System.out.println(e); }
        //System.out.println("FenixFramework's root object is: " + FenixFramework.getDomainRoot());
    }
}
