/*******************************************************************************
 *	Copyright (c) 2000 Silvere Martin-Michiellot All Rights Reserved.
 *	Silvere Martin-Michiellot grants you ("Licensee") a non-exclusive,
 *	royalty free, license to use, modify and redistribute this
 *	software in source and binary code form,
 *	provided that i) this copyright notice and license appear on all copies of
 *	the software; and ii) Licensee does not utilize the software in a manner
 *	which is disparaging to Silvere Martin-Michiellot.
 *
 *	This software is provided "AS IS," without a warranty of any kind. ALL
 *	EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 *	IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 *	NON-INFRINGEMENT, ARE HEREBY EXCLUDED. Silvere Martin-Michiellot
 *	AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 *	SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 *	OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL
 *	Silvere Martin-Michiellot OR ITS LICENSORS BE LIABLE
 *	FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 *	INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 *	CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 *	OR INABILITY TO USE SOFTWARE, EVEN IF Silvere Martin-Michiellot HAS BEEN
 *	ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 *	This software is not designed or intended for use in on-line control of
 *	aircraft, air traffic, aircraft navigation or aircraft communications; or in
 *	the design, construction, operation or maintenance of any nuclear
 *	facility. Licensee represents and warrants that it will not use or
 *	redistribute the Software for such purposes.
*******************************************************************************/

/*******************************************************************************
 *	This code is modified by Naman Turakhia @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *
 *	This code is repackaged after the code from Craig A. Lindley, from Digital
 *	Audio with Java
 *
 *	Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
 *	package com.db.media.audio.dsp.monitors;
*******************************************************************************/

/*******************************************************************************
 *	This is a Java implementation of the fast Fourier transform written by Jef
 *	Poskanzer. The copyright appears above.
*******************************************************************************/

/*******************************************************************************
 *	libfft.c - fast Fourier transform library
 *
 *	Copyright (C) 1989 by Jef Poskanzer.
 *
 *	Permission to use, copy, modify, and distribute this software and its
 *	documentation for any purpose and without fee is hereby granted, provided
 *	that the above copyright notice appear in all copies and that both that
 *	copyright notice and this permission notice appear in supporting
 *	documentation.  This software is provided "as is" without express or
 *	implied warranty.
*******************************************************************************/

import java.util.Scanner;
public class FFT
{
	private static final double TWOPI = 2.0 * Math.PI;

	// Limits on the number of bits this algorithm can utilize
	private static final int LOG2_MAXFFTSIZE = 27;
	private static final int MAXFFTSIZE = 1 << LOG2_MAXFFTSIZE;

	// Private class data
	private static int bits;
	private static int [] bitreverse;

	/*******************************************************************************
	 *	FFT class constructor
	 *	Initializes code for doing a fast Fourier transform
	 *
	 *	@param int bits is a power of two such that 2^b is the number
	 *	of samples.
	******************************************************************************/

	public FFT(int bits)
	{

		this.bits = bits;

		if (bits > LOG2_MAXFFTSIZE)
		{
			//System.out.println("" + bits + " is too big");
			System.exit(1);
		}
		else
		{
			bitreverse = new int[1<<bits];
			for (int i = (1 << bits) - 1; i >= 0; --i)
			{
				int k = 0;
				for (int j = 0; j < bits; ++j)
				{
					k *= 2;
					if ((i & (1 << j)) != 0)
					{
						k++;
					}
				}
				bitreverse[i] = k;
			}
		}
	}



	protected static Complex[] FFToutput(String args)
	{
		//int N = Integer.parseInt(args[0]);
		String s=args;
		int limit=-5; int diff=0;
		//WordCount a=new WordCount(s);
		String[] m;
		s=s.replaceAll("N","");
		s=s.trim().replaceAll(" +"," ");
		String l;
		String finale="";
		int a;
		String tt;
		Scanner h=new Scanner(s);
		while(h.hasNext())
		{
			l=h.next();
			if(l.contains("+") || !(l.contains("-")))
			{
				if(!finale.isEmpty())
					finale=finale.concat(" "+l);
				else
					finale=l;
			}
			else
			{
				a=l.lastIndexOf("-");
				if(a!=0)
				{
					tt=l.substring(0,a);
					if(!finale.isEmpty())
						finale=finale.concat(" ");
					finale=finale.concat(tt);
					finale=finale.concat("+");
					finale=finale.concat(l.substring(a));
				}
				else
				{
					if(!finale.isEmpty())
						finale=finale.concat(" ");
					finale=finale.concat(l);
				}
			}
		}
		s=finale;
		int bit=0;

		//System.out.println("Now s is this only: hoo: "+s);
		m=s.split(" ");
		int N=m.length;
		Complex[] x;

		//System.out.println("\n\n N is :"+N+"\n");
		int kd=0;
		for(int j=0;j<N;j++)
		{
			if(N==Math.pow(2,j))
			{
				bit=j;
				kd=N; break;
			}
			else if (Math.pow(2,j)>N)
			{
				kd=(int)Math.pow(2,j);
				bit=j;
				limit=j;
				break;
			}
			else
				continue;
		}
		x=new Complex[kd];
		if(limit!=-5)
		{
			diff=(int)Math.pow(2,limit)-N;

			//System.out.println("\n diff is :"+diff);
			for(int k=0;k<diff;k++)
			{
				x[k]=new Complex(0,0);
			}
		}
		int d;

		//File f=new File(s);
		//Scanner in=new Scanner(f);
		//original data

		for (int i = 0; i < N; i++)
		{
			if(limit==-5)
				d=0;
			else
				d=diff;
			x[i+d]=convert(m[i]);

		}

		Complex[] y = fft(x,bit);
		return y;
	}

