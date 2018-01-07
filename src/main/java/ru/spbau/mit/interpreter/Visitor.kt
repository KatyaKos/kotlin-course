package ru.spbau.mit.interpreter

import ru.spbau.mit.parser.FunBaseVisitor
import ru.spbau.mit.parser.FunParser

class Visitor : FunBaseVisitor<Ast>() {

    override fun visitFile(ctx: FunParser.FileContext): Ast = File(visit(ctx.block()) as Block, ctx.start.line)

    override fun visitBlock(ctx: FunParser.BlockContext): Ast =
            Block(ctx.statement().map { visit(it) as Statement }, ctx.start.line)

    override fun visitBlockWithBraces(ctx: FunParser.BlockWithBracesContext): Ast =
            BlockWithBraces(visit(ctx.block()) as Block, ctx.start.line)

    override fun visitFunction(ctx: FunParser.FunctionContext): Ast = Function(
            ctx.Identifier().text,
            ctx.parameters().Identifier().map { it.text },
            visit(ctx.blockWithBraces()) as BlockWithBraces,
            ctx.start.line
    )

    override fun visitPrintLn(ctx: FunParser.PrintLnContext): Ast =
            PrintLn(ctx.arguments().expression().map { visit(it) as Expression }, ctx.start.line)

    override fun visitVariable(ctx: FunParser.VariableContext): Ast =
            Variable(ctx.Identifier().text, visit(ctx.expression()) as Expression, ctx.start.line)

    override fun visitFunctionCallExpression(ctx: FunParser.FunctionCallExpressionContext): Ast =
            FunctionCallExpression(
                    ctx.Identifier().text,
                    ctx.arguments().expression().map { visit(it) as Expression },
                    ctx.start.line
            )

    override fun visitIdentifierExpression(ctx: FunParser.IdentifierExpressionContext): Ast =
            IdentifierExpression(ctx.text, ctx.start.line)

    override fun visitLiteralExpression(ctx: FunParser.LiteralExpressionContext): Ast =
            LiteralExpression(ctx.Literal().text.toInt(), ctx.start.line)

    override fun visitBinaryExpression(ctx: FunParser.BinaryExpressionContext): Ast =
        BinaryExpression(
                visit(ctx.left) as Expression,
                ctx.operation.text, visit(ctx.right) as Expression,
                ctx.start.line
        )

    override fun visitUnaryMinusExpression(ctx: FunParser.UnaryMinusExpressionContext): Ast =
            UnaryMinusExpression(visit(ctx.expression()) as Expression, ctx.start.line)

    override fun visitExpressionWithBraces(ctx: FunParser.ExpressionWithBracesContext): Ast =
            ExpressionWithBraces(visit(ctx.expression()) as Expression, ctx.start.line)

    override fun visitWhileStatement(ctx: FunParser.WhileStatementContext): Ast =
            WhileStatement(
                    visit(ctx.expression()) as Expression,
                    visit(ctx.blockWithBraces()) as BlockWithBraces,
                    ctx.start.line
            )

    override fun visitIfStatement(ctx: FunParser.IfStatementContext): Ast =
            IfStatement(
                    visit(ctx.expression()) as Expression,
                    visit(ctx.bodyTrue) as BlockWithBraces,
                    if (ctx.blockWithBraces().size >= 2) visit(ctx.bodyFalse) as BlockWithBraces else null,
                    ctx.start.line
            )

    override fun visitAssignment(ctx: FunParser.AssignmentContext): Ast =
            Assignment(ctx.Identifier().text, visit(ctx.expression()) as Expression, ctx.start.line)

    override fun visitReturnStatement(ctx: FunParser.ReturnStatementContext): Ast =
            ReturnStatement(visit(ctx.expression()) as Expression, ctx.start.line)
}