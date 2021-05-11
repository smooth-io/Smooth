package io.smooth.redux.defaults.section

import io.smooth.redux.SmoothStateType
import io.smooth.redux.StateSection

data class ContentSection<Content>(val content: Content, override val isActive: Boolean) :
    StateSection(
        isActive, "__content_section",
        SmoothStateType.Content
    ) {

    override fun changeActivationStatus(isActive: Boolean): StateSection =
        copy(isActive = isActive)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContentSection<*>

        if (content != other.content) return false
        if (isActive != other.isActive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content?.hashCode() ?: 0
        result = 31 * result + isActive.hashCode()
        return result
    }


}