/*******************************************************************************
 *	Random Graphical Encoding (E03.3)
 *
 *	Integers are generated randomly
 *	Random real values for integers are generated from the
 *	range 0.01 to 9.99 excluding the values with terminating zeros.
 *
 *	This code is written by Nilay Chheda @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import java.awt.Point;
import javax.swing.*;
import java.text.DecimalFormat;

public class RandomGraphical
{
	private String t_str,in,out;
	private String [] str;
	private FileWriter writer;
	private FileReader reader;
	private String A,C,G,T;
	private int temp,space_flag;
    private char[] s;
	private Point a,c,g,t;

	public RandomGraphical(File fasta)
    {
        str = new String[256];
		space_flag = 0;

		// save file name with its absolute path in varaible "in"
        in = fasta.getAbsolutePath();
		if((in.contains(".genbank")) == true)
        {
			JOptionPane.showMessageDialog (null,"Encoding of GenBank files are not supported yet !");
		}
        else if((in.contains(".fasta")) == true)
		{
        	out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E03.3.fasta";
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
		// generate m,n integer
        Random rnd = new Random();
		int m = rnd.nextInt(90);
        int n = rnd.nextInt(90);
		m = m + 10;
        n = n + 10;

		A = Integer.toString(m).concat(" ").concat(Integer.toString(n*(-1))).concat(" ");
        C = Integer.toString(n).concat(" ").concat(" ").concat(Integer.toString(m)).concat(" ");
		G = Integer.toString(m).concat(" ").concat(" ").concat(Integer.toString(n)).concat(" ");
        T = Integer.toString(n).concat(" ").concat(Integer.toString(m*(-1))).concat(" ");

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
			 *	graphical pair (m,n) generated randomly
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