package com.collisionsimulator.simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import java.awt.geom.Line2D;

public class UniverseSimulator extends Universe{
	
	private static double scaleForce = FluidSimulatorMain.scaleForce;
	
	public List<Corpo> corpos;
	private List<Corpo> subCollision;
	private Double[] xCorpos;
	private Double[] yCorpos;
	private Double[] xForceCorpos;
	private Double[] yForceCorpos;
	private Thread thread;
	private Rectangle collisionLimits;
	private double area = 1200d;
	private boolean restart = true;
	public boolean limitCollision = true;
	public int interactions;
	public int interactionWhile = 1;
	
	public UniverseSimulator() {
		super();
		
		corpos = new LinkedList<>();
		subCollision = new ArrayList<>();
		collisionLimits = new Rectangle(-600, -400, 1200, 800);
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
	public synchronized void draw(Graphics2D G, double x, double y, int w, int h, double angle, double scale){
//		System.out.println(1/scale);
		Graphics2D g = (Graphics2D) G.create();
		
		g.translate(w/2, h/2);
		
		
		g.scale(scale, scale);
		
		g.translate(-x, -y);
//		g.translate(-corpos.get(0).getX(), -corpos.get(0).getY());
		
		g.setColor(Color.BLACK);
		g.drawLine((int) ((x+(w/scale))), 0, (int) ((x-(w/scale))) , 0);
		g.drawLine(0, (int) ((y+(h/scale))), 0, (int) ((y-(h/scale))));
		
		
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g.drawLine(0, (int) y, 0, h);
		
		g.rotate(Math.toRadians(angle));
		
		g.draw(collisionLimits);
//		g.drawLine((int) p1X, (int) p1Y, (int) p2X, (int) p2Y);
//		g.draw(new Ellipse2D.Double(-area, -area, area*2, area*2));
		for (Corpo c: corpos){
			if(c.isRadioative){
				double area = c.radius;
				double area2 = c.radius*2d;
				double size = 10d;
				double areaPoint = (area+size/scale);
				double areaLight = (area2+size*2/scale);
				//Cria a elipse da luz
				Ellipse2D body = new Ellipse2D.Double(
						(c.getX()-(areaPoint>area? areaPoint: area)), 
						(c.getY()-(areaPoint>area? areaPoint: area)), 
						areaLight, 
						areaLight);
				g.setColor(c.background);
				float arLig = (float) (area2/areaLight); //minimo 0.5f
				int transparence = 50;
				float[] dist = {0.2f, arLig>0.5? arLig: 0.5f, 1f};
				RadialGradientPaint paint = 
						new RadialGradientPaint (
								new Point2D.Float((float) c.getX(), (float) c.getY()),
										(float) areaLight,
										dist,
										new Color[]{
												new Color(
														c.background.getRed(), 
														c.background.getGreen(),
														c.background.getBlue(),
														200), 
												c.radioativeColor!=null? c.radioativeColor:new Color(
																255,
																255,
																255,
																transparence)
															,
												new Color(0, 0, 0, 0),
												});
				g.setPaint(paint);
				g.fill(body);
			}
		}

		for (Corpo c: corpos){
			double area = c.radius;
			double area2 = c.radius*2d;
			Ellipse2D body = new Ellipse2D.Double(c.getX()-area, c.getY()-area, area2, area2);
			g.setColor(c.background);
			g.fill(body);
			g.setColor(c.border);
			g.draw(body);
		}
//		G.translate(x, y);
//		if(false)
		for (Corpo c: corpos){
//			calcula o angulo da força
			double newX = ((c.getCacheForceX()/c.getMass()))*scaleForce;
			double newY = ((c.getCacheForceY()/c.getMass()))*scaleForce;
			
			g.setColor(new Color(200, 0, 0));
			g.draw(new Line2D.Double(c.getX(), c.getY(), c.getX()+newX, c.getY()+newY));//Desenha a força resultante
		}
		for (Corpo c: corpos){
			
			g.setColor(new Color(0, 200, 0));
			g.setColor(c.background);
			g.draw(new Line2D.Double(c.getX(), c.getY(), c.getX()+c.getDeltaX()*scaleForce, c.getY()+c.getDeltaY()*scaleForce));//Desenha a direção delta
			
			g.setColor(Color.BLACK);
			g.draw(new Ellipse2D.Double(c.getX()-1, c.getY()-1, 2, 2));//Desenha o centro do corpo
		}
		
		for (Corpo c: corpos){
//			calcula o angulo da força
			Color[] cor = c.getTemporalForceColor();
			for(int i = 0; i < cor.length; i++){
				try {
					double newX = ((c.getTemporalForceX()[i] / c.getMass()));
					double newY = ((c.getTemporalForceY()[i] / c.getMass()));

					g.setColor(cor[i]);
					g.draw(new Line2D.Double(c.getX(), c.getY(), c.getX() + newX, c.getY() + newY));// Desenha
																									// a
																									// força
																									// temporária
				} catch (Exception e) {}
			}
			
		}
		
		g.setColor(Color.BLACK);
		for(Geometria geo: geometrys){
			g.draw(geo.shape());
			g.drawOval((int)geo.centerX()-1, (int)geo.centerY()-1, 2, 2);
		}
		
		
//		G.setColor(Color.DARK_GRAY);
//		G.drawRect(w/2-2, h/2-2, 4, 4);
	}
	
	public void start(){
		synchronized(this){
			if (restart) {
				xCorpos = new Double[corpos.size()];
				yCorpos = new Double[corpos.size()];
				xForceCorpos = new Double[corpos.size()];
				yForceCorpos = new Double[corpos.size()];
				for (int i = 0; i < corpos.size(); i++) {
					corpos.get(i).updateForces();
					xCorpos[i] = corpos.get(i).getX();
					yCorpos[i] = corpos.get(i).getY();
					xForceCorpos[i] = corpos.get(i).getCacheForceX();
					yForceCorpos[i] = corpos.get(i).getCacheForceY();
				}
				restart = false;
			}
			
			inRunning = true;
			thread = new Thread(new Update());
			thread.start();
		}
	}
	
	public void pause(){
		synchronized(this){
			inRunning = false;
		}
	}
	
	public void stop(){
		pause();
		synchronized (this) {
			if (thread != null && thread.isAlive()) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < corpos.size(); i++) {
				corpos.get(i).resetForces();
				corpos.get(i).resetDelta();
				corpos.get(i).resetTemporalForces();
				corpos.get(i).setX(xCorpos[i]);
				corpos.get(i).setY(yCorpos[i]);
				corpos.get(i).applyForceXY(xForceCorpos[i], yForceCorpos[i], 1);
			}
			restart = true;
		}
	}
	
