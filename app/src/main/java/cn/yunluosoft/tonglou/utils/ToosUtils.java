package cn.yunluosoft.tonglou.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.yunluosoft.tonglou.activity.LoginActivity;
import u.aly.cp;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import cn.yunluosoft.tonglou.activity.MainActivity;
import cn.yunluosoft.tonglou.model.MessageInfo;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;

public class ToosUtils {

    public static boolean isStringEmpty(String msg) {
        if (null == msg || "".equals(msg)) {
            return true;
        }
        return false;
    }

    public static String getTextContent(TextView textView) {
        return textView.getText().toString().trim();
    }

    public static void goReLogin(Context context) {
//        ToastUtils.displayShortToast(context, "验证失败，请重新登录");
        Intent intent = new Intent(context, LoginActivity.class);
        ShareDataTool.SaveInfo(context, null, null, null, null);
        ShareDataTool.SaveInfoDetail(context, null, null, null,null);
        MyApplication.getInstance().logout(null);
        context.startActivity(intent);
        MyApplication.getInstance().logout(null);
    }

    public static boolean isTextEmpty(TextView textView) {
        return isStringEmpty(getTextContent(textView));
    }

    public static boolean checkPwd(String str) {
        if (str.length() < 6) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean MatchPhone(String name) {
        Pattern p = Pattern
                .compile("^((1[3,7][0-9])|(15[^4,\\D])|(18[0-3,5-9])|(14[5,7]))\\d{8}$");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public static File saveImage2SD(String filePath, Bitmap bitmap) {
        try {
            File saveFile = null;

            if (bitmap != null) {
                saveFile = createFile(filePath);
            }
            FileOutputStream fos = new FileOutputStream(filePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();
            fos.write(bytes);
            fos.close();
            return saveFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static boolean isURL(String str) {
        // 转换为小�?
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();

    }

    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return false;
            }
        }
        return true;
    }

    public static boolean filterSysMes(Context context, EMMessage message) {
//		// ToastUtils.displaLongToast(context, "收到信息");
//		LogManager.LogShow("~~~~~~~~~", "userName------" + message.getFrom(),
//				LogManager.ERROR);
//		LogManager
//				.LogShow("~~~~~~~~~",
//						"userName------" + message.describeContents(),
//						LogManager.ERROR);
//		if (message.getFrom().equals(Constant.SYS_NAME)) {
//			TextMessageBody txtBody = (TextMessageBody) message.getBody();
//			// digest = txtBody.getMessage();
//			LogManager.LogShow("~~~~~~~~~", "@@@@@@@@@@", LogManager.ERROR);
//			LogManager.LogShow("~~~~~~~~~", txtBody.getMessage(),
//					LogManager.ERROR);
//
//			try {
//				ShareDataTool.saveNum(context, message.getIntAttribute("json"));
//			} catch (EaseMobException e) {
//				e.printStackTrace();
//			}
//			ShareDataTool.saveNoReadTime(context, System.currentTimeMillis());
//			Intent intent = new Intent();
//			intent.setAction(MainActivity.BROADCAST_ACTION);
//			context.sendBroadcast(intent);
//			return true;
//		} else if (message.getFrom().equals(Constant.SYS_GETNAME)) {
//			return true;
//		} else {
        return false;
//		}
//
    }

    public static void setEmmessage(EMMessage message, MessageInfo messageInfo) {
        if (ToosUtils.isStringEmpty(messageInfo.senderHeadUrl)) {
            messageInfo.senderHeadUrl = "";
        }
        if (ToosUtils.isStringEmpty(messageInfo.receiverHeadUrl)) {
            messageInfo.receiverHeadUrl = "";
        }
        message.setAttribute("senderUserId", messageInfo.senderUserId);
        message.setAttribute("receiverUserId", messageInfo.receiverUserId);
        message.setAttribute("senderImUserName", messageInfo.senderImUserName);
        message.setAttribute("receiverImUserName",
                messageInfo.receiverImUserName);
        message.setAttribute("senderHeadUrl", messageInfo.senderHeadUrl);
        message.setAttribute("receiverHeadUrl", messageInfo.receiverHeadUrl);
        message.setAttribute("senderNickName", messageInfo.senderNickName);
        message.setAttribute("receiverNickName", messageInfo.receiverNickName);
    }

    /**
     * 获取聊天传的参数
     *
     * @param message
     * @param
     */
    public static MessageInfo getMessageInfo(EMMessage message) {
        try {
            return new MessageInfo(message.getStringAttribute("senderUserId"),
                    message.getStringAttribute("receiverUserId"),
                    message.getStringAttribute("senderImUserName"),
                    message.getStringAttribute("receiverImUserName"),
                    message.getStringAttribute("senderHeadUrl"),
                    message.getStringAttribute("receiverHeadUrl"),
                    message.getStringAttribute("senderNickName"),
                    message.getStringAttribute("receiverNickName"));
        } catch (EaseMobException e) {
            e.printStackTrace();
            return null;

        }
    }

    public static String getEncrypt(String str) {
        BlowfishECB bf = new BlowfishECB("tg!$@hup1*301%#c");
        return bf.encrypt(str);
    }

    public static String getEncryptto(String str) {
        BlowfishECB bf = new BlowfishECB("tg!$@hup1*301%#c");
        return bf.decrypt(str);
    }


    public static File compressBmpToFile(Bitmap bmp) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String ss = Environment.getExternalStorageDirectory() + "/louyu/"
                    + String.valueOf(System.currentTimeMillis()) + ".JPEG";
            file = createFile(ss);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = 100;// 个人喜欢从80开始,
            bmp.compress(CompressFormat.JPEG, options, baos);
            while (baos.toByteArray().length / 1024 > 200 && options > 10) {
                baos.reset();
                options -= 10;
                bmp.compress(CompressFormat.JPEG, options, baos);
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void deleteFile(File file) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                }
                // 如果它是一个目录
                else if (file.isDirectory()) {
                    // 声明目录下所有的文件 files[];
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                        deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                    }
                }
                file.delete();
            }
        }
    }

    public static File createFile(String path) {
        File file = new File(path);
        File dir = new File(file.getParent());
        if (!dir.exists()) {
            try {
                // 按照指定的路径创建文件夹
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
        }
        if (!file.exists()) {
            try {
                // 在指定的文件夹中创建文件
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;

    }

}
