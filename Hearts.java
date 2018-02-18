/*
 * Throughout the program 2 OF CLUB is denoted by 'beginner' card.
 * Player[] array consists of three values i.e name of player, his/her points, cards in hand.
 * Sequence[] arrays are used for determining next player in sequence.
 */
import java.util.*;

public class Hearts
{
    public static final int A = 14,
                            J = 11,
                            Q = 12,
                            K = 13;
    
    public static final String spade =   "SPADE",
                                club =    "CLUB",
                               heart =   "HEART",
                             diamond = "DIAMOND";
                             
    static final int[] sequence1 = new int[]{ 0, 1, 2, 3 };//sequence to follow if player1 has 2OF CLUBS
    static final int[] sequence2 = new int[]{ 1, 2, 3, 0 };//sequence to follow if player2 has 2OF CLUBS
    static final int[] sequence3 = new int[]{ 2, 3, 0, 1 };//sequence to follow if player3 has 2OF CLUBS
    static final int[] sequence4 = new int[]{ 3, 0, 1, 2 };//sequence to follow if player4 has 2OF CLUBS
                             
    //deck of 52 cards
    public static List<Cards> cards = new ArrayList<Cards>(52);
    
    //player objects of type Player initilized with names and 0 pts
    public static Player player1 = new Player( "SOUTH", 0 );
    public static Player player2 = new Player( "WEST" , 0 );
    public static Player player3 = new Player( "NORTH", 0 );
    public static Player player4 = new Player( "EAST" , 0 );

    //default player array names: SOUTH WEST NORTH EAST
    public static Player[] player = new Player[]{ player1, player2, player3, player4 };
    
    Deck deck = new Deck();
    
    public static void main( String[] args) {
        
        Hearts hearts = new Hearts(); 
        
        hearts.playerSelection();
    }
    
    void playerSelection() {
        
        Scanner scan =  new Scanner(System.in);
        
        System.out.println( "\nChose how many players will play: " );
        int noOfPlayers = scan.nextInt();
        scan.nextLine();
         
        System.out.println( "\nEnter player names!" );
        
        for( int i = 0; i < noOfPlayers; i++ ) {
            
            System.out.print( "\nEnter " + (i+1) + " player's name : " );
            
            player[i].playerName = scan.nextLine().toUpperCase();    System.out.println();
        } 
        
        System.out.println( "\nPlayer 1: " + player[0].playerName + "\nPlayer 2: " + player[1].playerName + "\nPlayer 3: " + player[2].playerName + "\nPlayer 4: " + player[3].playerName + "\n");
        
        //initialize a deck with cards
        deck.initializeDeck( spade, club, heart, diamond, cards );
        
        deck.shuffleCards( cards );
        
        distributeCards();
        
        displayHand();
        
    }
    
    void startGame() {
        
        do{
            //shuffles deck
            cards = deck.shuffleCards( cards );
            
            //distributes card among players using subList Method and
            //sort hand of each player with the help of Collection.sort and comparator interface.
            distributeCards();
            
            //clear the card list to accumlate 12 cards passed by players. 
            cards.clear();
            
            //ask players to pass cards to players on their left.
            passCards();
            
            //find which player has 2 OF CLUBS.
            beginHand( has2OfClub() );
            
        }while( false ); //points[0] != 100 && points[1] != 100 && points[2] != 100 && points[3] != 100 
    }
    
    void distributeCards() {
        /*creating list of each players hand*/  /*      sorting the cards acc to the value      */   /*       sorting the cards acc to the suit   */
        player1.playerHand = cards.subList(  0, 13 );  Collections.sort( player1.playerHand, new sortByValue() );  Collections.sort( player1.playerHand, new sortBySuit() );

        player2.playerHand = cards.subList( 13, 26 );  Collections.sort( player2.playerHand, new sortByValue() );  Collections.sort( player2.playerHand, new sortBySuit() );

        player3.playerHand = cards.subList( 26, 39 );  Collections.sort( player3.playerHand, new sortByValue() );  Collections.sort( player3.playerHand, new sortBySuit() );

        player4.playerHand = cards.subList( 39, 52 );  Collections.sort( player4.playerHand, new sortByValue() );  Collections.sort( player4.playerHand, new sortBySuit() );
    }
    
    Player has2OfClub() {
        
        Cards beginning = new Cards( club, 2 );
        
        if( player1.playerHand.contains( beginning )  ) {
            
            gameNotifications( player[0].playerName );
            return player1;
        }
        else if( player2.playerHand.contains( beginning )  ) {
            
            gameNotifications( player[0].playerName );
            return player2;
        }
        else if( player1.playerHand.contains( beginning )  ) {
            
            gameNotifications( player[0].playerName );
            return player3;
        }
        else {
            
            gameNotifications( player[0].playerName );
            return player4;
        }
    }
    
    void gameNotifications( String player ) {
        
        System.out.print( "\nThe 2 OF CLUBS starts the game." + player + " play 2 OF CLUBS." );
    }
    
    void displayHand() {
        
        for( int i = 0; i < 13; i++ ) {
            
            System.out.println( player1.playerHand.get(i) + "\t" + player2.playerHand.get(i) + "\t" + player3.playerHand.get(i)+ "\t" + player2.playerHand.get(i) );
            //System.out.println( (i+1) + ": " + playerHand.get(i) );
        }
    }
    //firstPlayer will start the game
    void beginHand( Player fisrtPlayer ) {
        
        if( fisrtPlayer.playerName.equals(player1.playerName) ) 
            gameLoop(sequence1);
        else if( fisrtPlayer.playerName.equals(player2.playerName) )
            gameLoop(sequence2);
        else if( fisrtPlayer.playerName.equals(player2.playerName) )
            gameLoop(sequence3);
        else
            gameLoop(sequence4);
    }
    
    void passCards() {
        
        System.out.println( "\nPass 3 cards to player on your left" );
        for( int i = 0; i < 4; i++ ) {
            
            if( i == 3 )
                System.out.println( player[i].playerName + " pass three cards to " +  player[0].playerName + ".");
            else
                System.out.println( player[i].playerName + " pass three cards to " +  player[i+1].playerName + "." );
            
            //displayHand();
        }
        
    }
    
    void gameLoop( int[] sequence ) {
        
        //int[] sequence = seq;
        for( int i = 0; i < 4; i++ ) {
            
            
        }
    }
}