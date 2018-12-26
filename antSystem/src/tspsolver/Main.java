package tspsolver;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        ACO aco;
        aco=new ACO();
        try {
            System.out.println("起点为1，终点为20");
            aco.init("/Users/houxiangrui/houProjects/antSystem/data/tspdata.txt", 500,1,20);
            aco.run(200);
            aco.ReportResult();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
