/*******************************************************************************
 *	Chirp Z Transform (T06)
 *
 *	Input: 1D real/complex array, a complex number z at which transform
 *	is to be computed
 *	Output: A complex number which is Z transform of given input array at
 *	given complex number z
 *
 *	z=a*(w.^(0:m-1)),  where w=exp(j*2*pi*u/v)= cos(2*pi*u/v)+jsin(2*pi*u/v),
 *	take a,u,v,m as input, where m is an integer
 *
 *	This code is written by Naman Turakhia @ daiict
 *	This code was last modified on 8th of May, 2013
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;

public class ChirpZTransform
{
	private Complex z;
	private String s;
	private double a1;
	private double u1;
	private double v1;
	private long m1;

	public ChirpZTransform()
	{
		String msg = "z=a*(w.^(0:m-1)),  where w=exp(j*2*pi*u/v)=cos(2*pi*u/v)+jsin(2*pi*u/v),take a,u,v,m as input, where m is an integer";
		JOptionPane.showMessageDialog(null,msg);

		String a = JOptionPane.showInputDialog (null,"Enter value for a");
		String u = JOptionPane.showInputDialog (null,"Enter value for u");
		String v = JOptionPane.showInputDialog (null,"Enter value for v");
		String m = JOptionPane.showInputDialog (null,"Enter value for m");

		this.a1 = Double.parseDouble(a);
		this.u1 = Double.parseDouble(u);
		this.v1 = Double.parseDouble(v);
		this.m1 = Long.parseLong(m);

		while ( !(m1 > 0 ))
		{
			JOptionPane.showMessageDialog(null,"Please enter positive integer value for m");
			m = JOptionPane.showInputDialog (null,"Enter value for m");
			this.m1 = Long.parseLong(m);
		}
		this.z = new Complex(a1,0);
	}

	// Constructor with predefined Attributes
	public ChirpZTransform(double a,double u,double v, long m)
	{
		this.a1 = a;
		this.u1 = u;
		this.v1 = v;
		this.m1 = m;
		this.z = new Complex(a1,0);
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

	protected Complex[] ChirpZTransformed(String st)
	{
		this.s = st;
		this.s=this.s.replaceAll("N","");
		Complex[] output=new Complex[(int)m1];
		Complex w=new Complex(Math.cos(2*Math.PI*u1/v1),Math.sin(2*Math.PI*u1/v1));
		for(int rt=0;rt<m1;rt++)
		{

			z=Raisedto(w,rt);
	        z=z.times(new Complex(a1,0));
			Complex last=new Complex(0,0);

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
					else finale=l;
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
				last=last.plus(convert(in.next()).times(this.Raisedto(z,-i)));
		 	}

		 	output[rt]=last;
		}

		return output;
	}

	private Complex Raisedto(Complex z1,long n)
	{
		Complex temp3 = new Complex(0,0);
		if(z1.Equals(temp3))
			return z1;
		else if(n==0)
			return new Complex(1,0);
		else if(n<0)
		{
			Complex temp=new Complex(0,0);
			temp=z1.reciprocal();
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
			Complex result=z1;
			for(long j=1;j<n;j++)
			{
				result=result.times(z1);
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
        else return new Complex(Double.parseDouble(s),0);
	}
}