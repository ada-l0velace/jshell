package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.WritePlainFileService;

public class WriteFile extends MyDriveCommand {


    public WriteFile(Shell sh) { super(sh, "update", "This command updates a file content."); }

    public void execute(String[] args) {

        if (args.length >= 2){
            String path = args[0];
            String content = args[1];
            for(int x=2; x<args.length; x++){
                content= content + " " + args[x];
            }
            WritePlainFileService p = new WritePlainFileService(Token.token, path, content);
            p.execute();
            //System.out.println("Novo content: " + content);
        }

        else
            throw new RuntimeException("USAGE: " + name() + " <path> <text>");
    }
}
