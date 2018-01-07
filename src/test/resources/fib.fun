fun fib(a) {
    if (a <= 1) {
        return 1
    }
    return fib(a - 1) + fib(a - 2)
}

var b = 1
while (b <= 5) {
    println(b, fib(b))
    b = b + 1
}