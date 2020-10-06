package Grid;

/**
 * This is the Grid class.
 * This class can be used in order to create the grid of cells and construct the map.
 * @author Chris Zachariah
 */
public class Grid {
    /* Global varibales*/
    private final int HEIGHT = 120;
    private final int WIDTH = 160;
    private Cell[][] grid;

    /**
     * This is the Grid constructor.
     * This will initialize the grid will unblocked cells. 
     */
    public Grid() {
        this.grid = new Cell[HEIGHT][WIDTH];
        // create initial grid of unblocked cells
        for (int i = 0 ; i < HEIGHT ; ++i) {
            for (int j = 0 ; j < WIDTH ; ++j) {
                this.grid[i][j] = new Cell(j,i,1,0);
            }
        }
        generateMap();
    } // ends the Grid() constructor


    /**
     * This method will be used in order to generate all the various types and points in the map.
     */
    private void generateMap() {
        // 1.  start with getting the 8 random points and setting the hard type areas
        setHardCells();
    } // ends the generateMap() 

    /**
     * This method will be used in order to set all the hard to traverse cells in the grid.
     */
    private void setHardCells() {
        
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
     * a => unblocked w/ highway
     * b => hard w/ highway
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
