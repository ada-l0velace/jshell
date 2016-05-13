package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ChangeDirectoryService;


public class ChangeDirectory extends MyDriveCommand {

    public ChangeDirectory(Shell sh) { super(sh, "cwd", "This command changes the current directory."); }
    public void execute(String[] args) {
        if (args.length < 1)
            throw new RuntimeException("USAGE: "+name()+" [<path>]");
        else{
			ChangeDirectoryService s = new ChangeDirectoryService(Token.token, args[0]);
			s.execute();
		}
	}
}
