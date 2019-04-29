package com.wuyr.hexagram

import android.util.Base64

/**
 * @author wuyr
 * @github https://github.com/wuyr/HexagramDecoder
 * @since 2019-04-28 下午11:49
 */

fun String.autoDecodeOrEncode() = if (isCipherText()) decodeHexagram() else encodeHexagram()

fun String.isCipherText(): Boolean {
    var decodeHitCount = 0
    var temp: String
    var start = 0
    for (end in 0..length) {
        temp = substring(start, end)
        if (DECODE_MAP_TABLE.containsKey(temp)) {
            start = end
            decodeHitCount++
        }
    }
    return if (start != length) {
        val symbol = convertToSymbol()
        if (symbol.isNotEmpty()) symbol.isCipherText() else false
    } else true
}

fun String.encodeHexagram(): String {
    val base64 = Base64.encodeToString(
        toByteArray(),
        Base64.URL_SAFE and Base64.NO_WRAP and Base64.NO_PADDING
    )
    val result = StringBuilder()
    var temp: String
    for (char in base64) {
        temp = char.toString()
        if (ENCODE_MAP_TABLE.containsKey(temp)) {
            result.append(ENCODE_MAP_TABLE[temp])
        }
    }
    return result.toString()
}

fun String.decodeHexagram(): String {
    val base64 = StringBuilder()
    var start = 0
    var temp: String
    for (end in 0..length) {
        temp = substring(start, end)
        if (DECODE_MAP_TABLE.containsKey(temp)) {
            start = end
            base64.append(DECODE_MAP_TABLE[temp])
        }
    }
    return try {
        String(
            Base64.decode(
                base64.toString(),
                Base64.URL_SAFE and Base64.NO_WRAP and Base64.NO_PADDING
            )
        )
    } catch (t: Throwable) {
        base64.toString()
    }
}

fun String.convertToSymbol(): String {
    val result = StringBuilder()
    var start = 0
    var temp: String
    for (end in 0..length) {
        temp = substring(start, end)
        if (TEXT_MAP_TABLE.containsKey(temp)) {
            start = end
            result.append(TEXT_MAP_TABLE[temp])
        }
    }
    return result.toString()
}

fun String.convertToText(): String {
    val result = StringBuilder()
    var temp: String
    for (char in this) {
        temp = char.toString()
        if (SYMBOL_MAP_TABLE.containsKey(temp)) {
            result.append(SYMBOL_MAP_TABLE[temp])
        }
    }
    return result.toString()
}

