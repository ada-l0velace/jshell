package pt.tecnico.myDrive.presentation;

/**
 * Created by lolstorm on 26/04/16.
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;


public class Token extends MyDriveCommand {

    public static String token;

    public Token(Shell sh) {
        super(sh, "token", "The interpreter can keep several user sessions active simultaneously. The token commands" +
                " allows the interpreter to switch between active sessions. The command prints the token of current " +
                "active user. When invoked without arguments, it prints the token and username of current active " +
                "user. When a user is given, it changes the current active user to the given user, updates the " +
                "current token accordingly and prints its value.");
    }

    public void execute(String[] args) {
    	if(args.length == 0){
    		String key= null;
            String value= token;
            /*for (Object o : LoginUser.cookies.keySet()) {
                if (LoginUser.cookies.get((String) o).equals(value)) {
                  key = (String) o;
                  break;
                }
              }*/
            System.out.println(key + ": " + value);
            }
        if (args.length > 1)
            throw new RuntimeException("USAGE: " + name() + " [<username>]");
        if (args.length == 1)
            token = (String) LoginUser.cookies.get(args[0]);
        	System.out.println(args[0] + ": " + token);
    }
}
