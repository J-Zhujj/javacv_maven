import org.bytedeco.javacpp.opencv_core;

import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvMinMaxLoc;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_TM_CCORR_NORMED;
import static org.bytedeco.javacpp.opencv_imgproc.cvMatchTemplate;

public class TemplateMatch {

    private opencv_core.IplImage image;

    public void load(String filename) {
        image = cvLoadImage(filename);
    }

    public boolean matchTemplate(opencv_core.IplImage source) {
        boolean matchRes;
        opencv_core.IplImage result = cvCreateImage(opencv_core.cvSize(
                source.width() - this.image.width() + 1,
                source.height() - this.image.height() + 1),
                opencv_core.IPL_DEPTH_32F, 1);

        opencv_core.cvZero(result);
        cvMatchTemplate(source, this.image, result, CV_TM_CCORR_NORMED);
        double[] minVal = new double[2];
        double[] maxVal = new double[2];

        cvMinMaxLoc(result, minVal, maxVal);
        System.out.println(minVal[0]);
        System.out.println(maxVal[0]);
        matchRes = maxVal[0] > 0.99f ? true : false;
        cvReleaseImage(result);
        return matchRes;
    }


    public static void main(String[] args) {
        System.out.println("START...");
        TemplateMatch tm = new TemplateMatch();//实例化TemplateMatch对象
        tm.load("src/main/resources/image/small.jpg");//加载带比对图片，注此图片必须小于源图
        boolean result = tm.matchTemplate(cvLoadImage("src/main/resources/image/big.jpg"));//校验585.png是否包含于原图58home.png
        if (result){//打印匹配结果，boolean
            System.out.println("match");
        }else{
            System.out.println("un-match");
        }
        System.out.println("END...");
    }

}
