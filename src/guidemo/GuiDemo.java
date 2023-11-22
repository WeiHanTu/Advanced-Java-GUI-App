package guidemo;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * A frame that displays a multiline text, possibly with a background image
 * and with added icon images, in a DrawPanel, along with a variety of controlls.
 */
public class GuiDemo extends JFrame{
	
	/**
	 * The main program just creates a GuiDemo frame and makes it visible.
	 */
	public static void main(String[] args){
		JFrame frame = new GuiDemo();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private DrawPanel drawPanel;
	private SimpleFileChooser fileChooser;
	private TextMenu textMenu;
	private JCheckBoxMenuItem gradientOverlayCheckbox = new JCheckBoxMenuItem("Gradient Overlay", true);

	/**
	 * The constructor creates the frame, sizes it, and centers it horizontally on the screen.
	 */
	public GuiDemo() {
		
		super("Sayings");  // Specifies the string for the title bar of the window.
		final JPanel content = new JPanel();  // To hold the content of the window.
		content.setBackground(Color.LIGHT_GRAY);
		content.setLayout(new BorderLayout());
		this.setContentPane(content);
		
		// Create the DrawPanel that fills most of the window, and customize it.
		
		drawPanel = new DrawPanel();
		drawPanel.getTextItem().setText(
				"When I am down, these words encourage me:\n" +
				"       “It does not matter how slowly you go\n" +
				" so long as you do not stop.”\n" +
				"                                   by Andy Warhol"
			);
		drawPanel.getTextItem().setLineHeightMultiplier(1.8);
		drawPanel.getTextItem().setFontSize(36);
		drawPanel.getTextItem().setColor(Color.yellow);
		drawPanel.getTextItem().setJustify(TextItem.LEFT);
		drawPanel.setBackgroundImage(Util.getImageResource("resources/images/supernova.jpeg"));
		content.add(this.drawPanel, BorderLayout.CENTER);
		
		// Add an icon toolbar to the SOUTH position of the layout	
		final IconSupport iconSupport = new IconSupport(drawPanel);
		content.add(iconSupport.createToolbar(true), BorderLayout.SOUTH );
        // Create the tool bar to  NORTH position of the layout
		content.add(this.makeToolbar(), "North");
		
		// Create the menu bar and add it to the frame.  The TextMenu is defined by
		// a separate class.  The other menus are created in this class.
		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(makeFileMenu());
		this.textMenu = new TextMenu(drawPanel);
		menuBar.add(textMenu );
		menuBar.add( makeBackgroundMenu() );
		this.setJMenuBar(menuBar);
		menuBar.add(iconSupport.createMenu());//Create constructor of the menu bar for "Make a Menu".
		
		// Set the size of the window and its position.
		
		this.pack();  // Size the window to fit its content.
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - getWidth())/2, 50);
		
		// Create and customize the file chooser that is used for file operations.
		
