/*
 * The game logic consists of 13 iteration of mainloop i.e i < 13.
 * 
 * For each iteration of i, there is 4 iteration of innerloop i.e j < 4.
 * 
 * Each iteration of innerloop j asks player to play a valid card.
 * 
 * First card of first GAME i.e when i&j both are 0, player should play 2 of CLUB.
 * 
 * First player can decide which suit to play except HEART.
 * 
 * HEART can be played only when it is broken i.e player's hand does not contain card
 * similar to suitInPlay or player is left with all HEART cards.
 * 
 * Each time a card is played, it's suit value i.e SPADE, CLUB, HEART, DIAMOND is checked.
 * 
 * All the players have to follow the suit played by first player.
 * 
 * If the card played doesn't follow the suit in play i.e suitInPlay, player's hand is 
 * checked for if it contains suitInPlay or not.
 * 
 * handContainer is the container for card played in one trick.
 */

import java.util.*;

public class MainLoop{
    
    Scanner loopScan = new Scanner(System.in);
    
    final int[] SEQUENCE1 = new int[]{ 0, 1, 2, 3 };
    final int[] SEQUENCE2 = new int[]{ 1, 2, 3, 0 };
    final int[] SEQUENCE3 = new int[]{ 2, 3, 0, 1 };
    final int[] SEQUENCE4 = new int[]{ 3, 0, 1, 2 };
    
    private int[] sequence;//sequence to follow for each player.
    
    private Cards[] handContainer = new Cards[4];
    
    private String suitInPlay = "CLUB";//suit which is to be followed by players.
    
    private boolean heartBroken = false;//boolean to check if heart is broken or not.
    
    void mainLoop( String firstPlayer, Player[] player, List<Cards> cards ){
        
        for( int i = 0; i < 13; i++ ){/*==============GAME=================*/
            
            sequence = decideSequence( firstPlayer, player );

            for( int j = 0; j < 4; j++ ){/*==============TRICK=================*/
                
                playCard( i, j, player[sequence[j]] );//asks player to play a card.
                
                suitInPlay = handContainer[0].suit;//card played by first player becomes suitInPlay.
                
                System.out.println( "\n\t\t\t\tSuit in play: " + suitInPlay + "\n" );
                System.out.println( "\t\t\t\tHeart broken: " + isHeartBroken() ); 
                System.out.println( "\n\t\t\t\tCurrent Hand: ");
                
                showHand( handContainer, j );//show cards played in TRICK
            }
            
            for( int k = 0; k < 4; k++ ){

                cards.add( handContainer[k] );//cards which are played are added back to the deck.
            }
            
            firstPlayer = whoGetsPoints(player, sequence);//decides who will start the next TRICK i.e player with card having highest value A being heighest and 2 being lowest. 
        }
    }
    
    String isHeartBroken(){ //indicates if HEART is broken or not.
    
        if( heartBroken == true )
            return "Yes";
        else
            return  "No";
    }
    
    void showHand( Cards[] handContainer, int j ){
    
        for( int i = 0; i <= j; i++ )  
            System.out.println( "\t\t\t\t" + handContainer[i] );

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
            
            cardPlayed = getCardNumber( -1, currentPlayer.playerName );//gets card numeber from player
            
            if( i == 0 && j == 0 && cardPlayed != 0 ){//first card of each GAME needs to be 2 of CLUB
            
                System.out.println("\nPlease play 2 of CLUB!"          );
                System.out.println("\nPlease play " + suitInPlay + "\n");
            }
            else if( validSuit( currentPlayer.playerHand, cardPlayed, i, j) )               
                break;
        }while(true);
        
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
        
        for( int i = 0; i < playerHand.size(); i++ )   
            System.out.println( (i+1) + ":\t" + playerHand.get(i) );
    }
    
    String whoGetsPoints( Player[] player, int[] sequence ){
        
        int largest = handContainer[0].value;
        int indexOfBiggestCard = 0;
        
        for( int i = 1; i < 4; i++ ){
            
            //suit played by first player has to be followed by other players unless they
            //dont have card that belongs to suit played by first player i.e suitInPlay
            if( handContainer[0].suit.equals(handContainer[i].suit) && handContainer[i].value > largest ){
                
                largest = handContainer[i].value;
                
                indexOfBiggestCard = i;
            }
        }
        
        return player[sequence[indexOfBiggestCard]].playerName;//returns name of player holding heighest card in each TRICK 
    }
    
    boolean validSuit( List<Cards> playerHand, int cardPlayed, int i, int j ){
        
        boolean containsSuit = false;
        
        try{
            
            if( !playerHand.get(cardPlayed).suit.equals(suitInPlay) ){//checking if player has card of suit which is in play i.e suitInPlay
                
                containsSuit = validCard( playerHand, cardPlayed, i, j);
                
                return containsSuit;
            }
        }catch( Exception e ){
            
            System.out.println("Please enter valid card number!\n");
            
            return false;
        }
        
        return !containsSuit;
    }
    
    boolean validCard( List<Cards> playerHand, int cardPlayed, int i, int j ){
        /*
         * If card played by player is not HEART and of different suit than suitInPlay.
         * j < 0 is to make sure that this condition will not check
         * for first player because he will decide the suitInPlay.
         */
        if( !playerHand.get(cardPlayed).suit.equals(suitInPlay) && !playerHand.get(cardPlayed).suit.equals("HEART") && j > 0 ){
            
            //if player's hand contains card of suitInPlay returns false
            if( suitCheckingLoop(playerHand, suitInPlay) ){
                
                return false;
            }
        }else if( playerHand.get(cardPlayed).suit.equals("HEART") ){//If card played by player is HEART
            
            if( j == 0 ){//If first card played is HEART
                
                if( heartBroken == false ){
                    
                    //to check if player hand contains all HEART card
                    for( int n = 0; n < playerHand.size(); n++ ){
                    
                        if( !playerHand.get(n).suit.equals("HEART") )
                            return heartNotification();
                    }
                    
                    System.out.println("\nHeart is borken!");
                }
            }else if( j > 0 ){
            
                if( suitCheckingLoop(playerHand, suitInPlay) ){
                    
                    return false;
                }
                
                System.out.println("\n\t\t\t\tHeart is borken!");
            }
            
            heartBroken = true;
        }
        
        return true;
    }
    
    boolean suitCheckingLoop( List<Cards> playerHand, String suitToCheck ){
    
        for( int k = 0; k < playerHand.size(); k++ ){

            if( playerHand.get(k).suit.equals(suitToCheck) ){
                    
                System.out.println("\nPlease play " + suitInPlay + "!\n");
                
                return true;
            }
        }
        
        return false;
    }
    
    boolean heartNotification(){
    
        System.out.println("\nHeart is not broken! You can't play a HEART unless it's broken.\n");
                
        return false;
    }
}
