package com.collisionsimulator.simulator;

import java.awt.Color;

public class Corpo implements UnitSystem{
	
	private double x;
	private double y;
	public double angle;
	private double deltaX;
	private double deltaY;
	private double cacheForceX;
	private double cacheForceY;
	private double cacheForce;
	
	public Object identification;
	
//	private double force[];
//	private double angleForce[];
	private double forceX[];
	private double forceY[];
	private int forceTime[];
	private double temporalForceX[];
	private double temporalForceY[];
	private int temporalForceTime[];
	private Color temporalForceColor[];
//	private double rotat;
	
	private double mass;
	public double radius;
	public Color background;
	public Color border;
	public boolean isRadioative = false;
	public Color radioativeColor;
	
	public Corpo() {
//		force = new double[0];
//		angleForce = new double[0];
		forceX = new double[0];
		forceY = new double[0];
		forceTime = new int[0];
		
		//Instancia as forças temporárias
		temporalForceX = new double[0];
		temporalForceY = new double[0];
		temporalForceTime = new int[0];
		temporalForceColor = new Color[0];
	}

	@Override
	public synchronized void applyForce(double newForce, double newAngle, int time) {
//		double[] forceT = new double[force.length+1];
//		double[] angleT = new double[angleForce.length+1];
//		int[] timeT = new int[force.length+1];
//		for(int i = 0; i < force.length; i++){
//		forceT[i] = force[i];
//		angleT[i] = angleForce[i];
//		timeT[i] = timeForce[i];
//	}
//		int index = force.length;
//		forceT[index] = newForce;
//		angleT[index] = newAngle;
//		timeT[index] = time;
//		force = forceT;
//		angleForce = angleT;
//		timeForce = timeT;
		int length = forceTime.length;
		double x[] = new double[length+1];
		double y[] = new double[length+1];
		int timeT[] = new int[length+1];
		for(int i = 0; i < forceTime.length; i++){
			x[i] = forceX[i];
			y[i] = forceY[i];
			timeT[i] = forceTime[i];
		}

		double cos = Math.cos(Math.toRadians(newAngle));
		double sin = Math.sin(Math.toRadians(newAngle));
		x[length] = ((newForce)*cos);
		y[length] = ((newForce)*sin);
		timeT[length] = time;
		forceX = x;
		forceY = y;
		forceTime = timeT;
	}

	@Override
	public void applyForceXY(double newX, double newY, int time) {
		int length = forceTime.length;
		double x[] = new double[length+1];
		double y[] = new double[length+1];
		int timeT[] = new int[length+1];
		for(int i = 0; i < forceTime.length; i++){
			x[i] = forceX[i];
			y[i] = forceY[i];
			timeT[i] = forceTime[i];
		}

		x[length] = newX;
		y[length] = newY;
		timeT[length] = time;
		forceX = x;
		forceY = y;
		forceTime = timeT;
	}
	
	public void applyDelta(double newDeltaX, double newDeltaY){
		deltaX += newDeltaX;
		deltaY += newDeltaY;
	}
	
	public void addTemporalForceXY(double forceX, double forceY, Color cor, int time){
		int length = temporalForceTime.length;
		double x[] = new double[length+1];
		double y[] = new double[length+1];
		Color color[] = new Color[length+1];
		int timeT[] = new int[length+1];
		for(int i = 0; i < temporalForceTime.length; i++){
			x[i] = temporalForceX[i];
			y[i] = temporalForceY[i];
			color[i] = temporalForceColor[i];
			timeT[i] = temporalForceTime[i];
		}

		x[length] = forceX;
		y[length] = forceY;
		color[length] = cor;
		timeT[length] = time;
		temporalForceX = x;
		temporalForceY = y;
		temporalForceColor = color;
		temporalForceTime = timeT;
	}
	public void addTemporalForce(double force, double angle, Color cor, int time){
		int length = temporalForceTime.length;
		double x[] = new double[length+1];
		double y[] = new double[length+1];
		Color color[] = new Color[length+1];
		int timeT[] = new int[length+1];
		for(int i = 0; i < temporalForceTime.length; i++){
			x[i] = temporalForceX[i];
			y[i] = temporalForceY[i];
			color[i] = temporalForceColor[i];
			timeT[i] = temporalForceTime[i];
		}

		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		x[length] = ((force)*cos);
		y[length] = ((force)*sin);
		color[length] = cor;
		timeT[length] = time;
		temporalForceX = x;
		temporalForceY = y;
		temporalForceColor = color;
		temporalForceTime = timeT;
	}
	
