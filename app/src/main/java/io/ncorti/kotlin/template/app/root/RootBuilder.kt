package io.ncorti.kotlin.template.app.root

import io.smooth.arch.dagger.RibScope
import io.smooth.arch.dagger.builder.DaggerInteractorBuilder
import io.smooth.arch.dagger.component.InteractorComponent
import io.smooth.arch.dagger.module.InteractorModule
import dagger.*
import javax.inject.Inject


class RootBuilder @Inject constructor(
    dependencies: Dependencies
) : DaggerInteractorBuilder<RootScreen, RootFragmentInteractor, RootBuilder.RootComponent, RootBuilder.Dependencies>(
    dependencies
) {

    override fun getComponent(): RootComponent =
        DaggerRootBuilder_RootComponent.builder()
            .dependencies(getDependencies())
            .build()

    interface Dependencies {

    }

    @Component(
        modules = [RootModule::class],
        dependencies = [Dependencies::class]
    )
    @RibScope
    interface RootComponent :
        InteractorComponent<RootScreen, RootFragmentInteractor> {

        @Component.Builder
        interface Builder {

            fun dependencies(dependencies: Dependencies): Builder

            fun build(): RootComponent
        }

    }

    @Module(
        includes = [InteractorModule::class]
    )
    abstract class RootModule {

//        @[Binds RibScope]
//        abstract fun viewModel(rootViewModel: RootViewModel): ViewModel

    }


}