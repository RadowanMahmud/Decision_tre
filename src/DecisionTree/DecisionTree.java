package DecisionTree;

import java.util.Random;

public class DecisionTree {

    public String[][] dataset;
    public String[][] traingset;
    public String[][] testset;
    private int datarows,datacolumns;

    public DecisionTree(String[][] dataset,int datarows, int datacolumns){
        this.dataset = dataset;
        this.datarows = datarows;
        this.datacolumns = datacolumns;
    }

    public void trainData(){
        this.createtableset();
    }

    private void createtableset(){

        boolean[] traindatachecker= new boolean[this.datarows];//8124 without 0 of data set

        int numberodTestData=(this.datarows/5);//1624 number of test data
        int rows = (this.datarows-numberodTestData);//6500 number of train data

        traingset= new String[rows][this.datacolumns];//6500*23
        testset= new String[numberodTestData][this.datacolumns];//1624(23

        Random random= new Random();
        int traindataindex;

        for(int i=0;i<rows;i++){

            traindataindex=random.nextInt(this.datarows);// selecting random data from data set

            if(traindataindex==0 || traindatachecker[traindataindex]==true){
                i--;
                continue;
            }
            else {
                for(int j=0;j<this.datacolumns;j++){
                   // System.out.println(i+" "+j+"   "+traindataindex+" "+j);
                    traingset[i][j] = this.dataset[traindataindex][j];
                    traindatachecker[traindataindex]=true;
                }
            }

        }

        int testsetIndex=0;

        for(int i=0;i<traindatachecker.length;i++){
            if(traindatachecker[i]){
                for(int j=0;j<this.datacolumns;j++){
                    testset[testsetIndex][j] = this.dataset[i][j];
                }
                testsetIndex++;
            }
        }

    }

}
