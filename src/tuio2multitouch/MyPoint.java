package tuio2multitouch;

public class MyPoint {
	//This class is created to spoil the OO data structure.
	//Providing direct access to x and y for convenience
	public int x,y;
	
	public boolean visible() {
		if (x == TUIO2MultiTouch.INVALID_COORDINATE && 
			y == TUIO2MultiTouch.INVALID_COORDINATE)	
			return false;
		else
			return true;
	}
	public MyPoint () {
		x = y = 0;		
	}
	public MyPoint(int $x,int $y) {
		x = $x;
		y = $y;
	}
	public MyPoint(MyPoint $rhs) {
		x = $rhs.x;
		y = $rhs.y;
	}
	
	public int calcDistance(MyPoint $rhs) {
		//Probably overflow
		return (x-$rhs.x)*(x-$rhs.x) + (y-$rhs.y)*(y-$rhs.y);
	}
}
