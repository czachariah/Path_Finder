package Heuristic;

import Grid.Grid;
import Grid.Cell;

/**
 * This is the (Manhattan Distance / 4) Heuristic Class.
 */
public class ManhattanDistanceByFour extends Heuristic {
    @SuppressWarnings("unused")
    // Global Variables
    private Grid grid;
    private int end_x;
    private int end_y;

    /**
     * This is the constructor of the ManhattanDistanceByFour() class.
     * @param grid the grid
     */
    public ManhattanDistanceByFour(Grid grid) {
        super(grid);
        this.end_x = grid.getEndCell()[0][0];
        this.end_y = grid.getEndCell()[0][1];
    } // ends the ManhattanDistanceByFour() constructor


    /**
     * This method will get the (Manhattan Distance / 4) heurisitc for a node.
     * @param cell the current cell
     * @return a float value that will be used as a heurisitic (represents distance to goal) found using the (Manhattan Distance / 4)
     */
    public float getHeuristic(Cell cell) {
        return (float) 0.25 * Math.abs(cell.getX()-end_x) + Math.abs(cell.getY()-end_y);
    }
} // ends the ManhattanDistanceByFour() class
