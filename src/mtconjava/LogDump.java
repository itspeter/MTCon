package mtconjava;

import processing.core.PApplet;
import tuio2multitouch.TUIO2MultiTouch;

//This class is intended to display all the information on the screen.
//It will display all the multiple touch point and message zone on 4 different position 

public class LogDump {
	private MTConJava parent;

	public String lt, ld;	//Debug string at left-top , left-bottom when drawVis() is called 

	public LogDump(MTConJava $parent) {
		parent = $parent;
		lt = "";
		ld = "";
	}

	public void drawMultiTouchPoints() {
		TUIO2MultiTouch mtDevice = parent.mtDevice;

		for (int i = 0; i < mtDevice.MAX_MULTI_TOUCH_NUM; ++i) {
			if (!mtDevice.mt[i].visible())
				continue;
			parent.noStroke();
			//parent.stroke(50, 200, 200, 80);
			parent.fill(50, 200, 200, 80);
			parent.ellipse(mtDevice.mt[i].x, mtDevice.mt[i].y, 30, 30);
			
			parent.textFont(parent.font, 14);
			parent.fill(200, 80);
			parent.text(
					"[" + i + "]" +
			"(" + mtDevice.mt[i].x + "," + mtDevice.mt[i].y + ")",
					mtDevice.mt[i].x - i * 10,
					mtDevice.mt[i].y + i * 10);
		}
	}

	public void drawTriangle ( IDTag $tag ) {
		if ($tag.triNum < 3 && $tag.ID < 0 ) return ;	//No triangle can be visualized
		
		for (int i = 0; i < 3 ; ++i) {
			if (! $tag.triangle[i].visible()) 
				continue;
			
			if ($tag.triangle[i].touchIndex < 0 ) {	
				//It is interpolated point, we use stroke without fill
				parent.noFill();
				parent.stroke( 0 ,255 ,0, 80 );
			}
			else {
				//It is from touch point, we use fill without stroke
				parent.noStroke();
				parent.fill( 0, 255, 0 , 80 );
			}
			
			parent.ellipse($tag.triangle[i].x, $tag.triangle[i].y, 10, 10);

			parent.noStroke();
			parent.fill( 255, 244 );
	
			parent.text( "T" + i , $tag.triangle[i].x , $tag.triangle[i].y );

		}
		//Draw the longest edge
		parent.stroke( 0 , 255 , 0 , 80 );
		parent.line($tag.triangle[1].x , $tag.triangle[1].y , $tag.triangle[2].x , $tag.triangle[2].y );
	}
	public void drawVis() {
		drawMultiTouchPoints();
		
		// Draw Texts
		parent.noStroke();
		
		parent.fill(200, 150);
		parent.textFont(parent.font, 14);
		// Draw left-top DebugString
		parent.text(lt, 50, 20);
		// Draw left-bottom DebugString
		parent.text(ld, 50, 600);

		//Clean the string
		lt = ld = "";
	}

}
