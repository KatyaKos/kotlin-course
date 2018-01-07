package ru.spbau.mit.interpreter

import ru.spbau.mit.exceptions.ParsingException

abstract class Ast {

    abstract val line: Int

    abstract suspend fun accept(visitor: AstVisitor): Int?
}

data class File(val body: Block, override val line: Int): Ast() {
    override suspend fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class Block(val statements: List<Statement>, override val line: Int): Ast() {
    override suspend fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class BlockWithBraces(val block: Block, override val line: Int): Ast() {
    override suspend fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

abstract class Statement: Ast()


data class Function(
        val name : String,
        val parameters: List<String>,
        val body: BlockWithBraces,
        override val line: Int
): Statement() {
    override suspend fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class PrintLn(val arguments: List<Expression>, override val line: Int): Statement() {
    override suspend fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class Variable(val name: String, val expression: Expression?, override val line: Int): Statement() {
    override suspend fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

abstract class Expression: Statement() {
    override abstract suspend fun accept(visitor: AstVisitor): Int
}

data class WhileStatement(val condition: Expression, val body: BlockWithBraces, override val line: Int): Statement() {
    override suspend fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class IfStatement(
        val condition: Expression,
        val bodyTrue: BlockWithBraces,
        val bodyFalse: BlockWithBraces?,
        override val line: Int
): Statement() {
    override suspend fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }

}

data class Assignment(val name: String, val expression: Expression, override val line: Int): Statement() {
    override suspend fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class ReturnStatement(val expression: Expression, override val line: Int): Statement() {
    override suspend fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class FunctionCallExpression(
        val name: String,
        val arguments: List<Expression>,
        override val line: Int
): Expression() {
    override suspend fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }

    fun runtimeCheck(parameters: List<String>) {
        if (arguments.size != parameters.size) {
            throw ParsingException("Number of arguments in $name is different from number of parameters.")
        }
    }
}

data class IdentifierExpression(val name: String, override val line: Int): Expression() {
    override suspend fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }
}


data class LiteralExpression(val value: Int, override val line: Int): Expression() {
    override suspend fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }
}

data class BinaryExpression(
        val leftExpr: Expression,
        val operation: String,
        val rightExpr: Expression,
        override val line: Int
): Expression() {
    override suspend fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }
}

data class UnaryMinusExpression(val expression: Expression, override val line: Int): Expression() {
    override suspend fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }
}

data class ExpressionWithBraces(val expression: Expression, override val line: Int): Expression() {
    override suspend fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }
}