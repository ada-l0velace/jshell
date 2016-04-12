package pt.tecnico.myDrive.service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.ist.fenixframework.Atomic;
import pt.tecnico.myDrive.exception.MyDriveException;

/**
 * Created by lolstorm on 08/04/16.
 */
public abstract class MyDriveService  {
    static final Logger log = LogManager.getRootLogger();

    @Atomic
    public final void execute() throws MyDriveException {
        dispatch();
    }

    protected abstract void dispatch() throws MyDriveException;
}
