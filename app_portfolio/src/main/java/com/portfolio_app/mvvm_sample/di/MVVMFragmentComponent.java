package com.portfolio_app.mvvm_sample.di;

import com.portfolio_app.mvvm_sample.service.repository.MVVMRetrofitService;
import com.portfolio_app.mvvm_sample.view.ui.MVVMFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {MVVMFragmentRetrofitModule.class})
@MVVMFragmentScope
public interface MVVMFragmentComponent {

    MVVMRetrofitService getRetrofitService();

    void inject(MVVMFragment mvvmFragment);

    @Subcomponent.Builder
    interface Builder {
        MVVMFragmentComponent.Builder mvvmFragmentRetrofitModule(MVVMFragmentRetrofitModule mvvmFragmentRetrofitModule);

        MVVMFragmentComponent build();
    }
}
