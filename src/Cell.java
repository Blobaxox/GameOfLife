import java.awt.Color;
import java.awt.Graphics2D;

public class Cell implements Cloneable{
	
	private static int nbCellule = 0;
	private int id;
	private int x,y,size; // x et y du coin haut gauche de la cell // taille de la cell (hauteur et largeur)
	private Graphics2D grid; //reference du graphique utilisé par la fenêtre
	private boolean isAlive;
	
	
	
 	public Cell(int x, int y, int size, Graphics2D grid, boolean isAlive) {
		super();
		this.id = nbCellule;
		nbCellule++;
		this.x = x;
		this.y = y;
		this.size = size;
		this.grid = grid;
		this.isAlive = isAlive;
	}
 	
 	
 	public boolean isClicked(int x,int y) {
 		if((x >= this.x && x <= this.x+size) && (y >= this.y && y <= this.y+size)) {
 			return true;
 		}else {
 			return false;
 		}
 	}
 	
 	public void drawCell() {
 		if(!isAlive)
 		{
 			grid.setColor(Color.white);
 			grid.fillRect(x+1, y, size, size);
 			grid.setColor(Color.black);
 			grid.drawRect(x, y, size, size);
 		}
 		else
 			grid.fillRect(x+1, y, size, size);
 	}
 	
 	public void changeScale(int x,int y, int size)
 	{
 		setX(x);
 		setY(y);
 		setSize(size);
 	}
 	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public boolean getIsAlive() {
		return isAlive;
	}
	public boolean setIsAlive(boolean isAlive) { //return false if didn't change
		if (this.isAlive == isAlive)
		{
			return false;
			
		}else
		{
			this.isAlive = isAlive;
			return true;
			
		}
		
	}
	
	
}
