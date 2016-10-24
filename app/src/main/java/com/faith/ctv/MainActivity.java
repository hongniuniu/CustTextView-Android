package com.faith.ctv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TEST_TITLE = "自定义可滑动变高的View";
    public static final String shortStr = "我是中国人,我是中国人的的阿大发撒发生中国人";
    public static final String midStr = "我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人";
    public static final String longStr = "我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人" +
            "的的阿大发撒发生我是中国人我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人," +
            "我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人," +
            "我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人";
    public static final String longlongStr = "我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人" +
            "的的阿大发撒发生我是中国人我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人," +
            "我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人," +
            "我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人我是中国人的的阿大发撒发生;我是中国人,我是中" +
            "国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人\" +\n" +
            "            \"的的阿大发撒发生我是中国人我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,\" +\n" +
            "            \"我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,\" +\n" +
            "            \"我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人我是中国人的的阿大发撒发生;我是中国人,我是中\" +\n" +
            "            \"国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人";

    public static final int CLK_TYPE_SHORT = 0;
    public static final int CLK_TYPE_MID = 1;
    public static final int CLK_TYPE_LONG = 2;
    public static final int CLK_TYPE_LONG_LONG = 3;

    private int mClkType = CLK_TYPE_SHORT;

    /**
     * 初始化布局
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PicDescView pdv = (PicDescView) findViewById(R.id.id_descLayout);
        pdv.setTextDesc(TEST_TITLE);
        toast("点击按钮切换底部文字内容哦");

        findViewById(R.id.id_testHgtTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (mClkType) {
                    case CLK_TYPE_SHORT:
                        pdv.setTextDesc(TEST_TITLE,shortStr);
                        mClkType = CLK_TYPE_MID;
                        break;
                    case CLK_TYPE_MID:
                        pdv.setTextDesc(TEST_TITLE,midStr);
                        mClkType = CLK_TYPE_LONG;
                        break;
                    case CLK_TYPE_LONG:
                        pdv.setTextDesc(TEST_TITLE,longStr);
                        mClkType = CLK_TYPE_LONG_LONG;
                        toast("向上滑动试试...");
                        break;
                    case CLK_TYPE_LONG_LONG:
                        pdv.setTextDesc(TEST_TITLE,longlongStr);
                        mClkType = CLK_TYPE_SHORT;
                        toast("向上滑动试试...");
                        break;
                }


            }
        });
    }



    private void toast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}
