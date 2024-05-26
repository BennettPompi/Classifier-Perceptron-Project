/*
 * Perceptron.java
 * Copyright (c) 2024 Marcus A. Maloof.  All Rights Reserved.  See LICENSE.
 */

import java.util.ArrayList;

public class Perceptron extends Classifier {

    // data members
    final static double LR = .9;
    final static int MAX_EPOCHS = 50000;
    ArrayList<Double> weights;
    double output;

    // methods inherited from Classifier

    // other methods as you see fit

    /**
     * main needs to process the command-line arguments -p, -t, and -T.
     * The application logic is specified in the assignment's prompt.
     */

    public static void main(String args[]) {
        try {
            DataSet training = new DataSet();
            ArgParser ap = new ArgParser(args);
            training.load(ap.trainingFile);
            DataSet test = new DataSet();
            boolean holdOutTest = false;
            if (ap.proportion != -1) {
                holdOutTest = true;  
            } else if (!ap.testingFile.equals("")) {
                test.load(ap.testingFile);
            } else {
                test = training;
            }
            Perceptron perceptron = new Perceptron();
            if (holdOutTest) {
                double accuracy = perceptron.holdOut(ap.proportion, training);
                System.out.println("Weights: " + perceptron);
                System.out.println("Accuracy: " + accuracy);
                return;
            }
            else{
                perceptron.train(training);
                double accuracy = perceptron.classify(test);
                System.out.println("Weights: " + perceptron);
                System.out.println("Accuracy: " + accuracy);
            }
        } // try
        catch (FailedToConvergeException e) {
            System.out.println("Failed to converge!");
        } // catch
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } // catch
    } // Perceptron::main

    @Override
    public double classify(ArrayList<Double> example) throws Exception {
        double sum = 0.0;
        for (int i = 0; i < example.size() - 1; i++){
            sum += example.get(i) * weights.get(i);
        }
        return sum;
    }

    @Override
    public double classify(DataSet ds) throws Exception {
        int examples = ds.size();
        int correct = 0;
        for (int i = 0; i < examples; i++){
            ArrayList<Double> example = ds.get(i);
            double target = example.get(example.size()-1);
            double prediction = classify(example);
            //System.out.println(example);
            //System.out.println("Target: " + target);
            //System.out.println("Prediction: " + prediction);
            if (target * prediction > 0){
                correct++;
            }
        }
        return (double) correct / examples;
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
        return weights.toString();
    }
    private void updateWeights(double target, ArrayList<Double> example){
        for (int i = 0; i < example.size()-1; i++){
            double delta = LR * target * example.get(i);
            weights.set(i, weights.get(i) + delta);
        }
    }
    @Override
    public void train(DataSet ds) throws Exception {
        //initialize weights, inputs, and output
        int examples = ds.size();
        int inputSize = ds.get(0).size() -1;
        this.weights = new ArrayList<Double>();
        for (int i = 0; i < inputSize; i++){
            this.weights.add(0.0);
        }
        this.output = 0.0;

        //train the perceptron
        boolean converged = false;
        int epochs = 0;
        while (!converged){
            converged = true;
            if (epochs > MAX_EPOCHS){
                throw new FailedToConvergeException();
            }
            for (int i = 0; i < examples; i++){
                ArrayList<Double> example = ds.get(i);
                double target = example.get(example.size()-1);
                this.output = classify(example);
                if (target*this.output <= 0){
                    converged = false;
                    updateWeights(target, example);
                }
            }
            epochs++; 
        }
        //System.out.println("Converged after " + epochs + " epochs");
    }
} // Perceptron class
