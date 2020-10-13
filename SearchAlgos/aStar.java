package SearchAlgos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import Grid.Cell;
import Grid.Grid;

public class aStar {
	
	public aStar() {
		
	}
	
	public void findPath(Grid grid, List<Cell> path) {
		
		Cell[][] cellArr = grid.getGrid();
		int[][] temp = grid.getStartCell();
		Cell cStart = cellArr[temp[0][0]][temp[0][1]];
		temp = grid.getEndCell();
		Cell cTarget = cellArr[temp[0][0]][temp[0][1]];
		
		List<Cell> opened = new LinkedList<>();
		Set<Cell> closed = new HashSet<>();
		opened.add(cStart);
		
		
		//can optimize by using a heap (as per the instructions)
		//used a list for now because just trying to get a working algorithm first
		while(opened.size() > 0) {
			Cell curr = opened.get(0);
			for(int i = 1; i < opened.size(); i++) {
				if(opened.get(i).getfCost() < curr.getfCost() || opened.get(i).getfCost() == curr.getfCost() && opened.get(i).gethCost() < curr.gethCost()) {
					curr = opened.get(i);
				}
			}
			opened.remove(curr);
			closed.add(curr);
			if(curr.equals(cTarget)) {
				path = printShortestPath(cStart, cTarget);
				return;
			}
			
			List<Cell> neighbors = grid.getNeighbors(curr);
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
		
	}
	
	public List<Cell> printShortestPath(Cell start, Cell target){
		List<Cell> path = new LinkedList<>();
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
	
	
	
	
	
}
