package Grid;

/**
 * This is the Cell class. This will be used in order to construct the Grid.
 * Each Cell has its own unique properties.
 * 
 * @author Chris Zachariah
 */
public class Cell {
    // Global variables
    private int x,y;                 // these two values represent the (x,y) coordinates of the current cell
    private int type;                // this value represents the type of the variable
    private int highwayDir;          // this value indicates what direction the highway flows
    
	//gCost -> distance from starting cell
	//hCost -> distance from target cell
	//fCost = gCost + hCost
	private float gCost;
	private float hCost;
	
	//keep track of shortest path found
	public Cell parent;
	
	public boolean visited = false;


    /**
     * This is the constructor of the Cell class
     * @param x is the x-coordinate for this Cell
     * @param y is the y-coordinate for this Cell
     * @param type represents the type of the Cell
     * @param highwayDir is the direction of the highway of the Cell.(if it has one)
     */
    public Cell(int x, int y, int type, int highwayDir) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.highwayDir = highwayDir;
        this.gCost = 0;
        this.hCost = 0;
    } // ends the Cell() constructor


    /**
     * This method will return the location of the cell on the grid.
     * The x value is between [0,119] and y value is between [0,159].
     * @return  int array with indexes 0 and 1 corresponding to the x and y coordinates respectively.
     */
    public int[] getLocation() {
        return new int[] {this.x,this.y};
    } // ends the getLocation() method


    /**
     * This method will return the type of the Cell.
     * Types:
     * 0 => blocked cell
     * 1 => unblocked cell
     * 2 => hard cell
     * 3 => unblocked w/ highway
     * 4 => hard w/ highway
     * @return an int that represents the type of the cell
     */
    public int getType() {
        return this.type;
    } // ends the getType() method

    
    /**
     * This method will change the type of the Cell.
     * @param type is the new type of the Cell to change to
     */
    public void changeType(int type) {
        this.type = type;
    } // ends the changeType() method

    /**
     * This method will check if the Cell has a highway.
     * true => Cell has a highway
     * false => Cell does not have a highway
     * @return a boolean that tells if the Cell has a highway
     */
    public boolean hasHighway() {
        if (highwayDir == 0) {
            return false;
        } else {
            return true;
        }
    } //  ends the hasHighway() method


    /**
     * This method will return the direction of the highway that goes through the cell.
     * 0 => no highway
     * 1 => North
     * 2 => East
     * 3 => South
     * 4 => West
     * @return 0 if there is no highay or [1,4] which represents the direction of the highway
     */
    public int getHighwayDir() {
        return this.highwayDir;
    } // ends the getHighwayDir() method


    /**
     * This method will change the direction of the highway in the Cell.
     * @param dir is the new direction of the Cell
     */
    public void changeHighwayDir(int dir) {
        this.highwayDir = dir;
    } // ends the changeHighDir() method
    
    public boolean equals(Object o) {
    	if(!(o instanceof Cell))
    		return false;
    	Cell cell = (Cell) o;
  
    	return this.x == cell.x && this.y == cell.y;
    }
    
    public int getX() {
    	return this.x;
    }
    
    public int getY() {
    	return this.y;
    }
    
	public float getfCost() {
		return this.gCost + this.hCost;
	}
	
	public float getgCost() {
		return this.gCost;
	}
	
	public float gethCost() {
		return this.hCost;
	}
	
	public void setgCost(float g) {
		this.gCost = g;
	}
	
	public void sethCost(float h) {
		this.hCost = h;
	}

} // ends the Cell class
