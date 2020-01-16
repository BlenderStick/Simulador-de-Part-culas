package com.collisionsimulator.simulator;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class Universe {
	
	private double mousex, mousey;
	private int step;
	private int interactions;
	protected boolean inRunning;
	public List<Geometria> geometrys;
	protected List<UniverseViewer> viewers;
	
	public Universe() {
		viewers = new ArrayList<>();
		geometrys = new ArrayList<>();
	}

	/**
	 * Desenha o Universo baseado nas informações x, y, w, h, scale, angle definidos nos parametros
	 * @param g - Graphics2D onde será desenhado
	 * @param x - posição x do centro da visão
	 * @param y - posição y do centro da visão
	 * @param w - largura da visão
	 * @param h - altura da visão
	 * @param angle - angulo da visão
	 * @param scale - escala da visão (zoom)
	 */
	public abstract void draw(Graphics2D G, double x, double y, int w, int h, double angle, double scale);
	
	public void addViewer(UniverseViewer newUniverseViewer){
		viewers.add(newUniverseViewer);
	}
	public void removeViewer(UniverseViewer oldUniverseViewer){
		viewers.remove(oldUniverseViewer);
	}
	public UniverseViewer[] getViewers(){
		return viewers.toArray(new UniverseViewer[viewers.size()]);
	}
	
	public int getInteractions(){return interactions;}
	public void setInteractions(int newInteraction){
		if(inRunning){
			pause();
			interactions = newInteraction;
			start();
		} else
			interactions = newInteraction;
	}
	public int getStep(){
		return step;
	}
	public void setStep(int newStep){
		stepChange(newStep);
		step = newStep;
	}
	public abstract void stepChange(int step);
	
	public void setMouse(double x, double y){
		mousex = x;
		mousey = y;
	}
	public double getMouseX() {return mousex;}
	public double getMouseY() {return mousey;}
	
	public abstract void start();
	public abstract void pause();
	public abstract void stop();

	public abstract void mouseEntered(MouseEvent e);
	public abstract void mouseExited(MouseEvent e);
	public abstract void mousePressed(MouseEvent e);
	public abstract void mouseReleased(MouseEvent e);
	public abstract void mouseClicked(MouseEvent e);
	public abstract void mouseDragged(MouseEvent e);
	public abstract void mouseMoved(MouseEvent e);

	public boolean inRunning(){return inRunning;}
}
