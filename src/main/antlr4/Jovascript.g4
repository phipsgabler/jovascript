grammar Jovascript;

// LITERALS
NUMBER_LITERAL : [0-9]+ ;

BOOL_LITERAL : 'true' | 'false' ;

fragment STRING_TOKEN : ~["\\] | '\\' [btnfr"'\\] ;
STRING_LITERAL : '"' STRING_TOKEN* '"' ;


// OPERATORS
OFTYPE : ':' ;
SUBTYPE : '<:' ;
FORALL : 'forall' | '∀' ;
TYPE_LAMBDA : '/\\' | 'Λ' ;
FUNCTION_ARROW : '->' ;

LAMBDA_ARROW : '=>' ;
LAMBDA : '\\' | 'λ' ;

REL_OP : '<' | '>' | '<=' | '>=' | '==' | '!=' ;
MUL_OP : '*' | '/' | '%' ;
OR_OP : '||' ;
AND_OP : '&&' ;
SIGN_OP : '+' | '-' ;
NOT_OP : '!' ; //| '~' ;
//BITSHIFT_OP : '>>' | '<<' | '>>>' ;
//BITAND_OP : '&'

WILDCARD : '_';
LET : 'let' ;


// IDENTIFIERS
IDENTIFIER : [a-z] [a-zA-Z0-9_]* ;
TYPENAME : [A-Z] [a-zA-Z0-9_]* ;

WS : [ \t\r\n]+ -> skip ;

// see: https://github.com/antlr/grammars-v4/blob/master/java/Java.g4#L1014
COMMENT : '/*' .*? '*/' -> skip ;
LINE_COMMENT :'//' ~[\r\n]* -> skip ;

//////////////////////////////////////////////////////////////////////////////////////////////////

program : definition* EOF;

typeExpression : TYPENAME                                                             # TypeName
               | '(' typeExpression ')'                                               # ParenthesizedTypeExpression
//               |<assoc=right> FORALL TYPENAME '.' typeExpression                      # PolymorphicType
//               |<assoc=right> TYPE_LAMBDA TYPENAME '.' typeExpression                 # LambdaType
               |<assoc=right> dom=typeExpression FUNCTION_ARROW cod=typeExpression    # FunctionType
               ;

definition : valueDefinition ;
        // | typeDefinition

valueDefinition : LET ident=IDENTIFIER OFTYPE typ=typeExpression '=' expr=expression ';' ;
             // | inferredDefinition

expression : NUMBER_LITERAL                                                   # NumberLiteral
           | STRING_LITERAL                                                   # StringLiteral
           | BOOL_LITERAL                                                     # BoolLiteral
           | name=IDENTIFIER                                                  # IdentifierLiteral
       //  | '{' '}'                                                          # ObjectLiteral
           | '(' expression ')'                                               # ParenthesizedExpression
           | expression '.' IDENTIFIER                                        # MemberAccess
       //  | expression '[' expression ']'                                    # IndexAccess
           | expression '(' argumentList ')'                                  # FunctionCall
           | (NOT_OP | SIGN_OP) expression                                    # UnaryOperatorCall
           | lhs=expression MUL_OP rhs=expression                             # MultOperatorCall
           | lhs=expression SIGN_OP rhs=expression                            # SumOperatorCall
     //    | lhs=expression BITSHIFT_OP rhs=expression                             # BitOperatorCall
           | lhs=expression REL_OP rhs=expression                             # RelationOperatorCall
           | lhs=expression AND_OP rhs=expression                             # AndOperatorCall
           | lhs=expression OR_OP rhs=expression                              # OrOperatorCall
           | LAMBDA parameter=lambdaParameters LAMBDA_ARROW expr=expression   # LambdaExpression
        // | 'if' expression 'then' expression 'else' expression              # IfExpression
        // | objectLiteral
           ;

argumentList : first=expression ( ',' rest=expression )* ;

lambdaParameters : lambdaArgument             # LambdaParam
                 | '(' lambdaArgument ')'     # ParenthesizedLambdaParam
                 ;

lambdaArgument : ident=( IDENTIFIER | WILDCARD ) OFTYPE typ=typeExpression ;