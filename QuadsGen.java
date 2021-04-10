import gen.Tiny_lang_siiBaseListener;
import gen.Tiny_lang_siiParser;

import java.util.ArrayList;
import java.util.LinkedList;


public class QuadsGen extends Tiny_lang_siiBaseListener {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";


    Quads quads = new Quads();
    AssembStatments codeOb = new AssembStatments();

    TableDeSymbols ts;
    LinkedList<String> stack = new LinkedList<>();
    int cpt;
    ArrayList<String> errors = new ArrayList<>();

    public QuadsGen(TableDeSymbols ts){this.ts=ts;}


    @Override public void enterS(Tiny_lang_siiParser.SContext ctx) {
        quads.addQuad(new Quadruplet("START","OF","QUADRUPLET",""));
        codeOb.addAssemb(new Assembleur(ANSI_BLUE+"CODE"+ANSI_RESET,ANSI_BLUE+"OBJET"+ANSI_RESET,ANSI_BLUE+"STARTS"+ANSI_RESET));
    }

    @Override public void exitS(Tiny_lang_siiParser.SContext ctx) {
        quads.addQuad(new Quadruplet("END","OF","QUADRUPLET",""));
        codeOb.addAssemb(new Assembleur(ANSI_BLUE+"CODE"+ANSI_RESET,ANSI_BLUE+"OBJET"+ANSI_RESET,ANSI_BLUE+"ENDS"+ANSI_RESET));
    }


    @Override public void exitAffectation(Tiny_lang_siiParser.AffectationContext ctx) {

        if(ts.containsRow(ctx.ID().getText())){
            if(errors.size()>0){
                if(!errors.get(errors.size()-1).contains("0 DIVISION AT LINE")){
                    try{
                        String tmp = stack.removeLast();

                        String val = ts.getRow(tmp).value;
                        String type = ts.getRow(tmp).type;
                        String typeId = ts.getRow(ctx.ID().getText()).type;
                        if(type.compareTo(typeId)!=0){
                            errors.add("TYPE MISMATCH : "+typeId+" "+ctx.ID().getText()+" WITH "+type);
                        }else{
                            ts.getRow(ctx.ID().getText()).value=val;

                        }
                        quads.addQuad(new Quadruplet("=",tmp,"",ctx.ID().getText()));
                        codeOb.addAssemb(new Assembleur("MOV","AX",tmp));
                        codeOb.addAssemb(new Assembleur("MOV",ctx.ID().getText(),"AX"));
                    }catch(Exception e){

                    }


                }
            }else{
                String tmp = stack.removeLast();

                String val = ts.getRow(tmp).value;
                String type = ts.getRow(tmp).type;
                String typeId = ts.getRow(ctx.ID().getText()).type;
                if(type.compareTo(typeId)!=0){
                    errors.add("TYPE MISMATCH : "+typeId+" "+ctx.ID().getText()+" WITH "+type);
                }else{
                    ts.getRow(ctx.ID().getText()).value=val;

                }
                quads.addQuad(new Quadruplet("=",tmp,"",ctx.ID().getText()));
                codeOb.addAssemb(new Assembleur("MOV", "AX",tmp));
                codeOb.addAssemb(new Assembleur("MOV",ctx.ID().getText(),"AX"));
            }


        }else{
            errors.add("IDENTIFIER NOT DECLARED : "+ctx.ID().getText()+ " AT LINE "+ctx.getStart().getLine());
        }
    }

