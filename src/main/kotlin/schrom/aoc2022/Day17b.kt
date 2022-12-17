package schrom.aoc2022

import java.io.File


fun main() {

    val data = File("data/day17").readLines()
    val jetBlow = data.first().map { it }.toCollection(ArrayDeque())

    val chamber = TallNarrowChamber()

//    testdata
//    val patternSize = 35
//    val patternIncrease = 53

    // real data
    val patternSize = 1700
    val patternIncrease = 2654

    val toSkip = 1000000000000 / patternSize - 1
    var i = 1L

    while (true) {
        if (i > 1000000000000) {
            break
        }

        // there is a pattern - everytime when placing 35 (testdata) / 1700 tiles
        // the height increases by the same amount 53 (testdata) / 2654

        if (i < patternSize // first one is different because of floor
            || i >= (1000000000000 - patternSize) // last one is different because fewer tiles to do
        ) {
            var rock: Rock? = null
            if (i.mod(5) == 1) {
                rock = MinusShaped(3, chamber.highestRock + 4)
            } else if (i.mod(5) == 2) {
                rock = PlusShaped(3, chamber.highestRock + 4)
            } else if (i.mod(5) == 3) {
                rock = LShaped(3, chamber.highestRock + 4)
            } else if (i.mod(5) == 4) {
                rock = IShaped(3, chamber.highestRock + 4)
            } else if (i.mod(5) == 0) {
                rock = OShaped(3, chamber.highestRock + 4)
            }
            checkNotNull(rock)

            while (true) {
                if (jetBlow.first() == '<' && rock.canMoveLeft(chamber)) {
                    rock.moveLeft()
                }
                if (jetBlow.first() == '>' && rock.canMoveRight(chamber)) {
                    rock.moveRight()
                }
                jetBlow.addLast(jetBlow.removeFirst())

                if (rock.canMoveDown(chamber)) {
                    rock.moveDown()
                } else {
                    rock.settle(chamber)
                    break
                }
            }
            i++
        } else { // skip the whole thing
            i += (toSkip * patternSize)
        }

    }
    println(chamber.highestRock + toSkip * patternIncrease)
}
