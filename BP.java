/*
 * BP.java
 * Copyright (c) 2024 Marcus A. Maloof.  All Rights Reserved.  See LICENSE.
 */
import java.util.ArrayList;
public class BP extends Classifier {
    // data members
    int J;
    int I;
    //(J x I) matrix 
    ArrayList<ArrayList<Double>> v;
    //(K x J) matrix
    ArrayList<ArrayList<Double>> w;
    //(J x 1) vector
    ArrayList<Double> h;
    //(K x 1) vector
    ArrayList<Double> o;
    //(J x 1) vector
    ArrayList<Double> deltaHArr;
    //(K x 1) vector
    ArrayList<Double> deltaOArr;
    double[] y; 
    // other methods as you see fit
    final static int K = 2;
    final static double LR = .9;
    final static int MAX_EPOCHS = 50000;
    final static double MIN_ERROR = 0.1; 

    public void setJ(int J) {
        this.J = J;
    } // BP::setJ

    /**
     * main needs to process the command-line arguments -J, -p, -t, and -T.
     * The application logic is specified in the assignment's prompt.
     * See Perceptron.java for one of the use cases.
     */
    public static void main(String args[]) {
        try {
            DataSet training = new DataSet();
            ArgParser ap = new ArgParser(args);
            training.load(ap.trainingFile);
            DataSet test = new DataSet();
            boolean holdOutTest = false;
            BP bp = new BP();
            if (ap.proportion != -1) {
                holdOutTest = true;  
            }
            if (!ap.testingFile.equals("")) {
                test.load(ap.testingFile);
            }
            else {
                test = training;
            }
            if (ap.hiddenUnits < 1){
                throw new Exception("Number of hidden units must be greater than 0");
            }
            bp.setJ(ap.hiddenUnits);
            if (holdOutTest){
                double accuracy = bp.holdOut(ap.proportion, training);
                System.out.println(bp);
                System.out.println("Accuracy: " + accuracy);
            }
            else {
                bp.train(training);
                System.out.println(bp);
                System.out.println("Accuracy: " + bp.classify(test));
            }
        } // try
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } // catch
    } // BP::main
    private static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    } // BP::sigmoid
    private void initializeForDataSet(DataSet ds) {
        this.I = ds.get(0).size()-1;
        this.h = new ArrayList<Double>();
        this.o = new ArrayList<Double>();
        this.v = new ArrayList<ArrayList<Double>>();
        for (int j = 0; j < this.J; j++) {
            this.v.add(new ArrayList<Double>());
            this.h.add(0.0);
            for (int i = 0; i < this.I; i++) {
                this.v.get(j).add(this.random.nextGaussian());
            }
        }
        this.w = new ArrayList<ArrayList<Double>>();
        for (int k = 0; k < K; k++) {
            this.w.add(new ArrayList<Double>());
            this.o.add(0.0);
            for (int j = 0; j < this.J; j++) {
                this.w.get(k).add(this.random.nextGaussian());
            }
        }
        this.deltaHArr = new ArrayList<Double>();
        this.deltaOArr = new ArrayList<Double>();
        for (int j = 0; j < this.J; j++) {
            this.deltaHArr.add(0.0);
        }
        for (int k = 0; k < K; k++) {
            this.deltaOArr.add(0.0);
        }
    } // BP::initializeForDataSet
    @Override
    public double classify(ArrayList<Double> example) throws Exception {
        for (int j = 0; j < this.J; j++) {
            double sum = 0.0;
            for (int i = 0; i < this.I; i++) {
                sum += this.v.get(j).get(i) * example.get(i);
            }
            this.h.set(j, sigmoid(sum));
        }
        for (int k = 0; k < K; k++) {
            double sum = 0.0;
            for (int j = 0; j < this.J; j++) {
                sum += this.w.get(k).get(j) * this.h.get(j);
            }
            this.o.set(k, sigmoid(sum));
        }
        return (this.o.get(0) > this.o.get(1)) ? 1.0 : -1.0;
    }

    @Override
    public double classify(DataSet ds) throws Exception {
        double accuracy = 0.0;
        int m = ds.size(); 
        for (ArrayList<Double> example : ds) {
            this.classify(example);
            this.y = getEncodedY(example.get(this.I));
            boolean correct = true;
            //System.out.println("[" + this.y[0] + ", " + this.y[1] + "]");
            //System.out.println(this.o);
            for (int k = 0; k < K; k++) {
                correct = correct && (error(this.o.get(k), this.y[k]) < MIN_ERROR);
            }
            if (correct) {
                accuracy += 1.0;
            }
        }
        return accuracy / m;
    }

    @Override
    public double holdOut(double p, DataSet ds) throws Exception {
        DataSet[] split = ds.splitDataSet(ds, p);
        DataSet training = split[0];
        DataSet test = split[1];
        this.train(training);
        return this.classify(test);
    }

    @Override
    public String toString() {
        return "V: " + v.toString() + "\n" + "W: " +w.toString(); 
    }
    
    private static double error(double yHat, double y) {
        return 0.5 * Math.pow(yHat - y, 2);
    }
    private static double deltaOutput(double yHat, double y) {
        return yHat * (1 - yHat) * (y - yHat);
    }
    private static double deltaHidden(int j, ArrayList<Double> h, ArrayList<ArrayList<Double>> w, ArrayList<Double> deltaO) {
        double sum = 0.0;
        for (int k = 0; k < K; k++) {
            sum += w.get(k).get(j) * deltaO.get(k);
        }
        return h.get(j) * (1 - h.get(j)) * sum;
    }
    private void computeDeltas(){
        for (int k = 0; k < K; k++) {
            this.deltaOArr.set(k, deltaOutput(this.o.get(k), this.y[k]));
        }
        for (int j = 0; j < this.J; j++) {
            this.deltaHArr.set(j, deltaHidden(j, this.h, this.w, this.deltaOArr));
        }
    }
    private void adjustOutputWeights(){
        for (int k = 0; k < K; k++) {
            for (int j = 0; j < this.J; j++) {
                double delta = this.deltaOArr.get(k) * this.h.get(j);
                this.w.get(k).set(j, this.w.get(k).get(j) + LR * delta);
            }
        }
    }
    private void adjustHiddenWeights(ArrayList<Double> example){
        for (int j = 0; j < this.J; j++) {
            for (int i = 0; i < this.I - 1; i++) {
                double delta = this.deltaHArr.get(j) * example.get(i);
                this.v.get(j).set(i, this.v.get(j).get(i) + LR * delta);
            }
        }
    }
    private static double[] getEncodedY(double y){
        return (y == 1.0) ? new double[] {1.0, 0.0} : new double[] {0.0, 1.0};
    }
    private void clearDeltaVectors(){
        this.deltaHArr.forEach((d) -> d = 0.0);
        this.deltaOArr.forEach((d) -> d = 0.0);
    }
    @Override
    public void train(DataSet ds) throws Exception {
        int M = ds.size();
        this.initializeForDataSet(ds);
        int epoch = 0;
        double error = Double.POSITIVE_INFINITY;
        while (epoch < MAX_EPOCHS && error > MIN_ERROR) {
            /*if (epoch % 1000 == 0) {
                System.out.println("Epoch: " + epoch);
            }*/
            error = 0.0;
            for (int m = 0; m < M; m++) {
                this.clearDeltaVectors();
                ArrayList<Double> example = ds.get(m);
                double y = example.get(I);
                this.y = getEncodedY(y);
                this.classify(example);
                computeDeltas();
                //compute error and adjust w
                for (int k = 0; k < K; k++) {
                    double yHat = this.o.get(k);
                    error += error(yHat, this.y[k]);
                }
                this.adjustOutputWeights();
                //adjust v
                this.adjustHiddenWeights(example);
            }
            epoch++;
        }
        if (error > MIN_ERROR) {
            throw new FailedToConvergeException();
        }
    }
} // BP class
