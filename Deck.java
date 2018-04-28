import java.util.*;

public class Deck{
    
    private final int A = 14,
                      J = 11,
                      Q = 12,
                      K = 13;
    
    private final String SPADE =   "SPADE",
                         CLUB =    "CLUB",
                         HEART =   "HEART",
                         DIAMOND = "DIAMOND"; 
    
    void fillDeckWithCards(List<Cards> cards){
        
        //creating deck of 4 suits, each containing 13 cards.
        for( int i = 2; i < 15; i++ ){
            
            Cards c1 = new Cards( SPADE,  i);    Cards c3 = new Cards( CLUB,    i);
            Cards c2 = new Cards( HEART,  i);    Cards c4 = new Cards( DIAMOND, i);
            
            cards.add(c1);  cards.add(c2);       cards.add(c3);  cards.add(c4);
        }
    }
    
    List<Cards> shuffleCards(List<Cards> cards){
        
        Random random = new Random();
        
        for(int i = 0; i < 52; i++ ){
            
            int n = random.nextInt(52);
            int m = random.nextInt(52);
            
            swap(cards, n , m);
        }
        
        return cards;
    }
    
    void swap(List<Cards> cards, int n, int m) {
        
        Cards temp = cards.get(n);
        cards.set( n, cards.get(m) );
        cards.set( m, temp );
    }
}