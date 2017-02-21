/*******************************************************************************
 *	Graphical Encoding 2 (E03.2) (Yau et al., 2003)
 *
 *	Mapping ...
 *	A ---> (sin30, -sin60)
 *	C ---> (sin60,  sin30)
 *	G ---> (sin60, -sin30)
 *	T ---> (sin30,  sin60)
 *
 *	This code is written by Nilay Chheda @ daiict and Ruchin Shah @ daiict
 *	This code was last modified on 8th of May, 2013
 *******************************************************************************/

import java.util.*;
import java.io.*;
import java.awt.Point;
import javax.swing.*;

public class YauAtElGraphical
{
	private String t_str,in,out;
	private String [] str;
	private FileWriter writer;
	private FileReader reader;
	private String A,C,G,T;
	private int temp,space_flag;
    private char[] s;
	private Point a,c,g,t;
	private Double m = 0.5,n = 0.866;

	public YauAtElGraphical(File fasta)
    {
		A = Double.toString(m).concat(" ").concat(Double.toString(n*(-1))).concat(" ");
		C = Double.toString(n).concat(" ").concat(" ").concat(Double.toString(m)).concat(" ");
		G = Double.toString(m).concat(" ").concat(" ").concat(Double.toString(n)).concat(" ");
        T = Double.toString(n).concat(" ").concat(Double.toString(m*(-1))).concat(" ");

		str = new String[256];
        space_flag = 0;
		in = fasta.getAbsolutePath();
        if((in.contains(".genbank")) == true)
		{
        	JOptionPane.showMessageDialog (null,"Encoding of GenBank files are not supported yet !");
		}
        else if((in.contains(".fasta")) == true)
		{
			out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E03.2.fasta";
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
			 *	predefined graphical value character by character
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