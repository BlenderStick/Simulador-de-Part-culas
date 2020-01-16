package com.collisionsimulator.simulator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.lang.reflect.Array;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.collisionsimulator.simulator.components.MenuControl;
import com.collisionsimulator.simulator.components.MenuControlListener;
import com.collisionsimulator.simulator.components.MenuEditor;
import com.collisionsimulator.simulator.components.SimulationControl;
import com.collisionsimulator.simulator.components.SimulationListener;

public class SimulatorWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static SimulatorWindow simulatorWindow;
	
	private JPanel bottom = new JPanel();
	private JTabbedPane scenne = new JTabbedPane();
	private FocusPolicy focus;
	private Universe select;
	public UniverseSimulator simulationUniverse;
	public UEditor editorU;

	public SimulatorWindow() {
		super("Simulador de Colisão");

		SimulatorSystem.areaX = -600;
		SimulatorSystem.areaY = -600;
		SimulatorSystem.areaW = 1200;
		SimulatorSystem.areaH = 1200;
		
		simulationUniverse = new UniverseSimulator();
		editorU = new UEditor();
		
		SimulatorSystem.universe = simulationUniverse;
		
		select = simulationUniverse;
		focus = new FocusPolicy();
		
		
//		int[][] ints = new int[][]{new int[]{0, 10}, new int[]{1,20}};
//		int[] in = new int[]{2, 5};
//		int[][] res;
//		File file = new File("C:\\users\\henrique\\desktop\\JavaEscrita.txt");
//		ObjectInputStream objin;
//		ObjectOutputStream objout;
//		try {
//			objout = new ObjectOutputStream(new FileOutputStream(file));
//			objout.writeObject(ints);
//			objout.writeObject(in);
//			objout.flush();
//			objout.close();
//			
//			objin = new ObjectInputStream(new FileInputStream(file));
//			Object obj = objin.readObject();
//			res = new int[Array.getLength(obj)][];
//			for(int i = 0; i < res.length; i++){
////				res[i] = Array.getInt(obj, i);
//				Object sobj = Array.get(obj, i);
//				res[i] = new int[Array.getLength(sobj)];
//				for(int j = 0; j < res[i].length; j++){
//					res[i][j] = Array.getInt(sobj, j);
//					System.out.println(res[i][j]);
//				}
//			}
//			obj = objin.readObject();
//			int l = Array.getLength(obj);
//			for(int i = 0; i < l; i++){
//				System.out.println(Array.getInt(obj, i));
//			}
//		} catch (IOException | ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		
//		Corpo[] corpos = new Corpo[6];	
//		int count = 0;
//	
//		for(int i = 0; i < corpos.length; i++){
//			Corpo c = new Corpo();
//			c.setMass(10d);
//			c.radius = 10d;
//			c.setX(20*count);
//			c.setY(-20);
//			corpos[i] = c;
//			
//			count++;
//		}
//		corpos[5].setX(110);
//		corpos[5].applyForceXY(-50, 0, 1);
		
//		for(int i = corpos.length - 1; i >= 0; i--){
//			Corpo c = new Corpo();
//			c.setMass(10d);
//			c.radius = 10d;
//			c.setX(20d*count);
//			c.setY(-20d);
//			corpos[i] = c;
//			
//			count++;
//		}
//		corpos[5].setX(-10);
//		corpos[5].applyForceXY(50, 0, 1);

//		corpos[0].background = new Color(200, 50, 50);//Vermelho 1
//		corpos[1].background = new Color(200, 100, 50);//Laranja 2
//		corpos[2].background = new Color(100, 200, 50);//Verde   3
//		corpos[3].background = new Color(50, 200, 100);//Limão   4
//		corpos[4].background = new Color(50, 100, 200);//Ciano   5
//		corpos[5].background = new Color(50, 50, 200);//Azul     6
//		corpos[0].identification = "Vermelho";//Vermelho 1
//		corpos[1].identification = "Laranja";//Laranja 2
//		corpos[2].identification = "Verde";//Verde   3
//		corpos[3].identification = "Limão";//Limão   4
//		corpos[4].identification = "Ciano";//Ciano   5
//		corpos[5].identification = "Azul";//Azul     6
//		Corpo c1 = new Corpo();
//		Corpo c2 = new Corpo();
//		
//		c1.setX(20);
//		c2.setX(10);
//		c2.applyDelta(5, 0);
//		System.out.println(c2.directionCollision(c1));
		
//		for(Corpo c: corpos)
//			universe.corpos.add(c);
		
		
		bottom.setPreferredSize(new Dimension(600, 50));
		
		UniverseViewer universeViewer = new UniverseViewer();
		universeViewer.setLocation(0, 0);
		universeViewer.setSize(600, 400);
		universeViewer.setUniverse(editorU);
		universeViewer.setFocusable(true);
		
		UniverseViewer universeSimu = new UniverseViewer();
		universeSimu.setLocation(0, 0);
		universeSimu.setSize(600, 400);
		universeSimu.setUniverse(simulationUniverse);
		universeSimu.setFocusable(true);
		
		
		focus.setComponent(universeViewer);
		
		scenne.add("Universo", universeViewer);
		scenne.add("Simulação", universeSimu);
		scenne.setFocusable(false);
		bottom.setFocusable(false);
		
		scenne.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if(scenne.getTabRunCount() == 0)
					select = editorU;
				else
					select = simulationUniverse;
			}
		});
		
		bottom.setLayout(new BorderLayout());
		
		SimulationControl simulationControl = new SimulationControl();
		simulationControl.addSimulationListener(new SL());
		
		bottom.add(simulationControl, BorderLayout.LINE_START);
		
		MenuControl menuControl = new MenuControl();
		menuControl.addMenuControlListener(new MCL());
		bottom.add(menuControl, BorderLayout.CENTER);
		
		MenuEditor menuEditor = new MenuEditor();
		menuEditor.addMenuEditorListener(editorU.createMenuEditorListener());
		editorU.setMenuEditor(menuEditor);
		bottom.add(menuEditor, BorderLayout.LINE_END);
		
		rectangleGenerator(simulationUniverse, 500, -600, -400, 1200, 800);
		
		getContentPane().add(scenne, BorderLayout.CENTER);
		getContentPane().add(bottom, BorderLayout.PAGE_END);
		
		setFocusTraversalPolicy(focus);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);
		setVisible(true);
		
		universeViewer.requestFocus();
	}

	public static void circleGenerator(UniverseSimulator universe, int number, int radius){
		Random r = new Random();
		for(int i = 0; i <= number; i++){
			Corpo c = new Corpo();
			c.radius = 10d;
			c.setMass(10d);
			c.setX((double) r.nextInt(radius) * (r.nextDouble() * (r.nextBoolean()? 1d: -1d)));
			c.setY((double) r.nextInt(radius) * (r.nextDouble() * (r.nextBoolean()? 1d: -1d)));
			c.applyForce(r.nextInt(10), r.nextInt(360), 1);
			universe.corpos.add(c);
		}
	}
	public static void rectangleGenerator(UniverseSimulator universe, int number, int x, int y, int width, int height){
		Random r = new Random();
		for(int i = 0; i <= number; i++){
			Corpo c = new Corpo();
			c.radius = 10d;
			c.setMass(10d);
			c.setX(r.nextInt(width)+x);
			c.setY(r.nextInt(height)+y);
			c.applyForce(r.nextInt(100), 180, 1);
			universe.corpos.add(c);
		}
	}
	
	public static void generateSimulation(
			Universe universe, 
			double mass, 
			double radius, 
			double distance, 
			int x, int y, int width, int height){
		
	}
	
	private class SL implements SimulationListener{

		@Override
		public void start() {
			if (select!=null)
				select.start();
		}

		@Override
		public void pause() {
			if (select!=null)
				select.pause();
		}

		@Override
		public void stop() {
			if (select!=null)
				select.stop();
		}

		@Override
		public void render() {
			
		}
		
	}
	
	private class MCL implements MenuControlListener{

		@Override
		public void saveProject() {
			SimulatorSystem.saveFile(simulationUniverse.geometrys.toArray(new Geometria[simulationUniverse.geometrys.size()]), new ForceField[0]);
		}

		@Override
		public void saveAsProject(String fileName) {
			SimulatorSystem.saveFileAs(simulationUniverse.geometrys.toArray(new Geometria[simulationUniverse.geometrys.size()]), new ForceField[0], fileName);
		}

		@Override
		public void openProject(String fileName) {
			SimulatorSystem.openFile(fileName);
		}

		@Override
		public void saveCache(String fileName) {
			
		}

		@Override
		public void openCache(String fileName) {
			
		}

		@Override
		public void gravity(double newGravity) {
			
		}

		@Override
		public void temperature(double newTemperature) {
			
		}

		@Override
		public void force(ForceField newForce) {
			
		}

		@Override
		public void airflow(double airflowX, double airflowY) {
			
		}
		
	}
	
	private class FocusPolicy extends FocusTraversalPolicy{

		private Component principal;
		
		public void setComponent(Component newPrincipal){principal = newPrincipal;}
		
		@Override
		public Component getComponentAfter(Container aContainer, Component aComponent) {
			for(int i = 0; i < aContainer.getComponentCount(); i++)
				if(aContainer.getComponent(i) == aComponent)
					if(i < aContainer.getComponentCount() - 1)
						return aContainer.getComponent(i + 1);
					else
						return aContainer.getComponent(0);
			
			return principal;
		}

		@Override
		public Component getComponentBefore(Container aContainer, Component aComponent) {
			for(int i = aContainer.getComponentCount() - 1; i > 0; i--)
				if(aContainer.getComponent(i) == aComponent)
					if(i > 1)
						return aContainer.getComponent(i - 1);
					else
						return aContainer.getComponent(aContainer.getComponentCount() - 1);
			return principal;
		}

		@Override
		public Component getDefaultComponent(Container aContainer) {
			return principal;
		}

		@Override
		public Component getFirstComponent(Container aContainer) {
			return principal;
		}

		@Override
		public Component getLastComponent(Container aContainer) {
			return principal;
		}
		
	}
	
	
}








