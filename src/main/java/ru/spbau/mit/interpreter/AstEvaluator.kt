package ru.spbau.mit.interpreter

import ru.spbau.mit.exceptions.ParsingException

class AstEvaluator(scope: Scope): AstVisitor {
    private val scopes: MutableList<Scope> = mutableListOf(scope)

    override fun visit(file: File): Int? {
        return visit(file.body)
    }

    override fun visit(block: Block): Int? {
        scopes.add(Scope(scopes.last()))
        val statements = block.statements
        for (statement in statements) {
            statement.accept(this)?.let {
                scopes.removeAt(scopes.lastIndex)
                return it
            }
        }
        scopes.removeAt(scopes.lastIndex)
        return null
    }

    override fun visit(blockWithBraces: BlockWithBraces): Int? {
        return visit(blockWithBraces.block)
    }

    override fun visit(function: Function): Int? {
        val name = function.name
        val parameters = function.parameters
        val body = function.body
        val scope = scopes.last()
        scope.defineFunction(InterpreterFunction(name, parameters, body, scope))
        return null
    }

    override fun visit(printLn: PrintLn): Int? {
        val arguments = printLn.arguments
        print(arguments.map { it.accept(this) }.joinToString(", ") + "\n")
        return null
    }

    override fun visit(variable: Variable): Int? {
        val name = variable.name
        val expression = variable.expression
        val scope = scopes.last()
        expression?.let { scope.initializeVariable(name, expression.accept(this)) }
        return null
    }

    override fun visit(functionCallExpression: FunctionCallExpression): Int {
        val arguments = functionCallExpression.arguments
        val function = scopes.last().getFunction(functionCallExpression.name)
        val scope = Scope(function.scope)
        functionCallExpression.runtimeCheck(function.parameters)
        function.parameters.forEachIndexed {
            i, name -> scope.initializeVariable(name, arguments[i].accept(this))
        }
        scopes.add(scope)
        val result = function.body.accept(this) ?: 0
        scopes.removeAt(scopes.lastIndex)
        return result
    }

    override fun visit(identifierExpression: IdentifierExpression): Int {
        val name = identifierExpression.name
        return scopes.last().getVariable(name)
    }

    override fun visit(literalExpression: LiteralExpression): Int {
        return literalExpression.value
    }

    override fun visit(binaryExpression: BinaryExpression): Int {
        val left = binaryExpression.leftExpr.accept(this)
        val right = binaryExpression.rightExpr.accept(this)
        val operation = binaryExpression.operation
        return when(operation) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" -> if (right == 0) throw ParsingException("Division by zero.") else left / right
            "%" -> if (right == 0) throw ParsingException("Division by zero.") else left % right
            ">" -> if (left > right) 1 else 0
            "<" -> if (left < right) 1 else 0
            ">=" -> if (left >= right) 1 else 0
            "<=" -> if (left <= right) 1 else 0
            "==" -> if (left == right) 1 else 0
            "!=" -> if (left != right) 1 else 0
            "||" -> if (left == 1 || right == 1) 1 else 0
            "&&" -> if (left == 1 && right == 1) 1 else 0
            else -> throw ParsingException("Operation $operation doesn't exist.")
        }
    }

    override fun visit(unaryMinusExpression: UnaryMinusExpression): Int {
        return -unaryMinusExpression.expression.accept(this)
    }

    override fun visit(expressionWithBraces: ExpressionWithBraces): Int {
        return expressionWithBraces.expression.accept(this)
    }

    override fun visit(whileStatement: WhileStatement): Int? {
        val condition = whileStatement.condition
        val body = whileStatement.body
        var ty = condition.accept(this)
        while (ty != 0) {
            body.accept(this)
            ty = condition.accept(this)
        }
        return null
    }

    override fun visit(ifStatement: IfStatement): Int? {
        val condition = ifStatement.condition
        val bodyFalse = ifStatement.bodyFalse
        val bodyTrue = ifStatement.bodyTrue
        return when {
            condition.accept(this) != 0 -> bodyTrue.accept(this)
            bodyFalse != null -> bodyFalse.accept(this)
            else -> null
        }
    }

    override fun visit(assignment: Assignment): Int? {
        val name = assignment.name
        val expression = assignment.expression
        val scope = scopes.last()
        scope.setVariable(name, expression.accept(this))
        return null
    }

    override fun visit(returnStatement: ReturnStatement): Int? {
        val expression = returnStatement.expression
        return expression.accept(this)
    }
}