package objets;

import java.awt.Graphics2D;
import java.util.ArrayList;

import application.Dessinable;
import application.ZoneSimulation;
import physique.Calculs;
import physique.Vecteur2D;

public class Niveau implements Dessinable {
	private PointLumineux pointLumineux;
	private ArrayList<Obstacles> obstacles;
	private ArrayList<Vecteur2D> intersectionsObstacles = new ArrayList<Vecteur2D>();
	private int nbPoints = 0;
	private double largeur;
	private double hauteur;
	
	public Niveau(ArrayList<Obstacles> obstacles, ZoneSimulation zone) {
		largeur = zone.getLargeur();
		hauteur = zone.getHauteur();
		this.obstacles = obstacles;
		for (Obstacles obs: obstacles) {
			nbPoints += obs.getPoints().length;
		}
		ArrayList<Vecteur2D> inters = Calculs.interObjets(obstacles);
		if (inters != null) {
			intersectionsObstacles = inters;
			nbPoints += intersectionsObstacles.size();
		}
		pointLumineux = new PointLumineux(new Vecteur2D(0, 0), nbPoints + 4);
	}
	@Override
	public void dessiner(Graphics2D g2d) {
		for (Obstacles obs: obstacles) {
			obs.dessiner(g2d);
		}
		pointLumineux.dessiner(g2d);
	}
	/**
	 * Permet d'obtenir l'objet lumiere
	 * @return La lumiere
	 */
	public PointLumineux getPointLumineux() {
		return pointLumineux;
	}
	/**
	 * Permet d'obtenir les obstacles contenu dans le niveau
	 * @return Les obstacles
	 */
	public ArrayList<Obstacles> getObstacles() {
		return obstacles;
	}
	/**
	 * Permet d'obtenir le nombre de coins dans le niveau
	 * @return Le nombre de coins
	 */
	public int getNbPoints() {
		return nbPoints;
	}
	/**
	 * Obtient la largeur du niveau
	 * @return La largeur du niveau (en pixels)
	 */
	public double getLargeur() {
		return largeur;
	}
	/**
	 * Obtient la hauteur du niveau
	 * @return La hauteur du niveau (en pixels)
	 */
	public double getHauteur() {
		return hauteur;
	}
	/**
	 * Permet d'obtenir les points d'intersections entre les objets
	 * @return Les points d'intersections
	 */
	public ArrayList<Vecteur2D> getIntersectionsObstacles() {
		return intersectionsObstacles;
	}
	
	
	
}
