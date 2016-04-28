package xsf.athena.laughImages.model;

/**
 * Author: xsf
 * Time: created at 2016/4/28.
 * Email: xsf_uestc_ncl@163.com
 */
public interface ImageModel {
    void loadImageList(ImageModelImpl.OnLoadImageListListener listener);
}
