
//import java.io.File;
//import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import Grid.Grid;
import Heuristic.Chebyshev;
import Heuristic.EuclideanDistance;
import Heuristic.EuclideanDistanceByFour;
import Heuristic.ManhattanDistance;
import Heuristic.ManhattanDistanceByFour;
import Grid.Cell;
import SearchAlgos.UniformCostSearch;
import SearchAlgos.WeightedAStart;
import SearchAlgos.aStar;

public class test {
    public static void main(String[] args) {

     
        // buliding new grid (start + end + centers + highways + blocked + unblocked)
        Grid test = new Grid();
        test.generateEntireGrid();
        //test.printHardCenters();
        //test.printGrid();
        
        
        //EuclideanDistance heu = new EuclideanDistance(test);
        //EuclideanDistanceByFour heu = new EuclideanDistanceByFour(test);
        //ManhattanDistance heu = new ManhattanDistance(test);
        ManhattanDistanceByFour heu = new ManhattanDistanceByFour(test);
        //Chebyshev heu = new Chebyshev(test);


        aStar a = new aStar(test,heu);
        a.run();
        List<Cell> path = a.getPath();
        Set<Cell> explored = a.getExploredCells();
        for(int i = 0; i < test.getGrid().length; i++) {
        	for(int j = 0; j < test.getGrid()[0].length; j++) {
        		if(path.contains(test.getGrid()[i][j])) {
                    System.out.print("*");
                } else if (explored.contains(test.getGrid()[i][j])) {
                    System.out.print("V");
                } else {
                    //System.out.print("1");
                    if (test.getGrid()[i][j].getType() == 3) {
                        System.out.print("a");
                    } else if (test.getGrid()[i][j].getType() == 4) {
                        System.out.print("b");
                    } else {
                        System.out.print(test.getGrid()[i][j].getType());
                    }
                }
        	}
        	System.out.println();
        }


        System.out.println();
        System.out.println();
        System.out.println();

        
        WeightedAStart a2 = new WeightedAStart(test, heu, 2.5f);
        //a = new aStar(test,heu2);
        a2.run();
        path = a2.getPath();
        explored = a2.getExploredCells();
        for(int i = 0; i < test.getGrid().length; i++) {
        	for(int j = 0; j < test.getGrid()[0].length; j++) {
        		if(path.contains(test.getGrid()[i][j])) {
                    System.out.print("*");
                } else if (explored.contains(test.getGrid()[i][j])) {
                    System.out.print("V");
                } else {
                    //System.out.print("1");
                    if (test.getGrid()[i][j].getType() == 3) {
                        System.out.print("a");
                    } else if (test.getGrid()[i][j].getType() == 4) {
                        System.out.print("b");
                    } else {
                        System.out.print(test.getGrid()[i][j].getType());
                    }
                }
        	}
        	System.out.println();
        }



        System.out.println();
        System.out.println();
        System.out.println();

        

        UniformCostSearch a3 = new UniformCostSearch(test);
        //a = new aStar(test,heu2);
        a3.run();
        path = a3.getPath();
        explored = a3.getExploredCells();
        for(int i = 0; i < test.getGrid().length; i++) {
        	for(int j = 0; j < test.getGrid()[0].length; j++) {
        		if(path.contains(test.getGrid()[i][j])) {
                    System.out.print("*");
                } else if (explored.contains(test.getGrid()[i][j])) {
                    System.out.print("V");
                } else {
                    //System.out.print("1");
                    if (test.getGrid()[i][j].getType() == 3) {
                        System.out.print("a");
                    } else if (test.getGrid()[i][j].getType() == 4) {
                        System.out.print("b");
                    } else {
                        System.out.print(test.getGrid()[i][j].getType());
                    }
                }
        	}
        	System.out.println();
        }
        

        //Grid temp = new Grid(10, 10);
        //temp.endCell[0][0] = 2;
        //temp.endCell[0][1] = 1;

        // saving a grid (that has been fully built with everything) into a file in the current directory
        //File file = new File("./testGridSave.txt");
        //test.saveGrid(file);

        // importing a new Grid from a file that has all the Grid contents
        //Grid test2 = new Grid();    // new Grid (will be empty)
        //test2.importGrid(file);     // file that holds other Grid contents to copy over
        //test2.printHardCenters(); 
        //test2.printGrid();
    }    
}
