package rpc;

enum ENUMShape {
    triangle, rectangle, circle
   }

/**
* InnerShape
*/
public class Shape {
    public ENUMShape name; 
    public int id;

    public Shape(ENUMShape name, int id) {
        this.name=name;
        this.id = id;
    }

    @Override
    public String toString(){
        return String.format("Shape %s %d", this.name.toString(), this.id);
    }
}

