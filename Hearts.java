/*
 * Player holding 2 OF CLUB will begin the TRICK.
 * Player[] array consists of three values i.e name of player, his/her points, cards in hand.
 * Sequence[] arrays are used for determining next player in sequence.
 * Not using subList to create each player hands because it only creates view and clearing cards List
 * gives concurrentmodification exception.
 */
import java.util.*;

public class Hearts{
    
    //deck of 52 cards
    private List<Cards> cards = new ArrayList<Cards>(52);
    
    //player objects of type Player initilized with names and 0 pts
    private Player player1 = new Player( "SOUTH", 0 );
    private Player player2 = new Player( "WEST" , 0 );
    private Player player3 = new Player( "NORTH", 0 );
    private Player player4 = new Player( "EAST" , 0 );

    //default player array names: SOUTH WEST NORTH EAST
    private Player[] player = new Player[]{ player1, player2, player3, player4 };
    
    Scanner scan =  new Scanner(System.in);
    
    public static void main(String[] args){
        
        Hearts hearts = new Hearts(); 
        
        hearts.initializeGame();
    }
    
    void initializeGame(){
    
        enterPlayersName(inputHowManyPlayers());
        
        Deck deck = new Deck();
        
        //fills a deck with cards
        deck.fillDeckWithCards(cards);
        
        startGameLoop(deck);
    }
    
    void startGameLoop(Deck deck){
        
        MainLoop loop = new MainLoop();
        
        do{
            //shuffles deck
            cards = deck.shuffleCards(cards);
            
            //distributes card among players and sort hand of each
            //player with the help of Collection.sort and comparator interface.
            distributeCards();
            
            //sorting each player's hand
            sortHand();
            
            //clear the card list to accumlate 12 cards passed by players. 
            cards.clear();
            
            //ask players to pass cards to players on their left.
            choseCardsToPass();
            
            //adding passed cards to players
            passCards();
            
            //sorting each player's hand
            sortHand();
            
            //remove 12 passed cards
            cards.clear();
            
            loop.mainLoop(player, cards);
            
        }while( player1.points < 100 && player2.points < 100 && player3.points < 100 && player4.points < 100 );
        
        whoWonTheGame();
    }
    
    int inputHowManyPlayers(){
        
        int noOfPlayers = -1;
        
        //input validation
        while(noOfPlayers < 1 || noOfPlayers > 4){
            
            System.out.println( "\nChoose how many players will play(1-4): " );
            
            ///input validation
            try{
                 
                noOfPlayers = scan.nextInt();
            }
            catch( Exception e ) {
                
                System.out.println( "Please enter a number form 1-4!" );
                scan.nextLine();
                
                continue;
            }
            
            scan.nextLine();
        }
        
        return noOfPlayers;
    }
    
    void enterPlayersName(int noOfPlayers){
    
        System.out.println("\nEnter player names!");
        
        for( int i = 0; i < noOfPlayers; i++ ) {
            
            System.out.print("\nEnter " + (i+1) + " player's name : ");
            player[i].playerName = scan.nextLine().toUpperCase();
        } 
        
        System.out.println( "\n\nPlayer 1: " + player[0].playerName + "\nPlayer 2: " + player[1].playerName +
                              "\nPlayer 3: " + player[2].playerName + "\nPlayer 4: " + player[3].playerName + "\n" );
    }
    
    void whoWonTheGame(){
    
        int lowestPoints = player[0].points;
        String winner = "";
        
        for( int i = 1; i < 4; i++ ){
        
            if( player[i].points < lowestPoints ){
            
                winner = player[i].playerName;
            }
        }
        
        System.out.println( "\n\t\t\t\t\t\t\t" + winner + " won the game!" );
    }
    
    void distributeCards() {
        
        //distributes cards among players
        for( int i = 0; i < 52; ) {
                
            player[0].playerHand.add( cards.get(i++) );   player[1].playerHand.add( cards.get(i++) );
            player[2].playerHand.add( cards.get(i++) );   player[3].playerHand.add( cards.get(i++) );
        }
    }
    
