package SearchAlgos;

import Grid.Grid;
import Grid.Cell;

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
    List<Cell> exploredCells;   // list of explored Cells
    PriorityQueue<Cell> fringe; // heap that holds cells to be looked at
   
    Cell[][] grid;              // main grid we are working with 
    int[][] start;              // start Cell
    int[][] end;                // end Cell



    // constructor
    public AbstractSearch(Grid curGrid) {
        this.grid = curGrid.getGrid();
        this.start = curGrid.getStartCell();
        this.end = curGrid.getEndCell();

        this.path = new LinkedList<>();
        this.exploredCells = new LinkedList<>();
        this.fringe = new PriorityQueue<>();

    } // ends the AbstractSearch() constructor



    // method to find the HCost (other classes will Override)
    public float getHCost(Cell cell) {
        return 0f;
    } // ends the getHCost() method

    public List<Cell> getPath() {
        return path;
    }

    // method to run the algo
    public void run() {
		Cell cStart = grid[start[0][0]][start[0][1]];
		Cell cTarget = grid[end[0][0]][end[0][1]];
		
		List<Cell> opened = new LinkedList<>();
		Set<Cell> closed = new HashSet<>();
		opened.add(cStart);
		
		//can optimize by using a heap (as per the instructions)
		//used a list for now because just trying to get a working algorithm first
		while(opened.size() > 0) {
			Cell curr = opened.get(0);
			curr.visited = true;
			for(int i = 1; i < opened.size(); i++) {
				if(opened.get(i).getfCost() < curr.getfCost() || opened.get(i).getfCost() == curr.getfCost() && opened.get(i).gethCost() < curr.gethCost()) {
					curr = opened.get(i);
				}
			}
			opened.remove(curr);
			closed.add(curr);
			if(curr.getX() == cTarget.getX() && curr.getY() == cTarget.getY()){
				path = printShortestPath(cStart, cTarget, path);
				return;
			}
			
			List<Cell> neighbors = getNeighbors(curr);
			for(Cell c : neighbors) {
				if(c.getType() == 0 || closed.contains(c))
					continue;
				
				float neighborToCurr = curr.getgCost() + getDistance(curr, c);
				if(neighborToCurr < c.getgCost() || !opened.contains(c)) {
					c.setgCost(neighborToCurr);
					c.sethCost(getDistance(curr, cTarget));
					c.parent = curr;
					
					if(!opened.contains(c))
						opened.add(c);
				}
				
			}
			
		}
    } // ends the run() method



    public List<Cell> printShortestPath(Cell start, Cell target, List<Cell> path){
		Cell ptr = target;
		while(ptr != start) {
			path.add(ptr);
			ptr = ptr.parent;
		}
		return path;
	}
    
    

	/**
	 * 
	 * Still a little confused about heuristics, but I believe the getDistance method is the only method we
	 * will need to change. Logic should stay the same for a*
	 * 
	 * sqrt(8) represents the distance to travel diagonally across a cell
	 * 2 represents the distance to travel horizontally or vertically across a cell
	 */
	public float getDistance(Cell a, Cell b) {
		float distX = Math.abs(a.getX() - b.getX());
		float distY = Math.abs(a.getY() - b.getY());
		
		return distX > distY ? (float)Math.sqrt(8) * distY + 2 * (distX - distY) : (float)Math.sqrt(8) * distX + 2 * (distY - distX);
	}


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
