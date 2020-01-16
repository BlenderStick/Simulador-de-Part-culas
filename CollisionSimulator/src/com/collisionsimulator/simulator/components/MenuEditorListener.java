package com.collisionsimulator.simulator.components;

import java.util.EventListener;

public interface MenuEditorListener extends EventListener{
	public void addPointSelected();
	public void movePointSelected();
	public void deletePointSelected();
	public void openCloseGeometryChange();
	public void areaSimulationSelected();
}
