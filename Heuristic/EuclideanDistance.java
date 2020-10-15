package Heuristic;

import Grid.Grid;
import Grid.Cell;

/**
 * This is the Euclidean Distance Heuristic Class.
 */
public class EuclideanDistance extends Heuristic{
    @SuppressWarnings("unused")
    // Global Variables
    private Grid grid;
    private int end_x;
    private int end_y;

    /**
     * This is the constructor of the EuclideanDistance() class.
     * @param grid the grid
     */
    public EuclideanDistance(Grid grid) {
        super(grid);
        this.end_x = grid.getEndCell()[0][0];
        this.end_y = grid.getEndCell()[0][1];
    } // ends the EuclideanDistance() constructor


    /**
     * This method will get the Euclidean Distance heurisitc for a node.
     * @param cell the current cell
     * @return a float value that will be used as a heurisitic (represents distance to goal) found using the Euclidean Distance
     */
    public float getHeuristic(Cell cell) {
        return (float)(Math.sqrt(((end_x-cell.getX())*(end_x-cell.getX())) + ((end_y-cell.getY())*(end_y-cell.getY()))));
    }
} // ends the EuclideanDistance() class
