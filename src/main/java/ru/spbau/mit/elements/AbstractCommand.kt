package ru.spbau.mit.elements

@DslMarker
annotation class TexMarker

@TexMarker
abstract class Command(
        private val name: String,
        private val args: List<String> = listOf(),
        private vararg val optionalArgs: String
) : BasicElement() {

    val underCommands = arrayListOf<Element>()

    protected fun <T : Element> initElement(el: T, init: T.() -> Unit) {
        el.init()
        underCommands.add(el)
    }

    operator fun String.unaryPlus() = underCommands.add(TextElement(this))

    fun renderCommands(builder: StringBuilder, indent: Int) =
            underCommands.forEach { it.render(builder, indent) }

    fun inlineCommand(name: String, args: List<String> = listOf(), vararg optionalArgs: String) =
            initElement(CustomInlineCommand(name, args, *optionalArgs), {})

    fun blockCommand(name: String, args: List<String> = listOf(), vararg optionalArgs: String,
                     init: CustomBlockCommand.() -> Unit) =
            initElement(CustomBlockCommand(name, args, *optionalArgs), init)

    fun center(init: Center.() -> Unit) = initElement(Center(), init)

    fun flushLeft(init: FlushLeft.() -> Unit) = initElement(FlushLeft(), init)

    fun flushRight(init: FlushRight.() -> Unit) = initElement(FlushRight(), init)

    fun enumerate(init: Enumerate.() -> Unit) = initElement(Enumerate(), init)

    fun itemize(init: Itemize.() -> Unit) = initElement(Itemize(), init)

    fun math(init: Math.() -> Unit) = initElement(Math(), init)

    fun frame(title: String, vararg options: String, init: Frame.() -> Unit) =
            initElement(Frame(title, *options), init)
}

@TexMarker
abstract class InlineCommand(private val name: String,
                    private val args: List<String> = listOf(),
                    private vararg val optionalArgs: String
): Command(name, args, *optionalArgs) {
    override fun render(builder: StringBuilder, indent: Int) {
        builder.makeIndent(indent)
        builder.append("\\$name")
        builder.renderOptionalArgs(optionalArgs)
        builder.renderArgs(args)
        builder.newLine()
    }
}

@TexMarker
abstract class BlockCommand(private val name: String,
                        private val args: List<String> = listOf(),
                        private vararg val optionalArgs: String
): Command(name, args, *optionalArgs) {
    override fun render(builder: StringBuilder, indent: Int) {
        builder.makeIndent(indent)
        builder.append("\\begin{$name}")
        builder.renderOptionalArgs(optionalArgs)
        builder.renderArgs(args)
        builder.newLine()
        renderCommands(builder, indent + 4)
        builder.makeIndent(indent)
        builder.append("\\end{$name}")
        builder.newLine()
    }
}

abstract class ListCommand(private val name: String,
                           private val args: List<String> = listOf(),
                           private vararg val optionalArgs: String
): BlockCommand(name, args, *optionalArgs) {
    fun item(init: Item.() -> Unit) {
        initElement(Item(), init)
    }
}