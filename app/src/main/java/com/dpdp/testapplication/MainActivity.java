package com.dpdp.testapplication;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dpdp.testapplication.base.ClassAdapter;
import com.dpdp.testapplication.base.ClassBean;
import com.dpdp.testapplication.base.ClassEnum;
import com.dpdp.testapplication.databinding.ActivityMainBinding;
import com.dpjh.nativelib.NativeLib;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(mainBinding.getRoot());
        initView();

        // native jni test
       // new NativeLib().test();
    }

    private void initView() {
        mainBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.recyclerview.setAdapter(new ClassAdapter(this, getClassList()));
    }

    /**
     * 遍历枚举 找到所有的添加的activity
     */
    private List<ClassBean> getClassList() {
        List<ClassBean> classList = new ArrayList<>();
        for (ClassEnum classEnum : ClassEnum.values()) {
            ClassBean classBean = new ClassBean();
            classBean.setTag(classEnum.getTag());
            classBean.setDesc(classEnum.getDesc());
            classBean.setaClass(classEnum.getActivityClass());
            classBean.setJumpUrl(classEnum.getJumpUrl());
            classList.add(classBean);
        }
        return classList;
    }
}