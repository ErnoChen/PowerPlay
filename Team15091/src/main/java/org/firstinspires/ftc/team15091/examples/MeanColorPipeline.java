package org.firstinspires.ftc.team15091.examples;

import org.firstinspires.ftc.team15091.PipelineBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MeanColorPipeline extends PipelineBase {
    String data = "";
    Rect mask = new Rect((640 - 50) / 2, (480 - 50) / 2, 50, 50);

    @Override
    public Mat processFrame(Mat input) {
        // draw RBG boxes for reference only
        Imgproc.rectangle(input, new Rect(0, 0, 50, 50), RED, -1);
        Imgproc.rectangle(input, new Rect(50, 0, 50, 50), GREEN, -1);
        Imgproc.rectangle(input, new Rect(100, 0, 50, 50), BLUE, -1);

        frameTemp = new Mat(input, mask);
        Imgproc.GaussianBlur(frameTemp, frameTemp, new Size(45, 45), 0);
        Scalar meanColor = Core.mean(frameTemp);

        if (meanColor.val[0] > 150d) {
            Imgproc.rectangle(input, mask, RED, -1);
            data = "RED";
        } else if (meanColor.val[2] > 120d) {
            Imgproc.rectangle(input, mask, BLUE, -1);
            data = "BLUE";
        } else {
            Imgproc.rectangle(input, mask, GREEN, -1);
            data = "GREEN";
        }

        frameTemp.release();

        // show results on video feed
        Imgproc.rectangle(input, mask, GREEN, 2);
        Imgproc.rectangle(input, new Rect(150, 0, 200, 50), BLACK, -1);
        Imgproc.putText(input,
                String.format("%3.0f,%3.0f,%3.0f", meanColor.val[0], meanColor.val[1], meanColor.val[2]),
                new Point(155, 35),
                Imgproc.FONT_HERSHEY_SIMPLEX,
                1,
                YELLOW,
                2);
        return input;
    }
}
