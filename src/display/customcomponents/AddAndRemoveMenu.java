/**
 * [AddAndRemoveMenu.java]
 * Custom menu that allows the user to add and remove fields
 * Most importantly it is extremely good looking ;)
 * @author Yash Arora
 * December 2 2018
 */

package display.customcomponents;

import java.awt.Dimension; 
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import datamanagement.SignInManager;
import utilities.SinglyLinkedList;
import utilities.Utils;

/**
 * Custom menu that allows the user to add and remove fields
 * @author Yash Arora
 */
public class AddAndRemoveMenu extends JPanel {
	
	// Important class variables
	private String title;
	private ArrayList<String> items;
	private SignInManager manager;
	private SinglyLinkedList<CustomButton> itemButtons = new SinglyLinkedList<CustomButton>();
	private SinglyLinkedList<CustomButton> xButtons = new SinglyLinkedList<CustomButton>();
	private CustomTextField addItem;
	private CustomButton submitButton;
	private String message = "";

	/**
	 * Constructor
	 * @param manager SignInManager that handles students
	 * @param items List of all the items in the menu
	 * @param title String name of the menu
	 * @param maxY maximum size of the menu
	 */
	public AddAndRemoveMenu(SignInManager manager, ArrayList<String> items, String title, int maxY) {
		this.manager = manager;
		this.items = items;
		this.title = title;
		this.setPreferredSize(new Dimension(Utils.scale(300) + Utils.scale(400), maxY));
		this.addMouseListener(new MyMouseListener());
		this.setOpaque(false);
		
		// Create text field
		addItem = new CustomTextField("Add " + title);
		setVisible(true);
		
		// Put everything on a pane
		JLayeredPane pane = new JLayeredPane();
		pane.setPreferredSize(new Dimension(getPreferredSize()));

		// Create all the buttons
		initializeButtons();

		// Custom text field to add items
		addItem.setBounds(Utils.scale(300) + Utils.scale(50) + Utils.scale(20)*2, 0, addItem.getPreferredSize().width,
				addItem.getPreferredSize().height);
		addItem.setOpaque(false);
		pane.add(addItem);

		add(pane);

	}

    /**
     * paintComponent
     * draws all buttons and text on screen
     * @param g Graphics
     */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Title button
		CustomButton titleButton = new CustomButton(title + " List", Utils.scale(50), 0, Utils.scale(300), Utils.scale(50),
				Utils.colours[3]);
		titleButton.setSelectable(false);
		titleButton.draw(g, this);

		// Draw all the item buttons
		for (int i = 0; i < items.size(); i++) {
			itemButtons.get(i).draw(g, this);
			xButtons.get(i).draw(g, this);
		}
		
		// Set the ones that are selected
		for (int i = 0; i < xButtons.size(); i++) {
			if (isMouseOnButton(xButtons.get(i))) {
				itemButtons.get(i).setSelected(true);
			} else {
				itemButtons.get(i).setSelected(false);
			}
		}

		// Submit button
		submitButton = new CustomButton("Submit", addItem.getX() + addItem.getPreferredSize().width/2 - Utils.scale(300)/2,
				addItem.getPreferredSize().height + Utils.scale(20), Utils.scale(300), Utils.scale(50),
				Utils.colours[3]);
		submitButton.draw(g, this);

