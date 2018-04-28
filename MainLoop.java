/*
 * The game logic consists of 13 iteration of mainloop i.e outerloop < 13.
 * 
 * For each iteration of outerloop, there is 4 iteration of innerloop i.e innerloop < 4.
 * 
 * Each iteration of innerloop asks player to play a valid card.
 * 
 * First card of first GAME i.e when outerloop & innerloop both are 0, player should play 2 of CLUB.
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
    
    void mainLoop(Player[] player, List<Cards> cards){
        
        String firstPlayer = has2OfCLUB(player);//finds first player for first time i.e when outerlopp = 0;
        
        for(int outerloop = 0; outerloop < 13; outerloop++){/*==============GAME=================*/
            
            sequence = decideSequence(firstPlayer, player);//decides which sequence to follow
            
            trickLoop(outerloop, player);
            
            firstPlayer = whoGetsPoints(player);//decides who will start the next TRICK i.e player with card having highest value A being heighest and 2 being lowest. 
            
            showPointsDistribution(player);
            
            addingCardsBackToDeck(cards);
        }
    }
    
    String has2OfCLUB(Player[] player){
        
        for(int i = 0; i < 4; i++){
            
           if( player[i].playerHand.get(0).suit.equals("CLUB") &&  player[i].playerHand.get(0).value == 2 ) {
            
               notificationFor2OfCLUB( player[i].playerName );
               
               return  player[i].playerName;
           }
        }
        
        return null;
    }
    
    void notificationFor2OfCLUB(String player){
        
        System.out.println("\nThe 2 OF CLUB starts the game.  " + player + " play 2 OF CLUB.");
    }
    
    int[] decideSequence(String firstPlayer, Player[] player){
        
        for(int i = 0; i < 4; i++){
            
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
    
    void trickLoop(int outerloop, Player[]  player){
    
        for(int innerloop = 0; innerloop < 4; innerloop++){/*==============TRICK=================*/
                
            playCard(outerloop, innerloop, player[sequence[innerloop]]);//asks player to play a card.
                
            suitInPlay = handContainer[0].suit;//card played by first player becomes suitInPlay.
                
            displayInGameNotifications();//display notification after card is played.
                
            showHand(handContainer, innerloop);//show cards played in TRICK
        }
    }
    
    void playCard(int outerloop, int innerloop, Player currentPlayer){
        
        int cardPlayed;
        
        do{
            
            displayHand(currentPlayer.playerHand);
            
            cardPlayed = getCardNumber(-1, currentPlayer.playerName);//gets card numeber from player
            
            if(outerloop == 0 && innerloop == 0 && cardPlayed != 0){//first card of each GAME needs to be 2 of CLUB
            
                System.out.println("\nPlease play 2 of CLUB!"          );
                System.out.println("\nPlease play " + suitInPlay + "\n");
            }
            else if(validSuit( currentPlayer.playerHand, cardPlayed, innerloop ))// if card is valid break the loop             
                break;
                
        }while(true);
        
        handContainer[innerloop] = currentPlayer.playerHand.remove( cardPlayed );//card is removed from player's hand and added to handContainer array
    }
    
    void displayHand(List<Cards> playerHand){
        
        for(int i = 0; i < playerHand.size(); i++)   
            System.out.println( (i+1) + ":\t" + playerHand.get(i) );
    }
    
    int getCardNumber(int cardPlayed, String playerName){  
        
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
    
    boolean validSuit( List<Cards> playerHand, int cardPlayed, int innerloop ){
        
        boolean containsSuit = false;
        
        try{
            
            if( !playerHand.get(cardPlayed).suit.equals(suitInPlay) ){//checking if player has card of suit which is in play i.e suitInPlay
                
                containsSuit = validCard( playerHand, cardPlayed, innerloop);
                
                return containsSuit;
            }
        }catch(Exception e){
            
            System.out.println("Please enter valid card number!\n");
            
            return false;
        }
        
        return !containsSuit;
    }
    
    boolean validCard(List<Cards> playerHand, int cardPlayed, int innerloop){
        
        //If card played by player is not HEART and of different suit than suitInPlay.
        if( playerHand.get(cardPlayed).suit.equals("HEART") ){//If card played by player is HEART
            
            if( innerloop == 0 ){//If first card played is HEART
                
                if( heartBroken == false ){
                    
                    //to check if player hand contains all HEART card
                    for( int n = 0; n < playerHand.size(); n++ ){
                    
                        if( !playerHand.get(n).suit.equals("HEART") )
                            return ifHeartIsNotBroken();
                    }
                    
                    System.out.println("\nHeart is borken!");
                }
            }else if( innerloop > 0 ){
            
                if( suitCheckingLoop(playerHand, suitInPlay) ){
                    
                    return false;
                }
                
                System.out.println("\n\t\t\t\tHeart is borken!");
            }
            
            heartBroken = true;
        }else if( !playerHand.get(cardPlayed).suit.equals(suitInPlay) && innerloop > 0 ){//innerloop > 0 to not check first card unless it's HEART.
            
            //if player's hand contains card of suitInPlay returns false
            if( suitCheckingLoop(playerHand, suitInPlay) ){
                
                return false;
            }
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
    
    boolean ifHeartIsNotBroken(){
    
        System.out.println("\nHeart is not broken! You can't play a HEART unless it's broken.\n");
                
        return false;
    }
    
    void displayInGameNotifications(){
    
        System.out.println( "\n\t\t\t\tSuit in play: " + suitInPlay + "\n" );
        System.out.println( "\t\t\t\tHeart broken: "   + isHeartBroken()   ); 
        System.out.println( "\n\t\t\t\tCurrent Hand: ");
    }
    
    void showHand( Cards[] handContainer, int innerloop ){
    
        for( int i = 0; i <= innerloop; i++ )  
            System.out.println( "\t\t\t\t" + handContainer[i]);
    }
    
    void showPointsDistribution(Player[] player){
        
        System.out.println("\n\t\t\t\t\tPoints");
        
        for( int i = 0; i < 4; i++ )
            System.out.println("\t\t\t\t"+ player[i].playerName + ": \t" + player[i].points);
    }
    
    String isHeartBroken(){ //indicates if HEART is broken or not.
    
        if( heartBroken == true )
            return "Yes";
        else
            return  "No";
    }
    
    String whoGetsPoints(Player[] player){
        
        int largest = handContainer[0].value;
        
        int indexOfBiggestCard = 0;
        int pointsInTheTrick   = 0;
        
        for( int i = 1; i < 4; i++ ){
            
            //suit played by first player has to be followed by other players unless they
            //dont have card that belongs to suit played by first player i.e suitInPlay
            if( handContainer[0].suit.equals(handContainer[i].suit) && handContainer[i].value > largest ){//handContainer[0].suit is suit of card played by first player.
                
                largest = handContainer[i].value;
                
                indexOfBiggestCard = i;
            }
        }
        
        pointsInTheTrick = determinePoints(pointsInTheTrick);//determines how many points to add to players point table;
        
        player[sequence[indexOfBiggestCard]].points += pointsInTheTrick;
        
        return player[sequence[indexOfBiggestCard]].playerName;//returns name of player holding heighest card in each TRICK 
    }
    
    int determinePoints(int pointsInTheTrick){
        
        for( int j = 0; j < 4; j++ ){
            
            //if card is of HEART suit one point is added and if card is Q of CLUB then 13 points are added.
            if( handContainer[j].suit.equals("HEART") || (handContainer[j].suit.equals("CLUB") && handContainer[j].value == 12) ){
                
                if( handContainer[j].suit.equals("HEART") )
                    pointsInTheTrick++;
                else if(handContainer[j].suit.equals("CLUB") && handContainer[j].value == 12 )
                    pointsInTheTrick += 13;
            }
        }
        
        return pointsInTheTrick;
    }
    
    void addingCardsBackToDeck(List<Cards> cards){
        
        for( int k = 0; k < 4; k++ ){

            cards.add( handContainer[k] );//cards which are played are added back to the deck.
        }
    }
}