package tuio2multitouch;
//This package is used to convert MTCon to simple multi-touch vector (i.e. extract the tuiopoint from tuioCursor)

import java.util.Vector;
import processing.core.PApplet;
import processing.core.PFont;
import TUIO.*;

public class TUIO2MultiTouch {
	//Parameters
	static final int MAX_MULTI_TOUCH_NUM = 22;	//This is the maximum number for 3M multi touch screen
	static final int INVALID_COORDINATE = -1;	//This is a constant for point not appeared
	
	private TuioProcessing tuioClient;
	private int width , height;
	
	public MyPoint[] mt;		//An array that store the multi touch coordinate
	public TUIO2MultiTouch(TuioProcessing $tuioHandler, int $width , int $height) {
		//The size is for the tuio device, for example, to receive tuio message from an iPad, its size is 1024x768
		//If the size if given as the screen size, means the tuio coordinates is normalized to the current screen size.
		this.tuioClient = $tuioHandler;
		width = $width;
		height= $height;
		
		mt = new MyPoint[MAX_MULTI_TOUCH_NUM];
		for (int i=0;i<MAX_MULTI_TOUCH_NUM;++i) {
			mt[i] = new MyPoint();
		}
	}
	
	public boolean getCursors() {
		//Save all tuio cursors to mt
		for (int i=0;i<MAX_MULTI_TOUCH_NUM;++i) {
			mt[i].x = mt[i].y = INVALID_COORDINATE;
		}
		
		Vector tuioCursorList = tuioClient.getTuioCursors();
		for (int i = 0; i < tuioCursorList.size(); i++) {
			TuioCursor tcur = (TuioCursor) tuioCursorList.elementAt(i);
			int cursorID = tcur.getCursorID();
			mt[cursorID].x = tcur.getScreenX(width);
			mt[cursorID].y = tcur.getScreenY(height);
		}
		return true;
	}
}
