package com.milesilac.citylifescan.di;

import com.milesilac.citylifescan.network.CityScanController;
import com.milesilac.citylifescan.network.CityScanService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public CityScanController bindController(CityScanService cityScanService) {
        return new CityScanController(cityScanService);
    }
}
