package Grid;
import java.util.Random;

/**
 * This is the Grid class.
 * This class can be used in order to create the grid of cells and construct the map.
 * @author Chris Zachariah
 */
public class Grid {
    /* Global varibales*/
    private final int HEIGHT = 120;
    private final int WIDTH = 160;

    private final int NUMBER_HARD_CELL_CENTERS = 8;
    private final int HARD_CELL_AREA = 31;
    private final float HARD_CELL_PROB = 0.5f;

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
                    float curProb = (rand.nextInt(10)+1)/10f;
                    if (curProb >= HARD_CELL_PROB) {
                        this.grid[k][j].changeType(2);
                    }
                }
            }

        } // ends the for loop
    } // ends the setHardCells() method


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
     * 3 => unblocked w/ highway
     * 4 => hard w/ highway
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
                            System.out.print(3);
                        } else {
                            System.out.print(4);
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
