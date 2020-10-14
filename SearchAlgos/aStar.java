package SearchAlgos;

import Grid.Cell;
import Grid.Grid;
import Heuristic.Heuristic;

public class aStar extends AbstractSearch{
	
	Grid grid;
	Heuristic h;

	public aStar(Grid grid , Heuristic h) {
		super(grid);
		this.h = h; 
	} // ends the constructor

	@Override
	public float getHCost(Cell cell) {
        return h.getHeuristic(cell);
    } // ends the getHCost() method
} // ends the aStar class
