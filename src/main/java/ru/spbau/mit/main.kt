package ru.spbau.mit

import ru.spbau.mit.elements.Tex


fun start(init: Tex.() -> Unit): Tex {
    val res = Tex()
    res.init()
    return res
}