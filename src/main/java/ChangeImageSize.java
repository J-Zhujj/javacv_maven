import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;

import java.io.File;

import static org.bytedeco.javacpp.opencv_imgproc.INTER_CUBIC;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

public class ChangeImageSize {

    public static void main(String[] args) {
        new ChangeImageSize().change();
    }

    public void change(){
        try {

            String filepath = "/Users/zhujiajian/Downloads/waterfall-master/app/src/main/assets/im";
            int idx = 0 ;

            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());

            }else if (file.isDirectory()) {
                System.out.println("文件夹");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        System.out.println("path=" + readfile.getPath());
                        System.out.println("absolutepath="
                                + readfile.getAbsolutePath());
                        System.out.println("name=" + readfile.getName());
                        String path = readfile.getAbsolutePath();
                        path = readfile.getAbsolutePath().replace("\\","/" );

                        opencv_core.Mat img = opencv_imgcodecs.imread(path);
                        System.out.println(img.size().width());
                        if(img.size().width() < 1700){
                            opencv_imgcodecs.imwrite(
                                    "/Users/zhujiajian/Downloads/waterfall-master/app/src/main/assets/im1/" + idx++ + ".jpg", img);
                            continue;
                        }
                        double w2 = 1700d;
                        double bl = 1700d/img.size().width();
                        double h2 = bl*img.size().height();
                        System.out.println(bl);
                        System.out.println(h2);

                        opencv_core.Mat resultResized = new opencv_core.Mat();
                        resultResized.create((int)h2, (int)w2, opencv_core.CV_8UC3);
                        resize(img, resultResized, resultResized.size(), 0, 0,
                                INTER_CUBIC);
                        opencv_imgcodecs.imwrite(
                                "/Users/zhujiajian/Downloads/waterfall-master/app/src/main/assets/im1/" + idx++ + ".jpg", resultResized);


                    } else if (readfile.isDirectory()) {

                    }
                }

            }

        } catch (Exception e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }

    }
}
