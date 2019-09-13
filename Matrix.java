/**
 *
 * @author Jamie
 */
public class Matrix
{
    static final int SIGMOID = 0, TANH = 1;
    static java.util.Random rand = new java.util.Random();
    static int divideGaussianBy = 10; // this is used for random gaussian to get small numbers close to zero
    double matrix[][];
    int row, column;
    
    public Matrix(int row, int column)
    {
	this.row = row;
	this.column = column;
	matrix = new double[row][column];
    }
    
    public Matrix( int columnMatrix[] )
    {
	row = columnMatrix.length;
	column = 1;
	matrix = new double[row][column];
	for ( int i = 0; i < row; i++ ) matrix[i][0] = columnMatrix[i];
    }
    
    public void inverseElements()
    {
        for ( int i = 0; i < this.matrix.length; i++ )
            for ( int j = 0; j < this.matrix[i].length; j++ )
                this.matrix[i][j] = (this.matrix[i][j] != 0 ) ? 1.0/this.matrix[i][j] : this.matrix[i][j];
    }
    
    public Matrix matrixProduct( Matrix B )
    {
	Matrix C = new Matrix(row, B.column);
	if ( column == B.row )
	{
	    for ( int i = 0; i < row; i++ )
		for ( int j = 0; j < B.column; j++ )
		    for ( int k = 0; k < column; k++ )
			C.matrix[i][j] += matrix[i][k] * B.matrix[k][j];
	}
	return C;
    }
    
    // logistic function
    public double sigmoid( double x )
    {
	// sigmoid function: 1/(1+e^-x)
	return 1.0/(1.0+Math.pow(Math.E, -x));
    }
    
    // logistic function
    public double tanh( double x )
    {
        return Math.tanh(x);
    }
    
    public Matrix getActivationMatrix(int function)
    {
	Matrix a = new Matrix(row, column);
        
	for ( int i = 0; i < row; i++ )
        {
	    for ( int j = 0; j < column; j++ )
            {
                switch (function)
                {
                    case SIGMOID:
                        a.matrix[i][j] = sigmoid( matrix[i][j] );
                        break;
                    case TANH:
                        a.matrix[i][j] = tanh(matrix[i][j]);
                        break;
                }
            }
        }
	return a;
    }
    
    public Matrix getColumnMatrixWithBias()
    {
	Matrix b = new Matrix(row+1, 1);
	for ( int i = 0; i < row; i++ ) b.matrix[i][0] = matrix[i][0];
	b.matrix[row][0] = 1; // "dummy input"
	return b;
    }
    
    // fills the matrix with random values from -1 and 1
    public void randomFill()
    {
	for ( int i = 0; i < row; i++ )
        {
	    for ( int j = 0; j < column; j++ )
            {
		//matrix[i][j] = rand.nextDouble() > 0.5 ? rand.nextDouble() : -rand.nextDouble();
                matrix[i][j] += rand.nextGaussian()/divideGaussianBy;
                if ( matrix[i][j] > 1 ) matrix[i][j] = 1;
                else if ( matrix[i][j] < -1 ) matrix[i][j] = -1;
            }
        }
    }
    
    public void crossover(Matrix partner)
    {
	int randRows = rand.nextInt(matrix.length);
	int randColumns = rand.nextInt(matrix[0].length);
	
	for (int i =0; i<matrix.length; i++)
	{
	    for (int j = 0; j<matrix[0].length; j++)
	    {
		if ((i< randRows) || (i==randRows && j<=randColumns)) matrix[i][j] = partner.matrix[i][j];
	    }
	}
    }
    
    public void mutation()
    {
	double chanceOfMutation = 0.7;
	for ( int i = 0; i < matrix.length; i++ )
	{
	    for ( int j = 0; j < matrix[i].length; j++ )
	    {
		if ( rand.nextDouble() < chanceOfMutation )
		{
		    matrix[i][j] += rand.nextGaussian()/divideGaussianBy;
		    //matrix[i][j] += rand.nextDouble() > 0.5 ? rand.nextDouble() : -rand.nextDouble();
		    if ( matrix[i][j] > 1 ) matrix[i][j] = 1;
		    else if ( matrix[i][j] < -1 ) matrix[i][j] = -1;
		}
	    }
	}
	
    }
    
    public void print()
    {
	for ( int i = 0; i < this.matrix.length; i++ )
	{
	    for ( int j = 0; j < this.matrix[0].length; j++ )
		System.out.print(this.matrix[i][j] + " ");
	    System.out.println();
	}
    }
    
    public Matrix clone()
    {
	Matrix clone = new Matrix(row, column);
	for ( int i = 0; i < row; i++ )
	    for ( int j = 0; j < column; j++ )
		clone.matrix[i][j] = matrix[i][j];
	return clone;
    }
}
