import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class View extends JFrame implements ActionListener{
	
	private JButton newNode , evaluateGain , newGraph , newEdge ;
	private Canvas canvas;
	private JPanel buttonsPanel , panel1 , panel2, panel3 , panel4;
	DrawBoard drawPanel ;
	Log log , loops , paths ; 
	private Border raisedbevel , loweredbevel , compound , blackline;
	ArrayList<Node> nodes ; 
	boolean newNodeIsPressed = false ;
	
	public ArrayList<int[]> f_paths;
	public ArrayList<int[]> f_loops  ;
	
	public int[] forward_gains;
	public int[] loop_gains   ;
	
	private ArrayList<Integer> two_non;   
	private ArrayList<Integer> three_non; 
	private ArrayList<Integer> four_non;
	
	public View(){
		
		this.setTitle("Signal Flow Graph");
		this.setSize(1200, 750);
		this.setLayout(null);
		
		makeBorders();
		nodes = new ArrayList<Node>();
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		panel1.setBounds(150, 500, 540, 750-500);
		panel1.setBorder ( new TitledBorder ( new EtchedBorder (), "Log" ) );
		panel2.setBounds(690, 500, 250, 750-500);
		panel2.setBorder ( new TitledBorder ( new EtchedBorder (), "Paths" ) );
		panel3.setBounds(940, 500, 250, 750-500);
		panel3.setBorder ( new TitledBorder ( new EtchedBorder (), "Loops" ) );
		panel4.setBounds(0, 0, 150, 750);
		panel4.setBorder ( new TitledBorder ( new EtchedBorder (), "BUTTONS" ) );
		
		
		buttonsPanel = new ButtonPanel();
		drawPanel = new DrawBoard();
		log = new Log(40,10);
		loops = new Log(18,10);
		paths = new Log(18,10);
		panel1.add(log.jScrollPane1);
		panel2.add(paths.jScrollPane1);
		panel3.add(loops.jScrollPane1);
		panel4.setLayout(new FlowLayout());
//		panel4.add(buttonsPanel);
		
//		panel4.setBorder(  BorderFactory.createCompoundBorder(blackline, compound) );
		drawPanel.setBorder(  BorderFactory.createCompoundBorder(blackline, compound) );
		
//		this.add(buttonsPanel);
		this.add(drawPanel);
		this.add(panel1);
		this.add(panel2);
		this.add(panel3);
		add(panel4);
		
		newNode = new JButton("Add Node");
		newNode.setContentAreaFilled(false);
		newGraph = new JButton("new Graph");
		newGraph.setContentAreaFilled(false);
		evaluateGain = new JButton("get gain");
		evaluateGain.setContentAreaFilled(false);
		newEdge = new JButton("new edge");
		newEdge.setContentAreaFilled(false);
		
		drawPanel.getView(this);
		
		newNode.addActionListener(this);
		newGraph.addActionListener(this);
		evaluateGain.addActionListener(this);
		newEdge.addActionListener(this);
		
		newNode.setBounds(10, 10, 130, 50);
		newGraph.setBounds(10, 430 , 130, 200);
		evaluateGain.setBounds(10, 220, 130, 200);
		
		panel4.add(newNode);
		panel4.add(newEdge);
		panel4.add(newGraph);
		panel4.add(evaluateGain);
	
		
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	void makeBorders(){
		raisedbevel = BorderFactory.createRaisedBevelBorder();
		loweredbevel = BorderFactory.createLoweredBevelBorder();
		blackline = BorderFactory.createLineBorder(Color.black);
		compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
	}

	public void actionPerformed(ActionEvent e) {
		String pressed = e.getActionCommand();
		if(pressed=="Add Node"){
			drawPanel.setCurrentNode(true);
			drawPanel.setNewEdge(false);
			drawPanel.node1 = null;
			drawPanel.node2 = null;
			drawPanel.node1Checked = false ;
		}
		
		else if(pressed=="new Graph"){
			int f = JOptionPane.showConfirmDialog(drawPanel.getComponentPopupMenu(), "do you realy want to get new graph ??\nby clicking yes current graph will be deleted");
			if(f==0){
				drawPanel.reset();
				log.append("new graph has been drawn\n");
			}
			
		}
		
		else if(pressed=="get gain"){
			if(drawPanel.outNode.isEmpty()){
				log.append("no thing to do !!\n");
			}
			
			else{
				Transfere_Fun tf = new Transfere_Fun(drawPanel.outNode, drawPanel.edges);
				double solution = tf.solve();
				log.append("the all over gain = "+Double.toString(solution) +"\n");
				f_paths = tf.get_forward_paths() ;
				f_loops = tf.get_loops();
				two_non =  tf.two_nont ;  
				three_non = tf.three_nont ; 
				four_non = tf.four_nont ;
				
				paths.append(printPaths());
				loops.append(printLoops());
				loops.append(printTwoNon());
				loops.append(printThreeNon());
				loops.append(printFourNon());
				
				
			}
		}
		
		else if(pressed=="new edge"){
			drawPanel.setCurrentNode(false);
			drawPanel.setNewEdge(true);
			drawPanel.node1 = null;
			drawPanel.node2 = null ;
			drawPanel.node1Checked = false ;
		}
		
		
	}
	
	String printLoops(){
		String ret = "individual loops:\n";
		for(int i=0 ; i<f_loops.size() ; i++){
			int[] a = f_loops.get(i);
			ret += "L" + Integer.toString(i+1) + ": " ;
			for(int j=0 ; j<a.length ; j++){
				if(j!=a.length-1)
					ret += Integer.toString( a[j]) + " -> " ;
				else
					ret += Integer.toString( a[j]) ;
			}
			ret += "\n";
		}
		ret += '\n';
		return ret ;
	}
	
	String printPaths(){
		String ret = "forward paths:\n";
		for(int i=0 ; i<f_paths.size() ; i++){
			int[] a = f_paths.get(i);
			ret += "P" + Integer.toString(i+1) + ": " ;
			for(int j=0 ; j<a.length ; j++){
				if(j!=a.length-1)
					ret += Integer.toString( a[j]) + " -> " ;
				else
					ret += Integer.toString( a[j]) ;
			}
			ret += "\n";
		}
		ret += '\n';
		return ret ;
	}
	
	String printTwoNon(){
		String ret = "Two non touched loops:\n";
		int k = 1;
		for(int i=0 ; i<two_non.size() ; i = i+2){
			
			ret += Integer.toString(k) + ") " ;
			ret += "L" + Integer.toString(i+1) + " and " + "L" + Integer.toString(i+2);
			ret += "\n";
			k++;
		}
		ret += '\n';
		return ret ;
	}
	
	String printFourNon(){
		String ret = "Four non touched loops:\n";
		int k = 1;
		for(int i=0 ; i<four_non.size() ; i = i+4){
			
			ret += Integer.toString(k) + ") " ;
			ret += "L" + Integer.toString(i+1) + ", " + "L" + Integer.toString(i+2);
			ret += ", L" + Integer.toString(i+3) + " and " + "L" + Integer.toString(i+4);
			ret += "\n";
			k++;
		}
		ret += '\n';
		return ret ;
	}
	
	String printThreeNon(){
		String ret = "Three non touched loops:\n";
		int k = 1 ;
		for(int i=0 ; i<four_non.size() ; i = i+3){
			
			ret += Integer.toString(k) + ") " ;
			ret += "L" + Integer.toString(i+1) + ", L" + Integer.toString(i+2);
			ret += " and L" + Integer.toString(i+3) ;
			ret += "\n";
			k++ ;
		}
		ret += '\n';
		return ret ;
	}

}