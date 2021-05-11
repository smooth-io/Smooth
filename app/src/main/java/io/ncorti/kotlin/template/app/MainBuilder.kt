package io.ncorti.kotlin.template.app

import io.ncorti.kotlin.template.app.root.RootBuilder
import io.ncorti.kotlin.template.app.root.RootScreen
import io.smooth.arch.builder.Builder
import io.smooth.arch.dagger.RibScope
import io.smooth.arch.dagger.builder.BuilderKey
import io.smooth.arch.dagger.builder.container.DaggerContainerBuilder
import io.smooth.arch.dagger.component.container.ContainerComponent
import io.smooth.arch.dagger.module.container.ContainerModule
import dagger.*
import dagger.multibindings.IntoMap
import io.smooth.arch.dagger.module.InteractorModule
import javax.inject.Inject


class MainBuilder @Inject constructor(
    dependencies: Dependencies
) : DaggerContainerBuilder<MainRouter, MainBuilder.MainComponent, MainBuilder.Dependencies>(
    dependencies
) {

    override fun getComponent(): MainComponent =
        DaggerMainBuilder_MainComponent.builder()
            .dependencies(getDependencies())
            .build()

    interface Dependencies {

    }

    @Component(
        modules = [MainModule::class],
        dependencies = [Dependencies::class]
    )
    @RibScope
    interface MainComponent :
        ContainerComponent<MainRouter>,
        RootBuilder.Dependencies {

        @Component.Builder
        interface Builder {

            fun dependencies(dependencies: Dependencies): Builder

            fun build(): MainComponent
        }

    }

    @Module(
        includes = [ContainerModule::class, InteractorModule::class]
    )
    abstract class MainModule {

        @Binds
        abstract fun root(root: MainComponent): RootBuilder.Dependencies

        @Binds
        @IntoMap
        @BuilderKey(RootScreen::class)
        abstract fun bindRoot(rootBuilder: RootBuilder): Builder<*>

//        @[Binds RibScope]
//        abstract fun viewModel(rootViewModel: RootViewModel): ViewModel

    }


}