import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class ButtonPanel extends JPanel implements ActionListener {

	public ButtonPanel(){
		setName("button panel");
		setBorder(new TitledBorder(null, "Options",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
//		setBounds(0, 0, 150, 750);
		
		setLayout(new FlowLayout());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}
}