/*******************************************************************************
 *	This code does conversion of DNA to Protein using standard genetic codes.
 *
 *	Conversion Table
 *	================
 *	TTT F Phe | TCT S Ser | TAT Y Tyr | TGT C Cys
 *	TTC F Phe | TCC S Ser | TAC Y Tyr | TGC C Cys
 *	TTA L Leu | TCA S Ser | TAA * Ter | TGA * Ter
 *	TTG L Leu | TCG S Ser | TAG * Ter | TGG W Trp
 *	CTT L Leu | CCT P Pro | CAT H His | CGT R Arg
 *	CTC L Leu | CCC P Pro | CAC H His | CGC R Arg
 *	CTA L Leu | CCA P Pro | CAA Q Gln | CGA R Arg
 *	CTG L Leu | CCG P Pro | CAG Q Gln | CGG R Arg
 *	ATT I Ile | ACT T Thr | AAT N Asn | AGT S Ser
 *	ATC I Ile | ACC T Thr | AAC N Asn | AGC S Ser
 *	ATA I Ile | ACA T Thr | AAA K Lys | AGA R Arg
 *	ATG M Met | ACG T Thr | AAG K Lys | AGG R Arg
 *	GTT V Val | GCT A Ala | GAT D Asp | GGT G Gly
 *	GTC V Val | GCC A Ala | GAC D Asp | GGC G Gly
 *	GTA V Val | GCA A Ala | GAA E Glu | GGA G Gly
 *	GTG V Val | GCG A Ala | GAG E Glu | GGG G Gly
 *
 *	NOTE: Stop codons are skipped from the output.
 *
 *	This code is written by Nilay Chheda @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/
import java.util.Scanner;
import javax.swing.JOptionPane;

public class toGene
{
	// Fields
	private String seq;
	private boolean flag = false;

	// Constructor
	public toGene(String str)
	{
		seq = str;
	}

	protected boolean getFlag()
	{
		return flag;
	}

	/*******************************************************************************
	 *	This method do all the computation by simplly
	 *	calling appropriate methods
	 ******************************************************************************/
	protected String convertToGene()
	{
		String output="";
		if (seq.length()%3 != 0)
		{
			JOptionPane.showMessageDialog(null,"Invalid ORF input entered !\n"+
				 "Make sure that total characters entered are in multiple of 3");
		}
		else
		{

			flag = true;
			int counter=0;
			for(int i=0;i<seq.length();i=i+3)
			{
				String temp = seq.substring(i,i+3);

				if (temp.equals("ATG"))
				{
					output = output+"M";
				}
				else if(temp.equals("TTT")||temp.equals("TTC"))
				{
					output = output+"F";
				}
				else if(temp.equals("TTA")||temp.equals("TTG")||seq.substring(i,i+2).equals("CT"))
				{
					output = output+"L";
				}
				else if(temp.equals("ATT")||temp.equals("ATC")||temp.equals("ATA"))
				{
					output = output+"I";
				}
				else if(seq.substring(i,i+2).equals("GT"))
				{
					output = output+"V";
				}
				else if(temp.equals("AGT")||temp.equals("AGC")||seq.substring(i,i+2).equals("TC"))
				{
					output = output+"S";
				}
				else if(seq.substring(i,i+2).equals("CC"))
				{
					output = output+"P";
				}
				else if(seq.substring(i,i+2).equals("AC"))
				{
					output = output+"T";
				}
				else if(seq.substring(i,i+2).equals("GG"))
				{
					output = output+"G";
				}
				else if(seq.substring(i,i+2).equals("GC"))
				{
					output = output+"A";
				}
				else if(temp.equals("TAT")||temp.equals("TAC"))
				{
					output = output+"Y";
				}
				else if(temp.equals("TAA")||temp.equals("TAG")||temp.equals("TGA"))
				{
					//output = output+"*";
					output = output+"";
				}
				else if(temp.equals("CAT")||temp.equals("CAC"))
				{
					output = output+"H";
				}
				else if(temp.equals("CAA")||temp.equals("CAG"))
				{
					output = output+"Q";
				}
				else if(temp.equals("AAT")||temp.equals("AAC"))
				{
					output = output+"N";
				}
				else if(temp.equals("AAA")||temp.equals("AAG"))
				{
					output = output+"K";
				}
				else if(temp.equals("GAT")||temp.equals("GAC"))
				{
					output = output+"D";
				}
				else if(temp.equals("GAA")||temp.equals("GAG"))
				{
					output = output+"E";
				}
				else if(temp.equals("TGT")||temp.equals("TGC"))
				{
					output = output+"C";
				}
				else if(temp.equals("TGG"))
				{
					output = output+"W";
				}
				else if(temp.equals("AGG")||temp.equals("AGA")||seq.substring(i,i+2).equals("CG"))
				{
					output = output+"R";
				}
				counter++;

				// Enter new line
				if (counter%70 == 0)
				{
					output = output+"\n";
				}
			}
		}
		return output;
	}
}