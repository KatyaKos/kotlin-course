package ru.spbau.mit

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.spbau.mit.exceptions.*
import ru.spbau.mit.interpreter.Ast
import ru.spbau.mit.interpreter.Scope
import ru.spbau.mit.interpreter.Visitor
import ru.spbau.mit.parser.*

fun build(file: String): Ast {
    val lexer = FunLexer(CharStreams.fromFileName(file))
    val parser = FunParser(CommonTokenStream(lexer))
    parser.buildParseTree = true
    if (parser.numberOfSyntaxErrors > 0) {
        throw ParsingException("Unable to parse file. Something is wrong with syntax")
    }
    return Visitor().visit(parser.file())
}

fun interpret(ast: Ast) {
    ast.evaluate(Scope())
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        System.err.println("Please, provide path to file.")
    }
    try {
        interpret(build(args[0]))
    } catch (e: ScopeException) {
        System.err.println(e.message)
    } catch (e: ParsingException) {
        System.err.println(e.message)
    }
}