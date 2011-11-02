package mtconjava;

import java.util.Vector;
import processing.core.PApplet;
import processing.core.PFont;
//we need to import the TUIO library
//and declare a TuioProcessing client variable
import TUIO.*;
import tuio2multitouch.*;

public class MTConJava extends PApplet {
	/* TUIO Related */
	private TuioProcessing tuioClient;
	public TUIO2MultiTouch mtDevice;

	// these are some helper variables which are used
	// to create scalable graphical feedback
	float cursor_size = 15;
	float object_size = 60;
	float table_size = 760;
	float scale_factor = 1;
	
	//Visualization related
	LogDump myVis;
	public PFont font;
	
	//Triangle for recognizing the ID
	public IDTag tag;
	
	public int timeStamp = 0;
	public void setup() {
		size(1024,768);
		noStroke();
		fill(0);

		// noLoop();
		frameRate(30);

		hint(ENABLE_NATIVE_FONTS);
		font = createFont("Arial", 18);
		scale_factor = height / table_size;

		//Initialize the Multi-touch device;
		tuioClient = new TuioProcessing(this);
		mtDevice = new TUIO2MultiTouch(tuioClient, 640, 480);
		//Visualization related
		myVis = new LogDump(this);
		//ID Tag
		tag = new IDTag(this);
	}

	private void timeStampTicks() {	//Increase the time stamp and added to visualization
		timeStamp++;
		myVis.lt = "timeStamp : " + timeStamp + "\n" + myVis.lt;
	}
	public void draw() {
		background(0);
		timeStampTicks();
		
		tag.getTriangleMapping();
		
		mtDevice.getCursors();	//Get multi-touch device cursor
		myVis.drawVis();
		myVis.drawTriangle( tag );
		myVis.ld += "\nRight Angle : " + tag.calcRightAngleInDegree() ; 		
	
	}

/*	
	// these callback methods are called whenever a TUIO event occurs
	// Should be disable if not in DEBUG mode. Also for the reason that keep the
	// code simple

	// called when an object is added to the scene
	public void addTuioObject(TuioObject tobj) {
		println("add object " + tobj.getSymbolID() + " (" + tobj.getSessionID()
				+ ") " + tobj.getX() + " " + tobj.getY() + " "
				+ tobj.getAngle());
	}

	// called when an object is removed from the scene
	public void removeTuioObject(TuioObject tobj) {
		println("remove object " + tobj.getSymbolID() + " ("
				+ tobj.getSessionID() + ")");
	}

	// called when an object is moved
	public void updateTuioObject(TuioObject tobj) {
		println("update object " + tobj.getSymbolID() + " ("
				+ tobj.getSessionID() + ") " + tobj.getX() + " " + tobj.getY()
				+ " " + tobj.getAngle() + " " + tobj.getMotionSpeed() + " "
				+ tobj.getRotationSpeed() + " " + tobj.getMotionAccel() + " "
				+ tobj.getRotationAccel());
	}

	// called when a cursor is added to the scene
	public void addTuioCursor(TuioCursor tcur) {
		println("add cursor " + tcur.getCursorID() + " (" + tcur.getSessionID()
				+ ") " + tcur.getX() + " " + tcur.getY());
	}

	// called when a cursor is moved
	public void updateTuioCursor(TuioCursor tcur) {
		println("update cursor " + tcur.getCursorID() + " ("
				+ tcur.getSessionID() + ") " + tcur.getX() + " " + tcur.getY()
				+ " " + tcur.getMotionSpeed() + " " + tcur.getMotionAccel());
	}

	// called when a cursor is removed from the scene
	public void removeTuioCursor(TuioCursor tcur) {
		println("remove cursor " + tcur.getCursorID() + " ("
				+ tcur.getSessionID() + ")");
	}

	// called after each message bundle
	// representing the end of an image frame
	public void refresh(TuioTime bundleTime) {
		redraw();
	}
*/
}
