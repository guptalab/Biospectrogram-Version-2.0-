/*******************************************************************************
 *	Protein Real Value Encoding (User Choice) (E07)
 *	User can enter real value of his choice for encodin.
 *
 *	Protein Random Real Value Encoding (E07.4)
 *	Random real values will be generated for each of 20 characters and used for
 *	mapping
 *
 *	This code is written by Nilay Chheda @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.text.DecimalFormat;

public class PROreal
{
	private String t_str,in,out;
	private String[] str;
	private FileWriter writer;
	private FileReader reader;
	private int temp,space_flag;
	private char[] c;
	private static int option;

    //static values of electron-ion interaction
    public static double A=0.0373,C=0.0829,D=0.1263,E=0.0058,F=0.0946,G=0.0050,H=0.0242,I=0.0000,K=0.0371,L=0.0000,M=0.0823,N=0.0036,P=0.0198,Q=0.0761,R=0.0959,S=0.0829,T=0.0941,V=0.0057,W=0.0548,Y=0.0516;


	public PROreal(File fasta)
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
			option = JOptionPane.showConfirmDialog(null,"Do you want to enter values ? (No means random value will be used)","",JOptionPane.YES_NO_OPTION);
			if (option == 0)
        		out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E07.3.fasta";
			else
				out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E07.4.fasta";
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
    	if (option == 0)  	// user choice
    	{
    		JOptionPane.showMessageDialog(null,"Please enter only real number values.");
    		try
    		{
    			A = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for A: "));
    			C = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for C: "));
    			D = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for D: "));
    			E = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for E: "));
    			F = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for F: "));
    			G = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for G: "));
    			H = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for H: "));
    			I = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for I: "));
    			K = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for K: "));
    			L = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for L: "));
    			M = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for M: "));
    			N = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for N: "));
    			P = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for P: "));
    			Q = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for Q: "));
    			R = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for R: "));
    			S = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for S: "));
    			T = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for T: "));
    			V = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for V: "));
    			W = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for W: "));
    			Y = Double.parseDouble(JOptionPane.showInputDialog(null,"Value for Y: "));
    		}
			catch(NumberFormatException nfe)
			{
				JOptionPane.showMessageDialog(null,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
            	return false;
			}
    	}
    	else	// Random
    	{
    		double val;
			double[]  vals = new double[20];
        	Random rnd = new Random();

			for(int i=0;i<20;i++)
			{
        		int a;
            	int b;
				a = rnd.nextInt (10);
            	b = rnd.nextInt (100);
				if (b==0||b==10||b==20||b==30||b==40||b==50||b==60||b==70||b==80||b==90)
            	{
					b = b+1;
				}
            	vals[i] = (double)a + (double) b/100;
				DecimalFormat df = new DecimalFormat("0.00");
            	vals[i] =  Double.valueOf(df.format(vals[i]));
			}

			String s = "Random encoding has produced following mapping\n";
        	s = s+"A > "+vals[0]+"\nC > "+vals[1]+"\nD > "+vals[2]+"\nE > "+vals[3]+"\n";
        	s = s+"F > "+vals[4]+"\nG > "+vals[5]+"\nH > "+vals[6]+"\nI > "+vals[7]+"\n";
        	s = s+"K > "+vals[8]+"\nL > "+vals[9]+"\nM > "+vals[10]+"\nN > "+vals[11]+"\n";
        	s = s+"P > "+vals[12]+"\nQ > "+vals[13]+"\nR > "+vals[14]+"\nS > "+vals[15]+"\n";
        	s = s+"T > "+vals[16]+"\nV > "+vals[17]+"\nW > "+vals[18]+"\nY > "+vals[19]+"\n";

			JOptionPane.showMessageDialog (null,s);

			A = vals[0];
        	C = vals[1];
			D = vals[2];
        	E = vals[3];
        	F = vals[4];
        	G = vals[5];
			H = vals[6];
        	I = vals[7];
        	K = vals[8];
        	L = vals[9];
			M = vals[10];
        	N = vals[11];
        	P = vals[12];
        	Q = vals[13];
			R = vals[14];
        	S = vals[15];
        	T = vals[16];
        	V = vals[17];
			W = vals[18];
        	Y = vals[19];
    	}

        for(int i=0;i<256;i++)
        {
            str[i] = String.valueOf((char)i);
        }
        str[13] = "";
        str[10] = "\n";
        str[65] = " ".concat(Double.toString(PROreal.A));
        str[67] = " ".concat(Double.toString(PROreal.C));
        str[68] = " ".concat(Double.toString(PROreal.D));
        str[69] = " ".concat(Double.toString(PROreal.E));
        str[70] = " ".concat(Double.toString(PROreal.F));
        str[71] = " ".concat(Double.toString(PROreal.G));
        str[72] = " ".concat(Double.toString(PROreal.H));
        str[73] = " ".concat(Double.toString(PROreal.I));
        str[75] = " ".concat(Double.toString(PROreal.K));
        str[76] = " ".concat(Double.toString(PROreal.L));
        str[77] = " ".concat(Double.toString(PROreal.M));
        str[78] = " ".concat(Double.toString(PROreal.N));
        str[80] = " ".concat(Double.toString(PROreal.P));
        str[81] = " ".concat(Double.toString(PROreal.Q));
        str[82] = " ".concat(Double.toString(PROreal.R));
        str[83] = " ".concat(Double.toString(PROreal.S));
        str[84] = " ".concat(Double.toString(PROreal.T));
        str[86] = " ".concat(Double.toString(PROreal.V));
        str[87] = " ".concat(Double.toString(PROreal.W));
        str[89] = " ".concat(Double.toString(PROreal.Y));

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