package io.ncorti.kotlin.template.app

import io.smooth.constraint.BaseConstraint

class AlwaysTrueConstraint : BaseConstraint() {

    override fun check(): Boolean = true

    override fun clear() {

    }

}