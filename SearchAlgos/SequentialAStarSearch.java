package SearchAlgos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import Grid.*;
import Heuristic.*;

/**
 * This is the Sequential A Star Search Class.
 * This search method will use heuristics sequentially as it tries to find the path from start to end.
 */
public class SequentialAStarSearch {
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
    
    public int index;



    /**
     * This is the constructor of the Sequential A* Search.
     * @param grid the current grid to run the search on
     * @param weight1 the weight to inflate the h-values of the the heuristics (all except the first)
     * @param wieght2 the weight to use as a factor in prioritizing the inadmissible over the admissible heuristic
     */
    public SequentialAStarSearch(Grid curGrid , float weight1 , float wieght2) {
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



    

    /**
     * This method will run the main algorithm to find the path from start to goal.
     */
    public void run() {
        // first set up the array of fringe priority queues
        for (int i = 0 ; i < 5 ; ++i) {
            allFringes.add(new PriorityQueue<Cell>( new Comparator<Cell>() {
				public int compare(Cell first, Cell second) {
					return Float.compare(first.getfCost(), second.getfCost());
				}
			}));
			addCellToFringe(cStart, null, getGCost(cStart, cStart), getHCost(cStart, i), i);
			addCellToFringe(cTarget, null, Integer.MAX_VALUE, 0, i); // use the Intger.MAX_VALUE as infinity for the purpose of this algorithm
        } // ends the for loop

        /*
            For this algorithm, it is assumed that the heurisitc used in index 0 (the first one) is admissible
            and will be the anchor for the whole process.
        */
        PriorityQueue<Cell> admissibleFringe = allFringes.get(0);
        float admissCost, otherFringeCost;
        int numNodesSearched = 0; // just to count the number of nodes that were visited
        // this will be the main check for the algorithm : minimum cost must be less than infinity ( A.K.A. Integer.MAX_VALUE)
        while ((admissCost = getTotalFCost(admissibleFringe.peek()))  < Integer.MAX_VALUE) {
            //numNodesSearched++;
            // must go through all the other fringes in order to find one that has a lower fCost that the anchor (admissible one)
            for (int i = 1 ; i < 5 ; ++i) {
                numNodesSearched++;
                PriorityQueue<Cell> curFringe = allFringes.get(i);
                otherFringeCost = getTotalFCost(curFringe.peek());
                // here we check to see if this other fringe is getting us a better fCost that the admissible one 
                // (use w2 weight to help prioritize other firnge)
                if (otherFringeCost <= w2*admissCost) {
                    float goalGCost = mapOfAllExploredCells.get(cTarget)[i].getgCost(); // get the gCost of the target cell
                    if (goalGCost <= otherFringeCost) {
                        if (goalGCost < Integer.MAX_VALUE) {
                            // path was found, the target gCost was changed and the current fringe cell had a higher value
                            path = pathFound(i);
                            System.out.println("Number of Nodes Looked Through: " + numNodesSearched);
                            return;
                        }
                    } else {
                        exploreNeighbors(curFringe.poll(),i);
                    }
                } else {
                    float goalGCost = mapOfAllExploredCells.get(cTarget)[0].getgCost(); // get the gCost of the target cell using admissible heuristic
                    if (goalGCost <= admissCost) {
                        if (admissCost < Integer.MAX_VALUE) {
                            path = pathFound(0);
                            System.out.println("Number of Nodes Looked Through: " + numNodesSearched);
                            return;
                        }
                    } else {
                        exploreNeighbors(admissibleFringe.poll(),0);
                    }
                }
            } // ends for loop
        } // ends the while loop
        // algorithm did not find path
        path = null;
    } // ends the run() method


    /**
     * This method will build the path taken by the algorithm from start to end.
     * @param fringe the fringe where the goal was found
     * @return a linked list path from start to end
     */
    public List<Cell> pathFound(int fringe) {
        List<Cell> finalPath = new LinkedList<>();
        Cell ptr = mapOfAllExploredCells.get(cTarget)[fringe];
		while(ptr != null) { // will go backwards in the path to start whose parent would be null
            Cell temp = ptr.parent;
            finalPath.add(0,ptr);
			ptr = temp;
		}
		index = fringe;
		return finalPath;
    } // ends the pathFound() method


    /**
     * This method will return the path from start to finish.
     * @return the list path (could be null if a path was never found)
     */
    public List<Cell> getPath() {
        return path;
    } // ends the getPath() method


    /**
     * This method will return a map of all the Cells that have been explored by the algorithm.
     * @return the map of explored cells
     */
    public HashMap<Cell, Cell[]> getExploredCells() {
        return mapOfAllExploredCells;
    } // ends the getExploredCells() method


    /**
     * This method will be used in order to add Cells into the correct fringe.
     * @param cur the current cell to add
     * @param parent the parent of the cell
     * @param gCost the gCost of the current cell to add
     * @param hCost the hCost of the current cell to add
     * @param whichFringe the specific fringe to add the current cell to
     */
    public void addCellToFringe(Cell cur, Cell parent , float gCost , float hCost , int whichFringe) {
        PriorityQueue<Cell> correctFringe = allFringes.get(whichFringe);
        
        // if the cell is already in the fringe, this is most likely an newer update with another set of costs
        // so out with the old and in with the new
        if (isCellInFringe(cur, whichFringe)) {
            allFringes.get(whichFringe).remove(cur);
        }

        cur.setgCost(gCost);
        cur.sethCost(hCost);
        cur.visited = true;
        cur.parent = parent;
        cur.index = whichFringe;

        correctFringe.add(cur);
        insertCellIntoMap(cur, whichFringe);

    } // ends the addCellToFringe() method



    /**
     * This method will check if a cell is in a specific fringe.
     * @param cell the cell to check for
     * @param whichFringe the specific fringe to look into
     * @return true if the cell is found in the fringe and false otherwise
     */
    public boolean isCellInFringe(Cell cell, int whichFringe) {
        Cell[] specificCellGroup = mapOfAllExploredCells.get(cell);
        if (specificCellGroup != null && specificCellGroup[whichFringe] != null) {
            return true;
        } else {
            return false;
        }
	} // ends the isCellInFringe() method




    /**
     * This method will add a cell into the Map of all the visited cell in the correct index that matches the fringe that cell belongs to.
     * @param cell the cell to add
     * @param whichFringe the specifc fringe that the cell belongs to
     */
    public void insertCellIntoMap(Cell cell , int whichFringe) {
        /*
            The way the map works:
            - every cell key has a value that is an array of cells (5 of them to be exact)
            - each index in that array represents the heuristic it belongs to
            - at the end, the heurisitc that finds the goal will be traced by using these indices in the array in the map
        */
        Cell[] curCellHeuVersions = mapOfAllExploredCells.get(cell);
        if (curCellHeuVersions == null) { // current cell's first visit
            createPlaceForCellOnMap(cell);
            curCellHeuVersions = mapOfAllExploredCells.get(cell);
        }
        curCellHeuVersions[whichFringe] = cell;
    } // ends the insertCellIntoMap()



    /**
     * This method will create a new entry/place for the cell on the explored map.
     * @param cell the cell to make place on the map for
     */
    public void createPlaceForCellOnMap(Cell cell) {
        // create array to use as value for the key (cell)
        Cell[] cellArr = new Cell[6]; 
        for (int i = 0 ; i < 5 ; ++i) {
            Cell temp = new Cell(cell.getX(),cell.getY(),cell.getType(),cell.getHighwayDir());
            temp.setgCost(Integer.MAX_VALUE); // use the Intger.MAX_VALUE as infinity for the purpose of this algorithm
            temp.sethCost(0);
            cellArr[i] = temp;
        }
        mapOfAllExploredCells.put(cell, cellArr);
    } // ends the createPlaceForCellOnMap() method



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
     * @param whichHeuristic the specifc index number that represents which heurisitc to use to obtain the hCost
     * @return the hCost
     */
    public float getHCost(Cell cell , int whichHeurisitc) {
        // for this algorithm, use the w1 weight in order to inflate the heuristic values for each of the search procedures, similar to Weighted-A
        return w1*hArray[whichHeurisitc].getHeuristic(cell);
    } // ends the getHCost() method



    /**
     * This method will get the total fCost of a given cell.
     * @param cell the cell to find the fCost of
     * @return the fCost of the cell
     */
    public float getTotalFCost(Cell cell) {
        return cell.getgCost() + w1*cell.gethCost(); // remember to use w1 weight for inflation
    } // ends the getTotalFCost() method




    /**
     * This method will explore all the neighbors of the current node and add some to the fringe
     * @param cur the current cell
     * @param whichFringe the fringe the cell belongs to
     */
    public void exploreNeighbors(Cell cur , int whichFringe) {
        List<Cell> neighbors = getNeighbors(cur);
        cur.hasBeenExplored = true;

        for (Cell n : neighbors) {
            if (n.getType() == 0) {
                continue;
            }

            if (mapOfAllExploredCells.get(n) == null) {
                createPlaceForCellOnMap(n); // add new cell to the map
            }

            n = mapOfAllExploredCells.get(n)[whichFringe];
            float totalGCost = cur.getgCost() + getGCost(cur, n);
            if (totalGCost < n.getgCost()) {
                n.setgCost(totalGCost);
                if (!n.hasBeenExplored) {
                    addCellToFringe(n, cur, totalGCost, getHCost(n, whichFringe), whichFringe);
                }
                //addCellToFringe(n, cur, totalGCost, getHCost(n, whichFringe), whichFringe); // MY VERSION
            }
        }
    } // ends the exploreNeighbors() method




    /**
     * This method will get the neighbors of the current cell.
     * @param c is the current cell
     * @return a list of all the neighbors of the current cell
     */
    public List<Cell> getNeighbors(Cell c){
    	List<Cell> neighbors = new LinkedList<>();
    	Set<Cell> set = new HashSet<>();    // use thid for O(1) search
    	for(int i = -1; i <= 1; i++){
    		for(int j = -1; j <= 1; j++){
    			if(i == 0 && j == 0)
    				continue;
    			int x = c.getX() + i;
    			int y = c.getY() + j;
    			
    			if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && !set.contains(this.grid[x][y])) {
                    neighbors.add(this.grid[x][y]);
    				set.add(this.grid[x][y]);
    			}
    		}
    	}
    	return neighbors;
    } // ends the getNeighbors() method


    /**
     * This method will return the total cost of the shortest path obtained by the algorithm.
     * @return the total cost of the path
     */
    public float getPathCost() {
        if (path == null) {
            return -1f;
        }
        float totalCost = 0f;
        Cell cur = path.get(0);
        for (Cell next : path) {
            totalCost+= getGCost(cur, next);
            cur = next;
        }
        return totalCost;
    } // ends the getPathCost() method

} // ends the SequentialAStarSearch class
