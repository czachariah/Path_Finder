package SearchAlgos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import Grid.*;
import Heuristic.*;

/**
 * This is the Sequential A Star Search Class.
 * This search method will use heuristics sequentially as it tries to find the path from start to end.
 */
public class SequentialAStrarSearch {
    // Global variables
    Cell[][] grid;                // main grid to conduct searches on
    float w1;                     // w1(≥ 1.0) is used to inflate the heuristic values for each of the search procedures, similar to Weighted-A
    float w2;                     // w2(≥ 1.0) is used as a factor to prioritize the inadmissible search processes over the anchor, admissible one
    
    Heuristic[] hArray;                             // array to hold all the heuristics
    List<Cell> path;                                // final path from start to goal in the grid
    HashMap<Cell, Cell[]> mapOfAllExploredCells;    // a map of all the explored nodes and each of the ways it was explored 
    ArrayList<PriorityQueue<Cell>> allFringes;      // list of all the fringes for each of the heuristics

    Cell cStart;        // start cell
    Cell cTarget;       // target cell
    int[][] start;      // start Cell location
    int[][] end;        // target Cell location



    /**
     * This is the constructor of the Sequential A* Search.
     * @param grid the current grid to run the search on
     * @param weight1 the weight to inflate the h-values of the the heuristics (all except the first)
     * @param wieght2 the wieght to use as a factor in prioritizing the inadmissible over the admissible heuristic
     */
    public SequentialAStrarSearch(Grid curGrid , float weight1 , float wieght2) {
        this.grid = curGrid.getGrid();
        this.w1 = weight1;
        this.w2 = wieght2;
        
        this.hArray = new Heuristic[5];
		hArray[0] = new ManhattanDistance(curGrid);
		hArray[1] = new ManhattanDistanceByFour(curGrid);
		hArray[2] = new Chebyshev(curGrid);
		hArray[3] = new EuclideanDistance(curGrid);
        hArray[4] = new EuclideanDistanceByFour(curGrid);

        this.start = curGrid.startCell; 
        this.end = curGrid.endCell;

        this.cStart = grid[start[0][0]][start[0][1]];
        this.cTarget = grid[end[0][0]][end[0][1]];

        this.mapOfAllExploredCells = new HashMap<Cell, Cell[]>();
		this.allFringes = new ArrayList<PriorityQueue<Cell>>(5);
      
    } // ends the SequentialAStarSearch() constructor





} // ends the SequentialAStarSearch class
