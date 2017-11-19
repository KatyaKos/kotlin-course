package ru.spbau.mit.elements

class Center : BlockCommand("center", emptyList())

class FlushLeft : BlockCommand("flushleft", emptyList())

class FlushRight : BlockCommand("flushright", emptyList())

class Enumerate(vararg optionalsArgs: String) : ListCommand("enumerate", emptyList(), *optionalsArgs)

class Itemize(vararg optionalsArgs: String) : ListCommand("itemize", emptyList(), *optionalsArgs)

class Math : BlockCommand("math", emptyList())

class Frame(title: String, vararg optionalsArgs: String) : BlockCommand("frame", listOf(title), *optionalsArgs)

class Item : BlockCommand("item", emptyList()) {

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

class Tex : BlockCommand("", emptyList()) {

    private var documentClass: DocumentClass? = null

    private val usePackages: MutableList<UsePackage> = mutableListOf()

    override fun render(builder: StringBuilder, indent: Int) {
        documentClass?.render(builder, indent) ?:
                throw TexException("Your tex document doesn't contain any document classes.")
        usePackages.forEach { it.render(builder, indent) }
        renderCommands(builder, indent)
    }

    fun documentClass(name: String, vararg optionalArgs: String) {
        if (documentClass != null) {
            throw TexException("Your tex document should contain only one document class.")
        }
        documentClass = DocumentClass(name, *optionalArgs)
    }

    fun usePackage(name: String, vararg optionalArgs: String) {
        usePackages.add(UsePackage(name, *optionalArgs))
    }

    fun document(init: Document.() -> Unit) {
        initElement(Document(), init)
    }
}