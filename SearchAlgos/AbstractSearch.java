package SearchAlgos;

import Grid.Grid;
import Grid.Cell;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * This is an abstract class that A* Search, Weighted A* Search and Uniform-Cost Search will extend from. 
 */
public abstract class AbstractSearch {

    // Global variables
    List<Cell> path;            // shortest path
    Set<Cell> exploredCells;    // list of explored Cells
    PriorityQueue<Cell> fringe; // heap that holds cells to be looked at
   
    Cell[][] grid;              // main grid we are working with 
    int[][] start;              // start Cell
    int[][] end;                // end Cell
    
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
     * This is the constructor the AbstractSearch Class.
     * @param curGrid is the grid to be searched
     */
    public AbstractSearch(Grid curGrid) {
        this.grid = curGrid.getGrid();
        this.start = curGrid.getStartCell();
        this.end = curGrid.getEndCell();

        this.path = new LinkedList<>();
        this.exploredCells = new HashSet<>();
        this.fringe = new PriorityQueue<>(new Comparator<Cell>(){
            // -#   -> one < two
            // 0    -> one == two
            // +#   -> one > two 
            public int compare(Cell one, Cell two) {
                return Float.compare(one.getfCost(), two.getfCost());
            }
        });

    } // ends the AbstractSearch() constructor



    /**
     * This method is used to find the HCost for a specific Cell.
     * @param cell the cell that will be used in order to find the hCost
     * @return the hCost
     */
    public float getHCost(Cell cell) {
        return 0f;
    } // ends the getHCost() method

    /**
     * This method will return the path that had been constructed by the algorithm.
     * @return the path
     */
    public List<Cell> getPath() {
        return path;
    }

    /**
     * This method will return a Set of all the Cells that have been explored by the algorithm.
     * @return the set of explored cells
     */
    public Set<Cell> getExploredCells() {
        return exploredCells;
    }



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
     * The main method that runs the base A* algorithm. 
     */
    public void run() {        
        // get the start and end
        Cell cStart = grid[start[0][0]][start[0][1]];
		Cell cTarget = grid[end[0][0]][end[0][1]];
        
        // add the start to the fringe
        addToFringe(cStart, null, getGCost(cStart,cStart), getHCost(cStart)); // parent = null
        //System.out.println(cStart.getfCost() + " = " + cStart.getgCost() + " + " + cStart.gethCost());
        // some info about how many cells the algo has to go through to get from start to end
        int numNodesSearched = 0;
		while(fringe.size() > 0) {
            numNodesSearched++;
            // take the head of the queue (should be minimum fcost by defualt becuase of the heap/priority queue)
            Cell curr = fringe.poll();
            curr.visited = true;
            exploredCells.add(curr);

            // check if it is the goal Cell
			if(curr.getX() == cTarget.getX() && curr.getY() == cTarget.getY()){
                path = getShortestPath(cStart, cTarget);
                System.out.println("Number of Nodes Looked Through: " + numNodesSearched);
				return;
			}
            
            // get neighbors and check if has been visited or not
			List<Cell> neighbors = getNeighbors(curr);
			for(Cell c : neighbors) {
				if(c.getType() == 0) {
                    continue;
                }	

                // find the new total gCost from current cell to the neighbor
                float gCostCurrToNeighbor = curr.getgCost() + getGCost(curr, c);
                if (!exploredCells.contains(c)) {
                    if (!fringe.contains(c)) { // not explored yet and isn't on the fringe
                        addToFringe(c, curr, gCostCurrToNeighbor, getHCost(c));
                    } else {
                        // already on the fringe, but if the neighbor seems to be a better/cheap path going through this current cell, 
                        // replace the gCost of this neightbor and update the fringe with it
                        if (gCostCurrToNeighbor <= c.getgCost()) {
                            addToFringe(c, curr, gCostCurrToNeighbor, getHCost(c)); // will do the update (remove and insert with new info) if necessary
                        }
                    } 
                }
            }
        } // ends the while loop

        // if the algorithm gets here, that means that there is no route from the start to the goal
        System.out.println("NO PATH FOUND");
        path = null;
    } // ends the run() method


    /**
     * This method is used in order to obtain the shortest path that has been built by the algorithm.
     * @param start the starting Cell
     * @param target the ending Cell
     * @param path the list that will take the contents of the path
     * @return the path that was generated
     */
    public List<Cell> getShortestPath(Cell start, Cell target){
        List<Cell> finalPath = new LinkedList<>();
        Cell ptr = target;
		while(ptr != null) { // will go backwards in the path to start whose parent would be null
			finalPath.add(0,ptr);
			ptr = ptr.parent;
		}
		return finalPath;
	} // ends the getShortestPath() method
    
    
 
    // method used to add new cells to the fringe
    /**
     * This method will be used to add new Cells to the fringe for the algorithm.
     * @param cur the current cell
     * @param parent the parent of the cell
     * @param gcost the gCost of the cell
     * @param hcost the hCost of the cell
     */
    public void addToFringe(Cell cur , Cell parent , float gcost , float hcost) {
        cur.setgCost(gcost);
        cur.sethCost(hcost);
        cur.setfCost(cur.getgCost() + cur.gethCost());
        cur.parent = parent;
        cur.visited = true;

        if (fringe.contains(cur)){
            fringe.remove(cur);
        }
        fringe.add(cur);
        exploredCells.add(cur);
        
    } // ends the addToFringe() method 

    
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
     * @return
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

    }


} // ends the AbstractSearch class