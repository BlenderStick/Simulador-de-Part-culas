package com.collisionsimulator.simulator;


import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;


public class Geometria implements StructureSystem {
	
	private double[] x;
	private double[] y;
	private Rectangle2D selectShape;
	private Path2D shape;
	private double axis;
	private boolean close;
	private double centerx;
	private double centery;
	private double area;
	private double radius;
	
	private boolean autoUpdateCenter = false;
	private boolean autoUpdateShape = true;

	public Geometria() {this(null);}
	public Geometria(Geometria geo){
		if(geo!=null){
			x = new double[geo.pointsX().length];
			y = new double[geo.pointsY().length];
			System.arraycopy(geo.pointsX(), 0, x, 0, geo.pointsX().length);
			System.arraycopy(geo.pointsY(), 0, y, 0, geo.pointsY().length);
			shape = new Path2D.Double(geo.shape());
			selectShape = geo.selectShape().getBounds2D();
			area = geo.area();
			centerx = geo.centerX();
			centery = geo.centerY();
			radius = geo.radius();
			close = geo.isCloseGeometry();
		} else {
			x = new double[0];
			y = new double[0];
			selectShape = new Rectangle2D.Double();
		}
	}

	@Override
	public void addPoint(double x, double y) {
		synchronized(selectShape){
			double[] oldX = this.x;
			double[] oldY = this.y;
			this.x = new double[this.x.length + 1];
			this.y = new double[this.y.length + 1];
			System.arraycopy(oldX, 0, this.x, 0, oldX.length);
			System.arraycopy(oldY, 0, this.y, 0, oldX.length);
			this.x[this.x.length - 1] = x;
			this.y[this.y.length - 1] = y;
			if(autoUpdateCenter)
				updateCenter();
			if(autoUpdateShape)
				updateShape();
		}
	}

	@Override
	public void addPoint(double x, double y, int index) {
		synchronized(selectShape){
			if(index >= this.x.length){
				addPoint(x, y);
				return;
			}
			if(index < this.x.length){
				double[] oldX = this.x;//Cria uma cópia
				double[] oldY = this.y;
				this.x = new double[this.x.length + 1];//Instancia uma lista maior
				this.y = new double[this.y.length + 1];
				this.x[index] = x;//Insere as coordenadas
				this.y[index] = y;
				System.arraycopy(oldX, 0, this.x, 0, index);//Cola a primeira parde da lista
				System.arraycopy(oldY, 0, this.y, 0, index);
				System.arraycopy(oldX, index, this.x, index+1, this.x.length-index-1);//Cola a segunda parde da lista
				System.arraycopy(oldY, index, this.y, index+1, this.x.length-index-1);
				
				if(autoUpdateCenter)
					updateCenter();
				if(autoUpdateShape)
					updateShape();
			}
		}
	}

	@Override
	public void removePoint(int index) {
		synchronized(selectShape){
			if(index < this.x.length){
				double[] oldX = this.x;//Cria uma cópia
				double[] oldY = this.y;
				this.x = new double[this.x.length - 1];//Instancia uma lista menor
				this.y = new double[this.y.length - 1];
//				int count = 0;
//				for(int i = 0; i < oldX.length; i++){
//					if(i != index){
//						x[count] = oldX[i];
//						y[count] = oldY[i];
//						count ++;
//					}
//				}
				System.arraycopy(oldX, 0, this.x, 0, index);//Cola a primeira parde da lista
				System.arraycopy(oldY, 0, this.y, 0, index);
				System.arraycopy(oldX, index+1, this.x, index, oldX.length-index-1);//Cola a segunda parde da lista
				System.arraycopy(oldY, index+1, this.y, index, oldY.length-index-1);
				
				if(autoUpdateCenter)
					updateCenter();
				if(autoUpdateShape)
					updateShape();
			}
		}
	}

	@Override
	public double getPointX(int index) {
		if(index < x.length)
			return x[index];
		return Double.NaN;
	}

	@Override
	public double getPointY(int index) {
		if(index < y.length)
			return y[index];
		return Double.NaN;
	}

	@Override
	public void movePoint(double newX, double newY, int index) {
		synchronized(selectShape){
			if(index < x.length){
				x[index] = newX;
				y[index] = newY;
			}

			if(autoUpdateCenter)
				updateCenter();
			if(autoUpdateShape)
				updateShape();
		}
	}