	public void stepChange(int newStep){
		
	}
	
	public void areaCollision(){
		for (int i = 0; i < corpos.size(); i++) {
			Corpo cI = corpos.get(i);
			if (cI.radius() + cI.getX() >= collisionLimits.getWidth() + collisionLimits.getX()) {
//				cI.applyDelta(-cI.getDeltaX() * 2, 0);
				cI.setX((collisionLimits.getX()) + cI.radius + 1d);
			}
			if (cI.getX() - cI.radius() <= collisionLimits.getX()) {
//				cI.applyDelta(-cI.getDeltaX() * 2d, 0);
				cI.setX(collisionLimits.getWidth() + collisionLimits.getX() - cI.radius - 1d);
			}
			if (cI.radius() + cI.getY() >= collisionLimits.getHeight() + collisionLimits.getY()) {
				cI.applyDelta(0, -cI.getDeltaY() * 2d);
				cI.setY((collisionLimits.getHeight() + collisionLimits.getY()) - cI.radius);
			}
			if (cI.getY() - cI.radius() <= collisionLimits.getY()) {
				cI.applyDelta(0, -cI.getDeltaY() * 2d);
				cI.setY((collisionLimits.getY()) + cI.radius);
			}
		}
	}
	public void radCollision(){
		for (int i = 0; i < corpos.size(); i++) {
			Corpo c = corpos.get(i);
			if(Math.hypot(c.getX(), c.getY()) + c.radius >= area){
				double theta = Math.atan2(c.getY(), c.getX());
				if(theta<0)
					theta = Math.PI * 2 + theta;
				double a = c.getDeltaAngle() - theta + Math.PI - theta;
				double delta = c.getDelta();
				c.applyDelta(delta * Math.cos(a) - c.getDeltaX(), -delta * Math.sin(a) - c.getDeltaY());
			}
		}
	}
	
	public synchronized void updateWorldGravity(){
		for(Corpo c: corpos)
			c.applyForceXY(0, 0.5d, 1);
	}
	
