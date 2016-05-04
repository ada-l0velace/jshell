package pt.tecnico.myDrive.domain;

public class Extension extends Extension_Base {
    
    public Extension() {
        super();
    }

    /**
     * @// FIXME: 03/05/16 Implement is not done.
     * @param name
     */
    public Extension(String name, User owner, File file) {
        this();
        setName(name);
        setUser(owner);
        setFile(file);
    }
}
