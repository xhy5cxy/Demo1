package com.tour.app.network;

import com.tour.app.model.Trip;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.List;

/**
 * 行程相关API接口
 * 匹配后端接口格式
 */
public interface TripService {
    
    /**
     * 获取用户行程列表
     */
    @GET("trips")
    Call<ApiResponse<List<Trip>>> getTrips(
            @Query("userId") Long userId,
            @Query("page") int page,
            @Query("size") int size,
            @Query("status") Integer status
    );
    
    /**
     * 获取行程详情
     */
    @GET("trips/{id}")
    Call<ApiResponse<Trip>> getTripById(@Path("id") Long id);
    
    /**
     * 获取公开行程
     */
    @GET("trips/public")
    Call<ApiResponse<List<Trip>>> getPublicTrips(
            @Query("destination") String destination,
            @Query("page") int page,
            @Query("size") int size
    );
}
