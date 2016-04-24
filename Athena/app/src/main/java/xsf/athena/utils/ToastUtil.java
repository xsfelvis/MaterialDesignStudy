package xsf.athena.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import xsf.athena.AthenaApplication;
import xsf.athena.R;

/**
 * Author: xsf
 * Time: created at 2016/4/22.
 * Email: xsf_uestc_ncl@163.com
 */
public class ToastUtil {
    public static void show(String text, int showtime) {
        LayoutInflater inflater = (LayoutInflater) AthenaApplication.getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_layout, null);
        TextView content = (TextView) layout.findViewById(R.id.tv_content);
        if (!Tools.isEmpty(text)) {
            content.setText(text);
        } else {
            content.setText(R.string.err_system);
        }
        Toast toast = new Toast(AthenaApplication.getApplication());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(showtime);
        toast.setView(layout);
        toast.show();
    }

    public static void showWithImg(String text, int showtime) {
        LayoutInflater inflater = (LayoutInflater) AthenaApplication.getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_layout_with_img, null);
        TextView content = (TextView) layout.findViewById(R.id.tv_content);
        if (!Tools.isEmpty(text)) {
            content.setText(text);
        } else {
            content.setText(R.string.err_system);
        }
        Toast toast = new Toast(AthenaApplication.getApplication());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(showtime);
        toast.setView(layout);
        toast.show();
    }


}
