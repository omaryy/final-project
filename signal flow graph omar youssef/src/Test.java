import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;


public class Test extends JFrame{
	
	public static void main(String[] args){
		new Test();
	}

	public Test(){
		this.setTitle("Signal Flow Graph");
		this.setSize(1200, 750);
		this.setLayout(new FlowLayout());
		
		
		JFileChooser f = new JFileChooser();
		JTextPane t = new JTextPane();
		t.setText("mosab mohamed");
		
		JLayer<JButton> k = new JLayer<JButton>();
		
		JButton b = new JButton("mosan");
		b.setBounds(200,200,100,100);
		
		
		this.add(f);
		this.add(b);
		this.add(t);

		
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}