package application;

public class Matrix
{
	private double[][] data;
	
	public Matrix(int zeilen, int spalten ) throws IndexOutOfBoundsException
	{
		this(zeilen, spalten, 0.0);
	}

	public Matrix(int zeilen, int spalten, double init)
	{
		if (zeilen<1)
		{	throw(new IndexOutOfBoundsException("Matrix::zeilen war zu klein: "+zeilen));	}
		if (spalten<1)
		{	throw(new IndexOutOfBoundsException("Matrix::spalten war zu klein: "+spalten));	}

		data=new double[zeilen][spalten];
		
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++)
				data[i][j]=init;		
	}
	
	public static Matrix add(Matrix a, Matrix b) throws Exception
	{
		if(a.data.length!=b.data.length)
			throw(new Exception("Matrix::Dimensionen der Zeilen haut nicht hin! "+a.data.length+"*x != "+b.data.length+"*x"));
		if(a.data[0].length!=b.data[0].length)
			throw(new Exception("Matrix::Dimensionen der Spalten haut nicht hin! x*"+a.data[0].length+" != x*"+b.data[0].length));
		
		Matrix erg = new Matrix(a.data.length, a.data[0].length);
		
		for (int i = 0; i < a.data.length; i++)
		{
			for (int j = 0; j < a.data[0].length; j++)
			{
				erg.data[i][j]=a.data[i][j]+b.data[i][j];
			}
		}	
		return erg;
	}
	
	public static Matrix mult(Matrix a, Matrix b) throws Exception
	{
		if(a.data.length!=b.data[0].length)
			throw(new Exception("Matrix::Dimensionen der Zeilen haut nicht hin! "+a.data.length+"*x != x*"+b.data[0].length));
		if(a.data[0].length!=b.data.length)
			throw(new Exception("Matrix::Dimensionen der Spalten haut nicht hin! x*"+a.data[0].length+" != "+b.data.length+"*x"));
		
		Matrix erg = new Matrix(a.data.length, b.data[0].length, 0.0);
		
		for (int i = 0; i < erg.data.length; i++)
			for (int j = 0; j < erg.data[0].length; j++)
				for (int k = 0; k < a.data[0].length; k++)
					erg.data[i][j]
							+=
							a.data[i][k]
									*
									b.data[k][j];
		return erg;
	}
	
	public double get(int i, int j)
	{
		if(0<=i&&i<data.length
				&& 0<=j&&j<data[0].length)
		return data[i][j];
		throw(new IndexOutOfBoundsException("Matrix:: indexzugriff bei ("+i+" "+j+") nicht erlaubt!"));
	}
	
	public double set(int i, int j, double val)
	{
		if(0<=i&&i<data.length
				&& 0<=j&&j<data[0].length)
		return data[i][j]=val;
		throw(new IndexOutOfBoundsException("Matrix:: indexzugriff bei ("+i+" "+j+") nicht erlaubt!"));
	}
	
	@Override
	public String toString()
	{
		String erg="[";//="Matrix:\n[";
		for (int i = 0; i < data.length; i++)
		{
			for (int j = 0; j < data[i].length; j++)
				erg+=data[i][j]+" ";
			erg+="\n ";
		}
		erg=erg.substring(0, erg.length()-3);
		return erg+"]";
	}
}


















