package SearchAlgos;

import Heuristic.Heuristic;
import Grid.Grid;


public class UniformCostSearch extends AbstractSearch {
    Grid grid;
    Heuristic h;

    public UniformCostSearch(Grid grid) {
        super(grid);
    }
}