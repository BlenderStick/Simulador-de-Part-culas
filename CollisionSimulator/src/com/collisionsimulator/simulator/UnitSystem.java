package com.collisionsimulator.simulator;

public interface UnitSystem {
	public void applyForceXY(double x, double y, int time);
	public void applyForce(double angle, double module, int time);
	public void applyTorc(double distance, double module);
	public double getMass();
	public void setMass(double newMass);
	public void update();
	public double radius();
	public double getX();
	public double getY();
	
	public boolean isCollided(UnitSystem unit);
}
