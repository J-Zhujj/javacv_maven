import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * 获取轮廓
 */
public class Contours {

    public static void main(String[] args) {
        opencv_core.IplImage img = cvLoadImage("src/main/resources/wz.jpg", CV_LOAD_IMAGE_GRAYSCALE);
        opencv_core.IplImage img_temp = cvCreateImage(cvGetSize(img), 8, 1);

        cvThreshold(img, img, 200, 255, CV_THRESH_BINARY);

        imShow(cvarrToMat(img),"test_");

        opencv_core.CvMemStorage mem_storage = cvCreateMemStorage(0);
        opencv_core.CvSeq first_contour = new CvSeq();

        cvCopy(img, img_temp);
        double t = (double)cvGetTickCount();
        cvFindContours(img_temp, mem_storage, first_contour);
        cvZero(img_temp);
        cvDrawContours(
                img_temp,
                first_contour,
                cvScalar(100),
                cvScalar(100),
                1
        );
        t = (double)cvGetTickCount() - t;

        System.out.println( t/(cvGetTickFrequency()*1000.));

        cvClearMemStorage(mem_storage);

        imShow(cvarrToMat(img_temp),"test");


        cvCopy(img, img_temp);
        t = (double)cvGetTickCount();
        CvContourScanner scanner = cvStartFindContours(img_temp, mem_storage);
        while (cvFindNextContour(scanner) != null);
        first_contour = cvEndFindContours(scanner);

        cvZero(img_temp);
        cvDrawContours(
                img_temp,
                first_contour,
                cvScalar(100),
                cvScalar(100),
                1
        );
        t = (double)cvGetTickCount() - t;

        System.out.println(t/(cvGetTickFrequency()*1000.));

        cvClearMemStorage(mem_storage);

        imShow(cvarrToMat(img_temp),"test1");


        img = cvLoadImage("src/main/resources/image/lALPBbCc1VKG94_MnM0BVA_340_156_zjj.png", CV_LOAD_IMAGE_GRAYSCALE);
        cvThreshold(img, img, 128, 255, CV_THRESH_BINARY);
        img_temp = cvCloneImage(img);
        cvFindContours(
                img_temp,
                mem_storage,
                first_contour,
                new CvContour().sizeof(),
                CV_RETR_CCOMP,           //#1 需更改区域
                CHAIN_APPROX_NONE
        );

        cvZero(img_temp);
        cvDrawContours(
                img_temp,
                first_contour,
                cvScalar(100),
                cvScalar(100),
                1                       //#2 需更改区域
        );

        imShow(cvarrToMat(img_temp),"test3");
    }

    public static void imShow(Mat mat,String title) {
        //opencv自带的显示模块，跨平台性欠佳，转为Java2D图像类型进行显示
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        CanvasFrame canvas = new CanvasFrame(title, 1);
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.showImage(converter.convert(mat));

    }
}
