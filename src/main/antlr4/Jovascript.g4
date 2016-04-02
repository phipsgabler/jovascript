grammar Jovascript;


INT_LITERAL : [0-9]+ ;

IDENTIFIER : [a-z] [a-zA-Z0-9_]* ;

TYPENAME : [A-Z] [a-zA-Z0-9_]* ;

WS :   [ \t\r\n]+ -> skip ;



//////////////////////////////////////////////////////////////////////////////////////////////////

program : (definition | statement)* EOF;

type : TYPENAME;

definition : 'def' IDENTIFIER ':' type '=' expression ';' ;

statement : expression ';' ;

expression : INT_LITERAL ;
