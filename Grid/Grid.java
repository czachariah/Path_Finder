package Grid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Random;

/**
 * This is the Grid class.
 * This class can be used in order to create the grid of cells and construct the map.
 */
public class Grid {
    /* Global varibales*/
    private final int HEIGHT = 120;     // height of the grid
    private final int WIDTH = 160;      // width of the grid

    private final int NUMBER_HARD_CELL_CENTERS = 8; // the total number of hard cell centers
    private final int HARD_CELL_AREA = 31;          // the total square area the hard cell centers cover
    private final float HARD_CELL_PROB = 0.5f;      // the probability a cell in the hard area can become a hard cell

    private final int NUMBER_OF_HIGHWAYS = 4;               // total number of highways to create
    private final int NUMBER_OF_HIGHWAY_TRIES = 5;          // total number of tries to make highways before restarting the algorithm
    private final int STANDARD_HIGHWAY_PATH = 20;           // length of cells turned into a highway of the same direction before turning
    private final int MIN_HIGHWAY_LENGTH = 100;             // minimum length of the highway
    private final float HIGHWAY_STAYS_SAME_DIR = 0.6f;      // probability that the highway stays in the same direction

    private final float BLOCKED_CELL_PER = 0.2f;            // the percentage of the probability that will be made up of blocked cells

    private final int MIN_DIST_BETWEEN_START_AND_END = 100; // the minimum distance between the start and end points
    private final int CHOICE_REGION_AREA = 20;              // size of the area that the start or end cell can be placed from a random border 

    private Cell[][] grid;                                                      // main grid
    private int[][] hardCellCenters = new int[NUMBER_HARD_CELL_CENTERS][2];     // array of all the hard cell centers
    private Random rand = new Random();                                         // randomizer
    public int[][] startCell = new int[1][2];                                   // location of the startCell
    public int[][] endCell = new int[1][2];                                     // location of the endCell



    /**
     * This is the Grid constructor.
     * This will initialize the grid will unblocked cells. 
     */
    public Grid() {
        this.grid = new Cell[HEIGHT][WIDTH];
        // create initial grid of unblocked cells
        for (int i = 0 ; i < HEIGHT ; ++i) {            
            for (int j = 0 ; j < WIDTH ; ++j) {
                this.grid[i][j] = new Cell(i,j,1,0);
            }
        }
    } // ends the Grid() constructor




    /**
     * This method will be used in order to generate all the various types and points in the map.
     */
    public void generateEntireGrid() {
        setHardCells();
        while (!setHighways()) {
            resetAllHighways();
        }
        setBlockedCells();
        setStartAndEnd();
    } // ends the generateEntireGrid() 




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
     * This method will set the highways on the grid.
     * @return true if the highways have been created and false otherwise
     */
    private boolean setHighways() {
        for (int i = 0 ; i < NUMBER_OF_HIGHWAYS ; ++i) {
            int curTry = 1;
            while (curTry <= NUMBER_OF_HIGHWAY_TRIES && !createHighway()) {  // will continue to try to make highways until the max number of tries
                ++curTry;
            }
            if (curTry > NUMBER_OF_HIGHWAY_TRIES) { // highway creation was unsuccessful and must start over again from the top
                return false;
            }
        }
        return true;
    } // ends the 




