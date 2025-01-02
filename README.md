# Usage

This is a project I did for school last spring. It continues an implementation of both a single and multi-layer perceptron classifiers. Testing and training data can be found in the ./dta folder. Testing datasets end in .te.dta, while training datasets end in .tr.dta.


Usage is as follows:

Perceptron:

```bash
java Perceptron -t <training file> -T <testing file>
```


Backpropogation:

```bash
java BP -t <training file> -T <testing file> -J <number of hidden layer units>
```


This program will output input layer weights (V), hidden layer weights (W), as well as the model’s accuracy against the testing data. 