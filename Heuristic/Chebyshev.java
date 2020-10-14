package Heuristic;

import Grid.Grid;
import Grid.Cell;

public class Chebyshev extends Heuristic {
    // Global Variables
    private Grid grid;
    private int end_x;
    private int end_y;

    /**
     * This is the constructor of the Chebyshev() class.
     * @param grid the grid
     */
    public Chebyshev(Grid grid) {
        super(grid);
        this.end_x = grid.getEndCell()[0][0];
        this.end_y = grid.getEndCell()[0][1];
    } // ends the Chebyshev() constructor


    /**
     * This method will get the Chebyshev heurisitc for a node.
     * @param cell the current cell
     * @return a float value that will be used as a heurisitic (represents distance to goal) found using the Chebyshev
     */
    public float getHeuristic(Cell cell) {
        float dx = Math.abs(cell.getX() - end_x);
		float dy = Math.abs(cell.getY() - end_y);
        return (float) Math.max(dx, dy);
    }
} // ends the Chebyshev() class
