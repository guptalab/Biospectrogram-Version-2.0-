/*******************************************************************************
 *	Electro Ion encoding (E05.3) (Ning et al., 2003)
 *
 *	Mapping ...
 *	A ---> 0.1260
 *	C ---> 0.1340
 *	G ---> 0.0806
 *	T ---> 0.1335
 *
 *	This code is written by Ruchin Shah @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;

public class ElectroIonIntEncod
{
	private String t_str,in,out;
	private String[] str;
	private FileWriter writer;
	private FileReader reader;
    private static final double A=0.126,C=0.134,G=0.0806,T=0.1335;
	private int temp,space_flag;
	private char[] c;

    public ElectroIonIntEncod(File fasta)
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
        	out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E05.3.fasta";
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
        str[65] = " ".concat(Double.toString(ElectroIonIntEncod.A)).concat (" ");
		str[67] = " ".concat(Double.toString(ElectroIonIntEncod.C)).concat (" ");
        str[71] = " ".concat(Double.toString(ElectroIonIntEncod.G));
		str[84] = " ".concat(Double.toString(ElectroIonIntEncod.T));
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