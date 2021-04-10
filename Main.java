import gen.Tiny_lang_siiLexer;
import gen.Tiny_lang_siiParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {
    public static void main(String[] args){
        Tiny_lang_siiLexer lexer = new Tiny_lang_siiLexer(CharStreams.fromString(
                "Compil Prog (){\n" +
                "IntCompil l,m;\n" +
                "StringCompil a,s;\n" +
                "FloatCompil f1,f2;\n" +
                "start\n" +
                "m=9+9;\n" +
                        "do {" +
                        "   l = l-1;" +
                        "}while(l>10);" +
                "l=m-2*8;" +
                        "if(l<9) then{\n" +
                        "    m=m+1;" +
                        "do{" +
                        "m=m-1;" +
                        "}while(m>0);\n" +
                        "}" +
                        "else{" +
                        "m=m+f1;" +
                        "m=m/0;" +
                        "b=m+1;" +
                        "}" +
                        "}"));
        Tiny_lang_siiParser parser = new Tiny_lang_siiParser(new CommonTokenStream(lexer));

        TabSGen tabSGen = new TabSGen();
        QuadsGen quadsGen = new QuadsGen(tabSGen.ts);

        parser.addParseListener(tabSGen);
        parser.addParseListener(quadsGen);

        parser.s();


        tabSGen.printErrors();
        tabSGen.printTableDeSymbole();
        quadsGen.printErrors();
        quadsGen.printQuads();
        quadsGen.printObjets();

    }
}
