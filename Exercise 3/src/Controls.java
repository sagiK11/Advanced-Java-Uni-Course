import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controls extends JPanel {
	//Class Fields
	private HangedManGame hangedManGame;
	private JEditorPane txtInputLetter;

	/*Constructor*/
	public Controls(HangedManGame game) {
		this.hangedManGame = game;
		JLabel lblLetter = new JLabel( "Letter: " );
		JButton cmdEnter = new JButton( "Enter" );
		cmdEnter.addActionListener( new Listener( ) );

		txtInputLetter = new JEditorPane( );
		txtInputLetter.setEditable( true );
		txtInputLetter.setPreferredSize( new Dimension( 50 , 20 ) );
		txtInputLetter.setMaximumSize( new Dimension( 50 , 20 ) );

		add( lblLetter );
		add( txtInputLetter );
		add( cmdEnter );


	}

	/*Listener for the enter letter button*/
	private class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String letter = txtInputLetter.getText( ).trim( );
			txtInputLetter.setText( "" );

			hangedManGame.enterClicked( letter );
		}
	}

}
