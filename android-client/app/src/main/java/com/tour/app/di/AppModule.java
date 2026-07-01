package com.tour.app.di;

import com.tour.app.auth.repository.AuthRepository;
import com.tour.app.network.AuthService;
import com.tour.app.network.StrategyService;
import com.tour.app.network.SpotService;
import com.tour.app.network.TripService;
import com.tour.app.network.ApiClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * 应用程序依赖注入模块
 * 提供网络请求、认证服务和认证数据仓库的依赖注入配置
 */
@Module
public class AppModule {

    /**
     * 提供认证服务实例
     * 基于ApiClient创建认证服务接口的实现
     *
     * @return 认证服务接口实例
     */
    @Provides
    @Singleton
    public AuthService provideAuthService() {
        return ApiClient.getAuthService();
    }

    /**
     * 提供认证数据仓库实例
     * 创建并返回AuthRepository实例用于数据操作
     *
     * @return 认证数据仓库实例
     */
    @Provides
    @Singleton
    public AuthRepository provideAuthRepository(AuthService authService) {
        return new AuthRepository(authService);
    }

    /**
     * 提供攻略服务实例
     */
    @Provides
    @Singleton
    public StrategyService provideStrategyService() {
        return ApiClient.getStrategyService();
    }

    /**
     * 提供景点服务实例
     */
    @Provides
    @Singleton
    public SpotService provideSpotService() {
        return ApiClient.getSpotService();
    }

    /**
     * 提供行程服务实例
     */
    @Provides
    @Singleton
    public TripService provideTripService() {
        return ApiClient.getTripService();
    }
}
