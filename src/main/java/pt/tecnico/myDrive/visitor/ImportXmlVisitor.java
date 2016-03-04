package pt.tecnico.myDrive.visitor;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.PlainFile;

public class ImportXmlVisitor implements IVisitorImportXml {
    public void importXml(User user) {
        throw new UnsupportedOperationException("User");
    }

    public void importXml(File file) {
        throw new UnsupportedOperationException("File");
    }

    public void importXml(Directory directory) {
        throw new UnsupportedOperationException("Directory");
    }
    public void importXml(App app) {
        throw new UnsupportedOperationException("App");
    }
    public void importXml(Link link) {
        throw new UnsupportedOperationException("Link");
    }
    public void importXml(PlainFile plain_file) {
        throw new UnsupportedOperationException("PlainFile");
    }
}