	@Override
	public double[] pointsX() {
		return x;
	}

	@Override
	public double[] pointsY() {
		return y;
	}
	
	@Override
	public int getPointIndex(double x, double y) {
		for(int i = 0; i < this.x.length; i++)
			if(this.x[i] == x && this.y[i] == y)
				return i;
		return -1;
	}

	@Override public void openGeometry() {
		close = false;
		
		if(autoUpdateCenter)
			updateCenter();
		if(autoUpdateShape)
			updateShape();
	}

	@Override public void closeGeometry() {
		close = true;
		if(shape!=null)
			shape.closePath();
	}
	
	@Override
	public boolean isCollided(UnitSystem unit) {
		synchronized(selectShape){
			return isCollided(unit.getX(), unit.getY(), unit.radius()) > -1? true: false;
		}
	}

	@Override
	public int isCollided(double xCirculo, double yCirculo, double radius) {
		synchronized(selectShape){
			if (this.x.length > 1 && Math.hypot(centerx - xCirculo, centery - yCirculo) <= radius + radius) {
				double oldX = this.x[0];
				double oldY = this.y[0];
				
				//Testa cada reta
				for (int i = 1; i < this.x.length; i++) {
					double x = this.x[i];
					double y = this.y[i];
					
					//Detecta colisão com cada reta
					boolean result = collision(oldX, oldY, x, y, xCirculo, yCirculo, radius);

					if (result)
						return i;

					oldX = x;
					oldY = y;
				}
				//Detecta colisão com a última reta se a geometria for fechada
				if (close && collision(oldX, oldY, x[0], y[0], xCirculo, yCirculo, radius))
					return this.x.length;
				
				//Testa colisão com cada ponto da geometria
				for(int i = 0; i < x.length; i++){
					if(Math.hypot(xCirculo-x[i], yCirculo-y[i]) <= radius){
						double a = Math.atan2(xCirculo-x[i], yCirculo-y[i]);
						axis = a < -Math.PI / 2d || a > Math.PI? a-Math.PI/2d: a+Math.PI/2d;
						return i;
					}
				}
			}
			return -1;
		}
	}

	public double getIntersectionX(double xCirculo, double yCirculo, double radius, int index){
		synchronized(selectShape){
			double x1 = index > 0? x[index]: x[x.length-1];
			double y1 = index > 0? y[index]: x[x.length-1];
			double x2 = index > 0 && index < x.length? x[index]: x[0];
			double y2 = index > 0 && index < x.length? y[index]: x[0];
			
			double a = Math.atan2(y2 - y1, x2 - x1) + Math.PI / 2d;
			double x3 = xCirculo - radius * Math.cos(a);
			double y3 = yCirculo - radius * Math.sin(a);
			double x4 = xCirculo + radius * Math.cos(a);
			double y4 = yCirculo + radius * Math.sin(a);
			
			double det = ((x4 - x3) * (y2 - y1) - (y4 - y3) * (x2 - x1));
			double s = ((x4 - x3) * (y3 - y1) - (y4 - y3) * (x3 - x1)) / det;
			
			return x1 + (x2 - x1) * s;
		}
	}
	public double getIntersectionY(double xCirculo, double yCirculo, double radius, int index){
		synchronized(selectShape){
			double x1 = index > 0? x[index]: x[x.length-1];
			double y1 = index > 0? y[index]: x[x.length-1];
			double x2 = index > 0 && index < x.length? x[index]: x[0];
			double y2 = index > 0 && index < x.length? y[index]: x[0];
			
			double a = Math.atan2(y2 - y1, x2 - x1) + Math.PI / 2d;
			double x3 = xCirculo - radius * Math.cos(a);
			double y3 = yCirculo - radius * Math.sin(a);
			double x4 = xCirculo + radius * Math.cos(a);
			double y4 = yCirculo + radius * Math.sin(a);
			
			double det = ((x4 - x3) * (y2 - y1) - (y4 - y3) * (x2 - x1));
			double s = ((x4 - x3) * (y3 - y1) - (y4 - y3) * (x3 - x1)) / det;
			
			return y1 + (y2 - y1) * s;
		}
	}
	
