import javax.swing.*;
import java.awt.*;

public class HangedManPainting extends JPanel {
	//Class Fields
	private boolean[] partsArray;
	private static int partsFilled;
	private boolean manIsFullyHanged;
	//Class Constants
	private final int WIDTH = 280, HEIGHT = 300, PARTS_NUM = 10;

	public HangedManPainting() {
		manIsFullyHanged = false;
		partsFilled = 0;
		partsArray = new boolean[ PARTS_NUM ];
		setPreferredSize( new Dimension( WIDTH , HEIGHT ) );
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents( g ); // cleaning the screen

		//Background painting
		g.setColor( new Color( 74 , 164 , 224 ) );
		g.fillRect( 0 , 0 , getWidth( ) , getHeight( ) );

		//Background painting
		g.setColor( new Color( 45 , 105 , 145 ) );
		g.fillRect( 0 , 277 , getWidth( ) , getHeight( ) );

		//Man and pole painting
		g.setColor( Color.BLACK );
		buildStaticPole( g );
		paintParts( g );
	}

	/*Utility function for painting the body part */
	private void paintParts(Graphics g) {
		g.setColor( Color.WHITE );

		if( partsArray[ 0 ] ) // head
			g.drawOval( 128 , 80 , 30 , 30 );
		if( partsArray[ 1 ] ) // body
			g.drawLine( 143 , 110 , 143 , 200 );
		if( partsArray[ 2 ] ) //right hand
			g.drawLine( 143 , 150 , 190 , 100 );
		if( partsArray[ 3 ] ) // left hand
			g.drawLine( 143 , 150 , 90 , 100 );
		if( partsArray[ 4 ] )// right leg
			g.drawLine( 143 , 200 , 190 , 240 );
		if( partsArray[ 5 ] )// left leg
			g.drawLine( 143 , 200 , 90 , 240 );
		if( partsArray[ 6 ] )// left eye
			g.drawOval( 134 , 90 , 5 , 5 );
		if( partsArray[ 7 ] ) //right eye
			g.drawOval( 146 , 90 , 5 , 5 );
		if( partsArray[ 8 ] )//nose
			g.drawOval( 142 , 95 , 2 , 6 );
		if( partsArray[ 9 ] ) {// mouth
			g.drawOval( 138 , 102 , 10 , 2 );
			manIsFullyHanged = true;
		}
	}

	/*Adds a body part to the hanged man drawing and returns true if the painting is fully finished.*/
	public boolean addPart() {
		partsArray[ partsFilled++ ] = true;
		update( getGraphics( ) );
		return manIsFullyHanged;
	}

	/*Utility function for painting the static pole the man being hanged on*/
	private void buildStaticPole(Graphics g) {
		Graphics2D graphics2D = ( Graphics2D ) g;
		graphics2D.setStroke( new BasicStroke( 3 ) );

		graphics2D.drawLine( 140 , 60 , 140 , 75 ); // small vertical line
		graphics2D.drawLine( 140 , 60 , 210 , 60 ); // upper horizontal line
		graphics2D.drawLine( 210 , 60 , 210 , 260 ); // big vertical line
		graphics2D.drawLine( 160 , 260 , 240 , 260 ); // lower horizontal line
	}
}
