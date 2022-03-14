package physique;

import java.util.ArrayList;

import application.ZoneSimulation;
import objets.PointLumineux;
import objets.Niveau;
import objets.Obstacles;
import objets.SousTrait;
import objets.TraitPrincipal;
import objets.Traits;

public class Calculs {
	private Vecteur2D posLum;
	private TraitPrincipal[] traits;
	private TraitPrincipal[] traitsOrdonnes;
	private ArrayList<Obstacles> obstacles;
	ArrayList<Vecteur2D> inter;
	private PointLumineux lumiere;
	private Niveau niveau;

	/**
	 * Cr�e une instance calcul et pr�pare les donn�es n�c�ssaire
	 * @param niv
	 */
	public Calculs(Niveau niv) {
		niveau = niv;
		posLum = niv.getPointLumineux().getPos();
		traits = niv.getPointLumineux().getTraits();
		obstacles = niv.getObstacles();
		lumiere = niv.getPointLumineux();
		inter = niveau.getIntersectionsObstacles();
		int place = 0;
		//Place les traits aux extr�mit�s des segments
		for (int i = 0; i < obstacles.size(); i++) {
			for (int o = 0; o < obstacles.get(i).getPoints().length; o++) {
				Vecteur2D pos = obstacles.get(i).getPoints()[o];
				traits[place].setPosFinTous(pos);
				place++;
			}	
		}
		//place les traits aux intersection de segments
		int sizeInter = inter.size();
		for (int i = 0; i < sizeInter; i++) {
			traits[place + i].setPosFinTous(inter.get(i));;
		}
		//Place des traits dans les coins
		placerCoins();
	}
	/**
	 * Effectu les calculs n�c�ssaire pour d�placer la lumi�re
	 */
	public void calc() {
		//Initialise les positions des traits
		int place = 0;//Garde en m�moire le nombre de trait d�j� utilis�s
		for (int i = 0; i < obstacles.size(); i++) {
			for (int o = 0; o < obstacles.get(i).getPoints().length; o++) {
				Vecteur2D pos = obstacles.get(i).getPoints()[o];
				posLum = lumiere.getPos();
				traits[place].setPosIniTous(lumiere.getPos());
				traits[place].setPosFinTous(pos);
				place++;
			}	
		}
		//place les traits aux intersection de segments
		int sizeInter = inter.size();
		for (int i = 0; i < sizeInter; i++) {
			traits[place + i].setPosIniTous(lumiere.getPos());
			traits[place + i].setPosFinTous(inter.get(i));;
		}
		//Place les traits des coins
		placerCoins();
		//Effectue les calculs n�c�ssaire pour d�tecter les intersections
		for (int i = 0; i < traits.length; i++) {//Passe � travers chaque trait
			for (int w = 0; w < obstacles.size(); w++) {//It�re chaque objet
				Vecteur2D[] points = obstacles.get(w).getPoints();
				int nbSeg = points.length;
				if (nbSeg == 2) nbSeg = 1;
				for (int z = 0; z < nbSeg; z++) {//It�re chaque segment de l'objet
					Vecteur2D intersection = intersecte(traits[i].getPosIni(), traits[i].getPosFin(), points[z], points[(z + 1) % points.length]);//Fait les calculs d'intersections pour le trait principal
					if (intersection != null) traits[i].setPosFin(intersection);
					
					for (SousTrait trait: traits[i].getSousTraits()) {//Fait les calculs d'intersections pour chaque sous-traits
						intersection = intersecte(trait.getPosIni(), trait.getPosFin(), points[z], points[(z + 1) % points.length]);
						if (intersection != null) trait.setPosFin(intersection);
					}
				}
			}
		}
		//Effectue les calculs n�c�ssaire pour d�terminer l'angle des rayons
		for (int i = 0; i < traits.length; i++) {//Passe � travers chaque trait
			Vecteur2D abscisseX = new Vecteur2D(1, 0);
			Vecteur2D vecTrait = traits[i].getVecteurTrait();
			double angle = abscisseX.getAngle(vecTrait);
			if (vecTrait.getY() > 0) angle = 360 - angle;
			traits[i].setAngleAbscisseX(angle);
		}
		traitsOrdonnes = classerTraits(traits);
		niveau.getPointLumineux().clearPathTot();
		niveau.getPointLumineux().addIlluminationTot(traitsOrdonnes);
	}

