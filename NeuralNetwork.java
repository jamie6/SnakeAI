/**
 *
 * @author Jamie
 */
public class NeuralNetwork
{
    String fileName;
    int inputNodes, hiddenNodes, outputNodes; 
    Matrix weightLayers[];
    
    NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes, int numberOfLayers)
    {
	this.inputNodes = inputNodes;
	this.hiddenNodes = hiddenNodes;
	this.outputNodes = outputNodes;
	
	if ( numberOfLayers < 3 ) numberOfLayers = 3;
	weightLayers = new Matrix[numberOfLayers];
	
	for ( int i = 0; i < numberOfLayers; i++ )
	{
	    if ( i == 0 )
		weightLayers[i] = new Matrix(hiddenNodes, inputNodes+1);
	    else if ( i + 1 == numberOfLayers )
		weightLayers[i] = new Matrix(outputNodes, hiddenNodes+1);
	    else
		weightLayers[i] = new Matrix(hiddenNodes, hiddenNodes+1);
	    weightLayers[i].randomFill();
	}
    }
    
    NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes, Matrix weightLayers[])
    {
	this.inputNodes = inputNodes;
	this.hiddenNodes = hiddenNodes;
	this.outputNodes = outputNodes;
	
	this.weightLayers = new Matrix[weightLayers.length];
	
	for ( int i = 0; i < weightLayers.length; i++ )
	{
	    this.weightLayers[i] = weightLayers[i].clone();
	}
    }
    
    NeuralNetwork(String fileName)
    {
	readFromFile(fileName);
    }
    
    public int getOutput(int[] input)
    {
	Matrix inputMatrix = new Matrix(input);
        inputMatrix.inverseElements(); // helps to normalize the data
	Matrix inputMatrixWithBias = inputMatrix.getColumnMatrixWithBias();
	
	Matrix inputs, outputs, outputsWithBias;
	inputs = weightLayers[0].matrixProduct(inputMatrixWithBias);
	outputs = inputs.getActivationMatrix();
	outputsWithBias = outputs.getColumnMatrixWithBias();
	
	for ( int i = 1; i < weightLayers.length; i++ )
	{
	    inputs = weightLayers[i].matrixProduct(outputsWithBias);
	    outputs = inputs.getActivationMatrix();
	    outputsWithBias = outputs.getColumnMatrixWithBias();
	}
	
	double max = 0;
	int maxIndex = 0;
	for ( int i = 0; i < outputs.row; i++ )
	{
	    if ( outputs.matrix[i][0] > max )
	    {
		max = outputs.matrix[i][0];
		maxIndex = i;
		if ( maxIndex > 2 ) break;
	    }
	}
	return maxIndex;
    }
    
    public NeuralNetwork crossover( NeuralNetwork partner )
    {
	NeuralNetwork child = cloneNN();
	for ( int i = 0; i < weightLayers.length; i++ )
	{
	    child.weightLayers[i].crossover(partner.weightLayers[i]);
	}
	return child;
    }
    
    public void mutation()
    {
	for ( int i = 0; i < weightLayers.length; i++ )
	    weightLayers[i].mutation();
    }
    
    //@Override
    public NeuralNetwork cloneNN()
    {
	return new NeuralNetwork(inputNodes, hiddenNodes, outputNodes, weightLayers);
    }
    
    public void readFromFile(String fileName)
    {
	this.fileName = fileName;
	try
	{
	    java.io.File inputFile = new java.io.File(fileName);// + ".txt");
	    java.util.Scanner in = new java.util.Scanner(inputFile);
	    
	    inputNodes = in.nextInt();
	    hiddenNodes = in.nextInt();
	    outputNodes = in.nextInt();
	    
            in.nextLine();
            
	    java.util.LinkedList<Matrix> tempLL = new java.util.LinkedList<>();
	    java.util.Scanner tempLine;
	    for ( ; in.hasNext() ; )
	    {
		tempLine = new java.util.Scanner(in.nextLine());
		Matrix tempMatrix;
		if ( tempLL.isEmpty() )
		{
		    tempMatrix = new Matrix(hiddenNodes, inputNodes+1);
		}
		else if ( in.hasNextLine() )
		{
		    tempMatrix = new Matrix(hiddenNodes, hiddenNodes+1);
		}
		else tempMatrix = new Matrix(outputNodes, hiddenNodes+1);
		for ( int i = 0; i < tempMatrix.row; i++ )
		    for ( int j = 0; j < tempMatrix.column; j++ )
			tempMatrix.matrix[i][j] = tempLine.nextDouble();
		tempLL.add(tempMatrix);
	    }
	    weightLayers = new Matrix[tempLL.size()];
	    for ( int i = 0; i < tempLL.size(); i++ )
		weightLayers[i] = tempLL.get(i);
	}
	catch( java.io.FileNotFoundException e )
	{
	    System.out.println(e.getMessage());
            e.printStackTrace();
	}
    }
    
    public void saveToFile(String fileName)
    {
	try
	{
	    java.io.PrintWriter out = new java.io.PrintWriter(fileName + ".txt");
	    out.append(inputNodes + " " + hiddenNodes + " " + outputNodes + "\n");
	    for ( int i = 0; i < weightLayers.length; i++ )
	    {
		for ( int j = 0; j < weightLayers[i].row; j++ )
		{
		    for ( int k = 0; k < weightLayers[i].column; k++ )
		    {
			out.append(weightLayers[i].matrix[j][k] + " ");
		    }
		}
		out.append("\n");
	    }
            out.close();
	}
	catch(Exception e)
	{
	    System.out.println(e.getMessage());
            e.printStackTrace();
	}
    }
    
    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }
}
