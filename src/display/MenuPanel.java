package display;
import utilities.Utils; 

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class MenuPanel extends JPanel {
    private Window display;
    private boolean inWindow = false;
    public final int maxX;
    public final int maxY;
    private int highlight = -1;
    private static final int BACKBUTTON_WIDTH = 150;
    private static final int BACKBUTTON_HEIGHT = 80;

    MenuPanel(Window display) {
        this.display = display;
        this.maxX = display.maxX;
        this.maxY = display.maxY;
        this.addMouseListener(new MyMouseListener());
    }

    public void paintComponent(Graphics g) {
        g.setColor(new Color(191, 191, 191));
        g.fillRect(0, 0, maxX / 2, maxY);
        g.setColor(new Color(131, 131, 131));
        g.fillRect(maxX / 2, 0, maxX / 2, maxY);

        Font mainFont = Utils.getFont("assets/Hind-Light.ttf", 50.0f);
        Font buttonFont = Utils.getFont("assets/Hind-Light.ttf", 38.0f);
        FontMetrics mainFontMetrics = g.getFontMetrics(mainFont);
        FontMetrics buttonFontMetrics = g.getFontMetrics(buttonFont);

        g.setColor(new Color(91, 200, 203));
        if (highlight == 0) {
            g.setColor(new Color(131, 131, 131));
            g.fillRect(maxX / 4 - mainFontMetrics.stringWidth("Sign In") / 2 - 20,
                    maxY / 2 - 3 * mainFontMetrics.getHeight() / 4 - 10,
                    mainFontMetrics.stringWidth("Sign In") + 40,
                    3 * mainFontMetrics.getHeight() / 4 + 10);
            g.setColor(new Color(91, 200, 203));
        } else if (highlight == 1) {
            g.setColor(new Color(191, 191, 191));
            g.fillRect(3 * maxX / 4 - mainFontMetrics.stringWidth("Sign Out") / 2 - 20,
                    maxY / 2 - 3 * mainFontMetrics.getHeight() / 4 - 10,
                    mainFontMetrics.stringWidth("Sign Out") + 40,
                    3 * mainFontMetrics.getHeight() / 4 + 10);
            g.setColor(new Color(91, 200, 203));
        } else if (highlight == 2) {
            g.setColor(new Color(40, 40, 40));
        }
        g.fillRect(40,40,BACKBUTTON_WIDTH,BACKBUTTON_HEIGHT);
        g.setColor(new Color(255, 255, 255));
        g.setFont(buttonFont);
        g.drawString("Back",BACKBUTTON_WIDTH/2+40-buttonFontMetrics.stringWidth("Back")/2, BACKBUTTON_HEIGHT/2+40+buttonFontMetrics.getMaxAscent()/4);

        g.setFont(mainFont);
        g.setColor(new Color(15, 147, 116));
        g.drawString("Sign In", maxX/4-mainFontMetrics.stringWidth("Sign In")/2, maxY/2-mainFontMetrics.getHeight()/4);
        g.setColor(new Color(114, 189, 164));
        g.drawString("Sign Out", 3*maxX/4-mainFontMetrics.stringWidth("Sign Out")/2, maxY/2-mainFontMetrics.getHeight()/4);

        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point relScreenLocation = this.getLocationOnScreen().getLocation();
        int x = (int) Math.round(mouseLocation.getX()-relScreenLocation.getX());
        int y = (int) Math.round(mouseLocation.getY()-relScreenLocation.getY());
        if (inWindow) {
            if ((x >= 40) && (x <= 40 + BACKBUTTON_WIDTH) && (y >= 40) && (y <= 40 + BACKBUTTON_HEIGHT)) {
                highlight = 2;
            } else if ((x < maxX / 2) && (y < maxY)) {
                highlight = 0;
            } else if ((x < maxX) && (y < maxY)) {
                highlight = 1;
            }
        } else {
            highlight = -1;
        }

        repaint();
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e){
            inWindow = true;
        }
        public void mouseClicked(MouseEvent e) {
            if ((e.getX() >= 40) && (e.getX() <= 40 + BACKBUTTON_WIDTH) && (e.getY() >= 40) && (e.getY() <= 40 + BACKBUTTON_HEIGHT)){
                display.changeState(0);
            } else if (e.getX() < maxX/2){
                display.changeState(2);
            } else {
                display.changeState(3);
            }
        }
        public void mousePressed(MouseEvent e){

        }
        public void mouseExited(MouseEvent e){
            inWindow = false;
        }
        public void mouseReleased(MouseEvent e){

        }
    }

}
