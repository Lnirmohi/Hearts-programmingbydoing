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
            switch (value) {
                case 10:
                    return "10"  +  "  OF " +   suit;
                case 11:
                    return "J"   + "   OF " +   suit;
                case 12:
                    return "Q"   + "   OF " +   suit;
                case 13:
                    return "K"   + "   OF " +   suit;
                default:
                    return "A"   + "   OF " +   suit;
            }
        }
        
        return value + "   OF " +   suit;
    }
}

//using comparator interface to sort hand by suit
class sortBySuit implements Comparator<Cards> {
    
    @Override
    public int compare( Cards a, Cards b ) {
        
        return a.suit.compareTo(b.suit);
    }
}

//using comparator interface to sort hand by value
class sortByValue implements Comparator<Cards> {
    
    @Override
    public int compare( Cards a, Cards b ) {
        
        return a.value - b.value;
    }
}