package com.tour.app.auth.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tour.app.R;
import com.tour.app.MainActivity;
import com.tour.app.auth.ui.LoginActivity;
import com.tour.app.auth.repository.AuthRepository;
import com.tour.app.auth.viewmodel.AuthViewModel;
import com.tour.app.model.User;
import com.tour.app.network.ApiClient;
import com.tour.app.utils.AuthManager;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    
    private EditText etPhone, etPassword, etConfirmPassword, etUsername, etEmail, etNickname;
    private Button btnRegister;
    private ImageView ivBack;
    private TextView tvLogin;
    
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        initViews();
        setupViewModel();
        setupListeners();
        observeViewModel();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etNickname = findViewById(R.id.et_nickname);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_password2);
        btnRegister = findViewById(R.id.btn_register);
        ivBack = findViewById(R.id.iv_back);
        tvLogin = findViewById(R.id.tv_login);
    }

    private void setupViewModel() {
        AuthRepository repository = new AuthRepository(this);
        authViewModel = new ViewModelProvider(this, new AuthViewModel.Factory(repository))
                .get(AuthViewModel.class);
    }

    private void setupListeners() {
        // 返回按钮
        ivBack.setOnClickListener(v -> finish());

        // 注册按钮 - 添加调试日志
        btnRegister.setOnClickListener(v -> {
            Log.d(TAG, "注册按钮被点击了");
            register();
        });

        // 登录链接
        tvLogin.setOnClickListener(v -> finish());
        
        // 添加按钮状态检查
        Log.d(TAG, "注册按钮初始化状态 - 是否可用: " + btnRegister.isEnabled() + ", 是否可见: " + btnRegister.getVisibility());
    }

    private void observeViewModel() {
        authViewModel.getRegisterResult().observe(this, success -> {
            if (success != null && success) {
                Log.d(TAG, "注册成功，准备跳转到登录页面");
                Toast.makeText(this, "注册成功！请登录", Toast.LENGTH_SHORT).show();
                
                // 延迟跳转，确保Toast有足够时间显示
                getWindow().getDecorView().postDelayed(() -> {
                    Log.d(TAG, "开始跳转到LoginActivity");
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }, 1500); // 延迟1.5秒，让用户看到成功消息
            }
        });

        authViewModel.getLoginResult().observe(this, success -> {
            if (success != null && success) {
                // 登录成功，跳转到主页面
                String token = authViewModel.getAuthToken().getValue();
                if (token != null && !token.isEmpty()) {
                    // 创建用户对象用于AuthManager
                    User user = new User();
                    user.setUsername(etUsername.getText().toString().trim());
                    user.setId(1L); // 默认ID
                    
                    AuthManager.getInstance(this).saveAuthInfo(token, user);
                }
                
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
            // 恢复按钮状态
            btnRegister.setEnabled(true);
            btnRegister.setText("注册");
        });

        authViewModel.getIsLoading().observe(this, isLoading -> {
            btnRegister.setEnabled(!isLoading);
            btnRegister.setText(isLoading ? "注册中..." : "注册");
        });
    }

    private void register() {
        String username = etUsername.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 输入验证
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("请输入用户名");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("请输入手机号");
            return;
        }

        if (phone.length() != 11) {
            etPhone.setError("请输入正确的手机号");
            return;
        }

        // 邮箱改为可选，如果填写则验证格式
        if (!TextUtils.isEmpty(email) && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("请输入正确的邮箱格式");
            return;
        }

        // 昵称改为可选，如果未填写则使用用户名作为默认昵称
        if (TextUtils.isEmpty(nickname)) {
            nickname = username;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("请输入密码");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("密码长度至少6位");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("两次输入的密码不一致");
            return;
        }

        // 调用ViewModel进行注册
        authViewModel.register(username, password, phone, email, nickname);
    }

    private void saveLoginState() {
        // 登录状态由AuthManager管理，在自动登录时保存
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnRegister.setEnabled(true);
        btnRegister.setText("注册");
    }
}