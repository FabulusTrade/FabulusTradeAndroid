package ru.fabulus.fabulustrade.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.fabulus.fabulustrade.di.ViewModelKey
import ru.fabulus.fabulustrade.viewmodel.ProfileEditingViewModel
import ru.fabulus.fabulustrade.viewmodel.SetEmailViewModel
import ru.fabulus.fabulustrade.viewmodel.SetFirstAndLastNamesViewModel
import ru.fabulus.fabulustrade.viewmodel.SetPasswordFirstViewModel
import ru.fabulus.fabulustrade.viewmodel.SetPasswordSecondViewModel
import ru.fabulus.fabulustrade.viewmodel.SetPhoneNumberViewModel
import ru.fabulus.fabulustrade.viewmodel.SetUsernameViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SetUsernameViewModel::class)
    abstract fun bindSetUsernameViewModel(viewmodel: SetUsernameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetFirstAndLastNamesViewModel::class)
    abstract fun bindSetFirstAndLastNamesViewModel(viewmodel: SetFirstAndLastNamesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetEmailViewModel::class)
    abstract fun bindSetEmailViewModel(viewmodel: SetEmailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetPhoneNumberViewModel::class)
    abstract fun bindSetPhoneNumberViewModel(viewmodel: SetPhoneNumberViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetPasswordFirstViewModel::class)
    abstract fun bindSetPasswordFirstViewModel(viewmodel: SetPasswordFirstViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetPasswordSecondViewModel::class)
    abstract fun bindSetPasswordSecondViewModel(viewmodel: SetPasswordSecondViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileEditingViewModel::class)
    abstract fun bindProfileEditingViewModel(viewmodel: ProfileEditingViewModel): ViewModel
}
