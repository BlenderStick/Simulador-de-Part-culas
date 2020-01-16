package com.collisionsimulator.simulator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;

public class UniverseViewer extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public double x, y, angle = 0;
	public double scale = 1;
	
	private Universe universe;
	
	private ML ml;
	
	public UniverseViewer() {
		ml = new ML();
		this.addMouseListener(ml);
		this.addMouseMotionListener(ml);
		this.addMouseWheelListener(ml);
		this.addKeyListener(new KL());
	}
	
	public synchronized void paintComponent(Graphics graphics){
		universe.draw((Graphics2D) graphics.create(), x, y, getWidth(), getHeight(), angle, scale);
	}
	
	public void setUniverse(Universe newUniverse){universe = newUniverse;universe.addViewer(this);}
	
	private void updateMouseUniverse(double x, double y){
		if(universe != null)
			if(universe.viewers.get(0) == this)
				universe.setMouse(((x - getWidth() / 2d) / scale) + this.x, ((y - getHeight() / 2d) / scale) + this.y);
	}
	
	private class ML implements MouseListener, MouseMotionListener, MouseWheelListener{
		
		public double oldMouseX, oldMouseY, oldX, oldY;
		private boolean control = false;
		
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
//			int oldRealX = (int) ((x-(getWidth()/2-e.getX()))/scale);
//			int oldRealY = (int) ((y-(getHeight()/2-e.getY()))/scale);
			scale -= (e.getWheelRotation()/10d*scale);
//			int newRealX = (int) ((x-(getWidth()/2-e.getX()))/scale);
//			int newRealY = (int) ((y-(getHeight()/2-e.getY()))/scale);
			
//			x -= (int) ((oldRealX-x)-(oldRealX-newRealX));
//			y -= (int) ((oldRealY-y)-(oldRealY-newRealY));
//			x = x+(int)(oldRealX-newRealX);
//			y = y+(int)(oldRealY-newRealY);
//			System.out.println("StartX = "+oldRealX);
//			System.out.println("EndX   = "+newRealX);
//			System.out.println("Distance "+(newRealX-oldRealX));
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (control) {
				x = (oldX + (oldMouseX - e.getX()) / scale);
				y = (oldY + (oldMouseY - e.getY()) / scale);

				repaint();
				
			}
		}

		@Override public void mouseMoved(MouseEvent e) {updateMouseUniverse(e.getX(), e.getY());}

		@Override public void mouseClicked(MouseEvent e) {}

		@Override public void mouseEntered(MouseEvent e) {}

		@Override public void mouseExited(MouseEvent e) {}

		@Override public void mousePressed(MouseEvent e) {
			if(e.isControlDown() || e.getButton() == MouseEvent.BUTTON2){
				control = true;
				oldMouseX = e.getX();
				oldMouseY = e.getY();
				oldX = x;
				oldY = y;
			} else {
				control = false;
			}
			
		}

		@Override public void mouseReleased(MouseEvent e) {}
		
	}
	private class KL implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				if(universe.inRunning())
					universe.pause();
				else
					universe.start();
			} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				universe.stop();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
		
	}
	
}
