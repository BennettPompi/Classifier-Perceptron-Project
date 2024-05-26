public class ArgParser {
    public double proportion = -1;
    public String trainingFile = "";
    public String testingFile = "";
    public int hiddenUnits = -1;
    ArgParser(String[] args){
        for (int i = 0; i < args.length; i++){
            if (args[i].equals("-p")){
                proportion = Double.parseDouble(args[i+1]);
            }
            else if (args[i].equals("-t")){
                trainingFile = args[i+1];
            }
            else if (args[i].equals("-T")){
                testingFile = args[i+1];
            }
            else if (args[i].equals("-J")){
                hiddenUnits = Integer.parseInt(args[i+1]);
            }
            else if (args[i].equals("-h")){
                System.out.println("Usage: java BP -t <training file> [-T <testing file>] [-p <proportion>] -J <hidden units>");
                System.out.println("(-J is required for BP Only)");
                System.exit(0);
            }
        }
    }
}
