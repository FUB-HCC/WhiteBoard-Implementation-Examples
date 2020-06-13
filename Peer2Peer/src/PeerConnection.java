
public class PeerConnection extends Thread {

    private final Socket socket;
    private final WhiteBoard whiteBoard;
    private BufferedReader in = null;
    private PrintStream out = null;
    /**
     * 
     * @param socket
     * @param whiteBoard
     */
    PeerConnection(final Socket socket, final WhiteBoard whiteBoard) {
        this.socket = socket;
        this.whiteBoard = whiteBoard;
        this.currentShape = null;
    }
}