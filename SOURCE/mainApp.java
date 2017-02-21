/******************************************************************************
 *	This code is the heart of entire application. It integrates all the
 *	functionality.
 *
 *	This code, named mainApp.java:
 *  1) Generates GUI for the users
 *	2) Create user specified encoding and/or transformation objects
 *  3) Puts validity checks of inputs for respective encodings and
 *	   transformations
 *	4) Convert inputs in appropriate format before giving for encoding
 *	   or transformation
 *	5) Calls appropriate encode and transform methods.
 *
 *	This code is written by Nilay Chheda @ daiict and Naman Turakhia @ daiict
 *	This code was last modified on 18th May, 2013
 *
*******************************************************************************/


import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.net.*;
import javax.swing.filechooser.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.SwingConstants;
import java.util.Date;
import java.text.SimpleDateFormat;

public class mainApp
{
	// Fields
	private static boolean updateFlag=true;
	private static boolean switchFlag = true;
	private static Thread t1=new Thread();
	private static Thread update1=new Thread();
	private static Thread fetch1=new Thread();
	private static Thread encode1=new Thread();
	private static Thread transform1=new Thread();
	private static Thread swindow1=new Thread();
	private static Thread import1=new Thread();

	protected static fileHistory fetchedHistory = new fileHistory(50,1);
    protected static fileHistory encodedHistory = new fileHistory(50,2);
	protected static fileHistory transformedHistory = new fileHistory(50,3);
	protected static fileHistory proteinHistory = new fileHistory(50,4);

    private static int maxScreenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getHeight();
	private static int maxScreenWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getWidth();

    private static int fetchFlag = 0;
	private static int encodingFlag = 0;
    private static int transformationFlag = 0;

	private static double actualRAM=0;

    private static double plotDelay = 0.2;

    private static boolean slideWindow = true;
    private static boolean stagnantWindow = true;
    private static boolean genePrediction = true;
    private static boolean licenseAccepted = false;    
    private static boolean isRAMSet = true;

    private static String nextVersion = "Encodings and transforms for the Genbank files will be supported in the next version.";
	private static String prevtoptext="";
	private static String prevbottomtext="";

    private static File wencodedname;
    private static File config = new File("../config");

    private static File[] slidewindowplot;

    private static String osName= System.getProperty("os.name");

	// Fields declared below corrosponds to GUI elements
    private static JFrame app;

    private static JToolBar toolbar;

	private static JTextArea topLabel;
	private static JTextArea bottomLabel;

	private static JScrollPane topPane;
    private static JScrollPane bottomPane;
    private static JScrollPane menuPane;

	private static JSplitPane pane;

	// Different buttons for different functionalities
	private static JButton switchButton;
	private static JButton backButton;
	private static JButton homeButton;
    private static JButton displayButton;
    private static JButton importButton;
    private static JButton fetchButton;
	private static JButton manSeqButton;
	private static JButton encodeButton;
    private static JButton transformButton;
	private static JButton windowButton;
    private static JButton updateButton;
	private static JButton netButton;
    private static JButton prefButton;
	private static JButton exportButton;
	private static JButton clearHistoryButton;
	private static JButton orfButton;
	private static JButton geneButton;

    private static JMenu menuHelp;
	private static JMenu menuTools;

	// Menu Items for Tools Menu
	private static JMenuItem switch2;
	private static JMenuItem back;
	private static JMenuItem home;
    private static JMenuItem display;
    private static JMenuItem import2;
    private static JMenuItem fetch;
    private static JMenuItem manSeq;
	private static JMenuItem encode;
    private static JMenuItem transform;
    private static JMenuItem window;
    private static JMenuItem update;
    private static JMenuItem net;
    private static JMenuItem pref;
    private static JMenuItem export;
	private static JMenuItem clearHistory;
	private static JMenuItem orf;
	private static JMenuItem gene;

	private static JMenuItem exit;

	// Menu Items for Help Menu
	private static JMenuItem manual;
	private static JMenuItem softUpdate;
    private static JMenuItem convention;
    private static JMenuItem demo;
    private static JMenuItem feedback;
    private static JMenuItem about;

	private static JMenuBar mb;

    // constructor
	public mainApp()
    {
		// Nothing here !
	}

    // main
	public static void main (String[] args)
    {

    	initialize();	// Initialize the application

		SwingUtilities.invokeLater (new Runnable()
		{
	        public void run()
			{
				createAndShowGUI();
			}
		});
    }

	/*******************************************************************************
	 *	This method initializes basic settings of the application. It also shows
	 *	one time license agreement. Once the user accepts it, this method save
	 *	that event in config file.
	 *
	 *	Another important work it does is to create directories for different
	 *	history files and initilizes all the gui components	with the native
	 *	look and feel.
	*******************************************************************************/

	private static void initialize()
	{		
		// Setting Look and Feel of application similar to native OS.
		try
        {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			// use -> getCrossPlatformLookAndFeelClassName() to get platform independent look and feel
		}
        catch (UnsupportedLookAndFeelException ulfe)
		{
			errorHandler(ulfe);
		}
        catch (ClassNotFoundException cnfe)
		{
			errorHandler(cnfe);
		}
        catch (InstantiationException ie)
		{
            errorHandler(ie);
		}
        catch (IllegalAccessException iae)
		{
			errorHandler(iae);
		}

		try
		{
			// Config file is included with all values 0

			FileReader frConfig = new FileReader(config);
			BufferedReader brConfig = new BufferedReader(frConfig);

			String cmp;

			while((cmp = brConfig.readLine()) != null)
			{
				if (cmp.contains("License:1"))
				{
					licenseAccepted = true;
				}
			}
			brConfig.close();
		}
		catch(IOException ioe)
		{
			errorHandler(ioe);
		}

		if (!licenseAccepted)
		{
			JFrame license = new JFrame("License");
			JLabel licenseText = new JLabel("Sample License");
			JScrollPane pane = new JScrollPane(licenseText);
			JPanel buttons = new JPanel();

			final JButton accept = new JButton("Accept");
			final JButton reject = new JButton("Reject");

			try
			{
				FileReader fr = new FileReader(new File("../Help/License.txt"));
				BufferedReader br = new BufferedReader(fr);
				String temp,text = "<html><br/><br/><br/>";
				// Write File content in JLabel
				while((temp = br.readLine()) != null)
				{
					text = text.concat("&nbsp&nbsp&nbsp&nbsp"+temp+"<br/>");
				}
				text = text.concat("<br/><br/></html>");
				licenseText.setText(text);
				fr.close();
			}
			catch(FileNotFoundException fnfe)
			{
				errorHandler(fnfe);
			}
			catch(IOException ioe)
			{
				errorHandler(ioe);
			}

			buttons.setLayout(new GridLayout(1,2));
			buttons.add(accept);
			buttons.add(reject);

			pane.getViewport().setBackground(Color.white);

			license.getContentPane().add(pane,BorderLayout.CENTER);
        	license.getContentPane().add(buttons,BorderLayout.PAGE_END);

			license.setBounds(maxScreenWidth/4,maxScreenHeight/4,maxScreenWidth/2,maxScreenHeight/2);
			license.setResizable(false);
			license.setVisible(true);

			accept.addActionListener(new ActionListener()
			{
		  		public void actionPerformed(ActionEvent evt)
				{
					/*******************************************************************************
    				*	Following  four statements creates the directory in the program
    				*	directory for managing different files
					*******************************************************************************/

					fileHistory.dir.mkdir();
    				fileHistory.subdir1.mkdir();
					fileHistory.subdir2.mkdir();
					fileHistory.subdir3.mkdir();
					fileHistory.subdir4.mkdir();
					fileHistory.subdir5.mkdir();
					fileHistory.subdir6.mkdir();
					fileHistory.subdir7.mkdir();
					fileHistory.subdir8.mkdir();
					fileHistory.subdir9.mkdir();
					fileHistory.subdir10.mkdir();
					fileHistory.subdir11.mkdir();
					fileHistory.subdir12.mkdir();
					fileHistory.subdir13.mkdir();
					fileHistory.subdir14.mkdir();

					JOptionPane.showMessageDialog(null,"Thank you for accepting the license.");

					accept.setEnabled(false);

					try
					{
						FileWriter fwConfig = new FileWriter(config);
						fwConfig.write("License:1\n");
						fwConfig.write("RAM:0");						
						fwConfig.close();

						licenseAccepted = true;
					}
					catch(IOException ioe)
					{
						errorHandler(ioe);
					}
  				}
			});

			reject.addActionListener(new ActionListener()
			{
		  		public void actionPerformed(ActionEvent evt)
				{
					int c = JOptionPane.showConfirmDialog(null,"Are you sure ?","",JOptionPane.YES_NO_OPTION);
					if (c == 0)
					{
						System.exit(0);
					}
  				}
			});

			while (accept.isEnabled()) // Make sure that user has accepted the license.
			{
				try
				{
					Thread.sleep(500);
				}
				catch(InterruptedException ie)
				{
					errorHandler(ie);
				}
			}
			license.dispose();
		}

		try
		{
			//Update Config file
			FileReader frConfig = new FileReader(config);
			BufferedReader brConfig = new BufferedReader(frConfig);

			String cmp = "";
			while((cmp = brConfig.readLine()) != null)
			{
				if (cmp.contains("RAM:0"))
				{
					isRAMSet = false;
					break;
				}
			}
			frConfig.close();

			if (!isRAMSet)
			{
				String ram = JOptionPane.showInputDialog(app,"Please enter total RAM available in your system (in GB) :");
				actualRAM = Double.parseDouble(ram);

				FileWriter fwConfig = new FileWriter(config);
				if (licenseAccepted)
					fwConfig.write("License:1\n");
				else
					fwConfig.write("License:0\n");
				fwConfig.write("RAM:"+actualRAM);
				fwConfig.close();
			}
			else
			{
				// Extract actual RAM and write into global variable.
				frConfig = new FileReader(config);
				brConfig = new BufferedReader(frConfig);
				String ram;

				while((ram = brConfig.readLine()) != null)
				{
					if (ram.contains("RAM"))
					{
						actualRAM = Double.parseDouble(ram.substring(4));
					}
				}
				brConfig.close();
			}
		}
		catch(NumberFormatException nfe)
		{
			JOptionPane.showMessageDialog(app,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
			System.exit(0);
		}
		catch(IOException ioe)
		{
			errorHandler(ioe);
		}

		/*******************************************************************************
		 *	Code written below initialize the major GUI elements used in the application
		 *	with appropriate properties and values.
		*******************************************************************************/

		toolbar = new JToolBar();

		topLabel=new JTextArea();
		bottomLabel=new JTextArea();

		topPane = new JScrollPane(topLabel);
    	bottomPane = new JScrollPane(bottomLabel);
    	menuPane = new JScrollPane(toolbar);

		pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,topPane,bottomPane);

		// Different buttons for different functionalities
		switchButton = new JButton(" Switch2AA ",new ImageIcon("../Icons/switch1.png"));
		backButton=new JButton("    Back    ",new ImageIcon("../Icons/back.png"));
		homeButton=new JButton("    Home    ",new ImageIcon("../Icons/home.png"));
    	displayButton=new JButton("   Display  ",new ImageIcon("../Icons/display.png"));
    	importButton = new JButton("   Import   ",new ImageIcon("../Icons/upload.png"));
    	fetchButton = new JButton("    Fetch   ", new ImageIcon("../Icons/fetch.png"));
		manSeqButton = new JButton("Manual Seq. ",new ImageIcon("../Icons/subseq.png"));
		encodeButton = new JButton("   Encode   ", new ImageIcon("../Icons/encode.png"));
    	transformButton = new JButton("  Transform ",new ImageIcon("../Icons/transform.png"));
		windowButton = new JButton("   Window   ",new ImageIcon("../Icons/window.png"));
    	updateButton = new JButton("NCBI updates",new ImageIcon("../Icons/update.png"));
		netButton = new JButton("  Internet  ",new ImageIcon("../Icons/internet.png"));
    	prefButton = new JButton("Preferences ",new ImageIcon("../Icons/pref.png"));
		exportButton = new JButton("   Export   ",new ImageIcon("../Icons/plot.png"));
		clearHistoryButton=new JButton("    Clear   ",new ImageIcon("../Icons/clear.png"));
		orfButton=new JButton("ORFinder",new ImageIcon("../Icons/orf.png"));
		geneButton=new JButton("ProteinGen",new ImageIcon("../Icons/toGene.png"));

    	menuHelp  = new JMenu("Help");
		menuTools  = new JMenu("Tools");

		// Menu Items for Tools Menu
		switch2 = new JMenuItem("Switch2AA",new ImageIcon("../Icons/menu_switch1.png"));
		back = new JMenuItem("Back",new ImageIcon("../Icons/menu_back.png"));
		home = new JMenuItem("Home",new ImageIcon("../Icons/menu_home.png"));
    	display = new JMenuItem("Display",new ImageIcon("../Icons/menu_display.png"));
	    import2 = new JMenuItem("Import FASTA",new ImageIcon("../Icons/menu_upload.png"));
	    fetch = new JMenuItem("Fetch",new ImageIcon("../Icons/menu_fetch.png"));
	    manSeq = new JMenuItem("Manual Seq.",new ImageIcon("../Icons/menu_subseq.png"));
		encode = new JMenuItem("Encode",new ImageIcon("../Icons/menu_encode.png"));
    	transform  = new JMenuItem("Transform",new ImageIcon("../Icons/menu_transform.png"));
    	window = new JMenuItem("Window",new ImageIcon("../Icons/menu_window.png"));
    	update = new JMenuItem("NCBI Updates",new ImageIcon("../Icons/menu_update.png"));
    	net = new JMenuItem("Check Internet",new ImageIcon("../Icons/menu_internet.png"));
    	pref = new JMenuItem("Preferences",new ImageIcon("../Icons/menu_pref.png"));
    	export = new JMenuItem("Export to Matlab",new ImageIcon("../Icons/menu_plot.png"));
		clearHistory = new JMenuItem("Clear History",new ImageIcon("../Icons/menu_clear.png"));
		orf = new JMenuItem("ORFinder",new ImageIcon("../Icons/menu_orf.png"));
		gene = new JMenuItem("ProteneGenerator",new ImageIcon("../Icons/menu_toGene.png"));

		exit = new JMenuItem("Exit",new ImageIcon("../Icons/menu_exit.png"));

		// Menu Items for Help Menu
		manual  = new JMenuItem("User Manual",new ImageIcon("../Icons/menu_documentation.png"));
		softUpdate = new JMenuItem("Software Update",new ImageIcon("../Icons/menu_softupdate.png"));
    	convention = new JMenuItem("Naming Conventions",new ImageIcon("../Icons/menu_codes.png"));
    	demo = new JMenuItem("Product Demo",new ImageIcon("../Icons/menu_video.png"));
    	feedback = new JMenuItem("Feedback",new ImageIcon("../Icons/menu_feedback.png"));
    	about = new JMenuItem("About",new ImageIcon("../Icons/menu_about.png"));

		mb  = new JMenuBar();
	}

