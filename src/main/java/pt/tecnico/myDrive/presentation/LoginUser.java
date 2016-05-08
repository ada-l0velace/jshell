package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.LoginUserService;

import java.util.HashMap;
import java.util.Map;


public class LoginUser extends MyDriveCommand {

    public static Map cookies = new HashMap();

    public LoginUser(Shell sh) { super(sh, "login", "This command does a login and saves the token returned for future use."); }
    public void execute(String[] args) {
        if (args.length < 2)
            throw new RuntimeException("USAGE: "+name()+" <username> [<password>]");
        if (args.length >= 2) {
            cookies.put(args[0], new LoginUserService(args[0], args[1]).result());
        }
    }
}
