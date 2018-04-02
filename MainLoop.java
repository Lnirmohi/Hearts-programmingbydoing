/*
 * The game logic consists of  
 * The MainLoop class will run for each tr
 */

import java.util.*;

public class MainLoop{
    
    Scanner loopScan = new Scanner(System.in);
    
    final int[] SEQUENCE1 = new int[]{ 0, 1, 2, 3 };
    final int[] SEQUENCE2 = new int[]{ 1, 2, 3, 0 };
    final int[] SEQUENCE3 = new int[]{ 2, 3, 0, 1 };
    final int[] SEQUENCE4 = new int[]{ 3, 0, 1, 2 };
    
    private int[] sequence;
    
    Cards[] handContainer = new Cards[4];
    
    void mainLoop( String firstPlayer, Player[] player, List<Cards> cards ){    //playerWith2OfClub - name of player holding 2 OF CLUB
        
        for( int i = 0; i < 13; i++ ){
            
            sequence = decideSequence( firstPlayer, player );
            
            for( int j = 0; j < 4; j++ ){
                
                playCard( j, player[sequence[j]] );
            }
            
            for( int j = 0; j < 4; j++ ){
                
                System.out.println( handContainer[j] );
                cards.add( handContainer[j] );
            }
        }
    }
    
    int[] decideSequence( String firstPlayer, Player[] player ){
        
        for( int i = 0; i < 4; i++ ){
            
            if( firstPlayer.equals( player[0].playerName ) )
                return SEQUENCE1;
            else if( firstPlayer.equals( player[1].playerName ) )
                return SEQUENCE2;
            else if( firstPlayer.equals( player[2].playerName ) )
                return SEQUENCE3;
            else if( firstPlayer.equals( player[3].playerName ) )
                return SEQUENCE4;
        }
        
        return null;
    }
    
    void playCard( int j, Player player ){

        displayHand( player.playerHand );
        
        System.out.print( "\n" + player.playerName + "'s turn. Play a valid card: ");
        
        handContainer[j] =  player.playerHand.remove( loopScan.nextInt() - 1 );
    }
    
    void displayHand( List<Cards> playerHand ) {
        
        for( int i = 0; i < playerHand.size(); i++ ) {
            
            System.out.println( (i+1) + ":\t" + playerHand.get(i) );            
        }
    }
    
    boolean validCard() {
        
        return true;
    }
}
