package com.zc.chart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zc.chartlib.ChartView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList datas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ChartView.CharViewData charViewData = new ChartView.CharViewData();
            charViewData.text = "阿里巴巴技术有限公司";
            if (i == 0) {
                charViewData.size = 2000;
                charViewData.data = "2000分,行业No1 处于1%";
            } else if (i == 1) {
                charViewData.size = 1500;
                charViewData.data = "1500,行业No2 处于2%";
            } else if (i == 2) {
                charViewData.size = 1000;
                charViewData.data = "1000分,行业No3 处于3%";
            } else {
                charViewData.size = 500;
                charViewData.data = "500分,行业No4 处于4%";
            }
            datas.add(charViewData);
        }
        ChartView chartView = (ChartView) findViewById(R.id.chart_view);
        chartView.setBuild(new ChartView.Build()
                .setMaxSize(2000)
                .setX_point_size(5)
                .setDatas(datas)
                .setUnit("单位:分")
        );
    }
}
