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
    
    private Cards[] handContainer = new Cards[4];
    
    private String suitInPlay = "CLUB";//suit which is to followed by players
    
    private boolean heartBroken = false;//boolean to check if heart is broken
    
    void mainLoop( String firstPlayer, Player[] player, List<Cards> cards ){    //playerWith2OfClub - name of player holding 2 OF CLUB
        
        for( int i = 0; i < 13; i++ ){
            
            sequence = decideSequence( firstPlayer, player );

            for( int j = 0; j < 4; j++ ){
                
                playCard( i, j, player[sequence[j]] );
                
                suitInPlay = handContainer[0].suit;
                
                System.out.println( "\n\t\t\t\tSuit in play: " + suitInPlay + "\n" );
                System.out.println( "\t\t\t\tCurrent Hand: ");
                showHand( handContainer, j );
            }
            
            for( int k = 0; k < 4; k++ ){

                cards.add( handContainer[k] );
            }
            
            firstPlayer = whoGetsPoints(player, sequence);
        }
    }
    
    void showHand( Cards[] handContainer, int j ){
    
        for( int i = 0; i <= j; i++ ){
            
            System.out.println( "\t\t\t\t" + handContainer[i] );
        }
        
        System.out.println();
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
    
    void playCard( int i, int j, Player currentPlayer ){
        
        int cardPlayed;
        
        do{
            
            displayHand( currentPlayer.playerHand );
            
            cardPlayed = getCardNumber( -1, currentPlayer.playerName );
            
            if( validSuit( currentPlayer.playerHand, cardPlayed, i, j) ){
                
                break;
            }else
                continue;

        }
        while(true);
        
        handContainer[j] = currentPlayer.playerHand.remove( cardPlayed );//card is removed from player's hand and added to handContainer array
    }
    
    int getCardNumber( int cardPlayed, String playerName ){  
        
        while( cardPlayed < 0 || cardPlayed > 12 ){
            
            System.out.print( "\n" + playerName + "'s turn. Play a valid card: ");
            
            try{
                    
                cardPlayed = loopScan.nextInt() - 1;
            }catch( Exception e ){
                
                System.out.println( "Please enter valid number(1-13):" );
                loopScan.nextLine();
                continue;
             }
                
            loopScan.nextLine();
        }
        
        return cardPlayed;
    }
    
    void displayHand( List<Cards> playerHand ){
        
        for( int i = 0; i < playerHand.size(); i++ ){
            
            System.out.println( (i+1) + ":\t" + playerHand.get(i) );
        }
    }
    
    String whoGetsPoints( Player[] player, int[] sequence ){
        
        int largest = handContainer[0].value;
        int indexOfBiggestCard = 0;
        
        for( int i = 1; i < 4; i++ ){
            
            //suit played by first player has to be followed by other players unless they
            //dont have card that belongs to suit played by first player
            if( handContainer[0].suit.equals(handContainer[i].suit) && handContainer[i].value > largest ){
                
                largest = handContainer[i].value;
                indexOfBiggestCard = i;
            }
        }
        
        return player[sequence[indexOfBiggestCard]].playerName;
    }
    
    boolean validSuit( List<Cards> playerHand, int cardPlayed, int i, int j ){
        
        boolean containsSuit = false;
        
        try{
            
            if( !playerHand.get(cardPlayed).suit.equals(suitInPlay) && j > 0 ){//checking if player has card of suit which is in play i.e suitInPlay
                
                containsSuit = validCard( playerHand, cardPlayed, i, j);
                
                for( int k = 0; k < playerHand.size(); k++ ){

                    if( playerHand.get(k).suit.equals(suitInPlay) ){
                    
                        System.out.println("Please play " + suitInPlay + "\n");
                        return containsSuit;
                    }
                }
            }
        }catch( Exception e ){
            
            System.out.println("Please enter valid card number!\n");
            return false;
        }
        
        return !containsSuit;
    }
    
    boolean validCard( List<Cards> playerHand, int cardPlayed, int i, int j ){
        
        if( i == 0 && j == 0 && cardPlayed != 0 ){
            
            System.out.println("\nPlease play 2 of CLUB!");
            
            return false;
        }else if( !playerHand.get(cardPlayed).suit.equals("HEARTS") ){
           
            for( int k = 0; k < playerHand.size(); k++ ){

                if( playerHand.get(k).suit.equals(suitInPlay) ){
                    
                    System.out.println("Please play " + suitInPlay + "\n");
                    return false;
                }
            }
        }
        return true;
    }
}
