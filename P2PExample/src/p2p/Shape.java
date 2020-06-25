package p2p;

import java.io.Serializable;

enum ShapeType {
    triangle, rectangle, circle
   }
   
/**
 * implements Serializable for referencing across peers
 */
public class Shape implements Serializable {
    private static final long serialVersionUID = 1L;

    public ShapeType type; 
    public int id;

    /**
     * 
     * @param name  enum name of ENUMShape
     * @param id    unique id
     */
    public Shape(ShapeType type, int id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public String toString(){
        return String.format("%s id:%d", this.type.toString(), this.id);
    }
}

