package ru.topbun.models.recipe

enum class Difficulty {

    Easy, Normal, Hard;

    companion object{
        fun calculateDifficulty(time: Int, countIngredients: Int): Difficulty {
            val points = countIngredients + (time / 10)
            return when {
                points <= 7 -> Difficulty.Easy
                points <= 14 -> Difficulty.Normal
                else -> Difficulty.Hard
            }
        }
    }

}