package ru.spbau.mit.elements

class Center : BlockCommand("center")

class FlushLeft : BlockCommand("flushleft")

class FlushRight : BlockCommand("flushright")

class Enumerate(vararg optionalsArgs: String) : ListCommand("enumerate", emptyList(), *optionalsArgs)

class Itemize(vararg optionalsArgs: String) : ListCommand("itemize", emptyList(), *optionalsArgs)

class Math : BlockCommand("math") {
    override fun render(builder: StringBuilder, indent: Int) {
        builder.makeIndent(indent)
        builder.append("$$\n");
        renderCommands(builder, indent + 4)
        builder.makeIndent(indent)
        builder.append("$$\n");

    }
}

class Frame(title: String, vararg optionalsArgs: String) : BlockCommand("frame", listOf(title), *optionalsArgs)

class Item : BlockCommand("item") {

    override fun render(builder: StringBuilder, indent: Int) {
        builder.makeIndent(indent)
        builder.append("\\item\n")
        renderCommands(builder, indent + 4)
    }

}

class TexException(reason: String): Exception(reason)

class CustomInlineCommand(name: String, args: List<String> = listOf(), vararg optionalArgs: String)
    : InlineCommand(name, args, *optionalArgs)

class CustomBlockCommand(name: String, args: List<String> = listOf(), vararg optionalArgs: String)
    : BlockCommand(name, args, *optionalArgs)

class DocumentClass(name: String, vararg optionalArgs: String)
    : InlineCommand("documentclass", listOf(name), *optionalArgs)

class UsePackage(name: String, vararg optionalArgs: String)
    : InlineCommand("usepackage", listOf(name), *optionalArgs)

class Document : BlockCommand("document", emptyList())

class Tex : BlockCommand("") {

    private var documentClass: DocumentClass? = null

    private val usePackages: MutableList<UsePackage> = mutableListOf()

    override fun render(builder: StringBuilder, indent: Int) {
        documentClass?.render(builder, indent) ?:
                throw TexException("Your tex document doesn't contain any document classes.")
        usePackages.forEach { it.render(builder, indent) }
        renderCommands(builder, indent)
    }

    fun documentClass(name: String, vararg optionalArgs: String) {
        require(documentClass == null)
        documentClass = DocumentClass(name, *optionalArgs)
    }

    fun usePackage(name: String, vararg optionalArgs: String) {
        usePackages.add(UsePackage(name, *optionalArgs))
    }

    fun document(init: Document.() -> Unit) {
        initElement(Document(), init)
    }
}