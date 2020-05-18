package rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class WhiteBoardHandler {

    static String useHelpMessage = "For more information on how to use this service type: \"help\"";
    static String helpInfo = String.format("There are four services: \"create\", \"put\", \"delete\" and \"get\", please select one. To quit the connection type: \"stop\". When a Shape is created, use \"put\" to place the Shape onto the WhiteBoard");
    static String createShapeInfo = String.format("Choose a Shape by selecting one of: %s, %s, %s.", ENUMShape.circle, ENUMShape.triangle, ENUMShape.rectangle);
    static String errorMessageShape = String.format("This type of Shape is not available, maybe you can change that? %s", createShapeInfo);
    static String errorMessagePut = String.format("There is no Shape to \"put\" onto the whiteboard, \"create\" one first...");
    static String deleteInfo = String.format("Select the Shape you want to delete by ID: ");
    static String successMessage = String.format("Successfully");
    static String failureMessage = String.format("Failure in");
    static String errorMessageDelete = String.format("Please type in a number for id");
    static String defaultMessage = String.format("Typo? or %s", useHelpMessage);
    static String welcomeMessage = String.format("Welcome to this Whiteboard service! %s",useHelpMessage);
    static String goodbyMessage = "goodby";

    private final Socket socket;
    private final WhiteBoard whiteBoard;
    private BufferedReader in = null;
    private PrintStream out = null;
    private Shape currentShape;
    /**
     * 
     * @param socket
     * @param whiteBoard
     */
    WhiteBoardHandler(final Socket socket, final WhiteBoard whiteBoard) {
        this.socket = socket;
        this.whiteBoard = whiteBoard;
        this.currentShape = null;
    }
    /**
     * Upon the start all communication is handled here
     */
    public void startCommunicationHandler() {
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintStream(this.socket.getOutputStream());
            this.out.println(welcomeMessage);
            String messageIn = "";
            while (!messageIn.equals("stop")) {
                messageIn = this.in.readLine();
                handleCommand(messageIn);
                out.flush();
            }
            this.in.close();
            this.out.close();
            this.socket.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 
     * @param command
     * @throws IOException
     */
    public void handleCommand(String command) throws IOException {
        switch (command.strip().toLowerCase()) {
            case "create":
                this.out.println(createShapeInfo);
                final String shapeName = this.in.readLine().strip().toLowerCase();
                try {
                    final ENUMShape shapeType = ENUMShape.valueOf(shapeName);
                    this.currentShape = whiteBoard.createShape(shapeType);
                    this.out.println(String.format("Created %s, now you can \"put\" the Shape onto the Whiteboard." , currentShape.toString()));
                } catch (final IllegalArgumentException ex) {
                    this.out.println(errorMessageShape);
                }
                break;
            case "put":
                if (this.currentShape != null) {
                    this.whiteBoard.placeShape(this.currentShape);
                    this.currentShape = null;
                    this.out.println(this.whiteBoard.toString());
                } else {
                    this.out.println(errorMessagePut);
                }
                break;
            case "delete":
                this.out.println(deleteInfo + this.whiteBoard.toString());
                try {
                    command = this.in.readLine();
                    final int shapeId = Integer.parseInt(command);
                    final boolean removed = this.whiteBoard.removeShape(shapeId);
                    this.out.println(String.format("%s removed Shape %d: ", getStatusMessage(removed), shapeId)
                            + this.whiteBoard.toString());
                } catch (final NumberFormatException e) {
                    this.out.println(errorMessageDelete);
                }
                break;
            case "get":
                this.out.println(this.whiteBoard.toString());
                break;
            case "stop":
                this.out.println(goodbyMessage);
                break;
            case "help":
                this.out.println(helpInfo);
                break;
            default:
                out.println(defaultMessage);
        }
    }
    /**
     * returns the corresponding Message if an operation was successful or not
     * @param status
     * @return message
     */
    public String getStatusMessage(boolean status) {
        if (status){
            return successMessage;
        } else {
            return failureMessage;
        }
    }
}