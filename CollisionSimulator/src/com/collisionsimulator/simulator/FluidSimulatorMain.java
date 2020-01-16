package com.collisionsimulator.simulator;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.com.bismuthfernandes.geb.GEBLookAndFeel;

public class FluidSimulatorMain {
	
	public static double scaleForce = 5d;

	public FluidSimulatorMain() {}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new GEBLookAndFeel());
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimulatorWindow.simulatorWindow = new SimulatorWindow();
	}

}
