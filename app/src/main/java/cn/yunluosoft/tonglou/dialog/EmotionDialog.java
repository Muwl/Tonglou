package cn.yunluosoft.tonglou.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.fragment.ConstactFragment;
import cn.yunluosoft.tonglou.utils.Constant;

/**
 * @author Mu
 * @date 2015-8-5 上午11:16:32
 * @Description 情感对话框
 */
public class EmotionDialog implements OnClickListener {
    private Dialog d = null;
    private View view = null;
    private Context context = null;
    int height;
    private Handler handler;
    private View secret;
    private View married;
    private View nomarried;
    private View marriing;

    private ImageView secretImage;
    private ImageView marriedImage;
    private ImageView marriingImage;
    private ImageView nomarriedImage;
    private TextView cancel;
    private int flag;// 0 代表保密 1代表已婚 2代表未婚

    public EmotionDialog(final Context context, Handler handler, int flag) {
        super();
        this.context = context;
        this.flag = flag;
        this.handler = handler;
        d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = View.inflate(context, R.layout.emotion_dialog, null);
        d.setContentView(view);
        secret = (View) d.findViewById(R.id.emotion_dialog_secret);
        married = (View) d.findViewById(R.id.emotion_dialog_married);
        nomarried = (View) d.findViewById(R.id.emotion_dialog_nomarried);
        marriing = (View) d.findViewById(R.id.emotion_dialog_nomarriing);
        secretImage = (ImageView) d.findViewById(R.id.emotion_dialog_secretok);
        marriingImage = (ImageView) d.findViewById(R.id.emotion_dialog_nomarriingok);
        marriedImage = (ImageView) d
                .findViewById(R.id.emotion_dialog_marriedok);
        nomarriedImage = (ImageView) d
                .findViewById(R.id.emotion_dialog_nomarriedok);
        cancel = (TextView) d.findViewById(R.id.emotion_dialog_cancel);
        secret.setOnClickListener(this);
        married.setOnClickListener(this);
        nomarried.setOnClickListener(this);
        cancel.setOnClickListener(this);

        if (flag == Constant.EMOTION_SERCET) {
            secretImage.setVisibility(View.VISIBLE);
        } else if (flag == Constant.EMOTION_MARRIED) {
            marriedImage.setVisibility(View.VISIBLE);
        } else if (flag == Constant.EMOTION_MARRIEING) {
            marriingImage.setVisibility(View.VISIBLE);
        } else {
            nomarriedImage.setVisibility(View.VISIBLE);
        }
        init();
    }

    private void init() {
        Window dialogWindow = d.getWindow();
        LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        lp.width = LayoutParams.MATCH_PARENT;
        // dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
        lp.height = LayoutParams.WRAP_CONTENT;
        dialogWindow
                .setBackgroundDrawableResource(R.drawable.background_dialog);
        height = lp.height;
        d.show();
        dialogAnimation(d, view, getWindowHeight(), height, false);
    }

    private int getWindowHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;
    }

    private void dialogAnimation(final Dialog d, View v, int from, int to,
                                 boolean needDismiss) {

        Animation anim = new TranslateAnimation(0, 0, from, to);
        anim.setFillAfter(true);
        anim.setDuration(500);
        if (needDismiss) {
            anim.setAnimationListener(new AnimationListener() {

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    d.dismiss();
                }
            });

        }
        v.startAnimation(anim);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emotion_dialog_secret:
                handler.sendEmptyMessage(71);
                secretImage.setVisibility(View.VISIBLE);
                marriedImage.setVisibility(View.INVISIBLE);
                nomarriedImage.setVisibility(View.INVISIBLE);
                marriingImage.setVisibility(View.INVISIBLE);
                d.dismiss();
                break;
            case R.id.emotion_dialog_married:
                handler.sendEmptyMessage(72);
                secretImage.setVisibility(View.INVISIBLE);
                marriedImage.setVisibility(View.VISIBLE);
                nomarriedImage.setVisibility(View.INVISIBLE);
                marriingImage.setVisibility(View.INVISIBLE);
                d.dismiss();
                break;
            case R.id.emotion_dialog_nomarried:
                handler.sendEmptyMessage(73);
                secretImage.setVisibility(View.INVISIBLE);
                marriedImage.setVisibility(View.INVISIBLE);
                nomarriedImage.setVisibility(View.VISIBLE);
                marriingImage.setVisibility(View.INVISIBLE);
                d.dismiss();
                break;
            case R.id.emotion_dialog_nomarriing:
                handler.sendEmptyMessage(74);
                secretImage.setVisibility(View.INVISIBLE);
                marriedImage.setVisibility(View.INVISIBLE);
                nomarriedImage.setVisibility(View.INVISIBLE);
                marriingImage.setVisibility(View.VISIBLE);
                d.dismiss();
                break;
            case R.id.emotion_dialog_cancel:
                d.dismiss();
                break;

            default:
                break;
        }
    }

}
