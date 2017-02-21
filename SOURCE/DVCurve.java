/*******************************************************************************
 *	DVCurve Encoding (E01.4) (Zhang, 2009)
 *
 *	This code is written by Jigar Raisinghania @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.Point;

public class DVCurve
{
	private String t_str,in,out;
    private String[] str;
	private FileWriter writer;
    private FileReader reader;
	private int temp,space_flag;
    private char[] s;
	private String A="",C="",G="",T="";
	private Point a,c,g,t;

	public DVCurve(File fasta)
    {
		init();
        str = new String[256];
		space_flag = 0;
        in = fasta.getAbsolutePath();
		if((in.contains(".genbank")) == true)
        {
			JOptionPane.showMessageDialog (null,"Encoding of GenBank files are not supported yet !");
		}
        else if((in.contains(".fasta")) == true)
		{
        	out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E01.4.fasta";
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

	protected void init()
    {
		A = A.concat("1 1 1 1 ");
		C = C.concat("1 -1 1 1 ");
		G = G.concat("1 -1 1 -1 ");
        T = T.concat("1 1 1 -1 ");
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
        str[65] = "".concat(this.A);
		str[67] = "".concat(this.C);
        str[71] = "".concat(this.G);
		str[84] = "".concat(this.T);
        try
		{
        	reader = new FileReader(in);
            writer = new FileWriter(out);

			/*******************************************************************************
			 *	This loop traverse to the dna sequence and uses each character to compute
			 *	its	DV Curve values
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
}