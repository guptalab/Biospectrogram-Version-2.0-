/*******************************************************************************
 *	Z Transform (T03)
 *
 *	Input: 1D real/complex array, a complex number z at which transform
 *	is to be computed
 *	Output: A complex number which is Z transform of given input array at
 *	given complex number z
 *
 *	This code is written by Naman Turakhia @ daiict
 *	This code was last modified on 8th of May, 2013
 *******************************************************************************/

import java.util.*;
import java.io.*;

public class ZTransform
{
	private Complex z;
	private String s;

	public ZTransform(String s1,Complex z1)
	{
		z=z1;
		s=s1;
	}

	protected Complex getZ()
	{
		return z;
	}

	protected String getFilename()
	{
		return s;
	}

	protected void setZ(Complex z2)
	{
		z=z2;
	}

	protected void setFilename(String m)
	{
		s=m;
	}

	protected Complex ZTransformed()
	{
		Complex last=new Complex(0,0);
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
				if(!finale.isEmpty())
                	finale=finale.concat(" "+l);
				else
					finale=l;
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
		//System.out.println("Now s is this only: hoo: "+s);
        String[] m1=s.split(" ");
		Scanner in=new Scanner(s);
		long word=m1.length;
		for(long i=0;i<word;i++)
		{
			if(i==0)
			{
				last=last.plus(convert(in.next()));
				continue;
			}
			last=last.plus(convert(in.next()).times(this.Raisedto(-i)));
		 }
		 return last;
	}

	private Complex Raisedto(long n)
	{
		Complex temp3=new Complex(0,0);
		if(z.Equals(temp3))
			return z;
		else if(n==0)
			return new Complex(1,0);
		else if(n<0)
		{
			Complex temp=new Complex(0,0);
			temp=z.reciprocal();
			Complex result=temp;
			n=n*(-1);
			for(long j=1;j<n;j++)
			{
				result=result.times(temp);
			}
			return result;
		}
		else
		{
			Complex result=z;
			for(long j=1;j<n;j++)
			{
				result=result.times(z);
			}
			return result;
		}
	}

	private Complex convert(String s)
	{
    	if(s.contains("+"))
        {
			String[] temp=s.split("\\+");
        	double tem=Double.parseDouble(temp[0]);
			double tem2=0;
			if(temp[1].equals("i"))
            	tem2=1;
			else if(temp[1].equals("-i"))
            	tem2=-1;
			else
            {
                String[] temp2=temp[1].split("i");
				tem2=Double.parseDouble(temp2[0]);
			}
        	return new Complex(tem,tem2);
		}
        else
        	return new Complex(Double.parseDouble(s),0);
	}
}