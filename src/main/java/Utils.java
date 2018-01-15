import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;

public class Utils {
    public static void imShow(opencv_core.Mat mat, String title) {
        //opencv自带的显示模块，跨平台性欠佳，转为Java2D图像类型进行显示
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        CanvasFrame canvas = new CanvasFrame(title, 1);
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.showImage(converter.convert(mat));

    }
}
