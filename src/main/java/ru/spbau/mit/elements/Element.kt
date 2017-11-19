package ru.spbau.mit.elements

import java.io.OutputStream

interface Element {

    fun render(builder: StringBuilder, indent: Int)

    fun StringBuilder.newLine() = append("\n")

    fun StringBuilder.makeIndent(indent: Int) = append(" ".repeat(indent))

    fun StringBuilder.renderArgs(args: List<String>) {
        if (args.isNotEmpty()) {
            append(args.joinToString("}{", "{", "}"))
        }
    }

    fun StringBuilder.renderOptionalArgs(extraArgs: Array<out String>) {
        if (extraArgs.isNotEmpty()) {
            append(extraArgs.joinToString(",", "[", "]"))
        }
    }
}

abstract class BasicElement : Element {
    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, 0)
        return builder.toString()
    }

    fun toOutputStream(stream: OutputStream) = stream.write(toString().toByteArray())
}

class TextElement(private val text: String) : Element {
    override fun render(builder: StringBuilder, indent: Int) {
        val lines = text.split("\n")
        for (line in lines) {
            builder.makeIndent(indent)
            builder.append("$line\n")
        }
    }
}
