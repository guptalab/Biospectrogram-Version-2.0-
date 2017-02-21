/*******************************************************************************
 *	This file takes care of the fetching module of this application.
 *	It download the fasta and genbank file from the NCBI server based on
 *	ths accession number provided by the user.
 *
 *	This code is written by Nilay Chheda @ daiict
 *	This code was last modified on 8th of May, 2013
 *
 *******************************************************************************/

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;

public class seqDownload
{

 	// constructor
    public seqDownload(String s,boolean flag2)
	{
		// creating a directory for storing the downloaded files
        accession = s;
		boolean flag = true;

        try
		{
        	/*******************************************************************************
        	 *	creating URL for fetching by appending accession number
        	 *	Malformed URL Exception can occur here
			 *******************************************************************************/
            fastaUrl = new URL(defaultUrlFasta+s);
			genBankUrl = new URL(defaultUrlGenBank+s);

            /*******************************************************************************
             *	fetching file from  URL
             *	IOException can occur here
			 *******************************************************************************/


            fetchGenBank(genBankUrl,createGenBank ());
			fetchFasta(fastaUrl,createFastaFile ());
		}
        catch(MalformedURLException mfue)
		{
        	mainApp.errorHandler(mfue);

            flag = false;
            if(flag2)
            {
            	JOptionPane.showMessageDialog(null,"Something went wrong :( ");
            }
		}
        catch (IOException ioe)
		{
			flag = false;
			if(flag2)
			{
				JOptionPane.showMessageDialog (null,"Accession Number is incorrect OR NCBI server is not responding !");
			}

		}

		if (flag)
        {
			if (validateAccessionNumber())
            {
            	if(flag2)
            	{
				//JOptionPane.showMessageDialog (null,"Fasta and GenBank file has been downloaded successfully !");
				JOptionPane.showMessageDialog(null,"Fasta file downloaded successfully and saved as History/Fetched/"+fastaFile.getName());
            	JOptionPane.showMessageDialog(null,"GenBank file downloaded successfully and saved as History/Fetched/"+genBankFile.getName());

            	}
			}
            else
			{
				if(flag2)
				{
					JOptionPane.showMessageDialog (null,"Accession Number is incorrect !");
				}

				// delete fasta and genbank file whose accession number is incorrect
				if (fastaFile != null)
                {
					fastaFile.delete ();
				}
                if (genBankFile != null)
				{
                	genBankFile.delete ();
				}
			}
		}
	}

	/*******************************************************************************
	 *	This method create fasta file with the given accession number
	 *	at the appropriate location of fetched file history
	 *******************************************************************************/

	private File createFastaFile()
    {
		String temp = accession;
		temp = temp.replaceAll("\\.","_");
		temp = temp.concat (".fasta");
		File filename = new File("../History/Fetched/"+temp);
		return filename;
	}

	/*******************************************************************************
	 *	This method create genbank file with the given accession number
	 *	at the appropriate location of fetched file history
	 *******************************************************************************/

     private File createGenBank()
     {
		String temp = accession;
		temp = temp.replaceAll("\\.","_");
		temp = temp.concat (".genbank");
        File filename = new File("../History/Fetched/"+temp);
		return filename;
	}

	/*******************************************************************************
	 *	This method download the fasta file from the internet based on the
	 *	accession number.
	 *******************************************************************************/

     private void fetchFasta(URL u,File f) throws FileNotFoundException,IOException
     {
        // creating input stream for fetching data from internet
		// creating output stream for storing fetched data on the local directory
		BufferedInputStream in = null;
        FileOutputStream fout = null;

		fastaFile = f;

		try
        {
			in = new BufferedInputStream(u.openStream());
            fout = new FileOutputStream(fastaFile);
			byte data[] = new byte[1024];
            int count;
			while ((count = in.read(data, 0, 1024)) != -1)
            {
				fout.write(data, 0, count);
			}
		}
		finally
        {
			if (in != null)
            {
				in.close();
			}
            if (fout != null)
			{
            	fout.close();
			}
		}
	}

	/*******************************************************************************
	 *	This method download the genbank file from the internet based on the
	 *	accession number.
	 *******************************************************************************/

     private void fetchGenBank(URL u,File f) throws FileNotFoundException,IOException
     {
        // creating input stream for fetching data from internet
		// creating output stream for storing fetched data on the local directory
		BufferedInputStream in = null;
		FileOutputStream fout = null;

		genBankFile = f;

        try
		{
        	in = new BufferedInputStream(u.openStream());
            fout = new FileOutputStream(genBankFile);

			byte data[] = new byte[1024];
            int count;	// count stores the number of bytes that have been read.

			// transfering data from input stream to output stream

			while ((count = in.read(data, 0, 1024)) != -1)
            {
				fout.write(data, 0, count);
			}
		}
        finally
		{
        	if (in != null)
            {
				in.close();
			}
			if (fout != null)
            {
				fout.close();

			}
		}
	}

	/*******************************************************************************
	 *	This method checks if accession number is invalid by reading the
	 *	first line of the downloaded fasta file with the accession number
	 *	provided even if the accession number is incorrect. In case of
	 *	invallid accession number first line will have message
	 *	"Nothing has been found"
	 *******************************************************************************/

     private boolean validateAccessionNumber()
     {
		String content = null;
        try
		{
        	FileInputStream in = new FileInputStream(fastaFile);
            DataInputStream data = new DataInputStream(in);
			BufferedReader  buffer = new BufferedReader(new InputStreamReader(in));

            buffer.readLine ();           // ignore the first empty line in the file
			content = buffer.readLine ();
            in.close ();
		}
        catch(FileNotFoundException fnfe)
		{
            mainApp.errorHandler(fnfe);
			return false;
		}
        catch(IOException ioe)
		{
            mainApp.errorHandler(ioe);
			return false;
		}
        if ( content.compareTo("Nothing has been found") == 0 )
		{
        	return false;
		}
		else
        {
			return true;
		}
	}

    private String accession;
	private String defaultUrlFasta = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=protein&rettype=fasta&retmode=text&id=";
    private String defaultUrlGenBank = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=protein&rettype=gb&retmode=text&id=";
	private URL fastaUrl;
    private URL genBankUrl;
	private File fastaFile;
    private File genBankFile;
}
