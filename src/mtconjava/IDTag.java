package mtconjava;

import tuio2multitouch.TUIO2MultiTouch;
//This class consist of all tagging related functions.

public class IDTag {
	
	private MTConJava parent;
	private TUIO2MultiTouch mtDevice;
	
	public TriangleTracePoint[] triangle;
	public int ID;
	
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
			int triNum = 0;
			for (int i=0; i < mtDevice.mt.length  ; ++i ) {
				if ( !mtDevice.mt[i].visible() ) continue;
				triangle[ triNum ].copyFrom( mtDevice.mt[i] , i );
				triNum++;
				if (triNum ==3) {
					ID++;
					break;
				}
			}
			//Do triangle normalization (should be done when first initial registration)
//			triangleNormalize()
			
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
}
