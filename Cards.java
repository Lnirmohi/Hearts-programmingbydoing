import java.util.*;

class Cards{
    
    String suit; 
    int value;
    public Cards( String suit, int value ) {
        
        this.suit = suit;
        this.value = value;
    }
    
    public String toString() {
        
        if( value > 9 ) {
            if( value == 10 )
            return "10"  +  "  OF " +   suit;
            else if( value == 11 )
            return "J"   + "   OF " +   suit;
            else if( value == 12 )
            return "Q"   + "   OF " +   suit;
            else if( value == 13 )
            return "K"   + "   OF " +   suit;
            else
            return "A"   + "   OF " +   suit;
        }
        
        return value + "   OF " +   suit;
    }
}
//using comparator interface to sort hand by suit
class sortBySuit implements Comparator<Cards> {
    
    public int compare( Cards a, Cards b ) {
        
        return a.suit.compareTo(b.suit);
    }
}
//using comparator interface to sort hand by value
class sortByValue implements Comparator<Cards> {
    
    public int compare( Cards a, Cards b ) {
        
        return a.value - b.value;
    }
}