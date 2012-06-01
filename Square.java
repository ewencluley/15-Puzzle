
/**
 * Square holds information about squares within the 15 Puzzle Grid
 * 
 * @author Ewen Cluley
 * @version v1 (04/05/2010)
 */
public abstract class Square
{
    /**
     * The constructor takes in the x and y coordinates of the square.
     */
    public Square()
    {
    }
    
    public abstract int getValue();
    
    
    public abstract int hashCode();
    
    public boolean equals(Object o)
    {
        if(o instanceof Square){
            Square sq = (Square) o;
            if(sq.getValue() == getValue()){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
}
