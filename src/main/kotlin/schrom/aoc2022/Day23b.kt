package schrom.aoc2022

import java.io.File


fun main() {

    val data = File("data/day23").readLines()

    var map = GroveMap()
    val considerations = Considerations()

    data
        .forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c == '#') {
                    map[x, y] = GroveMap.Content.ELF
                }
            }
        }

    var round = 1
    while (true) {

        var moves = ProposeMoveMap()
        map.forEachElfDo { x, y ->
            for (r in 0..considerations.rules.lastIndex) {
                val proposal = considerations.rules[r].invoke(x, y, map)
                if (proposal != null) {
                    val target = proposal.getTarget()
                    moves[target.first, target.second].add(proposal)
                    break
                }
            }
        }
        moves = moves.cleanupCollisions()

        var movingElves = 0
        map = GroveMap()
        moves.forEachDo { x, y ->
            val move = moves[x, y].first()
            move.executeMove(map)
            if(move is ElfMove){
                movingElves++
            }
        }

        if(movingElves == 0){
            break
        }

        considerations.rotate()
        round++
    }

    println(round)
}