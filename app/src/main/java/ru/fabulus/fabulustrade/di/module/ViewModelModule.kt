package ru.fabulus.fabulustrade.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.fabulus.fabulustrade.di.ViewModelKey
import ru.fabulus.fabulustrade.viewmodel.SetUsernameViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SetUsernameViewModel::class)
    abstract fun bindViewModel(viewmodel: SetUsernameViewModel): ViewModel
}
