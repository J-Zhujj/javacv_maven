import org.bytedeco.javacpp.opencv_core.CvHistogram;
import org.bytedeco.javacpp.opencv_core.IplImage;

import static org.bytedeco.javacpp.helper.opencv_imgproc.cvCalcHist;
import static org.bytedeco.javacpp.opencv_core.CV_HIST_ARRAY;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * 图像相似度匹配
 */
public class CompareImg {

    public static void main(String[] args) {
        /**
         * lALPBbCc1VKG95LMqM0BPA_316_168_zl.png = TIM20180115103504_zl.png
         *
         * lALPBbCc1VKG94_MnM0BVA_340_156_zjj.png
         * = lALPBbCc1VKG95DMrM0BRg_326_172_zjj.png
         * = lALPBbCc1VKG95HMos0BOA_312_162_zjj.png
         * = lALPBbCc1VKG947Mns0BTg_334_158_zjj.png
         *
         */
        String path1 = "src/main/resources/image/lALPBbCc1VKG95LMqM0BPA_316_168_zl.png";
        String path2 = "src/main/resources/image/TIM20180115103504_zl.png";
        double ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/lALPBbCc1VKG94_MnM0BVA_340_156_zjj.png";
        path2 = "src/main/resources/image/lALPBbCc1VKG95DMrM0BRg_326_172_zjj.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/lALPBbCc1VKG94_MnM0BVA_340_156_zjj.png";
        path2 = "src/main/resources/image/lALPBbCc1VKG95HMos0BOA_312_162_zjj.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/lALPBbCc1VKG94_MnM0BVA_340_156_zjj.png";
        path2 = "src/main/resources/image/lALPBbCc1VKG947Mns0BTg_334_158_zjj.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/lALPBbCc1VKG95LMqM0BPA_316_168_zl.png";
        path2 = "src/main/resources/image/lALPBbCc1VKG95HMos0BOA_312_162_zjj.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望不匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/TIM20180115103504_zl.png";
        path2 = "src/main/resources/image/lALPBbCc1VKG95HMos0BOA_312_162_zjj.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望不匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/lALPBbCc1VKG95HMos0BOA_312_162_zjj.png";
        path2 = "src/main/resources/image/lALPBbCc1VKG947Mns0BTg_334_158_zjj.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/TIM20180115103504_zl.png";
        path2 = "src/main/resources/image/lALPBbCc1VKG947Mns0BTg_334_158_zjj.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望不匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/TIM20180115103504_zl.png";
        path2 = "src/main/resources/image/TIM20180115104603_zz.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望不匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/lALPBbCc1VKG94_MnM0BVA_340_156_zjj.png";
        path2 = "src/main/resources/image/TIM20180115104603_zz.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望不匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/lALPBbCc1VKG94_MnM0BVA_340_156_zjj.png";
        path2 = "src/main/resources/image/TIM20180115105631_zj.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望不匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/TIM20180115110114_zj.png";
        path2 = "src/main/resources/image/TIM20180115110129_zj.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");

        path1 = "src/main/resources/image/TIM20180115104603_zz.png";
        path2 = "src/main/resources/image/TIM20180115110129_zj.png";
        ret = new CompareImg().CmpPic(path1,path2);
        System.out.print("期望不匹配");
        System.out.print(ret + ":");
        System.out.println(ret>0?"匹配":"不匹配");
    }

    public double CmpPic(String path1,String path2) {
        int l_bins = 2;
        int hist_size[] = { l_bins };

        float v_ranges[] = { 0, 100 };
        float ranges[][] = { v_ranges };

        IplImage Image1 = cvLoadImage(path1, CV_LOAD_IMAGE_GRAYSCALE);
        IplImage Image2 = cvLoadImage(path2, CV_LOAD_IMAGE_GRAYSCALE);

        IplImage imageArr1[] = { Image1 };
        IplImage imageArr2[] = { Image2 };

        CvHistogram Histogram1 = CvHistogram.create(1, hist_size,
                CV_HIST_ARRAY, ranges, 1);
        CvHistogram Histogram2 = CvHistogram.create(1, hist_size,
                CV_HIST_ARRAY, ranges, 1);

        cvCalcHist(imageArr1, Histogram1, 0, null);
        cvCalcHist(imageArr2, Histogram2, 0, null);

        cvNormalizeHist(Histogram1, 100.0);
        cvNormalizeHist(Histogram2, 100.0);

        return cvCompareHist(Histogram1, Histogram2, CV_COMP_CORREL);
    }
}