		repaint();
	}

	/**
	 * isMouseOnPanel
	 * finds if the mouse is on the panel
	 * @param panel that is the jpanel screen currently being displayed
	 * @return true if the mouse is on the panel or false if not
	 * @throws IllegalComponentStateException
	 */
	public boolean isMouseOnPanel(JPanel panel) throws IllegalComponentStateException {
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		Point relScreenLocation = panel.getLocationOnScreen().getLocation();
		int x = (int) Math.round(mouseLocation.getX() - relScreenLocation.getX());
		int y = (int) Math.round(mouseLocation.getY() - relScreenLocation.getY());

		return ((x >= 0) && (x <= panel.getWidth()) && (y >= 0) && (y <= panel.getHeight()));
	}

	/**
	 * isMouseOnButton 
	 * checks whether or not the cursor is over the button
	 * @param b custom button to check
	 * @return true = is on button, false = not on button
	 * @throws IllegalComponentStateException
	 */
	public boolean isMouseOnButton(CustomButton b) throws IllegalComponentStateException {
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		Point relScreenLocation = this.getLocationOnScreen().getLocation();
		int x = (int) Math.round(mouseLocation.getX() - relScreenLocation.getX());
		int y = (int) Math.round(mouseLocation.getY() - relScreenLocation.getY());

		return ((x >= b.x) && (x <= b.x + b.width) && (y >= b.y) && (y <= b.y + b.height));
	}

	/**
	 * updateSpacing
	 * updates the field look when someone is added or removed
	 */
	private void updateSpacing() {
		for (int i = 0; i < items.size(); i++) {
			itemButtons.get(i).y = Utils.scale(50) * (i + 1);
			xButtons.get(i).y = Utils.scale(50) * (i + 1);
		}
	}
	
	/**
	 * alphabetize
	 * puts the menu in alphabetical order and redisplays
	 */
	private void alphabetize() {
		Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
		itemButtons.clear();
		xButtons.clear();
		initializeButtons();
	}
	
	// Creates all the buttons in the menu to be drawn
	private void initializeButtons() {
		
		// One X and one item button for each item in the list
		for (int i = 0; i < items.size(); i++) {
			CustomButton b = new CustomButton(items.get(i), Utils.scale(50), Utils.scale(50) * (i + 1),
					Utils.scale(300), Utils.scale(50), Utils.colours[4]);
			b.setSelectable(false);
			CustomButton x = new CustomButton("X", 0, Utils.scale(50) * (i + 1), Utils.scale(50), Utils.scale(50),
					Utils.colours[2]);

			// Add them to the list
			itemButtons.add(b);
			xButtons.add(x);
		}
	}
	
	/**
	 * getMessage
	 * tells user if they have been successful or not
	 * @return message string that tells the user if they have been successful
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * updateTxt 
	 * updates the list of the SERTS in the manager
	 */
	private void updateTxt() {
		SinglyLinkedList<String> sertList = new SinglyLinkedList<>();
		for (String sert : items) {
			sertList.add(sert);
		}

		manager.setSerts(sertList);
	}

	private class MyMouseListener implements MouseListener {

        /**
         * mouseClicked
         * changes jpanel based on button clicked or performs an action based on button pressed
         *
         * @param e MouseEvent
         */
		public void mouseClicked(MouseEvent e) {
			
			// Remove if on the x button
			for (int i = 0; i < xButtons.size(); i++) {
				if (isMouseOnButton(xButtons.get(i))) {
					message = "\"" + items.get(i) + "\" was successfuly removed.";
					items.remove(i);
					itemButtons.remove(i);
					xButtons.remove(i);
				}
				updateSpacing();
				updateTxt();
			}

			// Submit if on the submit button
			if (isMouseOnButton(submitButton)) {
				if (addItem.getText().isEmpty()) {
					message = "Please enter a field to be added.";
				} else if (items.contains(addItem.getText())) {
					message = "\"" + addItem.getText() + "\" has already been added.";
				} else {
					items.add(addItem.getText());
					itemButtons.add(new CustomButton(addItem.getText(), Utils.scale(50), 0,
							Utils.scale(300), Utils.scale(50), Utils.colours[3]));
					xButtons.add(new CustomButton("X", 0, 0, Utils.scale(50), Utils.scale(50),
							Utils.colours[3]));
					alphabetize();
					updateSpacing();
					message = "\"" + addItem.getText() + "\" was successfuly added.";
					addItem.reset();
				}
				updateTxt();
			}
		}
		
		public void mouseEntered(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}

}
