import java.util.ArrayList;
import java.util.Arrays;
/**
 * Holds data about the configuration of the puzzle
 * 
 * @author Ewen Cluley
 * @version v1(04/05/2010)
 */
public class Puzzle implements Cloneable
{
    private Square[][] grid;
    private Puzzle predecessor;
    private int blankRow;
    private int blankColumn;
    private int cost;
    private int depth;
    private int manhattan;

    /**
     * Builds a puzzle of size 4x4
     */
    public Puzzle()
    {
        grid = new Square[4][4];
        fillPuzzle2();
        predecessor = null;
        depth = 0;
        manhattan = calculateManhattan();
        setCost();
    }
    
    /**
     * Returns the square object from the specific possition in the grid.
     * @param i int for the row number
     * @param j int for column number
     * @return Square the square found at that location.
     */
    public Square getSquare(int i, int j)
    {    
        return grid[i][j];
    }
    
    /**
     * Moves a square into the empty space on the grid.  
     * If the square cannot be moved, e.g. it is not surrounded by an empty place, the function returns false.
     * @param row defines the row of the tile to move
     * @param column defines the column of the tile to move
     */
    public boolean moveSquare(int sX, int sY)
    {
        //check all positions arround square to be moved
        if(blankRow == (sX +1) ^ blankRow == (sX-1) ^ blankColumn == (sY+1) ^ blankColumn == (sY-1)){
            Square tempSq = grid[sX][sY];
            grid[sX][sY] = grid[blankRow][blankColumn];
            grid[blankRow][blankColumn] = tempSq;
            blankRow = sX;
            blankColumn = sY;
        } else {
            return false;
        }
        return true;
    }
    
    /**
     * sets the predecessor of the puzzle state during the search.
     * @param p a Puzzle object which created this one (it's parent)
     */
    private void setPredecessor(Puzzle p)
    {
         predecessor = p;
    }
    
