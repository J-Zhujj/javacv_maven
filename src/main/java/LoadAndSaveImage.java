import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgproc;

import static org.bytedeco.javacpp.opencv_core.FONT_HERSHEY_PLAIN;
import static org.bytedeco.javacpp.opencv_core.flip;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.circle;

public class LoadAndSaveImage {

    public static void main(String[] args) {
        //以彩色模式读取图像
        Mat  image = imread("src/main/resources/image/lALPBbCc1VKG94_MnM0BVA_340_156_zjj.png", IMREAD_COLOR);
        if (image==null||image.empty()) {
            System.out.println("读取图像失败，图像为空");
            return;
        }

        System.out.println("图像宽x高" + image.cols() + " x " + image.rows());
        /**
         * 显示图像,opencv自带的显示方法，跨平台性能不好，转换为java2D显示图像
         * windows下可以使用如下代码进行显示
         * opencv_highgui.imshow("原始图像", image);
         */
        Utils.imShow(image, "原始图像");
        //创建空mat，保存处理图像
        Mat result = new Mat();
        int flipCode=1;
        /**
         * flipCode
         * >0  水平翻转
         * =0 垂直翻转
         * <0  同时翻转
         *
         */
        flip(image, result, flipCode);
        //显示处理过的图像
        Utils.imShow(result, "水平翻转");
        /**
         * 保存图像
         * 也可使用opencv原生方法 opencv_imgcodecs. imwrite("output.bmp", result);
         */
        imwrite("src/main/resources/image/1/lALPBbCc1VKG94_MnM0BVA_340_156_zjj.png",result);

        //克隆图像
        Mat imageCircle = image.clone();
        /**
         * 在图像上画圆
         */
        circle(imageCircle, // 目标图像
                new Point(20, 50), // 圆心坐标
                65, // radius
                new Scalar(0,200,0,0), // 颜色，绿色
                2, // 线宽
                8, // 8-connected line
                0); // shift

        opencv_imgproc.putText(imageCircle, //目标图像
                "Lake and Tower", // 文本内容(不可包含中文)
                new Point(60, 20), // 文本起始位置坐标
                FONT_HERSHEY_PLAIN, // 字体类型
                2.0, // 字号大小
                new Scalar(0,255,0,3), //文本颜色，绿色
                1, // 文本字体线宽
                8, // 线形.
                false); //控制文本走向
        Utils.imShow(imageCircle, "画圆mark");

    }
}
