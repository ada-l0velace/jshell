package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.WritePlainFileService;


public class WriteFile extends MyDriveCommand {

    public WriteFile(Shell sh) { super(sh, "update", "This command updates a file content."); }
    public void execute(String[] args) {
        if (args.length < 3)
            throw new RuntimeException("USAGE: "+name()+"update <path> <text>");
        if (args.length > 3)
            System.out.println("FIXME");
        else
            System.out.println("FIXME");
    }
}