    /**
     * This method will be used in order to create the highway.
     * @return true if the highway is successfully created and false otherwise.
     */
    private boolean createHighway() {
        int highwayLen = 0;
        int[] startPoint = getBoundaryPoint();  // start point for the highway
        int curX = startPoint[0];
        int curY = startPoint[1];

        if (curX == -1 || curY == -1) {
            return false;
        }

        int dir = 0;
        if (curX == 0) { // Top Border
            dir = 3;
        } else if (curY == 159) { // Right Border
            dir = 4;
        } else if (curX == 119) { // Bottom Border
            dir = 1;
        } else { // Left Border
            dir = 2;
        }

        // create a structure to hold the current cell coordinates that are going to become highways
        LinkedList<int[]> list = new LinkedList<>();

        // get direction, make highway in that direction for 20 cells and repeat until you hit a boundary or another highway
        while (true) {
            for (int i = 1; i <= STANDARD_HIGHWAY_PATH ; ++i) {
                if (isValidCell(curX, curY)) {
                    if (this.grid[curX][curY].hasHighway()) {
                        resetCurrentHighway(list);
                        return false;
                    }
                } else {
                    if (highwayLen >= MIN_HIGHWAY_LENGTH) {
                        return true;
                    } else {
                        resetCurrentHighway(list);
                        return false;
                    }
                }
                
                if (i < STANDARD_HIGHWAY_PATH) {
                    this.grid[curX][curY].changeHighwayDir(dir);
                    if (this.grid[curX][curY].getType() == 1) {
                        this.grid[curX][curY].changeType(3);
                    } else if (this.grid[curX][curY].getType() == 2) {
                        this.grid[curX][curY].changeType(4);
                    } else {}
                    ++highwayLen;
                    int[] arr = new int[2];
                    arr[0] = curX;
                    arr[1] = curY;
                    list.addLast(arr);
                    
                    if (dir == 3) {
                        curX += 1;
                    } else if (dir == 4) {
                        curY -= 1;
                    } else if (dir == 1) {
                        curX -= 1;
                    } else {
                        curY += 1;
                    }
                } 
            }// ends the for-loop

            // now determine the new direction for the highway
            float probDir = (rand.nextInt(10)+1)/10f; // [0.1 , 1.0]
            if (probDir < HIGHWAY_STAYS_SAME_DIR) {
                if ((dir == 1) || (dir == 3)) { // current direction going north or south
                    probDir = (rand.nextInt(10)+1)/10f; // [0.1 , 1.0]
                    if (probDir <= 0.5) {
                        dir = 2;
                    } else {
                        dir = 4;
                    }
                } else { // current direction going east or west
                    probDir = (rand.nextInt(10)+1)/10f; // [0.1 , 1.0]
                    if (probDir <= 0.5) {
                        dir = 1;
                    } else {
                        dir = 3;
                    }
                }
            }
        } // ends the while-loop
    } // ends the createHighway() method




    /**
     * This method will check if a set of coordinates is valid inside the grid.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return  true if the (x,y) coordinate is valid / in-bounds else returns false
     */
    private boolean isValidCell(int x, int y) {
        if ((x < 0) || (x > 119)) {
            return false;
        }
        if ((y < 0) || (y > 159)) {
            return false;
        }
        return true;
    } // ends the isValidCell() method





    /**
     * This method will be used in order to reset the current highway.
     * @param list is the list of coordinates to go through and revert back to normal
     */
    private void resetCurrentHighway(LinkedList<int[]> list) {
        for (int[] arr : list) {
            if (this.grid[arr[0]][arr[1]].getType() == 3) {
                this.grid[arr[0]][arr[1]].changeType(1);
                this.grid[arr[0]][arr[1]].changeHighwayDir(0);
            } else if (this.grid[arr[0]][arr[1]].getType() == 4){
                this.grid[arr[0]][arr[1]].changeType(2);
                this.grid[arr[0]][arr[1]].changeHighwayDir(0);
            } else {}
        }
    } // ends the resetCurrentHighway() method
    




    /**
     * This method will be called in order to reset all the highways on the grid.
     */
    private void resetAllHighways() {
        for (int i = 0; i < HEIGHT ; ++i) {
            for (int j = 0 ; j < WIDTH ; ++j) {
                if (this.grid[i][j].getType() == 3) {
                    this.grid[i][j].changeType(1);
                    this.grid[i][j].changeHighwayDir(0);
                }
                if (this.grid[i][j].getType() == 4) {
                    this.grid[i][j].changeType(2);
                    this.grid[i][j].changeHighwayDir(0);
                }
            }
        }
    } // ends the resetHighways() method





