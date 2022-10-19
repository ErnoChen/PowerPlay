package org.firstinspires.ftc.team15091.examples;

import org.firstinspires.ftc.team15091.PipelineBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class HSVPipeline extends PipelineBase {
    Mat frameHSV = new Mat();
    double data = 0d;

    @Override
    public Mat processFrame(Mat input) {
        frameTemp = new Mat();

        Imgproc.cvtColor(input, frameHSV, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(frameHSV, frameHSV, Imgproc.COLOR_RGB2HSV);

        frameTemp = new Mat(frameHSV, mask);
        data = Core.mean(frameTemp).val[0];

        Imgproc.rectangle(frameHSV, mask, new Scalar(data, 255, 255), -1);

        Imgproc.cvtColor(frameHSV, frameHSV, Imgproc.COLOR_HSV2RGB);

        frameHSV.copyTo(input);
        frameTemp.release();
        frameHSV.release();
        Imgproc.rectangle(input, mask, GREEN, 2);
        Imgproc.rectangle(input, new Rect(0, 0, 150, 50), BLACK, -1);
        Imgproc.putText(input,
                String.format("hue: %3.0f", data),
                new Point(5, 35),
                Imgproc.FONT_HERSHEY_SIMPLEX,
                1,
                YELLOW,
                2);

        return input;
    }
}
