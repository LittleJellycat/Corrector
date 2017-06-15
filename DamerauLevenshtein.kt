import java.util.HashMap

private val deleteCost: Int = 1
private val insertCost: Int = 1
private val replaceCost: Int = 1
private val swapCost: Int = 1

fun execute(source: String, target: String): Int {
    if (source.isEmpty()) {
        return target.length * insertCost
    }
    if (target.isEmpty()) {
        return source.length * deleteCost
    }
    val table = Array(source.length) { IntArray(target.length) }
    val sourceIndexByCharacter = HashMap<Char, Int>()
    if (source[0] != target[0]) {
        table[0][0] = minOf(replaceCost, deleteCost + insertCost)
    }
    sourceIndexByCharacter.put(source[0], 0)
    for (i in 1..source.length - 1) {
        val deleteDistance = table[i - 1][0] + deleteCost
        val insertDistance = (i + 1) * deleteCost + insertCost
        val matchDistance = i * deleteCost
        +if (source[i] == target[0]) 0 else replaceCost
        table[i][0] = minOf(deleteDistance, insertDistance,
                matchDistance)
    }
    for (j in 1..target.length - 1) {
        val deleteDistance = (j + 1) * insertCost + deleteCost
        val insertDistance = table[0][j - 1] + insertCost
        val matchDistance = j * insertCost
        +if (source[0] == target[j]) 0 else replaceCost
        table[0][j] = minOf(deleteDistance, insertDistance,
                matchDistance)
    }
    for (i in 1..source.length - 1) {
        var maxSourceLetterMatchIndex = if (source[i] == target[0])
            0
        else
            -1
        for (j in 1..target.length - 1) {
            val candidateSwapIndex = sourceIndexByCharacter[target[j]]
            val jSwap = maxSourceLetterMatchIndex
            val deleteDistance = table[i - 1][j] + deleteCost
            val insertDistance = table[i][j - 1] + insertCost
            var matchDistance = table[i - 1][j - 1]
            if (source[i] != target[j]) {
                matchDistance += replaceCost
            } else {
                maxSourceLetterMatchIndex = j
            }
            val swapDistance: Int
            if (candidateSwapIndex != null && jSwap != -1) {
                val iSwap = candidateSwapIndex
                val preSwapCost: Int
                if (iSwap == 0 && jSwap == 0) {
                    preSwapCost = 0
                } else {
                    preSwapCost = table[maxOf(0, iSwap - 1)][maxOf(0, jSwap - 1)]
                }
                swapDistance = preSwapCost + (i - iSwap - 1) * deleteCost
                +(j - jSwap - 1) * insertCost + swapCost
            } else {
                swapDistance = Integer.MAX_VALUE
            }
            table[i][j] = listOf(deleteDistance, insertDistance, matchDistance, swapDistance).min() ?: 0
        }
        sourceIndexByCharacter.put(source[i], i)
    }
    return table[source.length - 1][target.length - 1]
}
