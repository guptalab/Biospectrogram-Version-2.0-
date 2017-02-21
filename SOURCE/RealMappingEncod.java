/*******************************************************************************
 *	Real Value Encoding (E05.1)
 *	(Cristea, 2003; Rosen, 2006; Chakravarthy et al., 2004)
 *
 *	Mapping ...
 *	A --->  1.5
 *	C --->  0.5
 *	G ---> -0.5
 *	T ---> -1.5
 *
 *	Real Value Encoding (User choice) (E05.2)
 *	User have option to provide different real values for encoding.
 *
 *	This code is written by Nilay Chheda @ daiict & Ruchin Shah @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;

public class RealMappingEncod
{
	private String t_str,out,in;
	private String[] str;
	private FileWriter writer;
	private FileReader reader;
	private double A= 1.5,C= 0.5,G=-0.5,T=-1.5;
    private int temp,space_flag;
	private char[] c;

	public RealMappingEncod(File fasta)
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
	 *	Setter method for encoding 'A' with different real value
	 *******************************************************************************/
	protected void setA(double x)
    {
		if (x>0)
        {
			A = x;
		}
	}

	/*******************************************************************************
	 *	Setter method for encoding 'C' with different real value
	 *******************************************************************************/
	protected  void setC(double x)
    {
		C = x;
	}

    /*******************************************************************************
     *	Setter method for encoding 'G' with different real value
	 *******************************************************************************/
	protected  void setG(double x)
    {
          G = x;
	}

	/*******************************************************************************
	 *	Setter method for encoding 'T' with different real value
	 *******************************************************************************/
	protected  void setT(double x)
    {
		T = x;
	}

	/*******************************************************************************
	 *	Setter method for encoding A,C,G,T  with different real values in single
	 *	method call.
	 *******************************************************************************/
	protected  void setACGT(double a,double c,double g,double t)
    {
		this.setA(a);
        this.setC(c);
		this.setG(g);
        this.setT(t);
	}
	protected boolean encode2(double r1,double r2,double r3,double r4)
    {
		this.setACGT (r1,r2,r3,r4);

    	out = out + "_E05.2.fasta";
        out = "../History/Encoded/"+out;

		for(int i=0;i<256;i++)
        {
			str[i] = String.valueOf((char)i);
		}
        str[13] = "";
		str[10] = "\n";
        if ( this.A > 0 )
		{
        	str[65] = "  ".concat(Double.toString(this.A));
		}
        else
		{
        	str[65] = " ".concat(Double.toString(this.A));
		}
        if ( this.C > 0 )
		{
        	str[67] = "  ".concat(Double.toString(this.C));
		}
        else
		{
        	str[67] = " ".concat(Double.toString(this.C));
		}
        if ( this.G > 0 )
		{
        	str[71] = "  ".concat(Double.toString(this.G));
		}
        else
		{
        	str[71] = " ".concat(Double.toString(this.G));
		}
        if (this.T > 0 )
		{
        	str[84] = "  ".concat(Double.toString(this.T));
		}
        else
		{
        	str[84] = " ".concat(Double.toString(this.T));
		}
		try
        {
			reader = new FileReader(in);
            writer = new FileWriter(out);

			/*******************************************************************************
			 *	This loop traverse to the dna sequence and maps each character to
			 *	prescribed real value character by character
			 *******************************************************************************/

             while(true)
             {
				temp = reader.read();
                if(temp == -1)
					break;
				if(temp==65 || temp==67 || temp==71 || temp==84)
                {
					space_flag = 1;
				}
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

	protected boolean encode1()
    {
		out = out + "_E05.1.fasta";
        out = "../History/Encoded/"+out;

		for(int i=0;i<256;i++)
        {
			str[i] = String.valueOf((char)i);
		}
        str[13] = "";
		str[10] = "\n";
        if ( this.A > 0 )
		{
        	str[65] = "  ".concat(Double.toString(this.A));
		}
        else
		{
        	str[65] = " ".concat(Double.toString(this.A));
		}
        if ( this.C > 0 )
		{
        	str[67] = "  ".concat(Double.toString(this.C));
		}
        else
		{
        	str[67] = " ".concat(Double.toString(this.C));
		}
        if ( this.G > 0 )
		{
        	str[71] = "  ".concat(Double.toString(this.G));
		}
        else
		{
        	str[71] = " ".concat(Double.toString(this.G));
		}
        if (this.T > 0 )
		{
        	str[84] = "  ".concat(Double.toString(this.T));
		}
        else
		{
        	str[84] = " ".concat(Double.toString(this.T));
		}
		try
        {
			reader = new FileReader(in);
            writer = new FileWriter(out);

			/*******************************************************************************
			 *	This loop traverse to the dna sequence and maps each character to
			 *	prescribed real value character by character
			 *******************************************************************************/

             while(true)
             {
				temp = reader.read();
                if(temp == -1)
					break;
				if(temp==65 || temp==67 || temp==71 || temp==84)
                {
					space_flag = 1;
				}
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
	/*******************************************************************************
	 *	Encode method uses the encoding parameter set in the constructor,
	 *	encodes dna sequence according to those parameters and write into
	 *	fasta file at appropriate location.
	 *******************************************************************************/
	protected boolean encode()
    {
		int choice = JOptionPane.showConfirmDialog(null,"Do you want to enter real number of your choice?(y/n) : ","",JOptionPane.YES_NO_OPTION);

		if (choice == 0)
        {
			String a,b,c,d;
            Double r1,r2,r3,r4;
			a = JOptionPane.showInputDialog (null,"Enter real value for A ");
            b = JOptionPane.showInputDialog (null,"Enter real value for C");
			c = JOptionPane.showInputDialog (null,"Enter real value for G");
            d = JOptionPane.showInputDialog (null,"Enter real value for T");
			if (a == null || b == null || c == null || d == null )
            {
				JOptionPane.showMessageDialog (null,"Please enter proper sequence !");
			}
            else
			{
            	r1 = Double.parseDouble (a);
                r2 = Double.parseDouble (b);
				r3 = Double.parseDouble (c);
                r4 = Double.parseDouble (d);
				this.setACGT (r1,r2,r3,r4);
			}
            out = out + "_E05.2.fasta";
			out = "../History/Encoded/"+out;
		}
        else
		{
        	out = out + "_E05.1.fasta";
            out = "../History/Encoded/"+out;
		}

		for(int i=0;i<256;i++)
        {
			str[i] = String.valueOf((char)i);
		}
        str[13] = "";
		str[10] = "\n";
        if ( this.A > 0 )
		{
        	str[65] = "  ".concat(Double.toString(this.A));
		}
        else
		{
        	str[65] = " ".concat(Double.toString(this.A));
		}
        if ( this.C > 0 )
		{
        	str[67] = "  ".concat(Double.toString(this.C));
		}
        else
		{
        	str[67] = " ".concat(Double.toString(this.C));
		}
        if ( this.G > 0 )
		{
        	str[71] = "  ".concat(Double.toString(this.G));
		}
        else
		{
        	str[71] = " ".concat(Double.toString(this.G));
		}
        if (this.T > 0 )
		{
        	str[84] = "  ".concat(Double.toString(this.T));
		}
        else
		{
        	str[84] = " ".concat(Double.toString(this.T));
		}
		try
        {
			reader = new FileReader(in);
            writer = new FileWriter(out);

			/*******************************************************************************
			 *	This loop traverse to the dna sequence and maps each character to
			 *	prescribed real value character by character
			 *******************************************************************************/

             while(true)
             {
				temp = reader.read();
                if(temp == -1)
					break;
				if(temp==65 || temp==67 || temp==71 || temp==84)
                {
					space_flag = 1;
				}
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