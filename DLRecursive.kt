fun calculateDistance(source: String, target: String): Int {
    return calcDist(source, target, source.length, target.length, mutableMapOf<Pair<Int, Int>, Int>())
}

private fun calcDist(source: String, target: String, i: Int, j: Int, memo: MutableMap<Pair<Int, Int>, Int>): Int {
    if (memo.containsKey(i to j)) return memo.getOrDefault(i to j, Int.MAX_VALUE)
    if (minOf(i, j) == 0) {
        val result = maxOf(i, j)
        memo[i to j] = result
        return result
    }
    if (i > 1 && j > 1 && source[i - 1] == target[j - 2] && source[i - 2] == target[j - 1]) {
        val result = listOf<Int>(calcDist(source, target, i - 1, j, memo) + 1,
                calcDist(source, target, i, j - 1, memo) + 1,
                calcDist(source, target, i - 2, j - 2, memo) + 1,
                calcDist(source, target, i - 1, j - 1, memo) + if (source[i - 1] == target[j - 1]) 1 else 0)
                .min() ?: Int.MAX_VALUE
        memo[i to j] = result
        return result
    }
    val result = listOf<Int>(calcDist(source, target, i - 1, j, memo) + 1,
            calcDist(source, target, i, j - 1, memo) + 1,
            calcDist(source, target, i - 1, j - 1, memo) + if (source[i - 1] == target[j - 1]) 1 else 0)
            .min() ?: Int.MAX_VALUE
    memo[i to j] = result
    return result
}
