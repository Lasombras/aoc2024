package tools

import java.io.BufferedReader

class ResourceReader(private val name: String) {

    fun <T> useLines(block: (Sequence<String>) -> T) =
        this.javaClass.classLoader.getResourceAsStream(name)?.bufferedReader()?.useLines(block)

    fun readText(): String? =
        this.javaClass.classLoader.getResourceAsStream(name)?.bufferedReader()?.readText()
}