	/*******************************************************************************
	 *	This method deletes all the files in all directories and clears history
	 *	directory from user's system.
	*******************************************************************************/
	private static boolean deleteDir(File dir)
	{
     	if (dir.isDirectory())
		{
        	String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
			{
				File fs=new File(dir, children[i]);
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success)
				{
                	return false;
				}
			}
		}
		boolean d=dir.delete();
		return d;
	}

	protected static void errorHandler(Exception e)
	{
		try
		{
			FileWriter fw = new FileWriter(new File("../Help/errors.log"),true);

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			String formattedDate = sdf.format(date);
			fw.write(formattedDate);
			fw.write("\n");
			fw.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			fw.write("\n");

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			fw.write(errors.toString());

			fw.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			fw.write("\n\n\n");

			fw.close();

			JOptionPane.showMessageDialog(null,"Something went wrong :(");
		}
		catch(IOException eda)
		{
		}
	}

	/*******************************************************************************
	 *	This method takes care of all the GUI part of entire application.
	 *	All UI related components are created and managed by this method
	*******************************************************************************/
    private static void createAndShowGUI()
	{

        app = new JFrame("BioSpectrogram");

		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setBounds(maxScreenWidth/4,maxScreenHeight/4,maxScreenWidth/2+295,maxScreenHeight/2);
        app.setIconImage(Toolkit.getDefaultToolkit().getImage("../Icons/logo.png"));
		app.setExtendedState(JFrame.MAXIMIZED_BOTH);
		app.setVisible(true);

		switchButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        switchButton.setHorizontalTextPosition(SwingConstants.CENTER);
        switchButton.setMaximumSize(new Dimension(90,60));
        switchButton.setMinimumSize(new Dimension(90,60));

		backButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        backButton.setHorizontalTextPosition(SwingConstants.CENTER);
        backButton.setMaximumSize(new Dimension(90,60));
        backButton.setMinimumSize(new Dimension(90,60));

      	homeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        homeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        homeButton.setMaximumSize(new Dimension(90,60));
        homeButton.setMinimumSize(new Dimension(90,60));

		displayButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        displayButton.setHorizontalTextPosition(SwingConstants.CENTER);
        displayButton.setMaximumSize(new Dimension(90,60));
        displayButton.setMinimumSize(new Dimension(90,60));

		importButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        importButton.setHorizontalTextPosition(SwingConstants.CENTER);
        importButton.setMaximumSize(new Dimension(90,60));
        importButton.setMinimumSize(new Dimension(90,60));

        fetchButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        fetchButton.setHorizontalTextPosition(SwingConstants.CENTER);
        fetchButton.setMaximumSize(new Dimension(90,60));
        fetchButton.setMinimumSize(new Dimension(90,60));

        manSeqButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        manSeqButton.setHorizontalTextPosition(SwingConstants.CENTER);
        manSeqButton.setMaximumSize(new Dimension(90,60));
        manSeqButton.setMinimumSize(new Dimension(90,60));

        encodeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        encodeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        encodeButton.setMaximumSize(new Dimension(90,60));
        encodeButton.setMinimumSize(new Dimension(90,60));

        transformButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        transformButton.setHorizontalTextPosition(SwingConstants.CENTER);
        transformButton.setMaximumSize(new Dimension(90,60));
        transformButton.setMinimumSize(new Dimension(90,60));

        windowButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        windowButton.setHorizontalTextPosition(SwingConstants.CENTER);
        windowButton.setMaximumSize(new Dimension(90,60));
        windowButton.setMinimumSize(new Dimension(90,60));

        updateButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        updateButton.setHorizontalTextPosition(SwingConstants.CENTER);
        updateButton.setMaximumSize(new Dimension(90,60));
        updateButton.setMinimumSize(new Dimension(90,60));

        netButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        netButton.setHorizontalTextPosition(SwingConstants.CENTER);
        netButton.setMaximumSize(new Dimension(90,60));
        netButton.setMinimumSize(new Dimension(90,60));

        prefButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        prefButton.setHorizontalTextPosition(SwingConstants.CENTER);
        prefButton.setMaximumSize(new Dimension(90,60));
        prefButton.setMinimumSize(new Dimension(90,60));

        exportButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        exportButton.setHorizontalTextPosition(SwingConstants.CENTER);
        exportButton.setMaximumSize(new Dimension(90,60));
        exportButton.setMinimumSize(new Dimension(90,60));

        clearHistoryButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        clearHistoryButton.setHorizontalTextPosition(SwingConstants.CENTER);
        clearHistoryButton.setMaximumSize(new Dimension(90,60));
        clearHistoryButton.setMinimumSize(new Dimension(90,60));

        orfButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        orfButton.setHorizontalTextPosition(SwingConstants.CENTER);
        orfButton.setMaximumSize(new Dimension(90,60));
        orfButton.setMinimumSize(new Dimension(90,60));

        geneButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        geneButton.setHorizontalTextPosition(SwingConstants.CENTER);
        geneButton.setMaximumSize(new Dimension(90,60));
        geneButton.setMinimumSize(new Dimension(90,60));

		switchButton.setFocusable(false);
		backButton.setFocusable(false);
		homeButton.setFocusable(false);
		displayButton.setFocusable(false);
		importButton.setFocusable(false);
        fetchButton.setFocusable(false);
        manSeqButton.setFocusable(false);
		encodeButton.setFocusable(false);
        transformButton.setFocusable(false);
		windowButton.setFocusable(false);
        updateButton.setFocusable(false);
		netButton.setFocusable(false);
        prefButton.setFocusable(false);
		exportButton.setFocusable(false);
		clearHistoryButton.setFocusable(false);
		orfButton.setFocusable(false);
		geneButton.setFocusable(false);

		switchButton.setToolTipText("<html><center>Switch to Protein Mode</center><html>");
		homeButton.setToolTipText("<html><center>Home</center><html>");
		backButton.setToolTipText("<html><center>Back</center><html>");
		displayButton.setToolTipText("<html><center>Display fetched sequence from History</center><html>");
		importButton.setToolTipText("<html><center>Import FASTA file to fetched history</center><html>");
        fetchButton.setToolTipText("<html><center>Fetch the sequence from NCBI</center><html>");
        manSeqButton.setToolTipText("<html><center>Manually enter the sequence</center><html>");
		encodeButton.setToolTipText("<html><center>Encode the fetched sequence</center><html>");
        transformButton.setToolTipText("<html><center>Transform the encoded sequence</center><html>");
		windowButton.setToolTipText("<html><center>Do different window analysis</center><html>");
        updateButton.setToolTipText("<html><center>Update files in fetched history from NCBI</center><html>");
		netButton.setToolTipText("<html><center>Check innternet connection</center><html>");
        prefButton.setToolTipText("<html><center>Preferences</center><html>");
		exportButton.setToolTipText("<html><center>Export to MATLAB</center><html>");
		clearHistoryButton.setToolTipText("<html><center>Clear all history</center><html>");
		orfButton.setToolTipText("<html><center>Six Frame ORF Finder</center><html>");
		geneButton.setToolTipText("<html><center>Protein Generator using standard Genetic Code</center><html>");

		toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(switchButton);
		toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(backButton);
		toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(homeButton);
        toolbar.addSeparator(new Dimension(10,60));
        toolbar.add(displayButton);
		toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(importButton);
        toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(fetchButton);
        toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(manSeqButton);
        toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(encodeButton);
        toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(transformButton);
        toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(windowButton);
        toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(updateButton);
        toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(netButton);
        toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(prefButton);
        toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(exportButton);
        toolbar.addSeparator(new Dimension(10,60));
		toolbar.add(clearHistoryButton);
        toolbar.addSeparator(new Dimension(10,60));
        toolbar.add(orfButton);
        toolbar.addSeparator(new Dimension(10,60));
        toolbar.add(geneButton);
        toolbar.addSeparator(new Dimension(10,60));

		toolbar.setBackground (Color.black);

		/*******************************************************************************
		 *	18 seperator*10 + 17 buttons*90 = 1710
		*******************************************************************************/

		toolbar.setPreferredSize(new Dimension(1710,60));

		toolbar.setFloatable(false);

      	topLabel.setFont(new Font("Monospaced",Font.BOLD,30));
        bottomLabel.setFont(new Font("Monospaced",Font.BOLD,30));
		topLabel.setEditable(false);
		bottomLabel.setEditable(false);

		backButton.setEnabled(false);
		back.setEnabled(false);

		/*******************************************************************************
		 *	Here getSequence(0,1) means that index 0 and flag 1
		 *	Index 0 refer to top element in the array which is added latest
		 *	Flag 1 means that we are looking into fetched history's array
		*******************************************************************************/
		topLabel.setText (getSequence (0,1).toString());
		topLabel.updateUI();
		bottomLabel.setText(" ");
		bottomLabel.updateUI();

        pane.setDividerLocation (app.getSize().height/2);
		pane.setDividerSize(5);
        pane.setBorder(new BevelBorder(BevelBorder.LOWERED));

		topPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		topPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		topPane.setBorder (new LineBorder(Color.BLACK,2,true));
        topPane.getViewport().setBackground (Color.WHITE);

        bottomPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		bottomPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		bottomPane.setBorder(new LineBorder(Color.BLACK,2,true));
        bottomPane.getViewport().setBackground (Color.WHITE);

		/*******************************************************************************
		 *	18 seperator*10 + 17 buttons*90 = 1710
		*******************************************************************************/
		menuPane.setPreferredSize(new Dimension(1710,100));

        menuPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        menuPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		app.getContentPane().add(pane,BorderLayout.CENTER);
        app.getContentPane().add(menuPane,BorderLayout.PAGE_START);

		menuHelp.setMnemonic(KeyEvent.VK_F);
        menuTools.setMnemonic(KeyEvent.VK_T);

		// Tools Menu
		menuTools.add(switch2);
		menuTools.add(back);
		menuTools.add(home);
		menuTools.add(display);
		menuTools.add(import2);
		menuTools.add(fetch);
		menuTools.add(manSeq);
		menuTools.add(encode);
		menuTools.add(transform);
		menuTools.add(window);
		menuTools.add(update);
		menuTools.add(net);
        menuTools.add(pref);
        menuTools.add(export);
        menuTools.add(clearHistory);
        menuTools.add(orf);
        menuTools.add(gene);

        menuTools.add(exit);

		// Help Menu
        menuHelp.add(manual);
		menuHelp.add(softUpdate);
		menuHelp.add(convention);
		menuHelp.add(demo);
		menuHelp.add(feedback);
        menuHelp.add(about);

		mb.add(menuTools);
    	mb.add(menuHelp);
		app.setJMenuBar(mb);

        /***************************************************************
         *	Action listeners for menu items of Tools Menu
		***************************************************************/
		switch2.addActionListener(new switchListener());
		home.addActionListener(new homeListener());
		back.addActionListener(new backListener());
		display.addActionListener(new displayListener());
		import2.addActionListener(new importListener());
		fetch.addActionListener(new fetchListener());
		manSeq.addActionListener(new manSeqListener());
		encode.addActionListener(new encodeListener());
        transform.addActionListener(new transformListener());
        window.addActionListener(new windowListener());
		update.addActionListener(new updateListener());
        net.addActionListener (new netListener());
        pref.addActionListener (new prefListener());
        export.addActionListener(new exportListener());
        clearHistory.addActionListener(new clearHistoryListener());
        orf.addActionListener(new orfListener());
        gene.addActionListener(new geneListener());

        exit.addActionListener(new exitListener());

		/*******************************************************************************
		 *	Action listeners for menu items of Help Menu
		*******************************************************************************/

		manual.addActionListener (new manualListener());
		softUpdate.addActionListener(new softUpdateListener());
		convention.addActionListener(new conventionListener());
		demo.addActionListener(new demoListener());
		feedback.addActionListener (new feedbackListener());
		about.addActionListener (new aboutListener());



		/*******************************************************************************
		 *	Action Listener for fetchButton is defined in seperate inner class called
		 *	fetchListener()
		 *	Need of the different class is because it needs to change the gui
		 *	in the main app object.
		 *	In the subsequence statements we have defined different action listeners
		 *	for different buttons. All this class are implemented at the end of this
		 *	file.
		*******************************************************************************/
		switchButton.addActionListener(new switchListener());
		homeButton.addActionListener(new homeListener());
		backButton.addActionListener(new backListener());
		displayButton.addActionListener(new displayListener());
		importButton.addActionListener(new importListener());
		fetchButton.addActionListener (new fetchListener());
		manSeqButton.addActionListener(new manSeqListener());
		encodeButton.addActionListener (new encodeListener());
        transformButton.addActionListener (new transformListener());
		windowButton.addActionListener (new windowListener());
		updateButton.addActionListener ( new updateListener());
		netButton.addActionListener ( new netListener());
		prefButton.addActionListener ( new prefListener());
		exportButton.addActionListener (new exportListener());
		clearHistoryButton.addActionListener(new clearHistoryListener());
		orfButton.addActionListener(new orfListener());
		geneButton.addActionListener(new geneListener());
	}


    /*******************************************************************************
     *	This method checks the internet connectivity of user.
     *	It will check to download test data from ncbi website itself
     *	This way it indicates if there are any problem in fetcing new data
	*******************************************************************************/
	protected static boolean checkInternet()
	{

    	// Defining and Initialising the objects

        BufferedInputStream in = null;
		FileOutputStream fout = null;
        byte data[] = new byte[1024];
		int count=0,temp=0;
        File  f = null;
		URL u = null;

		try
        {
			// url for test download
            //u = new URL("ftp://ftp.ncbi.nih.gov/README.ftp");
			u = new URL("http://www.ncbi.nlm.nih.gov/favicon.ico");
            f = new File("temp");
			in = new BufferedInputStream(u.openStream());
            fout = new FileOutputStream(f);

			// downloading the data
            while ((count = in.read(data, 0, 1024)) != -1)
			{
            	fout.write(data, 0, count);
                temp += count;
			}

            // closing the stream
			in.close();
            fout.close();

			if (temp==318)
            {
				/*******************************************************************************
				 *	To check internet connectivity, we are tryring to download an
				 *	object from the ncbi website itself, named "favicon.ico"
				 *	Then we compare the size of the downloaded file with "318" bytes
				 *	which is the precalculated size of the object which we have
				 *	assumed, is not going to change.
				 *	The file is deleted after check and redownloaded again on method call
				 *	This way, we are alsi making sure that the web server of ncbi
				 *	site, itself is also up.
				*******************************************************************************/

				f.delete();
                return true;
			}
            else
			{
            	f.delete();
                return false;
			}
		}

        catch(MalformedURLException mfue)
		{
            errorHandler(mfue);
			return false;
		}
        catch (UnknownHostException uhe)
		{
            errorHandler(uhe);
			return false;
		}
        catch (IOException ioe)
		{
            errorHandler(ioe);
			return false;
		}
		/*******************************************************************************
		 *	Here we have assumed even if there is any problem in connectivity on
		 *	user's computer or on server side
		 *	Download  size can't be excatly 1868 in any other case
		*******************************************************************************/
	}

	/*******************************************************************************
	 *	This method help to prevent multiple download of same file
	 *	It returns boolean value indicating if file, user want to downlod, is
	 *	already there in user's downloaded history.
	*******************************************************************************/
	private static boolean checkHistory(String number)
	{
    	number = number+".fasta";
    	fetchedHistory.updateFetchedFileList();
        String[] temp = fetchedHistory.getArray ();
		for(int i=0;i<fetchedHistory.getSize ();i++)
        {
			if ( temp[i].compareTo (number) == 0)
            {
                return true;
			}
		}
        return false;
	}


	/*******************************************************************************
	 *	This static method extracts the content of the fasta or genbank file to string
	 *	It is used in setText method for JLabels only.
	 *	Here index is the index of the file (fetched or encoded or transformed)
	 *	Flag has standard meanings:
	 *	1 for fetched files
	 *	2 for encoded files
	 *	3 for transformed files
	 *	4 for protein fetched files
	 *	NOTE: Implementation for genbank file needs to be implemented.
	*******************************************************************************/
	protected static StringBuffer getSequence(int index,int flag)
    {
		// Handling the fetched files
		if (flag == 1)
        {
        	fetchedHistory.updateFileList();
            if (fetchedHistory.getSize() > 0 )
			{
            	try
                {
					FileInputStream in = new FileInputStream(fetchedHistory.getElement(index));
					BufferedReader buffer = new BufferedReader(new InputStreamReader(in));

					/*
					String temp;
					while( (temp = buffer.readLine()) != null )
                    {
						readSeq = readSeq.concat(temp).concat("\n");
					}
                    in.close();
                    */

					long byteCount = in.available();
					long bufferCount;
					long totalCount;
					//System.out.println(" byte count in getsequence is : "+byteCount+"\n");
					if ( byteCount > 100*1024*1024 )
					{
						bufferCount = 100*1024*1024;
						totalCount = 2*(bufferCount+byteCount);
					}
					else
					{
						bufferCount = byteCount;
						totalCount = 2*(bufferCount+byteCount);
					}

					Runtime rt = Runtime.getRuntime();
					//rt.gc();
					long freeMem = rt.maxMemory() - (rt.totalMemory() - rt.freeMemory());

					if (2*totalCount > freeMem)
					{
						// Out of Memory Exception
						JOptionPane.showMessageDialog(app,"We are out of Heap Memory to display the file :(\n If you really want to look at the file, "+
							"please browse to the location "+fetchedHistory.getElement(index).getCanonicalPath()+" \nand open it using some text"+
								 " editor like Notepad++ for Windows or VI for Linux\\MAC\n"+
								 	"If software seems to be hung after pressing OK, minize & maximize :)");
						StringBuffer tempBuf = new StringBuffer(1);
						tempBuf.append(" ");
						return tempBuf;
					}
					else
					{
						try
						{
							StringBuffer fileData = new StringBuffer((int)byteCount);
							char[] buf = new char[(int)bufferCount];
							int numRead=0;
							while((numRead = buffer.read(buf)) != -1)
							{
								fileData.append(buf);
							}
							in.close();
							return fileData;
						}
						catch(OutOfMemoryError e)
						{
							in.close();
							//rt.gc();
							e.printStackTrace(System.out);
							JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
								"If software seems to be hung after pressing OK, minize & maximize :)");
							StringBuffer tempBuf = new StringBuffer(1);
							tempBuf.append(" ");
							return tempBuf;
						}
					}
				}
                catch(IOException ioe)
                {
                    errorHandler(ioe);
				}
			}
		}

		// Handling the encoded files
        if (flag == 2)
		{
        	encodedHistory.updateFileList ();
            if (encodedHistory.getSize () > 0 )
			{
            	try
                {
					FileInputStream in = new FileInputStream(encodedHistory.getElement(index));
					BufferedReader buffer = new BufferedReader(new InputStreamReader(in));

					/*
					String temp;
					while( (temp = buffer.readLine()) != null )
                    {
						readSeq = readSeq.concat(temp).concat("\n");
					}
                    in.close();
                    */

					long byteCount = in.available();
					long bufferCount;
					long totalCount;

					if ( byteCount > 100*1024*1024 )
					{
						bufferCount = 100*1024*1024;
						totalCount = 2*(bufferCount+byteCount);
					}
					else
					{
						bufferCount = byteCount;
						totalCount = 2*(bufferCount+byteCount);
					}

					Runtime rt = Runtime.getRuntime();
					//rt.gc();
					long freeMem = rt.maxMemory() - (rt.totalMemory() - rt.freeMemory());

					if (2*totalCount > freeMem)
					{
						// Out of Memory Exception
						JOptionPane.showMessageDialog(app,"We are out of Heap Memory to display the file :(\n If you really want to look at the file, "+
							"please browse to the location "+encodedHistory.getElement(index).getCanonicalPath()+" \nand open it using some text"+
								 " editor like Notepad++ for Windows or VI for Linux\\MAC\n"+
								 	"If software seems to be hung after pressing OK, minize & maximize :)");
						StringBuffer tempBuf = new StringBuffer(1);
						tempBuf.append(" ");
						return tempBuf;
					}
					else
					{
						try
						{
							StringBuffer fileData = new StringBuffer((int)byteCount);
							char[] buf = new char[(int)bufferCount];
							int numRead=0;
							while((numRead = buffer.read(buf)) != -1)
							{
								fileData.append(buf);
							}
							in.close();
							return fileData;
						}
						catch(OutOfMemoryError e)
						{
							in.close();
							//rt.gc();
							e.printStackTrace(System.out);
							JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
								"If software seems to be hung after pressing OK, minize & maximize :)");
							StringBuffer tempBuf = new StringBuffer(1);
							tempBuf.append(" ");
							return tempBuf;
						}
					}
				}
                catch(IOException ioe)
                {
                    errorHandler(ioe);
				}
			}
		}

        // handling the transformed files
        if (flag == 3)
		{
        	transformedHistory.updateFileList ();
            if (transformedHistory.getSize () > 0 )
			{
            	try
                {
					FileInputStream in = new FileInputStream(transformedHistory.getElement(index));
					BufferedReader buffer = new BufferedReader(new InputStreamReader(in));

					/*
					String temp;
					while( (temp = buffer.readLine()) != null )
                    {
						readSeq = readSeq.concat(temp).concat("\n");
					}
                    in.close();
                    */

					long byteCount = in.available();
					long bufferCount;
					long totalCount;

					if ( byteCount > 100*1024*1024 )
					{
						bufferCount = 100*1024*1024;
						totalCount = 2*(bufferCount+byteCount);
					}
					else
					{
						bufferCount = byteCount;
						totalCount = 2*(bufferCount+byteCount);
					}

					Runtime rt = Runtime.getRuntime();
					//rt.gc();
					long freeMem = rt.maxMemory() - (rt.totalMemory() - rt.freeMemory());

					if (2*totalCount > freeMem)
					{
						// Out of Memory Exception
						JOptionPane.showMessageDialog(app,"We are out of Heap Memory to display the file :(\n If you really want to look at the file, "+
							"please browse to the location "+transformedHistory.getElement(index).getCanonicalPath()+" \nand open it using some text"+
								 " editor like Notepad++ for Windows or VI for Linux\\MAC\n"+
								 	"If software seems to be hung after pressing OK, minize & maximize :)");
						StringBuffer tempBuf = new StringBuffer(1);
						tempBuf.append(" ");
						return tempBuf;
					}
					else
					{
						try
						{
							StringBuffer fileData = new StringBuffer((int)byteCount);
							char[] buf = new char[(int)bufferCount];
							int numRead=0;
							while((numRead = buffer.read(buf)) != -1)
							{
								fileData.append(buf);
							}
							in.close();
							return fileData;
						}
						catch(OutOfMemoryError e)
						{
							in.close();
							//rt.gc();
							e.printStackTrace(System.out);
							JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
								"If software seems to be hung after pressing OK, minize & maximize :)");
							StringBuffer tempBuf = new StringBuffer(1);
							tempBuf.append(" ");
							return tempBuf;
						}
					}
				}
                catch(IOException ioe)
                {
                    errorHandler(ioe);
				}
			}
		}

		// Handling the protein fetched files
		if (flag == 4)
        {
			proteinHistory.updateFileList();

            if (proteinHistory.getSize() > 0 )
			{
            	try
                {
					FileInputStream in = new FileInputStream(proteinHistory.getElement(index));
					BufferedReader buffer = new BufferedReader(new InputStreamReader(in));

					/*
					String temp;
					while( (temp = buffer.readLine()) != null )
                    {
						readSeq = readSeq.concat(temp).concat("\n");
					}
                    in.close();
                    */

					long byteCount = in.available();
					long bufferCount;
					long totalCount;

					if ( byteCount > 100*1024*1024 )
					{
						bufferCount = 100*1024*1024;
						totalCount = 2*(bufferCount+byteCount);
					}
					else
					{
						bufferCount = byteCount;
						totalCount = 2*(bufferCount+byteCount);
					}

					Runtime rt = Runtime.getRuntime();
					////rt.gc();
					long freeMem = rt.maxMemory() - (rt.totalMemory() - rt.freeMemory());

					if (2*totalCount > freeMem)
					{
						// Out of Memory Exception
						JOptionPane.showMessageDialog(app,"We are out of Heap Memory to display the file :(\n If you really want to look at the file, "+
							"please browse to the location "+proteinHistory.getElement(index).getCanonicalPath()+" \nand open it using some text"+
							" editor like Notepad++ for Windows or VI for Linux\\MAC\n"+
								 	"If software seems to be hung after pressing OK, minize & maximize :)");
						StringBuffer tempBuf = new StringBuffer(1);
						tempBuf.append(" ");
						return tempBuf;
					}
					else
					{
						try
						{
							StringBuffer fileData = new StringBuffer((int)byteCount);
							char[] buf = new char[(int)bufferCount];
							int numRead=0;
							while((numRead = buffer.read(buf)) != -1)
							{
								fileData.append(buf);
							}
							in.close();
							return fileData;
						}
						catch(OutOfMemoryError e)
						{
							in.close();
							//rt.gc();
							e.printStackTrace(System.out);
							JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
								"If software seems to be hung after pressing OK, minize & maximize :)");
							StringBuffer tempBuf = new StringBuffer(1);
							tempBuf.append(" ");
							return tempBuf;
						}
					}
				}
                catch(IOException ioe)
                {
                    errorHandler(ioe);
				}
			}
		}
		StringBuffer tempBuf = new StringBuffer(1);
		tempBuf.append(" ");
		return tempBuf;
	}


	/*******************************************************************************
	 *	This method is used to prepare appropriate input for
	 *	transformations and for windowing purpose as well
	 *	Here index takes the index of the file in its respective array
	 *	i.e. fetchedfilelist or encodedfilelist or transformedfilelist.
	 *	flags have standard meaning;
	 *	1 for fetched sequence
	 *	2 for encoded sequence
	 *	NOTE: Implementation for genbank file needs to be done if required.
	 *******************************************************************************/
	protected static StringBuffer getOnlySequence(int index,int flag)
	{
        if (flag == 1)
		{
        	fetchedHistory.updateFileList ();
            if (fetchedHistory.getSize () > 0 )
			{
            	try
                {
                    char character;
					int a,file=-1;
					String temp = null;

                    FileInputStream in = new FileInputStream(fetchedHistory.getElement(index));
					long byteCount = in.available();
					BufferedReader buffer = new BufferedReader(new InputStreamReader(in));

					/*******************************************************************************
					 *	This loop reads fetched file character by character and
					 *	writes it to string variable seq. It ignores new line
					 *	characters and this implementation is for FASTA file
					 *******************************************************************************/

					while((a = buffer.read()) !=  -1)
                    {
						character = (char) a;
                        if (character == '>')
						{
                        	String firstline=buffer.readLine();	// traverse the very first line and skip it.
                           // System.out.println("First line of the file is: "+firstline);
                            file = 0 ;          // indicates its a fasta file
							break;
						}
                        else
						{
                        	if (file == -1)
                            {
								file = 1;           // indicates its a genbank file
                                break;
							}
						}
                    }

                    if (file == 0) //fasta file
                    {
                    	/*
						while( (temp = buffer.readLine()) != null )
                    	{
							seq = seq.concat(temp);
						}
						*/

						long bufferCount;
						long totalCount;
						//System.out.println("byte count is: "+byteCount);
						if ( byteCount > 100*1024*1024 )
						{
							bufferCount = 100*1024*1024;
							totalCount = 2*(bufferCount+byteCount);
						}
						else
						{
							bufferCount = byteCount;
							totalCount = 2*(bufferCount+byteCount);
						}
						//System.out.println("before runtime\n");
						Runtime rt = Runtime.getRuntime();
						//rt.gc();
						long freeMem = rt.maxMemory() - (rt.totalMemory() - rt.freeMemory());

						if (2*totalCount > freeMem)
						{
							// Out of Memory Exception
							JOptionPane.showMessageDialog(app,"We are out of Heap Memory to process the file :(\n"+
								"File is located at the location "+fetchedHistory.getElement(index).getCanonicalPath()+
									" \nTry to rerun after allocating more RAM to the bio spectrogram. Refer to Readme File\n"+
								 	"If software seems to be hung after pressing OK, minize & maximize :)");
							StringBuffer tempBuf = new StringBuffer(1);
							tempBuf.append(" ");
							return tempBuf;
						}
						else
						{
							try
							{	//System.out.println("started reading data..\n");
								StringBuffer fileData = new StringBuffer((int)byteCount);
								char[] buf = new char[(int)bufferCount];
								int numRead=0;
								//System.out.println("byte: "+byteCount+" buffer: "+bufferCount);
								while((numRead = buffer.read(buf)) != -1)
								{
									fileData.append(buf);
								//	System.out.println(buf);
								}
								//System.out.println("done with it\n");
								in.close();
								return fileData;
							}
							catch(OutOfMemoryError e)
							{
								in.close();
								//rt.gc();
								e.printStackTrace(System.out);
								JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
									"If software seems to be hung after pressing OK, minize & maximize :)");
								StringBuffer tempBuf = new StringBuffer(1);
								tempBuf.append(" ");
								return tempBuf;
							}
						}
                    }

					// Genbank File
					// Please update below code segment to use larger buffer to read when
					// encoding of genbank is available.
					else if (file == 1)
                    {
                    	String seq = " ";
						FileInputStream in2 = new FileInputStream(fetchedHistory.getElement (index));
                        BufferedReader buffer2 = new BufferedReader(new InputStreamReader(in2));

						while( (temp = buffer2.readLine ()) != null )
                        {
							if (temp.compareTo("ORIGIN      ") == 0)
                            {
								// "Origin" found
                                break;
							}
						}

                        /*******************************************************************************
                         *	This loop reads fetched file character by character and
                         *	writes only ACGT to string variable seq.
                         *	This implementation is for GENBANK file.
						 *******************************************************************************/

                         while((a = buffer2.read ()) !=  -1)
                         {
							character = (char) a;
                            if (character == 'A' || character == 'a')
							{
                            	temp = ""+character;
                                seq = seq.concat(temp);
							}
                            else if (character == 'G' || character == 'g')
							{
                            	temp = ""+character;
                                seq = seq.concat(temp);
							}
                            else if (character == 'C' || character == 'c')
							{
                            	temp = ""+character;
                                seq = seq.concat(temp);
							}
                            else if (character == 'T' || character == 't')
							{
                            	temp = ""+character;
                                seq = seq.concat(temp);
							}
						}
                        in2.close ();
						file = -1;
					}
				}
                catch(IOException ioe)
				{
                    errorHandler(ioe);
				}
			}
		}

		// Implementation for getting sequence (from encoded files) for Transformed Files
		else if (flag == 2)
        {
			encodedHistory.updateFileList ();
            if (encodedHistory.getSize () > 0 )
			{
            	try
                {
                    char character;
					int a,file=-1;

                    FileInputStream in = new FileInputStream(encodedHistory.getElement(index));
					long byteCount = in.available();
					BufferedReader  buffer = new BufferedReader(new InputStreamReader(in));


                    while((a = buffer.read ()) !=  -1)
					{

                    	character = (char) a;

                        if (character == '>')
						{
                        	buffer.readLine ();
                            file = 0 ;                    // indicates its a fasta file
							break;
						}
                        else
						{
                        	if (file == -1)
                            {
								file = 1;           // indicates its a genbank file
                                break;
							}
						}
					}

					if (file == 0) //fasta file
					{
						/*
						while( (temp = buffer.readLine()) != null )
                    	{
							seq = seq.concat(temp);
						}
						*/

                    	long bufferCount;
						long totalCount;
						//System.out.println("byte count in getOnlySeq. flag 2 is: "+byteCount);
						if ( byteCount > 100*1024*1024 )
						{
							bufferCount = 100*1024*1024;
							totalCount = 2*(bufferCount+byteCount);
						}
						else
						{
							bufferCount = byteCount;
							totalCount = 2*(bufferCount+byteCount);
						}

						Runtime rt = Runtime.getRuntime();
						//rt.gc();
						long freeMem = rt.maxMemory() - (rt.totalMemory() - rt.freeMemory());

						if (2*totalCount > freeMem)
						{
							// Out of Memory Exception
							JOptionPane.showMessageDialog(app,"We are out of Heap Memory to process the file :(\n"+
								"File is located at the location "+fetchedHistory.getElement(index).getCanonicalPath()+
									" \nTry to rerun after allocating more RAM to the bio spectrogram. Refer to Readme File\n"+
								 	"If software seems to be hung after pressing OK, minize & maximize :)");
							StringBuffer tempBuf = new StringBuffer(1);
							tempBuf.append(" ");
							return tempBuf;
						}
						else
						{
							try
							{
								StringBuffer fileData = new StringBuffer((int)byteCount);
								char[] buf = new char[(int)bufferCount];
								int numRead=0;
								while((numRead = buffer.read(buf)) != -1)
								{
									fileData.append(buf);
								}
								in.close();
								return fileData;
							}
							catch(OutOfMemoryError e)
							{
								in.close();
								//rt.gc();
								e.printStackTrace(System.out);
								JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
									"If software seems to be hung after pressing OK, minize & maximize :)");
								StringBuffer tempBuf = new StringBuffer(1);
								tempBuf.append(" ");
								return tempBuf;
							}
						}
					}

					// Genbank File
					// Please update below code segment to use larger buffer to read when
					// encoding of genbank is available.
                    else if (file == 1)
					{
                    	//FileInputStream in2 = new FileInputStream(fetchedHistory.getElement (index));
                        //BufferedReader  buffer2 = new BufferedReader(new InputStreamReader(in2));
					}

				}
                catch(IOException ioe)
				{
                    errorHandler(ioe);
				}
			}
		}
		StringBuffer tempBuf = new StringBuffer(1);
		tempBuf.append(" ");
		return tempBuf;
	}


	/*******************************************************************************
	 *	NCBI database changes very frequently.
	 *	This method updates all the fetched files by downloading
	 *	it again from internet.
	 *	This functioanality can consume lot of time as well as data.
	 *	It will replace all the older fetched files with new ones.
	 *******************************************************************************/
	private static void updateHistory(JDialog dialog,JLabel label)
	{
		fetchedHistory.updateFetchedFileList();
		double percent=0;
		label.setText("  "+percent+"%");
		int xi=0;

    	for(int i=0;i<fetchedHistory.getSize ();i++)
        {
			/*******************************************************************************
			 *	When sequence is downloaded, it downloads fasta and genbank both
			 *	so if you are changing that behaviour than don't forget to remove this
			 *	if statement which make sure that same files are not downloaded repeatedly
			 *******************************************************************************/

			if (fetchedHistory.getElement(i).getName ().contains("genbank") || fetchedHistory.getElement (i).getName ().contains("user_"))
			{
				percent=((double)(i+1)/(double)(fetchedHistory.getSize()))*100;
				xi=(int)(percent*(double)10000);
				percent=(double)xi/(double)10000;
				label.setText(" "+percent+"%");
            	continue;
			}
			String s = fetchedHistory.getElement (i).getName ();
			s = s.substring (0,s.lastIndexOf ("."));

			seqDownload seq = new seqDownload(s,false);
			percent=((double)(i+1)/(double)(fetchedHistory.getSize()))*100;
			xi=(int)(percent*(double)10000);
			percent=(double)xi/(double)10000;
			label.setText(" "+percent+"%");
			if(updateFlag==false || !dialog.isShowing())
			{
				updateFlag=true;
				if(update1.isAlive())
					update1.interrupt();
				break;

			}
		}
		JOptionPane.showMessageDialog(app,"History updated successfully.");
	}



	/*******************************************************************************
	*	This static class implements Runnable Thread for Sliding Window Analysis
	*******************************************************************************/
	static class RunnableThread implements Runnable
	{
		public int encodingFlag,transformationFlag;
		public boolean forwardslide;
		public int fetchFlag;
		int dnaind=0,m,n,quarternion,startIndex,endIndex,windowW,windowG;
		char[] ary;
		double ra,rc,rg,rt,m2,n2,zta,ztu,ztv;
		long ztm;
		boolean realboolean,valid2;
		String name,s;
		Complex z1,z2;
		JDialog dialog;
		JButton ok;

		public RunnableThread(int enc,int trans,int fetchF,boolean f,JDialog dial,char[] ary,int startIndex,int endIndex,String name,String s,boolean realboolean,int dnaind,Complex z1,Complex z2,int m,int n,int quarternion,double ra,double rc,double rg,double rt,double m2,double n2,double zta,double ztu,double ztv,long ztm,JButton ok,int windowW,int windowG,boolean valid2)
		{
			this.windowW=windowW;
			this.windowG=windowG;
			this.valid2=valid2;
			this.ok=ok;
			fetchFlag=fetchF;encodingFlag=enc; transformationFlag=trans; forwardslide=f; dialog=dial;
			this.ary=ary;  this.startIndex=startIndex; this.endIndex=endIndex; this.name=name; this.s=s; this.realboolean=realboolean; this.dnaind=dnaind;this.z1=z1; this.z2=z2; this.m=m; this.n=n; this.quarternion=quarternion; this.ra=ra; this.rc=rc; this.rg=rg; this.rt=rt; this.m2=m2; this.n2=n2; this.zta=zta; this.ztu=ztu; this.ztv=ztv; this.ztm=ztm;
		}
		public void run()
		{
			try
			{
				int windows=windowW,diff1,diff2,diff3,ctr1;
				boolean bencode=true,btransform=true;
				double percent=0,diff;
				String filename31="";
				String filename32="";

				slidewindowplot=new File[ary.length-windows+1];
				ok.setText(" "+percent+"%");
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.toFront();
				ctr1=1;
				diff1=windowG-windowW+1;
				diff2=(ary.length-windowW+1);
				diff3=(ary.length-windowG+1);
				diff=((double)diff1/2)*((double)diff2+diff3);

				for(windows=windowW;windows<=windowG && valid2;windows++)
				{
					for(startIndex=1;startIndex<=(ary.length-windows+1) && bencode && btransform;startIndex++)
					{
						if(!dialog.isShowing())
						{
							t1.interrupt();
						}
						ok.setText(" "+percent+"%");
						endIndex=startIndex+windows-1;

						File fileName;
						if(forwardslide)
						{
							fileName = new File("../History/Window_Analysis/Fetched/W"+windows+"_"+startIndex+"_"+name);
						}
						else
						{
							fileName = new File("../History/Window_Analysis/Fetched/W"+windows+"r_"+startIndex+"_"+name);
						}
						try
						{
							FileWriter fr = new FileWriter(fileName);
							fr.write(">This fasta file is generated by Window Analysis of bioSpectrogram.");
							char[] charArray = s.substring(startIndex-1,endIndex).toCharArray();
							for(int i=0;i<charArray.length;i++)
							{
								if (i%70 == 0)
								{
									fr.write("\n");
								}
								fr.write(charArray[i]);
							}
							fr.close();
							fetchedHistory.updateFetchedFileList ();

							bencode=windowencode(fileName,encodingFlag,realboolean,dnaind,z1,z2,m,n,quarternion,ra,rc,rg,rt);

							if(bencode)
							{

								btransform=windowtransform(fileName,transformationFlag,m2,n2,zta,ztu,ztv,ztm,startIndex);
								File dir1=new File("../History/Window_Analysis/Encoded");
								wencodedname.renameTo(new File(dir1,wencodedname.getName()));
								fetchedHistory.updateFetchedFileList();
								encodedHistory.updateEncodedFileList();
								transformedHistory.updateTransformedFileList();
								proteinHistory.updateProteinFileList();

							}
							else
							{
								btransform=false;
							}
						}
						catch(IOException ioe)
						{
							errorHandler(ioe);
						}
						//percent=((double)startIndex*100)/((double)ary.length-windows+1);
						percent=((double)(ctr1*100))/((double)diff);
						ok.setText(" "+percent+"%");
						ctr1++;
					}

					if(startIndex==(ary.length-windows+2) && bencode && btransform)
					{
						int start=1;
						int end=startIndex-1;
						String filename3=slidewindowplot[0].getName().replaceFirst("_1_","_");
						String filet=filename3.substring(filename3.lastIndexOf('_')+1,filename3.lastIndexOf('.'));
						String fileenc=filename3.substring(0,filename3.lastIndexOf('_'));
						fileenc=fileenc.substring(fileenc.lastIndexOf('_')+1);
						if (filename3.contains("T05"))
						{
							// This transformations produces outputwhich can be plot in 1D
							filename3 = filename3.replace('.','_');
							filename3 = filename3.concat(".m");
							filename3 = "../History/Window_Analysis/Matlab_Files/"+filename3;
							String ssdf="";
							try
							{
								ssdf = slidewindowplot[0].getCanonicalPath().replaceFirst("_1_","_");
								FileWriter w = new FileWriter(filename3);
								w.write("% This Matlab script is generated using BioSpectrogram.\n");
								w.write("function keypressfunction \n");
								w.write("figure('KeyPressFcn',@stopfig); \n");
								w.write("for i=1:"+end+"\n");
								String[] ar=slidewindowplot[0].getCanonicalPath().split("_1_");
								w.write("s=strcat('"+ar[0]+"_',num2str(i),'_"+ar[1]+"');\n");
								w.write("val = importdata(s,'%s');\n");
								int title1=ary.length+1;
								w.write("plot(val);\n");
								w.write("xlabel('Index');\n");
								w.write("ylabel('Discrete Haar Wavelet Transform coefficients');\n");
								String name1=fetchedHistory.getElement(fetchFlag).getName();
								name1=name1.substring(0,name1.lastIndexOf('.'));
								w.write("title(sprintf(strcat('");

								if(name.contains("W_") || name.contains("user_"))
								{
									w.write("File: "+name);
								}
								else
								{
									w.write("Accession Number: "+name.substring(0,name.lastIndexOf(".")));
								}

								w.write(", Window size: "+windows+" \\n Encoding:"+fileenc+" \\n Transformation:T05  \\n Generated using BioSpectrogram Sliding Window Analysis and Matlab','");

								if(forwardslide)
								{
									w.write(" \\n Forward sliding window, Subsequence: Starting index: ',num2str(i),' Ending index: ',num2str(i+"+(windows-1)+"))),'Interpreter','none');");
								}

								else
								{
									w.write(" \\n Backward sliding window, Subsequence: Starting index: ',num2str("+title1+"-i),' Ending index: ',num2str("+title1+"-i-"+(windows-1)+"))),'Interpreter','none');");
								}

								w.write("pause("+plotDelay+");\n");
								w.write("end\n");
								w.write("end \n function stopfig(src,evnt) \nif evnt.Character=='q'\n pause;\n end \n end\n");

								w.close();
								if(windowW==windowG)
								{
									JOptionPane.showMessageDialog(app,"Matlab script file "+filename3+" successfully generated !");
								}
								else
								{
									if(windows==windowW)
									{
										filename31=filename3;
									}
									else if(windows==windowG)
									{
										filename32=filename3;
										JOptionPane.showMessageDialog(app,"Matlab script files "+filename31+"..........."+filename32+" successfully generated !");
									}
								}
							}
							catch (IOException ioe)
							{
								errorHandler(ioe);
							}
						}
						else if (filename3.contains("T01")||filename3.contains("T02")||filename3.contains("T03")||filename3.contains("T04")||filename3.contains("T06"))
						{
							String xlab="";
							String chirpzt="a="+zta+",u="+ztu+",v="+ztv+",m="+ztm;

							// This transformations produces outputwhich can be plot in 2D
							filename3 = filename3.replace('.','_');
							filename3 = filename3.concat(".m");

							//filename = filename.substring (0,transformedHistory.getElement(transformationFlag).getName().lastIndexOf ("."))+".m";
							filename3 = "../History/Window_Analysis/Matlab_Files/"+filename3;
							String ssdf="";
							try
							{
								ssdf = slidewindowplot[0].getCanonicalPath().replaceFirst("_1_","_");
								FileWriter w = new FileWriter(filename3);
								w.write("% This Matlab script is generated using BioSpectrogram.\n");
								w.write("function keypressfunction \n");
								w.write("figure('KeyPressFcn',@stopfig); \n");
								w.write("for i=1:"+end+"\n");
								String[] ar=slidewindowplot[0].getCanonicalPath().split("_1_");
								w.write("s=strcat('"+ar[0]+"_',num2str(i),'_"+ar[1]+"');\n");
								w.write("val = importdata(s,'%s');\n");
								w.write("ans = str2double(val);\n");

								w.write("subplot(2,1,1);\n");
								if(filename3.contains("T01"))
								{
									xlab="FFT ";
								}
								else if(filename3.contains("T02"))
								{
									xlab="Hilbert Transform ";
								}
								else if(filename3.contains("T03"))
								{
									xlab="Z Transform ";
								}
								else if(filename3.contains("T04"))
								{
									xlab="Analyic Signal ";
								}
								else if(filename3.contains("T06"))
								{
									xlab="ChirpZ Transform ";
								}

								w.write("plot(abs(ans));\n");
								if(filename3.contains("T03"))
								{
									w.write("xlabel('z="+m2+"+"+n2+"i');\n");
								}
								else
								{
									w.write("xlabel('Index');\n");
								}
								w.write("ylabel('"+xlab+"Magnitude');");

								int title1=ary.length+1;

								w.write("title(sprintf(strcat('");
								if(name.contains("W_") || name.contains("user_"))
								{
									w.write("File: "+name);
								}
								else
								{
									w.write("Accession Number: "+name.substring(0,name.lastIndexOf(".")));
								}
								if(filename3.contains("T06"))
								{
									w.write(", "+chirpzt+"\\n");
								}
								w.write(", Window size: "+windows+" , Encoding:"+fileenc+" , Transformation:"+filet+"  \\n Generated using BioSpectrogram Sliding Window Analysis and Matlab')),'Interpreter','none');");
								w.write("subplot(2,1,2);\n");
								w.write("plot(angle(ans));\n");

								if(filename3.contains("T03"))
								{
									w.write("xlabel('z="+m2+"+"+n2+"i');\n");
								}
								else
								{
									w.write("xlabel('Index');\n");
								}

								w.write("ylabel('"+xlab+"Phase');");

								if(forwardslide)
								{
									w.write("title(sprintf(strcat('Forward sliding window, Subsequence: Starting index: ',num2str(i),' Ending index: ',num2str(i+"+(windows-1)+"))));\n");
								}
								else
								{
									w.write("title(sprintf(strcat('Backward sliding window, Subsequence: Starting index: ',num2str("+title1+"-i),' Ending index: ',num2str("+title1+"-i-"+(windows-1)+"))));\n");
								}

								//w.write("title(strcat(num2str("+title1+"-i),' phase'));\n");
								w.write("pause("+plotDelay+");\n");
								w.write("end\n");
								w.write("end \n function stopfig(src,evnt) \nif evnt.Character=='q'\n pause;\n end \n end\n");
								w.close();

								if(windowW==windowG)
								{
									JOptionPane.showMessageDialog(app,"Matlab script file "+filename3+" successfully generated !");
								}
								else
								{
									if(windows==windowW)
									{
										filename31=filename3;
									}
									else if(windows==windowG)
									{
										filename32=filename3;
										JOptionPane.showMessageDialog(app,"Matlab script files \""+filename31+"\"\n.............\n\""+filename32+"\" successfully generated ! \n Subsequences in each window are saved as \"History/Window_Analysis/Fetched/W<windowsize>_<startIndex>_"+name+"\".Window size varies from "+windowW+" to "+windowG+".\n Encoded and transformed files for each window are saved in \"History/Window_Analysis/Encoded\" and \"History/Window_Analysis/Transformed\" folders respectively.");
									}
								}
							}
							catch (IOException ioe)
							{
								errorHandler(ioe);
							}
						}
					}
				}
				Runtime rt = Runtime.getRuntime();
							//rt.gc();
			}
			catch(OutOfMemoryError e)
			{

				//in.close();
				Runtime rt = Runtime.getRuntime();
				//rt.gc();
				e.printStackTrace(System.out);
				JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
					"If software seems to be hung after pressing OK, minize & maximize :)");
				dialog.dispose();
				if(t1.isAlive())
					t1.interrupt();
				//StringBuffer tempBuf = new StringBuffer(1);
				//tempBuf.append(" ");
				//return tempBuf;
			}
			dialog.dispose();
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for action "window"
	 *	It offers three options:
	 *	1. Sliding Window Analysis
	 *	2.Stagnant Window Analysis
	 *	3.Gene Prediction
	 *	Sliding window analysis allows the user to choose any encoding and transform
	 *	and a window size, and transformed files are saved in Window_Analysis folder
	 *	in History folder. A matlab file is generated for viewing outputs of all the
	 *	windows sequentially, inside History/Window_Analysis/Matlab_Files folder.
	 *	After running the matlab script, user needs to press space bar to go to the
	 *	next plot.Continously pressing space bar will allow the user to browse through
	 *	all the plots corresponding to different windows with titles indicating
	 *	starting Index of the window and window size can be known from the filename
	 *	(Filename starts with Ww, where w is the window size) for forward slidingwindow
	 *	and Wwr where w is the window size and r denotes reverse/Backward sliding window.
	*******************************************************************************/


    private static Thread t2 = new Thread();

    static class RunnableThread2 implements Runnable
    {
    	int fetchFlag,window;
    	String s;
    	JDialog dialog;
    	JButton ok;
    	public RunnableThread2(int fetchFlag,int window,String s,JButton ok,JDialog dialog)
		{
  			this.fetchFlag=fetchFlag;
  			this.window=window;
  			this.s=s;
    		this.ok=ok;
    		this.dialog=dialog;
		}

    	public void run()
    	{
			try
			{
				int startIndex, endIndex,lengthofseq,len,tempIndex,temparrayval,maxIndex=1;
				Complex[] array1,array2,array3,array4;
				char[] charArray;
				ChirpZTransform tempTrans;
				DNAindicator temp;
				double[] finalArray;
				String filename,transName,plotfile;
				FileWriter writer,w=null,writer1=null,fr;
				String results="",tname="",secondpart="",firstpart="",name,seq;
				String[] secpart=null;
				String[] ch=null;
				boolean valid=false;
				File fileName;

				bottomLabel.setText(" ");
				bottomLabel.updateUI();
				ok.setEnabled(false);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				String filename11 = fetchedHistory.getElement(fetchFlag).getName();
				filename11 = filename11.substring (0,fetchedHistory.getElement(fetchFlag).getName().lastIndexOf ("."))+"_geneprediction.fasta";
				filename11 = "../History/Gene_Prediction/"+filename11;
				File fileName1=new File(filename11);

				try
				{
					writer1=new FileWriter(fileName1);
					writer1.write("Gene prediction results: ");
					results=results.concat("Gene prediction results:").concat(" ");
				}
				catch(IOException e1)
				{
					errorHandler(e1);
				}
				double percent=0;
				ok.setText(" "+percent+"%");
				name = fetchedHistory.getElement(fetchFlag).getName();
				dialog.toFront();

				for(startIndex=0;startIndex<=(s.length()-window);startIndex++)
				{
					if(!dialog.isShowing()) t2.interrupt();
					{
						endIndex=startIndex+window-1;
					}
					try
					{
						valid = false;
						if (((endIndex-startIndex)+1)%3 == 0)
						{
							valid = true;
						}
						else
						{
							JOptionPane.showMessageDialog(app,"for gene finding, window size shud b multiple of 3");
						}
						if (valid && startIndex >= 0 && startIndex <= endIndex && endIndex < s.length())
						{
							/*******************************************************************************
								*	Fasta File Creation
							*******************************************************************************/

							fileName = new File("../History/Fetched/W"+window+"_"+name);
							try
							{
								fr = new FileWriter(fileName);
								fr.write(">This fasta file is generated by Window Analysis of bioSpectrogram.");
								charArray = s.substring(startIndex,endIndex+1).toCharArray();
								for(int i=0;i<charArray.length;i++)
								{
									if (i%70 == 0)
									{
										fr.write("\n");
									}
									fr.write(charArray[i]);
								}
								fr.close();
								fetchedHistory.updateFetchedFileList ();
							}
							catch(IOException exp)
							{
								errorHandler(exp);
							}

							/*******************************************************************************
								*	Encoding of Fasta File using DNA indicator
							*******************************************************************************/
							temp = new DNAindicator(fileName,4);	// Indicator with T
							temp = new DNAindicator(fileName,3);	// Indicator with G
							temp = new DNAindicator(fileName,2);	// Indicator with C
							temp = new DNAindicator(fileName,1);	// Indicator with A

							encodedHistory.updateEncodedFileList();

							/*******************************************************************************
								*	Transformation of Encoded Fasta File using ChirpZTransform
								*	with parameters a = 1, u = 1, v = m = size of seqence
							*******************************************************************************/

							double nextpercent=((double)((startIndex+1)*100))/((double)(s.length()-window+1));
							double dif=nextpercent-percent;
							seq = getOnlySequence(3,2).toString().replaceAll("N","").trim().replaceAll("\n","");
							lengthofseq=endIndex-startIndex+1;
							tempTrans = new ChirpZTransform(1,1,lengthofseq,lengthofseq);
							array1 = tempTrans.ChirpZTransformed(seq);
							percent=percent+(dif/4);
							ok.setText(" "+percent+"%");
							seq = getOnlySequence(2,2).toString().replaceAll("N","").trim().replaceAll("\n","");
							tempTrans = new ChirpZTransform(1,1,lengthofseq,lengthofseq);
							array2 = tempTrans.ChirpZTransformed(seq);
							percent=percent+(dif/4);
							ok.setText(" "+percent+"%");
							seq = getOnlySequence(1,2).toString().replaceAll("N","").trim().replaceAll("\n","");
							tempTrans = new ChirpZTransform(1,1,lengthofseq,lengthofseq);
							array3 = tempTrans.ChirpZTransformed(seq);
							percent=percent+(dif/4);
							ok.setText(" "+percent+"%");
							seq = getOnlySequence(0,2).toString().replaceAll("N","").trim().replaceAll("\n","");
							tempTrans = new ChirpZTransform(1,1,lengthofseq,lengthofseq);
							array4 = tempTrans.ChirpZTransformed(seq);
							finalArray = new double[array1.length];
							maxIndex=1;
							for(int j=0;j < array1.length;j++)
							{
								finalArray[j] = array1[j].abs()*array1[j].abs();
								finalArray[j] = finalArray[j] + array2[j].abs()*array2[j].abs();
								finalArray[j] = finalArray[j] + array3[j].abs()*array3[j].abs();
								finalArray[j] = finalArray[j] + array4[j].abs()*array4[j].abs();
								if ( finalArray[j] > finalArray[maxIndex] && j != 0)
								{
									maxIndex = j;
								}
							}

							filename = fetchedHistory.getElement(0).getName();
							filename = filename.substring (0,fetchedHistory.getElement(0).getName().lastIndexOf ("."))+"_"+(startIndex+1)+"_W01.fasta";
							filename = "../History/Gene_Prediction/Transformed/"+filename;
							try
							{
								writer = new FileWriter(filename);
								for(int i=1;i<finalArray.length;i++)
								{
									writer.write(Double.toString(finalArray[i]));
									writer.write("\n");
								}
								writer.close();

								tempIndex = (int) finalArray.length/3;
								temparrayval=(int)finalArray[maxIndex]*100;
								finalArray[maxIndex]=temparrayval/100;
								temparrayval=(int)finalArray[tempIndex]*100;
								finalArray[tempIndex]=temparrayval/100;
								temparrayval=(int)finalArray[tempIndex-1]*100;
								finalArray[tempIndex-1]=temparrayval/100;
								temparrayval=(int)finalArray[tempIndex+1]*100;
								finalArray[tempIndex+1]=temparrayval/100;

								if ( finalArray[maxIndex] == finalArray[tempIndex] && finalArray[tempIndex-1]<finalArray[tempIndex] && finalArray[tempIndex+1]<finalArray[tempIndex])
								{

									writer1.write(Integer.toString(startIndex+1)+" "+Integer.toString(endIndex+1));
									writer1.write("\n");
									results=results.concat(Integer.toString(startIndex+1)).concat(" ").concat(Integer.toString(endIndex+1)).concat("\n");
								}
							}
							catch(IOException exp)
							{
								errorHandler(exp);
							}

							/*******************************************************************************
								*	Lets create MATLAB file for final output.
							*******************************************************************************/
							try
							{
								if(startIndex==0)
								{
									tname=new File(filename).getCanonicalPath();
									ch=tname.split("_1_W0");
									firstpart=ch[0];
									secpart=ch[0].split("W"+window+"_");
									secondpart=secpart[1];
								}
								if(startIndex==(s.length()-window))
								{
									File f=new File(tname);
									transName = f.getName();
									plotfile=fetchedHistory.getElement(0).getName();
									plotfile=plotfile.substring(0,plotfile.lastIndexOf('.'))+"_W01.fasta";
									plotfile = plotfile.replace('.','_');
									plotfile = plotfile.concat(".m");
									plotfile = "../History/Gene_Prediction/Matlab_Files/"+plotfile;

									w = new FileWriter(plotfile);
									w.write("% This Matlab script is generated using BioSpectrogram.\n");
									w.write("function keypressfunction\n");
									w.write("figure('KeyPressFcn',@stopfig);\n");
									w.write("for i=1:"+(startIndex+1)+"\n");
									w.write("val = importdata(strcat('"+firstpart+"_"+"',num2str(i),'_W01.fasta')"+",'%s');\n");
									len=finalArray.length-1;
									w.write("w=linspace(2*pi/"+len+",2*pi,"+len+");\n");
									w.write("stem(w,val);\n");
									w.write("xlabel('Angular Frequency'); \n ylabel('Sum of Power spectra of indicator sequences');\n");
									w.write("title(sprintf(strcat('");
									if(secondpart.contains("W_") || secondpart.contains("user_"))
									{
										w.write("File: "+secondpart+".fasta \\n");
									}
									else
									{
										w.write("Accession Number:"+secondpart+"\\n");
									}
									w.write("C Yin,Yau Gene Prediction with window size: "+window+"\\n Sum of Power spectrum of all indicator sequences \\n Subsequence: Starting index: ',num2str(i),' Ending index: ',num2str(i+"+(window-1)+"),'\\n Generated using BioSpectrogram and Matlab')),'Interpreter','none')");
									w.write(";\n");
									w.write("pause("+plotDelay+");\n");
									w.write("end \n");
									w.write("end \n function stopfig(src,evnt)\n   if evnt.Character=='q'\n pause; \n end \nend");
									if(startIndex==(s.length()-window))
									{
										w.close();
									}
								}
							}
							catch (IOException ea)
							{
								errorHandler(ea);
							}
						}
						else
						{
							if (!valid)
							{
								JOptionPane.showMessageDialog(app,"Index values are invalid ");
							}
						}
					}
					catch(NumberFormatException nfe)
					{
						JOptionPane.showMessageDialog(app,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
					}
					percent=((double)((startIndex+1)*100))/((double)(s.length()-window+1));
					ok.setText(" "+percent+"%");
				}
				try
				{
					writer1.close();

					fetchedHistory.getElement(0).delete();
					encodedHistory.getElement(0).delete();
					encodedHistory.getElement(1).delete();
					encodedHistory.getElement(2).delete();
					encodedHistory.getElement(3).delete();
					prevtoptext=topLabel.getText();
					prevbottomtext=bottomLabel.getText();

					if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
					{
						backButton.setEnabled(false);
						backButton.setText("Back");
						back.setEnabled(false);
						back.setText("Back");
					}
					else
					{
						backButton.setEnabled(true);
						back.setEnabled(true);
					}

					topLabel.setText(getSequence(fetchFlag,1).toString());
					topLabel.updateUI();
					bottomLabel.setText(results);
					bottomLabel.updateUI();
					JOptionPane.showMessageDialog(app,"Gene prediction file   \""+filename11+"\" generated successfully.\n Transformation files for indicator sequences in each window are saved in History folder as \n \"History/Gene_Prediction/Transformed/W"+window+"_"+secondpart+"_<startIndex>_W01.fasta\" where startIndex varies from 1 to "+(s.length()-window+1)+".\n Matlab script for plotting the transformed files is saved as \"History/Gene_Prediction/Matlab_Files/W"+window+"_"+secondpart+"_W01_fasta.m\".");
				}
				catch(IOException ioe)
				{
					errorHandler(ioe);
				}
				dialog.dispose();
				Runtime rt = Runtime.getRuntime();
				//rt.gc();
			}
			catch(OutOfMemoryError e)
			{
				//in.close();
				Runtime rt = Runtime.getRuntime();
				//rt.gc();
				e.printStackTrace(System.out);
				JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
					"If software seems to be hung after pressing OK, minize & maximize :)");
				dialog.dispose();
				if(t2.isAlive())
					t2.interrupt();
				//StringBuffer tempBuf = new StringBuffer(1);
				//tempBuf.append(" ");
				//return tempBuf;
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Switch" Button
	*******************************************************************************/
	static class switchListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			if (switchFlag)	// currently in DNA mode, change to Protein Mode
			{
				switchFlag = false;

				switchButton.setToolTipText("<html><center>Switch to DNA Mode</center><html>");

				switchButton.setIcon(new ImageIcon("../Icons/switch2.png"));
				switchButton.setText(" Switch2DNA ");

				switch2.setIcon(new ImageIcon("../Icons/menu_switch2.png"));
				switch2.setText(" Switch2DNA ");

				windowButton.setEnabled(false);
				fetchButton.setEnabled(false);
				updateButton.setEnabled(false);
				orfButton.setEnabled(false);
				geneButton.setEnabled(false);

				window.setEnabled(false);
				fetch.setEnabled(false);
				update.setEnabled(false);
				orf.setEnabled(false);
				gene.setEnabled(false);
			}
			else // currently in Protein mode, change to DNA Mode
			{
				switchFlag = true;

				switchButton.setToolTipText("<html><center>Switch to Protein Mode</center><html>");

				switchButton.setIcon(new ImageIcon("../Icons/switch1.png"));
				switchButton.setText(" Switch2AA  ");

				switch2.setIcon(new ImageIcon("../Icons/menu_switch1.png"));
				switch2.setText(" Switch2AA  ");

				windowButton.setEnabled(true);
				fetchButton.setEnabled(true);
				updateButton.setEnabled(true);
				orfButton.setEnabled(true);
				geneButton.setEnabled(true);

				window.setEnabled(true);
				fetch.setEnabled(true);
				update.setEnabled(true);
				orf.setEnabled(true);
				gene.setEnabled(true);
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Back" Button
	*******************************************************************************/
	static class backListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			String temp=prevtoptext;
			prevtoptext=topLabel.getText();
			topLabel.setText(temp);
			topLabel.updateUI();
			temp=prevbottomtext;
			prevbottomtext=bottomLabel.getText();
			bottomLabel.setText(temp);
			bottomLabel.updateUI();

			if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
			{
				backButton.setEnabled(false);
				backButton.setText("Back");
				back.setEnabled(false);
				back.setText("Back");
			}
			else
			{
				backButton.setEnabled(true);
				back.setEnabled(true);
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Home" Button
	*******************************************************************************/
	static class homeListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evth)
		{
			if(fetchedHistory.getSize()>0)
			{
				prevtoptext=topLabel.getText();
				topLabel.setText(getSequence(0,1).toString());
			}
			else
			{
				prevtoptext=topLabel.getText();
				topLabel.setText(" ");
			}
			topLabel.updateUI();
			prevbottomtext=bottomLabel.getText();
			bottomLabel.setText(" ");
			bottomLabel.updateUI();
			if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
			{
				backButton.setEnabled(false);
				backButton.setText("Back");
				back.setEnabled(false);
				back.setText("Back");
			}
			else
			{
				backButton.setEnabled(true);
				back.setEnabled(true);
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Display" Button
	*******************************************************************************/
	static class displayListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			if (switchFlag)
			{
				fetchedHistory.updateFileList();
			}
			else
			{
				proteinHistory.updateFileList();
			}

			final JButton okButton=new JButton("OK");
			final JButton cancelButton=new JButton("Cancel");
			final JDialog dialog=new JDialog(app,"Display fetched sequence from History",JDialog.ModalityType.DOCUMENT_MODAL);
			final JPanel panel1=new JPanel();
			JLabel label=new JLabel("Select File for displaying:");
			fetchFlag=0;
			final JComboBox fetchedfiles;

			if (switchFlag)
			{
				fetchedfiles = new JComboBox(fetchedHistory.getArray());
			}
			else
			{
				fetchedfiles = new JComboBox(proteinHistory.getArray());
			}


			fetchedfiles.addActionListener (new ActionListener()
			{
            	public void actionPerformed(ActionEvent e)
                {
					JComboBox temp = (JComboBox) e.getSource ();
                    fetchFlag = temp.getSelectedIndex ();
				}
			});

            okButton.addActionListener (new ActionListener()
			{
            	public void actionPerformed(ActionEvent e)
                {
                	try
                	{
	            		prevtoptext=topLabel.getText();
						prevbottomtext=bottomLabel.getText();
						if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
						{
							backButton.setEnabled(false);
							backButton.setText("Back");
							back.setEnabled(false);
							back.setText("Back");
						}
						else
						{
							backButton.setEnabled(true);
							back.setEnabled(true);
						}

						if (switchFlag)
						{
							topLabel.setText(getSequence(fetchFlag,1).toString());
						}
						else
						{
							topLabel.setText(getSequence(fetchFlag,4).toString());
						}

						topLabel.updateUI();
						bottomLabel.setText(" ");
						bottomLabel.updateUI();
						dialog.dispose();
                	}
                	catch(OutOfMemoryError ev)
					{
						//in.close();
						Runtime rt = Runtime.getRuntime();
						//rt.gc();
						ev.printStackTrace(System.out);
						JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
							"If software seems to be hung after pressing OK, minize & maximize :)");
						dialog.dispose();

						//StringBuffer tempBuf = new StringBuffer(1);
						//tempBuf.append(" ");
						//return tempBuf;
					}
                }
			});

			cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					dialog.dispose();
				}
			});

			panel1.setLayout (new GridLayout(2,2));
            panel1.add(label);
            panel1.add(fetchedfiles);
            panel1.setBackground (Color.WHITE);
            panel1.setBorder (new EtchedBorder(EtchedBorder.RAISED));
            panel1.add(okButton);
            panel1.add(cancelButton);
            dialog.getContentPane ().add(panel1);
            dialog.setBounds(maxScreenWidth/3,maxScreenHeight/3,400,100);
            dialog.setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
		}
	}

	/*******************************************************************************
	 *	This static class implements Runnable Thread for "Import" Button
	*******************************************************************************/
	static class importThread implements Runnable
	{
		File src,dst;
		JDialog dialog;
		JLabel label;
		InputStream in;
		OutputStream out;
		public importThread(File src,File dst,JDialog dialog,JLabel label,InputStream in,OutputStream out)
		{
			this.src=src;
			this.dst=dst;
			this.dialog=dialog;
			this.in=in;
			this.out=out;
			this.label=label;
		}
		public void run()
		{
			try
			{
				byte[] buf = new byte[1024*1024];
				int len;
				int total=in.available();
				double percent=0;
				double count=0;
				String display="";
				while ((len = in.read(buf)) > 0 )
				{
					for(int i=0;i<len && !src.getName().contains(".fasta");i++)
					{
						int tmp=(char)buf[i];
						if(tmp>=97 && tmp<=122)
						tmp=tmp-32;
						buf[i]=(byte)tmp;
					}
					//display=display.concat(new String(new String(buf).toCharArray()));
					out.write(buf, 0, len);
					count=count+(double)len;
					DecimalFormat df=new DecimalFormat("#.##");
					percent=(count/(double)total)*100;
					label.setText(" "+df.format(percent)+"%  ");
				}

				in.close();
				out.close();

				fetchedHistory.updateFetchedFileList();
				prevtoptext=topLabel.getText();
				prevbottomtext=bottomLabel.getText();

				if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
				{
					backButton.setEnabled(false);
					backButton.setText("Back");
					back.setEnabled(false);
					back.setText("Back");
				}
				else
				{
					backButton.setEnabled(true);
					back.setEnabled(true);
				}

				topLabel.setText(getSequence(0,1).toString());
				topLabel.updateUI();

				bottomLabel.setText(" ");
				bottomLabel.updateUI();
				dialog.dispose();
				if (switchFlag)
				{
					JOptionPane.showMessageDialog(app,"File Uploaded to History/Fetched folder successfully !");
				}
				else
				{
					JOptionPane.showMessageDialog(app,"File Uploaded to History/Protein folder successfully !");
				}
				Runtime rt = Runtime.getRuntime();
							//rt.gc();
			}
			catch(OutOfMemoryError e)
			{
				//in.close();
				Runtime rt = Runtime.getRuntime();
				//rt.gc();
				e.printStackTrace(System.out);
				JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
					"If software seems to be hung after pressing OK, minize & maximize :)");
				dialog.dispose();
				if(import1.isAlive())
					import1.interrupt();
				//StringBuffer tempBuf = new StringBuffer(1);
				//tempBuf.append(" ");
				//return tempBuf;
			}
			catch(FileNotFoundException fnfe)
			{
				errorHandler(fnfe);
				dialog.dispose();
			}
			catch (IOException ioe)
			{
				errorHandler(ioe);
				dialog.dispose();
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Import" Button
	*******************************************************************************/
	static class importListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			final File src,dst;

			File df=new File("../History");
			JFileChooser fd = new JFileChooser(df);

			FileNameExtensionFilter filter = new FileNameExtensionFilter("FASTA file","fasta","fa","fna","fsa","mpfa");

			// Files with other extensions are getting uploaded
			fd.addChoosableFileFilter(filter);
			fd.setFileFilter(filter);
			fd.setDialogTitle("Upload a file");

			int status = fd.showOpenDialog(app);

			if (status == JFileChooser.APPROVE_OPTION)
			{
  				src = fd.getSelectedFile();
  				String name = fd.getSelectedFile().getName();
  				if (switchFlag)
  				{
  					dst = new File("../History/Fetched/"+name.substring(0,name.lastIndexOf('.'))+".fasta");
  				}
  				else
  				{
  					dst = new File("../History/Protein/"+name.substring(0,name.lastIndexOf('.'))+".fasta");
  				}

  				String comp=fd.getSelectedFile().getName().substring(fd.getSelectedFile().getName().lastIndexOf('.'));
  				boolean cond1 = comp.equalsIgnoreCase(".fasta") || comp.equalsIgnoreCase(".fa");
  				boolean cond2 = comp.equalsIgnoreCase(".fna") || comp.equalsIgnoreCase(".fsa");
				if (cond1||cond2||comp.equalsIgnoreCase(".mpfa"))
				{
					try
					{
						if(src.getCanonicalPath().equals(dst.getCanonicalPath()))
						{
							if (switchFlag)
							{
								JOptionPane.showMessageDialog(app,"File already exists in History/Fetched folder.");
							}
							else
							{
								JOptionPane.showMessageDialog(app,"File already exists in History/Protein folder.");
							}
						}
						else
						{
							try
							{
								final InputStream in;
								final OutputStream out;
								in = new FileInputStream(src);
						 		out = new FileOutputStream(dst);
								final JDialog dialog=new JDialog(app,"Uploading "+src.getName()+"...");

								JPanel panel=new JPanel();
								JLabel label=new JLabel("Uploading "+src.getName()+"...");

								JButton button=new JButton("Cancel");
								button.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent up)
									{
										if(import1.isAlive())
										{
											import1.interrupt();
										}
										try
										{
											in.close();
											out.close();
											dst.delete();
										}
										catch(IOException en)
										{
											errorHandler(en);
										}
										dialog.dispose();
	   									JOptionPane.showMessageDialog(app,"File not uploaded completely. Operation canceled by the user.");
									}
								});

								panel.setLayout(new GridLayout(1,2));
								panel.add(label);
								panel.add(button);
								dialog.getContentPane().add(panel);
								dialog.pack();
								dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

				        		dialog.addWindowListener(new WindowAdapter()
								{
	        						public void windowClosing(WindowEvent e)
									{
										if(import1.isAlive())
										{
											import1.interrupt();
										}
										try
										{
											in.close();
											out.close();
										}
										catch(IOException en)
										{
											errorHandler(en);
										}
	   									dialog.dispose();
										fetchedHistory.updateFetchedFileList();
										JOptionPane.showMessageDialog(app,"File not uploaded completely.Operation canceled by the user.");
									}
								});

								dialog.setVisible(true);
								dialog.setLocation(maxScreenWidth/2,maxScreenHeight/2);
								importThread importthread=new importThread(src,dst,dialog,label,in,out);
								import1=new Thread(importthread);
								import1.start();
							}
							catch(FileNotFoundException ea)
							{
								errorHandler(ea);
							}
						}
					}
					catch(IOException ioe)
					{
						errorHandler(ioe);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(app,"Please select valid fasta file !");
				}
			}
			else if (status == JFileChooser.CANCEL_OPTION)
			{
  				//JOptionPane.showMessageDialog(app,"Select FASTA file !");
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements Runnable Thread for "Fetch" Button
	*******************************************************************************/
	static class fetchThread implements Runnable
	{
    	JDialog dialog;
    	JLabel label;
    	String accNo;
    	public fetchThread(JDialog dialog,JLabel label,String accNo)
		{
    		this.dialog=dialog;
    		this.label=label;
    		this.accNo=accNo;
    	}
    	public void run()
    	{
    		try
    		{
				seqDownload seq = new seqDownload(accNo,true);
				fetchedHistory.updateFetchedFileList ();

				/*******************************************************************************
				*	Here getSequence(0,1) means that index 0 and flag 1
				*	Index 0 refer to top element in the array which is added latest
				*	Flag 1 means that we are looking into fetched history's array
				*******************************************************************************/

				dialog.dispose();
				prevtoptext=topLabel.getText();
				prevbottomtext=bottomLabel.getText();

				if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
				{
					backButton.setEnabled(false);
					backButton.setText("Back");
					back.setEnabled(false);
					back.setText("Back");
				}
				else
				{
					backButton.setEnabled(true);
					back.setEnabled(true);
				}

				topLabel.setText (getSequence(0,1).toString());
                topLabel.updateUI ();
				bottomLabel.setText (" ");
                bottomLabel.updateUI ();

				fetchedHistory.updateFetchedFileList();
    		}
    		catch(OutOfMemoryError e)
			{
				//in.close();
				Runtime rt = Runtime.getRuntime();
				//rt.gc();
				e.printStackTrace(System.out);
				JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
					"If software seems to be hung after pressing OK, minize & maximize :)");
				dialog.dispose();
				if(fetch1.isAlive())
					fetch1.interrupt();
				//StringBuffer tempBuf = new StringBuffer(1);
				//tempBuf.append(" ");
				//return tempBuf;
			}
    	}
    }

	/*******************************************************************************
	 *	This static class implements action listener for "Fetch" Button
	*******************************************************************************/
	static class fetchListener implements ActionListener
    {
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				String message = "Fetch uses Entrez utilities for fetching DNA sequences from NCBI database.\n";
				message = message+"Frequency, Timing and Registration of E-utility URL Requests \n";
				message = message+"In order not to overload the E-utility servers, NCBI recommends that users";
				message = message+"post no more than three URL requests per second and limit large jobs to either weekends\n";
				message = message+"or between 9:00 PM and 5:00 AM Eastern time during weekdays. \n";
				message = message+"Failure to comply with this policy may result in an IP address being blocked from accessing NCBI.";
				message = message+" If NCBI blocks an IP address, \n";
				message = message+"service will not be restored unless the developers of the software accessing the E-utilities register ";
				message = message+"values of the tool and email parameters with NCBI.\n For further information on user policy, visit : \n";
				message = message+"http://www.ncbi.nlm.nih.gov/books/NBK25497/";
				JOptionPane.showMessageDialog(app,message);

				// pops up the dialog box for accession number
				fetchedHistory.updateFetchedFileList();
				String tempAcc = JOptionPane.showInputDialog(app, "Enter accession number : ",null, JOptionPane.QUESTION_MESSAGE);
				final String accNo = tempAcc.replaceAll(" ","");

				// call check internet connection method
				if (accNo !=null)
				{
					if (! checkInternet ())
					{
						JOptionPane.showMessageDialog (app,"Please check your internet connection !");
					}
					else
					{
						if (checkHistory(accNo))
						{
							JOptionPane.showMessageDialog (app,"File already exist in your history !");
						}
						else
						{
							// download the sequence
							final JDialog dialog=new JDialog(app,"Fetching "+accNo+"...");
							JPanel panel=new JPanel();
							JLabel label=new JLabel("Fetching "+accNo+"...");

							JButton button=new JButton("Cancel");
							button.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent up)
								{
									if(fetch1.isAlive())
									{
										fetch1.interrupt();
									}
									dialog.dispose();
									if(fetchedHistory.getSize()>0)
									{
										File d=fetchedHistory.getElement(0);
										if(d.getName().contains(accNo.toString()))
										{
											d.delete();
										}
										fetchedHistory.updateFetchedFileList();
										d=fetchedHistory.getElement(0);
										if ( d.getName().contains(accNo.toString()) )
										{
											d.delete();
										}
									}
									JOptionPane.showMessageDialog(app,"Files for accession number "+accNo+" not downloaded completely.Incomplete files are deleted.");
								}
							});

							panel.setLayout(new GridLayout(1,2));
							panel.add(label);
							panel.add(button);
							dialog.getContentPane().add(panel);
							dialog.pack();
							dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
							dialog.addWindowListener(new WindowAdapter()
							{
								public void windowClosing(WindowEvent e)
								{
									//System.out.println("closing...");
									if(fetch1.isAlive())
									{
										fetch1.interrupt();
									}
									dialog.dispose();
									fetchedHistory.updateFetchedFileList();
									if(fetchedHistory.getSize()>0)
									{
										File d=fetchedHistory.getElement(0);
										if(d.getName().contains(accNo.toString()))
										{
											d.delete();
										}

										fetchedHistory.updateFetchedFileList();
										d=fetchedHistory.getElement(0);
										if(d.getName().contains(accNo.toString()))
										{
											d.delete();
										}
									}
									JOptionPane.showMessageDialog(app,"Files for accession number "+accNo+" not downloaded completely.Incomplete files are deleted.");
								}
							});

							dialog.setVisible(true);
							dialog.setLocation(maxScreenWidth/2,maxScreenHeight/2);
							fetchThread ft=new fetchThread(dialog,label,accNo);
							fetch1=new Thread(ft);
							fetch1.start();
						}
					}
				}
			}
			catch(NullPointerException np)
			{
				errorHandler(np);
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Manual Sequence" Button
	*******************************************************************************/
	static class manSeqListener implements ActionListener
    {
		public void actionPerformed(ActionEvent e)
        {
			FileWriter w;
			String temp = JOptionPane.showInputDialog (app,"Enter the sequence : ");

			if (temp == null )
            {
				// do something if required ! User has pressed cancel !
			}
            else if(temp.equals(""))
            {
            	JOptionPane.showMessageDialog(app,"No sequence entered!");
            }
            else
            {
            	boolean flagd=true;
            	File filename;
				char[] seq1 = temp.toCharArray();
                String name = JOptionPane.showInputDialog (app,"Enter the name for the file : user_");
                if (switchFlag)	// DNA Mode
               	{
               		filename = new File("../History/Fetched/user_"+name+".fasta");
                }
                else	// Protein Mode
                {
                	filename = new File("../History/Protein/user_"+name+".fasta");
                }
                try
				{
					String seqstring=new String(seq1);
					seqstring=seqstring.trim();
					seqstring=seqstring.replaceAll("\\s","");
					char[] seq=seqstring.toCharArray();
                	w = new FileWriter(filename.getCanonicalPath());
                    w.write(">This fasta file is generated by bioSpectrogram using the custom sequence.\n");
					for(int i=1 ; i <= seq.length; i++ )
                    {
						if ((i-1)%70 == 0 && i!=1)
                        {
							w.write("\n");
						}

						if (switchFlag)
						{
							// DNA mode
							if (seq[i-1]=='a'||seq[i-1]=='A'||seq[i-1]=='g'||seq[i-1]=='G'||seq[i-1]=='c'||seq[i-1]=='C'||seq[i-1]=='t'||seq[i-1]=='T')
                        	{
								// add more characters that can come in DNA sequence
                            	w.write(seq[i-1]);
							}
							else
							{
	                        	JOptionPane.showMessageDialog (app,"Please enter a valid DNA seqence !\n"+
                        			seq[i-1]+" is not allowed.");
                            	w.close();
								filename.delete();
								flagd=false;
                            	break;
							}
						}
						else
						{
							// Protein Mode
							boolean c1 = seq[i-1]=='a'||seq[i-1]=='A'||seq[i-1]=='c'||seq[i-1]=='C'||seq[i-1]=='d'||seq[i-1]=='D'||seq[i-1]=='e'||seq[i-1]=='E';
							boolean c2 = seq[i-1]=='f'||seq[i-1]=='F'||seq[i-1]=='g'||seq[i-1]=='G'||seq[i-1]=='h'||seq[i-1]=='H'||seq[i-1]=='i'||seq[i-1]=='I';
							boolean c3 = seq[i-1]=='k'||seq[i-1]=='K'||seq[i-1]=='l'||seq[i-1]=='L'||seq[i-1]=='m'||seq[i-1]=='M'||seq[i-1]=='n'||seq[i-1]=='N';
							boolean c4 = seq[i-1]=='p'||seq[i-1]=='P'||seq[i-1]=='q'||seq[i-1]=='Q'||seq[i-1]=='r'||seq[i-1]=='R'||seq[i-1]=='s'||seq[i-1]=='S';
							boolean c5 = seq[i-1]=='t'||seq[i-1]=='T'||seq[i-1]=='v'||seq[i-1]=='V'||seq[i-1]=='w'||seq[i-1]=='W'||seq[i-1]=='y'||seq[i-1]=='Y';
							if (c1 || c2 || c3 || c4 || c5)
                        	{
                            	w.write(seq[i-1]);
							}
							else
							{
	                        	JOptionPane.showMessageDialog (app,"Please enter a valid DNA seqence !\n"+
                        			seq[i-1]+" is not allowed.");
                            	w.close();
								filename.delete();
								flagd=false;
                            	break;
							}
						}

                        if (i == seq.length)
						{
                        	w.close ();
						}
					}
                    fetchedHistory.updateFileList();
                    proteinHistory.updateFileList();

					if(flagd)
					{
						prevtoptext=topLabel.getText();
						prevbottomtext=bottomLabel.getText();
						if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
						{
							backButton.setEnabled(false);
							backButton.setText("Back");
							back.setEnabled(false);
							back.setText("Back");
						}
						else
						{
							backButton.setEnabled(true);
							back.setEnabled(true);
						}

						if(switchFlag)
						{
							/*******************************************************************************
					 		*	Here getSequence(0,1) means that index 0 and flag 1
					 		*	Index 0 refer to top element in the array which is added latest
					 		*	Flag 1 means that we are looking into Fetched History
					 		*******************************************************************************/
							topLabel.setText(getSequence(0,1).toString());
							topLabel.updateUI();
                    		bottomLabel.setText("");
                    		bottomLabel.updateUI();
							JOptionPane.showMessageDialog(app,"File saved as History/Fetched/"+fetchedHistory.getElement(0).getName()+".");
						}
                    	else
                    	{
							/*******************************************************************************
					 		*	Here getSequence(0,4) means that index 0 and flag 4
					 		*	Index 0 refer to top element in the array which is added latest
					 		*	Flag 4 means that we are looking into Protein History
					 		*******************************************************************************/
							topLabel.setText(getSequence(0,4).toString());
							topLabel.updateUI();
                    		bottomLabel.setText("");
                    		bottomLabel.updateUI();
                    		JOptionPane.showMessageDialog(app,"File saved as History/Protein/"+proteinHistory.getElement(0).getName()+".");
                    	}
					}
				}
                catch (IOException ex)
				{
                    errorHandler(ex);
				}
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements Runnable Thread for "Encode" Button
	*******************************************************************************/
	static class encodeThread implements Runnable
	{
		public int encodingFlag,fetchFlag;
		int dnaind=0,m,n,quarternion;
		Complex z1,z2;
		char[] ary;
		double ra,rc,rg,rt;
		boolean realboolean;
		String name;
		JDialog dialog;
		JButton ok;
		boolean complexboolean, valid2;

		public encodeThread(int enc,int fetchF,JDialog dial,boolean realboolean,int dnaind,Complex z1,Complex z2,int m,int n,int quarternion,double ra,double rc,double rg,double rt,JButton ok,boolean valid2,boolean complexboolean)
		{
			this.valid2=valid2;
			this.ok=ok;
			fetchFlag=fetchF;encodingFlag=enc;  dialog=dial;
			this.realboolean=realboolean; this.dnaind=dnaind;this.z1=z1; this.z2=z2; this.m=m; this.n=n; this.quarternion=quarternion; this.ra=ra; this.rc=rc; this.rg=rg; this.rt=rt;
			this.complexboolean=complexboolean;
		}
		public void run()
		{
			try
			{
				ok.setEnabled(false);
				ok.setText("Encoding.....");
				//	boolean valid=true;
				if ( fetchFlag < fetchedHistory.getSize () )
				{
					try
					{
						switch (encodingFlag)
						{
							/*******************************************************************************
								*	Case statement works on the index of encoding scheme selected
								*	in the second Jcombobox
							*******************************************************************************/

							case 0:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{

									DNAindicator temp = new DNAindicator(fetchedHistory.getElement(fetchFlag),(dnaind+1));


									encodedHistory.updateFileList ();
									prevtoptext=topLabel.getText();
									prevbottomtext=bottomLabel.getText();

									if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
									{
										backButton.setEnabled(false);
										backButton.setText("Back");
										back.setEnabled(false);
										back.setText("Back");
									}
									else
									{
										backButton.setEnabled(true);
										back.setEnabled(true);
									}

									topLabel.setText (getSequence(fetchFlag,1).toString());
									topLabel.updateUI ();

									bottomLabel.setText (getSequence(0,2).toString());
									bottomLabel.updateUI ();
									JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

								}
								break;
							}

							case 1:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									//JOptionPane.showMessageDialog (app,nextVersion);

									Tetrahedron temp = new Tetrahedron(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}

								}
								break;
							}

							case 2:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									//JOptionPane.showMessageDialog (app,nextVersion);

									ZCurve temp = new ZCurve(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}

								}
								break;
							}

							case 3:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									DVCurve temp = new DVCurve(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							case 4:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									ComplexEncod temp = new ComplexEncod(fetchedHistory.getElement (fetchFlag));
									boolean valid=true;
									if(complexboolean)
									valid = temp.encode1(z1,z2);
									else
									valid=temp.encode2();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							case 5:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									ComplexEncod2 temp = new ComplexEncod2(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							case 6:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									RandomComplex temp = new RandomComplex(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							case 7:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									GraphicalEncod temp = new GraphicalEncod(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode1(m,n);
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							case 8:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									YauAtElGraphical temp = new YauAtElGraphical(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							case 9:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									RandomGraphical temp = new RandomGraphical(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}
							case 10:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									QuaternionEncod temp = new QuaternionEncod(fetchedHistory.getElement (fetchFlag));
									boolean valid=true;
									if(quarternion==0)
									valid = temp.encode3();
									else if(quarternion==1)
									valid = temp.encode4();

									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							case 11:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									RealMappingEncod temp = new RealMappingEncod(fetchedHistory.getElement (fetchFlag));
									boolean valid=true;
									if(realboolean)
									valid = temp.encode2(ra,rc,rg,rt);
									else valid=temp.encode1();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							case 12:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									ElectroIonIntEncod temp = new ElectroIonIntEncod(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							case 13:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									RandomReal temp = new RandomReal(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							case 14:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									QRMEncod_1 temp = new QRMEncod_1(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence(fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							case 15:
							{
								if (fetchedHistory.getElement (fetchFlag).getCanonicalPath().contains("genbank"))
								{
									JOptionPane.showMessageDialog (app,nextVersion);
								}
								else
								{
									QRMEncod_2 temp = new QRMEncod_2(fetchedHistory.getElement (fetchFlag));
									boolean valid = temp.encode();
									if (valid)
									{
										encodedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence (fetchFlag,1).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence(0,2).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Encoding file History/Encoded/"+encodedHistory.getElement(0).getName()+" generated successfully.");

									}
								}
								break;
							}

							default:
							{
								break;
							}
						}
					}
					catch(IOException ioe)
					{
						errorHandler(ioe);
					}
				}
				dialog.dispose();
				Runtime rt = Runtime.getRuntime();
				//rt.gc();
			}
			catch(OutOfMemoryError e)
			{
				//in.close();
				Runtime rt = Runtime.getRuntime();
				//rt.gc();
				e.printStackTrace(System.out);
				JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
					"If software seems to be hung after pressing OK, minize & maximize :)");
				dialog.dispose();
				if(encode1.isAlive())
					encode1.interrupt();
				//StringBuffer tempBuf = new StringBuffer(1);
				//tempBuf.append(" ");
				//return tempBuf;
			}
			catch(NumberFormatException nfe)
			{
				JOptionPane.showMessageDialog(app,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
			}
			catch(NullPointerException npe)
			{
				errorHandler(npe);
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Encode" Button
	*******************************************************************************/
	static class encodeListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String[] encodingList;
			if (switchFlag)
			{
				fetchedHistory.updateFileList();
			}
			else
			{
				proteinHistory.updateFileList();
			}

			encodedHistory.updateEncodedFileList();

			final JComboBox fetchedfiles;
			final JButton okButton = new JButton("OK");
			final JButton cancelButton = new JButton("Cancel");
			final JDialog dialog = new JDialog(app,"Encoding Dialog",JDialog.ModalityType.DOCUMENT_MODAL);
			final JPanel panel1 = new JPanel();

			if (switchFlag)
			{
				encodingList= new String[]
				{
					"Indicator Encoding",
					"TetraHedron Encoding",
					"Z curve Encoding",
					"DV curve Encoding",
					"Complex Encoding 1 & User choice",
					"Complex Encoding 2",
					"Random Complex Encoding",
					"Graphical Encoding 1",
					"Graphical Encoding 2",
					"Random Graphical Encoding",
					"Quaternion Encoding 1 & 2",
					"Real Value Encoding",
					"Electron Ion Encoding",
					"Random Real Value Encoding",
					"Quaternary Integer Mapping 1",
					"Quaternary Integer Mapping 2",
				};
			}
			else
			{
				encodingList= new String[]
				{
					"Indicator Encoding",
					"Electron Ion Encoding",
					"Random Real Value Encoding & User Choice",
				};
			}

			JLabel label1 = new JLabel("      Select File for encoding     :   ");
			JLabel label2 = new JLabel("      Select Encoding Scheme    :   ");


			if (switchFlag)
			{
				fetchedfiles = new JComboBox(fetchedHistory.getArray());
			}
			else
			{
				fetchedfiles = new JComboBox(proteinHistory.getArray());
			}

			final JComboBox encodings = new JComboBox(encodingList);
			fetchFlag=0;

			// Action Listener for JCoombobox for choosing fetech file
			fetchedfiles.addActionListener (new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JComboBox temp = (JComboBox) e.getSource ();
					fetchFlag = temp.getSelectedIndex ();
				}
			});

			encodingFlag=0;

			// Action Listener for JCoombobox for choosing encoding scheme
			encodings.addActionListener ( new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JComboBox temp = (JComboBox) e.getSource ();
					encodingFlag = temp.getSelectedIndex ();
				}
			});

			// Action Listener for "OK" button
			okButton.addActionListener (new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (switchFlag)
					{
						try
						{
							/*******************************************************************************
						 	*	Fasta File Creation
							*******************************************************************************/

							int dnaind=0;
							Complex z1=new Complex(0,0);
							Complex z2=new Complex(0,0);
							int m=0,n=0,quarternion=0,flag1=1;
							double ra=0,rc=0,rg=0,rt=0,m2=0,n2=0,rz1,cz1,rz2,cz2;
							boolean realboolean=false,valid2=true,complexboolean=false;

							if (fetchedHistory.getSize() == 0)
							{
								JOptionPane.showMessageDialog (app,"There are no fetched files!");
							}
							else
							{
								try
								{
									if (fetchedHistory.getElement(fetchFlag).getCanonicalPath().contains("genbank"))
									{
										JOptionPane.showMessageDialog (app,nextVersion);
										valid2=false;
									}
								}
								catch(IOException ioe)
								{
									errorHandler(ioe);
								}

								String name = fetchedHistory.getElement(fetchFlag).getName();
								if(encodingFlag==0 && valid2)
								{
									String en=JOptionPane.showInputDialog(app,"Enter the character to be encoded:(A/C/G/T)");
									if(en==null || en.equals(""))
									{
										JOptionPane.showMessageDialog(app,"No character entered!");
										valid2=false;
									}
									else if(en.equalsIgnoreCase("a"))
									{
										dnaind=0;
									}
									else if(en.equalsIgnoreCase("c"))
									{
										dnaind=1;
									}
									else if(en.equalsIgnoreCase("g"))
									{
										dnaind=2;
									}
									else if(en.equalsIgnoreCase("t"))
									{
										dnaind=3;
									}
									else
									{
										JOptionPane.showMessageDialog(app,"Invalid character entered!");
										valid2=false;
									}
								}
								if(encodingFlag==4 && valid2)
								{
									complexboolean=false;
									String aa,bb,cc,dd;
									int option;
									option = JOptionPane.showConfirmDialog(app,"Do you want to enter complex numbers of your choice for encoding?","",JOptionPane.YES_NO_OPTION);
									if (option == 0)
									{
										aa = JOptionPane.showInputDialog (app,"Enter real value for z1 ");
										bb = JOptionPane.showInputDialog (app,"Enter imaginary value for z1");
										cc = JOptionPane.showInputDialog (app,"Enter real value for z2");
										dd = JOptionPane.showInputDialog (app,"Enter imaginary value for z2");
										rz1 = Double.parseDouble (aa);
										cz1 = Double.parseDouble (bb);
										rz2 = Double.parseDouble (cc);
										cz2 = Double.parseDouble (dd);
										z1=new Complex(rz1,cz1);
										z2=new Complex(rz2,cz2);
										flag1=1;
										complexboolean=true;
									}
									else
									{
										complexboolean=false;
									}
								}
								if(encodingFlag==7 && valid2)
								{
									String a1,b1;

									// prompt user to enter values of m and n
									a1 = JOptionPane.showInputDialog (app,"Enter integer value of m");
									b1 = JOptionPane.showInputDialog (app,"Enter integer value of n");

									// proceed with the encoding only if valid values of m and n are given
									if (a1 == null || b1 == null || a1.equals("") || b1.equals(""))
									{
										JOptionPane.showMessageDialog (app,"Please enter proper sequence !");
										flag1=0;
										valid2=false;
									}
									else
									{
										m = Integer.parseInt(a1);
										n = Integer.parseInt(b1);
									}
								}
								if(encodingFlag==10 && valid2)
								{
									//String a11;
									//a11 = JOptionPane.showInputDialog (app,"Enter 0 for 3-bit, 1 for 4-bit:");
									//quarternion=Integer.parseInt(a11);
									quarternion = JOptionPane.showConfirmDialog(app,"Do you want to use Quaternion 1?(No means Quaternion 2)","",JOptionPane.YES_NO_OPTION);
									if(quarternion!=0 && quarternion!=1)
									{
										JOptionPane.showMessageDialog (app,"Invalid flag entered");
										valid2=false;
									}
								}
								if(encodingFlag==11 && valid2)
								{
									realboolean=false;
									String aa,bb,cc,dd;
									int option;
									option = JOptionPane.showConfirmDialog(app,"Do you want to enter real values of your choice?(y/n)","",JOptionPane.YES_NO_OPTION);
									if(option == 0)
									{
										aa = JOptionPane.showInputDialog (app,"Enter real value for A ");
										bb = JOptionPane.showInputDialog (app,"Enter real value for C");
										cc = JOptionPane.showInputDialog (app,"Enter real value for G");
										dd = JOptionPane.showInputDialog (app,"Enter real value for T");
										ra = Double.parseDouble(aa);
										rc = Double.parseDouble(bb);
										rg = Double.parseDouble(cc);
										rt = Double.parseDouble(dd);
										flag1=1;
										realboolean=true;
									}
									else
									{
										realboolean=false;
									}
								}
								if(valid2)
								{
									encodeThread et=new encodeThread(encodingFlag,fetchFlag,dialog,realboolean,dnaind,z1,z2,m,n,quarternion,ra,rc,rg,rt,okButton,valid2,complexboolean);
									encode1=new Thread(et);
									encode1.start();
								}
							}
						}
						catch(NumberFormatException nfe)
						{
							JOptionPane.showMessageDialog(app,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
						}
						catch(NullPointerException nul)
						{
							errorHandler(nul);
						}
					}
					else
					{
						if (proteinHistory.getSize() == 0)
						{
							JOptionPane.showMessageDialog (app,"There are no fetched files!");
						}
						else
						{
							try
							{
								switch(encodingFlag)
								{
									case 0:
									{
										if (proteinHistory.getElement(fetchFlag).getCanonicalPath().contains("genbank"))
										{
											JOptionPane.showMessageDialog(app,nextVersion);
										}
										else
										{

											PROindi temp = new PROindi(proteinHistory.getElement(fetchFlag));
											boolean valid = temp.encode();

											if (valid)
											{
												encodedHistory.updateFileList();
												prevtoptext=topLabel.getText();
												prevbottomtext=bottomLabel.getText();

												if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
												{
													backButton.setEnabled(false);
													backButton.setText("Back");
													back.setEnabled(false);
													back.setText("Back");
												}
												else
												{
													backButton.setEnabled(true);
													back.setEnabled(true);
												}

												topLabel.setText (getSequence(fetchFlag,4).toString());
												topLabel.updateUI ();

												bottomLabel.setText(getSequence(0,2).toString());
												bottomLabel.updateUI();
												JOptionPane.showMessageDialog(app,"Encoding file "+encodedHistory.getElement(0).getName()+" generated successfully.");
											}
										}
										break;
									}

									case 1:
									{
										if (proteinHistory.getElement(fetchFlag).getCanonicalPath().contains("genbank"))
										{
											JOptionPane.showMessageDialog(app,nextVersion);
										}
										else
										{
											PROelec temp = new PROelec(proteinHistory.getElement(fetchFlag));
											boolean valid = temp.encode();

											if (valid)
											{
												encodedHistory.updateFileList ();
												prevtoptext=topLabel.getText();
												prevbottomtext=bottomLabel.getText();

												if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
												{
													backButton.setEnabled(false);
													backButton.setText("Back");
													back.setEnabled(false);
													back.setText("Back");
												}
												else
												{
													backButton.setEnabled(true);
													back.setEnabled(true);
												}

												topLabel.setText (getSequence (fetchFlag,4).toString());
												topLabel.updateUI ();

												bottomLabel.setText (getSequence(0,2).toString());
												bottomLabel.updateUI ();
												JOptionPane.showMessageDialog(app,"Encoding file "+encodedHistory.getElement(0).getName()+" generated successfully.");

											}
										}
										break;
									}

									case 2:
									{
										if (proteinHistory.getElement(fetchFlag).getCanonicalPath().contains("genbank"))
										{
											JOptionPane.showMessageDialog(app,nextVersion);
										}
										else
										{
											PROreal temp = new PROreal(proteinHistory.getElement(fetchFlag));
											boolean valid = temp.encode();

											if (valid)
											{
												encodedHistory.updateFileList();
												prevtoptext=topLabel.getText();
												prevbottomtext=bottomLabel.getText();

												if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
												{
													backButton.setEnabled(false);
													backButton.setText("Back");
													back.setEnabled(false);
													back.setText("Back");
												}
												else
												{
													backButton.setEnabled(true);
													back.setEnabled(true);
												}

												topLabel.setText (getSequence (fetchFlag,4).toString());
												topLabel.updateUI ();

												bottomLabel.setText (getSequence(0,2).toString());
												bottomLabel.updateUI ();
												JOptionPane.showMessageDialog(app,"Encoding file "+encodedHistory.getElement(0).getName()+" generated successfully.");
											}
										}
										break;
									}
								}
							}
							catch(IOException ioe)
							{
								errorHandler(ioe);
							}
						}
						dialog.dispose();
					}
				}
			});

			// Action Listener for "CANCEL" button
			cancelButton.addActionListener (new ActionListener()
			{
				public  void actionPerformed(ActionEvent e)
				{
					if(encode1.isAlive())
					encode1.interrupt();

					encodedHistory.updateEncodedFileList();
					if(encodedHistory.getSize()>0 && !okButton.isEnabled())
					{
						prevtoptext=topLabel.getText();
						prevbottomtext=bottomLabel.getText();

						if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
						{
							backButton.setEnabled(false);
							backButton.setText("Back");
							back.setEnabled(false);
							back.setText("Back");
						}
						else
						{
							backButton.setEnabled(true);
							back.setEnabled(true);
						}

						topLabel.setText(getSequence(fetchFlag,1).toString());
						topLabel.updateUI();
						bottomLabel.setText(" ");
						bottomLabel.updateUI();
						File d=encodedHistory.getElement(0);
						encodedHistory.updateEncodedFileList();
						JOptionPane.showMessageDialog(app,"Encoding of "+fetchedHistory.getElement(fetchFlag).getName()+" not completed.");
					}
					dialog.dispose();
				}
			});

			panel1.setLayout (new GridLayout(3,2));
			panel1.add(label1);
			panel1.add(fetchedfiles);
			panel1.setBackground (Color.WHITE);
			panel1.setBorder (new EtchedBorder(EtchedBorder.RAISED));
			panel1.add(label2);
			panel1.add(encodings);
			panel1.add(okButton);
			panel1.add(cancelButton);
			dialog.getContentPane ().add(panel1);
			dialog.setBounds(maxScreenWidth/3,maxScreenHeight/3,500,150);
			dialog.setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);

			dialog.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					if(encode1.isAlive())
					{
						encode1.interrupt();
					}
					encodedHistory.updateEncodedFileList();

					if(encodedHistory.getSize()>0 && !okButton.isEnabled())
					{
						prevtoptext=topLabel.getText();
						prevbottomtext=bottomLabel.getText();
						if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
						{
							backButton.setEnabled(false);
							backButton.setText("Back");
							back.setEnabled(false);
							back.setText("Back");
						}
						else
						{
							backButton.setEnabled(true);
							back.setEnabled(true);
						}

						topLabel.setText(getSequence(fetchFlag,1).toString());
						topLabel.updateUI();
						bottomLabel.setText(" ");
						bottomLabel.updateUI();
						File d=encodedHistory.getElement(0);
						encodedHistory.updateEncodedFileList();
						JOptionPane.showMessageDialog(app,"Encoding of "+fetchedHistory.getElement(fetchFlag).getName()+" not completed.");
					}
					dialog.dispose();
				}
			});
			dialog.setVisible (true);
		}
	}

	/*******************************************************************************
	 *	This static class implements Runnable Thread for "Transform" Button
	*******************************************************************************/
	static class transformThread implements Runnable
	{
		int encodingFlag,transformationFlag;
		JDialog dialog;
		JButton ok;

		public transformThread(int encodingFlag,int transformationFlag,JDialog dialog,JButton ok)
		{
			this.encodingFlag=encodingFlag;
			this.transformationFlag=transformationFlag;
			this.dialog=dialog;
			this.ok=ok;
		}

		public void run()
		{
			try
			{
				ok.setText("Transforming...");
				if (encodingFlag < encodedHistory.getSize())
				{
					String filename1 = encodedHistory.getElement (encodingFlag).getName ();
					if(filename1.contains("E01.2") || filename1.contains("E01.3"))
					{
						JOptionPane.showMessageDialog (app,"Transformations for ZCurve and Tetrahedron encoding will be available in the next version.");
						dialog.dispose();
					}
					else if(filename1.contains("E03") || filename1.contains("E04"))
					{
						JOptionPane.showMessageDialog (app,"Transformations for Graphical encoding and Quarternion encoding will be available in the next version.");
						dialog.dispose();
					}
					else
					{
						switch(transformationFlag)
                        {
							case 0:
                            {
                                //2 represents that we need to get sequence from encoded file
                                String t = getOnlySequence(encodingFlag,2).toString();
                                t = t.trim();
                                t = t.replaceAll("N","").replaceAll("Y","").replaceAll("S","");
                                t = t.replaceAll("W","").replaceAll("K","").replaceAll("M","");
                                t = t.replaceAll("B","").replaceAll("D","").replaceAll("H","");
                                t = t.replaceAll("V","").replaceAll("-","").replaceAll("\\.","");
                                t = t.replaceAll("\n","");
                                String s = t;

                                String filename = encodedHistory.getElement (encodingFlag).getName ();
                                if (filename.contains("E01")||filename.contains("E02")||filename.contains("E05")||filename.contains("E06")||filename.contains("E07"))
                                {
                                	filename = filename.substring (0,encodedHistory.getElement (encodingFlag).getName().lastIndexOf ("."))+"_T01.fasta";
                                	filename = "../History/Transformed/"+filename;
									try
                                	{
										FileWriter writer = new FileWriter(filename);
                                    	Complex[] output = FFT.FFToutput(s);
										int i=0;
                                    	for(i=0;i<output.length;i++)
										{
	                                    	writer.write(output[i].toString ());
										}
                                    	writer.close();
                                    	transformedHistory.updateFileList ();
                                    	prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

                                    	topLabel.setText (getSequence (encodingFlag,2).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence (0,3).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Transformation file History/Transformed/"+transformedHistory.getElement(0).getName()+" generated successfully.");
									}
                                	catch (IOException ea)
									{
                                    	errorHandler(ea);
									}
									transformedHistory.updateFileList ();
								}
                                else
                                {
                                	JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding Scheme !");
                                	dialog.dispose();
								}
                                break;
							}

                            case 1:
                            {
								String t = getOnlySequence(encodingFlag,2).toString();
                                t = t.trim();
                                t = t.replaceAll("N","").replaceAll("Y","").replaceAll("S","");
                                t = t.replaceAll("W","").replaceAll("K","").replaceAll("M","");
                                t = t.replaceAll("B","").replaceAll("D","").replaceAll("H","");
                                t = t.replaceAll("V","").replaceAll("-","").replaceAll("\\.","");
                                t = t.replaceAll("\n","");
                                String s = t;

                                Hilbert temp = new Hilbert(s);
								String filename = encodedHistory.getElement (encodingFlag).getName ();
								if (filename.contains("E01")||filename.contains("E05")||filename.contains("E06")||filename.contains("E07"))
								{
									filename = filename.substring (0,encodedHistory.getElement (encodingFlag).getName().lastIndexOf ("."))+"_T02.fasta";
                                	filename = "../History/Transformed/"+filename;
									try
                                	{
										FileWriter writer = new FileWriter(filename);
                                    	Complex[] output = temp.HilbertOutput();
										for(int i=0;i<output.length;i++)
										{
	                                    	writer.write(output[i].toString ());
										}
                                    	writer.close();
                                    	transformedHistory.updateFileList ();
                                    	prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

    									topLabel.setText (getSequence (encodingFlag,2).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence (0,3).toString());
										bottomLabel.updateUI ();
    									JOptionPane.showMessageDialog(app,"Transformation file History/Transformed/"+transformedHistory.getElement(0).getName()+" generated successfully.");
									}
                                	catch (IOException ea)
									{
                                    	errorHandler(ea);
									}
									transformedHistory.updateFileList ();
								}
								else
								{
									JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding Scheme !");
									dialog.dispose();
								}
								break;
							}

                            case 2:
                            {
								// should only be applied to complex numbered sequence
                                String t = getOnlySequence(encodingFlag,2).toString();
                                t = t.trim();
                                t = t.replaceAll("N","").replaceAll("Y","").replaceAll("S","");
                                t = t.replaceAll("W","").replaceAll("K","").replaceAll("M","");
                                t = t.replaceAll("B","").replaceAll("D","").replaceAll("H","");
                                t = t.replaceAll("V","").replaceAll("-","").replaceAll("\\.","");
                                t = t.replaceAll("\n","");
                                String s = t;

								String a,b;
                                Integer m,n;
								a = JOptionPane.showInputDialog (app,"Enter real value of Z");
                                b = JOptionPane.showInputDialog (app,"Enter imaginary value of Z");
								if (a==null || b==null)
                                {
									JOptionPane.showMessageDialog (app,"Please enter proper complex value !");
								}
                                else
                                {
									m = Integer.parseInt(a);
									n = Integer.parseInt(b);
                                    ZTransform temp = new ZTransform(s,new Complex(m,n));
									String filename = encodedHistory.getElement (encodingFlag).getName ();
									if (filename.contains("E01")||filename.contains("E02")||filename.contains("E05")||filename.contains("E06")||filename.contains("E07"))
									{
										filename = filename.substring (0,encodedHistory.getElement (encodingFlag).getName().lastIndexOf ("."))+"_T03.fasta";
										filename = "../History/Transformed/"+filename;
										try
                                    	{
											FileWriter writer = new FileWriter(filename);
                                        	writer.write(temp.ZTransformed ().toString ());
											writer.close();
											transformedHistory.updateFileList ();
											prevtoptext=topLabel.getText();
											prevbottomtext=bottomLabel.getText();

											if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
											{
												backButton.setEnabled(false);
												backButton.setText("Back");
												back.setEnabled(false);
												back.setText("Back");
											}
											else
											{
												backButton.setEnabled(true);
												back.setEnabled(true);
											}

											topLabel.setText (getSequence (encodingFlag,2).toString());
											topLabel.updateUI ();

											bottomLabel.setText (getSequence (0,3).toString());
											bottomLabel.updateUI ();
											JOptionPane.showMessageDialog(app,"Transformation file History/Transformed/"+transformedHistory.getElement(0).getName()+" generated successfully.");
										}
										catch (IOException ea)
                                    	{
                                        	errorHandler(ea);
										}
										transformedHistory.updateFileList ();
									}
									else
									{
										JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding Scheme !");
										dialog.dispose();
									}
								}
                                break;
							}

                            case 3:
							{
                            	// should only be applied to real numbered sequence
                                String t = getOnlySequence(encodingFlag,2).toString();
                                t = t.trim();
                                t = t.replaceAll("N","").replaceAll("Y","").replaceAll("S","");
                                t = t.replaceAll("W","").replaceAll("K","").replaceAll("M","");
                                t = t.replaceAll("B","").replaceAll("D","").replaceAll("H","");
                                t = t.replaceAll("V","").replaceAll("-","").replaceAll("\\.","");
                                t = t.replaceAll("\n","");
                                String s = t;

								Hilbert temp = new Hilbert(s);

                                String filename = encodedHistory.getElement (encodingFlag).getName ();
                                if (filename.contains("E01")||filename.contains("E05")||filename.contains("E06")||filename.contains("E07"))
                                {
                                	filename = filename.substring (0,encodedHistory.getElement (encodingFlag).getName().lastIndexOf ("."))+"_T04.fasta";
                                	filename = "../History/Transformed/"+filename;
									try
                                	{
										FileWriter writer = new FileWriter(filename);
                                    	Complex[] output = temp.AnalyticSignal();
										for(int i=0;i<output.length;i++)
                                    	{
											writer.write(output[i].toString ());
										}
										writer.close();
										transformedHistory.updateFileList ();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence (encodingFlag,2).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence (0,3).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Transformation file History/Transformed/"+transformedHistory.getElement(0).getName()+" generated successfully.");
									}
	                                catch (IOException ea)
									{
										errorHandler(ea);
									}
									transformedHistory.updateFileList ();
								}
                                else
                                {
                                	JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding Scheme !");
                                	dialog.dispose();
								}
								break;
							}

                            case 4:
                            {
								// should only be applied to real numbered sequence
                                String t = getOnlySequence(encodingFlag,2).toString();
                                t = t.trim();
                                t = t.replaceAll("N","").replaceAll("Y","").replaceAll("S","");
                                t = t.replaceAll("W","").replaceAll("K","").replaceAll("M","");
                                t = t.replaceAll("B","").replaceAll("D","").replaceAll("H","");
                                t = t.replaceAll("V","").replaceAll("-","").replaceAll("\\.","");
                                t = t.replaceAll("\n","");
                                String s = t;

								DWavelet temp = new DWavelet();
                                String filename = encodedHistory.getElement (encodingFlag).getName ();
                                if (filename.contains("E01")||filename.contains("E05")||filename.contains("E06")||filename.contains("E07"))
                                {
                                	filename = filename.substring (0,encodedHistory.getElement (encodingFlag).getName().lastIndexOf ("."))+"_T05.fasta";
									filename = "../History/Transformed/"+filename;
                                	try
                                	{
										FileWriter writer = new FileWriter(filename);
                                    	double[] output=temp.discreteHaarWaveletTransformoutput(s);
                                    	String st="";
                                    	for(int i=0;i<output.length;i++)
                                    	{
											st = Double.toString (output[i])+"\n";
											writer.write(st);
										}
                                    	writer.close ();
                                    	transformedHistory.updateFileList ();
                                    	prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

                                    	topLabel.setText (getSequence (encodingFlag,2).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence (0,3).toString());
										bottomLabel.updateUI ();
                                    	JOptionPane.showMessageDialog(app,"Transformation file History/Transformed/"+transformedHistory.getElement(0).getName()+" generated successfully.");
									}
                                	catch (IOException ea)
									{
                                    	errorHandler(ea);
									}
									transformedHistory.updateFileList ();
								}
                                else
                                {
                                	JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding Scheme !");
                                	dialog.dispose();
								}
                                break;
							}

                            case 5:
                            {
                                String t = getOnlySequence(encodingFlag,2).toString();
                                t = t.trim();
                                t = t.replaceAll("N","").replaceAll("Y","").replaceAll("S","");
                                t = t.replaceAll("W","").replaceAll("K","").replaceAll("M","");
                                t = t.replaceAll("B","").replaceAll("D","").replaceAll("H","");
                                t = t.replaceAll("V","").replaceAll("-","").replaceAll("\\.","");
                                t = t.replaceAll("\n","");
                                String s = t;

                                String filename = encodedHistory.getElement(encodingFlag).getName();

                                ChirpZTransform temp = new ChirpZTransform();

                                if (filename.contains("E01")||filename.contains("E02")||filename.contains("E05")||filename.contains("E06")||filename.contains("E07"))
                                {
                                	filename = filename.substring (0,encodedHistory.getElement (encodingFlag).getName().lastIndexOf ("."))+"_T06.fasta";
									filename = "../History/Transformed/"+filename;
                                	try
                                	{
										FileWriter writer = new FileWriter(filename);
                                    	Complex[] output = temp.ChirpZTransformed(s);
										for(int i=0;i<output.length;i++)
                                    	{
											writer.write(output[i].toString ());
										}
										writer.close();
										transformedHistory.updateTransformedFileList();
										prevtoptext=topLabel.getText();
										prevbottomtext=bottomLabel.getText();

										if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
										{
											backButton.setEnabled(false);
											backButton.setText("Back");
											back.setEnabled(false);
											back.setText("Back");
										}
										else
										{
											backButton.setEnabled(true);
											back.setEnabled(true);
										}

										topLabel.setText (getSequence (encodingFlag,2).toString());
										topLabel.updateUI ();

										bottomLabel.setText (getSequence (0,3).toString());
										bottomLabel.updateUI ();
										JOptionPane.showMessageDialog(app,"Transformation file History/Transformed/"+transformedHistory.getElement(0).getName()+" generated successfully.");
									}
                                	catch (IOException ea)
									{
                                    	errorHandler(ea);
									}
									transformedHistory.updateFileList ();
								}
                                else
                                {
                                	JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding Scheme !");
                                	dialog.dispose();
								}
                                break;
							}

							case 6:
							{
								JOptionPane.showMessageDialog (app,"Discrete Cosine Transform will be available in the next version.");
							}

                            default:
                            {
								break;
							}
						}
					}
				}
				dialog.dispose();
				Runtime rt = Runtime.getRuntime();
				//rt.gc();
			}
			catch(OutOfMemoryError e)
			{
				//in.close();
				Runtime rt = Runtime.getRuntime();
				//rt.gc();
				e.printStackTrace(System.out);
				JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
					"If software seems to be hung after pressing OK, minize & maximize :)");
				dialog.dispose();
				if(transform1.isAlive())
					transform1.interrupt();
				//StringBuffer tempBuf = new StringBuffer(1);
				//tempBuf.append(" ");
				//return tempBuf;
			}
			catch(NumberFormatException nfe)
			{
				JOptionPane.showMessageDialog(app,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
			}
			catch(NullPointerException nu)
			{
				errorHandler(nu);
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Transform" Button
	*******************************************************************************/
	static class transformListener implements ActionListener
    {
		public void actionPerformed(ActionEvent e)
        {
        	encodedHistory.updateEncodedFileList();
        	transformedHistory.updateTransformedFileList();
			final JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("CANCEL");
			final JDialog dialog = new JDialog(app,"Transformation Dialog",JDialog.ModalityType.DOCUMENT_MODAL);
            JPanel panel1 = new JPanel();
			String[] transformationList = new String[]
            {
				"Fast Fourier Transform",
                "Hilbert Transform",
				"Z transform",
				"Analytic Signal Transform",
                "Discrete Haar Wavelet",
                "Chirp Z Transform",
                "Discrete Cosine Transformation"
			};

            JLabel label1 = new JLabel(" Select File for transformation : ");
            JLabel label2 = new JLabel(" Select Transformation Scheme   : ");

			final JComboBox encodings = new JComboBox(encodedHistory.getArray ());
            final JComboBox transformations = new JComboBox(transformationList);
			encodingFlag=0;
			// Action Listener for JCoombobox for choosing encoded file
			encodings.addActionListener (new ActionListener()
            {
				public void actionPerformed(ActionEvent e)
                {
					JComboBox temp = (JComboBox) e.getSource ();
                    encodingFlag = temp.getSelectedIndex ();
				}
			}
            );
			transformationFlag=0;
			// Action Listener for JCoombobox for choosing Transformation Scheme
			transformations.addActionListener (new ActionListener()
            {
				public void actionPerformed(ActionEvent e)
                {
					JComboBox b = (JComboBox) e.getSource ();
                    //String s = (String) b.getSelectedItem ();
					// Switch statement needed for selecting transformation scheme based on the index.
                    transformationFlag = b.getSelectedIndex ();
				}
			}
            );

			// Action Listener for "OK" button
			// NOTE: Need to add validation code before sending encoded file for transformation
            okButton.addActionListener (new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
                {
                	if (encodedHistory.getSize() == 0)
                	{
						JOptionPane.showMessageDialog (app,"There are no encoded files!");
						dialog.dispose();
                	}
                	else
                	{
                		try
	                	{
							okButton.setEnabled(false);
							transformThread tthread=new transformThread(encodingFlag,transformationFlag,dialog,okButton);
							transform1=new Thread(tthread);
							transform1.start();

						}
	                	catch(NumberFormatException nfe)
	                	{
	                		JOptionPane.showMessageDialog(app,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
						}
	                	catch(NullPointerException npe)
	                	{
	                		errorHandler(npe);
						}
                	}
				}
			});

			dialog.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					//System.out.println("closing...");
					if(transform1.isAlive())
					{
						transform1.interrupt();
					}

					transformedHistory.updateTransformedFileList();
					if(transformedHistory.getSize()>0 && !okButton.isEnabled())
					{
						prevtoptext=topLabel.getText();
						prevbottomtext=bottomLabel.getText();
						if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
						{
							backButton.setEnabled(false);
							backButton.setText("Back");
							back.setEnabled(false);
							back.setText("Back");
						}
						else
						{
							backButton.setEnabled(true);
							back.setEnabled(true);
						}

						topLabel.setText (getSequence (encodingFlag,2).toString());
						topLabel.updateUI ();

						bottomLabel.setText (" ");
						bottomLabel.updateUI ();

						File d=encodedHistory.getElement(encodingFlag);

						JOptionPane.showMessageDialog(app,"Transformation for encoding file "+d.getName()+" not completed.");
					}
					dialog.dispose();
				}
			});

			// Action Listener for "CANCEL" button
			cancelButton.addActionListener (new ActionListener()
            {
				public  void actionPerformed(ActionEvent e)
                {
                	if(transform1.isAlive())
					transform1.interrupt();
					transformedHistory.updateTransformedFileList();
					if(transformedHistory.getSize()>0 && !okButton.isEnabled())
					{
						prevtoptext=topLabel.getText();
						prevbottomtext=bottomLabel.getText();
						if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
						{
							backButton.setEnabled(false);
							backButton.setText("Back");
							back.setEnabled(false);
							back.setText("Back");
						}
						else
						{
							backButton.setEnabled(true);
							back.setEnabled(true);
						}

						topLabel.setText (getSequence (encodingFlag,2).toString());
						topLabel.updateUI ();

						bottomLabel.setText (" ");
						bottomLabel.updateUI ();

						File d=encodedHistory.getElement(encodingFlag);



						JOptionPane.showMessageDialog(app,"Transformation for encoding file "+d.getName()+" not completed.");
					}
					dialog.dispose ();
				}
			}
            );

            panel1.setLayout (new GridLayout(3,2));
			panel1.add(label1);
            panel1.add(encodings);
			panel1.setBackground (Color.WHITE);
            panel1.setBorder (new EtchedBorder(EtchedBorder.RAISED));
            panel1.add(label2);
			panel1.add(transformations);
            panel1.add (okButton);
            panel1.add(cancelButton);
			dialog.getContentPane ().add(panel1);
			dialog.setBounds (maxScreenWidth/3,maxScreenHeight/3,500,150);
            dialog.setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
		}
	}


	/*******************************************************************************
	 *	This static class implements Runnable Thread for "Window" Button
	*******************************************************************************/

	static class swindowThread implements Runnable
	{
    	int fetchFlag,startIndex,endIndex;
    	String s,reverss;
    	boolean valid;
    	JButton ok;
    	JDialog dialog;

		public swindowThread(int fetchFlag,int startIndex,int endIndex,String s,String reverss,boolean valid,JButton ok,JDialog dialog)
		{
    		this.fetchFlag=fetchFlag;
    		this.ok=ok;
    		this.dialog=dialog;
    		this.startIndex=startIndex;
    		this.endIndex=endIndex;
    		this.s=s;
    		this.reverss=reverss;
    		this.valid=valid;
		}

    	public void run()
    	{
    		try
    		{

				ok.setEnabled(false);
				ok.setText("Analysing...");
				/*******************************************************************************
					*	Fasta File Creation
				*******************************************************************************/

				String name = fetchedHistory.getElement(fetchFlag).getName();
				File fileName = new File("../History/Fetched/W_"+startIndex+"s_"+endIndex+"e_"+name);
				try
				{
					FileWriter fr = new FileWriter(fileName);
					fr.write(">This fasta file is generated by Window Analysis of bioSpectrogram.");
					char[] charArray = s.substring(startIndex-1,endIndex).toCharArray();
					for(int i=0;i<charArray.length;i++)
					{
						if (i%70 == 0)
						{
							fr.write("\n");
						}
						fr.write(charArray[i]);
					}
					fr.close();
					fetchedHistory.updateFetchedFileList ();

					prevtoptext=topLabel.getText();
					prevbottomtext=bottomLabel.getText();
					if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
					{
						backButton.setEnabled(false);
						backButton.setText("Back");
						back.setEnabled(false);
						back.setText("Back");
					}
					else
					{
						backButton.setEnabled(true);
						back.setEnabled(true);
					}

					topLabel.setText(getSequence(0,1).toString());
					topLabel.updateUI();
					bottomLabel.setText(" ");
					bottomLabel.updateUI();
				}
				catch(OutOfMemoryError e)
				{
					//in.close();
					Runtime rt = Runtime.getRuntime();
					//rt.gc();
					e.printStackTrace(System.out);
					JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
						"If software seems to be hung after pressing OK, minize & maximize :)");
					dialog.dispose();
					if(swindow1.isAlive())
						swindow1.interrupt();
					//StringBuffer tempBuf = new StringBuffer(1);
					//tempBuf.append(" ");
					//return tempBuf;
				}
				catch(IOException ioe)
				{
					errorHandler(ioe);
				}

				/*******************************************************************************
				*	Encoding of Fasta File using DNA indicator
				*******************************************************************************/

				DNAindicator temp = new DNAindicator(fileName,4);	// Indicator with T
				temp = new DNAindicator(fileName,3);	// Indicator with G
				temp = new DNAindicator(fileName,2);	// Indicator with C
				temp = new DNAindicator(fileName,1);	// Indicator with A

				encodedHistory.updateEncodedFileList();

				/*******************************************************************************
				*	Transformation of Encoded Fasta File using ChirpZTransform
				*	with parameters a = 1, u = 1, v = m = size of seqence
				*******************************************************************************/
				Complex[] array1,array2,array3,array4;

				String seq = getOnlySequence(3,2).toString().replaceAll("N","").trim().replaceAll("\n","");
				int lengthofseq=endIndex-startIndex+1;
				ChirpZTransform tempTrans = new ChirpZTransform(1,1,lengthofseq,lengthofseq);
				array1 = tempTrans.ChirpZTransformed(seq);
				seq = getOnlySequence(2,2).toString().replaceAll("N","").trim().replaceAll("\n","");
				tempTrans = new ChirpZTransform(1,1,lengthofseq,lengthofseq);
				array2 = tempTrans.ChirpZTransformed(seq);
				seq = getOnlySequence(1,2).toString().replaceAll("N","").trim().replaceAll("\n","");
				tempTrans = new ChirpZTransform(1,1,lengthofseq,lengthofseq);
				array3 = tempTrans.ChirpZTransformed(seq);
				seq = getOnlySequence(0,2).toString().replaceAll("N","").trim().replaceAll("\n","");
				tempTrans = new ChirpZTransform(1,1,lengthofseq,lengthofseq);
				array4 = tempTrans.ChirpZTransformed(seq);
				double[] finalArray = new double[array1.length];
				int maxIndex=1;

				for(int j=0;j < array1.length;j++)
				{
					finalArray[j] = array1[j].abs()*array1[j].abs();
					finalArray[j] = finalArray[j] + array2[j].abs()*array2[j].abs();
					finalArray[j] = finalArray[j] + array3[j].abs()*array3[j].abs();
					finalArray[j] = finalArray[j] + array4[j].abs()*array4[j].abs();
					if ( finalArray[j] > finalArray[maxIndex] && j != 0)
					{
						maxIndex = j;
					}
				}

				String filename = fetchedHistory.getElement(0).getName();
				filename = filename.substring (0,fetchedHistory.getElement(0).getName().lastIndexOf ("."))+"_W01.fasta";
				filename = "../History/Transformed/"+filename;
				try
				{
					FileWriter writer = new FileWriter(filename);
					for(int i=1;i<finalArray.length;i++)
					{
						writer.write(Double.toString(finalArray[i]));
						writer.write("\n");
					}
					writer.close();
					transformedHistory.updateTransformedFileList();
					int tempIndex = (int) finalArray.length/3;
					int temparrayval=(int)finalArray[maxIndex]*100;

					finalArray[maxIndex]=temparrayval/100;
					temparrayval=(int)finalArray[tempIndex]*100;
					finalArray[tempIndex]=temparrayval/100;
					temparrayval=(int)finalArray[tempIndex-1]*100;
					finalArray[tempIndex-1]=temparrayval/100;
					temparrayval=(int)finalArray[tempIndex+1]*100;
					finalArray[tempIndex+1]=temparrayval/100;
					if ( finalArray[maxIndex] == finalArray[tempIndex] && finalArray[tempIndex-1]<finalArray[tempIndex] && finalArray[tempIndex+1]<finalArray[tempIndex])
					{
						//Gene found
					}
				}
				catch(IOException exp)
				{
					errorHandler(exp);
				}

				/*******************************************************************************
					*	Lets create MATLAB file for final output.
				*******************************************************************************/

				String transName = fetchedHistory.getElement(0).getName();
				transName = transName.substring (0,fetchedHistory.getElement(0).getName().lastIndexOf ("."))+"_W01.fasta";
				String plotfile = transName.replace('.','_');
				plotfile = plotfile.concat(".m");
				String[] fname=plotfile.split(endIndex+"e_");
				String titlefile=fname[1];
				fname=titlefile.split("_W01");
				titlefile=fname[0];
				plotfile = "../History/Matlab_Files/"+plotfile;
				try
				{
					FileWriter w = new FileWriter(plotfile);
					w.write("% This Matlab script is generated using BioSpectrogram.\n");
					w.write("figure;\n");
					w.write("val = importdata('"+transformedHistory.getElement(0).getCanonicalPath()+"','%s');\n");
					int len=finalArray.length-1;

					w.write("w=linspace(2*pi/"+len+",2*pi,"+len+");\n");
					w.write("stem(w,val);\n");
					w.write("xlabel('Angular Frequency');\n");
					w.write("ylabel('Sum of power spectra of indicator sequences');\n");
					w.write("title(sprintf('");
					if(titlefile.contains("W_") || titlefile.contains("user_"))
					{
						w.write("File: "+titlefile+".fasta");
					}
					else
					{
						w.write("Accession Number: "+titlefile);
					}
					w.write(" \\n  Sum of Power spectrum of all indicator sequences \\n Subsequence: Starting index:"+startIndex+" , Ending index:"+endIndex+" \\n Generated using BioSpectrogram and Matlab'),'Interpreter','none');\n");
					w.close();
					JOptionPane.showMessageDialog(app,"Matlab script file   \""+plotfile+"\"   successfully generated ! \n Subsequence of  \""+name+"\"  from index "+startIndex+" to "+endIndex+"  is saved as  \" History/Fetched/W_"+startIndex+"s_"+endIndex+"e_"+name+"\".\n Indicator sequences for the subsequence are saved in  \"History/Encoded\" folder and \n output of sum of power spectra of indicator sequences is saved as \"History/Transformed/W_"+startIndex+"s_"+endIndex+"e_"+name.substring(0,name.lastIndexOf('.'))+"_W01.fasta.\" ");
				}
				catch (IOException ea)
				{
					errorHandler(ea);
				}
				dialog.dispose();
				Runtime rt = Runtime.getRuntime();
								//rt.gc();
			}
			catch(OutOfMemoryError e)
			{
				//in.close();
				Runtime rt = Runtime.getRuntime();
				//rt.gc();
				e.printStackTrace(System.out);
				JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
					"If software seems to be hung after pressing OK, minize & maximize :)");
				dialog.dispose();
				if(swindow1.isAlive())
					swindow1.interrupt();
				//StringBuffer tempBuf = new StringBuffer(1);
				//tempBuf.append(" ");
				//return tempBuf;
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Window" Button
	*******************************************************************************/
	static class windowListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
		{
			final JDialog dialog = new JDialog(app,"Windowing Dialog",JDialog.ModalityType.DOCUMENT_MODAL);
            final JPanel panel = new JPanel();
			final JButton ok = new JButton("OK");
            final JButton cancel = new JButton("CANCEL");

			JLabel label1 = new JLabel(" Select file for window analysis : ");
			JLabel label2 = new JLabel(" Select Scheme : ");
			String[] options = {"Sliding Window Analysis","Stagnant Window Analysis","C Yin,Yau Gene Prediction"};
			fetchedHistory.updateFetchedFileList();
            final JComboBox fetchedfiles = new JComboBox(fetchedHistory.getArray ());
            final JComboBox optionSelector = new JComboBox(options);
			fetchFlag=0;
            fetchedfiles.addActionListener (new ActionListener()
			{
            	public void actionPerformed(ActionEvent e)
                {
					JComboBox temp = (JComboBox) e.getSource ();
                    fetchFlag = temp.getSelectedIndex ();
				}
			});

			slideWindow=true;stagnantWindow=false;genePrediction=false;
            optionSelector.addActionListener (new ActionListener()
			{
            	public void actionPerformed(ActionEvent e)
                {
					JComboBox temp = (JComboBox) e.getSource ();
                    if (temp.getSelectedIndex () == 0)
                    {
                    	slideWindow = true;
                    	stagnantWindow=false; genePrediction=false;
					}
                    else if(temp.getSelectedIndex()==1)
                    {
                    	slideWindow = false;
                    	stagnantWindow=true; genePrediction=false;
					}
                    else
                    {
                    	slideWindow = false;
                    	stagnantWindow=false; genePrediction=true;

					}
				}
			}
            );

			ok.addActionListener (new ActionListener()
            {
				public void actionPerformed(ActionEvent e)
                {
                	try
                	{
						if(fetchedHistory.getSize()==0)
						{
							JOptionPane.showMessageDialog(app,"There are no fetched files!");
						}
						else
						{
							boolean condition = true;
							try
							{
								condition = fetchedHistory.getElement(fetchFlag).getCanonicalPath().contains(".genbank");
							}
							catch(IOException ioe)
							{
								errorHandler(ioe);
							}
							if(condition)
							{
								JOptionPane.showMessageDialog(app,nextVersion);
							}
							else
							{
								//System.out.println("Inside oklistener..\n");
								String s = getOnlySequence(fetchFlag,1).toString().replaceAll("N","").trim().replaceAll("\n","");
								//System.out.println("s is :\n"+s);
								StringBuffer reverses=new StringBuffer(s);
								reverses=reverses.reverse();
								String reverss=reverses.toString();
								String[] optionwindow={"Forward Sliding Window","Backward Sliding Window"};

								int window;
								String s1 = "There are "+s.length()+" characters in this DNA file !";
								JOptionPane.showMessageDialog (app,s1);
								if (genePrediction)
								{
									String a = JOptionPane.showInputDialog (app,"Enter window size : ");
									try
									{
										window = Integer.parseInt(a);
										if (window%3 == 0 )
										{
											if (window%3 == 0 && window < s.length())
											{
												RunnableThread2 r2=new RunnableThread2(fetchFlag,window,s,ok,dialog);
												t2=new Thread(r2,"Thread2");
												t2.start();
											}
											else
											{
												JOptionPane.showMessageDialog(app,"Size must be lesser than total size and a multiple of 3 !");
											}
										}
										else
										{
											JOptionPane.showMessageDialog(app,"for gene finding, window size should be multiple of 3");
										}
									}
									catch(NumberFormatException nfe)
									{
										JOptionPane.showMessageDialog(app,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
									}
								}
								else if(stagnantWindow)
								{
									//System.out.println("Inside Stagnant Window: \n");
									int startIndex, endIndex;
									String message = "\nMinimum Start Index can be 1";
									String a = JOptionPane.showInputDialog (app,"Enter Starting Index : "+message);
									message = "\nMaximum End Index can be total characters ";
									String b = JOptionPane.showInputDialog (app,"Enter Ending Index   : "+message);

									//provide option to quit
									int option = JOptionPane.showConfirmDialog(app,"Do you want to generate the power spectrum for this window?","",JOptionPane.YES_NO_OPTION);
									if ( option == 0)
									{
										try
										{
											startIndex = Integer.parseInt(a);
											endIndex = Integer.parseInt(b);
											boolean valid = false;
											if (((endIndex-startIndex)+1)%3 == 0)
											{
												valid = true;
											}
											else
											{
												//	JOptionPane.showMessageDialog(app,"for gene finding, window size shud b multiple of 3");
											}
											if (startIndex >= 1 && startIndex <= endIndex && endIndex <= s.length())
											{
												//System.out.println("dialog is: "+dialog.isShowing());
												swindowThread swt=new swindowThread(fetchFlag,startIndex,endIndex,s,reverss,valid,ok,dialog);
												swindow1=new Thread(swt);
												swindow1.start();
												//System.out.println("dialog2 is: "+dialog.isShowing());
											}
											else
											{
												if (!valid)
												JOptionPane.showMessageDialog(app,"Index values are invalid ");
											}
										}
										catch(NumberFormatException nfe)
										{
											JOptionPane.showMessageDialog(app,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
										}
									}
									else
									{
										startIndex = Integer.parseInt(a);
										endIndex = Integer.parseInt(b);

										// User did not select to generate power spectrum
										// User did not select to generate power spectrum
										String name = fetchedHistory.getElement(fetchFlag).getName();
										File fileName = new File("../History/Fetched/W_"+startIndex+"s_"+endIndex+"e_"+name);
										try
										{
											FileWriter fr = new FileWriter(fileName);
											fr.write(">This fasta file is generated by Window Analysis of bioSpectrogram.");
											char[] charArray = s.substring(startIndex-1,endIndex).toCharArray();
											for(int i=0;i<charArray.length;i++)
											{
												if (i%70 == 0)
												{
													fr.write("\n");
												}
												fr.write(charArray[i]);
											}
											fr.close();
											fetchedHistory.updateFetchedFileList ();

											prevtoptext=topLabel.getText();
											prevbottomtext=bottomLabel.getText();
											if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
											{
												backButton.setEnabled(false);
												backButton.setText("Back");
												back.setEnabled(false);
												back.setText("Back");
											}
											else
											{
												backButton.setEnabled(true);
												back.setEnabled(true);
											}

											topLabel.setText(getSequence(0,1).toString());
											topLabel.updateUI();
											bottomLabel.setText(" ");
											bottomLabel.updateUI();
										}
										catch(IOException exp)
										{
											exp.printStackTrace(System.out);
											JOptionPane.showMessageDialog(app,"Something went wrong :( ");
										}
									}
								}
								else
								{
									prevtoptext=topLabel.getText();
									prevbottomtext=bottomLabel.getText();

									if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
									{
										backButton.setEnabled(false);
										backButton.setText("Back");
										back.setEnabled(false);
										back.setText("Back");
									}
									else
									{
										backButton.setEnabled(true);
										back.setEnabled(true);
									}

									topLabel.setText(getSequence(fetchFlag,1).toString());
									topLabel.updateUI();
									bottomLabel.setText(" ");
									bottomLabel.updateUI();
									String[] optionwindow1={"Forward Sliding Window","Backward Sliding Window"};

									final boolean forwardslide;
									int wind=JOptionPane.showOptionDialog(app,"Choose type of Sliding window:","Sliding Window",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,optionwindow1,"Forward Sliding Window");

									if(wind==0)
									{
										forwardslide=true;
									}
									else
									{
										forwardslide=false;
									}

									final JDialog dialog = new JDialog(app,"Windowing Dialog",JDialog.ModalityType.DOCUMENT_MODAL);
									final JPanel panel = new JPanel();
									final JButton ok = new JButton("OK");
									final JButton cancel = new JButton("CANCEL");

									JLabel label1 = new JLabel("  Select Encoding                 :   ");
									JLabel label2 = new JLabel("  Select Transformation           :   ");

									String[] encodingList= new String[]
									{
										"Indicator Encoding(For A)",
										"Indicator Encoding(For C)",
										"Indicator Encoding(For G)",
										"Indicator Encoding(For T)",
										"TetraHedron Encoding",
										"Z curve Encoding",
										"DV curve Encoding",
										"Complex Encoding 1 & User choice",
										"Complex Encoding 2",
										"Random Complex Encoding",
										"Graphical Encoding 1",
										"Graphical Encoding 2",
										"Random Graphical Encoding",
										"Quaternion Encoding 1 & 2",
										"Real Value Encoding",
										"Electron Ion Encoding",
										"Random Real Value Encoding",
										"Quaternary Integer Mapping 1",
										"Quaternary Integer Mapping 2",
									};

									String[] transformationList = new String[]
									{
                                       	"Fast Fourier Transform",
						                "Hilbert Transform",
										"Z transform",
										"Analytic Signal Transform",
						                "Discrete Haar Wavelet Transform",
						                "Chirp Z Transform",
						                "Discrete Cosine Transformation"
									};

									final JComboBox encodings = new JComboBox(encodingList);
									final JComboBox transformations = new JComboBox(transformationList);

									encodingFlag=0;

									encodings.addActionListener(new ActionListener()
									{
										public void actionPerformed(ActionEvent e)
										{
											JComboBox temp = (JComboBox) e.getSource ();
											encodingFlag = temp.getSelectedIndex();
										}
									});

									transformationFlag=0;
									transformations.addActionListener(new ActionListener()
									{
										public void actionPerformed(ActionEvent e)
										{
											JComboBox b = (JComboBox) e.getSource ();
											transformationFlag = b.getSelectedIndex ();
										}
									});

									ok.addActionListener (new ActionListener()
									{
                                        public void actionPerformed(ActionEvent e)
                                        {
											try
											{
												boolean valid2=true;
												if(encodingFlag==4 || encodingFlag==5)
												{
													JOptionPane.showMessageDialog(app,"Transformations for ZCurve and Tetrahedron encoding will be available in the next version.");
													valid2=false;
												}
												else if(encodingFlag==11 || encodingFlag==12 || encodingFlag==13 || encodingFlag==14)
												{
													JOptionPane.showMessageDialog (app,"Transformations for Graphical encoding and Quarternion encoding will be available in the next version.");
													valid2=false;
												}
												if(valid2)
												{
													String s = getOnlySequence(fetchFlag,1).toString().replace("N","").trim().replaceAll("\\s+","");
													StringBuffer reverses1=new StringBuffer(s);
													reverses1=reverses1.reverse();
													String revers=reverses1.toString();
													if(!forwardslide) s=revers;
													char[] ary = s.toCharArray ();
													String a,b;
													int windowW=1,windowG=1;

													String s1 = "There are "+ary.length+" characters in this DNA file !";
													JOptionPane.showMessageDialog (app,s1);
													boolean valid1=true;
													String[] optionwindow2={"Single Window Size","A range of window sizes"};
													boolean validsize=false;
													boolean singlewindowsize=true;
													int wind1=JOptionPane.showOptionDialog(app,"Choose type of Sliding window:","Sliding Window",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,optionwindow2,"Single window size");
													if(wind1==1)
													{
														while(!validsize)
														{a = JOptionPane.showInputDialog (app,"Enter starting window size : ");
															b = JOptionPane.showInputDialog (app,"Enter ending window size  : ");
															windowW = Integer.parseInt(a);
															windowG=Integer.parseInt(b);
															if(windowG<windowW)
															{
																JOptionPane.showMessageDialog(app,"Ending window size must be greater than or equal to starting window size.");
																validsize=false;
															}	else validsize=true;}
															singlewindowsize=false;
													}
        											else
        											{
														a = JOptionPane.showInputDialog (app,"Enter window size : ");
														windowW = Integer.parseInt(a);
														windowG=windowW;
														singlewindowsize=true;
													}
													if(windowW>ary.length || windowW<1 || windowG>ary.length || windowG<1)
													{
														JOptionPane.showMessageDialog (app,"Window size must be between 1 and number of characters in sequence.");
														valid1=true;
														valid2=false;
													}
													else
													{
														valid1=false;valid2=true;
													}

													/*******************************************************************************
														*	Fasta File Creation
													*******************************************************************************/
													int dnaind=0;
													Complex z1=new Complex(0,0);
													Complex z2=new Complex(0,0);
													int m=0,n=0,quarternion=0,flag1=1,startIndex=1,windows=windowW,endIndex=startIndex+windows-1;
													double ra=0,rc=0,rg=0,rt=0,m2=0,n2=0,rz1,rz2,cz1,cz2;
													boolean realboolean=true;
													String name = fetchedHistory.getElement(fetchFlag).getName();
													if((encodingFlag==0 || encodingFlag==1 || encodingFlag==2 || encodingFlag==3) && valid2)
													{
														dnaind=encodingFlag;
													}
													if(encodingFlag==7 && valid2)
													{
														String aa,bb,cc,dd;
														String a12;

														aa = JOptionPane.showInputDialog (app,"Enter real value for z1 ");
														bb = JOptionPane.showInputDialog (app,"Enter imaginary value for z1");
														cc = JOptionPane.showInputDialog (app,"Enter real value for z2");
														dd = JOptionPane.showInputDialog (app,"Enter imaginary value for z2");
														rz1 = Double.parseDouble (aa);
														cz1 = Double.parseDouble (bb);
														rz2 = Double.parseDouble (cc);
														cz2 = Double.parseDouble (dd);
														z1=new Complex(rz1,cz1);
														z2=new Complex(rz2,cz2);
														flag1=1;
													}
													if(encodingFlag==11 && valid2)
													{
														String a1,b1;

														// prompt user to enter values of m and n
												        a1 = JOptionPane.showInputDialog (app,"Enter integer value of m");
														b1 = JOptionPane.showInputDialog (app,"Enter integer value of n");

														// proceed with the encoding only if valid values of m and n are given
												        if (a1 == null || b1 == null )
														{
												        	JOptionPane.showMessageDialog (app,"Please enter proper sequence !");
												        	flag1=0;
												        	valid2=false;
														}
												        else
														{
												        	m = Integer.parseInt(a1);
												            n = Integer.parseInt(b1);
														}
													}
													if(encodingFlag==14 && valid2)
													{
														//String a11;
														//a11 = JOptionPane.showInputDialog (app,"Enter 0 for 3-bit, 1 for 4-bit:");
														//quarternion=Integer.parseInt(a11);
														quarternion = JOptionPane.showConfirmDialog(app,"Do you want to use Quaternion 1?(No means Quaternion 2)","",JOptionPane.YES_NO_OPTION);
														if(quarternion!=0 && quarternion!=1)
														{
															JOptionPane.showMessageDialog (app,"Invalid flag entered.");
															valid2=false;
														}
													}
													if(encodingFlag==15 && valid2)
													{
														String aa,bb,cc,dd;
														int c;
														c = JOptionPane.showConfirmDialog(app,"Do you want to enter real values of your choice?(y/n)","",JOptionPane.YES_NO_OPTION);
														if( c == 0)
														{
															realboolean=true;
															aa = JOptionPane.showInputDialog (app,"Enter real value for A ");
															bb = JOptionPane.showInputDialog (app,"Enter real value for C");
															cc = JOptionPane.showInputDialog (app,"Enter real value for G");
															dd = JOptionPane.showInputDialog (app,"Enter real value for T");
															ra = Double.parseDouble (aa);
															rc = Double.parseDouble (bb);
															rg = Double.parseDouble (cc);
															rt = Double.parseDouble (dd);
															flag1=1;
														}
														else
														{
															realboolean=false;
														}
													}

													if(transformationFlag==2 && valid2)
													{
														String a2,b2;

														a2 = JOptionPane.showInputDialog (app,"Enter real value of Z");
						                                b2 = JOptionPane.showInputDialog (app,"Enter imaginary value of Z");
														if (a2==null || b2==null)
						                                {
															JOptionPane.showMessageDialog (app,"Please enter proper complex value !");
															valid2=false;
														}
						                                else
						                                {
															m2 = Double.parseDouble(a2);
															n2 = Double.parseDouble(b2);
														}
													}
													double zta=1,ztu=1,ztv=1;
													long ztm=1;

													if(transformationFlag==5 && valid2)
													{
														String msg = "z=a*(w.^(0:m-1)),  where w=exp(j*2*pi*u/v)=cos(2*pi*u/v)+jsin(2*pi*u/v),take a,u,v,m as input, where m is an integer";
														JOptionPane.showMessageDialog(app,msg);

														String ta = JOptionPane.showInputDialog(app,"Enter value for a");
														String tu = JOptionPane.showInputDialog(app,"Enter value for u");
														String tv = JOptionPane.showInputDialog(app,"Enter value for v");
														String tm = JOptionPane.showInputDialog(app,"Enter value for m");

														zta = Double.parseDouble(ta);
														ztu = Double.parseDouble(tu);
														ztv = Double.parseDouble(tv);
														ztm = Long.parseLong(tm);

														while ( !(ztm > 0 ))
														{
															JOptionPane.showMessageDialog(app,"Please enter positive integer value for m");
															tm = JOptionPane.showInputDialog (app,"Enter value for m");
															ztm = Long.parseLong(tm);
														}
														valid2=true;
													}
													if(valid2)
													{
														ok.setEnabled(false);
														RunnableThread r1=new RunnableThread(encodingFlag,transformationFlag,fetchFlag,forwardslide,dialog,ary,startIndex,endIndex,name,s,realboolean,dnaind,z1,z2,m,n,quarternion,ra,rc,rg,rt,m2,n2,zta,ztu,ztv,ztm,ok,windowW,windowG,valid2);
														t1=new Thread(r1,"Thread1");
														t1.start();
													}
												}
											}
											catch(NumberFormatException nfe)
											{
												JOptionPane.showMessageDialog(app,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
											}
											catch(NullPointerException nul)
											{
												errorHandler(nul);
											}
										}
									});

									cancel.addActionListener (new ActionListener()
									{
                                        public void actionPerformed(ActionEvent e)
                                        {
                                        	if(t1.isAlive())
											t1.interrupt();
											dialog.dispose ();
										}
									});

									panel.setLayout (new GridLayout(3,2));
									panel.add(label1);
									panel.add(encodings);
									panel.add(label2);
									panel.add(transformations);
									panel.add(ok);
									panel.add(cancel);
									panel.setBackground (Color.WHITE);
									panel.setBorder (new EtchedBorder(EtchedBorder.RAISED));
									dialog.getContentPane ().add(panel);
									dialog.setBounds (maxScreenWidth/3,maxScreenHeight/3,500,150);
									dialog.setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
									dialog.setVisible (true);
								}
							}
						}
					}
                	catch(NumberFormatException nfe)
                	{
                		JOptionPane.showMessageDialog(app,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
					}
                	catch(NullPointerException nul)
                	{
						errorHandler(nul);
					}
                	if(!t2.isAlive() && !swindow1.isAlive())
					{
						dialog.dispose ();
					}
				}
			});

            cancel.addActionListener (new ActionListener()
			{
            	public void actionPerformed(ActionEvent e)
                {
                	if(t2.isAlive())
					{
						t2.interrupt();
					}

                	if(swindow1.isAlive())
					{
						swindow1.interrupt();
					}

                	if(!ok.isEnabled())
                	{
                		prevtoptext=topLabel.getText();
						prevbottomtext=bottomLabel.getText();
						if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
						{
							backButton.setEnabled(false);
							backButton.setText("Back");
							back.setEnabled(false);
							back.setText("Back");
						}
						else
						{
							backButton.setEnabled(true);
							back.setEnabled(true);
						}

						topLabel.setText(getSequence(fetchFlag,1).toString());
						topLabel.updateUI();
						bottomLabel.setText(" ");
						bottomLabel.updateUI();
					}
					dialog.dispose ();
				}
			});

			panel.setLayout (new GridLayout(3,2));
            panel.add(label1);
			panel.add(fetchedfiles);
			panel.add(label2);
			panel.add(optionSelector);
            panel.add(ok);
			panel.add(cancel);
            panel.setBackground (Color.WHITE);
			panel.setBorder (new EtchedBorder(EtchedBorder.RAISED));
            dialog.getContentPane ().add(panel);
			dialog.setBounds (maxScreenWidth/3,maxScreenHeight/3,500,150);
            dialog.setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
			dialog.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					if(t2.isAlive())
					{
						t2.interrupt();
					}

					if(t1.isAlive())
					{
						t1.interrupt();
					}

					if(swindow1.isAlive())
					{
						swindow1.interrupt();
					}

					if(!ok.isEnabled())
					{
						prevtoptext=topLabel.getText();
						prevbottomtext=bottomLabel.getText();

						if((prevtoptext.equals("") || prevtoptext.equals(" ")) &&( prevbottomtext.equals("") || prevbottomtext.equals(" ")))
						{
							backButton.setEnabled(false);
							backButton.setText("Back");
							back.setEnabled(false);
							back.setText("Back");
						}
						else
						{
							backButton.setEnabled(true);
							back.setEnabled(true);
						}

						topLabel.setText(getSequence(fetchFlag,1).toString());
						topLabel.updateUI();
						bottomLabel.setText(" ");
						bottomLabel.updateUI();
					}
					proteinHistory.updateProteinFileList();
					transformedHistory.updateTransformedFileList();
					encodedHistory.updateEncodedFileList();
					fetchedHistory.updateFetchedFileList();
					dialog.dispose();
				}
			});
            dialog.setVisible (true);
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for Encoding function for
	 *	"Window" Button
	*******************************************************************************/
	static boolean windowencode(File filename,int encodingflag,boolean realboolean,int dnaind,Complex z1,Complex z2,int m,int n,int quarternion,double ra,double rc,double rg,double rt)
	{
		String name=filename.getName();
		try
		{
			switch (encodingflag)
			{
				case 0:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						DNAindicator temp = new DNAindicator(filename,1);
						encodedHistory.updateFileList ();
						return true;
					}
				}

				case 1:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						DNAindicator temp = new DNAindicator(filename,2);
						encodedHistory.updateFileList ();
						return true;
					}
				}

				case 2:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						DNAindicator temp = new DNAindicator(filename,3);
						encodedHistory.updateFileList ();
						return true;
					}
				}

				case 3:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						DNAindicator temp = new DNAindicator(filename,4);
						encodedHistory.updateFileList ();
						return true;
					}
				}

				case 4:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						Tetrahedron temp = new Tetrahedron(filename);
						boolean valid = temp.encode();
						if (valid)
						{
							encodedHistory.updateFileList ();

							return true;
						}
						else
						{
							return false;
						}
					}
				}

				case 5:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						ZCurve temp = new ZCurve(filename);
						boolean valid = temp.encode();
						if (valid)
						{
							encodedHistory.updateFileList ();

							return true;
						}
						else
						{
							return false;
						}
					}
				}

				case 6:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						DVCurve temp = new DVCurve(filename);
						boolean valid = temp.encode();
						if (valid)
						{
							encodedHistory.updateFileList ();

							return true;
						}
						else
						{
							return false;
						}
					}
				}

				case 7:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						ComplexEncod temp = new ComplexEncod(filename);
						boolean valid = temp.encode1(z1,z2);
						if (valid)
						{
							encodedHistory.updateFileList ();
							return true;

						}
						else
						{
							return false;
						}
					}
				}

				case 8:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						ComplexEncod temp = new ComplexEncod(filename);
						boolean valid = temp.encode2();
						if (valid)
						{
							encodedHistory.updateFileList ();
							return true;

						}
						else
						{
							return false;
						}
					}
				}

				case 9:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						ComplexEncod2 temp = new ComplexEncod2(filename);
						boolean valid = temp.encode();
						if (valid)
						{
							encodedHistory.updateFileList ();

							return true;
						}
						else
						{
							return false;
						}
					}
				}

				case 10:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						RandomComplex temp = new RandomComplex(filename);
						boolean valid = temp.encode();
						if (valid)
						{
							encodedHistory.updateFileList ();

							return true;
						}
						else
						{
							return false;
						}
					}
				}

				case 11:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						GraphicalEncod temp = new GraphicalEncod(filename);
						boolean valid = temp.encode1(m,n);
						if (valid)
						{
							encodedHistory.updateFileList ();


							return true;
						}
						else
						{
							return false;
						}
					}
				}


				case 12:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						YauAtElGraphical temp = new YauAtElGraphical(filename);
						boolean valid = temp.encode();
						if (valid)
						{
							encodedHistory.updateFileList ();
							return true;
						}
						else
						{
							return false;
						}
					}
				}


				case 13:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						RandomGraphical temp = new RandomGraphical(filename);
						boolean valid = temp.encode();
						if (valid)
						{
							encodedHistory.updateFileList ();

							return true;
						}
						else
						{
							return false;
						}
					}
				}

				case 14:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						QuaternionEncod temp = new QuaternionEncod(filename);
						boolean valid=true;
						if(quarternion==0)
						valid = temp.encode3();
						else if(quarternion==1)
						valid = temp.encode4();
						else return false;
						if (valid)
						{
							encodedHistory.updateFileList ();

							return true;
						}
						else
						{
							return false;
						}
					}
				}

				case 15:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						RealMappingEncod temp = new RealMappingEncod(filename);
						boolean valid=true;
						if(realboolean)
						valid = temp.encode2(ra,rc,rg,rt);
						else valid=temp.encode1();
						if (valid)
						{
							encodedHistory.updateFileList ();
							return true;
						}
						else
						{
							return false;
						}
					}
				}

				case 16:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						ElectroIonIntEncod temp = new ElectroIonIntEncod(filename);
						boolean valid = temp.encode();
						if (valid)
						{
							encodedHistory.updateFileList ();
							return true;
						}
						else
						{
							return false;
						}
					}
				}

				case 17:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						RandomReal temp = new RandomReal(filename);
						boolean valid = temp.encode();
						if (valid)
						{
							encodedHistory.updateFileList ();
							return true;
						}
						else
						{
							return false;
						}
					}
				}

				case 18:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						QRMEncod_1 temp = new QRMEncod_1(filename);
						boolean valid = temp.encode();
						if (valid)
						{
							encodedHistory.updateFileList ();
							return true;
						}
						else
						{
							return false;
						}
					}
				}

				case 19:
				{
					if (filename.getCanonicalPath().contains("genbank"))
					{
						JOptionPane.showMessageDialog (app,nextVersion);
						return false;
					}
					else
					{
						QRMEncod_2 temp = new QRMEncod_2(filename);
						boolean valid = temp.encode();
						if (valid)
						{
							encodedHistory.updateFileList ();
							return true;
						}
						else
						{
							return false;
						}
					}
				}

				default:
				{
					return false;
				}
			}
		}
		catch(IOException ioe)
		{
			errorHandler(ioe);
		}
		return false;
	}

	/*******************************************************************************
	 *	This static class implements action listener for Transformation function for
	 *	"Window" Button
	*******************************************************************************/
	static boolean windowtransform(File filename1,int transformflag,double m2,double n2,double zta,double ztu,double ztv,long ztm,int startIndex)
	{
		encodedHistory.updateEncodedFileList();

		String t = getOnlySequence(0,2).toString();
        t = t.trim();
        t = t.replaceAll("N","").replaceAll("Y","").replaceAll("S","");
        t = t.replaceAll("W","").replaceAll("K","").replaceAll("M","");
        t = t.replaceAll("B","").replaceAll("D","").replaceAll("H","");
        t = t.replaceAll("V","").replaceAll("-","").replaceAll("\\.","");
        t = t.replaceAll("\n","");
		String s = t;

		String filename=encodedHistory.getElement(0).getName();
		wencodedname=encodedHistory.getElement(0);

		switch(transformflag)
		{
			case 0:
			{
				if (filename.contains("E01")||filename.contains("E02")||filename.contains("E05")||filename.contains("E06"))
				{
					filename = filename.substring (0,filename.lastIndexOf ("."))+"_T01.fasta";
					filename = "../History/Window_Analysis/Transformed/"+filename;
					slidewindowplot[startIndex-1]=new File(filename);
					try
					{
						FileWriter writer = new FileWriter(filename);
						Complex[] output = FFT.FFToutput (s);
						int i=0;
						for(i=0;i<output.length;i++)
						{
							writer.write(output[i].toString ());
						}
						writer.close();
						return true;
					}
					catch (IOException ioe)
					{
						errorHandler(ioe);
						return false;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding Scheme !");
					return false;
				}
			}

			case 1:
			{

				Hilbert temp = new Hilbert(s);
				if (filename.contains("E01")||filename.contains("E05")||filename.contains("E06"))
				{
					filename = filename.substring (0,filename.lastIndexOf ("."))+"_T02.fasta";
					filename = "../History/Window_Analysis/Transformed/"+filename;
					slidewindowplot[startIndex-1]=new File(filename);
					try
					{
						FileWriter writer = new FileWriter(filename);
						Complex[] output = temp.HilbertOutput();
						for(int i=0;i<output.length;i++)
						{
							writer.write(output[i].toString ());
						}
						writer.close();
						return true;
					}
					catch (IOException ioe)
					{
						errorHandler(ioe);
						return false;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding !");
					return false;
				}
			}

			case 2:
			{
				ZTransform temp = new ZTransform(s,new Complex(m2,n2));

				if (filename.contains("E01")||filename.contains("E02")||filename.contains("E05")||filename.contains("E06"))
				{
					filename = filename.substring (0,filename.lastIndexOf ("."))+"_T03.fasta";
					filename = "../History/Window_Analysis/Transformed/"+filename;
					slidewindowplot[startIndex-1]=new File(filename);
					try
					{
						FileWriter writer = new FileWriter(filename);
						writer.write(temp.ZTransformed ().toString ());
						writer.close();
						return true;
					}
					catch (IOException ioe)
					{
						errorHandler(ioe);
						return false;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding !");
					return false;
				}
			}

			case 3:
			{
				Hilbert temp = new Hilbert(s);
				if (filename.contains("E01")||filename.contains("E05")||filename.contains("E06"))
				{
					filename = filename.substring (0,filename.lastIndexOf ("."))+"_T04.fasta";
					filename = "../History/Window_Analysis/Transformed/"+filename;
					slidewindowplot[startIndex-1]=new File(filename);
					try
					{
						FileWriter writer = new FileWriter(filename);
						Complex[] output = temp.AnalyticSignal();
						for(int i=0;i<output.length;i++)
						{
							writer.write(output[i].toString ());
						}
						writer.close();
						return true;
					}
					catch (IOException ioe)
					{
						errorHandler(ioe);
						return false;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding Scheme !");
					return false;
				}
			}

			case 4:
			{
				DWavelet temp = new DWavelet();
				if (filename.contains("E01")||filename.contains("E05")||filename.contains("E06"))
				{
					filename = filename.substring (0,filename.lastIndexOf ("."))+"_T05.fasta";
					filename = "../History/Window_Analysis/Transformed/"+filename;
					slidewindowplot[startIndex-1]=new File(filename);
					try
					{
						FileWriter writer = new FileWriter(filename);
						double[] output=temp.discreteHaarWaveletTransformoutput(s);
						String st="";
						for(int i=0;i<output.length;i++)
						{
							st = Double.toString (output[i])+"\n";
							writer.write(st);
						}
						writer.close ();
						return true;
					}
					catch (IOException ioe)
					{
						errorHandler(ioe);
						return false;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding Scheme !");
					return false;
				}
			}

			case 5:
			{
				ChirpZTransform temp = new ChirpZTransform(zta,ztu,ztv,ztm);

				if (filename.contains("E01")||filename.contains("E02")||filename.contains("E05")||filename.contains("E06"))
				{
					filename = filename.substring (0,filename.lastIndexOf ("."))+"_T06.fasta";
					filename = "../History/Window_Analysis/Transformed/"+filename;
					slidewindowplot[startIndex-1]=new File(filename);
					try
					{
						FileWriter writer = new FileWriter(filename);
						Complex[] output = temp.ChirpZTransformed(s);
						for(int i=0;i<output.length;i++)
						{
							writer.write(output[i].toString ());
						}
						writer.close();
						return true;
					}
					catch (IOException ioe)
					{
						errorHandler(ioe);
						return false;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(app,"This Transformation cannot be applied on selected Encoding Scheme !");
					return false;
				}
			}

			case 6:
			{
				JOptionPane.showMessageDialog (app,"Discrete Cosine Transform will be available in the next version.");
				return false;
			}

			default:
			{
				return false;
			}
		}
		//	return false;
	}


	/*******************************************************************************
	 *	This static class implements Runnable Thread for "NCBI Updates" Button
	*******************************************************************************/
	static class updateThread implements Runnable
	{
    	JDialog dialog;
    	JLabel label;
    	public updateThread(JDialog dialog,JLabel label)
		{
    		this.dialog=dialog;
    		this.label=label;
    	}

    	public void run()
    	{
    		try
    		{
    			updateHistory (dialog,label);
    			dialog.dispose();
    		}
    		catch(OutOfMemoryError e)
			{
				//in.close();
				Runtime rt = Runtime.getRuntime();
				//rt.gc();
				e.printStackTrace(System.out);
				JOptionPane.showMessageDialog(app,"Heap Memory exhausted. Current operation will be aborted :(\n"+
					"If software seems to be hung after pressing OK, minize & maximize :)");
				dialog.dispose();
				if(update1.isAlive())
					update1.interrupt();
				//StringBuffer tempBuf = new StringBuffer(1);
				//tempBuf.append(" ");
				//return tempBuf;
			}
    	}
    }

	/*******************************************************************************
	 *	This static class implements action listener for "NCBI Updates" Button
	*******************************************************************************/
	static class updateListener implements ActionListener
	{
    	public void actionPerformed(ActionEvent e)
        {

        	String message = "Fetch uses Entrez utilities for fetching DNA sequences from NCBI database.\n";
			message = message+"Frequency, Timing and Registration of E-utility URL Requests \n";
			message = message+"In order not to overload the E-utility servers, NCBI recommends that users";
			message = message+"post no more than three URL requests per second and limit large jobs to either weekends\n";
			message = message+"or between 9:00 PM and 5:00 AM Eastern time during weekdays. \n";
			message = message+"Failure to comply with this policy may result in an IP address being blocked from accessing NCBI.";
			message = message+" If NCBI blocks an IP address, \n";
			message = message+"service will not be restored unless the developers of the software accessing the E-utilities register ";
			message = message+"values of the tool and email parameters with NCBI.\n For further information on user policy, visit : \n";
			message = message+"http://www.ncbi.nlm.nih.gov/books/NBK25497/";
			JOptionPane.showMessageDialog(app,message);

			int c = JOptionPane.showConfirmDialog(app,"This action can take more time and  data usage.\n Do you want to continue (y/n) ?","",JOptionPane.YES_NO_OPTION);
            if (c == 0)
			{
				final JDialog dialog=new JDialog(app,"Updating History");
				JPanel panel=new JPanel();
				JLabel label=new JLabel("Updating History..........");

				JButton button=new JButton("Cancel");
				button.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent up)
					{
						if(update1.isAlive())
							updateFlag=false;
						dialog.dispose();
					}
				});

				panel.setLayout(new GridLayout(1,2));
				panel.add(label);
				panel.add(button);
				dialog.getContentPane().add(panel);
				dialog.pack();
				dialog.setVisible(true);
				dialog.setLocation(maxScreenWidth/2,maxScreenHeight/2);
				updateThread r=new updateThread(dialog,label);
				update1=new Thread(r);
				updateFlag=true;
				update1.start();
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Check Internet" Button
	*******************************************************************************/
	static class netListener implements ActionListener
	{
    	public void actionPerformed(ActionEvent e)
        {
			// call check internet connection method
            if (checkInternet())
			{
            	JOptionPane.showMessageDialog (app,"Your Internet connection is up !");
			}
            else
			{
            	JOptionPane.showMessageDialog (app,"Your internet connection is down !");
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Preferences" Button
	*******************************************************************************/
	static class prefListener implements ActionListener
	{
    	public void actionPerformed(ActionEvent e)
        {
			final JDialog preferenceBox = new JDialog(app,"Preferences",JDialog.ModalityType.DOCUMENT_MODAL);

			final JButton okButton = new JButton("OK");
            final JButton cancelButton = new JButton("CANCEL");

			JLabel label1 = new JLabel();
            JLabel label2 = new JLabel();
			JLabel label3 = new JLabel();
			JLabel label4 = new JLabel();

            JPanel panel1 = new JPanel();	// Current History Size
			JPanel panel2 = new JPanel();	// set history size
            JPanel panel3 = new JPanel();	// Button Pannel
			JPanel panel4 = new JPanel();	// set Font size
			JPanel panel5 = new JPanel();	// set MATLAB size
			JPanel panel6 = new JPanel();	// set RAM size

            JPanel fields = new JPanel();

			final JFormattedTextField field1 = new JFormattedTextField();	// Size for fetched history
            final JFormattedTextField field2 = new JFormattedTextField();	// Size for encoded history
			final JFormattedTextField field3 = new JFormattedTextField();	// size for transformed history
			final JFormattedTextField field4 = new JFormattedTextField();	// Disaply Font Size
			final JFormattedTextField field5 = new JFormattedTextField();	// MATLAB plot Delay
			final JFormattedTextField field6 = new JFormattedTextField();	// Actual RAM size
			final JFormattedTextField field7 = new JFormattedTextField();	// size for protein history

            int h1,h2,h3,h4,m1,m2,m3,m4;
			long s1,s2,s3,s4;

			h1 = fetchedHistory.getSize();
            h2 = encodedHistory.getSize();
			h3 = transformedHistory.getSize();
			h4 = proteinHistory.getSize();

            m1 = fetchedHistory.getMaxSize();
			m2 = encodedHistory.getMaxSize();
            m3 = transformedHistory.getMaxSize();
            m4 = proteinHistory.getMaxSize();

			s1 = fileHistory.getDirSize(1)/1024;
            s2 = fileHistory.getDirSize(2)/1024;
			s3 = fileHistory.getDirSize(3)/1024;
			s4 = fileHistory.getDirSize(4)/1024;

			String st1 = "    Fetched history            :  "+h1+"/"+m1+"  & "+s1+" KB ";
            String st2 = "    Encoded history           :  "+h2+"/"+m2+"  & "+s2+" KB ";
			String st3 = "    Transformed history  :  "+h3+"/"+m3+"  & "+s3+" KB ";
			String st4 = "    Protein history             :  "+h4+"/"+m4+"  & "+s4+" KB ";

            label1.setText(st1);
			label2.setText(st2);
            label3.setText(st3);
            label4.setText(st4);

			panel1.setLayout(new GridLayout(4,1));
            panel1.setBorder(new TitledBorder("Current History Size and Disc Space Usage"));
			panel1.setBackground (Color.white);
            panel1.add(label1);
			panel1.add(label2);
            panel1.add(label3);
            panel1.add(label4);

			panel2.setLayout(new GridLayout(4,2));
            panel2.setBorder(new TitledBorder("Set Maximum allowed number of files"));
			panel2.setBackground(Color.white);
            panel2.add(new JLabel("   In fetched history           : "));
			panel2.add(field1);
            panel2.add(new JLabel("   In encoded history         : "));
			panel2.add(field2);
            panel2.add(new JLabel("   In transformed history : "));
			panel2.add(field3);
			panel2.add(new JLabel("   In protein history         : "));
			panel2.add(field7);

			panel4.setLayout(new GridLayout(1,2));
			panel4.setBorder (new TitledBorder("Set Display Font Size"));
			panel4.setBackground (Color.white);
			panel4.add(new JLabel(" Enter Font Size (10-100) : "));
			panel4.add(field4);

			panel5.setLayout(new GridLayout(1,2));
			panel5.setBorder(new TitledBorder("Set MATLAB plot delay (in seconds)"));
			panel5.setBackground(Color.white);
			panel5.add(new JLabel(" Enter positive real value : "));
			panel5.add(field5);

			// RAM preferences
			panel6.setLayout(new GridLayout(1,2));
			panel6.setBorder(new TitledBorder("Set RAM size (in GB)"));
			panel6.setBackground(Color.white);
			panel6.add(new JLabel("<html>Enter amount of RAM bioSpectrogram should use : <br/>"+
				" 25% of actual RAM of your system is optimal. </html>"));
			panel6.add(field6);

			panel3.setLayout (new GridLayout(1,2));
			panel3.add(okButton);
			panel3.add(cancelButton);
			panel3.setPreferredSize(new Dimension(panel3.getWidth(),30));

			fields.setLayout(new GridLayout(5,1));
			fields.add(panel1);	// Information Panel
			fields.add(panel2);	// Set Size Panel
			fields.add(panel4);	// Set Font Panel
			fields.add(panel5); // MATLAB plot delay
			fields.add(panel6);	// RAM settings

			okButton.addActionListener (new ActionListener()
            {
				public void actionPerformed(ActionEvent e)
                {
					int n;
					int flag=1;
					try
                    {
                    	// Field 1
                    	if (!field1.getText().equals(""))
                    	{
                    		n = Integer.parseInt(field1.getText());
                    		JOptionPane.showMessageDialog(app,"Fetched History size saved.");
                    	}
                    	else
                    	{
                    		n = fetchedHistory.getSize();
                    	}
						if(n>0)
						{
							fetchedHistory.changeSize(n);
						}
						else
						{
							flag=-1;
						}

						// Field 2
						if (!field2.getText().equals(""))
                    	{
                    		n = Integer.parseInt(field2.getText());
                    		JOptionPane.showMessageDialog(app,"Encoded History size saved.");
                    	}
                    	else
                    	{
            				n = encodedHistory.getSize();
                    	}
						if(n>0)
						{
							encodedHistory.changeSize(n);
						}
						else
						{
							flag=-1;
						}

						// Field 3
						if (!field3.getText().equals(""))
                    	{
                    		n = Integer.parseInt(field3.getText());
                    		JOptionPane.showMessageDialog(app,"Transformed History size saved.");
                    	}
                    	else
                    	{
							n = transformedHistory.getSize();
                    	}
						if (n>0)
						{
							transformedHistory.changeSize(n);
						}
						else
						{
							flag=-1;
						}

						// Field 7
						if (!field7.getText().equals(""))
                    	{
                    		n = Integer.parseInt(field7.getText());
                    		JOptionPane.showMessageDialog(app,"Protein History size saved.");
                    	}
                    	else
                    	{
							n = proteinHistory.getSize();
                    	}
						if (n>0)
						{
							proteinHistory.changeSize(n);
						}
						else
						{
							flag=-1;
						}

						// Set Font Validations
						int fontSize=30;
						if (!field4.getText().equals(""))
						{
							fontSize = Integer.parseInt(field4.getText());
							if (fontSize >= 10 && fontSize <= 100)
							{
								JOptionPane.showMessageDialog(app,"Font size saved.");

								topLabel.setFont (new Font("Monospaced",Font.BOLD,fontSize));
        						bottomLabel.setFont (new Font("Monospaced",Font.BOLD,fontSize));
        						topLabel.updateUI();
        						bottomLabel.updateUI();
							}
							else
							{
								if (flag == -1)
								{
									JOptionPane.showMessageDialog (app,"Invalid size entered for history !");
								}
								else
								{
									JOptionPane.showMessageDialog(app,"Invalid font size entered !");
								}
							}
						}

						// Set MATLAB plot delay validations
						double temp;
						if (field5.getText().equals(""))
						{
							// Do nothing
						}
						else if ((temp = Double.parseDouble(field5.getText())) > 0)
						{
							plotDelay = temp;
							JOptionPane.showMessageDialog(app,"MATLAB delay saved.");
						}
						else
						{
							JOptionPane.showMessageDialog(app,"Invalid MATLAB delay size entered !");
						}

						// RAM Settings
						double ramSize = 0;
						if (!field6.getText().equals(""))
						{
							ramSize = Double.parseDouble(field6.getText());

							if (ramSize >= 0.60*actualRAM)
							{
								JOptionPane.showMessageDialog(app,"You have entered value which is 60% more than the total RAM you have !");
								JOptionPane.showMessageDialog(app,"Please reenter the value !");
								field6.setText("");
							}
							else
							{
								File runScript1 = new File("../biospec.bat");
								File runScript2 = new File("../biospec.sh");

								try
								{
									int sizeinMB = (int)(ramSize*1024);

									runScript1.createNewFile();
									runScript2.createNewFile();

									FileWriter f = new FileWriter(runScript1);
									//f.write("javac mainApp.java\n");
									f.write("cd SOURCE\n");
									f.write("START /B java -Xmx"+sizeinMB+"m mainApp");
									f.close();

									f = new FileWriter(runScript2);
									//f.write("javac mainApp.java\n");
									f.write("cd SOURCE\n");
									f.write("java -Xmx"+sizeinMB+"m mainApp &");
									f.close();
								}
								catch(IOException ioe)
								{
									errorHandler(ioe);
								}
								Desktop desktop = Desktop.getDesktop();
								JOptionPane.showMessageDialog(app,"Allocated RAM size saved.");
								try
								{
									String osName= System.getProperty("os.name");
									osName = osName.toLowerCase();
									if(osName.contains("mac")||osName.contains("linux")||osName.contains("solaris")||osName.contains("unix"))
									{
										File script = new File("../biospec.sh");
										String path = script.getCanonicalPath();
										Runtime.getRuntime().exec("sh "+path);
									}
									else
									{
										File script = new File("../biospec.bat");
										String path = script.getCanonicalPath();
										Runtime.getRuntime().exec(path);
									}
								}
								catch(IOException ioe)
								{
									errorHandler(ioe);
								}
								System.exit(0);
							}
						}
						else
						{
							preferenceBox.dispose();
						}
					}
                    catch(NumberFormatException nfe)
					{
                    	JOptionPane.showMessageDialog(app,"Invalid input entered. Please close the dialog(if not done automatically) and redo the operation");
					}
				}
			});

			cancelButton.addActionListener (new ActionListener()
            {
				public  void actionPerformed(ActionEvent e)
                {
					preferenceBox.dispose();
				}
			});

			preferenceBox.setResizable(false);
			preferenceBox.setLayout(new BorderLayout());
			preferenceBox.getContentPane().add(fields,BorderLayout.CENTER);
            preferenceBox.getContentPane().add(panel3,BorderLayout.SOUTH);
			preferenceBox.setBounds(maxScreenWidth/4,maxScreenHeight/8,600,600);
            preferenceBox.setVisible(true);

		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Export to MATLAB" Button
	*******************************************************************************/
	static class exportListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String[] optionwindow1={"Export Encoded File","Export Transformed File"};

			final boolean encoded;
			final int wind=JOptionPane.showOptionDialog(app,"Choose the type of file to export: ","Export to MATLAB",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,optionwindow1,"Export Encoded File");
            encodedHistory.updateEncodedFileList();
			transformedHistory.updateTransformedFileList();
			JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("CANCEL");
            JPanel panel = new JPanel();
			final JDialog dialog = new JDialog(app,"Export to MATLAB",JDialog.ModalityType.DOCUMENT_MODAL);

            JLabel label1 = new JLabel("   Select File for to export to MATLAB  :   ");
            final JComboBox transformation;
            final JComboBox encoding;

			transformation = new JComboBox(transformedHistory.getArray ());
			transformationFlag=0;

			// Action Listener for JCoombobox for choosing encoded file
			transformation.addActionListener (new ActionListener()
            {
				public void actionPerformed(ActionEvent e)
                {
					JComboBox temp = (JComboBox) e.getSource ();
                    transformationFlag = temp.getSelectedIndex ();
				}
			});

			encodingFlag=0;
			encoding = new JComboBox(encodedHistory.getArray ());

			encoding.addActionListener (new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
                {
					JComboBox temp = (JComboBox) e.getSource ();
                    encodingFlag = temp.getSelectedIndex ();
				}
			});

			// Action Listener for "OK" button
			// NOTE: Need to add validation code before sending encoded file for transformation
            okButton.addActionListener (new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
                {
                	if(wind==1)
                	{
						if (transformationFlag < transformedHistory.getSize())
						{
							String filename = transformedHistory.getElement(transformationFlag).getName();
							String fileenc=filename.substring(0,filename.lastIndexOf('.'));
							String filet=fileenc.substring(fileenc.lastIndexOf('_')+1);
							fileenc=fileenc.substring(0,fileenc.lastIndexOf('_'));
							String temp=fileenc;
							fileenc=fileenc.substring(fileenc.lastIndexOf('_')+1);
							String filen=temp.substring(0,temp.lastIndexOf('_'));

							if (filename.contains("T05") || filename.contains("W01"))
							{
								// This transformations produces outputwhich can be plot in 1D
								filename = filename.replace('.','_');
								filename = filename.concat(".m");
								filename = "../History/Matlab_Files/"+filename;
								try
								{
									FileWriter w = new FileWriter(filename);
									w.write("% This Matlab script is generated using BioSpectrogram.\n");
									w.write("figure;\n");
									w.write("val = importdata('"+transformedHistory.getElement(transformationFlag).getCanonicalPath()+"','%s');\n");

									if(filename.contains("T05"))
									{
										w.write("plot(double(val));\n");
										w.write("xlabel('Index');\n");
										w.write("ylabel('Discrete Haar Wavelet Transform coefficients');\n");
										if(filename.contains("W_") || filename.contains("user_"))
										{
											w.write("title(sprintf('File :"+filen+".fasta \\n Encoding:"+fileenc+" \\n Tranformation:"+filet+" \\n Generated using BioSpectrogram and Matlab'),'Interpreter','none');\n");
										}
										else
										{
											w.write("title(sprintf('Accession Number :"+filen+" \\n Encoding:"+fileenc+" \\n Tranformation:"+filet+" \\n Generated using BioSpectrogram and Matlab'),'Interpreter','none');\n");
										}
									}
									else
									{
										w.write("plot(double(val));\n");
										w.write("xlabel('Index');\n");
										w.write("ylabel('Sum of power spectra of indicator sequences');\n");
										if(filename.contains("W_") || filename.contains("user_"))
										{
											w.write("title(sprintf('File :"+filen+".fasta \\n Encoding:Indicator Encoding \\n Tranformation:Discrete Fourier Transform \\n Sum of power spectra of indicator sequences\\nGenerated using BioSpectrogram and Matlab'),'Interpreter','none');\n");
										}
										else
										{
											w.write("title(sprintf('Accession Number :"+filen+" \\n Encoding:Indicator Encoding \\n Tranformation:Discrete Fourier Transform \\n Sum of power spectra of indicator sequences\\n Generated using BioSpectrogram and Matlab'),'Interpreter','none');\n");
										}
									}
									w.close();
									JOptionPane.showMessageDialog(app,"Matlab script file "+filename+" successfully generated !");
								}
								catch (IOException ioe)
								{
									errorHandler(ioe);
								}
							}
							else if (filename.contains("T01")||filename.contains("T02")||filename.contains("T03")||filename.contains("T04")||filename.contains("T06"))
							{
								// This transformations produces outputwhich can be plot in 2D
								filename = filename.replace('.','_');
								filename = filename.concat(".m");
								//filename = filename.substring (0,transformedHistory.getElement(transformationFlag).getName().lastIndexOf ("."))+".m";
								filename = "../History/Matlab_Files/"+filename;
								String xlab="";
								try
								{
									if(filename.contains("T01"))
									{
										xlab="FFT ";
									}
									else if(filename.contains("T02"))
									{
										xlab="Hilbert Transform ";
									}
									else if(filename.contains("T03"))
									{
										xlab="Z Transform ";
									}
									else if(filename.contains("T04"))
									{
										xlab="Analyic Signal ";
									}
									else if(filename.contains("T06"))
									{
										xlab="ChirpZ Transform ";
									}

									FileWriter w = new FileWriter(filename);
									w.write("% This Matlab script is generated using BioSpectrogram.\n");
									w.write("figure;\n");
									w.write("val = importdata('"+transformedHistory.getElement(transformationFlag).getCanonicalPath()+"','%s');\n");
									w.write("ans = str2double(val);\n");
									w.write("subplot(2,1,1);\n");
									w.write("plot(abs(ans));\n");
									w.write("xlabel('Index');\n");
									w.write("ylabel('"+xlab+" Magnitude');\n");

									if(filename.contains("W_") || filename.contains("user_"))
									{
										w.write("title(sprintf('File :"+filen+".fasta , Encoding:"+fileenc+",  Tranformation:"+filet+" \\nGenerated using BioSpectrogram and Matlab'),'Interpreter','none');\n");
									}
									else
									{
										w.write("title(sprintf('Accession Number :"+filen+" , Encoding:"+fileenc+" , Tranformation:"+filet+" \\n Generated using BioSpectrogram and Matlab'),'Interpreter','none');\n");
									}

									w.write("subplot(2,1,2);\n");
									w.write("plot(angle(ans));\n");
									w.write("xlabel('Index');\n");
									w.write("ylabel('"+xlab+" Phase');\n");

									w.close();
									JOptionPane.showMessageDialog(app,"Matlab script file "+filename+" successfully generated !");
								}
								catch (IOException ioe)
								{
									errorHandler(ioe);
								}
							}
						}
					}
                	else
                	{
            			if(encodingFlag<encodedHistory.getSize())
            			{
            				String filename1=encodedHistory.getElement(encodingFlag).getName();
            				String filename=filename1.replace('.','_');
            				filename="../History/Matlab_Files/"+filename+".m";
            				try
            				{
            					FileWriter w=new FileWriter(filename);
            					w.write("% This Matlab script is generated using Biospectrogram.\n");
            					w.write("clear all;\nval=importdata('"+encodedHistory.getElement(encodingFlag).getCanonicalPath()+"');\n");
								w.write("fid=fopen('"+encodedHistory.getElement(encodingFlag).getCanonicalPath()+"');\n");

            				//	w.write("v=str2num(val.Sequence);\n");
            					w.write("tline = fgets(fid); \n tline = fgets(fid); \n v=[]; \n while ischar(tline)");

            					if(filename1.contains("E01.2") || filename1.contains("E01.3"))
            					{
            						w.write("\n v=[v,(tline)]; \n");

            					}
            					else
            					{
            						w.write("\n v=[v,str2num(tline)]; \n");

            					}
            					w.write("    tline = fgets(fid); \n end \n fclose(fid); \n");

            					if(filename1.contains("E03.1") ||filename1.contains("E03.2") || filename1.contains("E03.3"))
            					{
            						w.write("% x and y contain x co-ordinates and y co-ordinates for graphical representation. \n");
            						w.write("j=1; \n for i=1:(length(v)/2) \n    x(i)=v(j); \n    y(i)=v(j+1); \n    j=j+2; \n end \n");
								}
            					if(filename1.contains("E04.1"))
            					{
            						w.write("% x,y and z are coefficients for quarternion xi + yj + zk \n");
            						w.write("j=1; \n for i=1:(length(v)/3) \n    x(i)=v(j); \n    y(i)=v(j+1); \n z(i)=v(j+2); \n    j=j+3; \n end \n");
								}
            					if(filename1.contains("E04.2"))
            					{
            						w.write("% x,y,z and g are coefficients for quarternion x + yi + zj + gk \n");
            						w.write("j=1; \n for i=1:(length(v)/4) \n    x(i)=v(j); \n    y(i)=v(j+1); \n z(i)=v(j+2); \n g(i)=v(j+3); \n   j=j+4; \n end \n");
								}
            					if(filename1.contains("E01.2"))
            					{
            						w.write("v1=v;\nv2=v1(4:length(v1));\nfor i=1:length(v2)\n    if v2(i)~='X'\n    Xr(i)=v2(i);\n    else\n       break;\n    end\nend \nXr=str2num(Xr); \nv3=v2((i+3):length(v2)); \nfor i=1:length(v3)\n    if v3(i)~='X'\n    Xg(i)=v3(i);\n    else\n       break;\n    end\nend\nXg=str2num(Xg);\nv4=v3((i+3):length(v3));\nfor i=1:length(v4)\n    if v4(i)~='X'\n    Xb(i)=v4(i);\n    else\n       break;\n    end\nend\nXb=str2num(Xb);\n");
								}
            					if(filename1.contains("E01.3"))
            					{
            						w.write("v1=v;\nv2=v1(4:length(v1));\nfor i=1:length(v2)\n    if v2(i)~='y'\n    Xn(i)=v2(i);\n    else\n       break;\n    end\nend \nXn=str2num(Xn); \nv3=v2((i+3):length(v2)); \nfor i=1:length(v3)\n    if v3(i)~='z'\n    Yn(i)=v3(i);\n    else\n       break;\n    end\nend\nYn=str2num(Yn);\nv4=v3((i+3):length(v3));\nfor i=1:length(v4)\n    if v4(i)~='x'\n    Zn(i)=v4(i);\n    else\n       break;\n    end\nend\nZn=str2num(Zn);\n");

								}
            					w.close();
            					JOptionPane.showMessageDialog(app,"Matlab script file "+filename+" generated successfully!");
							}
            				catch(IOException ioe)
            				{
                        		errorHandler(ioe);
							}
						}
					}
					dialog.dispose();
				}
			});

			// Action Listener for "CANCEL" button
			cancelButton.addActionListener (new ActionListener()
			{
            	public void actionPerformed(ActionEvent e)
                {
					dialog.dispose ();
				}
			});

			panel.setLayout (new GridLayout(2,2));
            panel.add(label1);
            if(wind==1)
            {
            	panel.add(transformation);
            }
			else
			{
				panel.add(encoding);
			}

            panel.add(okButton);
			panel.add(cancelButton);
            panel.setBackground (Color.WHITE);
			panel.setBorder (new EtchedBorder(EtchedBorder.RAISED));
            dialog.getContentPane ().add(panel);
			dialog.setBounds (maxScreenWidth/3,maxScreenHeight/3,400,100);
            dialog.setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible (true);
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for "Clear History" Button
	*******************************************************************************/
	static class clearHistoryListener implements ActionListener
	{
		public void actionPerformed(ActionEvent clear)
		{
			int s11=JOptionPane.showOptionDialog(app,"All files in History will be deleted.Do you want to proceed?","Deleting History",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);

			if(s11==0)
			{
				File f1=new File("../History");
				boolean v=deleteDir(f1);
				if(v)
				{
					JOptionPane.showMessageDialog(app,"History cleared successfully.");
				}
				else
				{
					JOptionPane.showMessageDialog(app,"History not cleared.Some file in History Folder is being accessed by another application. Close that file and try again.");
				}
				fileHistory.dir.mkdir();
		    	fileHistory.subdir1.mkdir();
				fileHistory.subdir2.mkdir();
				fileHistory.subdir3.mkdir();
				fileHistory.subdir4.mkdir();
				fileHistory.subdir5.mkdir();
				fileHistory.subdir6.mkdir();
				fileHistory.subdir7.mkdir();
				fileHistory.subdir8.mkdir();
				fileHistory.subdir9.mkdir();
				fileHistory.subdir10.mkdir();
				fileHistory.subdir11.mkdir();
				fileHistory.subdir12.mkdir();
				fileHistory.subdir13.mkdir();
				fileHistory.subdir14.mkdir();

				fetchedHistory.updateFetchedFileList();
				encodedHistory.updateEncodedFileList();
				transformedHistory.updateTransformedFileList();
				proteinHistory.updateProteinFileList();

				backButton.setEnabled(false);
				back.setEnabled(false);

				topLabel.setText(" ");
				topLabel.updateUI();

				bottomLabel.setText(" ");
				bottomLabel.updateUI();
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for Button "ORFFinder"
	*******************************************************************************/
	static class orfListener implements ActionListener
	{
		public void actionPerformed(ActionEvent exit)
		{
			int option;
			option = JOptionPane.showConfirmDialog(app,"Do you want to enter sequence yourself?","",JOptionPane.YES_NO_OPTION);
			if (option == 0)
			{
				File output = new File("temp");
				String temp = JOptionPane.showInputDialog (app,"Enter the sequence : ");
				if (temp == null)
            	{
					// do something if required ! User has pressed cancel !
				}
            	else if(temp.equals(""))
            	{
	            	JOptionPane.showMessageDialog(app,"No sequence entered!");
            	}
            	else
            	{
	            	String name = JOptionPane.showInputDialog (app,"Give some name for the output file : ORF_");
	            	output.delete();
               		output = new File("../History/ORF/ORF_"+name+".txt");
            	}
            	ORFfinder orf1 = new ORFfinder(temp);
				try
				{
					FileWriter fw = new FileWriter(output);
					fw.write(orf1.compute());
					fw.close();
					JOptionPane.showMessageDialog(app,"Output File is available at "+output.getCanonicalPath()+
						"\n Please use text editor like Notepad++ for best vieweing");
				}
				catch(IOException ioe)
				{
					errorHandler(ioe);
				}
			}
			else	// Code when user selects one of the fetched files.
			{
				fetchedHistory.updateFileList();

				final JButton okButton=new JButton("OK");
				final JButton cancelButton=new JButton("Cancel");
				final JDialog dialog=new JDialog(app,"Select input file for ORF Finder",JDialog.ModalityType.DOCUMENT_MODAL);
				final JPanel panel1=new JPanel();
				JLabel label=new JLabel("Select File for ORF Finder:");
				fetchFlag=0;
				final JComboBox fetchedfiles;

				fetchedfiles = new JComboBox(fetchedHistory.getArray());

				fetchedfiles.addActionListener (new ActionListener()
				{
	            	public void actionPerformed(ActionEvent e)
	                {
						JComboBox temp = (JComboBox) e.getSource ();
	                    fetchFlag = temp.getSelectedIndex ();
					}
				});

            	okButton.addActionListener (new ActionListener()
				{
            		public void actionPerformed(ActionEvent e)
                	{
                		File output = new File("temp");
                		String name = fetchedHistory.getElement(fetchFlag).getName();
                		if (name.contains("genbank"))
                		{
							JOptionPane.showMessageDialog(app,"Please choose fasta file or enter DNA sequence !");
                		}
                		else
                		{
                			String temp = getOnlySequence(fetchFlag,1).toString();
                			output.delete();
	               			output = new File("../History/ORF/ORF_"+name+".txt");

	               			temp = temp.replaceAll("\n","");
							temp = temp.replaceAll("\0","");
	               			ORFfinder orf1 = new ORFfinder(temp);
							try
							{
								FileWriter fw = new FileWriter(output);
								fw.write(orf1.compute());
								fw.close();
								orf1.generateORFFasta(fetchFlag);
								JOptionPane.showMessageDialog(app,"Output File listing all ORF is available at "+output.getCanonicalPath()+
									"\n Please use text editor like Notepad++ for best vieweing\n");
							}
							catch(IOException ioe)
							{
								errorHandler(ioe);
							}
                		}

						dialog.dispose();
                	}
				});

				cancelButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dialog.dispose();
					}
				});

				panel1.setLayout (new GridLayout(2,2));
	            panel1.add(label);
	            panel1.add(fetchedfiles);
	            panel1.setBackground (Color.WHITE);
	            panel1.setBorder (new EtchedBorder(EtchedBorder.RAISED));
	            panel1.add(okButton);
	            panel1.add(cancelButton);
	            dialog.getContentPane ().add(panel1);
	            dialog.setBounds(maxScreenWidth/3,maxScreenHeight/3,400,100);
	            dialog.setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
	            dialog.setVisible(true);
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for Button "toGene"
	*******************************************************************************/
	static class geneListener implements ActionListener
	{
		public void actionPerformed(ActionEvent exit)
		{
			fetchedHistory.updateFileList();

			final JButton okButton=new JButton("OK");
			final JButton cancelButton=new JButton("Cancel");
			final JDialog dialog=new JDialog(app,"Select file for gene conversion",JDialog.ModalityType.DOCUMENT_MODAL);
			final JPanel panel1=new JPanel();
			JLabel label=new JLabel("Select File for gene conversion:");
			fetchFlag=0;
			final JComboBox fetchedfiles;

			fetchedfiles = new JComboBox(fetchedHistory.getArray());

			fetchedfiles.addActionListener (new ActionListener()
			{
            	public void actionPerformed(ActionEvent e)
                {
					JComboBox temp = (JComboBox) e.getSource ();
                    fetchFlag = temp.getSelectedIndex ();
				}
			});

        	okButton.addActionListener (new ActionListener()
			{
        		public void actionPerformed(ActionEvent e)
            	{
            		File output = new File("temp");
            		String temp1 = fetchedHistory.getElement(fetchFlag).getName();
            		if (temp1.contains("genbank"))
            		{
						JOptionPane.showMessageDialog(app,"Please choose fasta file or enter DNA sequence !");
            		}
            		else
            		{
            			String temp = getOnlySequence(fetchFlag,1).toString();
               			temp = temp.replaceAll("\n","");
						temp = temp.replaceAll("\0","");
            			JOptionPane.showMessageDialog(app,"There are "+temp.length()+" base pairs in selected DNA.");
            			String a = JOptionPane.showInputDialog (app,"Enter Starting Index(1-"+temp.length()+") : ");
            			String b = JOptionPane.showInputDialog (app,"Enter Ending Index(1-"+temp.length()+") : ");
            			int startIndex = Integer.parseInt(a);
            			int stopIndex = Integer.parseInt(b);
            			temp = temp.substring(startIndex-1,stopIndex);
            			String name = "Gene_"+startIndex+"_"+stopIndex+"_"+fetchedHistory.getElement(fetchFlag).getName();
            			output.delete();
	               		output = new File("../History/Protein/"+name);

	               		toGene cnvt = new toGene(temp);
						try
						{
							FileWriter fw = new FileWriter(output);
							fw.write("> Created by taking output of ORFinder as input for protein generator function using standard genetic code.\n");
							fw.write(cnvt.convertToGene());
							fw.close();
							if (cnvt.getFlag())
								JOptionPane.showMessageDialog(app,"Output File will be available at "+output.getCanonicalPath());
						}
						catch(IOException ioe)
						{
							errorHandler(ioe);
						}
            		}

					dialog.dispose();
            	}
			});

			cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					dialog.dispose();
				}
			});

			panel1.setLayout (new GridLayout(2,2));
            panel1.add(label);
            panel1.add(fetchedfiles);
            panel1.setBackground (Color.WHITE);
            panel1.setBorder (new EtchedBorder(EtchedBorder.RAISED));
            panel1.add(okButton);
            panel1.add(cancelButton);
            dialog.getContentPane ().add(panel1);
            dialog.setBounds(maxScreenWidth/3,maxScreenHeight/3,400,100);
            dialog.setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for Tools Menu Item "Exit"
	*******************************************************************************/
	static class exitListener implements ActionListener
	{
		public void actionPerformed(ActionEvent exit)
		{
			System.exit(0);
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for Help Menu Item "User Manual"
	*******************************************************************************/
	static class manualListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ex)
        {
			Desktop desktop = Desktop.getDesktop();
			try
			{
				File temp = new File("../Help/Manual.pdf");
				desktop.open(new File(temp.getCanonicalPath()));
			}
            catch(IllegalArgumentException iae)
			{
            	errorHandler(iae);
			}
            catch(IOException ioe)
			{
            	errorHandler(ioe);
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for Help Menu Item "Software Update"
	*******************************************************************************/

	static class softUpdateListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ex)
		{
			Desktop desktop = Desktop.getDesktop();
			try
			{
				URI updateLink = new URI("http://guptalab.org/biospectrogram/downloads.htm");
				desktop.browse(updateLink);
			}
			catch (URISyntaxException use)
			{
				errorHandler(use);
			}
			catch (IOException ioe)
			{
				errorHandler(ioe);
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for Help Menu Item "Codes"
	*******************************************************************************/
	static class conventionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ex)
        {
			Desktop desktop = Desktop.getDesktop();
			try
			{
				File temp = new File("../Help/Codes.pdf");
				desktop.open(new File(temp.getCanonicalPath()));
			}
            catch(IllegalArgumentException iae)
			{
            	errorHandler(iae);
			}
            catch(IOException ioe)
			{
            	errorHandler(ioe);
			}
		}
	}

	/*******************************************************************************
	 *	This static class implements action listener for Help Menu Item "Product Demo"
	*******************************************************************************/
	static class demoListener implements ActionListener
    {
		public void actionPerformed(ActionEvent e)
		{
			Desktop desktop = Desktop.getDesktop();
			try
			{
				URI updateLink = new URI("http://guptalab.org/biospectrogram/demo.htm");
				desktop.browse(updateLink);
			}
			catch (URISyntaxException use)
			{
				errorHandler(use);
			}
			catch (IOException ioe)
			{
				errorHandler(ioe);
			}
		}
    }

    	/*******************************************************************************
	 *	This static class implements action listener for Help Menu Item "Feedback"
	*******************************************************************************/
	static class feedbackListener implements ActionListener
    {
		public void actionPerformed(ActionEvent ae)
		{
			Desktop desktop = Desktop.getDesktop();
			try
			{
				URI updateLink = new URI("https://docs.google.com/forms/d/1YGu_I9z7Z56oAP1enGByBahqs-ItHbLqnBwCoJouOro/viewform");
				desktop.browse(updateLink);
			}
			catch (URISyntaxException use)
			{
				errorHandler(use);
			}
			catch (IOException ioe)
			{
				errorHandler(ioe);
			}
		}
	}


	/*******************************************************************************
	 *	This static class implements action listener for Help Menu Item "About Us"
	*******************************************************************************/
	static class aboutListener implements ActionListener
    {
		public void actionPerformed(ActionEvent e)
		{
			JDialog dialog = new JDialog(app,"About BioSpectrogram",JDialog.ModalityType.DOCUMENT_MODAL);
			JButton credits = new JButton("Credits");
			JButton homepage = new JButton("Homepage");
			credits.setBorder(new LineBorder(Color.BLACK,2,false));
			credits.addActionListener (new ActionListener()
        	{
				public void actionPerformed(ActionEvent ex)
				{
					Desktop desktop = Desktop.getDesktop();
                	try
					{
						File temp = new File("../Help/Credits.pdf");
						desktop.open(new File(temp.getCanonicalPath()));
					}
                	catch(IllegalArgumentException iae)
					{
                		errorHandler(iae);
					}
                	catch(IOException ioe)
					{
                		errorHandler(ioe);
					}
				}
			});

			homepage.addActionListener (new ActionListener()
        	{
        		public void actionPerformed(ActionEvent ex)
				{
					Desktop desktop = Desktop.getDesktop();
					try
					{
						URI updateLink = new URI("http://guptalab.org/biospectrogram/index.htm");
						desktop.browse(updateLink);
					}
					catch (URISyntaxException use)
					{
						errorHandler(use);
					}
					catch (IOException ioe)
					{
						errorHandler(ioe);
					}
				}
        	});

			String version = "<html><center><h1>BioSpectrogram</h1> <br/> Version : 1.0<br/>"+
				"Copyright \u00a9 2012 Manish K. Gupta </center></html>";
			JLabel label1 = new JLabel(version,new ImageIcon("../Icons/logo.png"),JLabel.CENTER);

			label1.setBorder(new BevelBorder(BevelBorder.RAISED));
			label1.setOpaque(true);
			label1.setBackground (Color.WHITE);

			JPanel jp = new JPanel(new GridLayout(1,2));

			credits.setMaximumSize(new Dimension(45,60));
			credits.setMinimumSize(new Dimension(45,60));
			credits.setBackground(Color.white);
			credits.setBorder(new BevelBorder(BevelBorder.RAISED));

			homepage.setMaximumSize(new Dimension(45,60));
			homepage.setMinimumSize(new Dimension(45,60));
			homepage.setBackground(Color.white);
			homepage.setBorder(new BevelBorder(BevelBorder.RAISED));

			jp.add(credits);
			jp.add(homepage);

			dialog.add(jp, BorderLayout.SOUTH);
			dialog.add(label1,BorderLayout.CENTER);
            dialog.setBounds(maxScreenWidth/3,maxScreenHeight/3,400,200);
            dialog.setResizable(false);
			dialog.setVisible(true);
		}
	}
}