		this.fileChooser = new SimpleFileChooser();
		try { // I'd like to use the Desktop folder as the initial folder in the file chooser.
			String userDir = System.getProperty("user.home");
			if (userDir != null) {
				File desktop = new File(userDir,"Desktop");
				if (desktop.isDirectory())
					fileChooser.setDefaultDirectory(desktop);
			}
		}
		catch (Exception e) {
		}

	} // end constructor
	
	/**
	 * Create the "File" menu from actions that are defined later in this class.
	 */
	private JMenu makeFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(newPictureAction);
		menu.add(saveImageAction);
		menu.addSeparator();
		menu.add(quitAction);
		return menu;
	}
	
	/**
	 * Create the "Background" menu, using objects of type ChooseBackgroundAction,
	 * a class that is defined later in this file.
	 */
	private JMenu makeBackgroundMenu() {
		JMenu menu = new JMenu("Background");
		menu.add(new ChooseBackgroundAction("Supernova"));
		menu.add(new ChooseBackgroundAction("Mandelbrot"));
		menu.add(new ChooseBackgroundAction("Earthrise"));
		menu.add(new ChooseBackgroundAction("Sunset"));
		menu.add(new ChooseBackgroundAction("Cloud"));
		menu.add(new ChooseBackgroundAction("Eagle_nebula"));
		menu.addSeparator();
		menu.add(new ChooseBackgroundAction("Custom..."));
		menu.addSeparator();
		menu.add(new ChooseBackgroundAction("Color..."));
		menu.addSeparator();
		menu.add(gradientOverlayCheckbox);
		gradientOverlayCheckbox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (gradientOverlayCheckbox.isSelected())
					drawPanel.setGradientOverlayColor(Color.WHITE);
				else
					drawPanel.setGradientOverlayColor(null);
			}
		});
		return menu;
	}
	
	/**
	 * Add a new toolbar with the same functionality as the "Background" menu.
	 * @return new JToolBar object
	 */
    private JToolBar makeToolbar() {
        JToolBar toolbar = new JToolBar();
        this.newPictureAction.putValue("ShortDescription", "Reset the image to its original condition.");
        toolbar.add(this.newPictureAction);
        this.saveImageAction.putValue("ShortDescription", "Save the image to a file.");
        toolbar.add(this.saveImageAction);
		toolbar.addSeparator(new Dimension(20, 0));
		toolbar.add(new ChooseBackgroundAction("Supernova"));
        toolbar.add(new ChooseBackgroundAction("Mandelbrot"));
		toolbar.add(new ChooseBackgroundAction("Earthrise"));
		toolbar.add(new ChooseBackgroundAction("Sunset"));
		toolbar.add(new ChooseBackgroundAction("Cloud"));
		toolbar.add(new ChooseBackgroundAction("Eagle_nebula"));
		toolbar.addSeparator();
		toolbar.add(new ChooseBackgroundAction("Custom..."));
		toolbar.addSeparator();
		toolbar.add(new ChooseBackgroundAction("Color..."));

        return toolbar;
    }
	
	
	private AbstractAction newPictureAction = 
		new AbstractAction("New", Util.iconFromResource("resources/action_icons/fileopen.png")) {
		    public void actionPerformed(ActionEvent evt) {
		    	drawPanel.clear();
		    	gradientOverlayCheckbox.setSelected(true);
		    	textMenu.setDefaults();
		    }
		};
	
	private AbstractAction quitAction = 
		new AbstractAction("Quit", Util.iconFromResource("resources/action_icons/exit.png")) {
		    public void actionPerformed(ActionEvent evt) {
		    	System.exit(0);
		    }
		};
		
	private AbstractAction saveImageAction = 
		new AbstractAction("Save Image...", Util.iconFromResource("resources/action_icons/filesave.png")) {
		    public void actionPerformed(ActionEvent evt) {
		    	File f = fileChooser.getOutputFile(drawPanel, "Select Ouput File", "saying.jpeg");
		    	if (f != null) {
		    		try {
		    			BufferedImage img = drawPanel.copyImage();
		    			String format;
		    			String fileName = f.getName().toLowerCase();
		    			if (fileName.endsWith(".png"))
		    				format = "PNG";
		    			else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg"))
		    				format = "JPEG";
		    			else {
		    				JOptionPane.showMessageDialog(drawPanel,
		    						"The output file name must end wth\n.png or .jpeg.");
		    				return;
		    			}
		    			ImageIO.write(img,format,f);
		    		}
		    		catch (Exception e) {
		    			JOptionPane.showMessageDialog(drawPanel, "Sorry, the image could not be saved.");
		    		}
		    	}
		    }
    	};
    	
    	
    /**
     * An object of type ChooseBackgroudnAction represents an action through which the
     * user selects the background of the picture.  There are three types of background:
     * solid color background ("Color..." command), an image selected by the user from 
     * the file system ("Custom..." command), and four built-in image resources
     * (Mandelbrot, Earthrise, Sunset, and Eagle_nebula).
     */
    private class ChooseBackgroundAction extends AbstractAction {
    	String text;
    	ChooseBackgroundAction(String text) {
    		super(text);
    		this.text = text;
    		if (!text.equals("Custom...") && !text.equals("Color...")) {
    			putValue(Action.SMALL_ICON, 
    					Util.iconFromResource("resources/images/" + text.toLowerCase() + "_thumbnail.jpeg"));
    		}
    		 // Create a BufferedImage, use it to make an ImageIcon, and use that icon for the "Color..." action.
    		if (text.equals("Color...")) {
                final BufferedImage img = new BufferedImage(32, 32, 1);
                final Graphics g = img.getGraphics();
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, 32, 32);
                g.setColor(Color.RED);
                g.fillRect(2, 2, 10, 30);
                g.setColor(Color.GREEN);
                g.fillRect(12, 2, 10, 30);
                g.setColor(Color.BLUE);
                g.fillRect(22, 2, 10, 30);
                g.dispose();

    			this.putValue(Action.SMALL_ICON, new ImageIcon(img));
    			this.putValue(Action.SHORT_DESCRIPTION, "<html>Use a solid color for background<br>instead of an image.</html>");
    		}
    		else if (text.equals("Custom...")) {
    			putValue(Action.SMALL_ICON,
    					Util.iconFromResource("resources/action_icons/fileopen.png"));
    			putValue(Action.SHORT_DESCRIPTION, "<html>Select an image file<br>to use as the background.</html>");
    		}
    		else
    			putValue(Action.SHORT_DESCRIPTION, "Use this image as the background.");
    			
    	}

		public void actionPerformed(ActionEvent evt) {
			if (text.equals("Custom...")) {
				File inputFile = fileChooser.getInputFile(drawPanel, "Select Background Image");
				if (inputFile != null) {
					try {
						BufferedImage img = ImageIO.read(inputFile);
						if (img == null)
							throw new Exception();
						drawPanel.setBackgroundImage(img);
					}
					catch (Exception e) {
						JOptionPane.showMessageDialog(drawPanel, "Sorry, couldn't read the file.");
					}
				}
			}
			else if (text.equals("Color...")) {
				Color c = JColorChooser.showDialog(drawPanel, "Select Color for Background", drawPanel.getBackground());
				if (c != null) {
					drawPanel.setBackground(c);
					drawPanel.setBackgroundImage(null);
				}
			}
			else {
				Image bg = Util.getImageResource("resources/images/" + text.toLowerCase() + ".jpeg");
				drawPanel.setBackgroundImage(bg);
			}
		}
    }

}
