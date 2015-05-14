package com.amap.apis.locationservice;  

public final class GDLocationStatusCodes {

	  public static final int SUCCESS = 0;
	  public static final int ERROR = 1;
 
	  
	  public static int getStatusCode(int paramInt)
	  {
	    if (((0 <= paramInt) && (paramInt <= 1))) {
	      return paramInt;
	    }
	    return 1;
	  }
}
  
