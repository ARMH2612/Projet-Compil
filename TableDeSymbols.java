import java.util.ArrayList;

public class TableDeSymbols {
    static public class TabRow{
        String name,type,value;
        int declared;
        public TabRow(String name, int declared, String type, String value){
            this.name = name;
            this.declared = declared;
            this.type = type;
            this.value = value;
        }

        public String toString(){
            String decl = "";
            if(declared==1){decl="declared";}else{decl="not declared";}
            return "name : "+name+" | Type : "+type+" | Value : "+value+" | Declared : "+decl;
        }
    }


    public ArrayList<TabRow> table = new ArrayList<TabRow>();


    public TabRow getRow(String name){
        if(!table.isEmpty()){
            for(int i=0; i< table.size(); i++){
                if(table.get(i).name.compareTo(name)==0){
                    return table.get(i);
                }
            }
        }

        return null;
    }
    public TabRow getRow(int i){
        return table.get(i);
    }

    public void addRow(TabRow tabRow){
        table.add(tabRow);
    }


    public boolean containsRow(String name){
        return getRow(name) != null;
    }

    public int getSize(){
        return table.size();
    }
}
