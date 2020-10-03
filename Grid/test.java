package Grid;

public class test {
    public static void main(String[] args) {
        Grid test = new Grid();
        Cell [][] grid = test.getGrid();
        grid[0][0].changeType(2);
        test.printGrid();
    }
    
}
