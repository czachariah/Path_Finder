package Heuristic;

import Grid.Grid;
import SearchAlgos.Node;

public class ManhattanDistance extends Heuristic {
    // Global Variables
    private Grid grid;
    private int end_x;
    private int end_y;

    /**
     * This is the constructor of the ManhattanDistance() class.
     * @param grid the grid
     */
    public ManhattanDistance(Grid grid) {
        super(grid);
        this.end_x = grid.getEndCell()[0][0];
        this.end_y = grid.getEndCell()[0][1];
    } // ends the ManhattanDistance() constructor


    /**
     * This method will get the Manhattan Distance heurisitc for a node.
     * @param node the current node
     * @return a float value that will be used as a heurisitic (represents distance to goal) found using the Manhattan Distance
     */
    public float getHeuristic(Node node) {
        return 0f;
    }
} // ends the ManhattanDistance() class
