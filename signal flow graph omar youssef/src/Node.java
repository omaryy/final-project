import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class Node {

	 int x,y,label,raduis;
	Color color ;
	Ellipse2D.Double node ;
	
	public Node(){
		this.raduis = 30 ;
		color = Color.cyan ;
		node = new Ellipse2D.Double(0, 0, raduis , raduis);
	}
	
	public void setDimesion(int x,int y ){
		this.x = x ;
		this.y = y ;
		node = new Ellipse2D.Double(x-30 , y-30 , raduis , raduis);
	}
	
	public void setLabel(int label){
		this.label = label ;
	}
	
	public Ellipse2D.Double getNode(){
		return node ;
	}
	
}