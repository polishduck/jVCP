package commConnector;

/**
 * comm port listener interface
 * @author Krzysiek
 */
public interface CommListenerInterface {
	/**
	 * message received notification
	 * @param message message in array
	 * @param len message length
	 */
    public void messageReceived(byte[] message);
    
    /**
     * port in use error notification
     */
    public void portAlreadyInUse();
    
    /**
     * write to port error notification
     */
    public void writingError();
    
    /**
     * read from port error notification
     */
    public void readingError();
}
