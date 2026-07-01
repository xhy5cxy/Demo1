package com.tour.app.ai;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tour.app.R;
import com.tour.app.ai.adapter.ChatAdapter;
import com.tour.app.ai.model.ChatMessage;
import com.tour.app.ai.service.AIServiceManager;
import com.tour.app.ai.model.AIRecommendRequest;
import com.tour.app.ai.model.AIRecommendResponse;

import java.util.ArrayList;
import java.util.List;

public class AImainActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private EditText etMessage;
    private ImageView btnSend;
    private Button btnNewChat;
    private View tourIcon;
    private AnimatorSet blinkAnimator;
    private AIServiceManager aiServiceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aimain);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initViews();
        initChat();
        setupClickListeners();
        setupKeyboardListener();
    }

    private void initViews() {
        // 找到返回按钮并设置点击事件
        ImageView backButton = findViewById(R.id.iv_back);
        backButton.setOnClickListener(v -> {
            //关闭当前Activity，返回上一页
            finish();
        });

        // 初始化聊天相关视图
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        btnNewChat = findViewById(R.id.btn_new_chat);
        
        // 初始化Tour图标
        tourIcon = findViewById(R.id.tour_icon);
        
        // 初始化AI服务管理器
        aiServiceManager = new AIServiceManager(this);
        
        // 初始化闪烁动画
        initBlinkAnimation();
        
        // 设置EditText输入法优化
        setupEditTextInputMethod();
    }
    
    private void setupEditTextInputMethod() {
        // 确保EditText可以获取焦点
        etMessage.setFocusable(true);
        etMessage.setFocusableInTouchMode(true);
        
        // 设置输入法选项，确保中文输入支持
        etMessage.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_SEND);
        
        // 设置输入法监听器，处理发送按钮点击
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });
        
        // 设置点击监听器，当用户点击输入框时显示软键盘
        etMessage.setOnClickListener(v -> {
            // 请求焦点
            etMessage.requestFocus();
            
            // 显示软键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etMessage, InputMethodManager.SHOW_IMPLICIT);
            }
            
            // 延迟滚动到底部，确保输入框完全可见
            etMessage.postDelayed(() -> scrollToBottom(), 100);
        });
        
        // 设置触摸监听器，当用户触摸输入框时显示软键盘
        etMessage.setOnTouchListener((v, event) -> {
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                // 请求焦点
                etMessage.requestFocus();
                
                // 显示软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(etMessage, InputMethodManager.SHOW_IMPLICIT);
                }
                
                // 延迟滚动到底部，确保输入框完全可见
                etMessage.postDelayed(() -> scrollToBottom(), 100);
            }
            return false;
        });
        
        // 设置输入法监听器，确保中文输入法正常工作
        etMessage.setOnKeyListener((v, keyCode, event) -> {
            // 允许所有按键输入，包括中文输入法
            return false;
        });
        
        // 设置文本变化监听器，确保中文输入正常
        etMessage.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(android.text.Editable s) {
                // 确保中文输入正常处理
                if (s.length() > 0) {
                    // 检查是否包含中文字符
                    String text = s.toString();
                    boolean containsChinese = text.matches(".*[\\u4e00-\\u9fa5]+.*");
                    if (containsChinese) {
                        // 中文输入正常，无需特殊处理
                    }
                }
                
                // 输入内容变化时，确保输入框可见
                if (s.length() > 0) {
                    etMessage.postDelayed(() -> scrollToBottom(), 50);
                }
            }
        });
    }
    
    private void setupKeyboardListener() {
        final View rootView = findViewById(R.id.main);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                
                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                
                // 如果键盘高度大于屏幕高度的15%，则认为键盘已显示
                boolean isKeyboardOpen = keypadHeight > screenHeight * 0.15;
                
                if (isKeyboardOpen) {
                    // 键盘打开时，调整输入框容器的位置，确保输入框可见
                    LinearLayout inputContainer = findViewById(R.id.input_container);
                    ConstraintLayout.LayoutParams inputParams = (ConstraintLayout.LayoutParams) inputContainer.getLayoutParams();
                    inputParams.bottomMargin = keypadHeight;
                    inputContainer.setLayoutParams(inputParams);
                    
                    // 调整RecyclerView的底部边距，确保聊天内容可见
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) chatRecyclerView.getLayoutParams();
                    params.bottomMargin = keypadHeight + inputContainer.getHeight() + 16; // 确保输入框上方有足够空间
                    chatRecyclerView.setLayoutParams(params);
                    
                    // 滚动到底部显示最新消息
                    scrollToBottom();
                } else {
                    // 键盘关闭时，恢复所有视图的位置
                    LinearLayout inputContainer = findViewById(R.id.input_container);
                    ConstraintLayout.LayoutParams inputParams = (ConstraintLayout.LayoutParams) inputContainer.getLayoutParams();
                    inputParams.bottomMargin = 0;
                    inputContainer.setLayoutParams(inputParams);
                    
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) chatRecyclerView.getLayoutParams();
                    params.bottomMargin = 16; // 恢复为正常边距
                    chatRecyclerView.setLayoutParams(params);
                }
            }
        });
    }

    private void initChat() {
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);
        
        // 添加欢迎消息
        addWelcomeMessage();
    }

    private void setupClickListeners() {
        // 新建对话按钮点击事件
        btnNewChat.setOnClickListener(v -> {
            clearChat();
            Toast.makeText(this, "已开始新的对话", Toast.LENGTH_SHORT).show();
        });

        // 发送按钮点击事件
        btnSend.setOnClickListener(v -> {
            sendMessage();
        });
        
        // 软键盘输入法支持：当用户点击键盘上的"发送"按钮时
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void addWelcomeMessage() {
        ChatMessage welcomeMessage = new ChatMessage(
            "您好,欢迎来到e旅行规划师!我将为您设计专属行程,解答旅途中的各类问题,让旅行无忧。有任何想法,请随时告诉我~", 
            false
        );
        messageList.add(welcomeMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "请输入消息内容", Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.length() > 300) {
            Toast.makeText(this, "消息内容不能超过300字", Toast.LENGTH_SHORT).show();
            return;
        }

        // 添加用户消息
        ChatMessage userMessage = new ChatMessage(message, true);
        messageList.add(userMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        
        // 清空输入框
        etMessage.setText("");
        
        // 隐藏软键盘，确保AI回答可见
        hideKeyboard();
        
        // 调用真实AI接口进行回复
        callAIResponse(message);
        
        scrollToBottom();
    }

    private void callAIResponse(String userMessage) {
        // 开始闪烁动画
        startBlinkAnimation();
        
        // 调用AI服务管理器进行智能回复
        aiServiceManager.getChatResponse(userMessage, new AIServiceManager.AICallback<String>() {
            @Override
            public void onSuccess(String response) {
                // 在主线程中更新UI
                runOnUiThread(() -> {
                    ChatMessage aiMessage = new ChatMessage(response, false);
                    messageList.add(aiMessage);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    scrollToBottom();
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                // 在主线程中显示错误信息
                runOnUiThread(() -> {
                    Toast.makeText(AImainActivity.this, "AI服务暂时不可用：" + errorMessage, Toast.LENGTH_SHORT).show();
                    // 使用默认回复
                    String defaultResponse = generateDefaultResponse(userMessage);
                    ChatMessage aiMessage = new ChatMessage(defaultResponse, false);
                    messageList.add(aiMessage);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    scrollToBottom();
                });
            }
        });
    }

    private String generateDefaultResponse(String userMessage) {
        // 简单的关键词匹配回复（作为备用）
        if (userMessage.contains("行程") || userMessage.contains("规划") || userMessage.contains("旅游")) {
            return "我可以帮您规划旅行行程！请告诉我您的目的地、出行时间、预算和兴趣偏好，我将为您定制专属行程。";
        } else if (userMessage.contains("酒店") || userMessage.contains("住宿")) {
            return "我可以为您推荐合适的酒店！请告诉我您的预算范围、入住时间和位置偏好。";
        } else if (userMessage.contains("美食") || userMessage.contains("餐厅")) {
            return "我可以推荐当地特色美食！请告诉我您喜欢的美食类型和预算范围。";
        } else if (userMessage.contains("天气") || userMessage.contains("气候")) {
            return "我可以提供天气信息！请告诉我您要查询的目的地和时间。";
        } else {
            return "感谢您的提问！我是您的旅行助手，可以帮您规划行程、推荐景点、预订服务等。请告诉我您的具体需求。";
        }
    }

    private void clearChat() {
        messageList.clear();
        chatAdapter.notifyDataSetChanged();
        addWelcomeMessage();
    }

    private void scrollToBottom() {
        if (messageList.size() > 0) {
            // 使用平滑滚动，提供更好的用户体验
            int lastPosition = messageList.size() - 1;
            
            // 如果当前已经在底部附近，直接滚动到底部
            LinearLayoutManager layoutManager = (LinearLayoutManager) chatRecyclerView.getLayoutManager();
            if (layoutManager != null) {
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                
                // 如果当前可见的最后一项距离底部不超过3项，使用平滑滚动
                if (lastVisiblePosition >= totalItemCount - 4) {
                    chatRecyclerView.smoothScrollToPosition(lastPosition);
                } else {
                    // 否则直接跳转到底部
                    chatRecyclerView.scrollToPosition(lastPosition);
                }
            } else {
                chatRecyclerView.scrollToPosition(lastPosition);
            }
        }
    }
    
    private void hideKeyboard() {
        // 隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && etMessage != null) {
            imm.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
        }
    }

    private void initBlinkAnimation() {
        // 创建透明度动画：从完全透明到完全不透明，再回到透明
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(tourIcon, "alpha", 1f, 0.2f);
        fadeOut.setDuration(300);
        
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(tourIcon, "alpha", 0.2f, 1f);
        fadeIn.setDuration(300);
        
        // 创建动画集合，循环播放3次
        blinkAnimator = new AnimatorSet();
        blinkAnimator.playSequentially(fadeOut, fadeIn);
        blinkAnimator.setStartDelay(100); // 延迟100ms开始
    }

    private void startBlinkAnimation() {
        if (tourIcon != null && blinkAnimator != null) {
            // 停止之前的动画
            if (blinkAnimator.isRunning()) {
                blinkAnimator.cancel();
            }
            
            // 重置图标透明度
            tourIcon.setAlpha(1f);
            
            // 设置动画重复次数（3次闪烁）
            blinkAnimator.setupStartValues();
            
            // 开始动画
            blinkAnimator.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理动画资源
        if (blinkAnimator != null && blinkAnimator.isRunning()) {
            blinkAnimator.cancel();
        }
    }
}