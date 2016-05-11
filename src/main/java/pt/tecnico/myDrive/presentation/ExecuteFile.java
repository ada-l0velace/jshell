package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ExecuteFileService;

import java.util.Arrays;

public class ExecuteFile extends MyDriveCommand {
	
    public ExecuteFile(Shell sh) { super(sh, "do", "This command executes a file with the args."); }
    
    public void execute(String[] args) {
        if (args.length < 1)
            throw new RuntimeException("USAGE: "+name()+" <path> [<args>]");
        else if (args.length > 1)
        {
        	String[] newArray = Arrays.copyOfRange(args, 1, args.length);
        	new ExecuteFileService(Token.token, args[0], newArray);
        }
        else
        {
        	new ExecuteFileService(Token.token, args[0]);
        }
    }
}
