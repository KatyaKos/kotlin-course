package ru.spbau.mit.interpreter

import ru.spbau.mit.parser.FunParser
import com.google.common.collect.ImmutableList
import ru.spbau.mit.exceptions.ParsingException

class InterpreterFunction(
        val name: String,
        val parameters: List<String>,
        val body: BlockWithBraces,
        val scope: Scope
)