	public synchronized double getResultForce(){
		double forceS = 0;
//		double X = 0, Y = 0;
		for (int i = 0; i < forceTime.length; i++) {
//			X += this.forceX[i];
//			Y += this.forceY[i];
		}
		
		return forceS;
	}

	@Override
	public void setMass(double newMass) {
		mass = newMass;
	}
	public double getMass(){return mass;}
	
	public void update(){
		double X = 0, Y = 0;
		for (int i = 0; i < forceTime.length; i++) {
			if (forceTime[i] > 0) {
				X += this.forceX[i];
				Y += this.forceY[i];
				forceTime[i] --;
			}
		}
		cacheForceX = X;
		cacheForceY = Y;
		cacheForce = Math.sqrt(X*X + Y*Y);
		
		deltaX += X/mass;
		deltaY += Y/mass;
		x +=deltaX;
		y +=deltaY;
//		x += ((forceS/mass)*cos);
//		y += ((forceS/mass)*sin);
	}
	public void updateForces(){
		double X = 0, Y = 0;
		for (int i = 0; i < forceTime.length; i++) {
			if (forceTime[i] > 0) {
				X += this.forceX[i];
				Y += this.forceY[i];
			}
		}
		cacheForceX = X;
		cacheForceY = Y;
		cacheForce = Math.sqrt(X*X + Y*Y);
	}
	
	
	public synchronized void clearForces(){
		//Elimina as forças
		int length = 0;
		for(int i = 0; i < forceTime.length; i++)
			if(forceTime[i] > 0)
				length ++;
		
		double[] newX = new double[length];
		double[] newY = new double[length];
		int[] newTime = new int[length];
		int count = 0;
		if(length>0)
			for (int i = 0; i < forceTime.length; i++)
				if(forceTime[i] > 0){
					newX[count] = forceX[i];
					newY[count] = forceY[i];
					newTime[count] = forceTime[i];
					count++;
				}
		forceX = newX;
		forceY = newY;
		forceTime = newTime;
		
		//Elimina a força temporária
		int Tlength = 0;
		for(int i = 0; i < temporalForceTime.length; i++)
			if(temporalForceTime[i] > 0)
				Tlength ++;
		
		double[] temporalX = new double[Tlength];
		double[] temporalY = new double[Tlength];
		Color[] temporalColor = new Color[Tlength];
		int[] temporalTime = new int[Tlength];
		int Tcount = 0;
		if(Tlength>0)
			for (int i = 0; i < temporalForceTime.length; i++)
				if(temporalForceTime[i] > 0){
					temporalX[Tcount] = temporalForceX[i];
					temporalY[Tcount] = temporalForceY[i];
					temporalColor[Tcount] = temporalForceColor[i];
					temporalTime[Tcount] = temporalForceTime[i] -1;
					Tcount++;
				}
		temporalForceX = temporalX;
		temporalForceY = temporalY;
		temporalForceColor = temporalColor;
		temporalForceTime = temporalTime;
	}
	public synchronized void resetForces(){
		//Reseta todas as forças
		forceX = new double[0];
		forceY = new double[0];
		forceTime = new int[0];
	}
	public synchronized void resetDelta(){
		deltaX = 0d;
		deltaY = 0d;
	}
	public synchronized void resetTemporalForces(){
		temporalForceX = new double[0];
		temporalForceY = new double[0];
		temporalForceColor = new Color[0];
		temporalForceTime = new int[0];
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public synchronized void setX(double newX){
		x = newX;
	}
	public synchronized void setY(double newY){
		y = newY;
	}
	
	public double getDeltaX(){return deltaX;}
	public double getDeltaY(){return deltaY;}
	public double getDelta(){
		return Math.sqrt(Math.pow(getDeltaX(), 2)+Math.pow(getDeltaY(), 2));
	}
	public double getDeltaAngle(){
		double atan = Math.atan2(getDeltaY(), getDeltaX());
		return atan < 0? Math.PI * 2d + atan: atan;
	}
	public double getDeltaCos(){
		double denumerator = Math.sqrt(getDeltaX()*getDeltaX()+getDeltaY()*getDeltaY());
		double numerator = getDeltaX();
		return numerator / denumerator;
	}
	public boolean inMoviment() {return deltaX != 0d || deltaY != 0;}
	
	public synchronized double getCacheForceX(){return cacheForceX;}
	public synchronized double getCacheForceY(){return cacheForceY;}
	public synchronized double getCacheForce(){return cacheForce;}

	public synchronized double[] getTemporalForceX(){return temporalForceX;}
	public synchronized double[] getTemporalForceY(){return temporalForceY;}
	public synchronized int[] getTemporalForceTime(){return temporalForceTime;}
	public synchronized Color[] getTemporalForceColor(){return temporalForceColor;}
	
	@Override
	public void applyTorc(double distance, double module) {
		
	}

	@Override
	public boolean isCollided(UnitSystem unit) {
		double rad1 = radius, rad2 = unit.radius();
		double pos1 = getX()-unit.getX();
		double pos2 = getY()-unit.getY();
		double distance = Math.sqrt((Math.pow(pos1, 2)+Math.pow(pos2, 2)));
		
//		return distance-Math.sqrt(Math.pow(rad1+rad2, 2)) <= 0d;
		return distance-(rad1+rad2) <= 0d;
	}
	
	public boolean isRealCollision(UnitSystem unit, boolean cyclic){
		double rad1 = radius, rad2 = unit.radius();
		double pos1 = getX()-unit.getX();
		double pos2 = getY()-unit.getY();
		double distance = Math.sqrt((Math.pow(pos1+getDeltaX(), 2)+Math.pow(pos2+getDeltaY(), 2)));
		double angle = Math.atan2(pos2, pos1);
		
		double deltaAngle = getDeltaAngle() > Math.PI * 2d? getDeltaAngle() - Math.PI * 2d: getDeltaAngle();
//		System.out.println(deltaAngle);
//		System.out.println("Angle: "+Math.toDegrees(angle));
//		System.out.println("Delta: "+Math.toDegrees(deltaAngle));
		
		if(deltaAngle < angle + Math.PI / 2d && deltaAngle > angle - Math.PI / 2d)
			return distance-Math.sqrt(Math.pow(rad1+rad2, 2)) <= 0;
		if(unit instanceof Corpo && cyclic == false)
			return ((Corpo) unit).isRealCollision(this, true);
		
		
		return false;
	}
	
	public boolean directionCollision(Corpo corpo){
		if(inMoviment()){
			double posx = corpo.getX() - getX();
			double posy = corpo.getY() - getY();
			double angle = Math.atan2(posy, posx);
			double deltaAngle = getDeltaAngle();
			
			if (angle < 0d)
				angle = Math.PI * 2d + angle;
			if(deltaAngle >= Math.PI * 2d)
				deltaAngle -= Math.PI * 2d;
//			System.out.println("Axis: "+Math.toDegrees(angle));
//			System.out.println("Angl: "+Math.toDegrees(deltaAngle));
			
			if(((deltaAngle < angle + Math.PI / 2d) && (deltaAngle > angle - Math.PI / 2d)))
				return true;
		}
		
//		System.out.println("Passou");
//		System.out.println(Math.toDegrees(angle));

		if(corpo.inMoviment()){
			double posx = corpo.getX() - getX();
			double posy = corpo.getY() - getY();
			double angle = Math.atan2(posy, posx) - Math.PI;
			double deltaAngle = corpo.getDeltaAngle();
			
			if (angle < 0d)
				angle = Math.PI * 2d + angle;
			if(deltaAngle >= Math.PI * 2d)
				deltaAngle -= Math.PI * 2d;
//			System.out.println("-Axis: "+Math.toDegrees(angle));
//			System.out.println("-Angl: "+Math.toDegrees(deltaAngle));
			
			if(deltaAngle < angle + Math.PI / 2d && deltaAngle > angle - Math.PI / 2d)
				return true;
		}
		
		return false;
	}
	
	public boolean uniqueDirection(Corpo corpo){
		if(inMoviment()){
			double posx = corpo.getX() - getX();
			double posy = corpo.getY() - getY();
			double angle = Math.atan2(posy, posx);
			double deltaAngle = getDeltaAngle();
			
			if (angle < 0d)
				angle = Math.PI * 2d + angle;
			if(deltaAngle >= Math.PI * 2d)
				deltaAngle -= Math.PI * 2d;
//			System.out.println("Axis: "+Math.toDegrees(angle));
//			System.out.println("Angl: "+Math.toDegrees(deltaAngle));
			
			return (deltaAngle < angle + Math.PI / 2d) && (deltaAngle > angle - Math.PI / 2d);
		}
		return false;
	}
	
	public boolean directionTest(Corpo corpo){
		if(corpo.getX() > this.getX() && this.getDeltaX() > 0){
			return true;
		}
		if(corpo.getX() < this.getX() && this.getDeltaX() < 0){
			return true;
		}
		if(corpo.getX() < this.getX() && corpo.getDeltaX() > 0){
			return true;
		}
		if(corpo.getX() > this.getX() && corpo.getDeltaX() < 0){
			return true;
		}
		return false;
	}
	
	@Override
	public double radius() {
		return radius;
	}
}
