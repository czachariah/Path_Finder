
import Grid.*;
import Heuristic.*;
import SearchAlgos.*;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.HashMap;

public class Test {
    public static void main(String[] args){
        float runTime = 0 ;
        int pathLength = 0;
        int numCellsVisited = 0;
        float pathCost = 0;

        for (int i = 1 ; i <= 5 ; ++i) {
            for (int j = 0 ; j <= 9 ; ++j) {
                String fileName = "NEW_GRID" + i + "." + j + ".txt";
                Grid grid = new Grid();
                File file = new File("./SavedGrids/" + fileName);
                grid.importGrid(file);

                //ManhattanDistance heu1 = new ManhattanDistance(grid);
                //EuclideanDistance heu2 = new EuclideanDistance(grid);
                //Chebyshev heu3 = new Chebyshev(grid);
                //ManhattanDistanceByFour heu4 = new ManhattanDistanceByFour(grid);
                //EuclideanDistanceByFour heu5 = new EuclideanDistanceByFour(grid);

                Heuristic[] arr = new Heuristic[5];
                arr[2] = new ManhattanDistance(grid);
                arr[3] = new ManhattanDistanceByFour(grid);
                arr[4] = new EuclideanDistance(grid);
                arr[0] = new EuclideanDistanceByFour(grid);
                arr[1] = new Chebyshev(grid);

                //AStarSearch search = new AStarSearch(grid,heu);
                //WeightedAStarSearch search = new WeightedAStarSearch(grid,heu,3f);
                //UniformCostSearch search = new UniformCostSearch(grid);

                SequentialAStarSearch search = new SequentialAStarSearch(grid,3f,3f,arr);

                long startTime = System.currentTimeMillis();
                search.run();
                long totalTime = System.currentTimeMillis() - startTime;
                List<Cell> path = search.getPath();
                HashMap<Cell, Cell[]> explored = search.getExploredCells();

                runTime += totalTime;
                pathLength += path.size();
                numCellsVisited += explored.size();
                pathCost += search.getPathCost();
            }
        }

        System.out.println("runTime : " + runTime/50.0);
        System.out.println("pathLength : " + pathLength/50.0);
        System.out.println("numCellsVisited : " + numCellsVisited/50.0);
        System.out.println("pathCost : " + pathCost/50.0);
        
    }
}
