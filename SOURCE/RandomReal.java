/*******************************************************************************
 *	Random Real Value Encoding (E05.4)
 *
 *	Real values are generated randomly
 *	Random real values are generated from the range
 *	0.01 to 9.99 excluding the values with terminating zeros.
 *
 *	This code is written by Nilay Chheda @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.text.DecimalFormat;

public class RandomReal
{
	private String t_str,out,in;
	private String[] str;
	private FileWriter writer;
	private FileReader reader;
	private double A,C,G,T;
    private int temp,space_flag;
	private char[] c;

	public RandomReal(File fasta)
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
        	out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E05.4.fasta";
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
	 *	Encode method uses the encoding parameter set in the constructor,
	 *	encodes dna sequence according to those parameters and write into
	 *	fasta file at appropriate location.
	 *******************************************************************************/
     protected boolean encode()
     {
		// generate four random real values
        double val;
		double[]  vals = new double[4];
        Random rnd = new Random();

		for(int i=0;i<4;i++)
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

		String s = "Random encoding has produced following mapping\n";
        s = s + "A > "+vals[0]+"\nC > "+vals[1]+"\nG > "+vals[2]+"\nT > "+vals[3];

		JOptionPane.showMessageDialog (null,s);

		A = vals[0];
        C = vals[1];
		G = vals[2];
        T = vals[3];

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
			 *	randomly generate real values character by character
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