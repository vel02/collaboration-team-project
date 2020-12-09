package cs.collaboration.yescredit.di;

import androidx.lifecycle.ViewModelProvider;

import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {


    @Binds
    abstract ViewModelProvider.Factory bindViewModelProviderFactory(ViewModelProviderFactory providerFactory);

}
