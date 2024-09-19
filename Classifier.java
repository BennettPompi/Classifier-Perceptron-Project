/*
 * Classifier.java
 * Copyright (c) 2024 Marcus A. Maloof.  All Rights Reserved.  See LICENSE.
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public abstract class Classifier {

  protected Random random = new Random();
  protected DecimalFormat df = new DecimalFormat( ".000" );

  public Classifier() { }

  // Classifies the example and returns the predicted class label of -1.0
  // or +1.0.

  abstract public double classify( ArrayList<Double> example ) throws Exception;

  // Classifies the examples of the data set and returns the accuracy.

  abstract public double classify( DataSet ds ) throws Exception;

  // Performs a hold-out evaluation using the data set and p, which is
  // the proportion of examples for the training set.

  abstract public double holdOut( double p, DataSet ds ) throws Exception;

  // Returns a string representation of the classifier.

  abstract public String toString();

  // Constructs a classifier using the data set.

  abstract public void train( DataSet ds ) throws Exception;

} // Classifier class
