/*******************************************************************************
 *	Random Complex Encoding (E02.4)
 *
 *	Complex numbes are generated randomly
 *	Random real values for complex numbers are generated from the
 *	range 0.01 to 9.99 excluding the values with terminating zeros.
 *	Here i is square root of -1
 *
 *	This code is written by Nilay Chheda @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class RandomComplex
{
	private String filename,t_str,in,out;
	private String[] str;
	private FileWriter writer;
	private FileReader reader;
	private String A,C,G,T;
    private int temp,space_flag;
	private char[] c;

	public RandomComplex(File fasta)
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
        	out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E02.4.fasta";
            out = "../History/Encoded/"+out;
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
    protected String createComplex(double x,double y)
	{
		String number;
        if (x>0)
		{
        	if(y>0)
            {
				number = "".concat(Double.toString(x)).concat("+").concat(Double.toString(y)).concat("i");
                //number = "  ".concat(Double.toString(x)).concat("+").concat(Double.toString(y)).concat("i");
			}
            else
			{
            	y = -y;
                number = "".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
				//number = "  ".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
			}
		}
        else
		{
        	if(y>0)
            {
				number = "".concat(Double.toString(x)).concat("+").concat(Double.toString(y)).concat("i");
                //number = "  ".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
			}
            else
			{
            	y=-y;
                number = "".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
				//number = " ".concat(Double.toString(x)).concat("-").concat(Double.toString(y)).concat("i");
			}
		}
		return number;
	}

	/*******************************************************************************
	 *	Encode method uses the encoding parameter set in the constructor,encodes
	 *	dna sequence according to those parameters and write into fasta file at
	 *	appropriate location.
	 *******************************************************************************/
    protected boolean encode()
	{
		double val;
		double[]  vals = new double[8];
        Random rnd = new Random();

		for(int i=0;i<8;i++)
        {
			int a;
            int b;
			a = rnd.nextInt (10);
            b = rnd.nextInt (100);
			if (b==0||b==10||b==20||b==30||b==40||b==50||b==60||b==70||b==80||b==90)
            {
				b = b+1;
			}
            vals[i] = (double)a + (double) b/100;
			DecimalFormat df = new DecimalFormat("0.00");
            vals[i] =  Double.valueOf (df.format (vals[i]));
		}

		A = createComplex (vals[0],vals[1]);
        C = createComplex (vals[2],vals[3]);
		G = createComplex (vals[4],vals[5]);
        T = createComplex (vals[6],vals[7]);

		String s = "Random encoding has produced following mapping\n";
        s = s + "A > "+this.A+"\nC > "+this.C+"\nG > "+this.G+"\nT > "+this.T;

		JOptionPane.showMessageDialog (null,s);

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
						if(temp>=97 && temp<=122)
						temp=temp-32;
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