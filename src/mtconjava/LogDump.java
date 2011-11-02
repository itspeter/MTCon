package mtconjava;

import processing.core.PApplet;
import tuio2multitouch.TUIO2MultiTouch;

//This class is intended to display all the information on the screen.
//It will display all the multiple touch point and message zone on 4 different position 

public class LogDump {
	private MTConJava parent;
	
	private String lt,ld;
	

	public LogDump( MTConJava $parent) {
		parent = $parent;
	}
	
	public void drawMultiTouchPoints() {
		TUIO2MultiTouch mtDevice = parent.mtDevice;
		
		for (int i = 0; i < mtDevice.MAX_MULTI_TOUCH_NUM; ++i) {
			if (!mtDevice.mt[i].visible()) continue;
			parent.stroke(192, 192, 192);
			parent.fill(192, 192, 192);
			parent.ellipse(mtDevice.mt[i].x, mtDevice.mt[i].y, 10 , 10 );
			parent.fill(0);
			parent.text("" + i, mtDevice.mt[i].x - 5, mtDevice.mt[i].y + 5);
		}

		
	}
	public void drawVis() {
		drawMultiTouchPoints();
		lt = ld = "";
	}

}
