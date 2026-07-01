package com.tour.app.strategy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tour.app.R;
import com.tour.app.strategy.adapter.CommentAdapter;
import com.tour.app.strategy.model.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StrategyDetailActivity extends AppCompatActivity {

    private ImageView ivBack;
    private ImageView ivLike;
    private ImageView ivShare;
    private ImageView ivCollect;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvDestination;
    private TextView tvDays;
    private TextView tvBudget;
    private TextView tvContent;
    private TextView tvLikeCount;
    private TextView tvViewCount;
    private TextView tvCollectCount;
    private TextView tvCommentCount;
    private LinearLayout layoutTags;
    private LinearLayout layoutRecommendations;
    
    // 评论相关组件
    private RecyclerView rvComments;
    private EditText etComment;
    private Button btnSendComment;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    
    private boolean isLiked = false;
    private boolean isCollected = false;
    private int likeCount = 0;
    
    // 随机数据生成器
    private Random random = new Random();
    
    // 随机攻略详情数据
    private String[] randomDetails = {
        "第一天：抵达目的地后，建议先入住酒店休息。下午可以前往当地著名景点，如[景点名称]，感受当地文化氛围。晚上推荐品尝当地特色美食，如[美食名称]。\n\n第二天：早餐后前往[景点名称]，这里有着丰富的历史文化底蕴。中午在该地特色餐厅用餐，下午可以体验[活动名称]，感受当地生活气息。\n\n第三天：上午参观[景点名称]，了解当地历史。中午在[餐厅名称]享用午餐，下午自由活动或购物，晚上准备返程。",
        
        "行程亮点：\n• 深度体验当地文化\n• 品尝地道美食\n• 参观历史古迹\n• 体验特色活动\n\n交通建议：\n• 机场到市区：可乘坐机场大巴或出租车\n• 市内交通：建议使用地铁或公交\n• 景点间交通：可租车或使用打车软件\n\n住宿推荐：\n• 经济型：[酒店名称]，价格实惠，交通便利\n• 舒适型：[酒店名称]，设施完善，服务周到\n• 豪华型：[酒店名称]，环境优美，体验极佳",
        
        "美食攻略：\n1. 早餐：推荐[早餐名称]，当地特色，味道独特\n2. 午餐：建议尝试[午餐名称]，食材新鲜，价格合理\n3. 晚餐：不可错过[晚餐名称]，环境优雅，菜品丰富\n\n购物指南：\n• 特产：当地特产有[特产名称]，适合作为伴手礼\n• 商场：[商场名称]，品牌齐全，购物环境好\n• 夜市：[夜市名称]，小吃丰富，价格实惠\n\n注意事项：\n• 天气：根据季节准备合适衣物\n• 安全：保管好个人财物\n• 语言：建议学习基本当地用语",
        
        "详细行程安排：\n🌅 早晨：建议早起观看日出，感受城市苏醒的美景\n☀️ 上午：参观主要景点，避开人流高峰\n🍽️ 中午：在该地特色餐厅享用午餐\n🌳 下午：可选择公园或博物馆等室内活动\n🌇 傍晚：欣赏日落美景，拍摄照片\n🌃 晚上：体验当地夜生活或品尝夜市美食\n\n预算分配：\n• 交通费用：约占总预算30%\n• 住宿费用：约占总预算40%\n• 餐饮费用：约占总预算20%\n• 门票购物：约占总预算10%",
        
        "摄影攻略：\n📸 最佳拍摄点：[地点名称]，光线充足，背景优美\n📸 拍摄时间：建议在黄金时段（日出后1小时或日落前1小时）\n📸 构图技巧：利用前景增加层次感，注意光线方向\n\n实用贴士：\n💡 必备物品：防晒霜、雨伞、充电宝、常用药品\n💡 网络通讯：建议购买当地SIM卡或使用国际漫游\n💡 货币兑换：可在机场或银行兑换当地货币\n💡 紧急联系：保存当地紧急电话号码"
    };
    
    // 随机标签数据
    private String[] randomTags = {
        "亲子游", "情侣游", "独自旅行", "家庭游", "朋友聚会",
        "摄影之旅", "美食之旅", "文化探索", "自然风光", "城市漫步",
        "历史古迹", "现代建筑", "乡村体验", "海岛度假", "山地徒步"
    };
    
    // 随机推荐景点
    private String[] randomSpots = {
        "[景点名称] - 历史文化遗址，适合拍照打卡",
        "[美食街] - 品尝当地特色小吃的最佳去处",
        "[购物中心] - 品牌齐全，购物环境舒适",
        "[公园] - 休闲散步，感受自然美景",
        "[博物馆] - 了解当地历史文化的好地方",
        "[观景台] - 俯瞰城市全景，视野开阔",
        "[特色街区] - 体验当地生活气息"
    };

    // 随机用户头像
    private int[] userAvatars = {
        R.drawable.ic_user_avatar_1,
        R.drawable.ic_user_avatar_2,
        R.drawable.ic_user_avatar_3,
        R.drawable.ic_user_avatar_4,
        R.drawable.ic_user_avatar_5
    };

    // 随机用户名
    private String[] userNames = {
        "旅行爱好者", "背包客", "摄影达人", "美食家", "文化探索者",
        "户外运动爱好者", "城市漫步者", "历史迷", "自然爱好者", "购物达人"
    };

    // 随机评论内容
    private String[] commentContents = {
        "这个攻略太实用了，按照这个行程玩得很开心！",
        "感谢分享，下次去旅游就参考这个攻略了。",
        "攻略写得很详细，特别是美食推荐部分很棒！",
        "行程安排很合理，不会太赶，体验很好。",
        "照片拍得真漂亮，期待去实地看看。",
        "预算控制得很好，性价比很高。",
        "交通建议很实用，省了不少时间。",
        "住宿推荐很到位，住得很舒服。",
        "景点选择很有特色，避开了人多的旅游团。",
        "攻略很全面，从吃到住到玩都考虑到了。"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_detail);
        
        initViews();
        setupClickListeners();
        initCommentSystem();
        loadStrategyData();
    }
    
    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        ivLike = findViewById(R.id.iv_like);
        ivShare = findViewById(R.id.iv_share);
        ivCollect = findViewById(R.id.iv_collect);
        tvTitle = findViewById(R.id.tv_title);
        tvAuthor = findViewById(R.id.tv_author);
        tvDestination = findViewById(R.id.tv_destination);
        tvDays = findViewById(R.id.tv_days);
        tvBudget = findViewById(R.id.tv_budget);
        tvContent = findViewById(R.id.tv_content);
        tvLikeCount = findViewById(R.id.tv_like_count);
        tvViewCount = findViewById(R.id.tv_view_count);
        tvCollectCount = findViewById(R.id.tv_collect_count);
        tvCommentCount = findViewById(R.id.tv_comment_count);
        layoutTags = findViewById(R.id.layout_tags);
        layoutRecommendations = findViewById(R.id.layout_recommendations);
        
        // 评论相关组件
        rvComments = findViewById(R.id.rv_comments);
        etComment = findViewById(R.id.et_comment);
        btnSendComment = findViewById(R.id.btn_send_comment);
    }
    
    private void initCommentSystem() {
        // 初始化评论列表
        commentList = new ArrayList<>();
        
        // 设置RecyclerView
        commentAdapter = new CommentAdapter(commentList);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setAdapter(commentAdapter);
        
        // 生成一些随机评论作为示例
        generateRandomComments();
    }
    
    private void generateRandomComments() {
        // 随机生成3-8条评论
        int commentCount = 3 + random.nextInt(6);
        for (int i = 0; i < commentCount; i++) {
            Comment comment = new Comment(
                i + 1,
                1, // 假设当前攻略ID为1
                userAvatars[random.nextInt(userAvatars.length)],
                userNames[random.nextInt(userNames.length)],
                commentContents[random.nextInt(commentContents.length)],
                System.currentTimeMillis() - random.nextInt(7 * 24 * 60 * 60 * 1000), // 随机时间（7天内）
                random.nextInt(50) // 随机点赞数
            );
            commentList.add(comment);
        }
        
        // 更新评论数量显示
        updateCommentCount();
    }
    
    private void updateCommentCount() {
        tvCommentCount.setText("(" + commentList.size() + ")");
    }
    
    private void setupClickListeners() {
        // 返回按钮
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        // 点赞按钮
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLike();
            }
        });
        
        // 收藏按钮
        ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCollect();
            }
        });
        
        // 分享按钮
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareStrategy();
            }
        });
        
        // 发送评论按钮
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }
    
    private void sendComment() {
        String commentText = etComment.getText().toString().trim();
        
        if (commentText.isEmpty()) {
            Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // 创建新评论
        Comment newComment = new Comment(
            commentList.size() + 1,
            1, // 当前攻略ID
            R.drawable.ic_user_avatar_1, // 当前用户头像
            "当前用户", // 当前用户名
            commentText,
            System.currentTimeMillis(),
            0 // 初始点赞数为0
        );
        
        // 添加到评论列表
        commentList.add(0, newComment); // 添加到顶部
        commentAdapter.notifyItemInserted(0);
        
        // 清空输入框
        etComment.setText("");
        
        // 更新评论数量
        updateCommentCount();
        
        // 滚动到顶部显示新评论
        rvComments.scrollToPosition(0);
        
        Toast.makeText(this, "评论发表成功", Toast.LENGTH_SHORT).show();
    }
    
    private void loadStrategyData() {
        Intent intent = getIntent();
        
        // 获取传递过来的攻略数据
        String title = intent.getStringExtra("strategy_title");
        String content = intent.getStringExtra("strategy_content");
        String destination = intent.getStringExtra("strategy_destination");
        int days = intent.getIntExtra("strategy_days", 3);
        String budget = intent.getStringExtra("strategy_budget");
        int likeCount = intent.getIntExtra("strategy_like_count", 0);
        int viewCount = intent.getIntExtra("strategy_view_count", 0);
        
        // 设置基础数据
        tvTitle.setText(title != null ? title : "未命名攻略");
        tvDestination.setText(destination != null ? destination : "未知目的地");
        tvDays.setText(days + "天");
        tvBudget.setText(budget != null ? budget : "预算待定");
        
        // 随机生成详细攻略内容
        String randomDetail = randomDetails[random.nextInt(randomDetails.length)];
        tvContent.setText((content != null ? content : "暂无攻略内容") + "\n\n" + randomDetail);
        
        // 随机生成作者信息
        String[] authors = {"旅行达人", "背包客小张", "摄影爱好者", "美食探索家", "文化体验者"};
        String randomAuthor = authors[random.nextInt(authors.length)];
        tvAuthor.setText("作者：" + randomAuthor);
        
        // 设置互动数据
        tvLikeCount.setText(String.valueOf(likeCount + random.nextInt(100)));
        tvViewCount.setText(String.valueOf(viewCount + random.nextInt(500)));
        tvCollectCount.setText(String.valueOf(random.nextInt(50)));
        
        // 生成随机标签
        generateRandomTags();
        
        // 生成随机推荐景点
        generateRandomRecommendations();
    }
    
    private void generateRandomTags() {
        layoutTags.removeAllViews();
        
        // 随机选择3-5个标签
        int tagCount = 3 + random.nextInt(3);
        for (int i = 0; i < tagCount; i++) {
            TextView tagView = new TextView(this);
            tagView.setText(randomTags[random.nextInt(randomTags.length)]);
            tagView.setPadding(16, 8, 16, 8);
            tagView.setTextSize(12);
            tagView.setBackgroundResource(R.drawable.tag_hot_bg);
            tagView.setTextColor(getResources().getColor(R.color.white));
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 8, 8);
            tagView.setLayoutParams(params);
            
            layoutTags.addView(tagView);
        }
    }
    
    private void generateRandomRecommendations() {
        layoutRecommendations.removeAllViews();
        
        // 随机选择2-4个推荐景点
        int spotCount = 2 + random.nextInt(3);
        for (int i = 0; i < spotCount; i++) {
            TextView spotView = new TextView(this);
            spotView.setText("• " + randomSpots[random.nextInt(randomSpots.length)]);
            spotView.setPadding(0, 4, 0, 4);
            spotView.setTextSize(14);
            spotView.setTextColor(getResources().getColor(R.color.text_secondary));
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            spotView.setLayoutParams(params);
            
            layoutRecommendations.addView(spotView);
        }
    }
    
    private void toggleLike() {
        isLiked = !isLiked;
        if (isLiked) {
            likeCount++;
            Toast.makeText(this, "点赞成功！", Toast.LENGTH_SHORT).show();
            ivLike.setImageResource(R.drawable.ic_like);
        } else {
            likeCount--;
            Toast.makeText(this, "取消点赞", Toast.LENGTH_SHORT).show();
            ivLike.setImageResource(R.drawable.ic_like);
        }
        tvLikeCount.setText(String.valueOf(likeCount));
    }

    private void toggleCollect() {
        isCollected = !isCollected;
        if (isCollected) {
            Toast.makeText(this, "收藏成功！", Toast.LENGTH_SHORT).show();
            ivCollect.setImageResource(R.drawable.ic_collection);
        } else {
            Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
            ivCollect.setImageResource(R.drawable.ic_collection);
        }
    }
    
    private void shareStrategy() {
        String shareText = "分享攻略：" + tvTitle.getText().toString() + " - " + tvContent.getText().toString().substring(0, Math.min(50, tvContent.getText().toString().length())) + "...";
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        
        try {
            startActivity(Intent.createChooser(shareIntent, "分享攻略"));
        } catch (Exception e) {
            Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
        }
    }
}