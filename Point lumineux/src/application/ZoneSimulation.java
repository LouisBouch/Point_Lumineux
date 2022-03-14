package application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JPanel;

import objets.Ligne;
import objets.Niveau;
import objets.Obstacles;
import objets.Triangle;
import physique.Calculs;
import physique.Vecteur2D;

public class ZoneSimulation extends JPanel {
	private static final long serialVersionUID = -3599969191469067224L;
	
	private int largeur;
	private int hauteur;
	
	private static int lar;
	private static int hau;

	private Niveau niveau;
	private ArrayList<Obstacles> obstacles = new ArrayList<Obstacles>();
	private Calculs calc;
	
	/**
	 * Crée une instance de la zone de simulation
	 */
	public ZoneSimulation() {
//		Vecteur2D point = new Vecteur2D(100, 100);
//		Vecteur2D v1 = new Vecteur2D(90, 110);
//		Vecteur2D v2 = new Vecteur2D(110, 110);
//		System.out.println(point.getOrientation(v1, v2));
//		System.out.println(v1.produitVectoriel(v2));
//		int a = 3;
//		int b = 1;
//		int c = 2;
//		if (a > b && a > c) {
//			if (b > c) System.out.println(b);
//			else System.out.println(c);
//		}
//		else if (a < b && a < c) {
//			if (b < c) System.out.println(b);
//			else System.out.println(c);
//		}
//		else System.out.println(a);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					niveau.getPointLumineux().setPos(new Vecteur2D(e.getX(), e.getY()));
					calc.calc();
					repaint();
				}
				else System.out.println(e.getX() + "  " + e.getY());
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				niveau.getPointLumineux().setPos(new Vecteur2D(e.getX(), e.getY()));
				calc.calc();
				repaint();
			}
		});
		this.setSize(1914, 1002);
		largeur = this.getWidth();
		hauteur = this.getHeight();
		lar = largeur;
		hau = hauteur;
		Vecteur2D[] triangle = {new Vecteur2D(100, 500),new Vecteur2D(200, 700),new Vecteur2D(500, 800)};
		obstacles.add(new Triangle(triangle));
		obstacles.add(new Ligne(new Vecteur2D(200, 600), new Vecteur2D(600, 200)));
		obstacles.add(new Ligne(new Vecteur2D(700, 700), new Vecteur2D(200, 300)));
		obstacles.add(new Ligne(new Vecteur2D(200, 700), new Vecteur2D(200, 200)));
//		obstacles.add(new PointTest(new Vecteur2D(1200, 500)));
		niveau = new Niveau(obstacles, this);
		calc = new Calculs(niveau);
		calc.calc();
		setBackground(Color.black);
	}//Fin ZoneSimulation
	
	/**
	 * Dessine sur l'application
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		niveau.dessiner(g2d);
	}

	/**
	 * Permet d'obtenir la largeur de la zone de simulation
	 */
	public int getLargeur() {
		return largeur;
	}
	/**
	 * Permet d'obtenir la hauteur de la zone de simulation
	 */
	public int getHauteur() {
		return hauteur;
	}
	/**
	 * Permet d'obtenir la largeur de la zone de simulation
	 */
	public static int getLar() {
		return lar;
	}
	/**
	 * Permet d'obtenir la hauteur de la zone de simulation
	 */
	public static int getHau() {
		return hau;
	}
}

/**
 * Repaint et calculs pour garder le tout à jour
 */

//public void run() {
//	while(running) {
//		calc.calc();
//		repaint();
//		try {
//			Thread.sleep(sleep);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//}//Fin run
///**
// * Démarre la simulation
// */
//public void demarrer() {	
//	if (!running) {
//		running = true;
//		simulation = new Thread(this);
//		simulation.start();	
//	}
//}//Fin demarrer
///**
// * Arrête la simulation
// */
//public void arreter() {
//	running = false;
//}//Fin arrêter
