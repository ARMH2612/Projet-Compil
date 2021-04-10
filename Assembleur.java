public class Assembleur {
    String vals[];
    public Assembleur(String s1,String s2,String s3){
        vals = new String[3];
        vals[0] = s1;
        vals[1] = s2;
        vals[2] = s3;
    }

    public void set(int index, String s){
        vals[index] = s;
    }
    public String toString(){
        return ""+vals[0]+" , "+vals[1]+" , "+vals[2];
    }
}
