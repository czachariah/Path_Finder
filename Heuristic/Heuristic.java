package Heuristic;

import Grid.Grid;
import Grid.Cell;

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
     * @param cell the current cell being looked at
     * @return a float value that will be used as a heurisitic (represents distance to goal)
     */
    public abstract float getHeuristic(Cell cell);
} // ends the Heuristic() class
