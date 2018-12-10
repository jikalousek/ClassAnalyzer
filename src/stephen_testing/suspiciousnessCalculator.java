package stephen_testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class suspiciousnessCalculator{
    public static void main(String[] args){
        Scanner reader = new Scanner(System.in);
        System.out.print("Please enter the filename: ");
        
        String fileName = reader.next();
        System.out.println(fileName);
        File file = new File(fileName);
        //2-dimensional array of strings
        List<List<String>> lines = new ArrayList<>();
        Scanner inputStream;
        try{
            inputStream = new Scanner(file);
            while (inputStream.hasNext()){
                String line = inputStream.next();
                String[] values = line.split(",");
                // this adds the currently parsed line to the 2-dimensional string array
                lines.add(Arrays.asList(values));
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int lineNo;
        int columnNo;
        List<Integer> containLine = new ArrayList<>();
        int stat = 0;
        int lastArgs = 0;
        for(lineNo = 0; lineNo < lines.size(); lineNo++){
            for(columnNo = 0; columnNo < lines.get(lineNo).size(); columnNo++){
                if(lines.get(lineNo).get(columnNo).contains("LINE")){
                    containLine.add(columnNo);
                }
                else if(lines.get(lineNo).get(columnNo).contains("STATUS")){
                    stat = columnNo;
                }
                else if(lines.get(lineNo).get(columnNo).contains("ARG")){
                    lastArgs = columnNo;
                }
            }
        }

        int[][] checkers = new int[containLine.size()][lines.size()-1];
        String[][] pass = new String[1][lines.size() - 1];
        int column = 0;
        int row = 0;
        for(lineNo = 1; lineNo < lines.size(); lineNo++){
            column = 0;
            for(columnNo = 0; columnNo < lines.get(lineNo).size(); columnNo++){
                if(containLine.contains(columnNo)){
                    if(lines.get(lineNo).get(columnNo).equals("+")){
                        checkers[column++][row] = 1;
                    }
                    else if(lines.get(lineNo).get(columnNo).equals("-")){
                        checkers[column++][row] = 0;
                    }
                    else if(lines.get(lineNo).get(columnNo).equals("")){
                        checkers[column++][row] = -1;
                    }
                }
                else if(columnNo == stat){
                    if(lines.get(lineNo).get(columnNo).equals("P")){
                        pass[0][row] = "P";
                    }
                    else{
                        pass[0][row] = "F";
                    }
                }
            }
            row++;
        }
        float totalPassed = 0;
        float totalFailed = 0;
        for(row = 0; row < pass[0].length; row++){
            if(pass[0][row] != null){
                if(pass[0][row].equals("P")){
                    totalPassed++;
                }
                else{
                    totalFailed++;
                }
            }
        }

        System.out.println("Total Passed: " + totalPassed);
        System.out.println("Total Failed: " + totalFailed);
        float passed = 0;
        float failed = 0;
        float[] suspiciousness = new float[containLine.size()];
        if(totalFailed > 0 && totalPassed > 0){
            for(column = 0; column < checkers.length; column++){
                for(row = 0; row < checkers[column].length; row++){
                    if(checkers[column][row] == 1 && pass[0][row].equals("P")){
                        passed++;
                    }
                    else if(checkers[column][row] == 1 && pass[0][row].equals("F")){
                        failed++;
                    }
                }
                suspiciousness[column] = (failed/totalFailed)/((passed/totalPassed)+(failed/totalFailed));
                passed = 0;
                failed = 0;
            }
        }
        else{
            for(column = 0; column < checkers.length; column++){
                suspiciousness[column] = 0;
            }
        }

        String suspicious = "";
        int susColumn = 0;
        for(column = 0; column < lines.get(0).size(); column++){
            if(column <= lastArgs){
                suspicious += ",";
            }
            else if(susColumn < suspiciousness.length){
                String floatToString = Float.toString(suspiciousness[susColumn]);
                String textToInsert = floatToString+",";
                suspicious += textToInsert;
                susColumn++;
            }
            else{
                suspicious += "";
            }
        }

        try{
            FileWriter fstream = new FileWriter(fileName, true);
            BufferedWriter fbw = new BufferedWriter(fstream);
            fbw.newLine();
            fbw.write(suspicious);
            fbw.close();
        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        reader.close();
    }
}