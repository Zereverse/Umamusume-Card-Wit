package com.example.umamusumematchmadeinthegluefactory.gameLogic

import android.os.Handler
import android.os.Looper

class GameLogic(
    private val onScoreUpdate: (Int) -> Unit,
    private val onFlipBack: (CardModel, CardModel) -> Unit,
    private val onMatchFound: (CardModel, CardModel) -> Unit,
    private val onGameEnd: () -> Unit
) {

    private val flipCards = mutableListOf<CardModel>()
    private var score = 0
    private var totalCards = 0
    private var matchedCards = 0
    private var isProcessing = false

    private val handler = Handler(Looper.getMainLooper())
    fun initGame(cardCount: Int) {
        totalCards = cardCount
        matchedCards = 0
        score = 0
        flipCards.clear()
        isProcessing = false
    }

    fun onCardTapped(card: CardModel) {

        if (card.isMatched || card.isFlipped || isProcessing) return

        card.isFlipped = true

        flipCards.add(card)

        if (flipCards.size < 2) {
            return
        }

        isProcessing = true

        val firstCard  = flipCards[0]
        val secondCard = flipCards[1]

        if (firstCard.name == secondCard.name) {
            score += 10
            onScoreUpdate(score)

            firstCard.isMatched  = true
            secondCard.isMatched = true
            matchedCards += 2

            onMatchFound(firstCard, secondCard)

            clearAndCheckWin()

        } else {
            score -= 5
            onScoreUpdate(score)

            // Flip back after 200ms delay
            handler.postDelayed({
                firstCard.isFlipped  = false
                secondCard.isFlipped = false
                onFlipBack(firstCard, secondCard)

                clearAndCheckWin()
            }, 500)
        }
    }

    private fun clearAndCheckWin() {
        flipCards.clear()
        isProcessing = false

        if (matchedCards >= totalCards && totalCards > 0) {
            onGameEnd()
        }
    }

    fun getScore() = score
}
