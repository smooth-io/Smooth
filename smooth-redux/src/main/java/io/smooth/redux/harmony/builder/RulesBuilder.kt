package io.smooth.redux.harmony.builder

import io.smooth.redux.harmony.ReducerRule

class RulesBuilder {

    private var rules: MutableList<ReducerRule<*>> = arrayListOf()

    fun addRule(rule: ReducerRule<*>) {
        rules.add(rule)
    }

    fun getRules(): List<ReducerRule<*>> = rules

}