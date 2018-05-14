/*
 * Player holding 2 OF CLUB will begin the TRICK.
 * The game logic consists of 13 iteration of mainloop i.e trickLoop < 13. 
 * For each iteration of mainLoop, there is 4 iteration of trickLoop i.e innerloop < 4. 
 * Each iteration of trickLoop asks player to play a valid card.
 * First card of first GAME i.e when mainLoop & trickLoop both are 0, player should play 2 of CLUB.
 * First player can decide which suit to play except HEART.
 * HEART can be played only when it is broken i.e player's hand does not contain card
 * similar to suitInPlay or player is left with all HEART cards. 
 * Each time a card is played, it's suit value i.e SPADE, CLUB, HEART, DIAMOND is checked. 
 * All the players have to follow the suit played by first player. 
 * If the card played doesn't follow the suit in play i.e suitInPlay, player's hand is 
 * checked for if it contains suitInPlay or not.
 */
import java.util.*;

class LoopCount{

    int  mainLoopCount;
    int trickLoopCount;
}

public class MainLoop{
    
    Scanner loopScan = new Scanner(System.in);
    
    final int[] SEQUENCE1 = new int[]{ 0, 1, 2, 3 };
    final int[] SEQUENCE2 = new int[]{ 1, 2, 3, 0 };
    final int[] SEQUENCE3 = new int[]{ 2, 3, 0, 1 };
    final int[] SEQUENCE4 = new int[]{ 3, 0, 1, 2 };
    
    //sequence to follow for each player e.g if second player starts the trick sequence will be equal to SEQUENCE2.
    private int[] sequence;
    
    //container for card played in one trick
    private Cards[] handContainer = new Cards[4];
    
    //suit which is to be followed by players.
    private String suitInPlay = "CLUB";
    
    //boolean to check if heart is broken or not.
    private boolean heartBroken = false;
    
    void mainLoop(Player[] player, List<Cards> cards){

        heartBroken =  false;
        
        //finds first player for first time i.e when outerlopp = 0;
        String firstPlayer = has2OfCLUB(player);
        
        LoopCount loopCount = new LoopCount();
        
        for(loopCount.mainLoopCount = 0; loopCount.mainLoopCount < 13; loopCount.mainLoopCount++){
            
            //decides which sequence to follow
            sequence = decideSequence(firstPlayer, player);
            
            //runs 4 times each time a players turn to play a valid card
            trickLoop(loopCount, player);
            
            //decides who will start the next TRICK i.e player with card having highest value A being heighest and 2 being lowest. 
            firstPlayer = whoGetsPoints(player);
            
            showPointsDistribution(player);
            
            addingCardsBackToDeck(cards);
        }
    }
    
    String has2OfCLUB(Player[] player){
        
        for(int i = 0; i < 4; i++){
            
           if( player[i].playerHand.get(0).suit.equals("CLUB") &&  player[i].playerHand.get(0).value == 2 ) {
            
               System.out.println("\nThe 2 OF CLUB starts the game.  " + player[i].playerName + " play 2 OF CLUB.");
               
               return  player[i].playerName;
           }
        }
        
        return null;
    }
    
