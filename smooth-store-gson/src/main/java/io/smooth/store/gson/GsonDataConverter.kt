package io.smooth.store.gson

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.smooth.store.converter.DataConverter

class GsonDataConverter<T>(private val gson: Gson) :
    DataConverter<T, String> {

    override fun convert(first: T): String =
        gson.toJson(first)

    override fun convertReversed(second: String): T {
        val typeToken = object : TypeToken<T>() {}.type
        return gson.fromJson(second, typeToken) as T
    }

}