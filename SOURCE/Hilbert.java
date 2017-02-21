/*******************************************************************************
 *	Hilbert Transform (T02) & Analytic Signal (T04)
 *
 *	Analytic Signal can also be computed with the help of Hilbert Transform
 *
 *	Input: 1D real array for finding analytic signal,
 *	1D real/complex array for finding hilbert transform in frequency domain
 *	Output: for analytic signal, 1D array of complex numbers which is analytic
 *	signal in time domain for the given input signal
 *	Output: for Hilbert transform, 1D array of complex numbers which is hilbert
 *	transform of given input in frequency domain
 *
 *	This code is written by Naman Turakhia @ daiict using algorithm discussed
 *	in "The Hilbert transform" by Mathias Johansson,
 *	Master Thesis in Mathematics/Applied Mathematics
 *
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.io.*;

public class Hilbert
{
	private Complex[] s1;
	private String input;

	public Hilbert(String m)
	{
	       s1=FFT.FFToutput(m);
	       input=m;
	}

	protected Complex[] HilbertOutput()
	{
		Complex[] s=s1;
		int length=s.length;
		if(length%2==0)
		{
			for(int i=1;i<=((length/2)-1);i++)
			{
				Complex temp=s[i];
				s[i]=temp.times(new Complex(0,-1));
 				//System.out.println("\nhere is it :\n\n"+s[i].toString());
			}
			s[0]=new Complex(0,0);
			s[length/2]=new Complex(0,0);
			for(int i=((length/2)+1);i<=(length-1);i++)
			{
				s[i]=s[i].times(new Complex(0,1));
			}
			//System.out.println("\nHilbert transform in frequency domain:\n");
			for(int m=0;m<s.length;m++)
			{
				//System.out.println(s[m].toString());
			}
			return s;
		}
		else
		{
			for(int i=1;i<=((length-1)/2);i++)
			{
				s[i]=s[i].times(new Complex(0,-1));
			}
			s[0]=new Complex(0,0);
			for(int i=((length+1)/2);i<=(length-1);i++)
			{
				s[i]=s[i].times(new Complex(0,1));
			}
			return s;
		}
	}

	protected Complex[] AnalyticSignal()
    {
		Complex[] result;
        Complex[] m=FFT.ifft(FFT.FFToutput(input));
		if(input.contains("i"))
        {
            //System.out.println("Error: Input must be a real sequence to find its analytic signal.\n");
			return null;
		}
		Complex[] c=FFT.ifft(HilbertOutput());
		for(int j=0;j<c.length;j++)
       	{
			c[j]=c[j].times(new Complex(0,1));
		}
        result=new Complex[m.length];
		//System.out.println("\n\nanalytic signal is: \n");
        for(int i=0;i<m.length;i++)
		{
			result[i]=m[i].plus(c[i]);
            //System.out.println(result[i].toString());
		}
        return result;
	}
}
