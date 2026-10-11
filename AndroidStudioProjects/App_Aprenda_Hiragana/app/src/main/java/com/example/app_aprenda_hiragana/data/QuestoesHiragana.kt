package com.example.app_aprenda_hiragana.data

import com.example.app_aprenda_hiragana.model.Questao

val questoesHiragana: List<Questao> = listOf(
    // Linha A
    Questao("あ", "a", listOf("a", "i", "u", "o")),
    Questao("い", "i", listOf("i", "a", "ri", "ni")),
    Questao("う", "u", listOf("u", "o", "wa", "e")),
    Questao("え", "e", listOf("e", "a", "ku", "he")),
    Questao("お", "o", listOf("o", "a", "ku", "wa")),

    // Linha KA
    Questao("か", "ka", listOf("ka", "ki", "ku", "ha")),
    Questao("き", "ki", listOf("ki", "sa", "ka", "ku")),
    Questao("く", "ku", listOf("ku", "ka", "ki", "su")),
    Questao("け", "ke", listOf("ke", "ka", "ku", "se")),
    Questao("こ", "ko", listOf("ko", "ki", "ku", "he")),

    // Linha SA
    Questao("さ", "sa", listOf("sa", "chi", "ki", "so")),
    Questao("し", "shi", listOf("shi", "sa", "chi", "u")),
    Questao("す", "su", listOf("su", "se", "ku", "nu")),
    Questao("せ", "se", listOf("se", "su", "ho", "ki")),
    Questao("そ", "so", listOf("so", "ru", "sa", "n")),

    // Linha TA
    Questao("た", "ta", listOf("ta", "na", "ma", "chi")),
    Questao("ち", "chi", listOf("chi", "sa", "ta", "shi")),
    Questao("つ", "tsu", listOf("tsu", "shi", "so", "u")),
    Questao("て", "te", listOf("te", "ta", "ni", "de")),
    Questao("と", "to", listOf("to", "te", "de", "na")),

    // Linha NA
    Questao("な", "na", listOf("na", "ta", "ma", "wa")),
    Questao("に", "ni", listOf("ni", "te", "ke", "ri")),
    Questao("ぬ", "nu", listOf("nu", "me", "su", "wa")),
    Questao("ね", "ne", listOf("ne", "wa", "re", "ru")),
    Questao("の", "no", listOf("no", "me", "nu", "wa")),

    // Linha HA
    Questao("は", "ha", listOf("ha", "ho", "ba", "hi")),
    Questao("ひ", "hi", listOf("hi", "ni", "u", "chi")),
    Questao("ふ", "fu", listOf("fu", "ne", "wa", "u")),
    Questao("へ", "he", listOf("he", "e", "ho", "ko")),
    Questao("ほ", "ho", listOf("ho", "ha", "ma", "ne")),

    // Linha MA
    Questao("ま", "ma", listOf("ma", "ta", "na", "mo")),
    Questao("み", "mi", listOf("mi", "mo", "shi", "ri")),
    Questao("む", "mu", listOf("mu", "mo", "nu", "su")),
    Questao("め", "me", listOf("me", "nu", "mo", "wa")),
    Questao("も", "mo", listOf("mo", "ma", "mi", "ho")),

    // Linha YA
    Questao("や", "ya", listOf("ya", "ha", "ma", "ra")),
    Questao("ゆ", "yu", listOf("yu", "yo", "fu", "wa")),
    Questao("よ", "yo", listOf("yo", "yu", "ki", "ru")),

    // Linha RA
    Questao("ら", "ra", listOf("ra", "na", "ta", "ri")),
    Questao("り", "ri", listOf("ri", "i", "ki", "ru")),
    Questao("る", "ru", listOf("ru", "ro", "ra", "wo")),
    Questao("れ", "re", listOf("re", "ne", "wa", "ru")),
    Questao("ろ", "ro", listOf("ro", "ru", "wo", "ra")),

    // Linha WA / N
    Questao("わ", "wa", listOf("wa", "re", "ne", "wo")),
    Questao("を", "wo", listOf("wo", "ru", "wa", "ro")),
    Questao("ん", "n", listOf("n", "ru", "ro", "wa"))
)