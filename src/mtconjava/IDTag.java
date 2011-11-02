package mtconjava;

import tuio2multitouch.MyPoint;
import tuio2multitouch.TUIO2MultiTouch;
//This class consist of all tagging related functions.

public class IDTag {
	
	private MTConJava parent;
	private TUIO2MultiTouch mtDevice;
	
	public TriangleTracePoint[] triangle;
	public int ID;
	public int triNum;
	//The condition that (ID>=0 || triNum>=3) means we got an triangle
	
	
	public IDTag(MTConJava $parent) {
		parent = $parent;
		mtDevice = parent.mtDevice;
		ID = -150;		//Below zero means un-tagged
		triangle = new TriangleTracePoint[3];
		for (int i=0;i<3;++i)
			triangle[i] = new TriangleTracePoint();
	}
	
	public boolean getTriangleMapping() {
		//This function will 
		// 1) get the first three points as triangle if not yet tagged
		// 2) compute next triangle position ( or interpolate it ) if tagged
		boolean tagged = (ID>=0) ;
		
		if (!tagged) {
			if ( mtDevice.numOfTouches < 3) return false;	//Less than 3 points. impossible to perform initial triangle registration
			triNum = 0;
			for (int i=0; i < mtDevice.mt.length  ; ++i ) {
				if ( !mtDevice.mt[i].visible() ) continue;
				triangle[ triNum ].copyFrom( mtDevice.mt[i] , i );
				triNum++;
				if (triNum==3) {
					//ID++;
					break;
				}
			}
			//Do triangle normalization (should be done when first initial registration)
			triangleNormalization();
			parent.myVis.ld += "\nNot yet tagged!";
			
		}
		else {
			int dxy;
			int dxyMin;			//Keep the minimal distance
			int dxyMinIndex;	//Keep the minimal distance index
			//First , recompute all the touchIndex.
			//We check this first to give priority to the exist mapping relation
			for (int i=0;i<3;++i) {
				if (triangle[i].touchIndex < 0) continue;	//It is already in "interpolating" status
				dxy = triangle[i].calcDistance( mtDevice.mt[ triangle[i].touchIndex ] );
				if (dxy > TriangleTracePoint.MOVING_THRESHOLD) { 
					triangle[i].touchIndex = -1;	//The point probaly not come from original touchIndex
				}
				else {	//Copy the newest position
					triangle[i].copyFrom( mtDevice.mt[ triangle[i].touchIndex ], triangle[i].touchIndex);
				}
			}
			
			
			//Then for every triangle that doesn't have a valid touchIndex..
			//	find "preemptive(i.e: not over-lapping with other triangle point" , "minimal distance" touchIndex
			for (int i=0;i<3;++i) {
				if (triangle[i].touchIndex >=0 ) continue;
				
				dxyMin = TriangleTracePoint.MOVING_THRESHOLD;
				dxyMinIndex = -1;
				for (int j=0;j<mtDevice.mt.length ;++j) {
					//Check if this touch point is valid
					if (!mtDevice.mt[j].visible()) continue;

					//check if this touch point is preemptive
					if (j==triangle[0].touchIndex || j==triangle[1].touchIndex || j==triangle[2].touchIndex ) 
						continue;
					
					//Calculate the minimum points.
					dxy = triangle[i].calcDistance(mtDevice.mt[j]);
					if (dxy < dxyMin) {
						dxyMin = dxy;
						dxyMinIndex = j;
					}
					
				}
				//If we find such point 
				if (dxyMinIndex >=0 ) {
					triangle[ i ].copyFrom( mtDevice.mt[ dxyMinIndex ], dxyMinIndex );
				}
			}
				
		}
		return true;
	}

	//=======================================================================================================================================================================================================================================//
	public float calcRightAngleInDegree() {
		if (ID<0 && triNum<3) return -1.0f;	//impossible to calculate right angle
		return calcRightAngle()*180/ parent.PI;
	}
	public float calcRightAngle() {
		if (ID<0 && triNum<3) return -1.0f;	//impossible to calculate right angle
		
	    MyPoint tmpV1 = new MyPoint( triangle[1].x - triangle[0].x , triangle[1].y - triangle[0].y );
	    MyPoint tmpV2 = new MyPoint( triangle[2].x - triangle[0].x , triangle[2].y - triangle[0].y );
       
		float numerator = tmpV1.x*tmpV2.x + tmpV1.y*tmpV2.y ;
		float denominator = ( parent.sqrt(tmpV1.x*tmpV1.x + tmpV1.y*tmpV1.y)* parent.sqrt(tmpV2.x*tmpV2.x + tmpV2.y*tmpV2.y) );
		float dotValue = numerator / denominator;
		return parent.acos( dotValue ) ;
		
	}
	public void triangleNormalization() {
		//This function should be called when "first" registration
		if (ID>=0 || triNum<3) return ;
		//Find the longest edge (i.e : find the index 0 )
		int dxyMax = 0;
		int dxy;
		int p0=0,p1=1,p2=2;
		for (int i=0;i<3;++i) {
			for (int j=i+1;j<3;++j) {
				dxy = triangle[i].calcDistance( triangle[j] );
				if (dxyMax < dxy) {
					p0 = 3-i-j;
					p1 = i;
					p2 = j;
					dxyMax = dxy;
				}
			}
		}
//		parent.myVis.ld += "Longest pair :" + p1 + "," + p2 + "\n" ;
		triangle[0].swapWith( triangle[p0] );

		//Decide whether we have to swap the order of p1 and p2		
	    MyPoint tmpV1 = new MyPoint( triangle[1].x - triangle[0].x , triangle[1].y - triangle[0].y );
	    MyPoint tmpV2 = new MyPoint( triangle[2].x - triangle[0].x , triangle[2].y - triangle[0].y );
	    int crossDotResult = tmpV1.x *tmpV2.y - tmpV1.y*tmpV2.x;
		parent.myVis.ld += "Cross result :" + crossDotResult;
		if (crossDotResult < 0)
			triangle[1].swapWith(triangle[2]);
	}
}
