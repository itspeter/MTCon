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
	TuioProcessing tuioClient;
	TUIO2MultiTouch mtDevice;

	// these are some helper variables which are used
	// to create scalable graphical feedback
	float cursor_size = 15;
	float object_size = 60;
	float table_size = 760;
	float scale_factor = 1;
	PFont font;

	public void setup() {
		// size(screen.width,screen.height);
		size(640, 480);
		noStroke();
		fill(0);

		loop();
		frameRate(30);
		// noLoop();

		hint(ENABLE_NATIVE_FONTS);
		font = createFont("Arial", 18);
		scale_factor = height / table_size;

		// we create an instance of the TuioProcessing client
		// since we add "this" class as an argument the TuioProcessing class
		// expects
		// an implementation of the TUIO callback methods (see below)
		tuioClient = new TuioProcessing(this);
		mtDevice = new TUIO2MultiTouch(tuioClient, 640, 480);

	}

	public void draw() {
		background(255);
		textFont(font, 18 * scale_factor);
		float obj_size = object_size * scale_factor;
		float cur_size = cursor_size * scale_factor;

		mtDevice.getCursors();
		for (int i = 0; i < 22; ++i) {
			if (!mtDevice.mt[i].visible()) continue;
			stroke(192, 192, 192);
			fill(192, 192, 192);
			ellipse(mtDevice.mt[i].x, mtDevice.mt[i].y, cur_size, cur_size);
			fill(0);
			text("" + i, mtDevice.mt[i].x - 5, mtDevice.mt[i].y + 5);
		}

	}

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
}
