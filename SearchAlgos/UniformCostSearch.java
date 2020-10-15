package SearchAlgos;

import Heuristic.Heuristic;
import Grid.Grid;

/**
 * This is the Uniform Cost Search Class.
 */
public class UniformCostSearch extends AbstractSearch {
    Grid grid;
    Heuristic h;

    /**
     * This is the constructor of the Uniform Cost Search class. 
     */
    public UniformCostSearch(Grid grid) {
        super(grid);
    }
}