    /**
     * This method will choose a random boundary and a starting point.
     * @return an int[2] array which will be the starting point for the highway.
     */
    private int[] getBoundaryPoint() {
        int randBound = rand.nextInt(4) + 1; // [1,4]
        int [] point = new int[2];
        if (randBound == 1) {  // Top Border
            point[0] = 0; 
            point[1] = rand.nextInt(WIDTH); // [0,159];
            int num = 0;
            while (this.grid[point[0]][point[1]].hasHighway()) { // Vailidation that chosen random point is not existing highway.
				point[0] = 0; 
                point[1] = rand.nextInt(WIDTH); // [0,159];
                ++num;
                if (num == 30) { // In the case that the border is taken over by a highway and this goes into an infinite loop.
                    point[0] = -1; 
                    point[1] = -1;
                    return point;
                }
			}
			return point;
        } else if (randBound == 2) { // Right Border
            point[0] = rand.nextInt(HEIGHT); // [0,119]
            point[1] = WIDTH-1; 
            int num = 0;
			while (this.grid[point[0]][point[1]].hasHighway()) { // Vailidation that chosen random point is not existing highway.
                point[0] = rand.nextInt(HEIGHT); // [0,119]
                point[1] = WIDTH-1;
                ++num;
                if (num == 30) { // In the case that the border is taken over by a highway and this goes into an infinite loop.
                    point[0] = -1; 
                    point[1] = -1;
                    return point;
                }
            }
			return point;
        } else if (randBound == 3) {  // Bottom Border
            point[0] = HEIGHT-1;
            point[1] = rand.nextInt(WIDTH); // [0,159]
            int num = 0;
			while (this.grid[point[0]][point[1]].hasHighway()) { // Vailidation that chosen random point is not existing highway.
                point[0] = HEIGHT-1;
                point[1] = rand.nextInt(WIDTH); // [0,159]
                ++num;
                if (num == 30) { // In the case that the border is taken over by a highway and this goes into an infinite loop.
                    point[0] = -1; 
                    point[1] = -1;
                    return point;
                }
            }
			return point;
        } else {  // Left Border
            point[0] = rand.nextInt(HEIGHT); // [0,119]
            point[1] = 0; 
            int num = 0;
			while (this.grid[point[0]][point[1]].hasHighway()) { // Vailidation that chosen random point is not existing highway.
                point[0] = rand.nextInt(HEIGHT); // [0,119]
                point[1] = 0; 
                ++num;
                if (num == 30) { // In the case that the border is taken over by a highway and this goes into an infinite loop.
                    point[0] = -1; 
                    point[1] = -1;
                    return point;
                }
            }
			return point;
        }
    } // ends getBoundaryPoint() method





    /**
     * This method will add blocked cells to 20% of the entire grid.
     */
    private void setBlockedCells() {
        int numBlocked = (int)(WIDTH*HEIGHT*BLOCKED_CELL_PER);
        int i = 0;
        while (i < numBlocked) {
            int x = rand.nextInt(120); // [0,119]
            int y = rand.nextInt(160); // [0,159]
            if (!this.grid[x][y].hasHighway()) {    // cannot block highways
                this.grid[x][y].changeType(0);
                ++i;
            }
        }
    } // ends the setBlockedCells() method  