	public synchronized void updateGravity(){
		Corpo[] cache = new Corpo[corpos.size()];
		int size = corpos.size();
		for(int ini = 0; ini<size; ini++){
			Corpo cIni = corpos.get(ini);
			for(int end = 0; end<corpos.size(); end++){
				Corpo cEnd = corpos.get(end);
				double dis = Point2D.distance(cEnd.getX(), cEnd.getY(), cIni.getX(), cIni.getY());
				if(dis!=0 && !containArray(cEnd, cache)){
//					System.out.print("Ini = "+ini+", end = "+end);
					double force = 0.67*((cIni.getMass()*cEnd.getMass())/(dis*dis));
					double angle = 0;
					
					if(cIni.getX() == cEnd.getX()){
						angle = 90;
						if(cIni.getY()>cEnd.getY())
							force *= -1;
						cIni.applyForce(-force, angle, 1);
						cEnd.applyForce(force, angle, 1);
//						System.out.println("X = X");
					} else if(cIni.getY()==cEnd.getY()){
						angle = 0;
						if(cIni.getX()<cEnd.getX())
							force *= -1;
						cIni.applyForce(-force, angle, 1);
						cEnd.applyForce(force, angle, 1);
//						System.out.println("Y = Y");
					} else {
						double larg = cIni.getX()-cEnd.getX();
						double altu = cIni.getY()-cEnd.getY();
//						if(cEnd.getMass()==50)
//						System.out.println("Item: "+end+" cat1 = "+cat1 + ", cat2 = "+ cat2);
//						System.out.println(cat1);
//						System.out.println(cat2);
//						System.out.println(cos);
//						System.out.println("Ini = " + ini + ", end = " + end + ", angle = "+angle);
//						if()
						
//						double cos = (dis*dis+larg*larg-(altu*altu))/(2*dis* larg);
//						angle = Math.toDegrees(Math.acos(cos));
						
						if(larg>0){
							if(altu>0){
								double sin = (dis*dis+altu*altu-(larg*larg))/(2*dis*altu);
								angle = Math.toDegrees(Math.asin(sin));
								cIni.applyForce(-force, angle, 1);
								cEnd.applyForce(force, angle, 1);
//								force *= -1; //esse
//								angle = 180d + angle;
							} else if (altu<0){
								double sin = (dis*dis+altu*altu-(larg*larg))/(2*dis*altu);
								angle = 180 + Math.toDegrees(Math.asin(sin));
								cIni.applyForce(force, angle, 1);
								cEnd.applyForce(-force, angle, 1);
//								System.out.print("Ini = " + ini + ", end = " + end + ", angle = ");
//								System.out.println(angle);
//								angle = 180d - angle;
							}
						} else if (larg<0){
							if (altu>0){
								double cos = (dis*dis+larg*larg-(altu*altu))/(2*dis*larg);
								angle = Math.toDegrees(Math.acos(cos));
								cIni.applyForce(-force, angle, 1);
								cEnd.applyForce(force, angle, 1);
//								double cos = (dis*dis+altu*altu-(larg*larg))/(2*dis* altu);
//								angle = Math.toDegrees(Math.asin(cos));
//								cIni.applyForce(-force, angle, 1);
//								cEnd.applyForce(force, angle, 1);
//								force *= -1;
//								angle = 180d + angle;
//								angle *= -1; //esse
//								System.out.println(angle);
							} else if(altu<0){
								double cos = (dis*dis+altu*altu-(larg*larg))/(2*dis*altu);
								angle = Math.toDegrees(Math.acos(cos)) - 90;
								cIni.applyForce(force, angle, 1);
								cEnd.applyForce(-force, angle, 1);
//								if(ini==1 && end==2)
//									System.out.println(angle);
//								angle = 180d - angle; //esse
//								force *= 10;
//								force *= -1;
							}
						}
					}
//					System.out.println(", force = "+force);
//					cIni.applyForce(force, angle, 1);
//					cEnd.applyForce(-force, angle, 1);
//					System.out.println(force);
				}
				
			}
			cache[ini] = cIni;
		}
//		System.out.println("Gravity");
	}
	
	public synchronized void collisionDetect(){
		if (limitCollision)
			areaCollision();
		
		if(corpos.size()>1)
			for(int i = 1; i < corpos.size(); i++){
				Corpo c1 = corpos.get(i);
				for(int j = 0; j < i; j++){
					Corpo c2 = corpos.get(j);
					if(c1.isCollided(c2) ){
						if(c1.directionCollision(c2)){
							forceReturn(c1, c2);
//							if (c1.inMoviment() && !subCollision.contains(c1))
//								subCollision.add(c1);
//							
//							if (c2.inMoviment() && !subCollision.contains(c2))
//								subCollision.add(c2);
							
						}
					}
				}
			}
	}
	
