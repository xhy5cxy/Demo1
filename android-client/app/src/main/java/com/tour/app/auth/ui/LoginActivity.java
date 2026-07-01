package com.tour.app.auth.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tour.app.App;
import com.tour.app.MainActivity;
import com.tour.app.R;
import com.tour.app.admin.ui.AdminMainActivity;
import com.tour.app.auth.repository.AuthRepository;
import com.tour.app.auth.viewmodel.AuthViewModel;
import com.tour.app.model.User;
import com.tour.app.utils.AuthManager;
import com.tour.app.utils.JWTUtils;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etPhone;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvForgotPassword;
    private ImageView ivBack;
    private ImageView ivWechat;
    private ImageView ivQQ;
    private Switch switchAdminLogin;
    private TextView tvAdminLogin;

    @Inject
    AuthRepository authRepository;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // 使用Dagger进行依赖注入
        ((App) getApplication()).getAppComponent().inject(this);

        initViews();
        setupViewModel();
        setupListeners();
        observeViewModel();
        
        // 检查是否从个人中心跳转过来的管理员登录
        checkAdminLoginIntent();
    }

    /**
     * 检查是否从个人中心跳转过来的管理员登录
     */
    private void checkAdminLoginIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("is_admin_login", false)) {
            // 如果是管理员登录，自动切换到管理员模式
            if (switchAdminLogin != null) {
                switchAdminLogin.setChecked(true);
                // 设置默认的管理员账号密码
                if (etPhone != null) {
                    etPhone.setText("admin");
                }
                if (etPassword != null) {
                    etPassword.setText("123456");
                }
            }
        }
    }
    
    /**
     * 处理管理员登录开关状态变化
     */
    private void handleAdminLoginSwitch(boolean isAdminLogin) {
        if (isAdminLogin) {
            // 切换到管理员登录模式
            if (etPhone != null) {
                etPhone.setText("admin");
            }
            if (etPassword != null) {
                etPassword.setText("123456");
            }
            Toast.makeText(this, "管理员登录模式", Toast.LENGTH_SHORT).show();
        } else {
            // 切换到普通用户登录模式
            if (etPhone != null) {
                etPhone.setText("");
            }
            if (etPassword != null) {
                etPassword.setText("");
            }
            Toast.makeText(this, "普通用户登录模式", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        ivBack = findViewById(R.id.iv_back);
        ivWechat = findViewById(R.id.iv_wechat);
        ivQQ = findViewById(R.id.iv_qq);
        switchAdminLogin = findViewById(R.id.switch_admin_login);
        tvAdminLogin = findViewById(R.id.tv_admin_login);
    }

    private void setupViewModel() {
        authViewModel = new ViewModelProvider(this, new AuthViewModel.Factory(authRepository))
                .get(AuthViewModel.class);
    }

    private void setupListeners() {
        // 返回按钮
        ivBack.setOnClickListener(v -> finish());

        // 登录按钮
        btnLogin.setOnClickListener(v -> login());

        // 注册链接
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // 忘记密码链接
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // 微信登录
        ivWechat.setOnClickListener(v -> {
            Toast.makeText(this, "微信登录", Toast.LENGTH_SHORT).show();
            // TODO: 实现微信登录
        });

        // QQ登录
        ivQQ.setOnClickListener(v -> {
            Toast.makeText(this, "QQ登录", Toast.LENGTH_SHORT).show();
            // TODO: 实现QQ登录
        });

        // 管理员登录开关监听器
        if (switchAdminLogin != null) {
            switchAdminLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleAdminLoginSwitch(isChecked);
                }
            });
        }

        // 管理员登录文本点击事件
        if (tvAdminLogin != null) {
            tvAdminLogin.setOnClickListener(v -> {
                if (switchAdminLogin != null) {
                    boolean newState = !switchAdminLogin.isChecked();
                    switchAdminLogin.setChecked(newState);
                }
            });
        }
    }

    private void observeViewModel() {
        authViewModel.getLoginResult().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                // 登录成功
                saveLoginState();
                
                // 获取token并保存到AuthManager
                String token = authViewModel.getAuthToken().getValue();
                if (token != null && !token.isEmpty()) {
                    // 从JWT token解析用户信息
                    Long userId = JWTUtils.getUserIdFromToken(token);
                    String username = JWTUtils.getUsernameFromToken(token);
                    
                    // 创建用户对象用于AuthManager
                    User user = new User();
                    user.setId(userId != null ? userId : 1L); // 如果解析失败，使用默认ID
                    user.setUsername(username != null ? username : etPhone.getText().toString().trim());
                    user.setPhone(etPhone.getText().toString().trim());
                    
                    Log.d(TAG, "登录成功 - 用户ID: " + user.getId() + ", 用户名: " + user.getUsername());
                    
                    AuthManager.getInstance(this).saveAuthInfo(token, user);
                    
                    // 登录成功后延迟跳转到主页面，避免立即跳转
                    btnLogin.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }, 500); // 延迟500ms
                } else {
                    // token为空，登录失败
                    Toast.makeText(this, "登录失败：无法获取认证信息", Toast.LENGTH_SHORT).show();
                    btnLogin.setEnabled(true);
                    btnLogin.setText("登录");
                }
            }
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
            // 恢复按钮状态
            btnLogin.setEnabled(true);
            btnLogin.setText("登录");
        });

        authViewModel.getIsLoading().observe(this, isLoading -> {
            btnLogin.setEnabled(!isLoading);
            btnLogin.setText(isLoading ? "登录中..." : "登录");
        });
    }

    private void login() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("请输入账号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("请输入密码");
            return;
        }

        // 检查是否为管理员登录
        boolean isAdminLogin = switchAdminLogin != null && switchAdminLogin.isChecked();
        
        if (isAdminLogin) {
            // 管理员登录验证
            if ("admin".equals(phone) && "123456".equals(password)) {
                // 管理员登录成功，跳转到管理员主页面
                Intent intent = new Intent(this, AdminMainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "管理员账号或密码错误", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 普通用户登录
            authViewModel.login(phone, password);
        }
    }

    private void saveLoginState() {
        // 登录状态现在由AuthManager管理，不再需要user_prefs
        // 保留此方法用于兼容性
    }
}