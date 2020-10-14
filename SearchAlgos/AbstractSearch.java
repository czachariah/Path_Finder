package SearchAlgos;

import Grid.Grid;
import Grid.Cell;

/**
 * This is an abstract class that A* Search, Weighted A* Search and Uniform-Cost Search will extend from. 
 */
public abstract class AbstractSearch {
    // Global variables
    Cell[] path;
    Cell[] exploredCells;
    Grid grid;
    int[][] start;
    int[][] end;

    // constructor
    public AbstractSearch(Grid curGrid) {
        grid = curGrid;
        start = curGrid.getStartCell();
        end = curGrid.getEndCell();
    } // ends the AbstractSearch() constructor

    // method used to run A* (will probably be used for others too, need to research)
    public Cell[] run() {
        return null;
    } // ends the run() method

    // method to get HCost (Heuristic Cost) , A* and Weighted A* will Override this method
    public float getHCost(Cell cell) {
        return 0f;
    } // ends the getHCost() method

} // ends the AbstractSearch class
