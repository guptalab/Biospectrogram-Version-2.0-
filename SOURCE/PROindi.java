/*******************************************************************************
 *	Protein Indicator Encoding (E07.1.1/2/3/4/5/6/7/8/9/10/11/12/13/14/15/16/
 *	17/18/19/20 for A/C/D/E/F/G/H/I/K/L/M/N/P/Q/R/S/T/V/W/Y)
 *
 *	This code is written by Nilay Chheda @ daiict and Jigar Raisinghania @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/


import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;

public class PROindi
{
	private String t_str,in,out;
	private String[] str;
	private FileWriter writer;
    private FileReader reader;
	private static Integer A=0,C=0,D=0,E=0,F=0,G=0,H=0,I=0,K=0,L=0,M=0,N=0,P=0,Q=0,R=0,S=0,T=0,V=0,W=0,Y=0;
    private int temp,space_flag;
	private char[] c;
	private boolean valid = true;

	public PROindi(File fasta)
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

		String s = JOptionPane.showInputDialog (null,"Enter the character to be indicated\n( A/C/D/E/F/G/H/I/K/L/M/N/P/Q/R/S/T/V/W/Y )");
		if(s==null || s.equals(""))
		{
			JOptionPane.showMessageDialog (null,"No character entered!");
			valid=false;
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
				PROindi.A=1;
            	out = out + "_E07.1.1.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='C' ||temp1=='c')
			{
	        	PROindi.C=1;
            	out = out + "_E07.1.2.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='D' ||temp1=='d')
			{
	        	PROindi.D=1;
            	out = out + "_E07.1.3.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='E' ||temp1=='e')
			{
				PROindi.E=1;
            	out = out + "_E07.1.4.fasta";
				out = "../History/Encoded/"+out;
			}
			else if(temp1=='F' ||temp1=='f')
        	{
				PROindi.F=1;
            	out = out + "_E07.1.5.fasta";
				out = "../History/Encoded/"+out;
			}
			else if(temp1=='G' ||temp1=='g')
        	{
				PROindi.G=1;
            	out = out + "_E07.1.6.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='H' ||temp1=='h')
			{
	        	PROindi.H=1;
            	out = out + "_E07.1.7.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='I' ||temp1=='i')
			{
	        	PROindi.I=1;
            	out = out + "_E07.1.8.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='K' ||temp1=='k')
			{
				PROindi.E=1;
            	out = out + "_E07.1.9.fasta";
				out = "../History/Encoded/"+out;
			}
			else if(temp1=='L' ||temp1=='l')
        	{
				PROindi.L=1;
            	out = out + "_E07.1.10.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='M' ||temp1=='m')
			{
	        	PROindi.M=1;
            	out = out + "_E07.1.11.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='N' ||temp1=='n')
			{
	        	PROindi.N=1;
            	out = out + "_E07.1.12.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='P' ||temp1=='p')
			{
				PROindi.P=1;
            	out = out + "_E07.1.13.fasta";
				out = "../History/Encoded/"+out;
			}
			else if(temp1=='Q' ||temp1=='q')
        	{
				PROindi.Q=1;
            	out = out + "_E07.1.14.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='R' ||temp1=='r')
			{
	        	PROindi.R=1;
            	out = out + "_E07.1.15.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='S' ||temp1=='s')
			{
	        	PROindi.S=1;
            	out = out + "_E07.1.16.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='T' ||temp1=='t')
			{
				PROindi.T=1;
            	out = out + "_E07.1.17.fasta";
				out = "../History/Encoded/"+out;
			}
			else if(temp1=='V' ||temp1=='v')
        	{
				PROindi.V=1;
            	out = out + "_E07.1.18.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='W' ||temp1=='w')
			{
	        	PROindi.W=1;
            	out = out + "_E07.1.19.fasta";
				out = "../History/Encoded/"+out;
			}
        	else if(temp1=='Y' ||temp1=='y')
			{
	        	PROindi.Y=1;
            	out = out + "_E07.1.20.fasta";
				out = "../History/Encoded/"+out;
			}
			else
			{
				JOptionPane.showMessageDialog (null,"Invalid Character Entered !");
				valid = false;
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

        if (valid)
        {
        	str[65] = " ".concat(Integer.toString(PROindi.A));
        	str[67] = " ".concat(Integer.toString(PROindi.C));
        	str[68] = " ".concat(Integer.toString(PROindi.D));
        	str[69] = " ".concat(Integer.toString(PROindi.E));
        	str[70] = " ".concat(Integer.toString(PROindi.F));
        	str[71] = " ".concat(Integer.toString(PROindi.G));
        	str[72] = " ".concat(Integer.toString(PROindi.H));
        	str[73] = " ".concat(Integer.toString(PROindi.I));
        	str[75] = " ".concat(Integer.toString(PROindi.K));
        	str[76] = " ".concat(Integer.toString(PROindi.L));
        	str[77] = " ".concat(Integer.toString(PROindi.M));
        	str[78] = " ".concat(Integer.toString(PROindi.N));
        	str[80] = " ".concat(Integer.toString(PROindi.P));
        	str[81] = " ".concat(Integer.toString(PROindi.Q));
        	str[82] = " ".concat(Integer.toString(PROindi.R));
        	str[83] = " ".concat(Integer.toString(PROindi.S));
        	str[84] = " ".concat(Integer.toString(PROindi.T));
        	str[86] = " ".concat(Integer.toString(PROindi.V));
        	str[87] = " ".concat(Integer.toString(PROindi.W));
        	str[89] = " ".concat(Integer.toString(PROindi.Y));

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