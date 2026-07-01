package com.tour.app.strategy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tour.app.R;
import com.tour.app.common.BaseActivity;
import com.tour.app.data.StrategyDataManager;
import com.tour.app.model.Strategy;
import com.tour.app.model.User;
import com.tour.app.network.ApiClient;
import com.tour.app.network.ApiResponse;
import com.tour.app.network.StrategyService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateStrategyActivity extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    
    private EditText etTitle, etDestination, etDays, etBudget, etContent;
    private Button btnPublish, btnUploadCover;
    private ImageView ivBack, ivCover;
    private String coverImagePath;
    private StrategyService strategyService;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_strategy;
    }

    @Override
    protected void initViews() {
        etTitle = findViewById(R.id.et_title);
        etDestination = findViewById(R.id.et_destination);
        etDays = findViewById(R.id.et_days);
        etBudget = findViewById(R.id.et_budget);
        etContent = findViewById(R.id.et_content);
        btnPublish = findViewById(R.id.btn_publish);
        btnUploadCover = findViewById(R.id.btn_upload_cover);
        ivBack = findViewById(R.id.iv_back);
        ivCover = findViewById(R.id.iv_cover);

        // 初始化网络服务
        strategyService = ApiClient.getStrategyService(this);
        
        setupClickListeners();
    }

    @Override
    protected void initData() {
        // 初始化数据
    }

    private void setupClickListeners() {
        // 返回按钮
        if (ivBack != null) {
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        // 上传封面按钮
        if (btnUploadCover != null) {
            btnUploadCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCoverImage();
                }
            });
        }

        // 发布按钮
        if (btnPublish != null) {
            btnPublish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publishStrategy();
                }
            });
        }
    }

    /**
     * 选择封面图片
     */
    private void selectCoverImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                // 显示选中的图片
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ivCover.setImageBitmap(bitmap);
                
                // 保存图片路径
                coverImagePath = getRealPathFromURI(selectedImageUri);
                
                Toast.makeText(this, "图片选择成功", Toast.LENGTH_SHORT).show();
                
            } catch (Exception e) {
                Toast.makeText(this, "图片选择失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    /**
     * 从URI获取真实文件路径
     */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return contentUri.getPath();
    }

    private void publishStrategy() {
        String title = etTitle.getText().toString().trim();
        String destination = etDestination.getText().toString().trim();
        String days = etDays.getText().toString().trim();
        String budget = etBudget.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        // 验证输入
        if (title.isEmpty()) {
            Toast.makeText(this, "请输入攻略标题", Toast.LENGTH_SHORT).show();
            return;
        }

        if (destination.isEmpty()) {
            Toast.makeText(this, "请输入目的地", Toast.LENGTH_SHORT).show();
            return;
        }

        if (days.isEmpty()) {
            Toast.makeText(this, "请输入行程天数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            Toast.makeText(this, "请输入攻略内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建攻略对象
        Strategy strategy = new Strategy();
        strategy.setTitle(title);
        strategy.setDestination(destination);
        strategy.setContent(content);
        
        // 设置默认用户信息（模拟当前用户）
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setNickname("旅行达人");
        strategy.setAuthor(currentUser);
        strategy.setUserId(1L);
        
        // 设置默认数据
        strategy.setViewCount(0);
        strategy.setLikeCount(0);
        strategy.setCollectCount(0);
        strategy.setStatus(1); // 已通过状态
        
        // 如果有封面图片，先上传图片
        if (coverImagePath != null && !coverImagePath.isEmpty()) {
            uploadCoverImageAndSaveStrategy(strategy);
        } else {
            // 没有封面图片，直接保存攻略
            saveStrategy(strategy);
        }
    }

    /**
     * 上传封面图片并保存攻略
     */
    private void uploadCoverImageAndSaveStrategy(Strategy strategy) {
        Toast.makeText(this, "正在上传封面图片...", Toast.LENGTH_SHORT).show();
        
        try {
            File imageFile = new File(coverImagePath);
            if (!imageFile.exists()) {
                Toast.makeText(this, "图片文件不存在", Toast.LENGTH_SHORT).show();
                return;
            }

            // 创建请求体
            RequestBody strategyIdBody = RequestBody.create(MediaType.parse("text/plain"), 
                    String.valueOf(strategy.getId() != null ? strategy.getId() : 0));
            
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("imageFile", 
                    imageFile.getName(), requestFile);

            // 调用上传接口
            Call<ApiResponse<String>> call = strategyService.uploadStrategyImage(strategyIdBody, imagePart);
            call.enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<String> apiResponse = response.body();
                        if (apiResponse.getCode() == 200) {
                            // 上传成功，设置封面图片URL
                            String imageUrl = apiResponse.getData();
                            strategy.setCoverImage(imageUrl);
                            saveStrategy(strategy);
                        } else {
                            Toast.makeText(CreateStrategyActivity.this, 
                                    "图片上传失败: " + apiResponse.getMessage(), 
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CreateStrategyActivity.this, 
                                "网络请求失败", 
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    Toast.makeText(CreateStrategyActivity.this, 
                            "网络连接失败，请检查网络设置", 
                            Toast.LENGTH_SHORT).show();
                }
            });
            
        } catch (Exception e) {
            Toast.makeText(this, "图片上传失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 保存攻略数据
     */
    private void saveStrategy(Strategy strategy) {
        StrategyDataManager dataManager = StrategyDataManager.getInstance(this);
        dataManager.saveStrategy(strategy);

        // 发布成功
        Toast.makeText(this, "攻略发布成功！", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}