package com.tour.app.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 用户相关API接口
 * 包含头像上传等功能
 */
public interface UserService {
    
    /**
     * 上传头像
     * @param userId 用户ID
     * @param avatarFile 头像文件
     * @return 上传结果
     */
    @Multipart
    @POST("avatar/upload/direct")
    Call<ApiResponse<String>> uploadAvatar(
            @Part("userId") RequestBody userId,
            @Part MultipartBody.Part avatarFile
    );
    
    /**
     * 删除头像
     * @param userId 用户ID
     * @return 删除结果
     */
    @POST("avatar/delete/{userId}")
    Call<ApiResponse<String>> deleteAvatar(@retrofit2.http.Path("userId") Long userId);
    
    /**
     * 获取头像信息
     * @param userId 用户ID
     * @return 头像信息
     */
    @POST("avatar/info/{userId}")
    Call<ApiResponse<String>> getAvatarInfo(@retrofit2.http.Path("userId") Long userId);
}