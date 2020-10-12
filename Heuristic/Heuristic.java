package Heuristic;

import Grid.Grid;
import SearchAlgos.Node;

/**
 * This is an abstract class to define different heuristic functions.
 */
public abstract class Heuristic {
    
    // Global Variables
    private Grid grid;

    /**
     * This is the Heuristic constructor.
     * @param grid is the grid
     */
    public Heuristic(Grid grid) {
        this.grid = grid;
    } // ends the Heuristic() constructor

    /**
     * This method will get the heurisitc for a node.
     * @param node the current node
     * @return a float value that will be used as a heurisitic (represents distance to goal)
     */
    public abstract float getHeuristic(Node node);
} // ends the Heuristic() class
