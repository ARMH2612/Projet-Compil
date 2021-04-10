import gen.Tiny_lang_siiBaseListener;
import gen.Tiny_lang_siiParser;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class TabSGen extends Tiny_lang_siiBaseListener {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";


    TableDeSymbols ts = new TableDeSymbols();
    ArrayList<String> errors = new ArrayList<>();
    boolean undefined = false ;

    String type;

    @Override
    public void exitType(Tiny_lang_siiParser.TypeContext ctx){

        switch (ctx.getText()){
            case "IntCompil" : type="int";break;
            case "FloatCompil" : type="float";break;
            case "StringCompil" : type="String";break;
            default: undefined =true;
        }

//        if(ctx.getText().equals("IntCompil")){type="int";}
//        if(ctx.getText().equals("FloatCompil")){type="float";}
//        if(ctx.getText().equals("StringCompil")){type="String";}
    }



    @Override
    public void exitDecalration(Tiny_lang_siiParser.DecalrationContext ctx) {
        Tiny_lang_siiParser.SuitDecContext vars = ctx.suitDec();
        String name = "";
        if (!undefined){
            if (vars.suitDec()!=null){
                for (int i =0 ; i< vars.getChildCount();i++){
                    name = vars.getChild(i).getText();
                    if (name.compareTo(",")!= 0){
                        if (!ts.containsRow(name)){
                            if(type.compareTo("String")==0){
                                ts.addRow(new TableDeSymbols.TabRow(name,1,type,null));
                            }else{
                                ts.addRow(new TableDeSymbols.TabRow(name,1,type,""+0));
                            }
                        }else{
                            errors.add("DOUBLE DECLARATION OF : "+name);
                        }
                    }
                }
            }else{
                name = vars.getChild(0).getText();
                if (!ts.containsRow(name)){
                    ts.addRow(new TableDeSymbols.TabRow(name,1,type,null));
                }else{
                    errors.add("DOUBLE DECLARATION OF : "+name);
                }
            }
        }else{
            StringBuilder sb = new StringBuilder("UNDEFINED TYPE OF : ");
            if (vars.suitDec()!=null){
                for (int i =0 ; i< vars.getChildCount();i++) {
                    name = vars.getChild(i).getText();
                    if (name.compareTo(",") != 0) {
                        sb.append(name);
                    }
                }
            }else{
                sb.append(vars.getChild(0).getText());
            }
            errors.add(sb.toString());
        }




//        for (;;vars=vars.suitDec()){
//            String name = vars.getChild(0).getText();
//            if(!ts.containsRow(name)){
//                ts.addRow(new TableDeSymbols.TabRow(name,1,type,null));
//            }else{
//
//                System.out.println(ts.containsRow(name));
//                System.out.println("DOUBLE DECLARATION DE LA VARIABLE : "+name);
//            }
//        }
//        for (int i = 0; i< vars.toString().length();i++){
//
//        }
//
//        // check if the variable exists :
//        if(ts.getRow(ctx.ID().getText()) == null){
//            // add a new row cz the variable doesn't exist :
//            ts.addRow(new TableDeSymbols.TabRow(ctx.ID().getText(),1,type,null));
//        }else{
//            System.out.println("DOUBLE DECLARATION DE LA VARIABLE : "+ctx.ID().getText());
//        }
    }





//    @Override
//    public void exitContinueDec(Tiny_lang_siiParser.ContinueDecContext ctx) {
//        // check if the variable exists :
//            System.out.println(ts.getSize());
//        Tiny_lang_siiParser.
//       // System.out.println(ctx.children);
//           // ts.addRow(new TableDeSymbols.TabRow(ctx.ID().getText(),1,type,null));
//            if(!ts.containsRow(ctx.ID().getText())){
//                // add a new row cz the variable doesn't exist :
//                ts.addRow(new TableDeSymbols.TabRow(ctx.ID().getText(),1,type,null));
//            }else{
//                System.out.println("DOUBLE DECLARATION DE LA VARIABLE : "+ctx.ID().getText());
//            }
//    }



    public void printTableDeSymbole(){

        System.out.println("------------------------------------------------------------------");
        System.out.println("***********************************");
        System.out.println("*********Table des symboles********");
        System.out.println("***********************************");
        for (int i = 0 ; i< ts.getSize() ; i++){
            System.out.println(ts.getRow(i));
        }

        System.out.println("------------------------------------------------------------------");

    }

    public void printErrors(){
        System.out.println(ANSI_RED+"------------------------------------------------------------------"+ANSI_RESET);
        if(errors.size()>0){
            System.out.println(ANSI_RED+"DECLARATION ERRORS : "+ANSI_RESET);
            for (int i = 0 ; i<errors.size();i++){
                System.out.println(ANSI_RED+errors.get(i)+ANSI_RESET);
            }
        }else{
            System.out.println(ANSI_GREEN+"NO TABLE DE SYMBOL ERRORS DETECTED"+ANSI_RESET);
        }
        System.out.println(ANSI_RED+"------------------------------------------------------------------"+ANSI_RESET);
    }


}
