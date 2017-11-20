fun foo(a) {
    fun bar(b) {
        return b + a
    }

    return bar(1)
}

println(foo(41)) // prints 42