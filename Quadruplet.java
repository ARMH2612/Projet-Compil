public class Quadruplet {
    String vals[];
    public Quadruplet(String[] quad){
        this.vals = quad;
    }

    public Quadruplet(String s1,String s2,String s3,String s4){
        vals = new String[4];
        vals[0] = s1;vals[1] = s2;vals[2] = s3;vals[3] = s4;
    }
    public String get(int index){
        return vals[index];
    }
    public void set(int index, String s){
        vals[index] = s;
    }
    public String toString(){
        return "("+vals[0]+" , "+vals[1]+" , "+vals[2]+" , "+vals[3]+")";
    }
}
