import java.awt.Color;
import java.awt.PageAttributes.OrientationRequestedType;

import javax.print.attribute.standard.OrientationRequested;
import javax.swing.*;


public class Log extends JTextArea{

	JLabel jLabel1 ;
	JScrollPane jScrollPane1 ;
	
	public Log(int width , int height){
		setName("log panel");
		setBackground(Color.white);
		
		setColumns(width);
        setLineWrap(true);
        setRows(height);
        setWrapStyleWord(true);
        
//        JScrollPane scroll = new JScrollPane ( this );
//        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        
       this.setEditable(false); // set textArea non-editable
       jScrollPane1 = new JScrollPane(this);
       jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

       //Add Textarea in to middle panel
//       middlePanel.add(scroll);
       
       
		
	}
}