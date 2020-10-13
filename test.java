import java.io.File;
import java.util.LinkedList;
import java.util.List;

import Grid.Grid;
import Grid.Cell;
import SearchAlgos.aStar;

public class test {
    public static void main(String[] args) {
        // buliding new grid (start + end + centers + highways + blocked + unblocked)
        Grid test = new Grid();
        test.generateEntireGrid();
        test.printHardCenters();
        test.printGrid();
        
        aStar a = new aStar();
        List<Cell> path = new LinkedList<>();
        a.findPath(test, path);        
        for(int i = 0; i < path.size(); i++) {
        	System.out.println("path: " + path.get(i).getX() + " , " + path.get(i).getY());
        }


        // saving a grid (that has been fully built with everything) into a file in the current directory
        File file = new File("./testGridSave.txt");
        test.saveGrid(file);

        // importing a new Grid from a file that has all the Grid contents
        Grid test2 = new Grid();    // new Grid (will be empty)
        //test2.importGrid(file);     // file that holds other Grid contents to copy over
        //test2.printHardCenters(); 
        //test2.printGrid();
    }    
}
