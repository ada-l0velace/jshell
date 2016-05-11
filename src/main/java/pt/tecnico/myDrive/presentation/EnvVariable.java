package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.EnvironmentVariableService;

public class EnvVariable extends MyDriveCommand 
{

    public EnvVariable(Shell sh) { super(sh, "env", "Creates or modifies an environment variable."); }

    public void execute(String[] args) 
    {
        if (args.length > 2 || args.length < 2)
            throw new RuntimeException("USAGE: "+name()+" [<name> [<value>]]");
        else
        {
            if (args[0].equals("["))
            {
                if(args[1].equals("[]]"))
                {
                    EnvironmentVariableService service = new EnvironmentVariableService(Token.token);
                    System.out.println(service.outputAll());
                }
                else
                {
                    throw new RuntimeException("USAGE: "+name()+" [<name> [<value>]]");
                }
            }
            else
            {
                if(args[1].equals("[]]"))
                {
                    String arg0 = args[0].substring(1, args[0].length());
                    EnvironmentVariableService service = new EnvironmentVariableService(Token.token, arg0);
                    System.out.println(service.output());
                }
                else
                {
                    String arg0 = args[0].substring(1, args[0].length());
                    String arg1 = args[1].substring(1, args[1].length() - 2);
                    EnvironmentVariableService service = new EnvironmentVariableService(Token.token, arg0, arg1);
                    service.execute();
                }
            }
        }
    }
}
