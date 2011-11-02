package mtconjava;

import tuio2multitouch.MyPoint;

public class TriangleTracePoint extends MyPoint {
	public static int MOVING_THRESHOLD = 4000;
	
	//touchIndex indicated where is the coordinate data come from.
	//a negative value means the current coordinate is "interpolated"
	public int touchIndex;
	

	public TriangleTracePoint() {
		super(-1,-1);
		// TODO Auto-generated constructor stub
	}
	public void copyFrom( MyPoint $rhs, int $index) {
		this.x = $rhs.x;
		this.y = $rhs.y;
		this.touchIndex = $index;
	}
	public void computeValidity(MyPoint $rhs) {
		//This function compare the current (x,y) with $rhs, 
		//to see if it is still in the MOVING_THRESHOLD
		int dxy = this.calcDistance($rhs);
		if (dxy > this.MOVING_THRESHOLD) 
			touchIndex = -1;	//The point probaly not come from original touchIndex
	}
}
