package com.collisionsimulator.simulator.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MenuEditorButton extends AbstractButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private long oldaction = 0L;
	
	public MenuEditorButton() {this(null, null);}
	public MenuEditorButton(String toolTipText){this(null, toolTipText);}
	public MenuEditorButton(Icon icon) {this(icon, null);}
	public MenuEditorButton(Icon icon, String toolTipText){
		super();
		this.setModel(new DefaultButtonModel());
		getModel().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				repaint();
			}
		});
		
		if(icon != null)
			setIcon(icon);
		if(toolTipText != null)
			this.setToolTipText(toolTipText);
		
		this.setFocusable(false);
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {getModel().setPressed(false); if(contains(e.getPoint())) getModel().setRollover(true);}
			public void mousePressed(MouseEvent e) {getModel().setPressed(true);getModel().setRollover(false);}
			public void mouseExited(MouseEvent e) {getModel().setRollover(false);}
			public void mouseEntered(MouseEvent e) {getModel().setRollover(true);}
			public void mouseClicked(MouseEvent e) {processActionListener();}
		});
		setBorder(javax.swing.border.LineBorder.createBlackLineBorder());
	}

	public void addActionListener(ActionListener actionListener){
		listenerList.add(ActionListener.class, actionListener);
	}
	public void removeActionListener(ActionListener actionListener){
		listenerList.remove(ActionListener.class, actionListener);
	}
	
	protected void processActionListener(){
		ActionEvent event = new ActionEvent(
				this, 
				ActionEvent.ACTION_FIRST, 
				"action", 
				System.currentTimeMillis() - oldaction, 
				ActionEvent.RESERVED_ID_MAX);
		ActionListener[] actions = listenerList.getListeners(ActionListener.class);
		for(ActionListener a: actions)
			a.actionPerformed(event);
	}
	
	public void paintComponent(Graphics g){
		Color old = g.getColor();
		
		if (getModel().isEnabled())
			if (getModel().isRollover())
				if (getModel().isSelected())
					g.setColor(new Color(90, 90, 90));
				else
					g.setColor(new Color(150, 150, 150));
			else if (getModel().isPressed() && !getModel().isSelected())
				g.setColor(new Color(50, 50, 50));
			else if (getModel().isSelected())
				g.setColor(new Color(70, 70, 70));
			else
				g.setColor(new Color(100, 100, 100));
		else
			g.setColor(new Color(200, 200, 200));
		
		g.fillRect(0, 0, getWidth()-1, getHeight()-1);
		if(getIcon() != null)
			getIcon().paintIcon(this, g, 0, 0);
		g.setColor(old);
	}
	
	
}
