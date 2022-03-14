package objets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import application.Dessinable;
import application.ZoneSimulation;
import physique.Vecteur2D;

public abstract class Traits implements Dessinable{
	private Vecteur2D posIni;
	private Vecteur2D posFin;
	
	/**
	 * Crée un trait à partir de deux points séparés
	 * @param posIni
	 * @param posFin
	 */
	public Traits(Vecteur2D posIni, Vecteur2D posFin) {
		this.posIni = posIni;
		this.posFin = posFin;
	}
	/**
	 * Crée un trait de base
	 */
	public Traits() {
		this.posIni = new Vecteur2D();
		this.posFin = new Vecteur2D();
	}

	/*
	 * Dessine le trait
	 */
	@Override
	public void dessiner(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.setStroke(new BasicStroke((int) 1));
		g2d.drawLine((int) Math.round(posIni.getX()), (int) Math.round(posIni.getY()), (int) Math.round(posFin.getX()), (int) Math.round(posFin.getY()));
	}
	/**
	 * Ajoute une angle au vecteur pour le distinguer du principale
	 * @param vecPoint Le vecteur position du point de l'obstacle
	 * @return La nouvelle position finale du vecteur
	 */
	public Vecteur2D shiftDeg(Vecteur2D vecPoint, double shift) {
		Vecteur2D vec = vecPoint.soustrait(posIni);
		double degree = Math.atan(vec.getY()/vec.getX());
		if (vec.getX() < 0) degree += Math.PI;
		Vecteur2D newVec = new Vecteur2D(Math.cos(degree + shift) * vec.getLongueur(), Math.sin(degree + shift) * vec.getLongueur());
		double multiplicateur = vecMax(posIni.additionne(newVec));
		return posIni.additionne(newVec.multiplicationParUnScalaire(multiplicateur));
	}
	/**
	 * Calcul un multtiplicateur pour s'assurer que le vecteur touche à la bordure de l'écran
	 * @param vecPoint Le vecteur position du point de l'obstacle
	 * @return La valeur à utiliser our la multiplicatoin de vectteur
	 */
	public double vecMax(Vecteur2D vecPoint) {
		Vecteur2D vecTrait = vecPoint.soustrait(posIni);
		double largeur = ZoneSimulation.getLar();
		double hauteur = ZoneSimulation.getHau();
		double multVerticale;
		double multHorizontale;
		if (vecTrait.getY() <= 0)  multVerticale = Math.abs(posIni.getY() / vecTrait.getY());
		else multVerticale = (hauteur - this.posIni.getY()) / vecTrait.getY();
		
		if (vecTrait.getX() <= 0) multHorizontale = Math.abs(posIni.getX() / vecTrait.getX());
		else multHorizontale = (largeur - posIni.getX()) / vecTrait.getX();
		
		if (multVerticale < multHorizontale) return multVerticale;
		else return multHorizontale;
	}
	/**
	 * Permet d'obtenir la position de départ du trait
	 * @return La position de départ du trait
	 */
	public Vecteur2D getPosIni() {
		return posIni;
	}
	/**
	 * Permet de changer la position inintiale du trait
	 * @param posIni Nouvelle position initiale (en pixels)
	 */
	public void setPosIni(Vecteur2D posIni) {
		this.posIni = posIni;
	}
	/**
	 * Permet d'obtenir la position finale du trait
	 * @return La position finale du trait
	 */
	public Vecteur2D getPosFin() {
		return posFin;
	}
	/**
	 * Permet de changer la position finale du trait
	 * @param posIni Nouvelle position finale (en pixels)
	 */
	public void setPosFin(Vecteur2D posFin) {
		this.posFin = posFin;

	}
	/**
	 * Permet d'obtenir le vecteur du trait
	 * @return Le vecteur trait
	 */
	public Vecteur2D getVecteurTrait() {
		return posFin.soustrait(posIni);
	}
}