    @Override public void exitExpression(Tiny_lang_siiParser.ExpressionContext ctx) {
        if(ctx.expression()!=null){
            String t1 = stack.removeLast();
            String t2 = stack.removeLast();
            String type1="",type2="";
            int iVal1=0,iVal2=0;
            float fVal1=0,fVal2=0;
            String amds = ctx.amds().getText();
            if(isNumeric(t1)){
                try{
                    iVal1 = Integer.valueOf(t1);
                    type1 = "int";

                }catch(Exception e){
                    try{
                        fVal1 = Float.valueOf(t1);
                        type1 = "float";

                    }catch (Exception fe){
                        e.printStackTrace();
                    }

                }
            }else{
                if(ts.containsRow(t1)){
                    type1 =  ts.getRow(t1).type;
                    if(type1.compareTo("int")==0){
                        iVal1=Integer.valueOf(ts.getRow(t1).value);
                    }else{
                        fVal1=Float.valueOf(ts.getRow(t1).value);
                    }


                }
            }

            if(isNumeric(t2)){
                try{
                    iVal2 = Integer.valueOf(t2);
                    type2 = "int";

                }catch(Exception e){
                    try{
                        fVal2 = Float.valueOf(t2);
                        type2 = "float";

                    }catch (Exception fe){
                        e.printStackTrace();
                    }

                }
            }else{
                if(ts.containsRow(t2)){
                    type2 =  ts.getRow(t2).type;
                    if(type2.compareTo("int")==0){
                        iVal2=Integer.valueOf(ts.getRow(t2).value);
                    }else{
                        fVal2=Float.valueOf(ts.getRow(t2).value);
                    }


                }
            }

            if(type1.compareTo(type2)==0){

                    if(amds.compareTo("/")==0 && t1.compareTo("0")==0){
                        errors.add("0 DIVISION OF "+t2);
                    }else{
                        String temp = "Temp"+(++cpt);
                        int resI = 0;
                        float resF = 0;
                        if (type1.compareTo("int")==0){
                            if(amds.compareTo("+")==0){
                                resI=iVal2+iVal1;
                            } else if(amds.compareTo("-")==0){
                                resI=iVal2-iVal1;
                            }else if(amds.compareTo("*")==0){
                                resI=iVal2*iVal1;
                            }else{
                                resI = Integer.valueOf(iVal2/iVal1);
                            }
                            ts.addRow(new TableDeSymbols.TabRow(temp,-1,type1,""+resI));

                        }
                        if (type1.compareTo("float")==0){
                            if(amds.compareTo("+")==0){
                                resF=fVal1+fVal2;
                            } else if(amds.compareTo("-")==0){
                                resF=fVal1-fVal2;
                            }else if(amds.compareTo("*")==0){
                                resF=fVal1*fVal2;
                            }else{
                                resF =fVal2/fVal1;
                            }
                            ts.addRow(new TableDeSymbols.TabRow(temp,-1,type1,""+resF));
                        }

                        quads.addQuad(new Quadruplet(ctx.amds().getText(),t2,t1,temp));
                        codeOb.addAssemb(new Assembleur("MOV","AX",t2));
                        switch (ctx.amds().getText()){
                            case "+" : codeOb.addAssemb(new Assembleur("ADD",t1,""));break;
                            case "-" : codeOb.addAssemb(new Assembleur("SUB",t1,""));break;
                            case "*" : codeOb.addAssemb(new Assembleur("MUL",t1,""));break;
                            case "/" : codeOb.addAssemb(new Assembleur("DIV",t1,""));break;
                        }
                        stack.push(temp);
                    }

            }else{
                errors.add("TYPE MISMATCH : "+t1+", WITH : "+t2);
            }



        }
    }
    @Override public void exitExpression1(Tiny_lang_siiParser.Expression1Context ctx) {
        if (ctx.value()!=null){
            stack.add(ctx.value().getText());
        }

    }

    int saveCond,saveJmp;String jmp;
    @Override public void exitCond(Tiny_lang_siiParser.CondContext ctx) {
        String t1 = stack.removeLast();
        String t2 = stack.removeLast();
        String sie = ctx.sie().getText();
        String brache = "";
        switch (sie){
            // i assigned the opposite SIE cz if the codition is wrong
            // then we banch otherwise keep executing
            case ">" : brache = "BLE";jmp="JLE";break;
            case ">=" : brache = "BL";jmp="JL";break;
            case "<" : brache = "BGE";jmp="JGE"; break;
            case "<=" : brache = "BG";jmp="JG"; break;
            case "==" : brache = "BNE";jmp="JNE"; break;
            case "!=" : brache = "BE";jmp="JE"; break;
        }
        quads.addQuad(new Quadruplet(brache,"",t2,t1));
        codeOb.addAssemb(new Assembleur("MOV","AX",t2));
        codeOb.addAssemb(new Assembleur("CMP", "AX",t1));
        saveJmp = codeOb.size();
        codeOb.addAssemb(new Assembleur(jmp,"",""));
        saveCond = quads.size()-1;
    }
    @Override public void enterIfcond(Tiny_lang_siiParser.IfcondContext ctx) {
        quads.addQuad(new Quadruplet("","START","IF",""));
    }

    @Override public void enterEl(Tiny_lang_siiParser.ElContext ctx) {
        quads.getQuad(saveCond).set(1,""+quads.size());
        codeOb.getAssemb(saveJmp).set(1,ANSI_BLUE+"Etiq"+saveCond+ANSI_RESET);
        codeOb.addAssemb(new Assembleur(ANSI_BLUE+"ETIQ"+saveCond+ANSI_RESET,ANSI_PURPLE+"ENTER"+ANSI_RESET,ANSI_PURPLE+"ELSE"+ANSI_RESET));
    }
    @Override public void exitEl(Tiny_lang_siiParser.ElContext ctx) {
        codeOb.addAssemb(new Assembleur("",ANSI_PURPLE+"EXIT"+ANSI_RESET,ANSI_PURPLE+"ELSE"+ANSI_RESET));
    }

