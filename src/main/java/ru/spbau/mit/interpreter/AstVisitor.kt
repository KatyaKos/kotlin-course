package ru.spbau.mit.interpreter

interface AstVisitor {
    fun visit(file: File): Int?
    fun visit(block: Block): Int?
    fun visit(blockWithBraces: BlockWithBraces): Int?
    fun visit(function: Function): Int?
    fun visit(printLn: PrintLn): Int?
    fun visit(variable: Variable): Int?
    fun visit(functionCallExpression: FunctionCallExpression): Int
    fun visit(identifierExpression: IdentifierExpression): Int
    fun visit(literalExpression: LiteralExpression): Int
    fun visit(binaryExpression: BinaryExpression): Int
    fun visit(unaryMinusExpression: UnaryMinusExpression): Int
    fun visit(expressionWithBraces: ExpressionWithBraces): Int
    fun visit(whileStatement: WhileStatement): Int?
    fun visit(ifStatement: IfStatement): Int?
    fun visit(assignment: Assignment): Int?
    fun visit(returnStatement: ReturnStatement): Int?
}