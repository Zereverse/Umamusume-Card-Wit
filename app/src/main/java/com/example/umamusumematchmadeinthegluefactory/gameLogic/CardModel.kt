package com.example.umamusumematchmadeinthegluefactory.gameLogic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CardModel(
    val id: Int,
    val name: String,
    val backImg: Int,
    val frontImg: Int,
    initialIsFlipped: Boolean = false,
    initialIsMatched: Boolean = false
) {
    var isFlipped by mutableStateOf(initialIsFlipped)
    var isMatched by mutableStateOf(initialIsMatched)
}
