import java.util.ArrayList;
import java.util.Stack;
import java.util.HashSet;
import java.util.Iterator;

/**
 * PuzzleSearch holds methods for searching for solutions to the puzzle.
 * 
 * @author Ewen Cluley
 * @version v1 (06/05/2010)
 */
public class PuzzleSearch
{
    private Puzzle start;
    private Puzzle goal;

    /**
     * PuzzleSearch is used to try different puzzle state objects
     */
    public PuzzleSearch(Puzzle start)
    {
        goal = new Puzzle();
        goal.fillGoal();
       
        this.start = start;
    }

    /**
     *Depth first search to find the goal puzzle state.
     */
    public Puzzle depthFirstSearch()
    {
        Stack<Puzzle> toDo = new Stack<Puzzle>();
        ArrayList<Puzzle> alreadySeen = new ArrayList<Puzzle>();
        toDo.push(start);
        while(!toDo.isEmpty()){
            Puzzle current = toDo.pop();
            if(current.equals(goal)){
                return current;
            } else {
                ArrayList<Puzzle> daughtersList = current.getDaughters();
                for(Puzzle p:daughtersList){
                    if(!toDo.contains(p) && !alreadySeen.contains(p)){
                        toDo.push(p);
                        
                    }
                }
                alreadySeen.add(current);
            }
        }
        return null;
    }
    
     /**
     *Breadth first search to find the goal puzzle state.
     */
    public Puzzle breadthFirstSearch()
    {
        ArrayList<Puzzle> toDo = new ArrayList<Puzzle>();
        ArrayList<Puzzle> alreadySeen = new ArrayList<Puzzle>();
        toDo.add(start);
        while(!toDo.isEmpty()){
            Puzzle current = toDo.remove(0);
            if(current.equals(goal)){
                return current;
            } else {
                ArrayList<Puzzle> daughtersList = current.getDaughters();
                for(Puzzle p:daughtersList){
                    if(!toDo.contains(p) && !alreadySeen.contains(p)){
                        toDo.add(p);
                    }
                }
                alreadySeen.add(current);
            }
        }
        return null;
    }
    
     /**
     *A* search to find the goal puzzle state.
     */
    public Puzzle aStarSearch()
    {
        int moves = 0;
        ArrayList<Puzzle> toDo = new ArrayList<Puzzle>();
        HashSet<Puzzle> alreadySeen = new HashSet<Puzzle>();
        toDo.add(start);
        while(!toDo.isEmpty()){
            Puzzle current = toDo.remove(0);
            if(current.equals(goal)){
                return current;
            } else {
                ArrayList<Puzzle> daughtersList = current.getDaughters();
                daughtersList = sortDaughters(daughtersList, alreadySeen, toDo); //sorts the daughters list
                
                if(toDo.isEmpty()){
                    toDo.addAll(daughtersList); //if the toDo list has nothing in it, add the daughters list in its entirity
                } else {
                    toDo = mergeIntoToDo(toDo, daughtersList); // if got things in it, merge the daughters into it.
                }
                alreadySeen.add(current);
            }
        }
        return null;
    }
    
    /**
     * Sorts thew daughters list and removes and which are int he toDo and alreadySeen lists
     * @param daughtersIn the ArrayList of daughters to be sorted
     * @param alreadySeen the ArrayLsit of states which have already been seen by the search algorithm
     * @param toDo the ArrayList of states for the algorithm to check next.
     */
    private ArrayList<Puzzle> sortDaughters(ArrayList<Puzzle> daughtersIn, HashSet<Puzzle> alreadySeen, ArrayList<Puzzle> toDo)
    {
        ArrayList<Puzzle> daughtersOut = new ArrayList<Puzzle>();
        daughtersOut.add(daughtersIn.get(0));
        for(Puzzle lookingAt:daughtersIn){
            for(int i =0; i<daughtersOut.size(); i++){
                if(lookingAt.getCost() < daughtersOut.get(i).getCost()){
                    if(!alreadySeen.contains(lookingAt)){
                        daughtersOut.add(i, lookingAt);
                        i = daughtersOut.size();
                    }
                } 
            }
        }
        
        return daughtersOut;
    }
    
    /**
     * Mereges an ArrayList of daughter states into the toDo list.
     */
    private ArrayList<Puzzle> mergeIntoToDo(ArrayList<Puzzle> toDo, ArrayList<Puzzle> daughters) //both array list's must be sorted!
    { 
        ArrayList<Puzzle> output = new ArrayList<Puzzle>();
        int daughtersI =0;
        int toDoI = 0;
        while(toDoI < toDo.size() && daughtersI < daughters.size()){
            if(toDo.get(toDoI).getCost() <= daughters.get(daughtersI).getCost()){
                output.add(toDo.get(toDoI));
                toDoI ++;
                
            }
            else{
                output.add(daughters.get(daughtersI));
                daughtersI ++;
                
            }
        }
        while(toDoI < toDo.size()){
            output.add(toDo.get(toDoI));
            toDoI ++;
            
        }
        while(daughtersI < daughters.size()){
            output.add(daughters.get(daughtersI));
            daughtersI ++;
            
        }
        
            return output;
    }
}
