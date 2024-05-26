/*
 * DataSet.java
 * Copyright (c) 2024 Marcus A. Maloof.  All Rights Reserved.  See LICENSE.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class DataSet extends ArrayList<ArrayList<Double>> {

    public DataSet() {}
    public DataSet[] splitDataSet(DataSet ds, double p){
        int n = ds.size();
        int m = (int) (n * p);
        DataSet[] result = new DataSet[2];
        result[0] = new DataSet();
        result[1] = new DataSet();
        for (int i = 0; i < m; i++){
            result[0].add(ds.get(i));
        }
        for (int i = m; i < n; i++){
            result[1].add(ds.get(i));
        }
        return result;
    }
    public void load(String filename) throws Exception {
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.startsWith("%") || line.isEmpty())
                    continue;
                String[] tokens = line.split(" ");
                ArrayList<Double> example = new ArrayList<Double>();
                for (String token : tokens) {
                    example.add(Double.parseDouble(token));
                } // for
                this.add(example);
            } // while
            scanner.close(); // DataSet::load
        } catch (Exception e) {
            throw new Exception("Failed to open file: " + filename);
        } // catch
    }

    public String toString(){
        String result = "";
        for (ArrayList<Double> example : this) {
            for (Double value : example) {
                result += value + " ";
            } // for
            result += "\n";
        } // for
        return result;
    }

} // DataSet class
