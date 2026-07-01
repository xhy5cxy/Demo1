package com.tour.app.di;

import com.tour.app.auth.repository.AuthRepository;
import com.tour.app.network.StrategyService;
import com.tour.app.network.SpotService;
import com.tour.app.network.TripService;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    AuthRepository authRepository();
    StrategyService strategyService();
    SpotService spotService();
    TripService tripService();
    void inject(com.tour.app.auth.ui.LoginActivity activity);
} 