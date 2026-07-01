package com.tour.app.auth.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tour.app.R;
import com.tour.app.auth.repository.AuthRepository;
import com.tour.app.auth.viewmodel.AuthViewModel;
import com.tour.app.network.ApiClient;

public class AuthTestActivity extends AppCompatActivity {
    
    private EditText etUsername, etPassword, etPhone, etEmail, etNickname;
    private Button btnTestLogin, btnTestRegister;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_test);
        
        initViews();
        setupViewModel();
        setupListeners();
        observeViewModel();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_test_username);
        etPassword = findViewById(R.id.et_test_password);
        etPhone = findViewById(R.id.et_test_phone);
        etEmail = findViewById(R.id.et_test_email);
        etNickname = findViewById(R.id.et_test_nickname);
        
        btnTestLogin = findViewById(R.id.btn_test_login);
        btnTestRegister = findViewById(R.id.btn_test_register);
        
        // 设置测试数据
        etUsername.setText("testuser");
        etPassword.setText("test123");
        etPhone.setText("13800138000");
        etEmail.setText("test@example.com");
        etNickname.setText("测试用户");
    }

    private void setupViewModel() {
        AuthRepository repository = new AuthRepository(this);
        authViewModel = new ViewModelProvider(this, new AuthViewModel.Factory(repository))
                .get(AuthViewModel.class);
    }

    private void setupListeners() {
        btnTestLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                return;
            }
            
            authViewModel.login(username, password);
        });

        btnTestRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String nickname = etNickname.getText().toString().trim();
            
            if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || 
                email.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                return;
            }
            
            authViewModel.register(username, password, phone, email, nickname);
        });
    }

    private void observeViewModel() {
        authViewModel.getLoginResult().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "登录测试成功！", Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getRegisterResult().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "注册测试成功！", Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "测试失败: " + error, Toast.LENGTH_LONG).show();
            }
        });

        authViewModel.getIsLoading().observe(this, isLoading -> {
            btnTestLogin.setEnabled(!isLoading);
            btnTestRegister.setEnabled(!isLoading);
            
            if (isLoading) {
                btnTestLogin.setText("登录中...");
                btnTestRegister.setText("注册中...");
            } else {
                btnTestLogin.setText("测试登录");
                btnTestRegister.setText("测试注册");
            }
        });
    }


}