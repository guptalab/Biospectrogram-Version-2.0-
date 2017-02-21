/*******************************************************************************
 *	Indicator Encoding (E01.1.1/2/3/4 for A/C/G/T) (Vos, 1992)
 *
 *	Mapping ... (For A)
 *	A --->  1
 *  C --->  0
 *	G --->  0
 *  T --->  0
 *
 *	This code is written by Jigar Raisinghania @ daiict and Nilay Chheda @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;

public class DNAindicator
{
	private String t_str,in,out;
	private String[] str;
	private FileWriter writer;
    private FileReader reader;
	private static Integer A=0,C=0,G=0,T=0;
    private int temp,space_flag;
	private char[] c;
	private boolean valid = true;

    public DNAindicator(File fasta)
	{
    	str = new String[256];
        space_flag = 0;
        int temp2;
       	char temp1;

		// save file name with its absolute path in the string variable "in"
        in = fasta.getAbsolutePath();
		if((in.contains(".genbank")) == true)
        {
            JOptionPane.showMessageDialog (null,"Encoding of GenBank files are not supported yet !");
		}
        else if((in.contains(".fasta")) == true || (in.contains(".fa")) == true)
		{
        	// create output path with output file name
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

		String s = JOptionPane.showInputDialog (null,"Enter the character to be indicated (A/C/G/T)");
		if(s==null || s.equals(""))
		{
			JOptionPane.showMessageDialog (null,"No character entered!"); valid=false;
		}
		else
		{

			char[] sarray = s.toCharArray ();

        	temp1 = sarray[0];

			if (s.length() > 1)
			{
				JOptionPane.showMessageDialog (null,"Multiple characters entered !");
				valid = false;
			}
			else if(temp1=='A' ||temp1=='a')
        	{
				DNAindicator.A=1;
            	out = out + "_E01.1.1.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='C' ||temp1=='c')
			{
	        	DNAindicator.C=1;
            	out = out + "_E01.1.2.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='G' ||temp1=='g')
			{
	        	DNAindicator.G=1;
            	out = out + "_E01.1.3.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='T' ||temp1=='t')
			{
				DNAindicator.T=1;
            	out = out + "_E01.1.4.fasta";
				out = "../History/Encoded/"+out;
			}
			else
			{
				JOptionPane.showMessageDialog (null,"Invalid Character Entered !");
				valid = false;
			}
		}
	}

	// In this constructor we are giving users facility to indicate
	// which indicator functnio they want to use.
	// Here flag = 1/2/3/4 corresponds to A/C/G/T
	public DNAindicator(File fasta, int flag)
	{
    	str = new String[256];
        space_flag = 0;
        int temp2;
       	char temp1;

		// save file name with its absolute path in the string variable "in"
        in = fasta.getAbsolutePath();
		if((in.contains(".genbank")) == true)
        {
            JOptionPane.showMessageDialog (null,"Encoding of GenBank files are not supported yet !");
		}
        else if((in.contains(".fasta")) == true || (in.contains(".fa")) == true)
		{
        	// create output path with output file name
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

		if(flag == 1)
        {
			DNAindicator.A=1;
			DNAindicator.C=0;
			DNAindicator.G=0;
			DNAindicator.T=0;
            out = out + "_E01.1.1.fasta";
			out = "../History/Encoded/"+out;
		}
        else if(flag == 2)
		{
        	DNAindicator.C=1;
        	DNAindicator.A=0;
        	DNAindicator.G=0;
        	DNAindicator.T=0;
            out = out + "_E01.1.2.fasta";
			out = "../History/Encoded/"+out;
		}
        else if(flag == 3)
		{
        	DNAindicator.G=1;
        	DNAindicator.C=0;
        	DNAindicator.A=0;
        	DNAindicator.T=0;
            out = out + "_E01.1.3.fasta";
			out = "../History/Encoded/"+out;
		}
        else if(flag == 4)
		{
			DNAindicator.T=1;
			DNAindicator.C=0;
			DNAindicator.G=0;
			DNAindicator.A=0;
            out = out + "_E01.1.4.fasta";
			out = "../History/Encoded/"+out;
		}
		else
		{
			valid = false;
		}
		if (valid)
		{
			this.encode();
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

		if (valid)
		{
			str[65] = " ".concat(Integer.toString(DNAindicator.A));
			str[67] = " ".concat(Integer.toString(DNAindicator.C));
        	str[71] = " ".concat(Integer.toString(DNAindicator.G));
			str[84] = " ".concat(Integer.toString(DNAindicator.T));

        	try
			{
	        	reader = new FileReader(in);
            	writer = new FileWriter(out);

				/*******************************************************************************
			 	*	This loop traverse to the dna sequence and maps each character to 0 or 1
			 	*	character by character
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
            	reader.close();
				writer.close();
			}
        	catch (IOException ioe)
			{
            	mainApp.errorHandler(ioe);
            	return false;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
}