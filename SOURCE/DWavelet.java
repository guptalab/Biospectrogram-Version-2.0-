/*******************************************************************************
 *	Discrete Haar Wavelet Transform (T05)
 *
 *  http://en.wikipedia.org/wiki/Discrete_wavelet_transform
 *
 *	This code is written by Naman Turakhia @ daiict
 *	This code was last modified on 8th of May, 2013
 *******************************************************************************/

import java.io.*;
import java.util.*;
public class DWavelet
{
	protected static double[] DHWaveletTransform(double[] input)
    {
		// This function assumes that input.length=2^n, n>1
		double[] output = new double[input.length];

        for (int length = input.length >> 1; ; length >>= 1)
		{
        	// length = input.length / 2^n, WITH n INCREASING to log(input.length) / log(2)
            for (int i = 0; i < length; ++i)
			{
            	double sum = (input[i * 2] + input[i * 2 + 1])*(1/Math.sqrt(2));
                double difference = (input[i * 2] - input[i * 2 + 1])*(1/Math.sqrt(2));
				output[i] = sum;
                output[length + i] = difference;

			}
            if (length == 1)
			{
            	return output;
			}
            //Swap arrays to do next iteration
			System.arraycopy(output, 0, input, 0, length << 1);
		}
	}

	protected static double[] discreteHaarWaveletTransformoutput(String args)
    {
		String s=args;
   	    int limit=-5; int diff=0;
		String[] m;
        s = s.substring (1);
        s=s.replaceAll("N","");
		s=s.trim();
        s = s.replace("+"," ");
		s = s.replace("i","");
        s = s.replace("-"," -");
		m=s.split("\\s+");
        int N=m.length;
		double[] x;

		int kd=0;
		for(int j=0;j<N;j++)
		{
			if(N==Math.pow(2,j))
			{
        		kd=N;
                break;
			}
			else if (Math.pow(2,j)>N)
			{
            	kd=(int)Math.pow(2,j);
				limit=j;
                break;
			}
            else
			{
            	continue;
			}
		}
        x=new double[kd];
		if(limit!=-5)
		{
			diff=(int)Math.pow(2,limit)-N;
			for(int k=0;k<diff;k++)
			{
				x[k]=0;
			}
		}
		int d;
        for (int i = 0; i < N; i++)
		{
        	if(limit==-5)
        	{
				d=0;
			}
        	else
			{
        		d=diff;
			}
            if (!m[i].isEmpty ())
				x[i+d]=Double.parseDouble(m[i]);
		}
        double[] y = DHWaveletTransform(x);
		return y;
	}
}