package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.interfaces.IElementXml;
import org.jdom2.Element;


public class User extends User_Base implements IElementXml {
    
    private Element _xml;

    public User() {
        super();
    }

    public User(String name,String password, Short umask) {
        super();
        setName(name);
        setPassword(password);
        setPermissions(new Permissions());
    }

    public Element getXml() {
        return _xml;
    }
    
    @Override
    public void importXml () {
        Element node = _xml;
        String name = node.getAttribute("name").getValue();
        String username = node.getAttribute("username").getValue();
        String password = node.getAttribute("password").getValue();
        Element permission = node.getChild("permissions");
        short umask = Short.parseShort(permission.getAttribute("umask").getValue());
        
        setName(new String(name));
        setUsername(new String(username));
        setPassword(new String(password));
        setPermissions(new Permissions(umask));
    }

    @Override
    public void exportXml () {
        throw new UnsupportedOperationException("Not Implemented!");
    }

}
