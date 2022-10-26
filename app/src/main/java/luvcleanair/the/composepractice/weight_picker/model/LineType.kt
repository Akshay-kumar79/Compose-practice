package luvcleanair.the.composepractice.weight_picker.model

sealed class LineType{
    object Normal: LineType()
    object FiveStep: LineType()
    object TenStep: LineType()
}
