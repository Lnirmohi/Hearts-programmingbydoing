/*
 * The game logic consists of  
 * The MainLoop class will run for each tr
 */
public class MainLoop{
    
    final int[] SEQUENCE1 = new int[]{ 0, 1, 2, 3 };
    final int[] SEQUENCE2 = new int[]{ 1, 2, 3, 0 };
    final int[] SEQUENCE3 = new int[]{ 2, 3, 0, 1 };
    final int[] SEQUENCE4 = new int[]{ 3, 0, 1, 2 };
    
    int[] sequence;
    
    void mainLoop( String firstPlayer, Player[] player ){    //playerWith2OfClub - name of player holding 2 OF CLUB
        
        for( int i = 0; i < 13; i++ ){
            
            sequence = decideSequence( firstPlayer, player );
            
            for( int j = 0; j < 4; j++ ){
                
                
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
}
