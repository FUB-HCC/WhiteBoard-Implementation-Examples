package rpc;

enum ENUMShape {
    triangle, rectangle, circle
   }

public class Shape {
    public ENUMShape name; 
    public int id;

    /**
     * 
     * @param name
     * @param id
     */
    public Shape(ENUMShape name, int id) {
        this.name=name;
        this.id = id;
    }

    @Override
    public String toString(){
        return String.format("%s id:%d", this.name.toString(), this.id);
    }
}

