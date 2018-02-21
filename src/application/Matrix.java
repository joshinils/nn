package application;

import java.time.chrono.MinguoChronology;
import java.util.function.Function;

import org.omg.CORBA.PUBLIC_MEMBER;

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
		{	throw(new IndexOutOfBoundsException("Matrix::Matrix zeilen war zu klein: "+zeilen));	}
		if (spalten<1)
		{	throw(new IndexOutOfBoundsException("Matrix::Matrix spalten war zu klein: "+spalten));	}

		data=new double[zeilen][spalten];
		
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++)
				data[i][j]=init;		
	}
	
	public static Matrix makeVec(double[] a)
	{
		Matrix erg=new Matrix(a.length, 1);
		for (int i = 0; i < erg.data.length; i++)
			erg.data[i][0]=a[i];
		return erg;
	}
	
	public static Matrix makeMat(double[][] a)
	{
		Matrix erg=new Matrix(a.length, a[0].length);
		for (int i = 0; i < erg.data.length; i++)
			for (int j = 0; j < a[0].length; j++)
				erg.data[i][j]=a[i][j];
		return erg;
	}
	
	public static Matrix add(Matrix a, Matrix b) throws Exception
	{
		if(a.data.length!=b.data.length)
			throw(new Exception("Matrix::add Dimensionen der Zeilen haut nicht hin! "+a.data.length+"*x != "+b.data.length+"*x"));
		if(a.data[0].length!=b.data[0].length)
			throw(new Exception("Matrix::add Dimensionen der Spalten haut nicht hin! x*"+a.data[0].length+" != x*"+b.data[0].length));
		
		Matrix erg = new Matrix(a.data.length, a.data[0].length);
		
		for (int i = 0; i < a.data.length; i++)
			for (int j = 0; j < a.data[0].length; j++)
				erg.data[i][j]=a.data[i][j]+b.data[i][j];
		return erg;
	}
	

	public static Matrix subtract(Matrix a, Matrix b) throws Exception
	{
		if(a.data.length!=b.data.length)
			throw(new Exception("Matrix::subtract Dimensionen der Zeilen haut nicht hin! "+a.data.length+"*x != "+b.data.length+"*x"));
		if(a.data[0].length!=b.data[0].length)
			throw(new Exception("Matrix::subtract Dimensionen der Spalten haut nicht hin! x*"+a.data[0].length+" != x*"+b.data[0].length));
		
		Matrix erg = new Matrix(a.data.length, a.data[0].length);
		
		for (int i = 0; i < a.data.length; i++)
			for (int j = 0; j < a.data[0].length; j++)
				erg.data[i][j]=a.data[i][j] - b.data[i][j];
		return erg;
	}
	
	public static Matrix mult(Matrix a, Matrix b) throws Exception
	{
		if(a.data[0].length!=b.data.length)
			throw(new Exception("Matrix::mult Dimensionen der Spalten haut nicht hin! x*"+a.data[0].length+" != "+b.data.length+"*x"));
		
		Matrix erg = new Matrix(a.data.length, b.data[0].length, 0.0);
		
		for (int i = 0; i < erg.data.length; i++)
			for (int j = 0; j < erg.data[0].length; j++)
				for (int k = 0; k < a.data[0].length; k++)
					erg.data[i][j]+=a.data[i][k]*b.data[k][j];
		return erg;
	}
	
	public static Matrix hadamard(Matrix a, Matrix b) throws Exception
	{
		if(a.data.length!=b.data.length)
			throw(new Exception("Matrix::hadamard Dimensionen der Zeilen haut nicht hin! "+a.data.length+"*x != "+b.data.length+"*x"));
		if(a.data[0].length!=b.data[0].length)
			throw(new Exception("Matrix::hadamard Dimensionen der Spalten haut nicht hin! x*"+a.data[0].length+" != x*"+b.data[0].length));
		Matrix erg=new Matrix(a.data.length, a.data[0].length);
		for (int i = 0; i < erg.data.length; i++)
			for (int j = 0; j < erg.data[0].length; j++)
				erg.data[i][j]=a.data[i][j]*b.data[i][j];
		return erg;
	}
	
	public static Matrix eye(int zeilen, int spalten)
	{
		Matrix erg=new Matrix(zeilen, spalten);
		for (int i = 0; i < erg.data.length&&i<erg.data[0].length; i++)
			erg.data[i][i]=1;
		return erg;
	}
		
	public static Matrix clone(Matrix a)
	{
		Matrix erg=new Matrix(a.data.length, a.data[0].length);
		for (int i = 0; i < a.data.length; i++)
			erg.data[i]=a.data[i].clone();
		return erg;
	}
	
	public static Matrix random(int zeilen, int spalten, double min, double max)
	{
		double interval=max-min;
		Matrix erg=new Matrix(zeilen, spalten);
		for (int i = 0; i < erg.data.length; i++)
			for (int j = 0; j < erg.data[0].length; j++)
				erg.data[i][j]=Math.random()*interval+min;
		return erg;
	}
	
	public Matrix apply(Function<Double, Double> foo)
	{
		Matrix erg=new Matrix(rows(), collumns());
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[0].length; j++)
				erg.data[i][j]=foo.apply(data[i][j]);
		return erg;
	}
	
	public double max()
	{
		double max=Double.NEGATIVE_INFINITY;
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[0].length; j++)
				if(max<data[i][j])
					max=data[i][j];
		return max;
	}
	
	public static Matrix scale(Matrix a, double s)
	{
		Matrix erg= new Matrix(a.data.length, a.data[0].length);
		for (int i = 0; i < a.data.length; i++)
			for (int j = 0; j < a.data[0].length; j++)
				erg.data[i][j]=a.data[i][j]*s;
		return erg;
	}
	
	public static Matrix transpose(Matrix a) 
	{
		Matrix erg=new Matrix(a.data[0].length, a.data.length);
		for (int i = 0; i < a.data.length; i++)
			for (int j = 0; j < a.data[0].length; j++)
				erg.data[j][i]=a.data[i][j];
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
	
	public int rows()
	{
		return data.length;
	}
	
	public int collumns()
	{
		return data[0].length;
	}
	
	@Override
	public String toString()
	{
		int len=0;
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[0].length; j++)
			{	
				int curr_len=String.format("% f",data[i][j]).length();
				if(len<curr_len)
					len=curr_len;
			}
		String erg="[";
		for (int i = 0; i < data.length; i++)
		{
			for (int j = 0; j < data[i].length; j++)
				erg+=String.format("% "+len+"f", data[i][j])+" ";
			erg+="\n ";
		}
		erg=erg.substring(0, erg.length()-3);
		return erg+"]";
	}
}


















