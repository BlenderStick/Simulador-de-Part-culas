package com.collisionsimulator.simulator.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class MenuEditor extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * 
	 * Add Point
	 * Move Point
	 * Delete Point
	 * Close/Open Geometry
	 * 
	 */
	
	private AbstractButton openCloseGeometry;
	private AbstractButton select;
	private boolean openCloseGeometryEventFire = true;
	
	public MenuEditor() {
		MenuEditorButton addPoint = new MenuEditorButton(new ImageIcon("./addPoint.png"), "Adicionar ponto");
		MenuEditorButton movePoint = new MenuEditorButton(new ImageIcon("./movePoint.png"), "Mover ponto");
		MenuEditorButton deletePoint = new MenuEditorButton(new ImageIcon("./deletePoint.png"), "Deletar ponto");
		openCloseGeometry = new MenuEditorButton(new ImageIcon("./openCloseGeometry.png"), "Fechar/Abrir geometria");
		MenuEditorButton areaSimulation = new MenuEditorButton("Definir área de simulação");
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(addPoint))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(deletePoint))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(movePoint))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(openCloseGeometry))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(areaSimulation))
					);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(addPoint)
							.addComponent(movePoint)
							.addComponent(deletePoint)
							.addComponent(openCloseGeometry)
							.addComponent(areaSimulation)));
		
		setPreferredSize(new Dimension(220, 50));
		
		setMenuEditorButtonPreferredSize(new Dimension(40, 40));
		setMenuEditorButtonMinimumSize(new Dimension(20, 20));
		setMenuEditorButtonMaximumSize(new Dimension(40, 40));
		addMenuEditorButtonActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() != openCloseGeometry)
					if (select == null) {
						select = (AbstractButton) e.getSource();
						select.setSelected(true);
					} else {
						select.setSelected(false);
						select = (AbstractButton) e.getSource();
						select.setSelected(true);
					}
				
				if(e.getSource() == addPoint)
					fireAddPointSelected();
				else if(e.getSource() == movePoint)
					fireMovePointSelected();
				else if(e.getSource() == deletePoint)
					fireDeletePointSelected();
				else if(e.getSource() == openCloseGeometry){
					if(openCloseGeometryEventFire){
						fireOpenCloseGeometrySelected();
					}
				}
				else if(e.getSource() == areaSimulation)
					fireAreaSimulationSelected();
			}
		});
		
		setBorder(javax.swing.border.LineBorder.createBlackLineBorder());
	}

	public void addMenuEditorListener(MenuEditorListener menuEditorListener){
		listenerList.add(MenuEditorListener.class, menuEditorListener);
	}
	public void removeMenuEditorListener(MenuEditorListener menuEditorListener){
		listenerList.remove(MenuEditorListener.class, menuEditorListener);
	}
	
	private void setMenuEditorButtonPreferredSize(Dimension preferredSize){
		for(Component c: getComponents())
			if (c instanceof MenuEditorButton)
				c.setPreferredSize(preferredSize);
	}
	private void setMenuEditorButtonMinimumSize(Dimension minimumSize){
		for(Component c: getComponents())
			if (c instanceof MenuEditorButton)
				c.setPreferredSize(minimumSize);
	}
	private void setMenuEditorButtonMaximumSize(Dimension maximumSize){
		for(Component c: getComponents())
			if (c instanceof MenuEditorButton)
				c.setMaximumSize(maximumSize);
	}
	private void addMenuEditorButtonActionListener(ActionListener action){
		for(Component c: getComponents())
			if (c instanceof MenuEditorButton)
				((MenuEditorButton) c).addActionListener(action);
	}
	
	public void setOpenCloseGeometryState(boolean newState){
		openCloseGeometryEventFire = false;
		openCloseGeometry.setSelected(newState);
		openCloseGeometryEventFire = true;
	}
	
	public void clearSelection(){
		select.setSelected(false);
		select = null;
	}
	
	protected void fireAddPointSelected(){
		MenuEditorListener[] mList = listenerList.getListeners(MenuEditorListener.class);
		for(MenuEditorListener m: mList)
			m.addPointSelected();
	}
	protected void fireMovePointSelected(){
		MenuEditorListener[] mList = listenerList.getListeners(MenuEditorListener.class);
		for(MenuEditorListener m: mList)
			m.movePointSelected();
	}
	protected void fireDeletePointSelected(){
		MenuEditorListener[] mList = listenerList.getListeners(MenuEditorListener.class);
		for(MenuEditorListener m: mList)
			m.deletePointSelected();
	}
	protected void fireOpenCloseGeometrySelected(){
		MenuEditorListener[] mList = listenerList.getListeners(MenuEditorListener.class);
		for(MenuEditorListener m: mList)
			m.openCloseGeometryChange();
	}
	protected void fireAreaSimulationSelected(){
		MenuEditorListener[] mList = listenerList.getListeners(MenuEditorListener.class);
		for(MenuEditorListener m: mList)
			m.areaSimulationSelected();
	}
}