	private void forceReturn(Corpo c1, Corpo c2){
		// Processamento dos angulos
		double a1 = Math.atan2(c2.getY() - c1.getY(), c2.getX() - c1.getX());
		double b1 = Math.atan2(c1.getDeltaY(), c1.getDeltaX());
		double y1 = b1 - a1;

		double a2 = Math.atan2(c1.getY() - c2.getY(), c1.getX() - c2.getX());
		// Declaração das massas
		double m1 = c1.getMass();
		double m2 = c2.getMass();

		double b2 = Math.atan2(c2.getDeltaY(), c2.getDeltaX());
		double y2 = b2 - a2;

		// Forças aplicadas do cI para o cJ
		double u1 = c1.getDelta();
		double u12 = u1 * Math.cos(y1); // Força tangencial
		double u11 = u1 * Math.sin(y1); // Força preservada

		// Forças aplicadas do CJ para o cI
		double u2 = c2.getDelta();
		double u22 = u2 * Math.sin(y2); // Força tangencial
		double u21 = u2 * Math.cos(y2); // Força preservada

		// Forças aplicadas
		double v12 = ((m1 - m2) * u12 - 2 * m2 * u21) / (m1 + m2);
		double v21 = ((m1 - m2) * u21 + 2 * m1 * u12) / (m1 + m2);
		
		double c1x = (u11 * -Math.sin(a1) + v12 * Math.cos(a1)) - c1.getDeltaX();
		double c1y =  (u11 * Math.cos(a1) + v12 * Math.sin(a1)) - c1.getDeltaY();
		double c2x = (u22 * -Math.sin(a2) - v21 * Math.cos(a2)) - c2.getDeltaX();
		double c2y =  (u22 * Math.cos(a2) - v21 * Math.sin(a2)) - c2.getDeltaY();
		
		c1.applyDelta(
				c1x, 
				c1y);

		c2.applyDelta(
				c2x, 
				c2y);
		
	}
	
	public synchronized void subCollisionDetect(){
		boolean start = !subCollision.isEmpty();
		if(start){
			System.out.println(interactionWhile);
			interactionWhile ++;
		}
		while (start) {
			int size = subCollision.size();
			List<Corpo> newSubCollision = new ArrayList<>();
			
//			System.out.println("Size: "+size);
			for (int i = 0; i < size; i++) {
				
				Corpo c1 = subCollision.get(i);
//				System.out.println(c1.identification + ", "+c1.getX());
				for (int j = 0; j < corpos.size(); j++) {
					Corpo c2 = corpos.get(j);
					if (c1 != c2 && (c1.inMoviment() || c2.inMoviment()))
						if (c1.isCollided(c2)) {
							if (c1.directionCollision(c2)) {
									forceReturn(c1, c2);
								
//								System.out.println("c1 Moviment " + c1.inMoviment());
//								System.out.println("c2 Moviment " + c2.inMoviment());
									
								if (!newSubCollision.contains(c1) && c1.inMoviment()){
									newSubCollision.add(c1);
								}
								if (!newSubCollision.contains(c2) && c2.inMoviment()){
									newSubCollision.add(c2);
								}
							}
						}
				}
			}
			
			subCollision = newSubCollision;
			
			start = !newSubCollision.isEmpty();
		}
	}
	
//	private void subProcess(Corpo c, int step){
//		System.out.println("SubProcess "+step);
//		for(int i = 0; i < corpos.size(); i++){
//			Corpo j = corpos.get(i);
//			if (c != j && (c.inMoviment() || j.inMoviment())) {
//				if(c.isCollided(j)){
//					if (c.directionCollision(j)) {
//						forceReturn(c, j);
//						if (j.inMoviment())
//							subProcess(j, step++);
//						if(c.inMoviment())
//							subProcess(c, step++);
//					}
//				}
//			}
//		}
//	}
	
	public synchronized void geometryCollisionDetect(){
		for(Corpo c: corpos)
			for(Geometria g: geometrys){
				if(g.isCollided(c)){
					double tmpF = c.getDeltaAngle();
					double tmpA = g.collisionAxis();
					double angle;
					
					if(tmpA < 0)
						tmpA += Math.PI * 2d;
					
					if (tmpF < Math.PI) { // Se delta < 180
						double axis = tmpA;
						double f = tmpF;
						angle = axis - (f - axis);
					} else { // Se delta > 180
						double axis = Math.PI + tmpA;
						double f = tmpF;
						angle = axis - (f - axis);
					}
					c.applyDelta(
							c.getDelta() * Math.cos(angle) - c.getDeltaX(), 
							c.getDelta() * Math.sin(angle) - c.getDeltaY());
					
				}
			}
	}
	
	private boolean containArray(Object obj, Object[] list){
		for(Object o: list)
			if(o==obj)
				return true;
		return false;
	}
	
	private void notifyUniverse(){
		synchronized(this){
			this.notify();
		}
	}
	
	private class Update implements Runnable {
		public void run() {
			try {
				for(UniverseViewer v: getViewers()){
					v.repaint();
				}
				while(inRunning){
					long time = System.currentTimeMillis();
//					updateGravity();
					updateWorldGravity();
					geometryCollisionDetect();
					collisionDetect();
//					System.out.println("Update");
//					subCollisionDetect();
					for(UniverseViewer v: getViewers()){
						v.repaint();
					}
					for(Corpo c: corpos){
						c.update();
					}
					for(Corpo c: corpos){
						c.clearForces();
					}
					long r = 10l-(System.currentTimeMillis()-time);
					if(r>0)
						Thread.sleep(r);
				}
				notifyUniverse();
			} catch (Exception e) {
				notifyUniverse();
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
