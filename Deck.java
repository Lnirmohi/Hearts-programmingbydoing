import java.util.*;

public class Deck{

    void initializeDeck( String spade, String club, String heart, String diamond, List<Cards> cards ) {
        
        for( int i = 2; i < 15; i++ ) {
            
            Cards c1 = new Cards( spade, i);    Cards c3 = new Cards( heart,    i);
            Cards c2 = new Cards( club,  i);    Cards c4 = new Cards( diamond,  i);
            
            cards.add(c1);  cards.add(c2);      cards.add(c3);  cards.add(c4);
        }
    }
    
    List shuffleCards( List<Cards> cards ) {
        
        Random random = new Random();
        
        for( int i = 0; i < 52; i++ ) {
            
            int n = random.nextInt(52);
            int m = random.nextInt(52);
            
            swap( cards, n , m );
        }
        
        return cards;
    }
    
    void swap( List<Cards> cards, int n, int m ) {
        
        Cards temp = cards.get(n);
        cards.set( n, cards.get(m) );
        cards.set( m, temp );
    }
}