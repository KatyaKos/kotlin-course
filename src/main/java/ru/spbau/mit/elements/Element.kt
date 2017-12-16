package ru.spbau.mit.elements

import java.io.OutputStream

abstract class Element {

    abstract fun render(builder: StringBuilder, indent: Int)

    protected fun StringBuilder.newLine() = append("\n")

    protected fun StringBuilder.makeIndent(indent: Int) = append(" ".repeat(indent))
}

abstract class BasicElement : Element() {

    protected fun StringBuilder.renderArgs(args: List<String>) {
        if (args.isNotEmpty()) {
            append(args.joinToString("}{", "{", "}"))
        }
    }

    protected fun StringBuilder.renderOptionalArgs(extraArgs: Array<out String>) {
        if (extraArgs.isNotEmpty()) {
            append(extraArgs.joinToString(",", "[", "]"))
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, 0)
        return builder.toString()
    }

    fun toOutputStream(stream: OutputStream) = stream.write(toString().toByteArray())
}

class TextElement(private val text: String) : Element() {
    override fun render(builder: StringBuilder, indent: Int) {
        val lines = text.split("\n")
        for (line in lines) {
            builder.makeIndent(indent)
            builder.append("$line\n")
        }
    }
}
