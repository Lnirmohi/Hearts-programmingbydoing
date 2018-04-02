import java.util.*;

public class Player
{
    String playerName;
    int points;
    List<Cards> playerHand = new ArrayList<Cards>(13);
    
    Player( String playerName, int points ) {
    
        this.playerName = playerName;
        this.points     = points;
    }
    
    public String toString() {
        
        return playerName + ": " + points;
    }
}
