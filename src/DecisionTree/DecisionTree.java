package DecisionTree;

import java.util.*;

public class DecisionTree {

    public String[][] dataset;
    public String[][] traingset;
    public String[][] testset;
    private int datarows,datacolumns;
    List<String> DataClass = new ArrayList<>();
    Map<String, Integer> classCatagoryMap = new HashMap<>();

    public DecisionTree(String[][] dataset,int datarows, int datacolumns, Map<String,Integer>classCatagoryMap,List<String> DataClass){
        this.dataset = dataset;
        this.datarows = datarows;
        this.datacolumns = datacolumns;
        this.classCatagoryMap = classCatagoryMap;
        this.DataClass= DataClass;
    }

    public void trainData(){
        this.createtableset();
        this.printDecisionTree(this.expandBranches(this.traingset),0);
       // this.expandBranches(this.traingset);

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
            if(!traindatachecker[i]){
                for(int j=0;j<this.datacolumns;j++){
                    testset[testsetIndex][j] = this.dataset[i][j];
                }
                testsetIndex++;
            }
        }
        //System.out.println(testsetIndex);

    }

    public double calculateTotalEntropy(String[][] workingTrainingset){

        Map<String, Integer> copyClassCatagoryMap= new HashMap<>(this.classCatagoryMap);

        for(int i=1; i<workingTrainingset.length;i++){
            copyClassCatagoryMap.put(workingTrainingset[i][this.datacolumns-1], copyClassCatagoryMap.get(workingTrainingset[i][this.datacolumns-1])+1);
        }// copying the class catagory map to count the number of times each class has been repeated


        double entropy = 0; int Check=0;

        for (Map.Entry<String, Integer> entry : copyClassCatagoryMap.entrySet()) {

            double catagoryFrequency = entry.getValue();

            if(catagoryFrequency > 0) Check++;

            entropy+= -((catagoryFrequency/(workingTrainingset.length))*(Math.log(catagoryFrequency/(workingTrainingset.length))/Math.log(2)));

        }

        if(Check==1) return 0;

        return entropy;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public double calculateBranchEntropy(Map<String, Integer> catMap, double branchFreq, int rows){

        double entropy = 0;
        for (Map.Entry<String, Integer> entry : catMap.entrySet()) {

            double catFrequency = entry.getValue();

            entropy+= -((catFrequency/branchFreq)*(Math.log(catFrequency/branchFreq)/Math.log(2)));

        }
        return entropy*(branchFreq/(rows));

    }


    public Node expandBranches(String[][] workingtrainingset){

        Node n = new Node();

        int rows=workingtrainingset.length;

        double totalEntropy= calculateTotalEntropy(workingtrainingset);

        if(totalEntropy==0){

            n.leaf=true;
            n.decision = workingtrainingset[0][this.datacolumns-1];
            return n;

            //there is only one catagory so returing only that
        }

        Set<String> temp = new HashSet<>();
        Set<String> branches = new HashSet<>();

        int currentColumnNumber=0, newTColumn=1;
        double maxGain = 0.0;



        while (currentColumnNumber< this.datacolumns-1){

            double featureEntropy=0;

            for(int i=0;i<rows;i++){
                branches.add(workingtrainingset[i][currentColumnNumber]);
            }//it is carrying all classes or types of a column

            for(String branch: branches){

                double branchFrequency=0;

                Map<String, Integer> copyClassCategoryMap = new HashMap<>(this.classCatagoryMap);

                for (int j=0;j<rows;j++){

                    if(branch.equals(workingtrainingset[j][currentColumnNumber])){
                        branchFrequency++;

                        copyClassCategoryMap.put(workingtrainingset[j][this.datacolumns-1], copyClassCategoryMap.get(workingtrainingset[j][this.datacolumns-1])+1);
                    }
                }

                if(Collections.frequency(copyClassCategoryMap.values(), 0) == copyClassCategoryMap.size()-1)//first condition returning how many categories are 0
                    //alternate condition should be copClassCategoryMap.size()==1 but as it is branch and others can be 0 so this board equaton
                    featureEntropy+=0;

                else
                    featureEntropy+= calculateBranchEntropy(copyClassCategoryMap,branchFrequency,rows);

            }

            if( (totalEntropy-featureEntropy) > maxGain){
                temp.clear();
                maxGain = totalEntropy-featureEntropy;
                newTColumn = currentColumnNumber;
                n.attribute = this.DataClass.get(currentColumnNumber);
                n.leaf=false;

                for(String s: branches)
                    temp.add(s);
            }
            currentColumnNumber++;
            branches.clear();
        }

        // System.out.println("\nSelected Feature: "+n.attribute+"\n");

        for(String s: temp){
            String[][] reducedTable= makeChildTable(workingtrainingset, s, newTColumn);
            n.nodes.put(s,expandBranches(reducedTable));
        }

        return n;
    }


    public String[][] makeChildTable(String[][] parentTable, String branchName, int column){

        int rTRow=0,temp=0,trow = parentTable.length;

        for(int i=0; i<trow;i++){

            if(parentTable[i][column].equals(branchName)) rTRow++;
        }
        String[][] newTable= new String[rTRow][this.datacolumns];

        //for(int i=0; i<this.datacolumns; i++) newTable[0][i] = parentTable[0][i];

        for(int i=0;i<trow;i++){
            if(parentTable[i][column].equals(branchName)){
                for (int j=0;j<this.datacolumns;j++)
                    newTable[temp][j]= parentTable[i][j];
                temp++;
            }
        }
        return newTable;
    }

    public void printDecisionTree(Node node,int depth){


        if(node.leaf){
            System.out.print(node.decision);
        }
        else {
            depth++;
            System.out.print(node.attribute);

            for(Map.Entry<String,Node> entry : node.nodes.entrySet()){
                System.out.println("");

                for (int i=0;i<depth;i++) System.out.print("\t");

                System.out.print("--"+entry.getKey()+"--> ");

                printDecisionTree(entry.getValue(),depth+2);
            }
        }
    }

}
