package Heuristic;

import Grid.Grid;
import Grid.Cell;

/**
 * This is the (Euclidean Distance / 4) Heuristic Class.
 */
public class EuclideanDistanceByFour extends Heuristic{
    @SuppressWarnings("unused")
    // Global Variables
    private Grid grid;
    private int end_x;
    private int end_y;

    /**
     * This is the constructor of the EuclideanDistanceByFour() class.
     * @param grid the grid
     */
    public EuclideanDistanceByFour(Grid grid) {
        super(grid);
        this.end_x = grid.getEndCell()[0][0];
        this.end_y = grid.getEndCell()[0][1];
    } // ends the EuclideanDistanceByFour() constructor


    /**
     * This method will get the (Euclidean Distance / 4) heurisitc for a node.
     * @param cell the current cell
     * @return a float value that will be used as a heurisitic (represents distance to goal) found using the (Euclidean Distance/4)
     */
    public float getHeuristic(Cell cell) {
        return (float)((0.25)*((Math.sqrt(((end_x-cell.getX())*(end_x-cell.getX())) + ((end_y-cell.getY())*(end_y-cell.getY()))))));
    }
} // ends the EuclideanDistanceByFour() class