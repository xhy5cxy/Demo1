package com.tour.app.createtrip;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tour.app.R;
import com.tour.app.common.BaseActivity;

public class CreateTripActivity extends BaseActivity {

    private EditText etTripName, etDestination, etStartDate, etEndDate, etBudget, etPeople, etDescription;
    private Button btnCreateTrip;
    private ImageView ivBack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_trip;
    }

    @Override
    protected void initViews() {
        etTripName = findViewById(R.id.et_trip_name);
        etDestination = findViewById(R.id.et_destination);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        etBudget = findViewById(R.id.et_budget);
        etPeople = findViewById(R.id.et_people);
        etDescription = findViewById(R.id.et_description);
        btnCreateTrip = findViewById(R.id.btn_create_trip);
        ivBack = findViewById(R.id.iv_back);

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

        // 创建行程按钮
        if (btnCreateTrip != null) {
            btnCreateTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createTrip();
                }
            });
        }
    }

    private void createTrip() {
        String tripName = etTripName.getText().toString().trim();
        String destination = etDestination.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        String budget = etBudget.getText().toString().trim();
        String people = etPeople.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // 验证输入
        if (tripName.isEmpty()) {
            Toast.makeText(this, "请输入行程名称", Toast.LENGTH_SHORT).show();
            return;
        }

        if (destination.isEmpty()) {
            Toast.makeText(this, "请输入目的地", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDate.isEmpty()) {
            Toast.makeText(this, "请选择开始日期", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate.isEmpty()) {
            Toast.makeText(this, "请选择结束日期", Toast.LENGTH_SHORT).show();
            return;
        }

        // 模拟创建成功
        Toast.makeText(this, "行程创建成功！", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}