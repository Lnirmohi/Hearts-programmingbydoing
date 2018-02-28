/*
 * Throughout the program 2 OF CLUB is denoted by 'beginning' card.
 * Player[] array consists of three values i.e name of player, his/her points, cards in hand.
 * Sequence[] arrays are used for determining next player in sequence.
 * Not using subList to create each player hands because it only creates view and clrearing cards List
 * gives concurrentmodification exception.
 */
import java.util.*;

public class Hearts {
    
    private static final int A = 14,
                             J = 11,
                             Q = 12,
                             K = 13;
    
    private static final String spade =   "SPADE",
                                 club =    "CLUB",
                                heart =   "HEART",
                              diamond = "DIAMOND";
                             
    static final int[] sequence1 = new int[]{ 0, 1, 2, 3 };//sequence to follow if player1 has 2OF CLUBS
    static final int[] sequence2 = new int[]{ 1, 2, 3, 0 };//sequence to follow if player2 has 2OF CLUBS
    static final int[] sequence3 = new int[]{ 2, 3, 0, 1 };//sequence to follow if player3 has 2OF CLUBS
    static final int[] sequence4 = new int[]{ 3, 0, 1, 2 };//sequence to follow if player4 has 2OF CLUBS 
    
    //deck of 52 cards
    private static List<Cards> cards = new ArrayList<Cards>(52);
    
    //player objects of type Player initilized with names and 0 pts
    private static Player player1 = new Player( "SOUTH", 0 );
    private static Player player2 = new Player( "WEST" , 0 );
    private static Player player3 = new Player( "NORTH", 0 );
    private static Player player4 = new Player( "EAST" , 0 );

    //default player array names: SOUTH WEST NORTH EAST
    private static Player[] player = new Player[]{ player1, player2, player3, player4 };
    
    Deck deck = new Deck();
    
    Scanner scan =  new Scanner(System.in);
    
    public static void main( String[] args) {
        
        Hearts hearts = new Hearts(); 
        
        hearts.playerSelection();
    }
    
    void playerSelection() {
        
        int noOfPlayers = -1;
        boolean validation = true;
        
        //input validation
        while( noOfPlayers < 1 || noOfPlayers > 4 ) {
            
            System.out.println( "\nChoose how many players will play(1-4): " );
            
            ///input validation
            try {
                 
                noOfPlayers = scan.nextInt();
            }
            catch( Exception e ) {
                
                System.out.println( "Please enter a number form 1-4!" );
                scan.nextLine();
                continue;
            }
            
            scan.nextLine();
        }
        
        System.out.println( "\nEnter player names!" );
        
        for( int i = 0; i < noOfPlayers; i++ ) {
            
            System.out.print( "\nEnter " + (i+1) + " player's name : " );
            
            player[i].playerName = scan.nextLine().toUpperCase();    System.out.println();
        } 
        
        System.out.println( "\nPlayer 1: " + player[0].playerName + "\nPlayer 2: " + player[1].playerName +
                            "\nPlayer 3: " + player[2].playerName + "\nPlayer 4: " + player[3].playerName + "\n" );
        
        //initialize a deck with cards
        deck.initializeDeck( spade, club, heart, diamond, cards );
        
        deck.shuffleCards( cards );
        
        distributeCards();
        
        cards.clear();
        
        //ask players to pass cards to players on their left.
        passCards();
        
        //find which player has 2 OF CLUBS.
        beginHand( has2OfClub() );

        displayHand1();
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
        
        //distributes cards among players
        for( int i = 0; i < 13; i++ ) {
                
            player[0].playerHand.add( cards.get(i)    );   player[1].playerHand.add( cards.get(i+13) );
            player[2].playerHand.add( cards.get(i+26) );   player[3].playerHand.add( cards.get(i+39) );
        }
        
        //sorting each player's hand
        sortHand();
    }
    
    Player has2OfClub() {
        
        Cards beginning = new Cards( club, 2 );
        
        if( player1.playerHand.contains( beginning )  ) {
            
            gameNotifications( player[0].playerName );
            return player1;
        }
        else if( player2.playerHand.contains( beginning )  ) {
            
            gameNotifications( player[1].playerName );
            return player2;
        }
        else if( player1.playerHand.contains( beginning )  ) {
            
            gameNotifications( player[2].playerName );
            return player3;
        }
        else {
            
            gameNotifications( player[3].playerName );
            return player4;
        }
    }
    