private val SYMBOL_MAP_TABLE = hashMapOf(
    "䷀" to "乾",
    "䷁" to "坤",
    "䷂" to "屯",
    "䷃" to "蒙",
    "䷄" to "需",
    "䷅" to "訟",
    "䷆" to "師",
    "䷇" to "比",
    "䷈" to "小畜",
    "䷉" to "履",
    "䷊" to "泰",
    "䷋" to "否",
    "䷌" to "同人",
    "䷍" to "大有",
    "䷎" to "謙",
    "䷏" to "豫",
    "䷐" to "隨",
    "䷑" to "蠱",
    "䷒" to "臨",
    "䷓" to "觀",
    "䷔" to "噬嗑",
    "䷕" to "贲",
    "䷖" to "剝",
    "䷗" to "複",
    "䷘" to "無妄",
    "䷙" to "大畜",
    "䷚" to "頤",
    "䷛" to "大過",
    "䷜" to "坎",
    "䷝" to "離",
    "䷞" to "鹹",
    "䷟" to "恒",
    "䷠" to "遁",
    "䷡" to "大壯",
    "䷢" to "晉",
    "䷣" to "明夷",
    "䷤" to "家人",
    "䷥" to "睽",
    "䷦" to "蹇",
    "䷧" to "解",
    "䷨" to "損",
    "䷩" to "益",
    "䷪" to "夬",
    "䷫" to "姤",
    "䷬" to "萃",
    "䷭" to "升",
    "䷮" to "困",
    "䷯" to "井",
    "䷰" to "革",
    "䷱" to "鼎",
    "䷲" to "震",
    "䷳" to "艮",
    "䷴" to "漸",
    "䷵" to "歸妹",
    "䷶" to "豐",
    "䷷" to "旅",
    "䷸" to "巽",
    "䷹" to "兌",
    "䷺" to "渙",
    "䷻" to "節",
    "䷼" to "中孚",
    "䷽" to "小過",
    "䷾" to "既濟",
    "䷿" to "未濟"
)
private val TEXT_MAP_TABLE = hashMapOf(
    "乾" to "䷀",
    "坤" to "䷁",
    "屯" to "䷂",
    "蒙" to "䷃",
    "需" to "䷄",
    "訟" to "䷅",
    "師" to "䷆",
    "比" to "䷇",
    "小畜" to "䷈",
    "履" to "䷉",
    "泰" to "䷊",
    "否" to "䷋",
    "同人" to "䷌",
    "大有" to "䷍",
    "謙" to "䷎",
    "豫" to "䷏",
    "隨" to "䷐",
    "蠱" to "䷑",
    "臨" to "䷒",
    "觀" to "䷓",
    "噬嗑" to "䷔",
    "贲" to "䷕",
    "剝" to "䷖",
    "複" to "䷗",
    "無妄" to "䷘",
    "大畜" to "䷙",
    "頤" to "䷚",
    "大過" to "䷛",
    "坎" to "䷜",
    "離" to "䷝",
    "鹹" to "䷞",
    "恒" to "䷟",
    "遁" to "䷠",
    "大壯" to "䷡",
    "晉" to "䷢",
    "明夷" to "䷣",
    "家人" to "䷤",
    "睽" to "䷥",
    "蹇" to "䷦",
    "解" to "䷧",
    "損" to "䷨",
    "益" to "䷩",
    "夬" to "䷪",
    "姤" to "䷫",
    "萃" to "䷬",
    "升" to "䷭",
    "困" to "䷮",
    "井" to "䷯",
    "革" to "䷰",
    "鼎" to "䷱",
    "震" to "䷲",
    "艮" to "䷳",
    "漸" to "䷴",
    "歸妹" to "䷵",
    "豐" to "䷶",
    "旅" to "䷷",
    "巽" to "䷸",
    "兌" to "䷹",
    "渙" to "䷺",
    "節" to "䷻",
    "中孚" to "䷼",
    "小過" to "䷽",
    "既濟" to "䷾",
    "未濟" to "䷿"
)
private val ENCODE_MAP_TABLE = hashMapOf(
    "A" to "䷀",
    "B" to "䷁",
    "C" to "䷂",
    "D" to "䷃",
    "E" to "䷄",
    "F" to "䷅",
    "G" to "䷆",
    "H" to "䷇",
    "I" to "䷈",
    "J" to "䷉",
    "K" to "䷊",
    "L" to "䷋",
    "M" to "䷌",
    "N" to "䷍",
    "O" to "䷎",
    "P" to "䷏",
    "Q" to "䷐",
    "R" to "䷑",
    "S" to "䷒",
    "T" to "䷓",
    "U" to "䷔",
    "V" to "䷕",
    "W" to "䷖",
    "X" to "䷗",
    "Y" to "䷘",
    "Z" to "䷙",
    "a" to "䷚",
    "b" to "䷛",
    "c" to "䷜",
    "d" to "䷝",
    "e" to "䷞",
    "f" to "䷟",
    "g" to "䷠",
    "h" to "䷡",
    "i" to "䷢",
    "j" to "䷣",
    "k" to "䷤",
    "l" to "䷥",
    "m" to "䷦",
    "n" to "䷧",
    "o" to "䷨",
    "p" to "䷩",
    "q" to "䷪",
    "r" to "䷫",
    "s" to "䷬",
    "t" to "䷭",
    "u" to "䷮",
    "v" to "䷯",
    "w" to "䷰",
    "x" to "䷱",
    "y" to "䷲",
    "z" to "䷳",
    "0" to "䷴",
    "1" to "䷵",
    "2" to "䷶",
    "3" to "䷷",
    "4" to "䷸",
    "5" to "䷹",
    "6" to "䷺",
    "7" to "䷻",
    "8" to "䷼",
    "9" to "䷽",
    "+" to "䷾",
    "/" to "䷿"
)
private val DECODE_MAP_TABLE = hashMapOf(
    "䷀" to "A",
    "䷁" to "B",
    "䷂" to "C",
    "䷃" to "D",
    "䷄" to "E",
    "䷅" to "F",
    "䷆" to "G",
    "䷇" to "H",
    "䷈" to "I",
    "䷉" to "J",
    "䷊" to "K",
    "䷋" to "L",
    "䷌" to "M",
    "䷍" to "N",
    "䷎" to "O",
    "䷏" to "P",
    "䷐" to "Q",
    "䷑" to "R",
    "䷒" to "S",
    "䷓" to "T",
    "䷔" to "U",
    "䷕" to "V",
    "䷖" to "W",
    "䷗" to "X",
    "䷘" to "Y",
    "䷙" to "Z",
    "䷚" to "a",
    "䷛" to "b",
    "䷜" to "c",
    "䷝" to "d",
    "䷞" to "e",
    "䷟" to "f",
    "䷠" to "g",
    "䷡" to "h",
    "䷢" to "i",
    "䷣" to "j",
    "䷤" to "k",
    "䷥" to "l",
    "䷦" to "m",
    "䷧" to "n",
    "䷨" to "o",
    "䷩" to "p",
    "䷪" to "q",
    "䷫" to "r",
    "䷬" to "s",
    "䷭" to "t",
    "䷮" to "u",
    "䷯" to "v",
    "䷰" to "w",
    "䷱" to "x",
    "䷲" to "y",
    "䷳" to "z",
    "䷴" to "0",
    "䷵" to "1",
    "䷶" to "2",
    "䷷" to "3",
    "䷸" to "4",
    "䷹" to "5",
    "䷺" to "6",
    "䷻" to "7",
    "䷼" to "8",
    "䷽" to "9",
    "䷾" to "+",
    "䷿" to "/"
)
