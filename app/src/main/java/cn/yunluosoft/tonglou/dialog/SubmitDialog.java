package cn.yunluosoft.tonglou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import cn.yunluosoft.tonglou.R;

/**
 * @author Mu
 * @date 2015-3-6
 * @description
 */
public class SubmitDialog extends Dialog implements
        View.OnClickListener {
    private Context context;
    private Handler handler;
    private ImageView imageView;
    private TextView tip;
    private TextView content;
    private TextView close;
    private String msg;
    private int flag;//1嗨团发布成功 2二手发布 3帮帮发布  4 二手发布
    private TextView back;
    private ImageView div;

    public SubmitDialog(Context context, int flag, Handler handler) {
        super(context, R.style.dialog2);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        this.flag = flag;
        this.handler = handler;
        setContentView(R.layout.submit_dialog);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        show();
        initView();

    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.submit_image);
        tip = (TextView) findViewById(R.id.submit_tip);
        content = (TextView) findViewById(R.id.submit_content);
        close = (TextView) findViewById(R.id.submit_close);
        back = (TextView) findViewById(R.id.submit_back);
        div= (ImageView) findViewById(R.id.submit_div);
        close.setOnClickListener(this);
        back.setOnClickListener(this);
        back.setVisibility(View.GONE);
        div.setVisibility(View.GONE);
        if (flag == 1) {
            tip.setText("发布成功");
            content.setText("请等待人员进入");
            close.setText("进入活动群");
            back.setVisibility(View.VISIBLE);
            div.setVisibility(View.VISIBLE);
        } else if (flag == 2) {
            tip.setText("发布成功");
            content.setVisibility(View.GONE);
            close.setText("确定");

        } else if (flag == 3) {
            tip.setText("发布成功");
            content.setVisibility(View.GONE);
            close.setText("确定");

        } else if (flag == 4) {
            tip.setText("发布成功");
            content.setVisibility(View.GONE);
            close.setText("确定");

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_close:
                handler.sendEmptyMessage(552);
                dismiss();
                break;

            case R.id.submit_back:
                handler.sendEmptyMessage(558);
                dismiss();
                break;
            default:
                break;
        }

    }

}
