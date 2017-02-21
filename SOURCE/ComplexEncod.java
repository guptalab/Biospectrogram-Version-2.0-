/*******************************************************************************
 *	Complex Encoding 1 (E02.1) (Anastassiou, 2001)
 *
 *	A & T are complex conjugate
 *	C & G are complex conjugate
 *	Here i is square root of -1
 *
 *	Mapping ... (By default)
 *	A --->  1+i
 *  C ---> -1-i
 *	G ---> -1+i
 *  T --->  1-i
 *
 *	Complex Encoding (User Choice) (E02.2)
 *
 *	A & T are complex conjugate
 *	C & G are complex conjugate
 *	Here i is square root of -1
 *
 *	Mapping is generated based on user input.
 *
 *	This code is written by Nilay Chheda @ daiict and Ruchin Shah @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;

public class ComplexEncod
{
	private String t_str,in,out;
    private String[] str;
	private FileWriter writer;
    private FileReader reader;
	private String A=" 1.0+1.0i",C="-1.0-1.0i",G="-1.0+1.0i",T=" 1.0-1.0i";
    private int temp,space_flag;
	private char[] c;
    public ComplexEncod(File fasta)
	{
    	str = new String[256];
        space_flag = 0;
		in = fasta.getAbsolutePath();
        if((in.contains(".genbank")) == true)
		{
        	JOptionPane.showMessageDialog (null,"Encoding of GenBank files are not supported yet !");
		}
        else if((in.contains(".fasta")) == true)
		{
        	out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."));
		}
        else
		{
        	try
            {
				throw new FileNotFoundException();
			}
            catch(FileNotFoundException fnfe)
			{
				mainApp.errorHandler(fnfe);
			}
		}
	}

	/*******************************************************************************
	 *	Setter method for encoding 'A' with different complex value
	*******************************************************************************/
	protected void setA(double x,double y)
    {
		if (x>0)
        {
			if(y>0)
            {
				A = "  ".concat(Double.toString(x)).concat("+").concat(Double.toString(y)).concat("i");
			}
            else
			{
            	y = -y;
                A = "  ".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
			}
		}
        else
		{
        	if(y>0)
            {
				A = " ".concat(Double.toString(x)).concat("+").concat(Double.toString(y)).concat("i");
			}
            else
			{
            	y=-y;
                A = " ".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
			}
		}
	}

	/*******************************************************************************
	 *	Setter method for encoding 'C' with different complex value
	*******************************************************************************/
    protected void setC(double x,double y)
	{
    	if (x>0)
        {
			if(y>0)
            {
				C = "  ".concat(Double.toString(x)).concat("+").concat(Double.toString(y)).concat("i");
			}
            else
			{
            	y=-y;
                C = "  ".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
			}
        }
		else
		{
        	if(y>0)
            {
				C = " ".concat(Double.toString(x)).concat("+").concat(Double.toString(y)).concat("i");
			}
            else
			{
            	y=-y;
                C = " ".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
			}
		}
	}

	/*******************************************************************************
	 *	Setter method for encoding 'G' with different complex value
	*******************************************************************************/
    protected void setG(double x,double y)
	{
    	if (x>0)
        {
			if(y>0)
			{
            	G = "  ".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
			}
            else
			{
            	y=-y;
                G = "  ".concat(Double.toString(x)).concat("+").concat(Double.toString(y)).concat("i");
			}
		}
        else
		{
        	if(y>0)
            {
				G = " ".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
			}
            else
			{
            	y=-y;
                G = " ".concat(Double.toString(x)).concat("+").concat(Double.toString(y)).concat("i");
			}
		}
	}

	/*******************************************************************************
	 *	Setter method for encoding 'T' with different complex value
	*******************************************************************************/
    protected void setT(double x,double y)
	{
    	if (x>0)
        {
			if(y>0)
            {
				T = "  ".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
			}
            else
			{
            	y=-y;
                T = "  ".concat(Double.toString(x)).concat("+").concat(Double.toString(y)).concat("i");
			}
		}
		else
		{
			if(y>0)
            {
				T = " ".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
			}
            else
			{
            	y=-y;
                T = " ".concat(Double.toString(x)).concat("+").concat(Double.toString(y)).concat("i");
			}
		}
	}

	/*******************************************************************************
	 *	Setter method for encoding A,C,G,T  with different complex values in single
	 *	method call.
	 *******************************************************************************/
	protected void setAGCT(double a,double b,double c,double d)
	{
    	this.setA (a,b);
        this.setT  (a,b);   // complement of Z1
		this.setC (c,d);
        this.setG (c,d);    // complement of Z2
	}

	/*******************************************************************************
	 *	Encode method uses the encoding parameter set in the constructor,
	 *	encodes dna sequence according to those parameters and write into
	 *	fasta file at appropriate location.
	 *******************************************************************************/
	 protected boolean encode2()
	 {
	 		out = out+"_E02.1.fasta";
            out = "../History/Encoded/"+out;
            	for(int i=0;i<256;i++)
        {
			str[i] = String.valueOf((char)i);
		}
        str[13] = "";
		str[10] = "\n";
		str[65] = " ".concat(this.A);
        str[67] = " ".concat(this.C);
		str[71] = " ".concat(this.G);
        str[84] = " ".concat(this.T);
		try
        {
			reader = new FileReader(in);
            writer = new FileWriter(out);

			/*******************************************************************************
			 *	This loop traverse to the dna sequence and maps each character to
			 *	predefined comeplex character by character
			*******************************************************************************/
			while(true)
            {
				temp = reader.read();
                if(temp == -1)
					break;
				if(temp==65 || temp==67 || temp==71 || temp==84)
                	space_flag = 1;
				else
				{
                	if(space_flag == 1)
                    {
						t_str = " ".concat(str[temp]);
                        writer.write(t_str);
						space_flag = 0;
						continue;
					}
                    space_flag = 0;
				}
                if(temp == 59 || temp == 62)
				{
                	t_str = "";
                    while(temp != 10)
					{
                    	t_str = t_str.concat(String.valueOf((char)temp));
                        temp = reader.read();
					}
                    t_str = t_str.concat("(Mapping: A>"+this.A+" C>"+this.C+" G>"+this.G+" T>"+this.T+")");
                    t_str = t_str.concat("\n");
					writer.write(t_str);
				}
                else
					writer.write(str[temp]);
			}
            writer.close();
			reader.close();
		}
        catch (IOException ioe)
		{
			mainApp.errorHandler(ioe);
			return false;
		}
		return true;
	}

	 protected boolean encode1(Complex z1,Complex z2)
	 {
	 	double z1R=z1.re();
	 	double z1I=z1.im();
	 	double z2R=z2.re();
	 	double z2I=z2.im();
	 	this.setAGCT (z1R,z1I,z2R,z2I);
	 	out = out+"_E02.2.fasta";
			out = "../History/Encoded/"+out;
				for(int i=0;i<256;i++)
        {
			str[i] = String.valueOf((char)i);
		}
        str[13] = "";
		str[10] = "\n";
		str[65] = " ".concat(this.A);
        str[67] = " ".concat(this.C);
		str[71] = " ".concat(this.G);
        str[84] = " ".concat(this.T);
		try
        {
			reader = new FileReader(in);
            writer = new FileWriter(out);

			/*******************************************************************************
			 *	This loop traverse to the dna sequence and maps each character to
			 *	predefined comeplex character by character
			*******************************************************************************/
			while(true)
            {
				temp = reader.read();
                if(temp == -1)
					break;
				if(temp==65 || temp==67 || temp==71 || temp==84)
                	space_flag = 1;
				else
				{
                	if(space_flag == 1)
                    {
						t_str = " ".concat(str[temp]);
                        writer.write(t_str);
						space_flag = 0;
						continue;
					}
                    space_flag = 0;
				}
                if(temp == 59 || temp == 62)
				{
                	t_str = "";
                    while(temp != 10)
					{
                    	t_str = t_str.concat(String.valueOf((char)temp));
                        temp = reader.read();
					}
                    t_str = t_str.concat("(Mapping: A>"+this.A+" C>"+this.C+" G>"+this.G+" T>"+this.T+")");
                    t_str = t_str.concat("\n");
					writer.write(t_str);
				}
                else
					writer.write(str[temp]);
			}
            writer.close();
			reader.close();
		}
        catch (IOException ioe)
		{
			mainApp.errorHandler(ioe);
			return false;
		}
		return true;
	}

    protected boolean encode()
	{
		int choice = JOptionPane.showConfirmDialog(null,"Do you want to enter complex number (y/n) : ","",JOptionPane.YES_NO_OPTION);
        if (choice == 0)
		{
        	String a,b,c,d;
            Double z1R,z1I,z2R,z2I;
			a = JOptionPane.showInputDialog (null,"Enter value for real part of Z1");
            b = JOptionPane.showInputDialog (null,"Enter value for imaginary part of Z1");
			c = JOptionPane.showInputDialog (null,"Enter value for real part of Z2");
            d = JOptionPane.showInputDialog (null,"Enter value for imaginary part of Z2");
			if (a == null || b == null || c == null || d == null )
            {
				JOptionPane.showMessageDialog (null,"Please enter proper sequence !");
			}
            else
			{
            	z1R= Double.parseDouble (a);
                z1I = Double.parseDouble (b);
				z2R= Double.parseDouble (c);
                z2I = Double.parseDouble(d);
				this.setAGCT(z1R,z1I,z2R,z2I);
			}
            out = out+"_E02.2.fasta";
			out = "../History/Encoded/"+out;
		}
        else
		{
        	out = out+"_E02.1.fasta";
            out = "../History/Encoded/"+out;
		}

		for(int i=0;i<256;i++)
        {
			str[i] = String.valueOf((char)i);
		}
        str[13] = "";
		str[10] = "\n";
		str[65] = " ".concat(this.A);
        str[67] = " ".concat(this.C);
		str[71] = " ".concat(this.G);
        str[84] = " ".concat(this.T);
		try
        {
			reader = new FileReader(in);
            writer = new FileWriter(out);

			/*******************************************************************************
			 *	This loop traverse to the dna sequence and maps each character to
			 *	predefined comeplex character by character
			*******************************************************************************/
			while(true)
            {
				temp = reader.read();
                if(temp == -1)
					break;
				if(temp==65 || temp==67 || temp==71 || temp==84)
                	space_flag = 1;
				else
				{
                	if(space_flag == 1)
                    {
						t_str = " ".concat(str[temp]);
                        writer.write(t_str);
						space_flag = 0;
						continue;
					}
                    space_flag = 0;
				}
                if(temp == 59 || temp == 62)
				{
                	t_str = "";
                    while(temp != 10)
					{
                    	t_str = t_str.concat(String.valueOf((char)temp));
                        temp = reader.read();
					}
					t_str = t_str.concat("(Mapping: A>"+this.A+" C>"+this.C+" G>"+this.G+" T>"+this.T+")");
                    t_str = t_str.concat("\n");
					writer.write(t_str);
				}
                else
					writer.write(str[temp]);
			}
            writer.close();
			reader.close();
		}
        catch (IOException ioe)
		{
			mainApp.errorHandler(ioe);
			return false;
		}
		return true;
	}
}