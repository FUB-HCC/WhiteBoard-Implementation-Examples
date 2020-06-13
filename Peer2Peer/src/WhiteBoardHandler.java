package rmiclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rmiinterface.*;

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
    static String goodbyMessage = "goodby :)";

    private final WhiteBoardInterface whiteBoard;
    private BufferedReader in = null;
    private Shape currentShape;
    /**
     * the shared remote object which mimics the behavior of a locally available object 
     * @param whiteBoard 
     */
    WhiteBoardHandler(final WhiteBoardInterface whiteBoard) {
        this.whiteBoard = whiteBoard;
        this.currentShape = null;
    }
    /**
     * Upon the start all System.in input is handled here
     */
    public void startInputHandler() {
        System.out.println(welcomeMessage);
        try {
            this.in = new BufferedReader(new InputStreamReader(System.in));
            String messageIn = "";
            while (!messageIn.equals("stop")) {
                messageIn = this.in.readLine();
                handleCommand(messageIn);
            }
            this.in.close();
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
                System.out.println(createShapeInfo);
                String shapeName = this.in.readLine().strip().toLowerCase();
                try {
                    ENUMShape shapeType = ENUMShape.valueOf(shapeName);
                    this.currentShape = whiteBoard.createShape(shapeType);
                    System.out.println(String.format("Created %s, now you can \"put\" the Shape onto the Whiteboard." , currentShape.toString()));
                } catch (final IllegalArgumentException ex) {
                    System.out.println(errorMessageShape);
                }
                break;
            case "put":
                if (this.currentShape != null) {
                    this.whiteBoard.placeShape(this.currentShape);
                    this.currentShape = null;
                    System.out.println(this.whiteBoard.whiteBoardToString());
                } else {
                    System.out.println(errorMessagePut);
                }
                break;
            case "delete":
                System.out.println(deleteInfo + this.whiteBoard.whiteBoardToString());
                try {
                    command = this.in.readLine();
                    final int shapeId = Integer.parseInt(command);
                    final boolean removed = this.whiteBoard.removeShape(shapeId);
                    System.out.println(String.format("%s removed Shape %d: ", getStatusMessage(removed), shapeId)
                            + this.whiteBoard.whiteBoardToString());
                } catch (final NumberFormatException e) {
                    System.out.println(errorMessageDelete);
                }
                break;
            case "get":
                System.out.println(this.whiteBoard.whiteBoardToString());
                break;
            case "stop":
                System.out.println(goodbyMessage);
                break;
            case "help":
                System.out.println(helpInfo);
                break;
            default:
                System.out.println(defaultMessage);
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