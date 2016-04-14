package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 14/04/16.
 */
public class LinkEmptyContentException extends MyDriveException {
        private static final long serialVersionUID = 1L;

        public LinkEmptyContentException() {
            super();
        }

        @Override
        public String getMessage() {
            return "You can't create a link with empty content!";
        }
}
