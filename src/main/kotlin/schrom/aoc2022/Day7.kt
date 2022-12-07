package schrom.aoc2022

import java.io.File

interface Node {
    val name: String
}

class AoCFile(override val name: String, val size: Int) : Node

class Directory(override val name: String, private val parent: Directory?) : Node {
    private val subDirectories = mutableSetOf<Directory>()
    private val files = mutableSetOf<AoCFile>()

    private fun addDSubdirectory(name: String) {
        subDirectories.add(Directory(name, this))
    }

    private fun addFile(name: String, size: Int) {
        files.add(AoCFile(name, size))
    }

    fun sizeOfDirectoryAndSubDirectories(): Int {
        return files.sumOf { it.size } + subDirectories.sumOf { it.sizeOfDirectoryAndSubDirectories() }
    }

    fun sizeMap(): Map<Directory, Int> {
        val entries = mutableMapOf<Directory, Int>()
        entries[this] = sizeOfDirectoryAndSubDirectories()
        subDirectories.forEach {
            entries.putAll(it.sizeMap())
        }
        return entries
    }

    // TODO should not mutate input data, instead call process() with remaining elements
    fun process(commands: MutableList<String>) {
        if (commands.isEmpty()) {
            return
        }

        val command = commands.removeFirst()
        when {
            command.startsWith("$ cd ") -> {
                when (val dirName = command.substring(5)) {
                    // cd / is only called in the first line, no need find root, just go on
                    "/" -> this.process(commands)

                    // elves are nice and never do cd .. in /
                    ".." -> parent?.process(commands)

                    // elves are nice and never cd into a directory that has not been ls'ed before
                    else -> subDirectories.find { it.name == dirName }?.process(commands)
                }
            }

            command == "$ ls" -> {
                while (commands.isNotEmpty() && !commands.first().startsWith("$")) {
                    val fileOrDir = commands.removeFirst().split(" ")
                    if (fileOrDir[0] == "dir") {
                        addDSubdirectory(fileOrDir[1])
                    } else {
                        addFile(fileOrDir[1], fileOrDir[0].toInt())
                    }
                }
                this.process(commands)
            }
        }
    }
}

fun main() {

    val data = File("data/day7").readLines().toMutableList()

    val root = Directory("/", null)
    root.process(data)

    val sum = root.sizeMap()
        .filter { it.key.name != "/" && it.value <= 100000 }
        .map { it.value }
        .sum()

    println(sum)
}