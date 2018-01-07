package ru.spbau.mit.interpreter

import ru.spbau.mit.exceptions.ScopeException
import ru.spbau.mit.exceptions.mustBeNotNull

class Scope(var parent: Scope? = null) {
    private val variables: MutableMap<String, Int> = HashMap()
    private val functions: MutableMap<String, InterpreterFunction> = HashMap()

    fun getVariable(name: String): Int =
            variables.getOrElse(name) {
                parent.mustBeNotNull().getVariable(name)
            }

    fun getFunction(name: String): InterpreterFunction =
            functions.getOrElse(name) {
                parent.mustBeNotNull().getFunction(name)
            }

    fun initializeVariable(name: String, value: Int = 0) {
        if (name in variables) throw ScopeException("Variable $name is already defined in the scope")
        variables.put(name, value)
    }

    fun defineFunction(function: InterpreterFunction) {
        val name = function.name
        if (name in functions) {
            throw ScopeException("Function $name already exists");
        }
        functions.put(name, function)
    }

    fun setVariable(name: String, value: Int) {
        if (name in variables) variables.put(name, value)
        else parent.mustBeNotNull().setVariable(name, value)
    }
}