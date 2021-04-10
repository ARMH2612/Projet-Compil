grammar Tiny_lang_sii;

// parser rules :
s : COMPIL NOMPROG PAR_OV PAR_FR
      ACC_OV
        listdeclaration
        START
        instruction
      ACC_FR
    ;


type : INTCOMPIL | FLOATCOMPIL | STRINGCOMPIL ;
decalration : type suitDec SEM_VIRG;
suitDec :  ID VIRG suitDec | ID ;
listdeclaration : decalration listdeclaration |  ;


instruction : affectation instruction
            | ifcond instruction
            | input instruction
            | output instruction
            | doWhile_inst instruction
            |
            ;


affectation : ID AFFECT expression SEM_VIRG;
expression :  expression amds expression1
            | expression1
            ;
expression1: value | PAR_OV expression PAR_FR;
value : INT | FLOAT | ID;
amds : ADD | MULT | DIV | SUB;

ifcond : IF PAR_OV cond PAR_FR THEN ACC_OV instruction ACC_FR
        |IF PAR_OV cond PAR_FR THEN ACC_OV instruction ACC_FR el
        ;
el : ELSE ACC_OV instruction ACC_FR;

cond : expression sie expression;
sie : INF | SUP | EQUAL | INFE | SUPE | NOQUAL;

doWhile_inst : DO ACC_OV instruction ACC_FR WHILE PAR_OV cond PAR_FR SEM_VIRG;



input : SCANCOMPIL PAR_OV GUILLUME adr GUILLUME VIRG ID PAR_FR SEM_VIRG ;
adr : ADR_INT | ADR_FLOAT | ADR_STRING;
output : PRINTCOMPIL PAR_OV STRING PAR_FR SEM_VIRG
        |PRINTCOMPIL PAR_OV ID PAR_FR SEM_VIRG
        ;


// Lexer rules :
COMPIL : 'Compil';
START : 'start';
INTCOMPIL : 'IntCompil';
FLOATCOMPIL : 'FloatCompil';
STRINGCOMPIL : 'StringCompil';
IF : 'if';
THEN : 'then';
ELSE : 'else';
DO : 'do';
WHILE : 'while';
SCANCOMPIL : 'scanCompil';
PRINTCOMPIL : 'printCompil';

AFFECT : '=';
ADD : '+';
SUB : '-';
MULT : '*';
DIV : '/';
INF : '<';
SUP : '>';
INFE : '<=';
SUPE : '>=';
EQUAL : '==';
NOQUAL : '!=';
ADR_INT : '%d';
ADR_FLOAT : '%f';
ADR_STRING : '%s';

PAR_OV : '(';
PAR_FR : ')';
GUILLUME : '"';
ACC_OV : '{';
ACC_FR : '}';
VIRG : ',';
SEM_VIRG : ';';




WHITESPACE : [ \n\t]->skip;
ID : [a-z] ([a-zA-Z]|[0-9])*;
NOMPROG : [A-Z] ([a-zA-Z]|[0-9])*;
INT : [0-9]+;
FLOAT : [0-9]+'.'[0-9]+;
STRING : '"' ([a-zA-Z|[0-9])* '"';
CHAIN : ([a-zA-Z]|[0-9])+;