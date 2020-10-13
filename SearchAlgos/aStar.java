package SearchAlgos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import Grid.Cell;
import Grid.Grid;

public class aStar {
	
	public void findPath(Grid grid) {
		
		Cell[][] cellArr = grid.getGrid();
		int[][] temp = grid.getStartCell();
		Cell cStart = cellArr[temp[0][0]][temp[0][1]];
		temp = grid.getEndCell();
		Cell cTarget = cellArr[temp[0][0]][temp[0][1]];
		
		List<Cell> opened = new LinkedList<>();
		Set<Cell> closed = new HashSet<>();
		opened.add(cStart);
		
		
		//optimize by using a heap, just trying to get a working algorithm first
		while(opened.size() > 0) {
			Cell curr = opened.get(0);
			for(int i = 1; i < opened.size(); i++) {
				if(opened.get(i).getfCost() < curr.getfCost() || opened.get(i).getfCost() == curr.getfCost() && opened.get(i).gethCost() < curr.gethCost()) {
					curr = opened.get(i);
				}
			}
			opened.remove(curr);
			closed.add(curr);
			if(curr.equals(cTarget))
				return;
			
		}
		
	}
	
	
	
}
