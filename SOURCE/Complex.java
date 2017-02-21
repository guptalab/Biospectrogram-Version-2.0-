/*******************************************************************************
 *	This code creates data type for complex numbers.
 *	http://algs4.cs.princeton.edu/99scientific/Complex.java
 *	I modified it according to need
 *
 *	The data type is "immutable" so once you create and initialize
 *	a Complex object, you cannot change it. The "final" keyword
 *	when declaring re and im enforces this rule, making it a
 *	compile-time error to change the .re or .im fields after
 *	they've been initialized.
 *
 *	% java Complex
 *	a            = 5.0 + 6.0i
 *	b            = -3.0 + 4.0i
 *	Re(a)        = 5.0
 *	Im(a)        = 6.0
 *  b + a        = 2.0 + 10.0i
 *  a - b        = 8.0 + 2.0i
 *  a * b        = -39.0 + 2.0i
 *  b * a        = -39.0 + 2.0i
 *  a / b        = 0.36 - 1.52i
 *	(a / b) * b  = 5.0 + 6.0i
 *  conj(a)      = 5.0 - 6.0i
 *  |a|          = 7.810249675906654
 *  tan(a)       = -6.685231390246571E-6 + 1.0000103108981198i
 *
 *	This code is written by Naman Turakhia @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.text.DecimalFormat;

public class Complex
{
	private final double re;   // the real part
    private final double im;   // the imaginary part

	// create a new object with the given real and imaginary parts
    public Complex(double real, double imag)
	{
    	re = real;
        im = imag;
	}

	// return a string representation of the invoking Complex object
    public String toString()
	{
		double tempReal = roundToDecimals(re);
        double tempIm = roundToDecimals(im);
		if (tempIm <  0)
        {
			return tempReal+"-"+(-tempIm)+"i\n";
		}
		else
		{
			return tempReal+"+"+tempIm+"i\n";
		}
	}

	// This method rounds the double value to 5 decimal places
	protected double roundToDecimals(double d)
	{
		DecimalFormat twoDForm = new DecimalFormat("#.#####");
        return Double.valueOf(twoDForm.format(d));
	}

    // return abs/modulus/magnitude and angle/phase/argument
	protected double abs()
    {
		return Math.hypot(re, im);
	}  // Math.sqrt(re*re + im*im)

    protected double phase()
	{
    	return Math.atan2(im, re);
	}  // between -pi and pi

    // return a new Complex object whose value is (this + b)
	protected Complex plus(Complex b)
    {
		Complex a = this;             // invoking object
        double real = a.re + b.re;
		double imag = a.im + b.im;
        return new Complex(real, imag);
	}

    // return a new Complex object whose value is (this - b)
	protected Complex minus(Complex b)
    {
		Complex a = this;
        double real = a.re - b.re;
		double imag = a.im - b.im;
        return new Complex(real, imag);
	}

    // return a new Complex object whose value is (this * b)
	protected Complex times(Complex b)
    {
		Complex a = this;
        double real = a.re * b.re - a.im * b.im;
		double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
	}

    // scalar multiplication
	// return a new object whose value is (this * alpha)
    protected Complex times(double alpha)
	{
    	return new Complex(alpha * re, alpha * im);
	}

    // return a new Complex object whose value is the conjugate of this
	protected Complex conjugate()
    {
		return new Complex(re, -im);
	}

    // return a new Complex object whose value is the reciprocal of this
	protected Complex reciprocal()
    {
		double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
	}

    // return the real or imaginary part
	protected double re()
    {
		return re;
	}

    protected double im()
    {
		return im;
	}

	// return a / b
    protected Complex divides(Complex b)
	{
    	Complex a = this;
        return a.times(b.reciprocal());
	}

    // return a new Complex object whose value is the complex exponential of this
	protected Complex exp()
    {
		return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
	}

    // return a new Complex object whose value is the complex sine of this
	protected Complex sin()
	{
    	return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
	}

	// return a new Complex object whose value is the complex cosine of this
    protected Complex cos()
	{
    	return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
	}

	// return a new Complex object whose value is the complex tangent of this
    protected Complex tan()
    {
		return sin().divides(cos());
	}

    // returns true if complex number is equal
	protected boolean Equals(Complex b)
    {
		if(this.re==b.re && this.im==b.im)
			return true;
        else
        	return false;
	}

    // a static version of plus
	protected static Complex plus(Complex a, Complex b)
    {
		double real = a.re + b.re;
        double imag = a.im + b.im;
		Complex sum = new Complex(real, imag);
        return sum;
	}

	protected boolean IsReal()
	{
		if(this.im==0)
			return true;
		else
			return false;
	}
}