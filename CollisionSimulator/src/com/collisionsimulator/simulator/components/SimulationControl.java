package com.collisionsimulator.simulator.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class SimulationControl extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<SimulationListener> listeners;
	private boolean play;
	private JButton playStop;
	private JButton cancel;
	private JButton render;

	public SimulationControl() {
		super();
		listeners = new ArrayList<>();
		playStop = new JButton(new ImageIcon("./playPause.png"));
		cancel = new JButton(new ImageIcon("./Cancel.png"));
		render = new JButton(new ImageIcon("./Render.png"));
		AL al = new AL();
		playStop.addActionListener(al);
		cancel.addActionListener(al);
		render.addActionListener(al);
		setPreferredSize(new Dimension(100, 45));
		
		playStop.setLocation(27, 2);
		playStop.setSize(40, 40);
		
		render.setLocation(6, 22);
		render.setSize(20, 20);
		
		cancel.setLocation(68, 22);
		cancel.setSize(20, 20);

		
		add(playStop);
		add(render);
		add(cancel);
		
		for(Component c: getComponents())
			c.setFocusable(false);
	}
	
	public void paintComponent(Graphics g){
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int[] x = new int[]{0, 6, 86, 94};
		int[] y = new int[]{43, 0, 0, 43};
		g.setColor(Color.DARK_GRAY);
		g.fillPolygon(x, y, 4);
		((Graphics2D) g).setStroke(new BasicStroke(2f));
		g.setColor(Color.BLACK);
		g.drawPolygon(
				x, 
				y, 
				4);
	}
	
	public Insets getInsets(){
		return new Insets(2, 2, 0, 2);
	}
	
	public void addSimulationListener(SimulationListener listener){
		listeners.add(listener);
	}
	public void removeSimulationListener(SimulationListener listener){
		listeners.remove(listener);
	}

	private void fireStart(){
		for(SimulationListener c: listeners)
			c.start();
		play = true;
	}
	private void fireStop(){
		for(SimulationListener c: listeners)
			c.pause();
		play = false;
	}
	private void fireCancel(){
		for(SimulationListener c: listeners)
			c.stop();
	}
	private void fireRender(){
		for(SimulationListener c: listeners)
			c.render();
	}
	
	private class AL implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == playStop)
				if(play)
					fireStop();
				else
					fireStart();
				
			 else if(e.getSource() == cancel)
				fireCancel();
			 else if(e.getSource() == render)
				fireRender();
		}
		
	}
}