	protected static Complex[] fft(Complex[] x,int bit)
	{
		FFT f=new FFT(bit);
		double[] xr=new double[x.length];
		double[] xi=new double[x.length];
		for(int h=0;h<x.length;h++)
		{
			xr[h]=x[h].re();
			xi[h]=x[h].im();
		}
		Complex[] retur=f.doFFT(xr,xi,false);
		return retur;
	}

	protected static Complex[] ifft(Complex[] x)
	{
		int bit=0;
		int kd=0;
		int limit=0;
		int N=x.length;
		for(int j=0;j<N;j++)
		{
			if(N==Math.pow(2,j))
			{
				bit=j;
				kd=N; break;
			}
			else if (Math.pow(2,j)>N)
			{
				kd=(int)Math.pow(2,j);
				bit=j;
				limit=j;
				break;
			}
			else
				continue;
		}
		FFT f=new FFT(bit);
		double[] xr=new double[x.length];
		double[] xi=new double[x.length];
		for(int h=0;h<x.length;h++)
		{
			xr[h]=x[h].re();
			xi[h]=x[h].im();
		}
		Complex[] retur=f.doFFT(xr,xi,true);
		return retur;
	}


	protected static Complex convert(String s)
	{
		if(s.contains("+"))
		{
			String[] temp;

			//if(s.contains("+"))
			temp=s.split("\\+");

			// else temp=s.split("\\-");
			//System.out.println("sdf"+temp[0]+"  "+temp[1]);

			double tem=Double.parseDouble(temp[0]);
			double tem2=0;

			//System.out.println("length is actually : \n"+temp[1].length()+"and temp[1] is : "+temp[1]);

			if(temp[1].equals("i"))
			{
				tem2=1;
			}
			else if(temp[1].equals("-i"))
			{
				tem2=-1;
			}
			else
			{
				String[] temp2=temp[1].split("i");
				tem2=Double.parseDouble(temp2[0]);
			}
			return new Complex(tem,tem2);
		}
		else
		{
			//System.out.println("s in convert is: "+s+"\n");
			double m=Double.parseDouble(s);
			return new Complex(m,0);
		}
	}

	/*******************************************************************************
	 *	A fast Fourier transform routine
	 *
	 *	@param double [] xr is the real part of the data to be transformed
	 *	@param double [] xi is the imaginary part of the data to be transformed
	 *	(normally zero unless inverse transofrm is effect).
	 *	@param boolean invFlag which is true if inverse transform is being
	 *	applied. false for a forward transform.
	*******************************************************************************/


	public static Complex[] doFFT(double [] xr, double [] xi, boolean invFlag)
	{
		int n, n2, i, k, kn2, l, p;
		double ang, s, c, tr, ti;

		n2 = (n = (1 << bits)) / 2;

		for (l = 0; l < bits; ++l)
		{
			for (k = 0; k < n; k += n2)
			{
				for (i = 0; i < n2; ++i, ++k)
				{
					p = bitreverse[k / n2];
					ang = TWOPI * p / n;
					c = Math.cos(ang);
					s = Math.sin(ang);
					kn2 = k + n2;

					if (invFlag)
						s = -s;

					tr = xr[kn2] * c + xi[kn2] * s;
					ti = xi[kn2] * c - xr[kn2] * s;

					xr[kn2] = xr[k] - tr;
					xi[kn2] = xi[k] - ti;
					xr[k] += tr;
					xi[k] += ti;
				}
			}
			n2 /= 2;
		}

		for (k = 0; k < n; k++)
		{
			if ((i = bitreverse[k]) <= k)
				continue;

			tr = xr[k];
			ti = xi[k];
			xr[k] = xr[i];
			xi[k] = xi[i];
			xr[i] = tr;
			xi[i] = ti;
			//System.out.println(""+xr[i]+"+"+xi[i]+"i  n:"+n);
		}


		// Finally, multiply each value by 1/n, if this is the forward
		// transform.
		if (invFlag)
		{
			double f = 1.0/n;

			for (i = 0; i < n ; i++)
			{
				xr[i] *= f;
				xi[i] *= f;
				//System.out.println(""+xr[i]+"+"+xi[i]+"i");
			}
			//System.out.println("max fft size: bitreverse length"+bitreverse.length+" "+Math.pow(2,bitreverse.length));
		}
		Complex[] ret=new Complex[n];
		for(k=0;k<n;k++)
		{
			// 	System.out.println(""+xr[k]+"+"+xi[k]+"i");
			ret[k]=new Complex(xr[k],xi[k]);
		}
		return ret;
	}
}