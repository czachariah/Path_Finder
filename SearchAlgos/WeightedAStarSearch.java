package SearchAlgos;

import Heuristic.Heuristic;
import Grid.Grid;
import Grid.Cell;

/**
 * This is the Weighted A* Search Class.
 */
public class WeightedAStarSearch extends AbstractSearch {
    Grid grid;
    Heuristic h;
    float weight;

    /**
     * This is the constructor of the WeightedAStarSearch class
     * @param grid the current grid
     * @param h the current heuristic to use
     * @param weight the current weight to use with the heuristic
     */
    public WeightedAStarSearch(Grid grid, Heuristic h , float weight) {
        super(grid);
        this.h = h;
        this.weight = weight;
    }

    @Override
	public float getHCost(Cell cell) {
        return h.getHeuristic(cell) * weight;
    } // ends the getHCost() method
}
