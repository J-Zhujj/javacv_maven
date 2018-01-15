import org.bytedeco.javacpp.Pointer;

import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;

import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;

public class DrawRect {
    //JCVMouseCallBack:继承类CvMouseCallback,重写call方法,以便于在cvSetMouseCallback(...)进行回调
    static JCVMouseCallBack jcvMouseCallBack = new JCVMouseCallBack();
    public static void main(String[] args) {
        //初始化画图区域
        IplImage image = cvCreateImage(cvSize(640, 360), IPL_DEPTH_8U, 3);
        //将画图区域置为0(矩阵)
        cvZero(image);
        IplImage temImage = cvCloneImage(image);
        //新建窗口显示画图区域
        cvNamedWindow( "DrawRebBox" );
        //设置鼠标回调函数，以响应鼠标事件
        cvSetMouseCallback("DrawRebBox", jcvMouseCallBack, image);
        while(true) {
            cvCopy(image, temImage);
            if(jcvMouseCallBack.isDrawBox()){
                //在temImage画布内，显示鼠标事件所画的图像
                drawBox(temImage, jcvMouseCallBack.getBox());
                //将图片保存到文件
                cvSaveImage("src/main/resources/drawRect.jpg", temImage);
            }
            cvShowImage("DrawRebBox", temImage);
            //按ESC键退出(ESC键对应的ASCII值为27)
            if(cvWaitKey( 15 )==27) {
                break;
            }
        }
        //释放资源
        cvReleaseImage(image);
        cvReleaseImage(temImage);
        cvDestroyWindow("DrawRebBox");
    }
    public static void drawBox(IplImage image,CvRect cvRect){
        cvRectangle (
                //image作为画布显示矩形
                image,
                //矩形的左上角坐标位置
                cvPoint(jcvMouseCallBack.getBox().x(),jcvMouseCallBack.getBox().y()),
                //矩形右下角坐标位置
                cvPoint(jcvMouseCallBack.getBox().x()+jcvMouseCallBack.getBox().width(),jcvMouseCallBack.getBox().y()+jcvMouseCallBack.getBox().height()),
                //边框的颜色
                CV_RGB(255, 0, 0),
                //线条的宽度:正值就是线宽，负值填充矩形,例如CV_FILLED，值为-1
                1,
                //线条的类型(0,8,4)
                4,
                //坐标的小数点位数
                0
        );
    }
}
/**
 * @功能说明：重写call(...)方法,实现函数回调
 * @time:2014年7月18日下午4:30:06
 * @version:1.0
 *
 */
class JCVMouseCallBack extends CvMouseCallback{
    //初始化box
    CvRect box = cvRect(-1, -1, 0, 0);
    //是否在画图的标识
    boolean drawBox = false;
    @Override
    /*
     * 参数说明(non-Javadoc)
     * @see org.bytedeco.javacpp.opencv_highgui.CvMouseCallback#call(int, int, int, int, org.bytedeco.javacpp.Pointer)
     * envent:鼠标事件代码0(鼠标移动),1(左键按下),4(左键放开),还有别的。
     * x:鼠标在画布上的x坐标
     * y:鼠标在画布上的y坐标
     * flags:是否有鼠标事件
     */
    public void  call(int event, int x, int y, int flags, Pointer pointer){
        //处理鼠标事件
        switch (event) {
            //鼠标左键按下
            case CV_EVENT_LBUTTONDOWN: {
                drawBox = true;
                //以鼠标按下的点为左上角的定点，在画布上画矩形
                box = cvRect(x, y, 0, 0);
            }
            break;
            //鼠标移动
            case CV_EVENT_MOUSEMOVE: {
                //鼠标左键被按下,开始画图
                if (drawBox) {
                    box.width(x - box.x());
                    box.height(y - box.y());
                }
            }
            break;
            //鼠标左键放开
            case CV_EVENT_LBUTTONUP: {
                drawBox = false;
            }
            break;
        }
    }
    public CvRect getBox() {
        return box;
    }
    public void setBox(CvRect box) {
        this.box = box;
    }
    public boolean isDrawBox() {
        return drawBox;
    }
    public void setDrawBox(boolean drawBox) {
        this.drawBox = drawBox;
    }
}
