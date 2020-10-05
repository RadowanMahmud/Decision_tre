package DecisionTree;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class ReadData {

    private String FileName;
    private int datarows=0;
    private int datacolumns=0;
    List<String> DataClass = new ArrayList<>();
    Map<String, Integer> classCatagoryMap = new HashMap<>();
    private String[][] data;

    public ReadData(String File){

        this.FileName = File;
    }

    public void getData() throws IOException {

        this.setNumberofDatarows();

        this.setDataClass();

        this.parseData();

        //this.printDataArray();

        DecisionTree d= new DecisionTree(this.data,this.datarows,this.datacolumns,this.classCatagoryMap,this.DataClass);
        d.trainData();
    }

    private void setNumberofDatarows() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.FileName));

        while (true) {
            try {
                if (bufferedReader.readLine()!= null){
                    this.datarows++;
                }
                else break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.datarows--;

        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDataClass() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.FileName));

        try {
            String temp=bufferedReader.readLine();
            this.DataClass = Arrays.asList(temp.split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        for(String b: this.DataClass){
//            System.out.println(b);
//        }

        System.out.println(this.datarows);

        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseData() throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.FileName));
        String frow;

        int count=0;
        String[] arr=new String[30];

        while ((frow = bufferedReader.readLine())!= null){

            arr=frow.split(",");

            if(count!=0) classCatagoryMap.put(arr[arr.length-1],0); //this line is storing all the class catagory and setting their value as 0

                if(count==0){

                    this.datacolumns=arr.length;
                    data = new String[this.datarows+1][arr.length];
                }


            for (int i=0;i<arr.length;i++)
            {
                data[count][i]=arr[i];
            }

            count++;
        }

        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printDataArray(){
        for(int i=0;i<this.datarows;i++){
            for (int j=0;j<this.datacolumns;j++){
                System.out.println(data[i][j]);
            }
        }
    }



}
