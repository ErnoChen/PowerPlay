package org.firstinspires.ftc.team15091.examples;

import org.firstinspires.ftc.team15091.PipelineBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class HSVPipeline extends PipelineBase {
    double data = 0d;

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV);

        frameTemp = new Mat(input, mask);
        Imgproc.GaussianBlur(frameTemp, frameTemp, new Size(45, 45), 0);
        data = Core.mean(frameTemp).val[0];

        Imgproc.rectangle(input, mask, new Scalar(data, 255, 255), -1);

        Imgproc.cvtColor(input, input, Imgproc.COLOR_HSV2RGB);
//        frameTemp.copyTo(input.rowRange(190, 290).colRange(280, 360));

        frameTemp.release();
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
