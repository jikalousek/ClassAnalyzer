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
        // Scanner reader = new Scanner(System.in);
        // System.out.print("Please enter the filename: ");
        
        String fileName = "example_targetclass_max.csv";//reader.next();
        // System.out.println(fileName);
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
        // System.out.println(lines.size());
        // System.out.println(lines.get(0).size());
        // System.out.println(lines.get(0).get(5));
        // int lineNo = 1;
        // for(List<String> line: lines) {
        //     int columnNo = 1;
        //     for (String value: line) {
        //         if(value.equals("")){
        //             System.out.println(value);
        //             columnNo++;
        //             continue;
        //         }
        //         System.out.println(value.getClass().getName());
        //         System.out.println("Line " + lineNo + " Column " + columnNo + ": " + value);
        //         columnNo++;
        //     }
        //     lineNo++;
        // }

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
                // System.out.println("Line " + lineNo + " Column " + columnNo + ": " + lines.get(lineNo).get(columnNo));
            }
        }

        System.out.println(lastArgs);
        // System.out.println(stat);
        // for(Integer line: containLine){
        //     System.out.println(line);
        // }

        int[][] checkers = new int[containLine.size()][lines.size()-1];
        String[][] pass = new String[1][lines.size() - 1];
        int column = 0;
        int row = 0;
        for(lineNo = 1; lineNo < lines.size(); lineNo++){
            column = 0;
            for(columnNo = 0; columnNo < lines.get(lineNo).size(); columnNo++){
                if(containLine.contains(columnNo)){
                    // System.out.println("Line " + lineNo + " Column " + columnNo + ": " + lines.get(lineNo).get(columnNo));
                    if(lines.get(lineNo).get(columnNo).equals("+")){
                        System.out.println("Line " + lineNo + " Column " + columnNo + ": " + lines.get(lineNo).get(columnNo));
                        System.out.println(column);
                        checkers[column++][row] = 1;
                    }
                    else if(lines.get(lineNo).get(columnNo).equals("-")){
                        System.out.println("Lines " + lineNo + " Column " + columnNo + ": " + lines.get(lineNo).get(columnNo));
                        System.out.println(column);
                        checkers[column++][row] = 0;
                    }
                    else if(lines.get(lineNo).get(columnNo).equals("")){
                        System.out.println("Liness " + lineNo + " Column " + columnNo + ": " + lines.get(lineNo).get(columnNo));
                        System.out.println(column);
                        checkers[column++][row] = 1;
                    }
                }
                else if(columnNo == stat){
                    System.out.println("Line " + lineNo + " Column " + columnNo + ": " + lines.get(lineNo).get(columnNo));
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

        for(column = 0; column < checkers.length; column++){
            for(row = 0; row < checkers[column].length; row++){
                System.out.println("Line " + row + " Column " + column + ": " + checkers[column][row]);
            }
        }

        int totalPassed = 0;
        int totalFailed = 0;
        for(row = 0; row < pass[0].length; row++){
            System.out.println("Line " + row + " Column " + column + ": " + pass[0][row]);
            if(pass[0][row].equals("P")){
                totalPassed++;
            }
            else{
                totalFailed++;
            }
        }

        System.out.println("Total Passed: " + totalPassed);
        System.out.println("Total Failed: " + totalFailed);

        float[] suspiciousness = new float[containLine.size()];
        if(totalFailed > 0){
            for(column = 0; column < checkers.length; column++){
                int passed = 0;
                int failed = 0;
                for(row = 0; row < checkers[column].length; row++){
                    if(checkers[column][row] == 1 && pass[0][row].equals("P")){
                        passed++;
                    }
                    else if(checkers[column][row] == 1 && pass[0][row].equals("F")){
                        failed++;
                    }
                }
                suspiciousness[column] = (failed/totalFailed)/((passed/totalPassed)+(failed/totalFailed));
            }
        }
        else{
            for(column = 0; column < checkers.length; column++){
                suspiciousness[column] = 0;
            }
        }

        System.out.println(containLine.size());

        for(column = 0; column < suspiciousness.length; column++){
            System.out.println(suspiciousness[column]);
        }

        String suspicious = "";
        int susColumn = 0;
        for(column = 0; column < lines.get(0).size(); column++){
            if(column <= lastArgs){
                suspicious += ",";
                System.out.println(column);
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

        System.out.println(suspicious);

        try{
            FileWriter fstream = new FileWriter(fileName, true);
            BufferedWriter fbw = new BufferedWriter(fstream);
            fbw.newLine();
            fbw.write(suspicious);
            fbw.close();
        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}