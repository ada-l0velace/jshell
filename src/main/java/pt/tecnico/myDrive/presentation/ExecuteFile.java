package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ExecuteFileService;


public class ExecuteFile extends MyDriveCommand {

    public ExecuteFile(Shell sh) { super(sh, "do", "This command executes a file with the args."); }
    public void execute(String[] args) {
        if (args.length < 1)
            throw new RuntimeException("USAGE: "+name()+" <path> [<args>]");
        if (args.length > 1)
            System.out.println("FIXME");
        else
            System.out.println("FIXME");
    }
}
