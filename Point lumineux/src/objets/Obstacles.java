package objets;

import java.awt.Graphics2D;

import application.Dessinable;
import physique.Vecteur2D;

public abstract class Obstacles implements Dessinable {
	private Vecteur2D[] points;
	
	public Obstacles(Vecteur2D[] points) {
		this.points = points;
	}
	
	@Override
	public abstract void dessiner(Graphics2D g2d) ;

	/**
	 * Permet d'obtenir les points de l'obstacle
	 * @return Les points de l'obstacle
	 */
	public Vecteur2D[] getPoints() {
		return points;
	}		
}
