import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * 普通矩、中心矩
 * hu矩
 */
public class hu {

    public static void main(String[] args) {
        IplImage src1 = cvLoadImage("src/main/resources/image/lALPBbCc1VKG94_MnM0BVA_340_156.png",CV_LOAD_IMAGE_GRAYSCALE);
        System.out.println(src1);
        CvSeq cvSeq = getImageContour(src1);
        IplImage src2 = cvLoadImage("src/main/resources/image/lALPBbCc1VKG95DMrM0BRg_326_172.png",CV_LOAD_IMAGE_GRAYSCALE);
        CvSeq cvSeq1 = getImageContour(src2);
        double result = cvMatchShapes(cvSeq,cvSeq1,1);
        System.out.println(result);

    }

    private static CvSeq getImageContour(IplImage src) {

        IplImage src1 = cvCreateImage(cvGetSize(src),8,1);

        cvCopy(src,src1);

        CvMemStorage mem = cvCreateMemStorage(0);

        CvSeq seq = new CvSeq();
        if (mem == null) {
            return null;
        }

        cvThreshold(src1,src1,200,255,CV_THRESH_BINARY);

        //cvFindContours(src1, mem, seq, new CvContour().sizeof(), CV_RETR_CCOMP,CHAIN_APPROX_NONE);
        cvFindContours(src1, mem, seq);

        return seq;
    }

    public static void main1(String[] args) {
        IplImage src ;
        src = cvCreateImage(cvSize(10,10),8,1);
        cvZero(src);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                cvSetReal2D(src,j,i,255);
            }
        }

        opencv_imgproc.CvMoments monent = new CvMoments();
        cvMoments(src,monent,2);

        double m00 = cvGetSpatialMoment(monent,0,0 );

        CvHuMoments huMoments = new CvHuMoments();
        cvGetHuMoments(monent,huMoments);

        System.out.println(m00);

        //imShow(cvarrToMat(src),"展示");

    }

    public static void imShow(Mat mat,String title) {
        //opencv自带的显示模块，跨平台性欠佳，转为Java2D图像类型进行显示
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        CanvasFrame canvas = new CanvasFrame(title, 1);
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.showImage(converter.convert(mat));

    }
}
