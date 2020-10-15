package SearchAlgos;

import Grid.Grid;
//import sun.jvm.hotspot.utilities.ConstIterator;
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


    // constructor
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
                return Float.compare(one.getgCost() + one.getfCost(), two.getgCost() + two.getfCost());
            }
        });

    } // ends the AbstractSearch() constructor



    // method to find the HCost (other classes will Override)
    public float getHCost(Cell cell) {
        return 0f;
    } // ends the getHCost() method

    public List<Cell> getPath() {
        return path;
    }

    public Set<Cell> getExploredCells() {
        return exploredCells;
    }

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
    }

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
    }

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
    }

    // method to run the algo
    public void run() {        
        // get the start and end
        Cell cStart = grid[start[0][0]][start[0][1]];
		Cell cTarget = grid[end[0][0]][end[0][1]];
        
        // add the start to the fringe
        addToFringe(cStart, null, getGCost(cStart,cStart), getHCost(cStart)); // parent = null

		while(fringe.size() > 0) {
            // take the head of the queue (should be minimum fcost by defualt)
            Cell curr = fringe.remove();
            curr.visited = true;
            exploredCells.add(curr);
            
            System.out.println(curr.getX() + " , " + curr.getY());

            // check if it is the goal Cell
			if(curr.getX() == cTarget.getX() && curr.getY() == cTarget.getY()){
				path = getShortestPath(cStart, cTarget, path);
				return;
			}
            
            // get neighbors and check 
			List<Cell> neighbors = getNeighbors(curr);
			for(Cell c : neighbors) {
				if(c.getType() == 0 || exploredCells.contains(c)) {
                    continue;
                }	
                float gCostCurrToNeighbor = curr.getgCost() + getGCost(curr, c);
				if(gCostCurrToNeighbor < c.getgCost() || !fringe.contains(c)) {			
					if(!fringe.contains(c)) {
                        addToFringe(c, curr, getGCost(curr, c), getHCost(c));
                    }
				}
			}
        } // ends the while loop

        // if the algorithm gets here, that means that there is no route from the start to the goal
        System.out.println("NO PATH FOUND");
        path = null;
    } // ends the run() method


    // call this method once the goal has been reached in order to get the full path from start to end
    public List<Cell> getShortestPath(Cell start, Cell target, List<Cell> path){
		Cell ptr = target;
		while(ptr != null) { // will go backwards in the path to start whose parent would be null
			path.add(ptr);
			ptr = ptr.parent;
		}
		return path;
	}
    
    
 
    // method used to add new cells to the fringe
    public void addToFringe(Cell cur , Cell parent , float gcost , float hcost) {
        cur.setgCost(gcost);
        cur.sethCost(hcost);
        cur.parent = parent;
        
        if (fringe.contains(cur)){
            fringe.remove(cur);
        }
        fringe.add(cur);
        exploredCells.add(cur);
        cur.visited = true;

    }

    
    // this method returns a list of the neighbors of the Cell
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
    }


} // ends the AbstractSearch class

/**
 * // look through and find the smallest fcost
            Cell[] arr = new Cell[fringe.size()];
            fringe.toArray(arr);
            for(int i = 0; i < arr.length; i++) {
				if(arr[i].getfCost() < curr.getfCost() || fringe.get(i).getfCost() == curr.getfCost() && fringe.get(i).gethCost() < curr.gethCost()) {
					curr = fringe.get(i);
				}
			}
            fringe.remove(curr);
            


    /**
	 * 
	 * Still a little confused about heuristics, but I believe the getDistance method is the only method we
	 * will need to change. Logic should stay the same for a*
	 * 
	 * sqrt(8) represents the distance to travel diagonally across a cell
	 * 2 represents the distance to travel horizontally or vertically across a cell
	 *
	public float getDistance(Cell a, Cell b) {
		float distX = Math.abs(a.getX() - b.getX());
		float distY = Math.abs(a.getY() - b.getY());
		
		return distX > distY ? (float)Math.sqrt(8) * distY + 2 * (distX - distY) : (float)Math.sqrt(8) * distX + 2 * (distY - distX);
    }
   
 */
