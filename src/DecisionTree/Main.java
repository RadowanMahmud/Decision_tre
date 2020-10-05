package DecisionTree;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ReadData data=new ReadData("src/Resources/1.csv");
        try {
            data.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
