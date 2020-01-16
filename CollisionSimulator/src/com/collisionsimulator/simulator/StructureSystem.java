package com.collisionsimulator.simulator;

import java.awt.Shape;

/**
 * Estrutura de Geometria
 * @author Erik Fernandes
 *
 */
public interface StructureSystem {
	
	/**
	 * Adiciona um ponto ao final da lista
	 * @param x
	 * @param y
	 */
	public void addPoint(double x, double y);
	/**
	 * Adiciona um ponto em um indice expecífico da lista
	 * @param x
	 * @param y
	 * @param index
	 */
	public void addPoint(double x, double y, int index);
	/**
	 * Remove um ponto em um indice expecífico na lista
	 * @param index
	 */
	public void removePoint(int index);
	/**
	 * Obtém a posição em x do ponto da lista
	 * @param index
	 * @return
	 */
	public double getPointX(int index);
	/**
	 * Obtém a posição em y do ponto da lista
	 * @param index
	 * @return
	 */
	public double getPointY(int index);
	/**
	 * Move o ponto expecificado para novas coordenadas(x, y)
	 * @param newX - Novas coordenadas em X
	 * @param newY - Novas coordenadas em Y
	 * @param index - Indice do ponto
	 */
	public void movePoint(double newX, double newY, int index);
	/**
	 * Retorna o indice do ponto(x, y) na lista, se o ponto não existir, o retorno será -1
	 * @param x
	 * @param y
	 * @return
	 */
	public int getPointIndex(double x, double y);
	
	public double[] pointsX();
	public double[] pointsY();
	
	/**
	 * Deixa a geometria visivelmente aberta
	 */
	public void openGeometry();
	/**
	 * Deixa a geometria visivelmente fechada
	 */
	public void closeGeometry();

	/**
	 * Verifica a ocorrência de colisão com um UnitSystem
	 * @param unit
	 * @return
	 */
	public boolean isCollided(UnitSystem unit);
	/**
	 * Verifica a ocorrência de colisão com um circulo hipotético
	 * @param x - posição x do circulo
	 * @param y - posição y do circulo
	 * @param radiu - raio do circulo
	 * @return Indice do segundo ponto da reta insidente, se não houver intersecção, o retorno é -1
	 */
	public int isCollided(double x, double y, double radiu);
	
	public Shape shape();
	public Shape selectShape();
}
