package SearchAlgos;

import Grid.Cell;

public class Node {
	
	//gCost -> distance from starting node
	//hCost -> distance from target node
	//fCost = gCost + hCost
	//Was thinking maybe don't need a node class?? since a node is essentially a cell why not just put all these variables in Cell.java?
	
	float gCost;
	float hCost;
	
    public Node(Cell cell) {
    	this.gCost = 0;
    	this.hCost = 0;
    }

	public float getfCost() {
		return this.gCost + this.hCost;
	}
}
