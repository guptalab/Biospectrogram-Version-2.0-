/*******************************************************************************
 *	Graphical Encoding 1 (E03.1) (Liao, 2005)
 *
 *	User input is taken for integers m & n.
 *
 *	Mapping ...
 *	A ---> ( m,-n)
 *	C ---> ( m, n)
 *	G ---> ( n,-m)
 *	T ---> ( n, m)
 *
 *	This code is written by Nilay Chheda @ daiict and Ruchin Shah @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import java.awt.Point;
import javax.swing.*;

public class GraphicalEncod
{
	private String t_str,in,out;
    private String [] str;
	private FileWriter writer;
	private FileReader reader;
    private String A,C,G,T;
	private int temp,space_flag;
    private char[] s;
	private Point a,c,g,t;

    public GraphicalEncod(File fasta)
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
			out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E03.1.fasta";
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
    	String a,b;
        Integer m,n;

		// prompt user to enter values of m and n
        a = JOptionPane.showInputDialog (null,"Enter integer m");
		b = JOptionPane.showInputDialog (null,"Enter integer n");

		// proceed with the encoding only if valid values of m and n are given
        if (a == null || b == null )
		{
        	JOptionPane.showMessageDialog (null,"Please enter proper sequence !");
		}
        else
		{
        	m = Integer.parseInt(a);
            n = Integer.parseInt(b);

			A = Integer.toString(m).concat(" ").concat(Integer.toString(n*(-1))).concat(" ");
            C = Integer.toString(m).concat(" ").concat(" ").concat(Integer.toString(n)).concat(" ");
            G = Integer.toString(n).concat(" ").concat(Integer.toString(m*(-1))).concat(" ");
            T = Integer.toString(n).concat(" ").concat(" ").concat(Integer.toString(m)).concat(" ");
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
			 *	graphical pair (m,n) as given by user character by character
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

	protected boolean encode1(int m,int n)
	{
		A = Integer.toString(m).concat(" ").concat(Integer.toString(n*(-1))).concat(" ");
        C = Integer.toString(m).concat(" ").concat(" ").concat(Integer.toString(n)).concat(" ");
        G = Integer.toString(n).concat(" ").concat(Integer.toString(m*(-1))).concat(" ");
		T = Integer.toString(n).concat(" ").concat(" ").concat(Integer.toString(m)).concat(" ");

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
			 *	graphical pair (m,n) as given by user character by character
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
