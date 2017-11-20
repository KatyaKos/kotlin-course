import ru.spbau.mit.interpreter.*

object ExampleAsts {

    private fun printy(numbers: List<Expression>): PrintLn = PrintLn(numbers)
    private fun literal(number: Int): LiteralExpression = LiteralExpression(number)
    private fun identifier(name: String): IdentifierExpression = IdentifierExpression(name)

    val easyAst0 = File(Block(listOf(Variable("a", literal(1)), Variable("b", literal(-1)),
            IfStatement(
                    RelationExpression(identifier("b"), ">", literal(0)),
                    BlockWithBraces(Block(listOf(printy(listOf(identifier("b")))))),
                    BlockWithBraces(Block(listOf(
                            Assignment("b",
                                    BinaryExpression(identifier("b"), "+", literal(3))),
                            printy(listOf(
                                    BinaryExpression(identifier("b"), "-", identifier("a")))))))
            ))))


    val easyAst1 = File(Block(listOf(Variable("a", literal(10)), Variable("b", literal(20)),
            IfStatement(
                    RelationExpression(identifier("a"), ">", identifier("b")),
                    BlockWithBraces(Block(listOf(printy(listOf(literal(1)))))),
                    BlockWithBraces(Block(listOf(printy(listOf(literal(0))))))
            ))))

    val fibAst = File(Block(listOf(Function("fib", listOf("a"), BlockWithBraces(Block(listOf(
                    IfStatement(
                            RelationExpression(identifier("a"), "<=", literal(1)),
                            BlockWithBraces(Block(listOf(ReturnStatement(literal(1))))),null),
                    ReturnStatement(
                            BinaryExpression(
                                    FunctionCallExpression("fib", listOf(
                                            BinaryExpression(identifier("a"), "-", literal(1)))),
                                    "+",
                                    FunctionCallExpression("fib", listOf(
                                            BinaryExpression(identifier("a"), "-", literal(2))))
                            )
                    )
            )))), Variable("b", literal(1)), WhileStatement(
                    RelationExpression(identifier("b"), "<=", literal(5)),
                    BlockWithBraces(Block(listOf(
                            printy(listOf(
                                    identifier("b"),
                                    FunctionCallExpression("fib", listOf(identifier("b")))
                            )),
                            Assignment("b", BinaryExpression(identifier("b"), "+", literal(1)))
                    )))))))

    val fooAst = File(Block(listOf(
            Function("foo", listOf("a"), BlockWithBraces(Block(listOf(
                    Function("bar", listOf("b"), BlockWithBraces(Block(listOf(
                            ReturnStatement(BinaryExpression(identifier("b"), "+", identifier("a")))
                    )))), ReturnStatement(FunctionCallExpression("bar", listOf(literal(1))))
            )))), printy(listOf(FunctionCallExpression("foo", listOf(literal(41))))))))
}