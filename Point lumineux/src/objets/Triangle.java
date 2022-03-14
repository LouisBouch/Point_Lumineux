package objets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import physique.Vecteur2D;

public class Triangle extends Obstacles {
	private Vecteur2D[] listePoints;
	private Path2D.Double triangle = new Path2D.Double();
	
	public Triangle(Vecteur2D[] points) {
		super(points);
		listePoints = points;
	}

	@Override
	public void dessiner(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke((int) 3));
		g2d.setColor(Color.DARK_GRAY);
		triangle.moveTo(listePoints[0].getX(), listePoints[0].getY());
		for(int i = 1; i < listePoints.length; i++) {
			triangle.lineTo(listePoints[i].getX(), listePoints[i].getY());
		}
		triangle.lineTo(listePoints[0].getX(), listePoints[0].getY());
		g2d.draw(triangle);
		triangle.reset();
	}

}
