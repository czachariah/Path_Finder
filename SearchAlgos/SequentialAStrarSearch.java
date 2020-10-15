package SearchAlgos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import Grid.Grid;
import Heuristic.Heuristic;
import Grid.Cell;

/**
 * This is the Sequential A Star Search Class.
 * This search method will use heuristics sequentially as it tries to find the path from start to end.
 */
public class SequentialAStrarSearch {

    // Global Variables
    List<Cell> path;                    // shortest path
    Set<Cell> exploredCells;            // list of explored Cells
    
    Heuristic[] heuArr;                          // array of heuristics
    ArrayList<PriorityQueue<Cell>> fringeArr;    // array of fringes for each heurisitc
   
    Cell[][] grid;                      // main grid we are working with 
    int[][] start;                      // start Cell location
    int[][] end;                        // end Cell location
    Cell cStart;                        // starting cell
    Cell cTarget;                       // ending cell

    float w1;                           // weight used to inflate heurisitcs
    float w2;                           // weight used to help prioritze inadmissible heurisitcs

    /**
     * Row 0 is Cell A and Column 0 is Cell B;
     * 
     * Use these charts to find out the gCost from Cell A to Cell B;
     * 
     * How to read:
     *  Column/Row 0 = normal
     *  Column/Row 1 = hard to traverse
     *  Column/Row 2 = blocked
     *  Column/Row 3 = normal highway
     *  Column/Row 4 = hard highway
     *  
     */
    // 
    final float[][] HORIZONAL_VERTICAL_COST = 
    {
			{ 1f    ,    1.5f  ,   -1f  ,   1f     ,   1.50f  },
			{ 1.5f  ,    2f    ,   -1f  ,   1.5f   ,   2f     },
			{ -1f   ,    -1f   ,   -1f  ,   -1f    ,   -1f    },
			{ 1f    ,    1.50f ,   -1f  ,   0.25f  ,   0.375f },
			{ 1.5f  ,    2f    ,   -1f  ,   0.375f ,   0.5f   }
	};
    
    
    final float NormToNormDiag = (float) Math.sqrt(2.0);
	final float NormToHardDiag = ((float) Math.sqrt(2.0) + (float) Math.sqrt(8.0))/2;
	final float HardToHardDiag = (float) Math.sqrt(8.0);
	
    final float[][] DIAGONAL_COSTS = 
    {
			{ NormToNormDiag   ,   NormToHardDiag   ,    -1f   ,      NormToNormDiag      ,       NormToHardDiag      },
			{ NormToHardDiag   ,   HardToHardDiag   , 	 -1f   ,      NormToHardDiag      ,       HardToHardDiag      },
			{     -1f          ,        -1f         ,    -1f   ,           -1f            , 		   -1f            },
			{ NormToNormDiag   ,   NormToHardDiag   , 	 -1f   ,    NormToNormDiag/4.0f   ,     NormToHardDiag/4.0f   },
			{ NormToHardDiag   ,   HardToHardDiag   , 	 -1f   ,    NormToHardDiag/4.0f   ,     HardToHardDiag/4.0f   }
	};


    /**
     * This is the constructor of the Sequential A* Search.
     * @param grid the current grid to run the search on
     * @param arr an array of all the heuristics (with the first one being admissible)
     * @param weight1 the weight to inflate the h-values of the the heuristics (all except the first)
     * @param wieght2 the wieght to use as a factor in prioritizing the inadmissible over the admissible heuristic
     */
    public SequentialAStrarSearch(Grid curGrid , Heuristic[] arr , float weight1 , float wieght2) {
        this.grid = curGrid.getGrid();
        this.start = curGrid.getStartCell();
        this.end = curGrid.getEndCell();

        cStart = grid[start[0][0]][start[0][1]];
		cTarget = grid[end[0][0]][end[0][1]];

        this.heuArr = arr;
        this.w1 = weight1;
        this.w2 = wieght2;

        this.path = new LinkedList<>();
        this.exploredCells = new HashSet<>();

        // initilize the finge array and assign the start and goal to each one
        this.fringeArr = new ArrayList<PriorityQueue<Cell>>(heuArr.length);
        for(int i = 0; i < heuArr.length; i++) {
			fringeArr.add(new PriorityQueue<Cell>( new Comparator<Cell>() {
				public int compare(Cell o1, Cell o2) {
					return Float.compare(o1.getfCost(), o2.getfCost());
				}
			}));
			addToFringe(cStart, null, getGCost(cStart, cStart), getHCost(cStart, i), i); // parent = null
			addToFringe(cTarget, null, (float)Integer.MAX_VALUE, 0, i); // parent = null
		}
    } // ends the SequentialAStarSearch() constructor


