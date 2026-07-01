package com.tour.app.auth.ui;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tour.app.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForgotPasswordActivity extends AppCompatActivity {
//   主要方法：
//- initViews() - 初始化视图组件
//- initListeners() - 设置点击监听器
//- isValidPhone() - 验证手机号格式
//- sendVerificationCode() - 发送验证码
//- startCountDown() - 开始倒计时
//- validateResetInput() - 验证重置密码输入
//- performResetPassword() - 执行重置密码
//- isValidResetCode() - 验证重置验证码
    private EditText etPhone;
    private EditText etVerificationCode;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnSendCode;
    private Button btnResetPassword;
    private ImageView ivBack;

    private CountDownTimer countDownTimer;
    private boolean isCodeSent = false;

    // 线程池和Handler
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        
        // 延迟初始化，减少主线程负担
        mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            // 使用固定大小的线程池，减少资源消耗
            executorService = Executors.newFixedThreadPool(2);
            initViews();
            initListeners();
        });
    }

    private void initViews() {
        etPhone = findViewById(R.id.et_phone);
        etVerificationCode = findViewById(R.id.et_verification_code);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSendCode = findViewById(R.id.btn_send_code);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        ivBack = findViewById(R.id.iv_back);
        
        // 调试信息
        Log.d(TAG, "initViews called");
        if (ivBack != null) {
            Log.d(TAG, "返回按钮找到，ID: " + ivBack.getId());
        } else {
            Log.e(TAG, "返回按钮为null");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {
        // 返回按钮
        if (ivBack != null) {
            Log.d(TAG, "设置返回按钮监听器");
            
            // 添加触摸监听器作为备用
            ivBack.setOnTouchListener((v, event) -> {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "返回按钮被触摸");
                    Toast.makeText(this, "返回按钮被触摸", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });
            
            // 设置点击监听器
            ivBack.setOnClickListener(v -> {
                Log.d(TAG, "返回按钮被点击");
                Toast.makeText(this, "正在返回...", Toast.LENGTH_SHORT).show();
                // 添加一个简单的延迟来测试响应性
                v.postDelayed(() -> {
                    finish(); // 返回登录页面
                }, 100);
            });
            
            Log.d(TAG, "返回按钮监听器设置完成");
        } else {
            Log.e(TAG, "返回按钮为null，无法设置监听器");
        }

        // 发送验证码按钮
        if (btnSendCode != null) {
            btnSendCode.setOnClickListener(v -> {
                String phone = etPhone.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (!isValidPhone(phone)) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                sendVerificationCode(phone);
            });
        }

        // 重置密码按钮
        if (btnResetPassword != null) {
            btnResetPassword.setOnClickListener(v -> {
                String phone = etPhone.getText().toString().trim();
                String code = etVerificationCode.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                
                if (!validateResetInput(phone, code, newPassword, confirmPassword)) {
                    return;
                }
                
                performResetPassword(phone, code, newPassword);
            });
        }
    }

    /**
     * 验证手机号格式
     */
    private boolean isValidPhone(String phone) {
        return phone.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 发送验证码
     */
    private void sendVerificationCode(String phone) {
        // 显示发送中提示
        Toast.makeText(this, "正在发送验证码...", Toast.LENGTH_SHORT).show();
        
        // 在子线程中执行发送验证码逻辑
        executorService.execute(() -> {
            // 模拟网络请求延迟
            try {
                Thread.sleep(800); // 模拟800ms的网络请求
            } catch (InterruptedException e) {
                Log.e(TAG, "发送验证码过程被中断", e);
            }
            
            // 回到主线程更新UI
            mainHandler.post(() -> {
                // 模拟发送验证码（实际应用中应该调用服务器API）
                isCodeSent = true;
                
                // 开始倒计时
                startCountDown();
                
                Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
            });
        });
    }

    /**
     * 开始倒计时
     */
    private void startCountDown() {
        if (btnSendCode != null) {
            btnSendCode.setEnabled(false);
            btnSendCode.setText("60s");
            
            countDownTimer = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    btnSendCode.setText((millisUntilFinished / 1000) + "s");
                }

                @Override
                public void onFinish() {
                    btnSendCode.setEnabled(true);
                    btnSendCode.setText("发送验证码");
                    isCodeSent = false;
                }
            }.start();
        }
    }

    /**
     * 验证重置密码输入
     */
    private boolean validateResetInput(String phone, String code, String newPassword, String confirmPassword) {
        if (phone.isEmpty()) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (!isValidPhone(phone)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (code.isEmpty()) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (code.length() != 6) {
            Toast.makeText(this, "验证码格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (newPassword.isEmpty()) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (newPassword.length() < 6) {
            Toast.makeText(this, "密码长度不能少于6位", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "请确认新密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }

    /**
     * 执行重置密码
     */
    private void performResetPassword(String phone, String code, String newPassword) {
        // 显示重置中提示
        Toast.makeText(this, "正在重置密码...", Toast.LENGTH_SHORT).show();
        
        // 在子线程中执行重置密码逻辑
        executorService.execute(() -> {
            // 模拟网络请求延迟
            try {
                Thread.sleep(1200); // 模拟1.2秒的网络请求
            } catch (InterruptedException e) {
                Log.e(TAG, "重置密码过程被中断", e);
            }
            
            // 在子线程中验证重置验证码
            boolean isSuccess = isValidResetCode(code);
            
            // 回到主线程更新UI
            mainHandler.post(() -> {
                if (isSuccess) {
                    // 重置成功
                    Toast.makeText(this, "密码重置成功！", Toast.LENGTH_SHORT).show();
                    
                    // 延迟一下再返回登录页面，让用户看到成功提示
                    if (btnResetPassword != null) {
                        btnResetPassword.postDelayed(() -> {
                            finish(); // 返回登录页面
                        }, 1500); // 1.5秒后返回
                    }
                } else {
                    // 重置失败
                    Toast.makeText(this, "验证码错误或已过期", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    /**
     * 验证重置验证码
     * 这里可以替换为实际的验证逻辑
     */
    private boolean isValidResetCode(String code) {
        // 简单的验证逻辑，实际应用中应该调用服务器API验证
        // 这里为了演示，只要验证码是6位数字就认为有效
        return code.matches("\\d{6}");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭线程池
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        // 取消倒计时
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "系统返回键被按下");
        Toast.makeText(this, "正在返回...", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
} 