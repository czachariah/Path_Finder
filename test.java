import java.io.File;

public class test {
    public static void main(String[] args) {
        Grid test = new Grid();
        test.generateEntireGrid();
        test.printHardCenters();
        test.printGrid();
        File file = new File("./test.txt");
        test.saveGrid(file);
    }    
}
