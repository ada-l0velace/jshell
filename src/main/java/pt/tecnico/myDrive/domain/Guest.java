package pt.tecnico.myDrive.domain;

public class Guest extends Guest_Base {
    
    public Guest() {
        super();
    }

    /**
     * Function to set the token live time of the user
     * @return the time in minutes of live time of the token.
     */
    @Override
    public int tokenTimeExpiration() {
        return Integer.MAX_VALUE;
    }
}
