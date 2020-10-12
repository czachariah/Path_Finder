import java.io.File;

public class test {
    public static void main(String[] args) {
        Grid test = new Grid();
        test.generateEntireGrid();
        //test.printHardCenters();
        //test.printGrid();

        File file = new File("./testGridSave.txt");
        test.saveGrid(file);

        Grid test2 = new Grid();
        test2.importGrid(file);
        test2.printHardCenters();
        test2.printGrid();
    }    
}
