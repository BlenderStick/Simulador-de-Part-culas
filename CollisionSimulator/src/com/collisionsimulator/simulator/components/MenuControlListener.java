package com.collisionsimulator.simulator.components;

import com.collisionsimulator.simulator.ForceField;

public interface MenuControlListener {
	public void saveProject();
	public void saveAsProject(String fileName);
	public void openProject(String fileName);
	public void saveCache(String fileName);
	public void openCache(String fileName);

	public void gravity(double newGravity);
	public void temperature(double newTemperature);
	public void force(ForceField newForce);
	public void airflow(double airflowX, double airflowY);
}
