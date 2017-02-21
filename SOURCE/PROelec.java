/*******************************************************************************
 *	Protein Electro Ion Encoding (E07.2) (Vaidyanathan, 2005)
 *	Mapping ...
 *	A ---> 0.0373
 *	C ---> 0.0829
 *	D ---> 0.1263
 *	E ---> 0.0058
 *	F ---> 0.0946
 *	G ---> 0.0050
 *	I ---> 0.0000
 *	H ---> 0.0242
 *	K ---> 0.0371
 *	L ---> 0.0000
 *	M ---> 0.0823
 *	N ---> 0.0036
 *	P ---> 0.0198
 *	Q ---> 0.0761
 *	R ---> 0.0959
 *	S ---> 0.0829
 *	T ---> 0.0941
 *	V ---> 0.0057
 *	W ---> 0.0548
 *	Y ---> 0.0516
 *
 *	This code is written by Nilay Chheda @ daiict and Jigar Raisinghania @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;

public class PROelec
{
	private String t_str,in,out;
	private String[] str;
	private FileWriter writer;
	private FileReader reader;
	private int temp,space_flag;
	private char[] c;

    //static values of electron-ion interaction
    public static final double A=0.0373,C=0.0829,D=0.1263,E=0.0058,F=0.0946,G=0.0050,H=0.0242,I=0.0000,K=0.0371,L=0.0000,M=0.0823,N=0.0036,P=0.0198,Q=0.0761,R=0.0959,S=0.0829,T=0.0941,V=0.0057,W=0.0548,Y=0.0516;


	public PROelec(File fasta)
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
        	out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E07.2.fasta";
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

    public boolean encode()
    {
        for(int i=0;i<256;i++)
        {
            str[i] = String.valueOf((char)i);
        }
        str[13] = "";
        str[10] = "\n";
        str[65] = " ".concat(Double.toString(PROelec.A));
        str[67] = " ".concat(Double.toString(PROelec.C));
        str[68] = " ".concat(Double.toString(PROelec.D));
        str[69] = " ".concat(Double.toString(PROelec.E));
        str[70] = " ".concat(Double.toString(PROelec.F));
        str[71] = " ".concat(Double.toString(PROelec.G));
        str[72] = " ".concat(Double.toString(PROelec.H));
        str[73] = " ".concat(Double.toString(PROelec.I));
        str[75] = " ".concat(Double.toString(PROelec.K));
        str[76] = " ".concat(Double.toString(PROelec.L));
        str[77] = " ".concat(Double.toString(PROelec.M));
        str[78] = " ".concat(Double.toString(PROelec.N));
        str[80] = " ".concat(Double.toString(PROelec.P));
        str[81] = " ".concat(Double.toString(PROelec.Q));
        str[82] = " ".concat(Double.toString(PROelec.R));
        str[83] = " ".concat(Double.toString(PROelec.S));
        str[84] = " ".concat(Double.toString(PROelec.T));
        str[86] = " ".concat(Double.toString(PROelec.V));
        str[87] = " ".concat(Double.toString(PROelec.W));
        str[89] = " ".concat(Double.toString(PROelec.Y));

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
                if(temp==65 || temp==67 || temp==68 || temp==69 ||temp==70 || temp==71 || temp==72 || temp==73 ||temp==75 || temp==76 || temp==77 || temp==78 ||temp==80 || temp==81 || temp==82 || temp==83 ||temp==84 || temp==86 || temp==87 || temp==89)
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
                    String proteinTemp = "(Mapping: A>"+this.A+" C>"+this.C+" D>"+this.D+" E>"+this.E;
                    proteinTemp = proteinTemp+" F>"+this.F+" G>"+this.G+" H>"+this.H+" I>"+this.I;
                    proteinTemp = proteinTemp+" K>"+this.K+" L>"+this.L+" M>"+this.M+" N>"+this.N;
                    proteinTemp = proteinTemp+" P>"+this.P+" Q>"+this.Q+" R>"+this.R+" S>"+this.S;
                    proteinTemp = proteinTemp+" T>"+this.T+" V>"+this.V+" W>"+this.W+" Y>"+this.Y+")";

                    t_str = t_str.concat(proteinTemp);
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