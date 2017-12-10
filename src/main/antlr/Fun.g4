grammar Fun;

file
    : block EOF
    ;

block
    : (statement)*
    ;

blockWithBraces
    : '{' block '}'
    ;

statement
    : function
    | printLn
    | variable
    | expression
    | whileStatement
    | ifStatement
    | assignment
    | returnStatement
;

function
    : 'fun' name=Identifier '(' parameters ')' blockWithBraces
    ;

printLn
    : name='println' '(' arguments ')'
    ;

variable
    : 'var' name=Identifier ('=' expression)?
    ;

expression
    : name=Identifier '(' arguments ')' #functionCallExpression
    | Identifier #identifierExpression
    | Literal #literalExpression
    | left = expression operation = ('+' | '-' | '*' | '/' | '%') right = expression #binaryExpression
    | left = expression operation = ('>' | '<' | '>=' | '<=') right = expression #binaryExpression
    | left = expression operation = ('==' | '!=') right = expression #binaryExpression
    | left = expression operation = ('&&' | '||') right = expression #binaryExpression
    | '-' expression #unaryMinusExpression
    | '(' expression ')' #expressionWithBraces
    ;

whileStatement
    : 'while' '(' cond=expression ')' body=blockWithBraces
    ;

ifStatement
    : 'if' '(' cond=expression ')' bodyTrue=blockWithBraces ('else' bodyFalse=blockWithBraces)?
    ;

assignment
    : name=Identifier '=' expression
    ;

returnStatement
    : 'return' expression
    ;

parameters
    : (Identifier (',' Identifier)*)?
    ;

arguments
    : (expression (',' expression)*)?
    ;

Identifier
    : ([a-zA-Z_]) ([a-zA-Z_] | [0-9])*
    ;

Literal
    :   '0'
    |   '-'? [1-9] [0-9]*
    ;

Comment
    : '//' ~[\r\n]* -> skip
    ;

Emptiness
    : [ \t\r\n] -> skip
    ;