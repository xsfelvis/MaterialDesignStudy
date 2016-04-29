package xsf.athena.laughImages.presenter;

import java.util.List;

import xsf.athena.bean.ImageBean;
import xsf.athena.laughImages.model.ImageModel;
import xsf.athena.laughImages.model.ImageModelImpl;
import xsf.athena.laughImages.view.ImgView;

/**
 * Author: xsf
 * Time: created at 2016/4/28.
 * Email: xsf_uestc_ncl@163.com
 */
public class ImagePresenterImpl implements ImagePresenter, ImageModelImpl.OnLoadImageListListener {
    private ImageModel mImageModel;
    private ImgView mImageView;

    public ImagePresenterImpl(ImgView imageView) {
        this.mImageModel = new ImageModelImpl();
        this.mImageView = imageView;
    }

    @Override
    public void loadImageList() {
        mImageView.showProgress();
        mImageModel.loadImageList(this);

    }

    @Override
    public void onSuccess(List<ImageBean> list) {
        mImageView.addImages(list);
        mImageView.hideProgress();

    }

    @Override
    public void onFailure(String msg, Exception e) {
        mImageView.hideProgress();
        mImageView.loadFailMsg();

    }
}