    /**
     * This method will set the start and end cells in the grid.
     */
    public void setStartAndEnd() {
        int dist = 0;
        int x1 , y1, x2, y2;
        x1 = y1 = x2 = y2 = 0;
        while (dist < MIN_DIST_BETWEEN_START_AND_END) {
            // get coordinate for the start cell
            int dir = rand.nextInt(4) + 1; 
            if (dir == 1) { // top border
                x1 = rand.nextInt(CHOICE_REGION_AREA);      // [0,19]
                y1 = rand.nextInt(WIDTH);                   // [0,159]
                while (this.grid[x1][y1].getType() == 0) {  // if the choice is blocked, try another random location
                    x1 = rand.nextInt(CHOICE_REGION_AREA);  // [0,19]
                    y1 = rand.nextInt(WIDTH);               // [0,159]
                }
            } else if (dir == 2) { // right border
                x1 = rand.nextInt(HEIGHT);                                  // [0,119]
                y1 = (WIDTH - 1) - rand.nextInt(CHOICE_REGION_AREA);        // [140,159]
                while (this.grid[x1][y1].getType() == 0) {                  // if the choice is blocked, try another random location
                    x1 = rand.nextInt(120);                                 // [0,119]
                    y1 = (WIDTH - 1) - rand.nextInt(CHOICE_REGION_AREA);    // [140,159]
                }
            } else if (dir == 3) { // bottom border
                x1 = (HEIGHT - 1) - rand.nextInt(CHOICE_REGION_AREA);       // [100,119]
                y1 = rand.nextInt(WIDTH);                                   // [0,159]
                while (this.grid[x1][y1].getType() == 0) {                  // if the choice is blocked, try another random location
                    x1 = (HEIGHT - 1) - rand.nextInt(CHOICE_REGION_AREA);   // [100,119]
                    y1 = rand.nextInt(WIDTH);                               // [0,159]
                }
            } else { // left border
                x1 = rand.nextInt(HEIGHT);                  // [0,119]
                y1 = rand.nextInt(CHOICE_REGION_AREA);      // [0,19]
                while (this.grid[x1][y1].getType() == 0) {  // if the choice is blocked, try another random location
                    x1 = rand.nextInt(HEIGHT);              // [0,119]
                    y1 = rand.nextInt(CHOICE_REGION_AREA);  // [0,19]
                }
            }

            // get coordinate for the end cell
            dir = rand.nextInt(4) + 1; 
            if (dir == 1) { // top border
                x2 = rand.nextInt(CHOICE_REGION_AREA);      // [0,19]
                y2 = rand.nextInt(WIDTH);                   // [0,159]
                while (this.grid[x2][y2].getType() == 0) {  // if the choice is blocked, try another random location
                    x2 = rand.nextInt(CHOICE_REGION_AREA);  // [0,19]
                    y2 = rand.nextInt(WIDTH);               // [0,159]
                }
            } else if (dir == 2) { // right border
                x2 = rand.nextInt(HEIGHT);                                  // [0,119]
                y2 = (WIDTH - 1) - rand.nextInt(CHOICE_REGION_AREA);        // [140,159]
                while (this.grid[x2][y2].getType() == 0) {                  // if the choice is blocked, try another random location
                    x2 = rand.nextInt(120);                                 // [0,119]
                    y2 = (WIDTH - 1) - rand.nextInt(CHOICE_REGION_AREA);    // [140,159]
                }
            } else if (dir == 3) { // bottom border
                x2 = (HEIGHT - 1) - rand.nextInt(CHOICE_REGION_AREA);       // [100,119]
                y2 = rand.nextInt(WIDTH);                                   // [0,159]
                while (this.grid[x2][y2].getType() == 0) {                  // if the choice is blocked, try another random location
                    x2 = (HEIGHT - 1) - rand.nextInt(CHOICE_REGION_AREA);   // [100,119]
                    y2 = rand.nextInt(WIDTH);                               // [0,159]
                }
            } else { // left border
                x2 = rand.nextInt(HEIGHT);                  // [0,119]
                y2 = rand.nextInt(CHOICE_REGION_AREA);      // [0,19]
                while (this.grid[x2][y2].getType() == 0) {  // if the choice is blocked, try another random location
                    x2 = rand.nextInt(HEIGHT);              // [0,119]
                    y2 = rand.nextInt(CHOICE_REGION_AREA);  // [0,19]
                }
            }
            // get distance between start and end cells
            dist = distBetween(x1, y1, x2, y2);
        } // ends the while loop
        startCell[0][0] = x1;
        startCell[0][1] = y1;
        endCell[0][0] = x2;
        endCell[0][1] = y2;
    } // ends the setStartAndEnd() method




    /**
     * This method will measure the distance between two points on the grid.
     * @param x1 is the x-coordinate of the start point
     * @param y1 is the y-coordinate of the start point
     * @param x2 is the x-coordinate of the end point
     * @param y2 is the y-coordinate of the end point
     * @return an integer which represents the distance between the two points
     */
    private int distBetween(int x1, int y1, int x2, int y2) {
        return (int)(Math.sqrt(((x2-x1)*(x2-x1)) + ((y2-y1)*(y2-y1))));
    } // ends the distBetween() method



    /**
     * This method will return the grid.
     * @return the Cell grid
     */
    public Cell[][] getGrid() {
        return this.grid;
    } // ends the getGrid() method




    /**
     * This method will return the start Cell (goal) of the grid.
     * @return the start Cell int double array [1][2]
     */
    public int[][] getStartCell() {
        return startCell;
    } // ends the getStartCell() method




