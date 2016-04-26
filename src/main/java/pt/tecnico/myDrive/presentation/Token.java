package pt.tecnico.myDrive.presentation;

/**
 * Created by lolstorm on 26/04/16.
 */
public class Token extends MyDriveCommand {

    public Token(Shell sh) {
        super(sh, "token", "The interpreter can keep several user sessions active simultaneously. The token commands allows the interpreter to switch between active sessions. The command prints the token of current active user. When invoked without arguments, it prints the token and username of current active user. When a user is given, it changes the current active user to the given user, updates the current token accordingly and prints its value.");
    }

    public void execute(String[] args) {
        if (args.length < 3)
            throw new RuntimeException("USAGE: " + name() + " [<username>]");
        if (args.length > 3)
            System.out.println("FIXME");
        else
            System.out.println("FIXME");
    }
}
