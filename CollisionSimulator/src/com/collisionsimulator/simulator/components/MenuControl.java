package com.collisionsimulator.simulator.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.collisionsimulator.simulator.ForceField;
import com.collisionsimulator.simulator.SimulatorSystem;


public class MenuControl extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton save;
	private JButton saveAs;
	private JButton load;
	private JButton saveCache;
	private JButton loadCache;
	private JSpinner gravity;
	private JSpinner temperature;
	private JButton force;
	private JButton airflow;
	
	private List<MenuControlListener> listeners;

	public MenuControl() {
		super();
		listeners = new ArrayList<>();
		
		save = new JButton("Save");
		saveAs = new JButton("Save As");
		load = new JButton("Load");
		saveCache = new JButton("Save Cache");
		loadCache = new JButton("Load Cache");
		gravity = new JSpinner(new SpinnerNumberModel(0d, 0d, Integer.MAX_VALUE, 0.1d));
		temperature = new JSpinner(new SpinnerNumberModel(0d, Integer.MIN_VALUE, Integer.MAX_VALUE, 0.1d));
		force = new JButton("Adicionar Força");
		airflow = new JButton("Definir Corrente de Vento");
		
		gravity.setToolTipText("Gravidade");
		temperature.setToolTipText("Temperatura");
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(true, layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(save, 10, 80, 200)
							.addComponent(gravity, 20, 80, 200))
					.addGroup(true, layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(saveAs, 10, 80, 200)
							.addComponent(temperature, 20, 80, 200))
					.addGroup(true, layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(load, 10, 80, 200))
					.addGroup(true, layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(saveCache, 30, 120, 200)
							.addComponent(force, 30, 120, 200))
					.addGroup(true, layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(loadCache, 30, 120, 200)
							.addComponent(airflow, 30, 120, 200))
					);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(save, 10, 20, 20)
							.addComponent(saveAs, 10, 20, 20)
							.addComponent(load, 10, 20, 20)
							.addComponent(saveCache, 10, 20, 20)
							.addComponent(loadCache, 10, 20, 20)
						)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(gravity, 10, 20, 20)  
							.addComponent(temperature, 10, 20, 20)
							.addComponent(force, 10, 20, 20)    
							.addComponent(airflow, 10, 20, 20)
						)
				);
		
		setPreferredSize(new Dimension(300, 50));
		setMinimumSize(new Dimension(0, 40));
		
		ActionListener al = new AL();
		ChangeListener cl = new CL();
//		gravity.getModel().add
		for(Component c: getComponents()){
			c.setFocusable(false);
			if(c instanceof JButton)
				((JButton) c).addActionListener(al);
			if(c instanceof JSpinner)
				((JSpinner) c).getModel().addChangeListener(cl);
		}

		gravity.setFocusable(true);
		temperature.setFocusable(true);
	}

	public void addMenuControlListener(MenuControlListener newMCL) {listeners.add(newMCL);}
	public void removeMenuControlListener(MenuControlListener newMCL) {listeners.remove(newMCL);}

	private void fireSave(){
		for(MenuControlListener mcl: listeners)
			mcl.saveProject();
	}
	private void fireSave(String fileName){
		for(MenuControlListener mcl: listeners)
			mcl.saveAsProject(fileName);
	}
	private void fireLoad(String fileName){
		for(MenuControlListener mcl: listeners)
			mcl.openProject(fileName);
	}
	private void fireSaveCache(String fileName){
		for(MenuControlListener mcl: listeners)
			mcl.saveCache(fileName);
	}
	private void fireOpenCache(String fileName){
		for(MenuControlListener mcl: listeners)
			mcl.openCache(fileName);
	}
	private void fireGravity(double gravity){
		for(MenuControlListener mcl: listeners)
			mcl.gravity(gravity);
	}
	private void fireTemperature(double temperature){
		for(MenuControlListener mcl: listeners)
			mcl.temperature(temperature);
	}
	private void fireForce(ForceField force){
		for(MenuControlListener mcl: listeners)
			mcl.force(force);
	}
	private void fireGravity(double airflowX, double airflowY){
		for(MenuControlListener mcl: listeners)
			mcl.airflow(airflowX, airflowY);
	}
	
	private class AL implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == save){
				if(SimulatorSystem.fileSave != null)
					fireSave();
				else {
					JFileChooser file = new JFileChooser();
					file.setFileFilter(new FileNameExtensionFilter("Simulator || *.simu", "simu"));
					file.setDialogTitle("Save Project");
					file.setMultiSelectionEnabled(false);
					
					if(file.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
						String fileName = file.getSelectedFile().getAbsolutePath();
						if(fileName != null)
							fireSave(fileName);
					}
				}
			} else if(e.getSource() == load){
				JFileChooser file = new JFileChooser();
				file.setFileFilter(new FileNameExtensionFilter("Simulator || *.simu", "simu"));
				file.setDialogTitle("Open Project");
				file.setMultiSelectionEnabled(false);
				if(file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					String fileName = file.getSelectedFile().getAbsolutePath();
					if(fileName != null)
						fireLoad(fileName);
				}
				
			} else if(e.getSource() == saveAs){
				JFileChooser file = new JFileChooser();
				file.setFileFilter(new FileNameExtensionFilter("Simulator || *.simu", "simu"));
				file.setDialogTitle("Save Project");
				file.setMultiSelectionEnabled(false);
				if(file.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
					String fileName = file.getSelectedFile().getAbsolutePath();
					if(fileName != null)
						fireSave(fileName);
				}
			} else if(e.getSource() == saveCache){
				JFileChooser file = new JFileChooser();
				file.setFileFilter(new FileNameExtensionFilter("Simulator Cache || *.simucache", "simucache"));
				file.setDialogTitle("Save Cache");
				file.setMultiSelectionEnabled(false);
				if(file.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
					String fileName = file.getSelectedFile().getAbsolutePath();
					if(fileName != null)
						fireSaveCache(fileName);
				}
			} else if(e.getSource() == loadCache){
				JFileChooser file = new JFileChooser();
				file.setFileFilter(new FileNameExtensionFilter("Simulator Cache || *.simucache", "simucache"));
				file.setDialogTitle("Open Cache");
				file.setMultiSelectionEnabled(false);
				if(file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					String fileName = file.getSelectedFile().getAbsolutePath();
					if(fileName != null)
						fireOpenCache(fileName);
				}
			} else if(e.getSource() == force)
				fireForce(null);
			 else if(e.getSource() == force)
				 fireGravity(0d, 0.5d);
		}
		
	}
	
	private class CL implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == gravity)
				fireGravity((double) gravity.getValue());
			else if (e.getSource() == temperature)
				fireTemperature((double) temperature.getValue());
		}
		
	}
	
}
