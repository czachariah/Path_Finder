package Grid;
import java.util.Random;


/**
 * This is the Grid class.
 * This class can be used in order to create the grid of cells and construct the map.
 * @author Chris Zachariah
 */
public class Grid {
    /* Global varibales*/
    private final int HEIGHT = 120;     // height of the grid
    private final int WIDTH = 160;      // width of the grid

    private final int NUMBER_HARD_CELL_CENTERS = 8; // the total number of hard cell centers
    private final int HARD_CELL_AREA = 31;          // the total square area the hard cell centers cover
    private final float HARD_CELL_PROB = 0.5f;      // the probability a cell in the hard area can become a hard cell

    private final int NUMBER_OF_HIGHWAYS = 4;               // total number of highways to create
    private final int NUMBER_OF_HIGHWAY_TRIES = 10;         // total number of tries to make highways before restarting the algorithm
    private final int FIRST_HIGHWAY_PATH = 20;              // length of first straight path before highways starts turning
    private final int MIN_HIGHWAY_LENGTH = 100;             // minimum length of the highway
    private final float HIGHWAY_STAYS_SAME_DIR = 0.6f;      // probability that the highway stays in the same direction
    private final float HIGHWAY_GOES_UP_OR_LEFT = 0.2f;     // probability that the highway goes in the up or left direction 
    private final float HIGHWAY_GOES_DOWN_OR_RIGHT = 0.2f;  // probability that the highway goes in the down or right direction 

    private Cell[][] grid;
    private int[][] hardCellCenters = new int[NUMBER_HARD_CELL_CENTERS][2];
    private Random rand = new Random(); 


    /**
     * This is the Grid constructor.
     * This will initialize the grid will unblocked cells. 
     */
    public Grid() {
        this.grid = new Cell[HEIGHT][WIDTH];
        // create initial grid of unblocked cells
        for (int i = 0 ; i < HEIGHT ; ++i) {            // height   ==   rows    ==   y value
            for (int j = 0 ; j < WIDTH ; ++j) {         // width    ==   columns ==   x value
                this.grid[i][j] = new Cell(j,i,1,0);
            }
        }
        generateMap();
    } // ends the Grid() constructor



    /**
     * This method will be used in order to generate all the various types and points in the map.
     */
    private void generateMap() {
        setHardCells();
        setHighways();
    } // ends the generateMap() 



    /**
     * This method will be used in order to set all the hard to traverse cells in the grid.
     */
    private void setHardCells() {
        for (int i = 0 ; i < NUMBER_HARD_CELL_CENTERS ; ++i) {
            // get random (x,y)
            int xCenter = rand.nextInt(WIDTH);  // random value between [0, 160)
            int yCenter = rand.nextInt(HEIGHT); // random value between [0, 120)

            hardCellCenters[i][0] = xCenter;
            hardCellCenters[i][1] = yCenter;
            
            // get the hard cell area borders
            int left_border = xCenter - (HARD_CELL_AREA/2);
            int right_border = xCenter + (HARD_CELL_AREA/2);
            int top_border = yCenter - (HARD_CELL_AREA/2);
            int bottom_border = yCenter + (HARD_CELL_AREA/2);

            // make sure that all values are within range
            if (left_border < 0) { left_border = 0; }
            if (top_border < 0) { top_border = 0; }
            if (right_border >= WIDTH) { right_border = (WIDTH - 1); }
            if (bottom_border >= HEIGHT) { bottom_border = (HEIGHT - 1); }

            // go through the area and fill in the hard cells based on the probability
            for (int j = left_border; j <= right_border ; ++j) {
                for(int k = top_border; k <= bottom_border ; ++k) {
                    float curProb = (rand.nextInt(10)+1)/10f;   // get probability from [0.1 , 1.0]
                    if (curProb >= HARD_CELL_PROB) {
                        this.grid[k][j].changeType(2);
                    }
                }
            }
        } // ends the for loopß
    } // ends the setHardCells() method



    /**
     * This method will create and set the highways on the grid.
     */
    private void setHighways() {
        for (int i = 0 ; i < NUMBER_OF_HIGHWAYS ; ++i) {
            int[] startPoint = getBoundaryPoint();  // start point for the highway
            
        }
    } // ends the 



    /**
     * This method will choose a random boundary and a starting point.
     * @return an int[2] array which will be the starting point for the highway
     */
    private int[] getBoundaryPoint() {
        int randBound = rand.nextInt(4) + 1; // [1,4]
        int [] point = new int[2];
        if (randBound == 1) {  // Top Border
            point[0] = 0; 
            point[1] = rand.nextInt(160); // [0,159];

			while (this.grid[point[0]][point[1]].hasHighway()) { // Vailidation that chosen random point is not existing highway.
				point[0] = 0; 
                point[1] = rand.nextInt(160); // [0,159];
			}
			return point;
		} else if (randBound == 2) { // Right Border
            point[0] = rand.nextInt(120); // [0,119]
			point[1] = 159; 

			while (this.grid[point[0]][point[1]].hasHighway()) { // Vailidation that chosen random point is not existing highway.
				point[0] = rand.nextInt(120); // [0,119]
                point[1] = 159; 
            }
			return point;
        } else if (randBound == 3) {  // Bottom Border
            point[0] = 119;
			point[1] = rand.nextInt(160); // [0,159]

			while (this.grid[point[0]][point[1]].hasHighway()) { // Vailidation that chosen random point is not existing highway.
				point[0] = 119;
			    point[1] = rand.nextInt(160); // [0,159]
            }
			return point;
        } else {  // Left Border
            point[0] = rand.nextInt(120); // [0,119]
			point[1] = 0; 

			while (this.grid[point[0]][point[1]].hasHighway()) { // Vailidation that chosen random point is not existing highway.
				point[0] = rand.nextInt(120); // [0,119]
                point[1] = 0; 
            }
			return point;
        }
    } // ends getBoundaryPoint() method



    /**
     * This method will return the grid.
     * @return the Cell grid
     */
    public Cell[][] getGrid() {
        return this.grid;
    } // ends the getGrid() method



    /**
     * This method will print out the grid to Standard Output.
     * 0 => blocked
     * 1 => unblocked
     * 2 => hard
     * a => unblocked w/ highway    (type = 3)
     * b => hard w/ highway         (type = 4)
     */
    public void printGrid() {
        for (int i = 0 ; i < HEIGHT ; ++i) {
            for (int j = 0 ; j < WIDTH ; ++j) {
                Cell cur = this.grid[i][j];
                if (cur.getType() == 0) {
                    System.out.print(0);    // this is blocked (no need to check for highways)
                } else {
                    if (cur.hasHighway()) {
                        if (cur.getType() == 1) {
                            System.out.print("a");
                        } else {
                            System.out.print("b");
                        } 
                    } else {
                        System.out.print(cur.getType());
                    }
                }
            }
            System.out.println();
        }
    } // ends the printGrid() method
    
} // ends the Grid class