    int[] decideSequence(String firstPlayer, Player[] player){
        
        //return the sequence to be followed while playing the trick, each index decides player's no.
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
    
    void trickLoop(LoopCount loopCount, Player[]  player){
    
        for(loopCount.trickLoopCount = 0; loopCount.trickLoopCount < 4; loopCount.trickLoopCount++){/*==============TRICK=================*/
            
            //asks player to play a card.
            playCard(loopCount, player[sequence[loopCount.trickLoopCount]]);
            
            //card played by first player becomes suitInPlay.
            suitInPlay = handContainer[0].suit;
            
            //display notification after card is played.
            displayInGameNotifications();
            
            //show cards played in TRICK
            showHand(handContainer, loopCount.trickLoopCount);
        }
    }
    
    void playCard(LoopCount loopCount, Player currentPlayer){
        
        int cardPlayed;
        
        do{
            
            displayHand(currentPlayer.playerHand);
            
            if(currentPlayer.isPlayerHuman == true)
                cardPlayed = getCardNumber(-1, currentPlayer.playerName); //gets card number from player
            else{
                
                //if player is computer
                cardPlayed = selectCardsForComputer(currentPlayer, loopCount);
                System.out.print( "\n" + currentPlayer.playerName + "'s turn. Play a valid card: " + (cardPlayed+1));
                break;
            }
            
            //part below runs only if the player is not computer
            //first card of each GAME needs to be 2 of CLUB
            if(loopCount.mainLoopCount == 0 && loopCount.trickLoopCount == 0 && cardPlayed != 0){
            
                System.out.println("\nPlease play 2 of CLUB!"          );
                System.out.println("\nPlease play " + suitInPlay + "\n");
            }else if(loopCount.mainLoopCount == 0 && (currentPlayer.playerHand.get(cardPlayed).suit.equals("HEART") ||
                    (currentPlayer.playerHand.get(cardPlayed).suit.equals("SPADE") &&
                     currentPlayer.playerHand.get(cardPlayed).value == 12))){
            
                System.out.println( "\nA Heart or the Q of SPDADE cannot be played for first trick.");
            }else  if(validSuit( currentPlayer, cardPlayed, loopCount ))// if card is valid break the loop             
                break;
                
        }while(true);
        
        if(currentPlayer.playerHand.get(cardPlayed).suit.equals("HEART"))
            heartBroken = true;
        
        //card is removed from player's hand and added to handContainer array
        handContainer[loopCount.trickLoopCount] = currentPlayer.playerHand.remove( cardPlayed );
    }
    
    void displayHand(List<Cards> playerHand){
        
        for(int i = 0; i < playerHand.size(); i++)   
            System.out.println( (i+1) + ":\t" + playerHand.get(i) );
    }
    
    int getCardNumber(int cardPlayed, String playerName){  
        
        while( cardPlayed < 0 || cardPlayed > 12 ){
            
            System.out.print( "\n" + playerName + "'s turn. Play a valid card: ");
            
            //input validation
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
    
    boolean validSuit(Player player, int cardPlayed, LoopCount loopCount){
        
        boolean containsSuit = false;
        
        try{
            
            //checking if player has card of suit which is in play i.e suitInPlay if he plays card other than suitInPlay
            if( !player.playerHand.get(cardPlayed).suit.equals(suitInPlay) ){
                
                containsSuit = validCard( player, cardPlayed, loopCount);
                
                return containsSuit;
            }
        }catch(Exception e){
            
            System.out.println("Please enter valid card number!\n");
            
            return false;
        }
        
        return !containsSuit;
    }
    
    boolean validCard(Player player, int cardPlayed, LoopCount loopCount){
        
        if(loopCount.mainLoopCount == 0 && (player.playerHand.get(cardPlayed).suit.equals("HEART") ||
                                           (player.playerHand.get(cardPlayed).suit.equals("SPADE") && player.playerHand.get(cardPlayed).value == 12)))
            return false;
        else if(player.playerHand.get(cardPlayed).suit.equals("HEART") ){//If card played by player is HEART
            
            return checkForHEART(loopCount, player);
        }else if(!player.playerHand.get(cardPlayed).suit.equals(suitInPlay) && loopCount.trickLoopCount > 0){//trickLoopCount > 0 to not check first card unless it's HEART.

            //if player's hand contains card of suitInPlay returns false
            if(suitCheckingLoop(player, suitInPlay))
                return false;
        }
        
        return true;
    }
    
    boolean checkForHEART(LoopCount loopCount, Player player){
        
        //If first card played is HEART
        if(loopCount.trickLoopCount == 0){
            
            //If first card played is HEART and HEART is not broken
            if(heartBroken == false){
                
                //to check if player hand contains all HEART card
                for(int n = 0; n < player.playerHand.size(); n++){
                
                    if(!player.playerHand.get(n).suit.equals("HEART"))
                        return ifHeartIsNotBroken(player.isPlayerHuman);
                }
            }
        }else if(loopCount.trickLoopCount > 0){
        
            if(suitCheckingLoop(player, suitInPlay))
                return false;
        }
        
        if(player.isPlayerHuman == true)
            System.out.println("\n\t\t\t\tHeart is borken!");
        
        return true;
    }
    
    boolean suitCheckingLoop(Player player, String suitToCheck){
        
        for(int k = 0; k < player.playerHand.size(); k++){

            if(player.playerHand.get(k).suit.equals(suitToCheck)){
                
                if( player.isPlayerHuman == true)
                    System.out.println("\nPlease play " + suitInPlay + "!\n");
                
                return true;
            }
        }
        
        return false;
    }
    
    boolean ifHeartIsNotBroken(boolean isPlayerHuman){
        
        if(isPlayerHuman == true)
            System.out.println("\nHeart is not broken! You can't play a HEART unless it's broken.\n");
                
        return false;
    }
    
    void displayInGameNotifications(){
    
        System.out.println( "\n\t\t\t\tSuit in play: " + suitInPlay + "\n" );
        System.out.println( "\t\t\t\tHeart broken: "   + isHeartBroken()   ); 
        System.out.println( "\n\t\t\t\tCurrent Hand: ");
    }
    
    void showHand(Cards[] handContainer, int trickLoopCount){
    
        for( int i = 0; i <= trickLoopCount; i++ )  
            System.out.println( "\t\t\t\t" + handContainer[i]);
    }
    
    void showPointsDistribution(Player[] player){
        
        System.out.println("\n\t\t\t\t\tPoints");
        
        for( int i = 0; i < 4; i++ )
            System.out.println("\t\t\t\t"+ player[i].playerName + ": \t" + player[i].points);
    }
    
    //indicates if HEART is broken or not.
    String isHeartBroken(){ 
    
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
        
        //determines how many points to add to players point table;
        pointsInTheTrick = determinePoints(pointsInTheTrick);
        
        player[sequence[indexOfBiggestCard]].points += pointsInTheTrick;
        
        //returns name of player holding heighest card in each TRICK 
        return player[sequence[indexOfBiggestCard]].playerName;
    }
    
    int determinePoints(int pointsInTheTrick){
        
        for( int j = 0; j < 4; j++ ){
            
            //if card is of HEART suit one point is added and if card is Q of CLUB then 13 points are added.
            if( handContainer[j].suit.equals("HEART") || (handContainer[j].suit.equals("SPADE") && handContainer[j].value == 12) ){
                
                if( handContainer[j].suit.equals("HEART") )
                    pointsInTheTrick++;
                else if(handContainer[j].suit.equals("SPADE") && handContainer[j].value == 12 )
                    pointsInTheTrick += 13;
            }
        }
        
        return pointsInTheTrick;
    }
    
    void addingCardsBackToDeck(List<Cards> cards){
        
        for( int k = 0; k < 4; k++ )
            //cards which are played are added back to the deck.
            cards.add( handContainer[k] );
  
    }
    
    int selectCardsForComputer(Player player, LoopCount loopCount){
        
        Random random = new Random();
        
        List<Integer> validCardsForComputerToPlay = new ArrayList(13);
        
        if(loopCount.mainLoopCount == 0 && loopCount.trickLoopCount == 0)
            return 0;
            
        validCardsForComputerToPlay.clear();
        
        for(int i = 0; i < player.playerHand.size(); i++){
            
            if(validSuit(player, i, loopCount)){
            
                validCardsForComputerToPlay.add(i);
                //uncomment the belove line to see no of playable cards for computer player
                System.out.println(i+1);
            }
        }
        
        //size for random.nextInt
        int playableCards = validCardsForComputerToPlay.size();
        
        return validCardsForComputerToPlay.get(random.nextInt(playableCards));
    }
}