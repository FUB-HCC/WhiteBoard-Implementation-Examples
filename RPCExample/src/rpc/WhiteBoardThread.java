package rpc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class WhiteBoardThread extends Thread {
    private Socket socket;
    private WhiteBoard whiteBoard;  
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String createShapeInfo = String.format("Choose one type of Shape by sending one of: %s, %s, %s", ENUMShape.circle, ENUMShape.triangle, ENUMShape.rectangle);
    private String whatYouCanDoInfo = "There are 4 services: create, put, delete and get";
    private Shape currentShape = null;
    WhiteBoardThread(Socket s, WhiteBoard wb){
        this.socket = s;
        this.whiteBoard = wb; 
        
    }
    public void run() {
        try{
            this.in= new DataInputStream(this.socket.getInputStream());
            this.out =new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e){
            e.printStackTrace();
        }
        String messageIn = "";
        while (!messageIn.equals("stop")) {
            try{
                messageIn = this.in.readUTF(); 
                System.out.println(messageIn);
                handleCommand(messageIn); 
                out.flush();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        try{
            in.close();
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handleCommand(String command) throws IOException {

        switch (command.strip().toLowerCase()) {
            case "create":
                this.out.writeUTF(createShapeInfo);
                String shapeName = this.in.readUTF().strip();
                try{
                    ENUMShape shapeType = ENUMShape.valueOf(shapeName);
                    this.currentShape = whiteBoard.createShape(shapeType);
                    this.out.writeUTF(this.currentShape.toString());
                } catch (IllegalArgumentException ex) {  
                    this.out.writeUTF("this shape is not available,"+ createShapeInfo);
                }                
                break;
            case "put":
                if (this.currentShape!=null){
                    this.whiteBoard.placeShape(this.currentShape); 
                    this.currentShape = null;
                    this.out.writeUTF(this.whiteBoard.toString());
                } else {
                    this.out.writeUTF("there is no shape to put onto the whiteboard, create one first...");
                }
                break;
            case "delete":
                this.out.writeUTF("select the shape you want to delete by id: "+this.whiteBoard.toString());
                try{
                    command = this.in.readUTF(); 
                    int shapeId = Integer.parseInt(command);
                    boolean removed  = this.whiteBoard.removeShape(shapeId);
                    this.out.writeUTF(String.format("removed %d %b: ", shapeId, removed) + this.whiteBoard.toString());
                } catch (NumberFormatException e) {
                    this.out.writeUTF(String.format("id %s does not exist! try again", command));
                }
                break;
            case "get":
                this.out.writeUTF(this.whiteBoard.toString());
                break;
            default:
                out.writeUTF(this.whatYouCanDoInfo);
                break;
        }
    }
}