package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.LoginUserService;


public class LoginUser extends MyDriveCommand {

    public LoginUser(Shell sh) { super(sh, "login", "This command does a login and saves the token returned for future use."); }
    public void execute(String[] args) {
        if (args.length < 3)
            throw new RuntimeException("USAGE: "+name()+" <username> [<password>]");
        if (args.length > 3)
            System.out.println("FIXME");
        else
            System.out.println("FIXME");
    }
}
