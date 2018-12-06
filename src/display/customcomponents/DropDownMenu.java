package display.customcomponents;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import utilities.Utils;


public class DropDownMenu extends JPanel {
	String selectedText = "Select";
	String title;

    String[] items;
	
	public DropDownMenu(String[] items, String title) {
		this.items = items;
		this.title = title;
		this.setPreferredSize(new Dimension(Utils.scale(180), Utils.scale(30)*2));
        setVisible(true);
	}
	
	public String getSelectedText() {
		return selectedText;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		CustomButton titleButton = new CustomButton(title, 0, 0, Utils.scale(180), Utils.scale(30), Utils.colours[3]);
		titleButton.setSelectable(false);
		titleButton.draw(g, this);
		
		CustomButton select = new CustomButton(selectedText, 0, Utils.scale(30), Utils.scale(180), Utils.scale(30), Utils.colours[4]);
		select.setSelectable(false);
		if (isMouseOnPanel(this)) {
			setSize(Utils.scale(180), Utils.scale(30) * (items.length + 2));
		} else {
			setSize(this.preferredSize());
		}
		
		select.draw(g, this);
		
		for (int i = 1; i <= items.length; i++) {
			CustomButton b = new CustomButton(items[i-1], 0, (i * Utils.scale(30)) + Utils.scale(30), Utils.scale(180), Utils.scale(30));
			if (b.isMouseOnButton(this) && isMouseOnPanel(this)) {
				selectedText = items[i-1];
			}
			b.draw(g, this);
		}
				
		repaint();
	}

	public boolean isMouseOnPanel(JPanel panel) throws IllegalComponentStateException{
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point relScreenLocation = panel.getLocationOnScreen().getLocation();
        int x = (int) Math.round(mouseLocation.getX() - relScreenLocation.getX());
        int y = (int) Math.round(mouseLocation.getY() - relScreenLocation.getY());

        return ((x >= 0) && (x <= panel.getWidth()) && (y >= 0) && (y <= panel.getHeight()));
        //return ((x >= panel.getX()) && (x <= panel.getX() + panel.getWidth()) && (y >= panel.getY()) && (y <= panel.getY() + panel.getHeight()));
	}

	public void drop() {
		setSize(this.preferredSize());
	}
	
	public void reset() {
		selectedText = "Select";
	}

}
