package io.smooth.redux.harmony.modification

import io.smooth.redux.SmoothStateType
import io.smooth.redux.StateSection
import kotlin.reflect.KClass

fun byType(
    type: SmoothStateType
): Modification = Modification {
    this.type == type
}

fun bySection(
    sectionClass: KClass<out StateSection>
): Modification = Modification {
    this::class == sectionClass
}

fun byName(
    name: String
): Modification = Modification {
    this.name == name
}

fun activeSections(): Modification = Modification {
    this.isActive
}

fun deactivatedSections(): Modification = Modification {
    !this.isActive
}