    /**
     * This method is used to add a Cell to it's respective fringe.
     * @param cur the current cell
     * @param parent the cell's parent cell
     * @param gCost the total gCost of the current cell
     * @param hCost the hCost of the current cell
     * @param whichFringe the index of the fringe to where the cell will be added
     */
    public void addToFringe(Cell cur, Cell parent, float gCost, float hCost , int whichFringe) {
        PriorityQueue<Cell> chosenFringe = fringeArr.get(whichFringe);
        
        if (chosenFringe.contains(cur)) {
            chosenFringe.remove(cur);
        }
        chosenFringe.add(cur);
        
        cur.setgCost(gCost);
        cur.sethCost(hCost);
        
        cur.visited = true;
        exploredCells.add(cur);
    } // ends teh addToFringe() method

    /**
     * This method will be used in order to get the gCost of the current Cell its neighbor
     * @param cur the current cell
     * @param next the neighboring cell
     * @return the gCost between the two cells
     */
    public float getGCost(Cell cur , Cell next) {
        if ((cur.getX() == next.getX()) && (cur.getY() == next.getY())) { // same cell
            return 0f;
        } else if (((cur.getX() == next.getX()) && (Math.abs(cur.getY() - next.getY()) == 1)) 
        || ((Math.abs(cur.getX() - next.getX()) == 1) && (cur.getY() == next.getY()))) { // horizontal or vertical neighbor
            return costHorVert(cur, next);
        } else if ((Math.abs(cur.getX() - next.getX()) == 1) && (Math.abs(cur.getY() - next.getY()) == 1)) { // diagonal neighbor
            return costDiag(cur, next);
        } else {
            return -1f;
        }
    } // ends the getGCost() method


    /**
     * This method is used to get the gCost from two neighboring Cells that are horizontal or verticle from each other.
     * @param from the current cell
     * @param to the neighboring cell
     * @return the gCost
     */
    public float costHorVert(Cell from, Cell to) {
        int type_from = from.getType();
        int type_to = to.getType();

        // need to get the type of the cell and match the gCost chart type
        // this idea to make a chart was an after throught that came in order to optimize and not use code to calculate all the costs at every iteration

        if (type_from == 0) { // blocked
            type_from = 2;
        } else if (type_from == 1) { // easy
            type_from = 0;
        } else if (type_from == 2) { // hard
            type_from = 1;
        } else if (type_from == 3) { // easy highway
            type_from = 3;
        } else { // hard highway
            type_from = 4;
        }

        if (type_to == 0) { // blocked
            type_to = 2;
        } else if (type_to == 1) { // easy
            type_to = 0;
        } else if (type_to == 2) { // hard
            type_to = 1;
        } else if (type_to == 3) { // easy highway
            type_to = 3;
        } else { // hard highway
            type_to = 4;
        }

        return HORIZONAL_VERTICAL_COST[type_from][type_to];
    } // ends the costHorVert() method


    /**
     * This method is used in order to get the gCost between two neighboring cells that are diagonal from each other.
     * @param from the current cell
     * @param to the neighboring cell
     * @return the gCost
     */
    public float costDiag(Cell from, Cell to) {
        int type_from = from.getType();
        int type_to = to.getType();

        // need to get the type of the cell and match the gCost chart type
        // this idea to make a chart was an after throught that came in order to optimize and not use code to calculate all the costs at every iteration

        if (type_from == 0) { // blocked
            type_from = 2;
        } else if (type_from == 1) { // easy
            type_from = 0;
        } else if (type_from == 2) { // hard
            type_from = 1;
        } else if (type_from == 3) { // easy highway
            type_from = 3;
        } else { // hard highway
            type_from = 4;
        }

        if (type_to == 0) { // blocked
            type_to = 2;
        } else if (type_to == 1) { // easy
            type_to = 0;
        } else if (type_to == 2) { // hard
            type_to = 1;
        } else if (type_to == 3) { // easy highway
            type_to = 3;
        } else { // hard highway
            type_to = 4;
        }

        return DIAGONAL_COSTS[type_from][type_to];
    }// ends the costDiag() method


    /**
     * This method is used to find the HCost for a specific Cell.
     * @param cell the cell that will be used in order to find the hCost
     * @param whichFringe is the index number to find which fringe-heuristic to use
     * @return the hCost
     */
    public float getHCost(Cell cell, int whichFringe) {
        return w1 * heuArr[whichFringe].getHeuristic(cell);
    } // ends the getHCost() method

} // ends the SequentialAStarSearch class
