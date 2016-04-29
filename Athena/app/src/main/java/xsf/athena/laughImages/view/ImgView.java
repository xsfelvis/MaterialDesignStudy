package xsf.athena.laughImages.view;

import java.util.List;

import xsf.athena.bean.ImageBean;

/**
 * Author: xsf
 * Time: created at 2016/4/28.
 * Email: xsf_uestc_ncl@163.com
 */
public interface ImgView {
    void addImages(List<ImageBean> list);
    void showProgress();
    void hideProgress();
    void loadFailMsg();
}