	/**
	 * Ajoute une angle au vecteur pour le distinguer du principale
	 * @param vecPoint Le vecteur position du point de l'obstacle
	 * @return La nouvelle position finale du vecteur
	 */
	public Vecteur2D shiftDeg(Vecteur2D vecPoint, double shift) {
		Vecteur2D vec = vecPoint.soustrait(posLum);
		double degree = Math.atan(vec.getY()/vec.getX());
		if (vec.getX() < 0) degree += Math.PI;
		Vecteur2D newVec = new Vecteur2D(Math.cos(degree + shift) * vec.getLongueur(), Math.sin(degree + shift) * vec.getLongueur());
		double multiplicateur = vecMax(posLum.additionne(newVec));
		return posLum.additionne(newVec.multiplicationParUnScalaire(multiplicateur));
	}
	/**
	 * Calcul un multtiplicateur pour s'assurer que le vecteur touche � la bordure de l'�cran
	 * @param vecPoint Le vecteur position du point de l'obstacle
	 * @return La valeur � utiliser our la multiplicatoin de vectteur
	 */
	public double vecMax(Vecteur2D vecPoint) {
		Vecteur2D vecTrait = vecPoint.soustrait(posLum);
		double largeur = ZoneSimulation.getLar();
		double hauteur = ZoneSimulation.getHau();
		double multVerticale;
		double multHorizontale;
		if (vecTrait.getY() <= 0)  multVerticale = Math.abs(posLum.getY() / vecTrait.getY());
		else multVerticale = (hauteur - posLum.getY()) / vecTrait.getY();

		if (vecTrait.getX() <= 0) multHorizontale = Math.abs(posLum.getX() / vecTrait.getX());
		else multHorizontale = (largeur - posLum.getX()) / vecTrait.getX();

		if (multVerticale < multHorizontale) return multVerticale;
		else return multHorizontale;
	}
	/**
	 * Fait un calcul d'intersection. S'il y a bel et bien une intersection, le point intersect� sera retourn�. Sinon La valeur null sera retourn�e.
	 * @param traitPointIni Le point initiale du premier trait
	 * @param traitPointIni Le point Finale du premier trait
	 * @param segPointIni Le point de d�part du segment
	 * @param segPointFin Le point d'arriv� du segment
	 * @return Le point intersect� s'il y a lieu
	 */
	public static Vecteur2D intersecte(Vecteur2D traitPointIni, Vecteur2D traitPointFin, Vecteur2D segPointIni, Vecteur2D segPointFin) {
		double ori1;
		double ori2;
		double ori3;
		double ori4;
		//Premi�re �tape (Orientation trait lumi�re et premier point du segment)
		ori1 = segPointIni.getOrientation(traitPointIni, traitPointFin);
		//Deuxi�me �tape (Orientation trait lumi�re et deuxi�me point du segment)
		ori2 = segPointFin.getOrientation(traitPointIni, traitPointFin);
		if (ori1 * ori2 <= 0) {
			//Troisi�me �tape (Orientation segment et premier point du trait de lumi�re)
			ori3 = traitPointIni.getOrientation(segPointIni, segPointFin);
			//Quatri�me �tape (Orientation segment et deuxi�me point du trait de lumi�re)
			ori4 = traitPointFin.getOrientation(segPointIni, segPointFin);
			if (ori3 * ori4 <= 0) {
				Vecteur2D vOrLum = traitPointFin.soustrait(traitPointIni);
				Vecteur2D vOrSeg = segPointFin.soustrait(segPointIni);
				if (ori1 == 0 && ori2 == 0 && ori3 == 0 && ori4 == 0) {	
					Vecteur2D interParallele = intersecteParallele(traitPointIni,  traitPointFin, segPointIni, segPointFin);
					return interParallele;	
				}
				else return pointIntersection(traitPointIni, vOrLum, segPointIni, vOrSeg);
			}
			else return null;
		}
		else return null;
	}
	/**
	 * Permet de savoir si le segment est � l'interieur du rayon de lumi�re
	 * @param traitIni Le point de d�part du trait de lumi�re
	 * @param traitFin Le point d'arriv� du trait de lumi�re
	 * @param pointIni Le point de d�part du segment
	 * @param pointFin Le point d'arriv� du segment
	 * @return Le point d'intersection s'il y a lieu
	 */
	public static Vecteur2D intersecteParallele(Vecteur2D traitIni, Vecteur2D traitFin, Vecteur2D pointIni, Vecteur2D pointFin) {
		boolean seg1 = false;
		boolean seg2 = false;
		double distpoint1Ini;
		double distPoint2Ini;
		double distpoint1Fin;
		double distPoint2Fin;
		double pointIniP;
		double pointFinP;
		double traitIniP;
		double traitFinP;
		if (traitFin.soustrait(traitIni).getX() != 0) {
			pointIniP = pointIni.getX();
			pointFinP = pointFin.getX();
			traitIniP = traitIni.getX();
			traitFinP = traitFin.getX();
		}
		else {
			pointIniP = pointIni.getY();
			pointFinP = pointFin.getY();
			traitIniP = traitIni.getY();
			traitFinP = traitFin.getY();
		}
		distpoint1Ini = pointIniP - traitIniP;//Distance entre le point initiale du segment et le point initiale du trait
		distPoint2Ini = pointIniP - traitFinP;//Distance entre le point initiale du segment et le point finale du trait
		if (distpoint1Ini * distPoint2Ini <= 0) seg1 = true;

		distpoint1Fin = pointFinP - traitIniP;//Distance entre le point finale du segment et le point initiale du trait
		distPoint2Fin = pointFinP - traitFinP;//Distance entre le point finale du segment et le point finale du trait  
		if (distpoint1Fin * distPoint2Fin <= 0) seg2 = true;
		if (seg1 && !seg2) {
			if (distpoint1Ini * distpoint1Fin <= 0) return traitIni;//Retourne la poisition de la lumi�re si celle-ci se trouve � l'int�rieur du segment
			else return pointIni;
		}
		if (!seg1 && seg2) {
			if (distpoint1Ini * distpoint1Fin <= 0) return traitIni;//Retourne la poisition de la lumi�re si celle-ci se trouve � l'int�rieur du segment
			else return pointFin;
		}
		if (seg1 && seg2) {
			if (Math.abs(distpoint1Ini) < Math.abs(distpoint1Fin)) return pointIni;
			else return pointFin;
		}
		return null;
	}
	/**
	 * Peremt de calculer le point d'intersection entre deux droites
	 * @param v1 Point initiale de la premi�re droite
	 * @param v1Or Vecteur orientation de la premi�re droite
	 * @param v2 Point initiale de la deuxi�me droite
	 * @param v2Or Vecteur orientation de la deuxi�me droite
	 * @return Le point ou il y a une intersection
	 */
	public static Vecteur2D pointIntersection(Vecteur2D v1, Vecteur2D v1Or, Vecteur2D v2, Vecteur2D v2Or) {
		/*  t  k : r�ponse
		 * |a, b : e|
		 * |c, d : f|
		 */
		if (v1Or.isNull() || v2Or.isNull()) {
			System.out.println("null");
			return null;
		}
		double a = v1Or.getX(), b = -v2Or.getX(), c = v1Or.getY(), d = -v2Or.getY(), e = v2.getX() - v1.getX(), f = v2.getY() - v1.getY();
		double k = (a*f-c*e)/(a*d-c*b);//Multiplicateur du segment
		if (Double.isNaN(k)) {
			System.out.println("NaN");
			return null;
		}
		return v2.additionne(v2Or.multiplicationParUnScalaire(k));	
	}
	/**
	 * Permet de classer les traits principals selon leur orientation par rapport � l'axe des x
	 * @param traitsNonOrdonnes Les traits � classer
	 * @return Le nouveau tableau de traits calss�s
	 */
	public TraitPrincipal[] classerTraits(TraitPrincipal[] traitsNonOrdonnes) {
		TraitPrincipal[] traitsOrdonnes = traitsNonOrdonnes.clone();
		int indexPivot;
		int i = -1;
		double valeurPivot; 
		
		int longueur = traitsOrdonnes.length;
		//Si les traitsOrdonnesx sont trop petits
		if (longueur == 0) {
			return traitsOrdonnes;
		}
		if (longueur == 1) {
			return traitsOrdonnes;
		}
		else if (longueur == 2) {
			if (traitsOrdonnes[0].getAngleAbscisseX() < traitsOrdonnes[1].getAngleAbscisseX()) return traitsOrdonnes;
			else {
				return changer(traitsOrdonnes, 0, 1);
			}
		}
		//Si les traitsOrdonnesx sont plus grands que 2, trouve le pivot et les index
		//Pivot
		if (traitsOrdonnes[0].getAngleAbscisseX() > traitsOrdonnes[longueur/2].getAngleAbscisseX() && traitsOrdonnes[0].getAngleAbscisseX() > traitsOrdonnes[longueur - 1].getAngleAbscisseX()) {
			if (traitsOrdonnes[longueur/2].getAngleAbscisseX() > traitsOrdonnes[longueur - 1].getAngleAbscisseX()) indexPivot = longueur/2;
			else indexPivot = longueur - 1;
		}
		else if (traitsOrdonnes[0].getAngleAbscisseX() < traitsOrdonnes[longueur/2].getAngleAbscisseX() && traitsOrdonnes[0].getAngleAbscisseX() < traitsOrdonnes[longueur - 1].getAngleAbscisseX()) {
			if (traitsOrdonnes[longueur/2].getAngleAbscisseX() < traitsOrdonnes[longueur - 1].getAngleAbscisseX()) indexPivot = longueur/2;
			else indexPivot = longueur - 1;
		}
		else indexPivot = 0;
		valeurPivot = traitsOrdonnes[indexPivot].getAngleAbscisseX();

		//Index
		for (int j = 0; j < traitsOrdonnes.length; j++) {
			if (j != indexPivot) {
				if (traitsOrdonnes[j].getAngleAbscisseX() < valeurPivot) {
					i++;
					if (i != j) {
						if (i == indexPivot) indexPivot = j;
						changer(traitsOrdonnes, i, j);
					}
				}
			}
		}
		if (indexPivot != i + 1) {		
			changer(traitsOrdonnes, indexPivot, i + 1);
			indexPivot = i + 1;
		}
		//Fait appel � la r�cursivit� pour s�parer les taches
		return merge(
				classerTraits(
						separer(traitsOrdonnes, 0, indexPivot - 1)), 
				traitsOrdonnes[indexPivot], 
				classerTraits(
						separer(traitsOrdonnes, indexPivot + 1, longueur - 1)));

	}
	/**
	 * Peremt de coller deux tableaux de traits ainsi qu'un point milieu
	 * @param plusPetits Le tableau des petits
	 * @param milieu Le trait du milieu
	 * @param plusGrands Le tableau des grands
	 * @return Le nouveau tableau
	 */
	public TraitPrincipal[] merge(TraitPrincipal[] plusPetits, TraitPrincipal milieu, TraitPrincipal[] plusGrands) {
		TraitPrincipal[] tout = new TraitPrincipal[plusPetits.length + plusGrands.length + 1];
		for (int i = 0; i < plusPetits.length; i++) {
			tout[i] = plusPetits[i];
		}
		tout[plusPetits.length] = milieu;
		for (int i = 0; i < plusGrands.length; i++) {
			tout[i + plusPetits.length + 1] = plusGrands[i];
		}
		return tout;
	}
	/**
	 * Permet de s�parer un tableau en un sous-tableau
	 * @param traits Le tableau initiale
	 * @param indexIni D�but du nouveau tableau
	 * @param indexFin Fin du nouveau tableau
	 * @return Le nouveau tableau
	 */
	public TraitPrincipal[] separer(TraitPrincipal[] traits, int indexIni, int indexFin) {
		TraitPrincipal[] partie = new TraitPrincipal[indexFin - indexIni + 1];
		for (int i = 0; i < indexFin - indexIni + 1; i++) {
			partie[i] = traits[indexIni + i];
		}
		return partie;
	}
	/**
	 * Permet de placer les traits des coins
	 */
	public void placerCoins() {
		int length = traits.length;
		traits[length - 1].setPosIniTous(lumiere.getPos());
		traits[length - 2].setPosIniTous(lumiere.getPos());
		traits[length - 3].setPosIniTous(lumiere.getPos());
		traits[length - 4].setPosIniTous(lumiere.getPos());
		
		traits[length - 1].setPosFinTous(new Vecteur2D());
		traits[length - 2].setPosFinTous(new Vecteur2D(ZoneSimulation.getLar(), 0));
		traits[length - 3].setPosFinTous(new Vecteur2D(0, ZoneSimulation.getHau()));
		traits[length - 4].setPosFinTous(new Vecteur2D(ZoneSimulation.getLar(), ZoneSimulation.getHau()));
	}
	/**
	 * Permet d'�changer deux valeurs dans un tableau
	 * @param tab Le tableau
	 * @param index1 Premier �l�ment � changer
	 * @param index2 Deuxi�me �l�ment � changer
	 * @return Le nouveau tableau
	 */
	public TraitPrincipal[] changer(TraitPrincipal[] tab, int index1, int index2) {
		TraitPrincipal sub = tab[index1];
		tab[index1] = tab[index2];
		tab[index2] = sub;
		return tab;
	}
	/**
	 * Permet de toruver les intersections entre obstacles
	 * @return Les points d'intersections
	 */
	public static ArrayList<Vecteur2D> interObjets(ArrayList<Obstacles> obs) {
		ArrayList<Vecteur2D> inter = new ArrayList<Vecteur2D>();
		for (Obstacles obIni: obs) {//It�re chaque obstacle
			Vecteur2D[] pointsIni = obIni.getPoints();
			int lengthIni = pointsIni.length;

			for (int i = 0; i < lengthIni; i++) {//It�re chaque point de l'obstacle
					Vecteur2D pointIni = pointsIni[i];
					Vecteur2D pointFin = pointsIni[(i + 1) % lengthIni];; 
				for (Obstacles obTeste: obs) {//It�re chaque obstacle pour tester les collisions
					Vecteur2D[] pointsTeste = obTeste.getPoints();
					int lengthTeste = pointsTeste.length;

					for (int o = 0; o < lengthTeste; o++) {//It�re chaque point de l'obstacle pour tester les collisions
						Vecteur2D intersect = intersecte(pointsIni[i], pointsIni[(i + 1) % lengthIni], pointsTeste[o], pointsTeste[(o + 1) % lengthTeste]);
						if (intersect != null) inter.add(intersect);
					}
				}
			}

		}

		return inter;
	}
		
}
//	double k = (7-1*10/2.0)/(3-1*4/2.0);
//	double t = (10-(4/(3-1*4/2.0))*(7-1*10/2.0))/2.0;
//	System.out.println(a);
//	System.out.println(b);
//	System.out.println(c);
//	System.out.println(d);
//	System.out.println(e);
//	System.out.println(f);
//	System.out.println();
//	if (a == 0) k = 0;
//	else k = 1;
//	if (v1Or.isParallele(v2Or)) {
//	System.out.println("Parall�le");
//}
//k = (f-c*e/a)/(d-c*b/a);//Multiplicateur du segment
//		return v1.additionne(v1Or.multiplicationScalaire(t));
//if (indexPivot == longueur - 1) {
//TraitPrincipal sub = traitsOrdonnes[indexPivot];
//traitsOrdonnes[indexPivot] = traitsOrdonnes[indexLeft];
//traitsOrdonnes[indexLeft] = sub;
//indexPivot = indexLeft;
//}
//else if (indexPivot == 0) {
//TraitPrincipal sub = traitsOrdonnes[indexRight];
//traitsOrdonnes[indexRight] = traitsOrdonnes[indexPivot];
//traitsOrdonnes[indexPivot] = sub;
//indexPivot = indexRight;
//}
//else {
//if (longueur == 3) return traitsOrdonnes;
//}
//int longueur = traitsOrdonnes.length;
//if (longueur == 1) {
//	return traitsOrdonnes;
//}
//else if (longueur == 2) {
//	if (traitsOrdonnes[0].getAngleAbscisseX() < traitsOrdonnes[1].getAngleAbscisseX()) return traitsOrdonnes;
//	else {
//		TraitPrincipal sub = traitsOrdonnes[0];
//		traitsOrdonnes[0] = traitsOrdonnes[1];
//		traitsOrdonnes[1] = sub;
//		return traitsOrdonnes;
//	}
//}
//else {
//	int indexPivot;
//	int indexLeft = -1;
//	int indexRight = -1;
//
//	double valeurPivot;
//	boolean goodMatch = false;
//
//
//	if (traitsOrdonnes[0].getAngleAbscisseX() > traitsOrdonnes[longueur/2].getAngleAbscisseX() && traitsOrdonnes[0].getAngleAbscisseX() > traitsOrdonnes[longueur - 1].getAngleAbscisseX()) {
//		if (traitsOrdonnes[longueur/2].getAngleAbscisseX() > traitsOrdonnes[longueur - 1].getAngleAbscisseX()) indexPivot = longueur/2;
//		else indexPivot = longueur - 1;
//	}
//	else if (traitsOrdonnes[0].getAngleAbscisseX() < traitsOrdonnes[longueur/2].getAngleAbscisseX() && traitsOrdonnes[0].getAngleAbscisseX() < traitsOrdonnes[longueur - 1].getAngleAbscisseX()) {
//		if (traitsOrdonnes[longueur/2].getAngleAbscisseX() < traitsOrdonnes[longueur - 1].getAngleAbscisseX()) indexPivot = longueur/2;
//		else indexPivot = longueur - 1;
//	}
//	else indexPivot = 0;
//
//	valeurPivot = traitsOrdonnes[indexPivot].getAngleAbscisseX();
//
//	//� repenser
//	for (int i = 0; true; i++) {
//		if (i == longueur) {
//			return traitsOrdonnes;
//		}
//		if (traitsOrdonnes[i].getAngleAbscisseX() >= valeurPivot && i != indexPivot && (indexLeft < 0 || !goodMatch)) {
//			indexLeft = i;
//			if (traitsOrdonnes[i].getAngleAbscisseX() != valeurPivot) goodMatch = true;
//		}
//		if (traitsOrdonnes[longueur - 1 - i].getAngleAbscisseX() <= valeurPivot && longueur - 1 - i != indexPivot && (indexRight < 0 || !goodMatch)) {
//			indexRight = longueur - 1 - i;
//			if (traitsOrdonnes[longueur - 1 - i].getAngleAbscisseX() != valeurPivot) goodMatch = true;
//		}
//		if (indexLeft > -1 && indexRight > -1 && goodMatch) {
//			if (indexLeft > indexRight) {
//				if (indexPivot > indexLeft) {
//					TraitPrincipal sub = traitsOrdonnes[indexPivot];
//					traitsOrdonnes[indexPivot] = traitsOrdonnes[indexLeft];
//					traitsOrdonnes[indexLeft] = sub;
//					indexPivot = indexLeft;
//				}
//				else if (indexPivot < indexRight) {
//					TraitPrincipal sub = traitsOrdonnes[indexRight];
//					traitsOrdonnes[indexRight] = traitsOrdonnes[indexPivot];
//					traitsOrdonnes[indexPivot] = sub;
//					indexPivot = indexRight;
//				}
//				else {
//					if (longueur == 3) return traitsOrdonnes;
//				}
//				return merge(
//						classerTraits(
//								separer(traitsOrdonnes, 0, indexPivot - 1)), 
//						traitsOrdonnes[indexPivot], 
//						classerTraits(
//								separer(traitsOrdonnes, indexPivot + 1, longueur - 1)));
//			}
//			else {
//				if (indexRight != indexLeft) {
//					TraitPrincipal sub = traitsOrdonnes[indexRight];
//					traitsOrdonnes[indexRight] = traitsOrdonnes[indexLeft];
//					traitsOrdonnes[indexLeft] = sub;
//					indexRight = -1;
//					indexLeft = -1;
//					i = 0;
//				}
//				else {
//					if (indexPivot > indexLeft) {
//						TraitPrincipal sub = traitsOrdonnes[indexPivot];
//						traitsOrdonnes[indexPivot] = traitsOrdonnes[indexLeft + 1];
//						traitsOrdonnes[indexLeft + 1] = sub;
//					}
//					else if (indexPivot < indexRight) {
//						TraitPrincipal sub = traitsOrdonnes[indexRight - 1];
//						traitsOrdonnes[indexRight - 1] = traitsOrdonnes[indexPivot];
//						traitsOrdonnes[indexPivot] = sub;
//					}
//					return traitsOrdonnes;
//				}
//			}
//		}
//
//	}
//}
//int longueur = traitsOrdonnes.length;
//niveau.getPointLumineux().clearPath();
//for (int i = 0; i < longueur; i++) {
//	niveau.getPointLumineux().addIllumination(traitsOrdonnes[i].getSousTraits()[1].getPosFin(), traitsOrdonnes[(i + 1) % longueur].getSousTraits()[0].getPosFin());
//}


//double anglePrec = -1;
//boolean ordonne = true;
//for (TraitPrincipal traits: traitsOrdonnes){
//	if (anglePrec < 0) {
//		anglePrec = traits.getAngleAbscisseX();
//	}
//	else {
//		System.out.println(traits.getAngleAbscisseX() - anglePrec);	
//		if (anglePrec - Vecteur2D.getEpsilon() >= traits.getAngleAbscisseX()) {
//			ordonne = false;
//		}
//		anglePrec = traits.getAngleAbscisseX();
//	}
//}
//System.out.println(360 + traitsOrdonnes[0].getAngleAbscisseX() - traitsOrdonnes[traitsOrdonnes.length - 1].getAngleAbscisseX());	
//System.out.println(ordonne);
//System.out.println();