
/**
 * Represents a square with a number in it
 * 
 * @author Ewen
 * @version v1 (04/05/2010)
 */
public class FilledSquare extends Square
{
    private int value;

    /**
     * Builds a filled square at possition x, y with a value
     */
    public FilledSquare(int value)
    {
        super();
        this.value = value;
    }

    /**
     * Gets the value of the square
     */
    public int getValue()
    {
        return value;
    }
    
    public int hashCode()
    {
        return value;
    }
    
}
