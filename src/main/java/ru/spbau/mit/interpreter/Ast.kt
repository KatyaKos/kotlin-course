package ru.spbau.mit.interpreter

import ru.spbau.mit.exceptions.ParsingException

abstract class Ast {
    abstract fun accept(visitor: AstVisitor): Int?
}

data class File(val body: Block): Ast() {
    override fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class Block(val statements: List<Statement>): Ast() {
    override fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class BlockWithBraces(val block: Block): Ast() {
    override fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

abstract class Statement: Ast()


data class Function(val name : String, val parameters: List<String>, val body: BlockWithBraces): Statement() {
    override fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class PrintLn(val arguments: List<Expression>): Statement() {
    override fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class Variable(val name: String, val expression: Expression?): Statement() {
    override fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

abstract class Expression: Statement() {
    override abstract fun accept(visitor: AstVisitor): Int
}

data class WhileStatement(val condition: Expression, val body: BlockWithBraces): Statement() {
    override fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class IfStatement(
        val condition: Expression,
        val bodyTrue: BlockWithBraces,
        val bodyFalse: BlockWithBraces?
): Statement() {
    override fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }

}

data class Assignment(val name: String, val expression: Expression): Statement() {
    override fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class ReturnStatement(val expression: Expression): Statement() {
    override fun accept(visitor: AstVisitor): Int? {
        return visitor.visit(this)
    }
}

data class FunctionCallExpression(val name: String, val arguments: List<Expression>): Expression() {
    override fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }

    fun runtimeCheck(parameters: List<String>) {
        if (arguments.size != parameters.size) {
            throw ParsingException("Number of arguments in $name is different from number of parameters.")
        }
    }
}

data class IdentifierExpression(val name: String): Expression() {
    override fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }
}


data class LiteralExpression(val value: Int): Expression() {
    override fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }
}

data class BinaryExpression(val leftExpr: Expression, val operation: String, val rightExpr: Expression): Expression() {
    override fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }
}

data class UnaryMinusExpression(val expression: Expression): Expression() {
    override fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }
}

data class ExpressionWithBraces(val expression: Expression): Expression() {
    override fun accept(visitor: AstVisitor): Int {
        return visitor.visit(this)
    }
}