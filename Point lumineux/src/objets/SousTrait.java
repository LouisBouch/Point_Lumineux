package objets;

import java.awt.Graphics2D;

import application.Dessinable;
import physique.Vecteur2D;

public class SousTrait extends Traits implements Dessinable {
	/**
	 * Crée un sous-trait à partir de deux points séparés
	 * @param posIni
	 * @param posFin
	 */
	public SousTrait(Vecteur2D posIni, Vecteur2D posFin) {
		super(posIni, posFin);
	}
	/**
	 * Crée un sous-trait de base
	 */
	public SousTrait() {
		super();
	}
	/*
	 * Dessine le trait
	 */
	@Override
	public void dessiner(Graphics2D g2d) {
		super.dessiner(g2d);
	}

}
