package SearchAlgos;

import Grid.Cell;
import Grid.Grid;
import Heuristic.Heuristic;

/**
 * This is the A* Search Class.
 */
public class AStarSearch extends AbstractSearch{
	
	// Global variables
	Grid grid;
	Heuristic h;

	/**
	 * This is the constructor of the AStarSearch class.
	 * @param grid the current grid
	 * @param h the current heuristic to use
	 */
	public AStarSearch(Grid grid , Heuristic h) {
		super(grid);
		this.h = h; 
	} // ends the constructor


	@Override
	public float getHCost(Cell cell) {
        return h.getHeuristic(cell);
    } // ends the getHCost() method
} // ends the AStarSearch class
