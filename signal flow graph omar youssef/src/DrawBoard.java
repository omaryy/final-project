
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.security.spec.ECField;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.management.AttributeChangeNotification;
import javax.print.attribute.standard.MediaSize.Other;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DrawBoard extends JPanel{
	
	boolean newEdge = false , newNode = false , node1Checked = false ;
	boolean newNodeAdded , newEdgeAdded , newGraphPressed , getGainPressed ;
	ArrayList<Node> nodes ;
	Node currentNode , node1 , node2;
	int numOfNodes = 0;
	Image background ;
	ArrayList<ArrayList<Integer>> edges ;
	ArrayList<ArrayList<Node>> outNode ;
	
	View view ;
	
	String log = "" ;
	
	public DrawBoard(){
	
		edges = new ArrayList<ArrayList<Integer>>();
		outNode = new ArrayList<ArrayList<Node>>();
		
		try {
			background = ImageIO.read(new File("C:\\Users\\elfares\\Desktop\\projects\\signal flow graph mos\\grid.png") );
		}
		
		catch (IOException e1) {
			setWarning("can't load the background");
		}
		
		nodes = new ArrayList<Node>();
		repaint();
		this.setName("draw panel");
		this.setBackground(Color.white);
		setBounds(150, 0, 1200-150, 500);
		setLayout(null);
		
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				if(newNode){
					if( contain(e) ){
						
						currentNode = new Node();
						currentNode.setDimesion(e.getX(),200);
						
						repaint();
					}
				}
			}
			
			public void mouseDragged(MouseEvent e) {}
		});
		
		this.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				
			}
			
			public void mousePressed(MouseEvent e) {
				
			}
			
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(newNode){
					if( contain(e) ){
						boolean intersect = false ;
						for(int i=0 ; i<nodes.size() ; i++){
							Node n = nodes.get(i);
							if( n.node.intersects(currentNode.getNode().getBounds2D()) ){
								intersect = true ;
								break;
							}
						}
						if(intersect){
							setWarning("can't put this node here");
						}
						
						else{
							setCurrentNode(false);
							nodes.add(currentNode);
							outNode.add(new ArrayList<Node>());
							edges.add(new ArrayList<Integer>());
							currentNode.label = numOfNodes ;
							view.log.append("node #"+numOfNodes+" has added succefuly\n");
							numOfNodes++;
							
						}
						
						repaint();
					}
				}
				
				if(newEdge){
					
					if(contain(e)){
						int index = 0 ;
						for(int i=0 ; i<nodes.size() ; i++){
							Node n = nodes.get(i);
							if( n.getNode().contains(e.getPoint()) ){
								if(node1Checked){
									node2 = n ;
									if(outNode.get(node1.label)!=null && outNode.get(node1.label).contains(node2)){
										setWarning("these 2 nodes already have edge connects them");
									}
									else{
										String sGain = "" ;
										int gain = 0 ;
										
										while(gain==0.0 || sGain==""){
											try{
												 sGain = JOptionPane.showInputDialog(getComponentPopupMenu(), "enter the gain of this edge");
												 gain = Integer.parseInt( sGain ); 
											}
											catch(Exception ex){
												
											}
										}
										
										
										if(edges.get(node1.label)==null){
											edges.set(node1.label, new ArrayList<Integer>());
										}
										
										if(outNode.get(node1.label)==null){
											outNode.set(node1.label, new ArrayList<Node>());
										}
											
										outNode.get(node1.label).add(node2);
										edges.get(node1.label).add(gain);
										
										
										log = "" ;
								        for(int k=0 ; k<edges.size() ; k++){
								        	ArrayList<Integer> s = edges.get(k);
								        	ArrayList<Node> m = outNode.get(k);
								        	if(s!=null && !s.isEmpty()){
								        		log += "node #"+ Integer.toString(k)+"\n" ;
								        		for(int j=0 ; j<m.size() ; j++){
								        			log += "has an out edge to node #"+ Integer.toString(m.get(j).label)+ " with gain = "+s.get(j).toString()+"\n";
								        		}
								        		log+="\n";
								        	}
								        	
								        }
								        view.log.append("new edge from node #"+node1.label+" to node #"+node2.label+" added\n\n");
								        view.log.append(log);
								        
								        
								        node1 = null ;
										node2 = null ;
										node1Checked = false ;
										setNewEdge(false);
										
										break;
									}
								}
								else{
									node1 = n ;
									node1Checked = true ;
									break ;
								}
							}
						}
						
					}
					
					repaint();
				}
				
			}
		});
		
	}
	
	String getLog(){
		if(newEdgeAdded){
			return log ;
		}
		else
			return "";
	}
	
	void resetLog(){
		log = "";
	}
	
	public boolean contain(MouseEvent e){
		return this.contains(e.getPoint());
	}
	
	public void painting(){
		repaint();
	}
	
	public void setCurrentNode(boolean state){
		newNode = state ;
	}
	
	public void setNewEdge(boolean state){
		newEdge = state ;
	}

	public void setNewNodeAdded(boolean state){
		newNodeAdded = state ;
	}
	
	public void setNewEdgeAdded(boolean state){
		newEdgeAdded = state ;
	}
	
	public void setNewGraphPressed(boolean state){
		newGraphPressed = state ;
	}
	
	public void setGetGainPressed(boolean state){
		getGainPressed = state ;
	}
	
	public void setWarning(String s){
		JOptionPane.showMessageDialog(getComponentPopupMenu(), s , "warning", JOptionPane.ERROR_MESSAGE, null);
	}
	
	void getView(View v){
		this.view = v ;
	}
	
	public void reset(){
		setCurrentNode(false);
		setNewEdge(false);
		node1 = null;
		node2 = null;
		node1Checked = false ;
		edges.clear();
		outNode.clear();
		nodes.clear();
		numOfNodes = 0 ;
		currentNode = null;
		repaint();
	}
	
	public void paint(Graphics g)
    {
		Graphics2D graphSettings = (Graphics2D)g;
        graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

        graphSettings.setStroke(new BasicStroke(2));
            
        graphSettings.setComposite(AlphaComposite.getInstance(
                 AlphaComposite.SRC_OVER, 1.00f));
        
        graphSettings.drawImage(background, 0, 0,1200-150, 500, null);
        
        
        if(newNode)
        	graphSettings.draw(currentNode.getNode());
       
        for(int i=0 ; i<nodes.size() ; i++){
        	Node n = nodes.get(i);
        	graphSettings.draw(n.getNode());
        	graphSettings.drawString(Integer.toString(n.label), (int)n.node.getCenterX()-3 , (int)n.node.getCenterY()+5 );
        }
        
        
        for(int i=0 ; i<edges.size() ; i++){
        	ArrayList<Integer> listEdge = edges.get(i);
        	ArrayList<Node> listNode = outNode.get(i);
        	
        	if(edges.get(i)!=null){
        		
        		for(int j=0 ; j<listNode.size() ; j++){
        			Node node = listNode.get(j) ;
        			double edge = listEdge.get(j);
            		if(node.label == i ){//self edge
            			graphSettings.draw(new Arc2D.Double( node.node.getCenterX()-15 , 200-65, 30 , 100 , 0, 180, Arc2D.OPEN) );
            			graphSettings.drawString(Double.toString(edge), (int)node.node.getCenterX()-10 , 132);
            		}
            		
            		else if(i> node.label){
            			graphSettings.drawString( Double.toString(edge), Math.abs( (node.x+nodes.get(i).x)  )/2 -17 , 165- Math.abs( (node.x-nodes.get(i).x)  )/2);
            			graphSettings.drawString("<<<",Math.abs( (node.x+nodes.get(i).x)  )/2 -24,(int) 174-Math.abs( (node.x-nodes.get(i).x)  )/2);
            			
        				graphSettings.draw(new Arc2D.Double(Math.min(nodes.get(i).node.getCenterX(),  node.node.getCenterX()), 170-Math.abs( (node.x-nodes.get(i).x)  )/2, Math.abs( node.x-nodes.get(i).x  ) , Math.abs( (node.x-nodes.get(i).x)  ) , 0, 180, Arc2D.OPEN) );
            		}
            		
            		else{
            			boolean hap = false ;
            			Line2D.Double l = new Line2D.Double(Math.min(node.getNode().getCenterX(), nodes.get(i).node.getCenterX())+15 , node.getNode().getCenterY() , Math.max(node.getNode().getCenterX(), nodes.get(i).node.getCenterX())-15 , node.getNode().getCenterY());
            			for(int k=0 ; k<nodes.size() ; k++){
            				if(k!=i && node!=nodes.get(k) && l.intersects(nodes.get(k).getNode().getBounds2D())){
            					hap =  true ;
            					break;
            				}
            			}
            			if(hap){
            				graphSettings.drawString( Double.toString(edge), Math.abs( (node.x+nodes.get(i).x)  )/2 -10 , 215+ Math.abs( (node.x-nodes.get(i).x)  )/2);
            				graphSettings.draw(new Arc2D.Double(Math.min(nodes.get(i).node.getCenterX(),  node.node.getCenterX()), 200-Math.abs( (node.x-nodes.get(i).x)  )/2, Math.abs( node.x-nodes.get(i).x  ) , Math.abs( (node.x-nodes.get(i).x)  ) , 180, 180, Arc2D.OPEN) );
            				
            			}
            				
            			else{
            				
            				graphSettings.draw(l);
            				graphSettings.draw(new Line2D.Double( (int) ( nodes.get(i).node.getCenterX()+node.node.getCenterX() )/2 - 20 , 200-7, (int) ( nodes.get(i).node.getCenterX()+node.node.getCenterX() )/2 -10 , 200-15) );
            				graphSettings.draw(new Line2D.Double( (int) ( nodes.get(i).node.getCenterX()+node.node.getCenterX() )/2 - 20 , 200-23, (int) ( nodes.get(i).node.getCenterX()+node.node.getCenterX() )/2 -10 , 200-15) );
                			graphSettings.drawString( Double.toString(edge), (int) ( nodes.get(i).node.getCenterX()+node.node.getCenterX() )/2  , (int)node.node.getCenterY()-5);
            			}
            				
            			
            		}
            	}
        		
        	}
        }
    }
}