    /**
     * This method will return the end Cell (goal) of the grid.
     * @return the end Cell int double array [1][2]
     */
    public int[][] getEndCell() {
        return endCell;
    } // ends the getEndCell() method

    


    /**
     * This method will print out the grid to Standard Output.
     * 0 => blocked
     * 1 => unblocked
     * 2 => hard
     * a => unblocked w/ highway    (type = 3)
     * b => hard w/ highway         (type = 4)
     */
    public void printGrid() {
        System.out.println("Grid:");
        for (int i = 0 ; i < this.grid.length ; ++i) {
            for (int j = 0 ; j < this.grid[i].length ; ++j) {
                Cell cur = this.grid[i][j];
                if (cur.getType() == 3) {
                    System.out.print("a");
                } else if (cur.getType() == 4) {
                    System.out.print("b");
                } else {
                    System.out.print(cur.getType());
                }
            }
            System.out.println();
        }
    } // ends the printGrid() method




    /**
     * This method will print out all the hard centers of the grid.
     */
    public void printHardCenters() {
        System.out.println("Hard-Cell Centers:");
        for (int i = 0 ; i < NUMBER_HARD_CELL_CENTERS ; ++i) {
            System.out.println("(" + hardCellCenters[i][0] + " , " + hardCellCenters[i][1] + ")");
        }
    } // ends that printHardCenters() method




    /**
     * This method will save the contents of the grid to a txt file in the main directory.
     * @param file is the file pointer to the txt file to save the grid contents to
     */
    public void saveGrid(File file) {
        try {
            PrintWriter writer = new PrintWriter(file,"UTF-8");
            // write in the start and end cells
            writer.println(this.startCell[0][0] + "," + this.startCell[0][1]);
            writer.println(this.endCell[0][0] + "," + this.endCell[0][1]);

            // write in the hard centers
            for (int i = 0 ; i < NUMBER_HARD_CELL_CENTERS ; ++i) {
                writer.println(this.hardCellCenters[i][0] + "," + this.hardCellCenters[i][1]);
            }

            // write in the grid
            for (int i = 0 ; i < HEIGHT ; ++i) {
                for (int j = 0 ; j < WIDTH ; ++j) {
                    Cell cur = this.grid[i][j];
                    if (cur.getType() == 3) {
                        writer.print("a");
                    } else if (cur.getType() == 4) {
                        writer.print("b");
                    } else {
                        writer.print(cur.getType());
                    }
                }
                writer.println();
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving the grid.");
        }
    } // ends the saveGrid() method
    


    
    /**
     * This method will import a new Grid from a txt file.
     * @param file is the file pointer to the txt file with all the Grid contents to import from
     */
    public void importGrid(File file) {
        try {
            FileReader fileToRead = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileToRead);
            String line = reader.readLine();
            int linesRead = 1;
            while (line != null) {
                if (linesRead <= NUMBER_HARD_CELL_CENTERS + 2) { // first 10 lines are the start and end cells + hard centers
                    String[] coords = line.split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);

                    if (linesRead == 1) {
                        this.startCell[0][0] = x;
                        this.startCell[0][1] = y;
                        ++linesRead;
                    } else if (linesRead == 2) {
                        this.endCell[0][0] = x;
                        this.endCell[0][1] = y;
                        ++linesRead;
                    } else {
                        this.hardCellCenters[linesRead - 3][0] = x;
                        this.hardCellCenters[linesRead - 3][1] = y;
                        ++linesRead;
                    }
                } else { // get the grid contents
                    for (int i = 0 ; i < HEIGHT ; ++i) {
                        for (int j = 0 ; j < WIDTH ; ++j) {
                            Cell cur = this.grid[i][j];
                            if (line.charAt(j) == '0') {
                                cur.changeType(0);
                            } else if (line.charAt(j) == '1') {
                                cur.changeType(1);
                            } else if (line.charAt(j) == '2') {
                                cur.changeType(2);
                            } else if (line.charAt(j) == 'a') {
                                cur.changeType(3);
                            } else {
                                cur.changeType(4);
                            }
                        }
                        line = reader.readLine();
                    }
                }
                line = reader.readLine();
            } // ends the while loop
            reader.close();
            fileToRead.close();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Error importing grid.");
        }
    } // ends the importGrid() method
    
} // ends the Grid class
