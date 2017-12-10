package ru.spbau.mit.exceptions

class ScopeException(override var message: String) : Throwable(message)

fun <T> T?.mustBeNotNull(): T = this ?: throw ScopeException("Found unexpected null.")