    void displayHand( List<Cards> playerHand ) {
        
        for( int i = 0; i < playerHand.size(); i++ ) {
            
            System.out.println( (i+1) + ":\t" + playerHand.get(i) );
        }
    }
    
    void choseCardsToPass(){

        System.out.println( "\nPass 3 cards to player on your left\n" );
        
        for( int i = 0; i < 4; i++ ){
            
            int firstCard = 1, secondCard = 1, thirdCard = 1;//cards that are going to be passed to next player i.e on left
            
            if( i == 3 )
                System.out.println( "\n" + player[i].playerName + " pass 3 cards to " +  player[0].playerName + ".\n"   );
            else
                System.out.println( "\n" + player[i].playerName + " pass 3 cards to " +  player[i+1].playerName + ".\n" );
            
            displayHand( player[i].playerHand );
            
            //for input validation and check if user has chose three different card
            while( firstCard == secondCard || secondCard == thirdCard || firstCard == thirdCard ){
                
                System.out.println( "\nPlease select three DIFFERENT number in front of cards you want to pass(1-13):" );
                
                try{
                    
                    System.out.print( "First  Card: " );
                    firstCard  = scan.nextInt() - 1;//array tarts from index 0
                    
                    System.out.print( "Second Card: " );
                    secondCard = scan.nextInt() - 1;
                    
                    System.out.print( "Third  Card: " );
                    thirdCard  = scan.nextInt() - 1;
                    
                }catch( Exception e ){
                    
                    scan.next();//consumes nextLine character
                    firstCard = 1; secondCard = 1; thirdCard = 1;
                    
                    continue;
                }
                
                //if player enters a number less than 1 or a greater than 13
                if( firstCard < 0 || firstCard > 12 || secondCard < 0 || secondCard > 12 || thirdCard < 0 || thirdCard > 12 ) {
                    
                    firstCard = 1; secondCard = 1; thirdCard = 1;
                    
                    continue;
                }
            }
            
            //for loop will sort card nos i.e firstCard, secondCard, thirdCard in ascending order
            //in case player enters nos which are not in sequence
            for( int j = 1; j < 4; j++ ) {
                
                int temp;
                if( firstCard > secondCard ) {
                    
                    temp       = firstCard;
                    firstCard  = secondCard;
                    secondCard = temp;
                }
                
                if( secondCard > thirdCard ) {
                    
                    temp       = secondCard;
                    secondCard = thirdCard;
                    thirdCard  = temp;
                }
                
                if( firstCard < secondCard && secondCard < thirdCard )
                    break;
            }
            
            removingCards( i, firstCard, secondCard, thirdCard );//removes passed cards from playerHand
        }
    }
    
    void passCards(){
        
        int[] cardPassingSequence = new int[]{ 1, 2, 3, 0 };
        
        //finally adding passed cards to players
        //from cards list to playerhand
        //cardPassingSequence array is used to loop through players array
        for( int k = 0, l = 0; k < cardPassingSequence.length; k++, l += 3 ){
                
            player[cardPassingSequence[k]].playerHand.add( cards.get(0+l) );//adding 'l' i.e 3, 6, 9 to increase
            player[cardPassingSequence[k]].playerHand.add( cards.get(1+l) );//cards index
            player[cardPassingSequence[k]].playerHand.add( cards.get(2+l) );
        }
    }
    
    void removingCards( int i, int firstCard, int secondCard, int thirdCard ){
        
        //using empty cards list to hold passed cards
        //adding removed cards to list cards
        //which is empty after cards were distuributed among player
        cards.add( player[i].playerHand.get( firstCard)  );
        cards.add( player[i].playerHand.get( secondCard) );
        cards.add( player[i].playerHand.get( thirdCard)  );
            
        //removing passed cards from player's hand
        player[i].playerHand.remove( player[i].playerHand.get( firstCard)    );
        player[i].playerHand.remove( player[i].playerHand.get( secondCard-1) );
        player[i].playerHand.remove( player[i].playerHand.get( thirdCard-2)  );
    }
    
    void sortHand(){
        
        for( int i = 0; i < 4; i++ ){
            
            Collections.sort( player[i].playerHand, new sortByValue() );
            Collections.sort( player[i].playerHand, new sortBySuit()  );
        }
    }
}
