class FailedToConvergeException extends RuntimeException {
  public FailedToConvergeException() { super( "failed to converge" ); }
  public FailedToConvergeException( String message ) { super( message ); }
}
