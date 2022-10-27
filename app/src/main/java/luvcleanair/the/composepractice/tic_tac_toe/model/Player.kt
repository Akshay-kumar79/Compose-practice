package luvcleanair.the.composepractice.tic_tac_toe.model

sealed class Player{
    object X: Player()
    object O: Player()
    object Non: Player()
}