    @Override public void exitIfcond(Tiny_lang_siiParser.IfcondContext ctx) {
        if(ctx.el()==null){
            quads.getQuad(saveCond).set(1,""+quads.size());
            codeOb.getAssemb(saveJmp).set(1,ANSI_BLUE+"Etiq"+saveCond+ANSI_RESET);
            codeOb.addAssemb(new Assembleur(ANSI_BLUE+"ETIQ"+saveCond+ANSI_RESET,ANSI_PURPLE+"Exit"+ANSI_RESET,ANSI_PURPLE+"IF"+ANSI_RESET));
        }
        quads.addQuad(new Quadruplet("","END","IF",""));
    }


    int saveWhile;
    @Override public void enterDoWhile_inst(Tiny_lang_siiParser.DoWhile_instContext ctx) {
        quads.addQuad(new Quadruplet("","START","WHILE",""));
        saveWhile = quads.size();
        codeOb.addAssemb(new Assembleur(ANSI_BLUE+"ETIQ"+saveWhile+ANSI_RESET,ANSI_PURPLE+"Enter"+ANSI_RESET,ANSI_PURPLE+"WHILE"+ANSI_RESET));
    }


    @Override public void exitDoWhile_inst(Tiny_lang_siiParser.DoWhile_instContext ctx) {
        String sie = ctx.cond().sie().getText();
        String brache = "";
        switch (sie){
            case ">" : brache = "BG";jmp="JG"; break;
            case ">=" : brache = "BGE";jmp="JGE"; break;
            case "<" : brache = "BL";jmp="JL"; break;
            case "<=" : brache = "BLE";jmp="JLE"; break;
            case "==" : brache = "BE";jmp="JE"; break;
            case "!=" : brache = "BNE";jmp="JNE"; break;
        }
        quads.getQuad(saveCond).set(0,brache);
        quads.getQuad(saveCond).set(1,""+saveWhile);
        codeOb.getAssemb(saveJmp).set(1,ANSI_BLUE+"Etiq"+saveWhile+ANSI_RESET);
        codeOb.addAssemb(new Assembleur("",ANSI_PURPLE+"EXIT"+ANSI_RESET,ANSI_PURPLE+"WHILE"+ANSI_RESET));
        quads.addQuad(new Quadruplet("","END","WHILE",""));
    }



    public void printQuads(){

        System.out.println("\n");
        System.out.println(ANSI_BLUE+"-------------------------------"+ANSI_RESET);
        System.out.println(ANSI_BLUE+"Quadruplet : "+ANSI_RESET);
//        System.out.println(quads.size());
//        System.out.println(stack);
        for (int i = 0; i < quads.size(); i++) {
            if(quads.getQuad(i).toString().contains("END") || quads.getQuad(i).toString().contains("START")){
                System.out.println(ANSI_YELLOW + i+" - "+quads.getQuad(i)+ANSI_RESET);
            }else{
                System.out.println( i+" - "+quads.getQuad(i));
            }
        }
        System.out.println(ANSI_BLUE+"-------------------------------"+ANSI_RESET);
    }

    public void printObjets(){

        System.out.println("\n");
        System.out.println(ANSI_BLUE+"-------------------------------"+ANSI_RESET);
        System.out.println(ANSI_BLUE+"CODE OBJET : "+ANSI_RESET);
//        System.out.println(quads.size());
//        System.out.println(stack);
        for (int i = 0; i < codeOb.size(); i++) {
                System.out.println( i+" - "+codeOb.getAssemb(i));
        }
        System.out.println(ANSI_BLUE+"-------------------------------"+ANSI_RESET);
    }

    public void printErrors(){

        System.out.println(ANSI_RED+"------------------------------------------------------------------"+ANSI_RESET);
        if(errors.size()>0){
            System.out.println(ANSI_RED+"QUADRUPLES ERRORS : "+ANSI_RESET);
            for (int i = 0 ; i<errors.size();i++){
                System.out.println(errors.get(i));
            }
        }else{
            System.out.println(ANSI_GREEN+"NO QUADRUPLES ERRORS DETECTED"+ANSI_RESET);
        }
        System.out.println(ANSI_RED+"------------------------------------------------------------------"+ANSI_RESET);

    }


    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public ArrayList<Quadruplet> getQuads(){
        return quads.getAll();
    }

}
