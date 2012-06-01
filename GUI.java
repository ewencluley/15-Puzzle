import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import java.util.TimerTask;
import java.util.Timer;

/**
 * The main GUI window for the system. displays the grid of tiles and will show how solutions were found
 * 
 * @author Eqwen Cluley
 * @version v1
 */
public class GUI
{
    private Puzzle puzzle;
    private JFrame window;
    private Puzzle solution;
    private PuzzleSearch searcher;
    private JPanel gridPanel;
    private JLabel[][] grid;
    private Container pane;
    private Timer timer;
    private Stack<Puzzle> solutions;

    /**
     * Builds a GUI window and initalises the puzzle.  It also setsup a timer object to be used later when a solution has been found.
     */
    public GUI()
    {
        timer = new Timer();
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        puzzle = new Puzzle();
        searcher = new PuzzleSearch(puzzle);
        pane = window.getContentPane();
        draw();
    }

    /**
     * Redraws the grid, based upon the current puzzle.  By updating the puzzle, and calling this method you can change the display
     */
    public void draw()
    {
        pane.removeAll();
        gridPanel = null;
        gridPanel = new JPanel();
        grid = new JLabel[4][4];
        pane.setLayout(new FlowLayout());
        gridPanel.setLayout(new GridLayout(0,4));
        for(int i =0; i<4;i++){
            for(int j = 0; j<4; j++){
                grid[i][j] = new JLabel("" + puzzle.getSquare(i,j).getValue(), 4);
                grid[i][j].setFont(new Font("Sans-serif", Font.BOLD, 24));
                grid[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
                if(puzzle.getSquare(i,j).getValue() == 16){
                    grid[i][j].setText("  ");
                }
                gridPanel.add(grid[i][j]);
            }
        }
        pane.add(gridPanel);
        JPanel controlls = new JPanel();
        controlls.setLayout(new GridLayout(2, 3));
        JButton solveDF = new JButton("Solve- Depth First");
        solveDF.addActionListener(new SolveListener("DF"));
        JButton solveBF = new JButton("Solve- Breadth First");
        solveBF.addActionListener(new SolveListener("BF"));
        JButton solveAS = new JButton("Solve- A*");
        solveAS.addActionListener(new SolveListener("AS"));
        JButton reset = new JButton("Set Puzzle 1");
        reset.addActionListener(new ResetListener());
        controlls.add(solveDF);
        controlls.add(solveBF);
        controlls.add(solveAS);
        controlls.add(reset);
        pane.add(controlls);
        
        window.setSize(600,200);
        window.setVisible(true); 
    }
    
    /**
     *Puts the steps to reach a given solution onto a stack so that they are arranged in reverse order.
     */
    private Stack<Puzzle> buildList(Puzzle p)
    {
        Stack<Puzzle> list = new Stack<Puzzle>();
        Puzzle sol = p;
        while(!(sol.getPredecessor() == null)){
            list.push(sol);
            //sol.print();
            sol = sol.getPredecessor();
        }
        list.push(puzzle);
        return list;
    }

    /**
     * Listener for the solve buttons.
     */
    private class SolveListener implements ActionListener
    {
        private String type;
        
        /**
         * Takes in a strign to define which type of search it will execute.
         * @param type String "BF" for breadth first search, "DF" for depth first and any other for A*
         */
        private SolveListener(String type)
        {
            this.type = type;
        }
                
        public void actionPerformed(ActionEvent e){
            Puzzle solution =null;
            if(type == "DF"){
                solution = searcher.depthFirstSearch();
            } else if(type == "BF"){
                solution = searcher.breadthFirstSearch();
            } else {
                solution = searcher.aStarSearch();
            }
            solutions = buildList(solution);  
            for(int i =0; i<solutions.size(); i++){
                timer.schedule(new theTask(), i * 1000L);
            }        
        }
    }
    
    private class ResetListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e){
            puzzle = new Puzzle();
            puzzle.fillPuzzle2();
            solution = null;
            solutions = new Stack<Puzzle>();
            draw();
        }
    }
    
    /**
     * this updates the puzzle and is used to display the steps taken to the solution.
     */
    private class theTask extends TimerTask
    {
        public void run()
        {
            Puzzle current = solutions.pop();
            puzzle = current;
            draw();
        }
    }
    
    public static void main(String[] args)
    {
        new GUI();
    }
}