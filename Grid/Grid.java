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
    } // ends the Grid() constructor

    

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
