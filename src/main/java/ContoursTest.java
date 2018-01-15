import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

import java.util.Vector;

import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * 获取轮廓
 */
public class ContoursTest {

    // 角度判断所用变量
    protected int angle = 30;
    public final static String PICTURE_EXT = ".jpg";

    public static void main(String[] args) {
        new ContoursTest().card("src/main/resources/result/wz");
    }

    public void card(String fileName) {
        String filePath = getClass().getResource("/").getPath().substring(1);
        filePath = "";
        Mat srcMat = opencv_imgcodecs.imread(filePath + fileName + PICTURE_EXT);
        float srcWidth = srcMat.size().width();
        float srcHeight = srcMat.size().height();

        Mat blurMat = srcMat.clone();
        // 高斯模糊。Size中的数字影响车牌定位的效果。
        opencv_imgproc.GaussianBlur(srcMat, blurMat, new Size(1, 1), 0, 0,
                opencv_core.BORDER_DEFAULT);
        opencv_imgcodecs.imwrite(
                filePath + fileName + "_1_GaussianBlur" + PICTURE_EXT, blurMat);
        System.out.println("高斯模糊");

        // 灰度化
        Mat grayMat = blurMat.clone();
        opencv_imgproc.cvtColor(blurMat, grayMat, opencv_imgproc.CV_RGB2GRAY);
        opencv_imgcodecs.imwrite(filePath + fileName + "_2_gray" + PICTURE_EXT,
                grayMat);
        System.out.println("灰度化");

        // 对图像进行Sobel 运算，得到的是图像的一阶水平方向导数。
        Mat gradMat = new Mat();
        Mat gradXMat = new Mat();
        Mat gradYMat = new Mat();
        Mat absGradXMat = new Mat();
        Mat absGradTMat = new Mat();

        opencv_imgproc.Sobel(grayMat, gradXMat, opencv_core.CV_16S, 1, 0, 3, 1,
                1, opencv_core.BORDER_DEFAULT);
        opencv_core.convertScaleAbs(gradXMat, absGradXMat);

        opencv_imgproc.Sobel(grayMat, gradYMat, opencv_core.CV_16S, 0, 1, 3, 1,
                1, opencv_core.BORDER_DEFAULT);
        opencv_core.convertScaleAbs(gradYMat, absGradTMat);

        // Total Gradient (approximate)
        opencv_core.addWeighted(absGradXMat, 0.5, absGradTMat, 0.5, 0, gradMat);

        opencv_imgcodecs.imwrite(filePath + fileName + "_3_sobel" + PICTURE_EXT,
                gradMat);
        System.out.println("边缘检测");

        // 对图像进行二值化。将灰度图像（每个像素点有256 个取值可能）转化为二值图像（每个像素点仅有1 和0 两个取值可能）。
        Mat thresholdMat = new Mat();
        opencv_imgproc.threshold(gradMat, thresholdMat, 0, 255,
                opencv_imgproc.CV_THRESH_OTSU
                        + opencv_imgproc.CV_THRESH_BINARY);

        opencv_imgcodecs.imwrite(
                filePath + fileName + "_4_threshold" + PICTURE_EXT,
                thresholdMat);
        System.out.println("二值化");

        // 使用闭操作。对图像进行闭操作以后，可以看到车牌区域被连接成一个矩形装的区域。
        Mat element = opencv_imgproc.getStructuringElement(
                opencv_imgproc.MORPH_RECT, new Size(17, 3));
        opencv_imgproc.morphologyEx(thresholdMat, thresholdMat,
                opencv_imgproc.MORPH_CLOSE, element);

        opencv_imgcodecs.imwrite(
                filePath + fileName + "_5_morphology" + PICTURE_EXT,
                thresholdMat);
        System.out.println("闭操作");

        MatVector contours = new MatVector();
        opencv_imgproc.findContours(thresholdMat, contours, // a vector of
                // contours
                opencv_imgproc.CV_RETR_EXTERNAL, // 提取外部轮廓
                opencv_imgproc.CV_CHAIN_APPROX_NONE); // all pixels of each
        // contours

        System.out.println(contours.size());


        Mat contoursMat = new Mat();
        // Draw red contours on the source image
        srcMat.copyTo(contoursMat);
        opencv_imgproc.drawContours(contoursMat, contours, -1,
                new Scalar(60, 20, 220, 255));
        opencv_imgcodecs.imwrite(
                filePath + fileName + "_6_contours" + PICTURE_EXT, contoursMat);
        System.out.println("画轮廓");

        Vector<RotatedRect> rects = new Vector<RotatedRect>();

        float area = 0;
        RotatedRect rect1 = null;
        for (int i = 0; i < contours.size(); ++i) {
            RotatedRect rect = minAreaRect(contours.get(i));
            /////
            float area1 = rect.size().area();
            if(area1 > area){
                rect1 = rect;
                area = area1;
            }
            /////
            //rects.add(minAreaRect(contours.get(i)));
        }

        Vector<Mat> resultVec = new Vector<Mat>();
        Mat angleMat = srcMat.clone();
        int k = 1;
        for (int i = 0; i < rects.size(); i++) {
            RotatedRect minRect = rects.get(i);
            float width = minRect.size().width();
            float height = minRect.size().height();
            if (width > srcWidth * 0.2 && height > srcHeight * 0.2
                    && width < srcWidth * 0.95 && height < srcHeight * 0.95) {

                // rotated rectangle drawing
                // 旋转这部分代码确实可以将某些倾斜的车牌调整正，但是它也会误将更多正的车牌搞成倾斜！所以综合考虑，还是不使用这段代码。
                // 2014-08-14,由于新到的一批图片中发现有很多车牌是倾斜的，因此决定再次尝试这段代码。

                float r = width / height;
                float angle = minRect.angle();
                System.out.println(angle);
                Size rect_size = new Size((int) width, (int) height);
                if (r < 1) {
                    angle = 90 + angle;
                    rect_size = new Size(rect_size.height(), rect_size.width());
                }
                if (angle - this.angle < 0 && angle + this.angle > 0) {
                    // Create and rotate image
                    Mat rotmat = getRotationMatrix2D(minRect.center(), angle,
                            1);
                    Mat img_rotated = new Mat();
                    warpAffine(angleMat, img_rotated, rotmat, angleMat.size()); // CV_INTER_CUBIC

                    Mat resultMat = showResultMat(img_rotated, rect_size,
                            minRect.center(), k++);
                    resultVec.add(resultMat);
                }

                Mat rectMat = new Mat(srcMat, minRect.boundingRect());

                // 提取轮廓
                opencv_imgcodecs.imwrite(
                        filePath + fileName + "_7_rect" + PICTURE_EXT, rectMat);
                System.out.println("提取轮廓");
            }
        }

        /////切除黑色底图
        System.out.println(rect1.angle());
        Mat rectMat = new Mat(srcMat, rect1.boundingRect());
        resultVec.add(rectMat);

        opencv_imgcodecs.imwrite(
                filePath + fileName + "_8_remove" + PICTURE_EXT, rectMat);
        System.out.println("切除黑色底图");
        /////

        System.out.println(rectMat.size().width());
        System.out.println(rectMat.size().height());

        int size = (int) resultVec.size();
        for (int i = 0; i < size; i++) {
            Mat img = resultVec.get(i);

            ///切块方法
            int x = (int)((320/1795d)*img.size().width());
            int y = (int)((236/2509d)*img.size().height());
            int h = (int)((70/2509d)*img.size().height());
            int w = (int)((1120/1795d)*img.size().width());
            ///
            // 申请表名称
            Rect nameRect = new Rect(x, y, w, h);
            //Rect nameRect = new Rect(0, 0, img.size().width(), img.size().height());
            Mat nameMat = new Mat(img, nameRect);
            opencv_imgcodecs.imwrite(
                    filePath + fileName + "_9_name" + PICTURE_EXT, nameMat);
            System.out.println("申请表名称");

            /// 改变尺寸方法
//            File targetFile = new File("data\\sqb_small_1");
            //opencv_core.IplImage im = opencv_core.cvCreateImage(opencv_core.cvSize(img.size().width()/2,img.size().height()/2), img.depth(), img.channels());
//            IplImage matImage = new IplImage(img);
//            BufferedImage bi = new BufferedImage(img.size().width()/2, img.size().height()/2, BufferedImage.TYPE_3BYTE_BGR);
//            bi.getGraphics().drawImage(matImage.getScaledInstance(500, 200, Image.SCALE_SMOOTH),
//                    0, 0, null);
//            ImageIO.write(bi, "jpg", targetFile);
            int w2 = (int)((700/1795d)*img.size().width());
            double bl = 700/1795d;
            int h2 = (int)(((2509d*bl)/2509d)*img.size().height());
            Mat resultResized = new Mat();
            resultResized.create(h2, w2, opencv_core.CV_8UC3);
            resize(img, resultResized, resultResized.size(), 0, 0,
                    INTER_CUBIC);
            opencv_imgcodecs.imwrite(
                    filePath + fileName + "_9_name__1" + PICTURE_EXT, resultResized);
            ///


            // 联系人
            int x_1 = (int)((364/1795d)*img.size().width());
            int y_1 = (int)((483/2509d)*img.size().height());
            int w_1 = (int)((443/1795d)*img.size().width());
            int h_1 = (int)((89/2509d)*img.size().height());

            Rect sexRect = new Rect(x_1, y_1, w_1, h_1);
            Mat sexMat = new Mat(img, sexRect);
            opencv_imgcodecs.imwrite(
                    filePath + fileName + "_9_lxr" + PICTURE_EXT, sexMat);
            System.out.println("联系人");

            ///图片合并方法
            Mat m1 = nameMat.clone();
            Mat m2 = sexMat.clone();

            int t_h = m1.size().height() + m2.size().height();
            int t_w = Math.max(m1.size().width(), m2.size().width());
            Mat t_m = new Mat(t_h,t_w,opencv_core.CV_8UC3);
            Mat war = t_m.apply(new Rect(0,0,t_w,m1.size().height()));
            m1.copyTo(war);
            war = t_m.apply(new Rect(0,m1.size().height(),m2.cols(),m2.rows()));
            m2.copyTo(war, m2);
            opencv_imgcodecs.imwrite(
                    filePath + fileName + "_99_hb" + PICTURE_EXT, t_m);
            ///

            ///非空验证

            ///
        }
    }

    private Mat showResultMat(Mat src, Size rect_size, Point2f center,
                              int index) {
        Mat img_crop = new Mat();
        getRectSubPix(src, rect_size, center, img_crop);

        Mat resultResized = new Mat();
        resultResized.create(270, 428, opencv_core.CV_8UC3);
        resize(img_crop, resultResized, resultResized.size(), 0, 0,
                INTER_CUBIC);
        return resultResized;
    }
}
