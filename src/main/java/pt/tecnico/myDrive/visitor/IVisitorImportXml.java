package pt.tecnico.myDrive.visitor;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.PlainFile;

public interface IVisitorImportXml {
    public void importXml(User file);
    public void importXml(File file);
    public void importXml(Directory file);
    public void importXml(App file);
    public void importXml(Link link);
    public void importXml(PlainFile file);
}
