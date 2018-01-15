import org.bytedeco.javacpp.opencv_core.IplImage;


import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class SizeHalf {
    public static void main(String[] args) {
        //读取图像
        IplImage srcInImage = cvLoadImage("src/main/resources/image/lALPBbCc1VKG94_MnM0BVA_340_156_zjj.png");
        if (srcInImage !=null) {
            //显示未处理的图像
            cvShowImage("srcInImage", srcInImage);
//      if (srcInImage.width()%2 !=0&&srcInImage.height()%2 !=0) {
//          return;
//      }
            //为图像输出分配空间
            IplImage srcOutImage = cvCreateImage(cvSize(srcInImage.width()/2, srcInImage.height()/2), srcInImage.depth(), srcInImage.nChannels());
            //减半
            cvPyrDown(srcInImage, srcOutImage);
            cvShowImage("srcOutImage", srcOutImage);
            //将处理结果保存
            cvSaveImage("src/main/resources/image/smoothHalfSmall.jpg", srcOutImage);
            //等待按键
            cvWaitKey();
            //释放空间
            cvReleaseImage(srcInImage);
            cvReleaseImage(srcOutImage);
            cvDestroyAllWindows();

        }
    }
}
