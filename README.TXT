------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all he/she needs to know. The comments should usually include at least:
------------------------------------------------------------------------

PROJECT TITLE: 15 Puzzle
PURPOSE OF PROJECT: To create a program to display and solve a 15 puzzle
VERSION or DATE: v1.0
HOW TO START THIS PROJECT: Run the GUI class's main method
AUTHORS: Ewen Cluley
USER INSTRUCTIONS: Each solve algorithm has a different button to invoke it. Press the button for the type of search you would 
like to use.

Displaying the puzzle

Squares are held within a 2 dimensional array of Square objects.  These can have values from 1 
through to 16 (16 represents the blank square).  They can be swapped on the grid but only if the 
number squares 1 to 15 are being swapped with the blank square (16).

Example puzzle:

    2        3   4 
    1   5   7   8 
    9   6  10  11 
 13  14  15  12

Search method comparison:

Each search uses a different ordering of states it needs to check in finding a solution.  
Depth first checks down the search tree giving daughters of the state being checked 
precedence over the siblings.  This means depth first will look down the tree first.  It 
is very unefficient for solving the 15 Puzzle as it will continue down one edge of the tree, 
taking a long time to return back up the tree to search from another node.

Depth first, in this case will rarely finish (unless for very easily solved puzzles)

Breadth First will seach all siblins from a node before looking at their daughters.  This 
makes it ok for solving easily solved puzzles but again for a difficult configuration will be 
very slow. To solve the example puzzle it must look at 281 different states before getting to the goal.

A* search is much more intuitve, looking at heuristics to rate how good/ bad a current state is (how 
close it is to the goal).  The heuristics used are Manhattan distance and the search nodes depth within 
the tree.  It will rate each daughter it finds and those with a lower score (closer to the goal state) will be 
searched from first.  This is a much more efficent search algorithm and can find a solution to the 
example within 7 moves.


