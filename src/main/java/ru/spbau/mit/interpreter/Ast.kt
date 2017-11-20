package ru.spbau.mit.interpreter

import ru.spbau.mit.exceptions.ParsingException

abstract class Ast {
    abstract fun evaluate(scope: Scope): Int?
}

data class File(val body: Block): Ast() {
    override fun evaluate(scope: Scope): Int? {
        return body.evaluate(scope)
    }
}

data class Block(val statements: List<Statement>): Ast() {
    override fun evaluate(scope: Scope): Int? {
        var newscope = Scope(scope)
        var result: Int? = null
        for (statement in statements) {
            result = statement.evaluate(newscope)
            if (result != null) {
                break
            }
        }
        return result
    }
}

data class BlockWithBraces(val block: Block): Ast() {
    override fun evaluate(scope: Scope): Int? {
        return block.evaluate(scope)
    }

}

abstract class Statement: Ast()


data class Function(val name : String, val parameters: List<String>, val body: BlockWithBraces): Statement() {
    override fun evaluate(scope: Scope): Int? {
        scope.defineFunction(InterpreterFunction(name, parameters, body, scope))
        return null
    }
}

data class PrintLn(val arguments: List<Expression>): Statement() {
    override fun evaluate(scope: Scope): Int? {
        print(arguments.map { it.evaluateExpression(scope) }.joinToString(", ") + "\n")
        return null
    }
}

data class Variable(val name: String, val expression: Expression?): Statement() {
    override fun evaluate(scope: Scope): Int? {
        if (expression != null) scope.initializeVariable(name, expression.evaluateExpression(scope))
        return null
    }
}

abstract class Expression: Statement() {
    abstract fun evaluateExpression(scope: Scope): Int

    override fun evaluate(scope: Scope): Int? {
        return evaluateExpression(scope)
    }
}

data class WhileStatement(val condition: Expression, val body: BlockWithBraces): Statement() {
    override fun evaluate(scope: Scope): Int? {
        while (condition.evaluateExpression(scope) != 0) body.evaluate(scope)
        return null
    }
}

data class IfStatement(
        val condition: Expression,
        val bodyTrue: BlockWithBraces,
        val bodyFalse: BlockWithBraces?
): Statement() {
    override fun evaluate(scope: Scope): Int? = when {
        condition.evaluateExpression(scope) != 0 -> bodyTrue.evaluate(scope)
        bodyFalse != null -> bodyFalse.evaluate(scope)
        else -> null
    }

}

data class Assignment(val name: String, val expression: Expression): Statement() {
    override fun evaluate(scope: Scope): Int? {
        scope.setVariable(name, expression.evaluateExpression(scope))
        return null
    }
}

data class ReturnStatement(val expression: Expression): Statement() {
    override fun evaluate(scope: Scope): Int? {
        return expression.evaluateExpression(scope)
    }
}

data class FunctionCallExpression(val name: String, val arguments: List<Expression>): Expression() {
    override fun evaluateExpression(scope: Scope): Int {
        val function = scope.getFunction(name)
        val execScope = Scope(function.scope)
        if (arguments.size != function.parameters.size) {
            throw ParsingException("Number of arguments in $name is different from number of parameters.")
        }
        function.parameters.forEachIndexed {
            i, name -> execScope.initializeVariable(name, arguments.get(i).evaluateExpression(scope))
        }
        return function.body.evaluate(execScope) ?: 0
    }
}

data class IdentifierExpression(val name: String): Expression() {
    override fun evaluateExpression(scope: Scope): Int {
        return scope.getVariable(name)
    }
}


data class LiteralExpression(val value: Int): Expression() {
    override fun evaluateExpression(scope: Scope): Int {
        return value
    }
}

data class BinaryExpression(val leftExpr: Expression, val operation: String, val rightExpr: Expression): Expression() {
    override fun evaluateExpression(scope: Scope): Int {
        var left = leftExpr.evaluateExpression(scope)
        var right = rightExpr.evaluateExpression(scope)
        return when(operation) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" -> if (right == 0) throw sun.security.pkcs.ParsingException("Division by zero.") else left / right
            "%" -> if (right == 0) throw sun.security.pkcs.ParsingException("Division by zero.") else left % right
            else -> throw sun.security.pkcs.ParsingException("Operation $operation doesn't exist.")
        }
    }
}

data class RelationExpression(val leftExpr: Expression, val operation: String, val rightExpr: Expression): Expression() {
    override fun evaluateExpression(scope: Scope): Int {
        var left = leftExpr.evaluateExpression(scope)
        var right = rightExpr.evaluateExpression(scope)
        return when(operation) {
            ">" -> if (left > right) 1 else 0
            "<" -> if (left < right) 1 else 0
            ">=" -> if (left >= right) 1 else 0
            "<=" -> if (left <= right) 1 else 0
            else -> throw sun.security.pkcs.ParsingException("Operation $operation doesn't exist.")
        }
    }
}

data class EqualityExpression(val leftExpr: Expression, val operation: String, val rightExpr: Expression): Expression() {
    override fun evaluateExpression(scope: Scope): Int {
        var left = leftExpr.evaluateExpression(scope)
        var right = rightExpr.evaluateExpression(scope)
        return when(operation) {
            "==" -> if (left == right) 1 else 0
            "!=" -> if (left != right) 1 else 0
            else -> throw sun.security.pkcs.ParsingException("Operation $operation doesn't exist.")
        }
    }
}

data class orAndExpression(val leftExpr: Expression, val operation: String, val rightExpr: Expression): Expression() {
    override fun evaluateExpression(scope: Scope): Int {
        var left = leftExpr.evaluateExpression(scope)
        var right = rightExpr.evaluateExpression(scope)
        return when(operation) {
            "||" -> if (left == 1 || right == 1) 1 else 0
            "&&" -> if (left == 1 && right == 1) 1 else 0
            else -> throw sun.security.pkcs.ParsingException("Operation $operation doesn't exist.")
        }
    }
}

data class UnaryMinusExpression(val expression: Expression): Expression() {
    override fun evaluateExpression(scope: Scope): Int {
        return -expression.evaluateExpression(scope)
    }
}

data class ExpressionWithBraces(val expression: Expression): Expression() {
    override fun evaluateExpression(scope: Scope): Int {
        return expression.evaluateExpression(scope)
    }
}