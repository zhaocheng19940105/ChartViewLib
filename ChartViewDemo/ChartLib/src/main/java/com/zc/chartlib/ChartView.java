package com.zc.chartlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by zhaocheng on 2016/11/3.
 */

public class ChartView extends View {


    private final static int DEFAULT_PADDING = 10;
    private final static int DEFAULT_TEXT_SIZE = 22;
    private final static int DEFAULT_TEXT_MAX_WIDTH = 200;
    private final static int X_BOTTOM_LINE = 30;
    private int x_padding;
    private int y_padding;
    private int textSize;
    private int chart_height;
    private int line_width = 2;
    private int textMaxWidth;
    private int maxSize;
    private int x_point_size = 4;
    private String unit;

    private List<CharViewData> datas;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }


    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChartView, defStyleAttr, 0);
        x_padding = a.getDimensionPixelSize(R.styleable.ChartView_x_padding, DEFAULT_PADDING);
        y_padding = a.getDimensionPixelSize(R.styleable.ChartView_y_padding, DEFAULT_PADDING);
        textSize = a.getDimensionPixelSize(R.styleable.ChartView_text_size, DEFAULT_TEXT_SIZE);
        chart_height = a.getDimensionPixelOffset(R.styleable.ChartView_chart_height, DEFAULT_PADDING);
        textMaxWidth = a.getDimensionPixelOffset(R.styleable.ChartView_text_max_width,DEFAULT_TEXT_MAX_WIDTH);
        a.recycle();
    }


    public static class CharViewData {
        public String text;
        public int size;
        public String data;
    }


    public void setBuild(Build build) {

        if (build == null)
            throw new RuntimeException("Build is null");
        if (build.maxSize != 0)
            maxSize = build.maxSize;
        if (build.datas != null)
            datas = build.datas;
        if (build.x_point_size != 0)
            x_point_size = build.x_point_size;

        if (build.line_width != 0)
            line_width = build.line_width;
        if (build.x_padding != 0)
            x_padding = build.x_padding;

        if (build.y_padding != 0)
            y_padding = build.y_padding;

        if (build.textSize != 0)
            textSize = build.textSize;

        if (build.chart_height != 0)
            chart_height = build.chart_height;

        if (build.textMaxWidth != 0)
            textMaxWidth = build.textMaxWidth;

        if (!TextUtils.isEmpty(build.unit))
            unit = build.unit;

        invalidate();
    }


    public static class Build {
        private int x_padding;
        private int y_padding;
        private int textSize;
        private int chart_height;
        private List<CharViewData> datas;
        private int line_width;
        private int textMaxWidth;
        private int maxSize;
        private int x_point_size;
        private String unit;

        public Build setDatas(List<CharViewData> datas) {
            this.datas = datas;
            return this;
        }

        public Build setUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public Build setX_padding(int x_padding) {
            this.x_padding = x_padding;
            return this;
        }

        public Build setY_padding(int y_padding) {
            this.y_padding = y_padding;
            return this;
        }

        public Build setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Build setChart_height(int chart_height) {
            this.chart_height = chart_height;
            return this;
        }

        public Build setLine_width(int line_width) {
            this.line_width = line_width;
            return this;
        }

        public Build setTextMaxWidth(int textMaxWidth) {
            this.textMaxWidth = textMaxWidth;
            return this;
        }

        public Build setMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Build setX_point_size(int x_point_size) {
            this.x_point_size = x_point_size;
            return this;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (getWidth() != 0 && getHeight() != 0) {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(textSize);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            int textHeight = (int) (fontMetrics.bottom - fontMetrics.top);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(line_width);
            int lineStartX = x_padding * 2 + textMaxWidth;
            int lineEndY = getHeight() - y_padding * 3 - textHeight;

            if (!TextUtils.isEmpty(unit)) {
                float v1 = textPaint.measureText(unit);
                canvas.drawText(unit, getWidth() - x_padding - v1, y_padding + textHeight, textPaint);
            }

            //绘制X轴
            canvas.drawLine(lineStartX - x_padding, lineEndY, getWidth() - x_padding, lineEndY, paint);
            int xWidth = getWidth() - x_padding * 2 - lineStartX;
            int itemWidth = xWidth / (x_point_size * 2);
            for (int x = 1; x <= x_point_size * 2; x++) {
                if (x % 2 != 0) {
                    canvas.drawLine(itemWidth * x + lineStartX, lineEndY, itemWidth * x + lineStartX, lineEndY + X_BOTTOM_LINE / 2, paint);
                } else {
                    canvas.drawLine(itemWidth * x + lineStartX, lineEndY, itemWidth * x + lineStartX, lineEndY + X_BOTTOM_LINE, paint);
                    String data = String.valueOf(maxSize / (x_point_size * 2) * x);
                    float v = textPaint.measureText(data) / 2;
                    canvas.drawText(data, itemWidth * x + lineStartX - v, lineEndY + X_BOTTOM_LINE + textHeight, textPaint);
                }
            }

            //绘制Y轴
            canvas.drawLine(lineStartX, y_padding + textHeight, lineStartX, lineEndY + y_padding, paint);
            if (datas != null && datas.size() > 0) {
                int itemHeight = (lineEndY - y_padding - textHeight) / datas.size();
                for (int i = 0; i < datas.size(); i++) {
                    float temp = datas.get(i).size / (float) maxSize;
                    canvas.drawRect(lineStartX, itemHeight * i + y_padding * 2 + textHeight, xWidth * temp + lineStartX, itemHeight * i + y_padding * 2 + chart_height + textHeight, paint);

                    canvas.save();
                    canvas.translate(x_padding, itemHeight * i + y_padding * 2 + textHeight);
                    if (!TextUtils.isEmpty(datas.get(i).text)) {
                        StaticLayout layout = new StaticLayout
                                (datas.get(i).text, textPaint, textMaxWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                        layout.draw(canvas);
                    }
                    if (!TextUtils.isEmpty(datas.get(i).data)) {
                        canvas.translate(lineStartX + x_padding, chart_height);
                        StaticLayout layout2 = new StaticLayout
                                (datas.get(i).data, textPaint, xWidth - x_padding, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                        layout2.draw(canvas);
                    }
                    canvas.restore();
                }


            }

        } else {
            super.onDraw(canvas);
        }
    }
}
