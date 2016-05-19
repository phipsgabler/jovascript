grammar Jovascript;


NUMBER_LITERAL : ( '+' | '-' )* [0-9]+ ;

IDENTIFIER : [a-z] [a-zA-Z0-9_]* ;

TYPENAME : [A-Z] [a-zA-Z0-9_]* ;

WS :   [ \t\r\n]+ -> skip ;

OPERATOR : [+-] ;



//////////////////////////////////////////////////////////////////////////////////////////////////

program : definition* EOF;

typeExpression : TYPENAME                                           # TypeName
               | '(' typeExpression ')'                             # ParenthesizedTypeExpression
               |<assoc=right> typeExpression '->' typeExpression    # FunctionType
               ;

definition : valueDefinition ;
        // | typeDefinition

valueDefinition : 'def' ident=IDENTIFIER ':' typ=typeExpression '=' expr=expression ';' ;

expression : NUMBER_LITERAL                                         # NumberLiteral
           | IDENTIFIER                                             # IdentifierExpression
           | '(' expression ')'                                     # ParenthesizedExpression
           | expression OPERATOR expression                         # OperatorExpression
           | '\\' parameter=lambdaParameters '=>' expr=expression   # Lambda
        // | objectLiteral
           ;

lambdaParameters : lambdaArgument             # LambdaParam
                 | '(' lambdaArgument ')'     # ParenthesizedLambdaParam
                 ;

lambdaArgument : ident=( IDENTIFIER | '_' ) ':' typ=typeExpression ;