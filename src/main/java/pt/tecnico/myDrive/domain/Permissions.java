package pt.tecnico.myDrive.domain;

public class Permissions extends Permissions_Base {
    
    /**
     * Default constructor
     */
    public Permissions() {
        super();
    }

    /**
     * Alternate constructor for Permissions with Umask
     * @param umask represents the numeric user mask.
     */
    public Permissions(short umask) {
        super();
        setUmask(umask);
    }

    /**
     * Returns the byte position
     * @param  position (int) Which represents the position of the byte.
     * @return byte which represents the byte on that position.         
     */
    public byte getBit(int position) {
       
       return (byte)((getUmask() >> position) & 1);
    }

    /**
     * @return true if can write the file, false otherwise.
     */
    public boolean worldCanWrite() {
        return (getBit(2) == 1) ? true : false;
    }

    /**
     * Permission table
     * @param  position (int) Which represents the position of the byte.
     * @return char represents the string form of that byte. 
     */
    public char getBitToChar(int position) {
       char permission;
       switch (position) {
            case 0:  permission = readToString(getBit(7));
                     break;
            case 1:  permission = writeToString(getBit(6));
                     break;
            case 2:  permission = executeToString(getBit(5));
                     break;
            case 3:  permission = deleteToString(getBit(4));
                     break;
            case 4:  permission = readToString(getBit(3));
                     break;
            case 5:  permission = writeToString(getBit(2));
                     break;
            case 6:  permission = executeToString(getBit(1));
                     break;
            case 7:  permission = deleteToString(getBit(0));
                     break;
            default: permission = '\0';
                     break;
        }
        return permission;
    }    

    /**
     * Returns the string representation of a byte.
     * @param  b (byte) which represents the a byte.
     * @return  char represents the string form of that byte.
     */
    public char readToString (byte b) {
        return (b == 1) ? 'r' : '-';
    }

    /**
     * Returns the string representation of a byte.
     * @param  b (byte) which represents the a byte.
     * @return  char represents the string form of that byte.
     */
    public char writeToString (byte b) {
        return (b == 1) ? 'w' : '-';
    }

    /**
     * Returns the string representation of a byte.
     * @param  b (byte) which represents the a byte.
     * @return  char represents the string form of that byte.
     */
    public char executeToString (byte b) {
        return (b == 1) ? 'x' : '-';
    }

    /**
     * Returns the string representation of a byte.
     * @param  b (byte) which represents the a byte.
     * @return  char represents the string form of that byte.
     */
    public char deleteToString (byte b) {
        return (b == 1) ? 'd' : '-';
    }

    /**
     * toString override for unix permission style representation.
     * @return String which is the final output representation.
     */
    public String toString() {
        String pa = "";
        for (int i=0; i <= 7; i++ )
            pa += getBitToChar(i);
        
        return pa;
    }
    
}
