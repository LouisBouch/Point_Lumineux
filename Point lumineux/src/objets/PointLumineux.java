package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import application.Dessinable;
import physique.Vecteur2D;

public class PointLumineux implements Dessinable{
	private Vecteur2D pos;
	private TraitPrincipal[] traits;
	private Ellipse2D.Double lum = new Ellipse2D.Double();
	private Path2D.Double illuminationTot = new Path2D.Double();
	private int longueur;
	private Color couleur = new Color(50, 145, 168, 127);
	
	public PointLumineux(Vecteur2D pos, int nbTraits) {
		this.pos = pos;
		traits = new TraitPrincipal[nbTraits];
		longueur = traits.length;
		for (int i = 0; i < longueur; i++) {
			traits[i] = new TraitPrincipal();
		}
	}
	@Override
	public void dessiner(Graphics2D g2d) {
//		for (TraitPrincipal trait: traits) {
//			trait.dessiner(g2d);
//		}
		g2d.setColor(couleur);
		g2d.fill(illuminationTot);
		g2d.setColor(Color.YELLOW);
		g2d.fill(lum);
	}
	/**
	 * Permet d'effacer otutes la lumière
	 */
	public void clearPathTot() {
		illuminationTot.reset();
	}
	
	/**
	 * Permet d'ajouter la totaisté de l'illumination
	 * @param point1 Le premier point du triangle
	 * @param point2 Le deuxieme pointt du triangle
	 */
	public void addIlluminationTot(TraitPrincipal[] traitsOrdonnes) {
		illuminationTot.moveTo(traitsOrdonnes[0].getSousTraits()[0].getPosFin().getX(), traitsOrdonnes[0].getSousTraits()[0].getPosFin().getY());
		for (int i = 0; i < longueur; i++) {
			Vecteur2D point1  = traitsOrdonnes[i].getSousTraits()[0].getPosFin(); 
			Vecteur2D point2  = traitsOrdonnes[i].getPosFin(); 
			Vecteur2D point3 = traitsOrdonnes[(i)].getSousTraits()[1].getPosFin();
			illuminationTot.lineTo(point1.getX(), point1.getY());
			illuminationTot.lineTo(point2.getX(), point2.getY());
			illuminationTot.lineTo(point3.getX(), point3.getY());
		}
		illuminationTot.closePath();	
	}
	/**
	 * Permet d'obtenir la position de la lumière
	 * @return Position de la lumière (en pixels)
	 */
	public Vecteur2D getPos() {
		return pos;
	}
	/**
	 * Permet de palcer la lumière
	 * @param pos L'endroit où la placer (en pixels)
	 */
	public void setPos(Vecteur2D pos) {
		this.pos = pos;
		lum.setFrame(pos.getX() - 13/2.0, pos.getY() - 13/2.0, 13, 13);
	}
	/**
	 * Permet d'obtenir les traits
	 * @return Les traits
	 */
	public TraitPrincipal[] getTraits() {
		return traits;
	}
}
///**
// * Permet d'ajouter une zone d'illumination ayant pour sommet le point lumineux
// * @param point1 Le premier point du triangle
// * @param point2 Le deuxieme pointt du triangle
// */
//public void addIllumination(Vecteur2D point1, Vecteur2D point2) {
//	if (point1.isDifferent(point2)) {
//		nbComposantesIlluminees++;
//		illumine.get(nbComposantesIlluminees - 1).moveTo(pos.getX(), pos.getY());
//		illumine.get(nbComposantesIlluminees - 1).lineTo(point1.getX(), point1.getY());
//		illumine.get(nbComposantesIlluminees - 1).lineTo(point2.getX(), point2.getY());
//		illumine.get(nbComposantesIlluminees - 1).closePath();	
//	}
//}
//if (nbComposantesIlluminees > 0) {
//	for (int i = 0; i < longueur; i++) {
//		g2d.fill(illumine.get(i));
//	}
//}
//for (int i = 0; i < longueur; i++) {
//	traits[i] = new TraitPrincipal();
//	illumine.add(new Path2D.Double());
//}
//private int nbComposantesIlluminees = 0;
/**
 * Permet d'effacer les triiangles de lumières
 */
//public void clearPath() {
//	nbComposantesIlluminees = 0;
//	for (Path2D.Double path: illumine) {
//		path.reset();
//	}
//}
//private ArrayList<Path2D.Double> illumine = new ArrayList<Path2D.Double>();
//illumine.add(new Path2D.Double());