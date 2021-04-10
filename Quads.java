import java.util.ArrayList;

public class Quads {
    ArrayList<Quadruplet> quads = new ArrayList<>();

    public void addQuad(Quadruplet q){
        quads.add(q);
    }
    public Quadruplet getQuad(int index){
        return quads.get(index);
    }
    public void setQuad(int index,String value,int position){
        Quadruplet q = quads.get(index);
        q.set(position,value);
        quads.set(index,q);
    }
    public int size(){
        return quads.size();
    }


    public ArrayList<Quadruplet> getAll() {
        return quads;
    }
}
