/*
 * FailedToConvergeException.java
 * Copyright (c) 2009 Marcus A. Maloof.  All Rights Reserved.  See LICENSE.
 */

class FailedToConvergeException extends RuntimeException {
  public FailedToConvergeException() { super( "failed to converge" ); }
  public FailedToConvergeException( String message ) { super( message ); }
}
