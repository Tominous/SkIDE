package com.skide
object Info {
    const val version = "2019.1u3"
    var classLoader: ClassLoader? = null
    var prodMode = false

}
fun main(args: Array<String>) = CoreManager().bootstrap(args, null)