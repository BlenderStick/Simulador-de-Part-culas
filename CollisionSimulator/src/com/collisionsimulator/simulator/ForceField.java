package com.collisionsimulator.simulator;

public interface ForceField {
	public double radius();
	public void applyForce(UnitSystem particle);
	public int startInteraction();
	public int endInteraction();
	public void nextInteraction();
	public void endSimulation();
}
