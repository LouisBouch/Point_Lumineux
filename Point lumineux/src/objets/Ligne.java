package objets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import physique.Vecteur2D;

public class Ligne extends Obstacles {
	private Vecteur2D posIni;
	private Vecteur2D posFin;
	
	/**
	 * Crée une ligne à partir d'un tableau de deux points
	 * @param points La liste de points
	 */
	private Ligne(Vecteur2D[] points) {
		super(points);
		this.posIni = points[0];
		this.posFin = points[1];
	}
	/**
	 * Crée une ligne à partir de deux points séparés
	 * @param posIni
	 * @param posFin
	 */
	public Ligne(Vecteur2D posIni, Vecteur2D posFin) {
		this(getPoints(posIni, posFin));
		this.posIni = posIni;
		this.posFin = posFin;
	}
	/*
	 * Dessine la ligne
	 */
	@Override
	public void dessiner(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke((int) 3));
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawLine((int) Math.round(posIni.getX()), (int) Math.round(posIni.getY()), (int) Math.round(posFin.getX()), (int) Math.round(posFin.getY()));
	}
	/**
	 * Permet de transformé les points en tableau
	 * @param posIni La position initiale de la ligne
	 * @param posFin LA position finale de la ligne
	 * @return Le tableau formé des points de la ligne
	 */
	public static Vecteur2D[] getPoints(Vecteur2D posIni, Vecteur2D posFin) {
		Vecteur2D[] points = {posIni, posFin};
		return points;
	}
}
