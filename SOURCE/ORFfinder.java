/*******************************************************************************
 *	This code does the six frame ORF finding for any given DNA sequence.
 *
 *	Start Codon: "ATG"
 *	Stop Codon: "TAG" or "TAA" or "TGA"
 *
 *	This code is written by Nilay Chheda @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;
import java.io.*;

public class ORFfinder
{
	// Fields
	private int upLimit=100000;
	private int downLimit=0;
	private String seq;
	private String revSeq;
	private ArrayList<resultSet> results = new ArrayList<resultSet>();

	// Constructor
	public ORFfinder(String str)
	{
		seq = str;
		revSeq = this.reverseString(this.complement(seq));
	}

	/*******************************************************************************
	 *	This method do all the computation by simplly
	 *	calling appropriate methods
	 ******************************************************************************/

	protected String compute()
	{
		this.findORF();
		this.sort();
		return this.printORF();
	}

	/*******************************************************************************
	 *	This method compute complement of entire DNA sequence which used for
	 *	finding ORF on reverse DNA strand.
	 ******************************************************************************/
	protected String complement(String s)
	{
		char[] array = s.toCharArray();
		for(int i=0;i<s.length();i++)
		{
			if (array[i] == 'A') array[i] = 'T';
			else if (array[i] == 'T') array[i] = 'A';
			else if (array[i] == 'C') array[i] = 'G';
			else if (array[i] == 'G') array[i] = 'C';
		}
		return new String(array);
	}

	/*******************************************************************************
	 *	This method calls six different methods to compute six frame ORFs.
	 ******************************************************************************/
	protected void findORF()
	{
		this.findORF1();
		this.findORF2();
		this.findORF3();

		this.findrORF1();
		this.findrORF2();
		this.findrORF3();
	}

	/*******************************************************************************
	 *	This method finds ORF for "+1" frame
	 ******************************************************************************/
	protected void findORF1()
	{
		boolean flag=true;
		String temp = seq;
		int startIndex=0,stopIndex=0;
		ArrayList<Integer> starts = new ArrayList<Integer>();
		for(int i=0;i+3<=temp.length();i=i+3)
		{
			String cut = temp.substring(i,i+3);
			if (flag)
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					//System.out.println("Found Start Codon ("+cut+") from "+(i+0+1)+" to "+(i+2+1));
					//System.out.println("Now searching for Stop Codon...\n");
					flag = false;
					startIndex = i+0+1;
				}
			}
			else
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					starts.add(i+0+1);
				}

				if(cut.equalsIgnoreCase("TAG")||cut.equalsIgnoreCase("TAA")||cut.equalsIgnoreCase("TGA"))
				{
					//System.out.println("Found Stop Codon ("+cut+") from "+(i+0+1)+" to "+(i+2+1));
					//System.out.println("Now searching for Start Codon...\n");
					flag = true;
					stopIndex = i+2+1;

					int[] codons = new int[starts.size()];
					for(int j=0;j<starts.size();j++)
					{
						codons[j] = (int) starts.get(j);
					}
					starts.clear();
					if (upLimit >= stopIndex - startIndex && stopIndex - startIndex >= downLimit)
						results.add(new resultSet("+1",startIndex,stopIndex,stopIndex-startIndex+1,codons));
				}

			}
		}
	}

	/*******************************************************************************
	 *	This method finds ORF for "+2" frame
	 ******************************************************************************/
	protected void findORF2()
	{
		boolean flag=true;
		String temp = seq.substring(1);
		int startIndex=0,stopIndex=0;
		ArrayList<Integer> starts = new ArrayList<Integer>();
		for(int i=0;i+3<=temp.length();i=i+3)
		{
			String cut = temp.substring(i,i+3);
			if (flag)
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					//System.out.println("Found Start Codon ("+cut+") from "+(i+0+2)+" to "+(i+2+2));
					//System.out.println("Now searching for Stop Codon...\n");
					flag = false;
					startIndex = i+0+2;
				}
			}
			else
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					starts.add(i+0+2);
				}

				if(cut.equalsIgnoreCase("TAG")||cut.equalsIgnoreCase("TAA")||cut.equalsIgnoreCase("TGA"))
				{
					//System.out.println("Found Start Codon ("+cut+") from "+(i+0+2)+" to "+(i+2+2));
					//System.out.println("Now searching for Start Codon...\n");
					flag = true;
					stopIndex = i+2+2;

					int[] codons = new int[starts.size()];
					for(int j=0;j<starts.size();j++)
					{
						codons[j] = (int) starts.get(j);
					}
					starts.clear();
					if (upLimit >= stopIndex - startIndex && stopIndex - startIndex >= downLimit)
						results.add(new resultSet("+2",startIndex,stopIndex,stopIndex-startIndex+1,codons));
				}

			}
		}
	}

	/*******************************************************************************
	 *	This method finds ORF for "+3" frame
	 ******************************************************************************/
	protected void findORF3()
	{
		boolean flag=true;
		String temp = seq.substring(2);
		int startIndex=0,stopIndex=0;
		ArrayList<Integer> starts = new ArrayList<Integer>();
		for(int i=0;i+3<=temp.length();i=i+3)
		{
			String cut = temp.substring(i,i+3);
			if (flag)
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					//System.out.println("Found Start Codon ("+cut+") from "+(i+0+3)+" to "+(i+2+3));
					//System.out.println("Now searching for Stop Codon...\n");
					flag = false;
					startIndex = i+0+3;
				}
			}
			else
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					starts.add(i+0+3);
				}

				if(cut.equalsIgnoreCase("TAG")||cut.equalsIgnoreCase("TAA")||cut.equalsIgnoreCase("TGA"))
				{
					//System.out.println("Found Start Codon ("+cut+") from "+(i+0+3)+" to "+(i+2+3));
					//System.out.println("Now searching for Start Codon...\n");
					flag = true;
					stopIndex = i+2+3;

					int[] codons = new int[starts.size()];
					for(int j=0;j<starts.size();j++)
					{
						codons[j] = (int) starts.get(j);
					}
					starts.clear();
					if (upLimit >= stopIndex - startIndex && stopIndex - startIndex >= downLimit)
						results.add(new resultSet("+3",startIndex,stopIndex,stopIndex-startIndex+1,codons));
				}

			}
		}
	}

	/*******************************************************************************
	 *	This method finds ORF for "-1" frame
	 ******************************************************************************/
	protected void findrORF1()
	{
		boolean flag=true;
		String temp = revSeq;
		int startIndex=0,stopIndex=0;
		ArrayList<Integer> starts = new ArrayList<Integer>();
		for(int i=0;i+3<=temp.length();i=i+3)
		{
			String cut = temp.substring(i,i+3);
			if (flag)
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					//System.out.println("Found Start Codon ("+cut+") from "+(i+0+1)+" to "+(i+2+1));
					//System.out.println("Now searching for Stop Codon...\n");
					flag = false;
					stopIndex = seq.length()-(i+0+1) + 1;
				}
			}
			else
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					starts.add(seq.length()-(i+0+1) + 1);
				}

				if(cut.equalsIgnoreCase("TAG")||cut.equalsIgnoreCase("TAA")||cut.equalsIgnoreCase("TGA"))
				{
					//System.out.println("Found Stop Codon ("+cut+") from "+(i+0+1)+" to "+(i+2+1));
					//System.out.println("Now searching for Start Codon...\n");
					flag = true;
					startIndex = seq.length()-(i+2+1) + 1;

					int[] codons = new int[starts.size()];
					for(int j=0;j<starts.size();j++)
					{
						codons[j] = (int) starts.get(j);
					}
					starts.clear();
					if (upLimit >= stopIndex - startIndex && stopIndex - startIndex >= downLimit)
						results.add(new resultSet("-1",startIndex,stopIndex,stopIndex-startIndex+1,codons));
				}
			}
		}
	}

	/*******************************************************************************
	 *	This method finds ORF for "-2" frame
	 ******************************************************************************/
	protected void findrORF2()
	{
		boolean flag=true;
		String temp = revSeq.substring(1);
		int startIndex=0,stopIndex=0;
		ArrayList<Integer> starts = new ArrayList<Integer>();
		for(int i=0;i+3<=temp.length();i=i+3)
		{
			String cut = temp.substring(i,i+3);
			if (flag)
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					//System.out.println("Found Start Codon ("+cut+") from "+(i+0+2)+" to "+(i+2+2));
					//System.out.println("Now searching for Stop Codon...\n");
					flag = false;
					stopIndex = seq.length()-(i+0+2) + 1;
				}
			}
			else
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					starts.add(seq.length()-(i+0+2) + 1);
				}

				if(cut.equalsIgnoreCase("TAG")||cut.equalsIgnoreCase("TAA")||cut.equalsIgnoreCase("TGA"))
				{
					//System.out.println("Found Start Codon ("+cut+") from "+(i+0+2)+" to "+(i+2+2));
					//System.out.println("Now searching for Start Codon...\n");
					flag = true;
					startIndex = seq.length()-(i+2+2) + 1;

					int[] codons = new int[starts.size()];
					for(int j=0;j<starts.size();j++)
					{
						codons[j] = (int) starts.get(j);
					}
					starts.clear();
					if (upLimit >= stopIndex - startIndex && stopIndex - startIndex >= downLimit)
						results.add(new resultSet("-2",startIndex,stopIndex,stopIndex-startIndex+1,codons));
				}

			}
		}
	}

	/*******************************************************************************
	 *	This method finds ORF for "-3" frame
	 ******************************************************************************/
	protected void findrORF3()
	{
		boolean flag=true;
		String temp = revSeq.substring(2);
		int startIndex=0,stopIndex=0;
		ArrayList<Integer> starts = new ArrayList<Integer>();
		for(int i=0;i+3<=temp.length();i=i+3)
		{
			String cut = temp.substring(i,i+3);
			if (flag)
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					//System.out.println("Found Start Codon ("+cut+") from "+(i+0+3)+" to "+(i+2+3));
					//System.out.println("Now searching for Stop Codon...\n");
					flag = false;
					stopIndex = seq.length()-(i+0+3) + 1;
				}
			}
			else
			{
				if(cut.equalsIgnoreCase("ATG"))
				{
					starts.add(seq.length()-(i+0+3));
				}

				if(cut.equalsIgnoreCase("TAG")||cut.equalsIgnoreCase("TAA")||cut.equalsIgnoreCase("TGA"))
				{
					//System.out.println("Found Start Codon ("+cut+") from "+(i+3)+" to "+(i+2+3));
					//System.out.println("Now searching for Start Codon...\n");
					flag = true;
					startIndex = seq.length()-(i+2+3) + 1;

					int[] codons = new int[starts.size()];
					for(int j=0;j<starts.size();j++)
					{
						codons[j] = (int) starts.get(j);
					}
					starts.clear();
					if (upLimit >= stopIndex - startIndex && stopIndex - startIndex >= downLimit)
						results.add(new resultSet("-3",startIndex,stopIndex,stopIndex-startIndex+1,codons));
				}
			}
		}
	}

	/*******************************************************************************
	 *	This method sets limit on maximum size of ORF to be found
	 ******************************************************************************/
	protected void setULimit(int a)
	{
		if (a > 0 && a < seq.length())
			this.upLimit = a;
	}

	/*******************************************************************************
	 *	This method sets limit on minimum size of ORF to be found
	 ******************************************************************************/
	protected void setDLimit(int a)
	{
		if (a > 0 && a < seq.length())
			this.downLimit = a;
	}

	/*******************************************************************************
	 *	This method reverses the input string which is used for finding ORF on
	 *	reverse DNA strand.
	 ******************************************************************************/
	protected String reverseString(String s)
	{
		return new StringBuffer(s).reverse().toString();
	}

	/*******************************************************************************
	 *	This method sorts the results according to total size of the ORF.
	 *	Don't use this method if you want to see results according to six frames.
	 ******************************************************************************/
	protected void sort()
	{
		for(int i=0;i<results.size();i++)
		{
			resultSet temp = results.get(i);
			int j;
			for(j=i;j>0 && temp.length < results.get(j-1).length;j--)
			{
				results.set(j,results.get(j-1));
			}
			results.set(j,temp);
		}
	}

	/*******************************************************************************
	 *	This method writes final output in an output file in nice formatted way.
	 ******************************************************************************/
	protected String printORF()
	{
		String output = "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n";
		output = output+"Frame | From | To | Length | Start Codons                 \n";
		output = output+"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n";
		for (int i=results.size()-1;i>=0;i--)
		{
			output = output+results.get(i).toString();
		}
		return output;
	}

	protected void generateORFFasta(int fetchFlag)
	{
		try
		{
			String a = JOptionPane.showInputDialog(null,"Enter minimum size for ORF : ");
			String b = JOptionPane.showInputDialog(null,"Enter maximum size for ORF : ");
			int min = Integer.parseInt(a);
			int max = Integer.parseInt(b);
			Scanner sc = new Scanner(this.printORF());

			// Skip first three lines
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();

			String line = "";
			int cnt=0;

			String src = mainApp.getOnlySequence(fetchFlag,1).toString();

			src = src.replaceAll("\n","");
			src = src.replaceAll("\0","");

			for (int i=0;sc.hasNextLine();i++)
			{
				line = sc.nextLine();
				String[] ary = line.split(" | ");

				//System.out.println(ary[2]); //startIndex
				//System.out.println(ary[4]); //stopIndex
				//System.out.println(ary[6]); //length of ORF

				if (Integer.parseInt(ary[6]) < min || Integer.parseInt(ary[6]) > max)
					continue;
				else
				{
					String temp = src;

					temp = temp.substring(Integer.parseInt(ary[2])-1,Integer.parseInt(ary[4]));

					String fname = "../History/ORF/ORF_"+ary[2]+"_"+ary[4]+"_"+mainApp.fetchedHistory.getElement(fetchFlag).getName();
					FileWriter fw = new FileWriter(new File(fname));
					cnt++;
					fw.write("> This file is generated using ORF Finder finction.");
					char[] array = temp.toCharArray();
					for(int k=0;k<array.length;k++)
					{
						String temp1 = ""+array[k];
						if (k%70 == 0)
						{
							temp1 = "\n"+array[k];
						}
						fw.write(temp1);
					}
					fw.close();
				}
			}
			JOptionPane.showMessageDialog(null,"Fasta files cotaining ORFs are generated in /History/ORF folder.");
		}
		catch(FileNotFoundException fnfe)
		{
			mainApp.errorHandler(fnfe);
		}
		catch(IOException ioe)
		{
			mainApp.errorHandler(ioe);
		}
	}

	/*******************************************************************************
	 *	This inner class is defines ORF as an object
	 ******************************************************************************/
	class resultSet
	{
		public String frame;
		public int fromIndex;
		public int toIndex;
		public int length;
		public int[] startCodons;

		// Constructor
		public resultSet(String a,int b, int c, int d, int[] e)
		{
			this.frame = a;
			this.fromIndex = b;
			this.toIndex = c;
			this.length = d;
			this.startCodons = e;
		}

		/*******************************************************************************
	 	*	This method is used for creating formatted output.
	 	******************************************************************************/
		public String toString()
		{
			String codons="";
			for(int i=0;i<startCodons.length;i++)
			{
				if (i==0)
				{
					codons = codons+startCodons[i];
				}
				else
				{
					codons = codons+" , "+startCodons[i];
				}

			}

			return frame+" | "+fromIndex+" | "+toIndex+" | "+length+" | { "+codons+" } \n";
		}
	}
}
