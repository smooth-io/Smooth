package io.smooth.store.converter

interface DataConverter<First, Second> {

    fun convert(first: First): Second

    fun convertReversed(second: Second): First

}