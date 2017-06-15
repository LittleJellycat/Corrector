import java.io.File
import java.util.*
import java.util.regex.Pattern

internal var dictionary = HashMap<String, Int>()
private fun getKeywords() {
    val keywords = File("C:\\Users\\kotat\\IdeaProjects\\corrector\\keywords.txt").readLines()
    keywords.forEach { dictionary.put(it, -1) }
}

fun makeMagic(text: String, regex: List<Pair<String, String>>): String? {
    getKeywords()
    addUserWords(text, regex)
    return fixSpelling(text)
}

fun main(args: Array<String>) {
    val sc = Scanner(System.`in`)
    val path = sc.nextLine()
    val regexPath = sc.nextLine()
    sc.close()
    val reader = File(regexPath).readLines()
    val regex = (0..reader.size / 2 - 1).map { reader[2 * it] to reader[2 * it + 1] }
    println(makeMagic(File(path).readText(), regex))
}

private fun addUserWords(text: String, regex: List<Pair<String, String>>) {
    val lines = text.lines()
    lines.forEach {
        regex.forEach { (regex, groups) ->
            val matchResult = Pattern.compile(regex).matcher(it)
            if (matchResult.matches()) {
                groups.split(" ").forEach { group ->
                    dictionary[matchResult.group(group.toInt())] = -1
                }
            }
        }
        val split: List<String> = it.split("\\W+".toRegex())
        (split.forEach { if (dictionary[it] != -1) dictionary.merge(it, 1, Int::plus) })
    }
}

fun fixSpelling(text: String): String? {
    val tokens = text.split("\\W+".toRegex())
    val wordsToChange = HashMap<String, String>()
    tokens.forEach {
        if (it.length > 3 && dictionary[it] == 1) {
            val fixed = fixWord(it)
            if (fixed != null)
                wordsToChange.put(it, fixed)
        }
    }
    var newText = text
    wordsToChange.forEach { newText = newText.replace(it.key, it.value) }
    return newText
}

fun fixWord(word: String): String? {
    val weights = HashMap<Int, String>()
    dictionary.keys.forEach { if (it != word) weights.put(calculateDistance(word, it), it) }
    if (weights.keys.min()!! < 4) return weights[weights.keys.min()!!] else return null
}