    void gameNotifications( String player ) {
        
        System.out.print( "\nThe 2 OF CLUBS starts the game." + player + " play 2 OF CLUBS." );
    }
    
    void displayHand( List<Cards> playerHand ) {
        
        for( int i = 0; i < playerHand.size(); i++ ) {
            
            //System.out.println( player1.playerHand.get(i) + "\t" + player2.playerHand.get(i) + "\t" + player3.playerHand.get(i)+ "\t" + player2.playerHand.get(i) );
            System.out.println( (i+1) + ":\t" + playerHand.get(i) );
        }
    }
    
    //for test
    void displayHand1() {
        
        for( int i = 0; i < 13; i++ ) {
            
            System.out.println( player1.playerHand.get(i) + "\t" + player2.playerHand.get(i) + "\t" + player3.playerHand.get(i)+ "\t" + player4.playerHand.get(i) );
        }
    }
   
    //firstPlayer will start the game
    void beginHand( Player fisrtPlayer ) {
        
        if( fisrtPlayer.playerName.equals(player1.playerName) ) 
            gameLoop(sequence1);
            
        else if( fisrtPlayer.playerName.equals(player2.playerName) )
            gameLoop(sequence2);
            
        else if( fisrtPlayer.playerName.equals(player3.playerName) )
            gameLoop(sequence3);
            
        else if( fisrtPlayer.playerName.equals(player4.playerName) )
            gameLoop(sequence4);
    }
    
    void passCards() {
        
        int firstCard, secondCard, thirdCard;//cards that are going to be passed to next player i.e on left
        
        System.out.println( "\nPass 3 cards to player on your left\n" );
        
        for( int i = 0; i < 4; i++ ) {
            
            firstCard = 1; secondCard = 1; thirdCard = 1;
            
            if( i == 3 )
                System.out.println( "\n" + player[i].playerName + " pass 3 cards to " +  player[0].playerName + ".\n");
            else
                System.out.println( "\n" + player[i].playerName + " pass 3 cards to " +  player[i+1].playerName + ".\n" );
            
            displayHand( player[i].playerHand );
            
            //for input validation and check if user has chose three different card
            while( firstCard == secondCard || secondCard == thirdCard || firstCard == thirdCard ) {
                
                System.out.println( "\nPlease select three DIFFERENT number in front of cards you want to pass(1-13):" );
                
                try {
                    
                    System.out.print( "First  Card: " );
                    firstCard  = scan.nextInt() - 1;
                    
                    System.out.print( "Second Card: " );
                    secondCard = scan.nextInt() - 1;
                    
                    System.out.print( "Third  Card: " );
                    thirdCard  = scan.nextInt() - 1;
                } catch( Exception e ) {
                    
                    scan.next();
                    continue;
                }
                
                //if player enterss a less than 1 or a greater than 13
                if( firstCard < 0 || firstCard > 12 || secondCard < 0 || secondCard > 12 || thirdCard < 0 || thirdCard > 12 ) {
                    
                    firstCard = 1; secondCard = 1; thirdCard = 1;
                    continue;
                }
            }
            
            //for loop will sort card nos i.e firstCard, secondCard, thirdCard in ascending order
            //in case player enters nos are out of sequence
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
            
            removingCards( i, firstCard, secondCard, thirdCard );//removes cards from playerHand
        }
        
        //finally adding passed cards to players
        //from cards list to playerhand
        //sequence2 array is used to loop through players array
        for( int k = 0, l = 0; k < sequence2.length; k++, l += 3 ) {
                
            player[sequence2[k]].playerHand.add( cards.get(0+l) );//adding 'l' i.e 3, 6, 9 to increase
            player[sequence2[k]].playerHand.add( cards.get(1+l) );//cards index
            player[sequence2[k]].playerHand.add( cards.get(2+l) );
        }
        
        sortHand();
    }
    
    void removingCards( int i, int firstCard, int secondCard, int thirdCard ) {
        
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
    
    void sortHand() {
        
        for( int i = 0; i < 4; i++ ) {
            
            Collections.sort( player[i].playerHand, new sortByValue() );
            Collections.sort( player[i].playerHand, new sortBySuit()  );
        }
    }
    
    void gameLoop( int[] sequence ) {
        
        //int[] sequence = seq;
        for( int i = 0; i < 4; i++ ) {
            
            
        }
    }
}