	private boolean collision(double x1, double y1, double x2, double y2, double unitX, double unitY, double unitR){
		axis = Math.atan2(y2 - y1, x2 - x1);
		double a = axis + Math.PI / 2d;
		double radX = unitR * Math.cos(a);
		double radY = unitR * Math.sin(a);
//		if(lineIntersects(
//				unitX, unitY, 
//				unitX + radX, unitY + radY, 
//				x1, y1, 
//				x2, y2))
//			System.out.println(Math.toDegrees(axis));
		
		return lineIntersects(
				unitX, unitY, 
				unitX + radX, unitY + radY, 
				x1, y1, 
				x2, y2);
	}
	
	public boolean contains(double x, double y){
		if(shape!=null)
			return shape.contains(x, y);
		return false;
	}
	
	public double centerX() {return centerx;}
	public double centerY() {return centery;}
	public double area() {return area;}
	public double radius() {return radius;}
	public boolean isCloseGeometry() {return close;}
	public boolean isAutoUpdateCenter() {return autoUpdateCenter;}
	public boolean isAutoUpdateShape() {return autoUpdateShape;}
	public void setAutoUpdateCenter(boolean bUpdate) {autoUpdateCenter = bUpdate;}
	public void setAutoUpdateShape(boolean bUpdate) {autoUpdateShape = bUpdate;}
	
	/**
	 * Retorna o eixo x local(em radianos) da última colisão
	 * @return Eixo x local(em radianos)
	 */
	public double collisionAxis(){return axis;}
	
	@Override
	public Shape shape() {return shape;}

	@Override
	public Shape selectShape() {return selectShape;}
	
	public void updateCenter(){
		synchronized(selectShape){
			computeArea();
			computeCenter();
			computeRadius();
		}
	}
	public void updateShape(){
		synchronized(selectShape){
			if (x.length > 0) {
				shape = new Path2D.Double();
				shape.moveTo(x[0], y[0]);
				for (int i = 1; i < x.length; i++) {
					shape.lineTo(x[i], y[i]);
				}
				if (x.length > 3) {
					double X = 0, Y = 0, w = 0, h = 0;
					for (int i = 0; i < x.length; i++) {
						X = Math.min(x[i], X);
						Y = Math.min(y[i], Y);
						w = Math.max(x[i], X);
						h = Math.max(y[i], Y);
					}
					selectShape.setFrame(X, Y, w, h);
				} else {
					selectShape.setFrame(0, 0, 0, 0);
				}
				if(close)
					shape.closePath();
			} else {
				shape = null;
				selectShape.setFrame(0, 0, 0, 0);
			}
		}
	}
	
	private void computeArea(){
		area = 0d;
		if(x.length>1){
			double oldx = x[0];
			double oldy = y[0];
			for(int i = 1; i < x.length; i++){
				area += oldx * y[i] - x[i] * oldy;
				oldx = x[i];
				oldy = y[i];
			}
			area += oldx * y[0] - x[0] * oldy;
			area /= 2d;
		}
	}
	private void computeCenter(){
		centerx = 0;
		centery = 0;
		if (x.length > 1) {
			double oldx = x[0];
			double oldy = y[0];
			for(int i = 1; i < x.length; i++){
				centerx +=(oldx + x[i]) * (oldx * y[i] - x[i] * oldy);
				centery +=(oldy + y[i]) * (oldx * y[i] - x[i] * oldy);
				oldx = x[i];
				oldy = y[i];
			}
			centerx +=(oldx + x[0]) * (oldx * y[0] - x[0] * oldy);
			centery +=(oldy + y[0]) * (oldx * y[0] - x[0] * oldy);
			centerx /= ((x.length) * area);
			centery /= ((y.length) * area);
			
		}
	}
	private void computeRadius(){
		radius = 0;
		if(x.length>1){
			for(int i = 0; i < x.length; i++)
				radius = Math.max(Math.hypot(centerx - x[i], centery - y[i]), radius);
		} else
			radius = 0;
		
	}
	
	/**
	 * Verifica se há intersecção de retas
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 * @return
	 */
	public static boolean lineIntersects(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
		double det = ((x4 - x3) * (y2 - y1) - (y4 - y3) * (x2 - x1));
		double s = ((x4 - x3) * (y3 - y1) - (y4 - y3) * (x3 - x1)) / det;
		double t = ((x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1)) / det;
		return (s > 0 && s < 1) && (t > 0 && t < 1);
	}

}
