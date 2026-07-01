package com.tour.app.ai.test;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tour.app.ai.modelscope.ModelScopeClient;
import com.tour.app.ai.service.AIServiceManager;

import com.tour.app.R;

/**
 * ModelScope接口测试Activity
 */
public class ModelScopeTestActivity extends AppCompatActivity {
    private static final String TAG = "ModelScopeTest";
    
    private EditText etApiKey;
    private EditText etTestMessage;
    private Button btnTestConnection;
    private Button btnSendMessage;
    private TextView tvResult;
    
    private AIServiceManager aiServiceManager;
    private ModelScopeClient modelScopeClient;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_scope_test);
        
        initViews();
        initAIService();
    }
    
    private void initViews() {
        etApiKey = findViewById(R.id.et_api_key);
        etTestMessage = findViewById(R.id.et_test_message);
        btnTestConnection = findViewById(R.id.btn_test_connection);
        btnSendMessage = findViewById(R.id.btn_send_message);
        tvResult = findViewById(R.id.tv_result);
        
        // 设置默认测试消息
        etTestMessage.setText("你好，请介绍一下北京的著名景点");
        
        btnTestConnection.setOnClickListener(v -> testConnection());
        btnSendMessage.setOnClickListener(v -> sendMessage());
    }
    
    private void initAIService() {
        aiServiceManager = new AIServiceManager(this);
        modelScopeClient = new ModelScopeClient(this);
        
        // 设置使用ModelScope服务
        aiServiceManager.setAIServiceType(AIServiceManager.AIServiceType.MODEL_SCOPE);
    }
    
    private void testConnection() {
        tvResult.setText("正在测试连接...");
        
        modelScopeClient.testConnection(new ModelScopeClient.ModelScopeCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    tvResult.setText("✅ 连接测试成功！\n\nAI回复：" + response);
                    Log.d(TAG, "ModelScope连接测试成功：" + response);
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    tvResult.setText("❌ 连接测试失败：" + error);
                    Log.e(TAG, "ModelScope连接测试失败：" + error);
                });
            }
        });
    }
    
    private void sendMessage() {
        String message = etTestMessage.getText().toString().trim();
        if (message.isEmpty()) {
            tvResult.setText("请输入测试消息");
            return;
        }
        
        tvResult.setText("正在发送消息...");
        
        aiServiceManager.getChatResponse(message, new AIServiceManager.AICallback<String>() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    tvResult.setText("✅ AI回复成功！\n\n回复内容：" + response);
                    Log.d(TAG, "AI回复成功：" + response);
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    tvResult.setText("❌ 发送消息失败：" + error);
                    Log.e(TAG, "发送消息失败：" + error);
                });
            }
        });
    }
}