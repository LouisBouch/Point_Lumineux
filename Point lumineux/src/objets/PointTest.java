package objets;

import java.awt.Graphics2D;

import physique.Vecteur2D;

public class PointTest extends Obstacles {
	Vecteur2D pos;
	
	public PointTest(Vecteur2D pos){
		super(getPoint(pos));
		this.pos = pos;
	}

	@Override
	public void dessiner(Graphics2D g2d) {
		
	}
	public static Vecteur2D[] getPoint(Vecteur2D pos) {
		Vecteur2D[] points = {pos};
		return points;
	}

	public Vecteur2D getPos() {
		return pos;
	}
	
}
