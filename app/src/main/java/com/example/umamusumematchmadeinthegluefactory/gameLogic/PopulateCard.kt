package com.example.umamusumematchmadeinthegluefactory.gameLogic

import com.example.umamusumematchmadeinthegluefactory.R

class PopulateCard(private val numCards: Int) {

    private fun allCards(): List<CardModel> {
        val rawList = listOf(
            "haru" to R.drawable.haru,
            "black" to R.drawable.black,
            "agtac" to R.drawable.agtac,
            "bakushin" to R.drawable.bakushin,
            "gold" to R.drawable.gold,
            "cafe" to R.drawable.cafe,
            "cap" to R.drawable.cap,
            "city" to R.drawable.city,
            "cross" to R.drawable.cross,
            "diamond" to R.drawable.diamond,
            "fenomeno" to R.drawable.fenomeno,
            "mambo" to R.drawable.mambo,
            "mcqueen" to R.drawable.mcqueen,
            "turbo" to R.drawable.turbo,
            "suzuka" to R.drawable.suzuka,
            "teio" to R.drawable.teio,
            "week" to R.drawable.week,
            "wei" to R.drawable.wei
        )

        return rawList.mapIndexed { index, (name, img) ->
            CardModel(id = index, name = name, backImg = R.drawable.backcard, frontImg = img)
        }
    }

    fun populateCard(): List<CardModel> {
        val available = allCards()
        val selected = if (numCards < available.size) {
            available.shuffled().take(numCards)
        } else {
            available
        }

        val gameList = mutableListOf<CardModel>()
        selected.forEachIndexed { index, card ->
            // Create two distinct CardModel instances for each unique card with unique IDs
            gameList.add(CardModel(id = index * 2, name = card.name, backImg = card.backImg, frontImg = card.frontImg))
            gameList.add(CardModel(id = index * 2 + 1, name = card.name, backImg = card.backImg, frontImg = card.frontImg))
        }
        
        return gameList.shuffled()
    }
}
