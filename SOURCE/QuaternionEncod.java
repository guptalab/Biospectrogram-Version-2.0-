/*******************************************************************
 *	Quaternion Encoding 1/2 (E04.1/2)
 *	(Brodzik and Peters, 2005; Akhtar et al., 2007)
 *
 *	Mapping ... (Quaternion 1)
 *	A --->  i + j + k
 *  C --->  i - j - k
 *	G ---> -i - j + k
 *  T ---> -i + j - k
 *
 *	Mapping ... (Quaternion 2)
 *	A --->  1 + i + j + k
 *  C --->  1 + i - j - k
 *	G --->  1 - i - j + k
 *  T --->  1 - i + j - k
 *
 *	This code is written by Ruchin Shah @ daiict
 *	This code was last modified on 8th of May, 2013
 *
*******************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;

public class QuaternionEncod
{
	private String t_str,in,out;
	private String[] str;
	private FileWriter writer;
	private FileReader reader;

	// 3 bit and 4 bit vectors represented by string of 1s and -1s
    private static final String[] A={" 1  1  1  "," 1  1  1  1  "},C={" 1 -1 -1  "," 1  1 -1 -1  "},
    G={"-1 -1  1  "," 1 -1 -1  1  "},T={"-1  1 -1  "," 1 -1  1 -1  "};
	private int temp,space_flag;
    private char[] c;

	public QuaternionEncod(File fasta)
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
	 *	Encode method uses the encoding parameter set in the constructor,
	 *	encodes dna sequence according to those parameters and write into
	 *	fasta file at appropriate location.
	 *******************************************************************************/

	protected boolean encode3()
	{
		int flag=0;


		if (flag == 0 || flag == 1)
        {
			if (flag == 0)
            {
				out = out+"_E04.1.fasta";
               	out = "../History/Encoded/"+out;
			}
            else
			{
            	out = out+"_E04.2.fasta";
                out = "../History/Encoded/"+out;
			}
            for(int i=0;i<256;i++)
			{
            	str[i] = String.valueOf((char)i);
			}
            str[13] = "";
			str[10] = "\n";
            str[65] = " ".concat(QuaternionEncod.A[flag]);
			str[67] = " ".concat(QuaternionEncod.C[flag]);
            str[71] = " ".concat(QuaternionEncod.G[flag]);
			str[84] = " ".concat(QuaternionEncod.T[flag]);
            try
			{
				reader = new FileReader(in);
                writer = new FileWriter(out);

				/*******************************************************************************
				 *	This loop traverse to the dna sequence and maps each character to
				 *	prescribed quaternion value character by character
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
                        t_str = t_str.concat("Mapping: A>+i+j+k C>+i-j-k G>-i-j+k T>-i+j-k");
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
        else
		{
        	JOptionPane.showMessageDialog (null,"Invalid flag entered !");
        	return false;
		}
	}
	protected boolean encode4()
	{
		int flag=1;


		if (flag == 0 || flag == 1)
        {
			if (flag == 0)
            {
				out = out+"_E04.1.fasta";
               	out = "../History/Encoded/"+out;
			}
            else
			{
            	out = out+"_E04.2.fasta";
                out = "../History/Encoded/"+out;
			}
            for(int i=0;i<256;i++)
			{
            	str[i] = String.valueOf((char)i);
			}
            str[13] = "";
			str[10] = "\n";
            str[65] = " ".concat(QuaternionEncod.A[flag]);
			str[67] = " ".concat(QuaternionEncod.C[flag]);
            str[71] = " ".concat(QuaternionEncod.G[flag]);
			str[84] = " ".concat(QuaternionEncod.T[flag]);
            try
			{
				reader = new FileReader(in);
                writer = new FileWriter(out);

				/*******************************************************************************
				 *	This loop traverse to the dna sequence and maps each character to
				 *	prescribed quaternion value character by character
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
                        t_str = t_str.concat("Mapping: A>1+i+j+k C>1+i-j-k G>1-i-j+k T>1-i+j-k");
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
        else
		{
        	JOptionPane.showMessageDialog (null,"Invalid flag entered !");
        	return false;
		}
	}
	protected boolean encode()
    {
    	int flag;
    	try
    	{
    		flag = JOptionPane.showConfirmDialog(null,"Do you want to apply Quaternion 1?(No means Quaternion 2)","",JOptionPane.YES_NO_OPTION);
    	}
    	catch(NumberFormatException nfe)
    	{
    		JOptionPane.showMessageDialog(null,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
    		return false;
    	}

		if (flag == 0 || flag == 1)
        {
			if (flag == 0)
            {
				out = out+"_E04.1.fasta";
               	out = "../History/Encoded/"+out;
			}
            else
			{
            	out = out+"_E04.2.fasta";
                out = "../History/Encoded/"+out;
			}
            for(int i=0;i<256;i++)
			{
            	str[i] = String.valueOf((char)i);
			}
            str[13] = "";
			str[10] = "\n";
            str[65] = " ".concat(QuaternionEncod.A[flag]);
			str[67] = " ".concat(QuaternionEncod.C[flag]);
            str[71] = " ".concat(QuaternionEncod.G[flag]);
			str[84] = " ".concat(QuaternionEncod.T[flag]);
            try
			{
				reader = new FileReader(in);
                writer = new FileWriter(out);

				/*******************************************************************************
				 *	This loop traverse to the dna sequence and maps each character to
				 *	prescribed quaternion value character by character
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
        else
		{
        	JOptionPane.showMessageDialog (null,"Invalid flag entered !");
        	return false;
		}
	}
}