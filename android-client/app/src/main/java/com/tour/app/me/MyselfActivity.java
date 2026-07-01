package com.tour.app.me;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.tour.app.R;
import com.tour.app.auth.ui.LoginActivity;
import com.tour.app.common.BaseActivity;
import com.tour.app.common.BottomNavigationManager;
import com.tour.app.createtrip.CreateTripActivity;
import com.tour.app.model.User;
import com.tour.app.network.ApiClient;
import com.tour.app.network.ApiResponse;
import com.tour.app.network.UserService;
import com.tour.app.utils.AuthManager;
import com.tour.app.utils.AvatarCacheManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyselfActivity extends BaseActivity {

    private LinearLayout btnLogout;
    private LinearLayout btnAdminLogin;
    private TextView tvUsername;
    private TextView tvUserLevel;
    private ImageView ivSettings;
    private ImageView ivAvatar;
    
    // 权限请求码
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_GALLERY_PERMISSION = 101;
    
    // 请求码
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    
    // 拍照文件路径
    private Uri photoUri;
    
    // 用户服务
    private UserService userService;
    private AvatarCacheManager avatarCacheManager;
    
    @Override
    protected int getLayoutId() {
        return R.layout.activity_myself;
    }

    @Override
    protected void initViews() {
        // 初始化退出登录按钮
        btnLogout = findViewById(R.id.btn_logout);
        btnAdminLogin = findViewById(R.id.btn_admin_login);
        tvUsername = findViewById(R.id.tv_username);
        tvUserLevel = findViewById(R.id.tv_user_level);
        ivSettings = findViewById(R.id.iv_settings);
        ivAvatar = findViewById(R.id.iv_avatar);
        
        // 初始化用户服务
        userService = ApiClient.getUserService(this);
        
        // 初始化头像缓存管理器
        avatarCacheManager = new AvatarCacheManager(this);
        
        // 设置退出登录按钮点击事件
        if (btnLogout != null) {
            btnLogout.setOnClickListener(this::onLogoutClick);
        }
        
        // 设置管理员登录按钮点击事件
        if (btnAdminLogin != null) {
            btnAdminLogin.setOnClickListener(this::onAdminLoginClick);
        }
        
        // 设置设置图标点击事件
        if (ivSettings != null) {
            ivSettings.setOnClickListener(this::onSettingsClick);
        }
        
        // 设置头像点击事件
        if (ivAvatar != null) {
            ivAvatar.setOnClickListener(this::onAvatarClick);
        }
        
        // 设置底部导航栏加号按钮点击事件
        android.widget.FrameLayout btnAdd = findViewById(R.id.btn_add);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(this::onAddClick);
        }
        
        // 加载用户信息
        loadUserInfo();
    }

    @Override
    protected void initData() {
        // 可以在这里加载用户数据
    }
    
    /**
     * 头像点击事件
     */
    private void onAvatarClick(View view) {
        // 显示头像修改选项对话框
        showAvatarOptionsDialog();
    }
    
    /**
     * 显示头像修改选项对话框
     */
    private void showAvatarOptionsDialog() {
        String[] options = {"拍照", "从相册选择", "取消"};
        
        new AlertDialog.Builder(this)
                .setTitle("修改头像")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // 拍照
                            takePhoto();
                            break;
                        case 1: // 从相册选择
                            chooseFromGallery();
                            break;
                        case 2: // 取消
                            dialog.dismiss();
                            break;
                    }
                })
                .show();
    }
    
    /**
     * 拍照功能
     */
    private void takePhoto() {
        // 检查相机权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.CAMERA}, 
                    REQUEST_CAMERA_PERMISSION);
            return;
        }
        
        // 检查存储权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 
                    REQUEST_CAMERA_PERMISSION);
            return;
        }
        
        // 启动相机
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // 创建文件
            File photoFile = createImageFile();
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    
    /**
     * 从相册选择
     */
    private void chooseFromGallery() {
        // 检查存储权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 
                    REQUEST_GALLERY_PERMISSION);
            return;
        }
        
        // 启动相册选择
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }
    
    /**
     * 创建图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        
        try {
            File image = File.createTempFile(
                    imageFileName,  /* 前缀 */
                    ".jpg",         /* 后缀 */
                    storageDir      /* 目录 */
            );
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 处理权限请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQUEST_CAMERA_PERMISSION:
                    takePhoto();
                    break;
                case REQUEST_GALLERY_PERMISSION:
                    chooseFromGallery();
                    break;
            }
        } else {
            Toast.makeText(this, "权限被拒绝，无法使用该功能", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 处理活动结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    // 处理拍照结果
                    if (photoUri != null) {
                        handleImageResult(photoUri);
                    }
                    break;
                case REQUEST_IMAGE_PICK:
                    // 处理相册选择结果
                    if (data != null && data.getData() != null) {
                        handleImageResult(data.getData());
                    }
                    break;
            }
        }
    }
    
    /**
     * 处理图片结果
     */
    private void handleImageResult(Uri imageUri) {
        // 显示选择的图片
        ivAvatar.setImageURI(imageUri);
        
        // 上传图片到服务器
        uploadAvatarToServer(imageUri);
    }
    
    /**
     * 上传头像到服务器
     */
    private void uploadAvatarToServer(Uri imageUri) {
        try {
            // 获取当前用户ID
            AuthManager authManager = AuthManager.getInstance(this);
            User currentUser = authManager.getCurrentUser();
            
            if (currentUser == null || currentUser.getId() == null) {
                Toast.makeText(this, "用户未登录，无法上传头像", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // 获取文件路径
            String filePath = getRealPathFromURI(imageUri);
            if (filePath == null) {
                Toast.makeText(this, "无法获取图片文件", Toast.LENGTH_SHORT).show();
                return;
            }
            
            File file = new File(filePath);
            if (!file.exists()) {
                Toast.makeText(this, "图片文件不存在", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // 创建请求体
            RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), 
                    String.valueOf(currentUser.getId()));
            
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part avatarFile = MultipartBody.Part.createFormData("avatarFile", 
                    file.getName(), requestFile);
            
            // 显示上传进度
            Toast.makeText(this, "正在上传头像...", Toast.LENGTH_SHORT).show();
            
            // 调用上传接口
            Call<ApiResponse<String>> call = userService.uploadAvatar(userIdBody, avatarFile);
            call.enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<String> apiResponse = response.body();
                        if (apiResponse.getCode() == 200) {
                            // 上传成功
                            Toast.makeText(MyselfActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                            
                            // 更新本地用户信息中的头像URL
                            String avatarUrl = apiResponse.getData();
                            if (avatarUrl != null) {
                                currentUser.setAvatar(avatarUrl);
                                authManager.updateUser(currentUser);
                            }
                        } else {
                            // 上传失败
                            Toast.makeText(MyselfActivity.this, 
                                    "头像上传失败: " + apiResponse.getMessage(), 
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 网络请求失败
                        Toast.makeText(MyselfActivity.this, 
                                "网络请求失败，请检查网络连接", 
                                Toast.LENGTH_SHORT).show();
                    }
                }
                
                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    // 网络错误
                    Toast.makeText(MyselfActivity.this, 
                            "网络错误: " + t.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                }
            });
            
        } catch (Exception e) {
            Toast.makeText(this, "上传失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 从URI获取真实文件路径
     */
    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        android.database.Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }
    
    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        AuthManager authManager = AuthManager.getInstance(this);
        User currentUser = authManager.getCurrentUser();
        
        if (currentUser != null) {
            // 设置用户名
            if (tvUsername != null) {
                String username = currentUser.getUsername();
                if (username != null && !username.isEmpty()) {
                    tvUsername.setText(username);
                } else if (currentUser.getPhone() != null && !currentUser.getPhone().isEmpty()) {
                    tvUsername.setText(currentUser.getPhone());
                }
            }
            
            // 设置用户等级（可以根据用户ID或其他逻辑计算）
            if (tvUserLevel != null) {
                if (currentUser.getId() != null) {
                    // 简单的等级计算逻辑
                    long userId = currentUser.getId();
                    if (userId <= 10) {
                        tvUserLevel.setText("Lv.1 新手旅行者");
                    } else if (userId <= 50) {
                        tvUserLevel.setText("Lv.2 初级旅行者");
                    } else if (userId <= 100) {
                        tvUserLevel.setText("Lv.3 中级旅行者");
                    } else if (userId <= 200) {
                        tvUserLevel.setText("Lv.4 高级旅行者");
                    } else {
                        tvUserLevel.setText("Lv.5 旅行达人");
                    }
                } else {
                    tvUserLevel.setText("Lv.1 新手旅行者");
                }
            }
            
            // 加载头像
            loadUserAvatar(currentUser);
        }
    }
    
    /**
     * 加载用户头像
     */
    private void loadUserAvatar(User user) {
        if (ivAvatar == null || user == null) {
            return;
        }
        
        // 首先尝试从缓存加载
        if (avatarCacheManager.hasAvatarInCache(user.getId())) {
            String cachePath = avatarCacheManager.getAvatarCachePath(user.getId());
            if (cachePath != null) {
                File cacheFile = new File(cachePath);
                if (cacheFile.exists()) {
                    ivAvatar.setImageURI(Uri.fromFile(cacheFile));
                    return;
                }
            }
        }
        
        // 检查用户是否有头像URL
        String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            // 使用图片加载库加载头像（这里使用Glide或Picasso）
            // 由于项目中没有引入图片加载库，这里使用简单的URI加载
            try {
                // 如果是网络URL，需要特殊处理
                if (avatarUrl.startsWith("http")) {
                    // 这里可以添加网络图片加载逻辑
                    // 暂时使用默认头像
                    ivAvatar.setImageResource(R.drawable.ic_user);
                } else {
                    // 如果是本地路径，直接设置
                    File avatarFile = new File(avatarUrl);
                    if (avatarFile.exists()) {
                        ivAvatar.setImageURI(Uri.fromFile(avatarFile));
                        // 将头像保存到缓存
                        saveAvatarToCache(user.getId(), avatarFile);
                    } else {
                        // 文件不存在，使用默认头像
                        ivAvatar.setImageResource(R.drawable.ic_user);
                    }
                }
            } catch (Exception e) {
                // 加载失败，使用默认头像
                ivAvatar.setImageResource(R.drawable.ic_user);
            }
        } else {
            // 没有头像，使用默认头像
            ivAvatar.setImageResource(R.drawable.ic_user);
        }
    }
    
    /**
     * 将头像文件保存到缓存
     */
    private void saveAvatarToCache(Long userId, File avatarFile) {
        if (userId == null || avatarFile == null || !avatarFile.exists()) {
            return;
        }
        
        try {
            // 读取文件为Bitmap
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(avatarFile.getAbsolutePath());
            if (bitmap != null) {
                // 保存到缓存
                avatarCacheManager.saveAvatarToCache(userId, bitmap);
            }
        } catch (Exception e) {
            android.util.Log.e("MyselfActivity", "Failed to save avatar to cache", e);
        }
    }
    
    /**
     * 管理员登录按钮点击事件
     */
    private void onAdminLoginClick(View view) {
        // 显示管理员登录确认对话框
        showAdminLoginConfirmationDialog();
    }
    
    /**
     * 显示管理员登录确认对话框
     */
    private void showAdminLoginConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("管理员登录")
                .setMessage("确定要进入管理员页面吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    // 用户确认，跳转到管理员登录页面
                    redirectToAdminLogin();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    // 用户取消，不做任何操作
                    dialog.dismiss();
                })
                .show();
    }
    
    /**
     * 跳转到管理员登录页面
     */
    private void redirectToAdminLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("is_admin_login", true);
        startActivity(intent);
    }
    
    /**
     * 设置图标点击事件
     */
    private void onSettingsClick(View view) {
        // 跳转到设置页面
        Intent intent = new Intent(MyselfActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
    
    /**
     * 退出登录按钮点击事件
     */
    private void onLogoutClick(View view) {
        // 显示确认对话框
        showLogoutConfirmationDialog();
    }
    
    /**
     * 显示退出登录确认对话框
     */
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("退出登录")
                .setMessage("确定要退出登录吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    // 用户确认退出，执行退出登录逻辑
                    performLogout();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    // 用户取消，不做任何操作
                    dialog.dismiss();
                })
                .show();
    }
    
    /**
     * 执行退出登录逻辑
     */
    private void performLogout() {
        // 清除认证信息
        AuthManager authManager = AuthManager.getInstance(this);
        authManager.clearAuthInfo();
        
        // 显示退出成功提示
        Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show();
        
        // 跳转到登录页面
        redirectToLogin();
    }
    
    /**
     * 跳转到登录页面
     */
    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void setCurrentNavigationItem() {
        // 设置我的按钮为选中状态
        if (bottomNavigationManager != null) {
            bottomNavigationManager.setSelectedItem(R.id.btn_me);
        }
    }
    
    /**
     * 底部导航栏加号按钮点击事件
     */
    private void onAddClick(View view) {
        // 跳转到创建行程页面
        Intent intent = new Intent(MyselfActivity.this, CreateTripActivity.class);
        startActivity(intent);
    }
}