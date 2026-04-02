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

    // -------------------------------------------------------
    // Call this once when the game starts
    // -------------------------------------------------------
    fun initGame(cardCount: Int) {
        totalCards = cardCount
        matchedCards = 0
        score = 0
        flipCards.clear()
        isProcessing = false
    }

    // -------------------------------------------------------
    // Call this when a player taps a card
    // -------------------------------------------------------
    fun onCardTapped(card: CardModel) {

        // Ignore if already matched, already flipped, or waiting for delay
        if (card.isMatched || card.isFlipped || isProcessing) return

        // Step 1 — Flip card face-up
        card.isFlipped = true

        // Step 2 — Add to flipCards list
        flipCards.add(card)

        // Step 3 — Are two cards selected?
        if (flipCards.size < 2) {
            // No — wait for 2nd card
            return
        }

        // Yes — lock further taps while processing
        isProcessing = true

        val firstCard  = flipCards[0]
        val secondCard = flipCards[1]

        // Step 4 — Do both cards share the same name?
        if (firstCard.name == secondCard.name) {
            // MATCH
            score += 10
            onScoreUpdate(score)

            // Disable flip — cards stay face-up
            firstCard.isMatched  = true
            secondCard.isMatched = true
            matchedCards += 2

            onMatchFound(firstCard, secondCard)

            // Clear flipCards, then check win condition
            clearAndCheckWin()

        } else {
            // NO MATCH
            score -= 5
            onScoreUpdate(score)

            // Flip back after 200ms delay
            handler.postDelayed({
                firstCard.isFlipped  = false
                secondCard.isFlipped = false
                onFlipBack(firstCard, secondCard)

                // Clear flipCards, then check win condition
                clearAndCheckWin()
            }, 500) // Increased delay slightly to be more visible
        }
    }

    // -------------------------------------------------------
    // Clear state & check if game is over
    // -------------------------------------------------------
    private fun clearAndCheckWin() {
        flipCards.clear()
        isProcessing = false

        // matchedCards == totalCards means all matched → end game
        if (matchedCards >= totalCards && totalCards > 0) {
            onGameEnd()
        }
    }

    fun getScore() = score
}
