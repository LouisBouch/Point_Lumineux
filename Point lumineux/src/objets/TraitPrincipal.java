package objets;

import java.awt.Graphics2D;

import application.Dessinable;
import physique.Vecteur2D;

public class TraitPrincipal extends Traits implements Dessinable  {
	private SousTrait[] sousTraits = {new SousTrait(), new SousTrait()};
	private double angleAbscisseX;
	
	/**
	 * Crée un trait à partir de deux points séparés
	 * @param posIni
	 * @param posFin
	 */
	public TraitPrincipal(Vecteur2D posIni, Vecteur2D posFin) {
		super(posIni, posFin);
	}
	/**
	 * Crée un trait de base
	 */
	public TraitPrincipal() {
		super();
	}
	
	
	/*
	 * Dessine le trait
	 */
	@Override
	public void dessiner(Graphics2D g2d) {
		super.dessiner(g2d);
		for (SousTrait trait: sousTraits) {
			if (trait != null) trait.dessiner(g2d);
		}
	}
//	/**
//	 * Permet de changer la position inintiale du trait
//	 * @param posIni Nouvelle position initiale (en pixels)
//	 */
//	public void setPosIni(Vecteur2D posIni) {
//		super.setPosIni(posIni);
//	}
//	/**
//	 * Permet de changer la position finale du trait
//	 * @param posIni Nouvelle position finale (en pixels)
//	 */
//	public void setPosFin(Vecteur2D posFin) {
//		super.setPosFin(posFin);
//	}
	/**
	 * Permet de changer la position inintiale du trait et des sous-traits
	 * @param posIni Nouvelle position initiale (en pixels)
	 */
	public void setPosIniTous(Vecteur2D posIni) {
		super.setPosIni(posIni);
		sousTraits[0].setPosIni(posIni);
		sousTraits[1].setPosIni(posIni);
	}
	/**
	 * Permet de changer la position finale du trait et des sous-traits
	 * @param posIni Nouvelle position finale (en pixels)
	 */
	public void setPosFinTous(Vecteur2D posFin) {
		super.setPosFin(posFin);
		sousTraits[0].setPosFin(shiftDeg(posFin, 1E-10));
		sousTraits[1].setPosFin(shiftDeg(posFin, -1E-10));
	}
	/**
	 * Permet d'obtenir les sous-traits
	 * @return Les sous traits
	 */
	public SousTrait[] getSousTraits() {
		return sousTraits;
	}
	/**
	 * Établit l'angle entre le trait et l'abscisse des X
	 * @param angleAbscisseX
	 */
	public void setAngleAbscisseX(double angleAbscisseX) {
		this.angleAbscisseX = angleAbscisseX;
	}
	/**
	 * Permet d'obtenir l'angle entre le trait et l'abscisse
	 * @return l'angle entre le trait et l'abscisse
	 */
	public double getAngleAbscisseX() {
		return angleAbscisseX;
	}
	
	
	
	
}
