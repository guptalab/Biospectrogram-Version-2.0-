/*******************************************************************************
 *	ZCurve Encoding (E01.4) (Zhang and Zhang, 1994)
 *
 *	This code is written by Jigar Raisinghania @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;

public class ZCurve
{
	private String t_str,in,out;
	private String[] str;
	private FileWriter writer;
	private FileReader reader;
	private static Integer A=0,C=0,G=0,T=0;
	private int temp,space_flag;
    private char[] c;

	public ZCurve(File fasta)
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
        	out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E01.3.fasta";
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
            	JOptionPane.showMessageDialog (null,"File is not recognized as fasta file !");
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
		//Strings to contain indicator sequences
        String xa=new String();
		String xc=new String();
        String xt=new String();
		String xg=new String();

        //Character arrays of Indicator sequences
		char[] xa1;
        char[] xc1;
		char[] xg1;
        char[] xt1;

		int temp2;
    	char temp1;
        for(int i=0;i<256;i++)
		{
        	str[i] = String.valueOf((char)i);
		}
        str[13] = "";
		str[10] = "\n";

		int z=0;
        while(z!=4)
		{
        	ZCurve.A=0;
            ZCurve.C=0;
			ZCurve.G=0;
            ZCurve.T=0;
			if(z==0)
            	ZCurve.A=1;
			else if(z==1)
            	ZCurve.C=1;
			else if(z==2)
            	ZCurve.G=1;
			else if(z==3)
          		ZCurve.T=1;

			str[65] = " ".concat(Integer.toString(ZCurve.A));
            str[67] = " ".concat(Integer.toString(ZCurve.C));
			str[71] = " ".concat(Integer.toString(ZCurve.G));
            str[84] = " ".concat(Integer.toString(ZCurve.T));

			try
            {
				reader = new FileReader(in);
                writer = new FileWriter(out);

				/*******************************************************************************
				 *	This loop traverse to the dna sequence and uses each character to
				 *	compute its Z Curve values
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
                            if(z==0)
								xa=xa.concat(t_str);
							else if(z==1)
                            	xc=xc.concat(t_str);
							else if(z==2)
                            	xg=xg.concat(t_str);
							else if(z==3)
                            	xt=xt.concat(t_str);

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
					}
                    else
					{
                		if(z==0)
                        	xa=xa.concat(str[temp]);
						else if(z==1)
                        	xc=xc.concat(str[temp]);
						else if(z==2)
                        	xg=xg.concat(str[temp]);
						else if(z==3)
                        	xt=xt.concat(str[temp]);
					}
				}
                z++;
				reader.close();
                writer.close ();
			}
            catch (IOException ioe)
			{
                mainApp.errorHandler(ioe);
                return false;
			}
		}

        //Conversion from String to Character Array
		xa1=xa.toCharArray();
        xc1=xc.toCharArray();
		xg1=xg.toCharArray();
        xt1=xt.toCharArray();

		//Integer Arrays to contain integer values of characters in Indicator sequences.
        int[] xai= new int[xa1.length];
		int[] xci= new int[xc1.length];
        int[] xgi= new int[xg1.length];
		int[] xti= new int[xt1.length];
        int k=0;

		for(int l=0;l<xa1.length-4;l++)
        {
			if(xa1[l]!=32)
        	{
				xai[k]=(int)xa1[l];
        		if(xai[k]==48)
					xai[k]=0;
				else if(xai[k]==49)
        			xai[k]=1;
				k++;
			}
		}
        k=0;
		for(int l=0;l<xa1.length-4;l++)
        {
			if(xc1[l]!=32)
        	{
				xci[k]=(int)xc1[l];
        		if(xci[k]==48)
					xci[k]=0;
				else if(xci[k]==49)
                	xci[k]=1;
				k++;
			}
		}
        k=0;
		for(int l=0;l<xa1.length-4;l++)
        {
			if(xg1[l]!=32)
        	{
				xgi[k]=(int)xg1[l];
                if(xgi[k]==48)
					xgi[k]=0;
				else if(xgi[k]==49)
        			xgi[k]=1;
				k++;
			}
		}
        k=0;
		for(int l=0;l<xa1.length-4;l++)
        {
			if(xt1[l]!=32)
        	{
				xti[k]=(int)xt1[l];
        		if(xti[k]==48)
					xti[k]=0;
				else if(xti[k]==49)
        			xti[k]=1;
				k++;
			}
		}

        //The Strings required as output of Z Curve
		int[] xn= new int[xai.length];
        int[] yn= new int[xai.length];
		int[] zn= new int[xai.length];

        //Formula to convert the indicator to Z-Curve Sequences
		for(int i=0;i<k;i++)
        {
			xn[i]=(2*(xai[i]+xgi[i]))-1;
        	yn[i]=(2*(xai[i]+xci[i]))-1;
			zn[i]=(2*(xai[i]+xti[i]))-1;
		}

        String xn1;
		String yn1;
        String zn1;
		xn1="";
        yn1="";
		zn1="";

        //Converting Integer array to String to be written to file
		for(int l=0;l<k;l++)
        {
			xn1=xn1.concat(" ");
        	xn1=xn1.concat(Integer.toString(xn[l]));
		}
		for(int l=0;l<k;l++)
        {
			yn1=yn1.concat(" ");
        	yn1=yn1.concat(Integer.toString(yn[l]));
		}
		for(int l=0;l<k;l++)
        {
			zn1=zn1.concat(" ");
        	zn1=zn1.concat(Integer.toString(zn[l]));
		}
        try
		{
        	reader = new FileReader(in);
            writer = new FileWriter(out);

			while(true)
            {
				temp = reader.read();
				if(temp == -1)
                	break;
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
			}

 			//Writing Sequences to output file
            writer.write("xn= "+ xn1 +"\n");
			writer.write("yn= "+ yn1 +"\n");
            writer.write("zn= "+ zn1 +"\n");

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
}