    /**
     * gets the predecessor of the puzzle state during the search.
     * @return Puzzle object which created this one (it's parent)
     */
    public Puzzle getPredecessor()
    {
         return predecessor;
    }
    
    
    /**
     * Prints the puzzle out to the terminal.
     */
    public void print()
    {
        for(int i =0; i <4; i++){
            for(int j =0; j<4; j++){
                if(grid[i][j] instanceof FilledSquare){
                    if(grid[i][j].getValue() < 10){
                        System.out.print(" ");
                    }
                    System.out.print(" " + grid[i][j].getValue() + " ");
                } else {
                    System.out.print("    ");
                }
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------");
    }
    
    /**
     * Used to create a bit copy of the object. 
     * @return Object the cloned object.
     */
    public Object clone() throws CloneNotSupportedException
    {
        Puzzle copy = (Puzzle) super.clone();
        copy.grid = new Square[4][4];
        
        for(int i =0; i <4; i++){
            for(int j =0; j<4; j++){
                copy.grid[i][j] = grid[i][j];
           }
        }
        return copy;
    }
    
    /**
     * gets the daughter states of the puzzle state
     * @return ArrayList<Puzzle> of puzzle objects representing the possible moves from the current state..
     */
    public ArrayList<Puzzle> getDaughters()
    {
        ArrayList<Puzzle> daughters = new ArrayList<Puzzle>();
        for(int i =blankRow-1; i <= blankRow +1; i++){
            for(int j =blankColumn-1; j<=blankColumn+1; j++){
                
                try{
                   Puzzle theClone = (Puzzle) clone();
                   try{
                        boolean success = theClone.moveSquare(i, j);
                        if(success){
                            theClone.manhattan = theClone.calculateManhattan();
                            theClone.setPredecessor(this);
                            theClone.depth += 1;
                            theClone.setCost();
                            daughters.add(theClone);
                        }
                    } catch (IndexOutOfBoundsException e){
                    }
                }catch(CloneNotSupportedException e){
                }
            }
        }
        return daughters;
    }
    
    /**
     * compares 2 Puzzle objects to see if they are equivalent.  This will be true if the grid has the same configuration of squares.
     * @return boolean true if the objects are equal, otherwise false.
     */
    public boolean equals(Object o)
    {
        if(o == null){
            //object is null;
            return false;
        }
        if(o instanceof Puzzle){
            Puzzle compare = (Puzzle) o;
            boolean isEqual = true;
            for(int i =0; i <4; i++){
                for(int j =0; j<4; j++){
                    Square current = compare.grid[i][j];
                    //System.out.println(current);
                    if(!(current.equals(grid[i][j]))){
                        // one of the squares doesnt match;
                        return false;
                    } 
                }
            }
            return true; //everthing matches
        }
        return false; //not comparing 2 puzzle onbjects
    }
    
    /**
     * returns the redefined Hashcode 
     * @returns int the objects Hash Cose.
     */
    public int hashCode()
    {
        return Arrays.deepHashCode(grid);
    }
    
    /**
     * sets the cost variable withint he puzzle object
     */
    public void setCost()
    {
        cost = calculateManhattan() + depth; 
    }
    
    /**
     * Calculates the cost of the puzzle state using Manhattan Distance
     */
    public int calculateManhattan()
    {
        //calculate the manahattan distance from the goal state.
        int manhattanDistance = 0;
        int cost = 0;
        
        for(int i =0; i <4; i++){
            for(int j =0; j<4; j++){
                int actualNumber = i*4 + j +1;
                int m = grid[i][j].getValue();
                
                int correctCol = m % 4; // the remainder of the number found /4 will represent the column number of m
                if (correctCol == 0){ // if it is 0 then it is the last square in that row and will have column number 4
                    correctCol = 4;
                }
                correctCol -= 1; // -1 to make it represent an array column (start at 0 not 1)
                double divide = (double) m/4;
                int correctRow = (int) Math.ceil(divide) -1;

                int thisManhattanDistance = (int) Math.sqrt(Math.pow((i-correctRow),2)) + (int) Math.sqrt(Math.pow((j-correctCol),2));
                manhattanDistance += thisManhattanDistance;
               
            }
        }
        return manhattanDistance;
    }
    
    /**
     * returns the cost of the state from the goal state.
     * @return int the cost of the state
     */
    public int getCost()
    {
        return cost;
    }
    /**
     * Calculates the number of tiles in incorrect possitions in the grid.
     * @return int number of misplaced tiles
     */
    public int calculateMisplacedTiles()
    {
        int misplacedTiles = 0;
         for(int i =0; i <4; i++){
            for(int j =0; j<4; j++){
                int actualNumber = i*4 + j +1; //the number which should appear to satisfy the goal
                int m = grid[i][j].getValue();
                if(!(m == actualNumber) && m != 0){
                    misplacedTiles ++;
                }
            }
        }
        return misplacedTiles;
    }
    //****************************************************************************************************************
    //***************Methods to fill different configurations of the puzzle******************************************
    //****************************************************************************************************************
    /**
     * Fills a pre-defined Puzzle
     * 
     *      1    2    3    4 
     *      5    6    7    8 
     *      9  10  11  12 
     *   13  14          15  
     * 
     */
    public void fillPuzzle()
    {
        grid[0][0] = new FilledSquare(1);
        grid[0][1] = new FilledSquare(2);
        grid[0][2] = new FilledSquare(3);
        grid[0][3] = new FilledSquare(4);
        grid[1][0] = new FilledSquare(5);
        grid[1][1] = new FilledSquare(6);
        grid[1][2] = new FilledSquare(7);
        grid[1][3] = new FilledSquare(8);
        grid[2][0] = new FilledSquare(9);
        grid[2][1] = new FilledSquare(10);
        grid[2][2] = new FilledSquare(11);
        grid[2][3] = new FilledSquare(12);
        grid[3][0] = new FilledSquare(13);
        grid[3][1] = new FilledSquare(14);
        grid[3][2] = new EmptySquare();
        grid[3][3] = new FilledSquare(15);

        blankRow = 3;
        blankColumn = 2;
    }
    
     /**
     * Fills the goal state into the puzzle object.
     */
    public void fillGoal()
    {
        grid[0][0] = new FilledSquare(1);
        grid[0][1] = new FilledSquare(2);
        grid[0][2] = new FilledSquare(3);
        grid[0][3] = new FilledSquare(4);
        grid[1][0] = new FilledSquare(5);
        grid[1][1] = new FilledSquare(6);
        grid[1][2] = new FilledSquare(7);
        grid[1][3] = new FilledSquare(8);
        grid[2][0] = new FilledSquare(9);
        grid[2][1] = new FilledSquare(10);
        grid[2][2] = new FilledSquare(11);
        grid[2][3] = new FilledSquare(12);
        grid[3][0] = new FilledSquare(13);
        grid[3][1] = new FilledSquare(14);
        grid[3][2] = new FilledSquare(15);
        grid[3][3] = new EmptySquare();

        blankRow = 3;
        blankColumn = 3;
    }
    
     /**
     * Fills a pre-defined Puzzle
     * 
     *      2        3   4 
     *      1   5   7   8 
     *      9   6  10  11 
     *   13  14  15  12 
     */
    public void fillPuzzle2()
    {
        grid[0][0] = new FilledSquare(2);
        grid[0][1] = new EmptySquare();
        grid[0][2] = new FilledSquare(3);
        grid[0][3] = new FilledSquare(4);
        grid[1][0] = new FilledSquare(1);
        grid[1][1] = new FilledSquare(5);
        grid[1][2] = new FilledSquare(7);
        grid[1][3] = new FilledSquare(8);
        grid[2][0] = new FilledSquare(9);
        grid[2][1] = new FilledSquare(6);
        grid[2][2] = new FilledSquare(10);
        grid[2][3] = new FilledSquare(11);
        grid[3][0] = new FilledSquare(13);
        grid[3][1] = new FilledSquare(14);
        grid[3][2] = new FilledSquare(15);
        grid[3][3] = new FilledSquare(12);

        blankRow = 0;
        blankColumn = 1;
    }
     //****************************************************************************************************************
    //***************End of Methods to fill different configurations of the puzzle***********************************
    //****************************************************************************************************************
}

