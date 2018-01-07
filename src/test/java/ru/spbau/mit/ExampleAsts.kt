import ru.spbau.mit.interpreter.*

object ExampleAsts {

    private fun printy(numbers: List<Expression>, line: Int): PrintLn = PrintLn(numbers, line)
    private fun literal(number: Int, line: Int): LiteralExpression = LiteralExpression(number, line)
    private fun identifier(name: String, line: Int): IdentifierExpression = IdentifierExpression(name, line)

    val easyAst0 = File(Block(listOf(
            Variable("a", literal(1, 1), 1),
            Variable("b", literal(-1, 2), 2),
            IfStatement(
                    BinaryExpression(identifier("b", 3), ">", literal(0, 3), 3),
                    BlockWithBraces(
                            Block(listOf(printy(listOf(identifier("b", 4)), 4)), 4),
                            3),
                    BlockWithBraces(
                            Block(listOf(
                                    Assignment("b",
                                            BinaryExpression(
                                                    identifier("b", 6),
                                                    "+",
                                                    literal(3, 6),
                                                    6
                                            ), 6
                                    ),
                                    printy(listOf(
                                            BinaryExpression(
                                                    identifier("b", 7),
                                                    "-",
                                                    identifier("a", 7),
                                                    7
                                            )), 7
                                    )), 6
                            ), 5
                    ), 3
            )), 1), 1)


    val easyAst1 = File(Block(listOf(
            Variable("a", literal(10, 1), 1),
            Variable("b", literal(20, 2), 2),
            IfStatement(
                    BinaryExpression(identifier("a", 3), ">", identifier("b", 3), 3),
                    BlockWithBraces(Block(listOf(printy(listOf(literal(1, 4)), 4)), 4), 3),
                    BlockWithBraces(Block(listOf(printy(listOf(literal(0, 6)), 6)), 6), 5),
                    3
            )), 1), 1)

    val fibAst = File(Block(listOf(
            Function("fib", listOf("a"),
                    BlockWithBraces(Block(listOf(
                            IfStatement(
                                    BinaryExpression(
                                            identifier("a", 2),
                                            "<=",
                                            literal(1, 2),
                                            2),
                                    BlockWithBraces(Block(listOf(
                                            ReturnStatement(literal(1, 3), 3)
                                    ), 3), 2),
                                    null, 2),
                            ReturnStatement(
                                    BinaryExpression(
                                            FunctionCallExpression("fib", listOf(
                                                    BinaryExpression(
                                                            identifier("a", 5),
                                                            "-",
                                                            literal(1, 5),
                                                            5)
                                            ), 5),
                                            "+",
                                            FunctionCallExpression("fib", listOf(
                                                    BinaryExpression(
                                                            identifier("a", 5),
                                                            "-",
                                                            literal(2, 5),
                                                            5
                                                    )),
                                                    5
                                            ), 5
                                    ), 5
                            )
                    ), 2), 1
                    ), 1
            ),
            Variable("b", literal(1, 8), 8),
            WhileStatement(
                    BinaryExpression(
                            identifier("b", 9),
                            "<=",
                            literal(5, 9),
                            9),
                    BlockWithBraces(Block(listOf(
                            printy(listOf(
                                    identifier("b", 10),
                                    FunctionCallExpression("fib", listOf(identifier("b", 10)), 10)
                            ), 10),
                            Assignment("b", BinaryExpression(
                                    identifier("b", 11),
                                    "+",
                                    literal(1, 11), 11), 11)
                    ), 10), 9), 9
            )
    ), 1), 1)

    val fooAst = File(Block(listOf(
            Function("foo", listOf("a"),
                    BlockWithBraces(Block(listOf(
                            Function("bar", listOf("b"), BlockWithBraces(Block(listOf(
                                    ReturnStatement(
                                            BinaryExpression(
                                                    identifier("b", 3),
                                                    "+",
                                                    identifier("a", 3), 3),
                                            3
                                    )
                            ), 3), 2), 2),
                            ReturnStatement(
                                    FunctionCallExpression("bar", listOf(literal(1, 6)), 6),
                                    6
                            )
                    ), 2), 1), 1
            ),
            printy(listOf(FunctionCallExpression("foo", listOf(literal(41, 9)), 9)), 9)
    ), 1), 1)
}