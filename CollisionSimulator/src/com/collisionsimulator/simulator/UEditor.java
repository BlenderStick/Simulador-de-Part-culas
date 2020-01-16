package com.collisionsimulator.simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.collisionsimulator.simulator.components.MenuEditor;
import com.collisionsimulator.simulator.components.MenuEditorListener;

public class UEditor extends Universe {
	
	
	private boolean spaceMode;
	private boolean geometryEditorMode;
	private MenuEditor menu;
	private MenuEditorListener mel;
	private Geometria select;
	private boolean addPoint;
	private boolean deletePoint;
	private boolean movePoint;
	
	public UEditor() {super();}

	@Override
	public void draw(Graphics2D G, double x, double y, int w, int h, double angle, double scale) {
		Graphics2D g = (Graphics2D) G.create();
		
		g.translate(w/2, h/2);
		g.scale(scale, scale);
		g.translate(-x, -y);
		
		g.setColor(Color.BLACK);
		g.drawLine((int) ((x+(w/scale))), 0, (int) ((x-(w/scale))) , 0); //Desenha a linha horizontal
		g.drawLine(0, (int) ((y+(h/scale))), 0, (int) ((y-(h/scale)))); //Desenha a linha vertical
		
		g.rotate(angle);
		
		for(Geometria geo: geometrys)
			g.draw(geo.shape());
		
		
		if(spaceMode){
			g.setColor(Color.RED);
			g.drawRect(SimulatorSystem.areaX, SimulatorSystem.areaY, SimulatorSystem.areaW, SimulatorSystem.areaH);
			drawSpacePoints(g, getMouseX(), getMouseY(), scale);
		} else if(geometryEditorMode) {
			
		}
	} 

	public boolean isSpaceMode(){return spaceMode;}
	public boolean isGeometryEditorMode(){return geometryEditorMode;}
	
	private void drawSpacePoints(Graphics2D g, double mouseX, double mouseY, double scale){
		int area = (int) Math.max((5d / scale), 5d);
		
		g.setColor(Color.RED);
		if(rectangleIntersect(SimulatorSystem.areaX, SimulatorSystem.areaY, area, mouseX, mouseY))
			g.setColor(Color.BLACK);
		g.fillRect(SimulatorSystem.areaX - area, SimulatorSystem.areaY-area, area*2, area*2);

		g.setColor(Color.RED);
		if(rectangleIntersect(SimulatorSystem.areaX, SimulatorSystem.areaX + SimulatorSystem.areaH, area, mouseX, mouseY))
			g.setColor(Color.BLACK);
		g.fillRect(SimulatorSystem.areaX - area, SimulatorSystem.areaX + SimulatorSystem.areaH-area, area*2, area*2);

		g.setColor(Color.RED);
		if(rectangleIntersect(SimulatorSystem.areaX + SimulatorSystem.areaW, SimulatorSystem.areaY, area, mouseX, mouseY))
			g.setColor(Color.BLACK);
		g.fillRect(SimulatorSystem.areaX + SimulatorSystem.areaW - area, SimulatorSystem.areaY-area, area*2, area*2);
		
		g.setColor(Color.RED);
		if(rectangleIntersect(SimulatorSystem.areaX + SimulatorSystem.areaW, SimulatorSystem.areaX + SimulatorSystem.areaH, area, mouseX, mouseY))
			g.setColor(Color.BLACK);
		g.fillRect(SimulatorSystem.areaX + SimulatorSystem.areaW - area, SimulatorSystem.areaX + SimulatorSystem.areaH-area, area*2, area*2);
	}
	
	private boolean rectangleIntersect(double x, double y, double area, double px, double py){
		return 
				x - area < px && x + area > px && 
				y - area < py && y + area > py;
	}
	
	public void setSpaceMode() {spaceMode = true; geometryEditorMode = false;}
	public void setGeometryEditorMode() {spaceMode = false; geometryEditorMode = true;}
	public void setViewMode() {spaceMode = false; geometryEditorMode = false;}
	public void setMenuEditor(MenuEditor menuEditor) {menu = menuEditor;}
	
	public void repaint(){
		for(UniverseViewer u: viewers)
			u.repaint();
	}
	
	@Override public void stepChange(int step) {}

	public void start() {}
	public void pause() {}
	public void stop() {}
	
	
	public void setMouse(double x, double y){
		super.setMouse(x, y);
		repaint();
	}
	
	public MenuEditorListener createMenuEditorListener(){
		if(mel == null)
			mel = new MEL();
		return mel;
	}
	
	private class MEL implements MenuEditorListener{

		@Override
		public void addPointSelected() {
			spaceMode = false;
			geometryEditorMode = true;
			addPoint = true;
			movePoint = false;
			deletePoint = false;
			repaint();
		}

		@Override
		public void movePointSelected() {
			spaceMode = false;
			geometryEditorMode = true;
			addPoint = false;
			movePoint = true;
			deletePoint = false;
			repaint();
		}

		@Override
		public void deletePointSelected() {
			spaceMode = false;
			geometryEditorMode = true;
			addPoint = false;
			movePoint = false;
			deletePoint = true;
			repaint();
		}

		@Override
		public void openCloseGeometryChange() {
			if(select != null)
				if(select.isCloseGeometry()) {
					select.openGeometry();
					menu.setOpenCloseGeometryState(true);
				} else {
					select.closeGeometry();
					menu.setOpenCloseGeometryState(false);
				}
			repaint();
		}

		@Override
		public void areaSimulationSelected() {
			if(spaceMode){
				spaceMode = false;
				geometryEditorMode = true;
			} else {
				spaceMode = true;
				geometryEditorMode = false;
			}
			repaint();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
	
	
}
