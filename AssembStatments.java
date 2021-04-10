import java.util.ArrayList;

public class AssembStatments {
    ArrayList<Assembleur> assembs = new ArrayList<>();

    public void addAssemb(Assembleur a){
        assembs.add(a);
    }
    public Assembleur getAssemb(int index){
        return assembs.get(index);
    }

    public int size(){
        return assembs.size();
    }
}
