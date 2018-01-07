package ru.spbau.mit.interpreter

interface AstVisitor {
    suspend fun visit(file: File): Int?
    suspend fun visit(block: Block): Int?
    suspend fun visit(blockWithBraces: BlockWithBraces): Int?
    suspend fun visit(function: Function): Int?
    suspend fun visit(printLn: PrintLn): Int?
    suspend fun visit(variable: Variable): Int?
    suspend fun visit(functionCallExpression: FunctionCallExpression): Int
    suspend fun visit(identifierExpression: IdentifierExpression): Int
    suspend fun visit(literalExpression: LiteralExpression): Int
    suspend fun visit(binaryExpression: BinaryExpression): Int
    suspend fun visit(unaryMinusExpression: UnaryMinusExpression): Int
    suspend fun visit(expressionWithBraces: ExpressionWithBraces): Int
    suspend fun visit(whileStatement: WhileStatement): Int?
    suspend fun visit(ifStatement: IfStatement): Int?
    suspend fun visit(assignment: Assignment): Int?
    suspend fun visit(returnStatement: ReturnStatement): Int?
}