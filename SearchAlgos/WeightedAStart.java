package SearchAlgos;

import Heuristic.Heuristic;
import Grid.Grid;
import Grid.Cell;

public class WeightedAStart extends AbstractSearch {
    Grid grid;
    Heuristic h;
    float weight;

    public WeightedAStart(Grid grid, Heuristic h , float weight) {
        super(grid);
        this.h = h;
        this.weight = weight;
    }

    @Override
	public float getHCost(Cell cell) {
        return h.getHeuristic(cell) * weight;
    } // ends the getHCost() method
}
