package Heuristic;

import Grid.Grid;
import SearchAlgos.Node;

/**
 * This is an abstract class to define different heuristic functions.
 */
public abstract class Heuristic {
    
    // Global Variables
    private Grid grid;  
    private int end_x;
    private int end_y;

    /**
     * This is the Heuristic constructor.
     * @param grid is the grid
     * @param end_x is the x-coordinate of the end goal cell
     * @param end_y is the y-coordinate of the end goal cell
     */
    public Heuristic(Grid grid , int end_x , int end_y) {
        this.grid = grid;
        this.end_x = end_x;
        this.end_y = end_y;
    } // ends the Heuristic() constructor

    public abstract float getHeuristic(Node node);
} // ends the Heuristic() class
