package com.tour.app.network;

import com.tour.app.model.Spot;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.List;

/**
 * 景点相关API接口
 * 匹配后端接口格式
 */
public interface SpotService {
    
    /**
     * 获取景点列表
     */
    @GET("spots")
    Call<ApiResponse<List<Spot>>> getSpots(
            @Query("page") int page,
            @Query("size") int size,
            @Query("city") String city,
            @Query("minRating") Double minRating
    );
    
    /**
     * 获取景点详情
     */
    @GET("spots/{id}")
    Call<ApiResponse<Spot>> getSpotById(@Path("id") Long id);
    
    /**
     * 搜索景点
     */
    @GET("spots/search")
    Call<ApiResponse<List<Spot>>> searchSpots(
            @Query("keyword") String keyword,
            @Query("page") int page,
            @Query("size") int size
    );
    
    /**
     * 获取城市景点
     */
    @GET("spots/city/{city}")
    Call<ApiResponse<List<Spot>>> getSpotsByCity(@Path("city") String city);
}
