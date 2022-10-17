package org.firstinspires.ftc.team15091.examples;

import org.firstinspires.ftc.team15091.PipelineBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class YellowDuckPipeline extends PipelineBase {
    int threshold1 = 85, threshold2 = 255;
    /*
     * Points which actually define the sample region rectangles, derived from above values
     *
     * Example of how points A and B work to define a rectangle
     *
     *   ------------------------------------
     *   | (0,0) Point A                    |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |                  Point B (70,50) |
     *   ------------------------------------
     *
     */
    Rect leftBox = new Rect(5, 240, 50, 30);
    Rect midBox = new Rect(480, 240, 50, 30);
    Mat leftMat, midMat;
    String data = "";
    @Override
    public Mat processFrame(Mat input) {
        Core.rotate(input, input, Core.ROTATE_180);
        contoursList.clear();
        Imgproc.rectangle(input, new Rect(0,0,50,50), YELLOW, -1);

        frameTemp = new Mat();
        //Convert to RBG to YUV, so we can extract yellow channel
        Imgproc.cvtColor(input, frameTemp, Imgproc.COLOR_RGB2YUV);
        Imgproc.GaussianBlur(frameTemp,frameTemp,new Size(3,3),0);
        Core.extractChannel(frameTemp, frameTemp, 1);
        Imgproc.threshold(frameTemp, frameTemp, threshold1, threshold2, Imgproc.THRESH_BINARY_INV);

        leftMat = new Mat(frameTemp, leftBox);
        midMat = new Mat(frameTemp, midBox);
        double leftAvg = Core.mean(leftMat).val[0];
        double midAvg = Core.mean(midMat).val[0];

        Imgproc.findContours(frameTemp, contoursList, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        contoursList.removeIf((cont) -> {
            double area = Imgproc.contourArea(cont);
            if (area > 80d) {
                return false;
            }
            return true;
        });

        frameTemp.release();
        leftMat.release();
        midMat.release();

        // show results on video feed
        Imgproc.drawContours(input, contoursList, -1, RED, 2, 8);
        Imgproc.rectangle(input, leftBox, GREEN, 2);
        Imgproc.rectangle(input, midBox, GREEN, 2);
        data = "RIGHT";
        if (leftAvg > 0d) {
            Imgproc.rectangle(input, leftBox, YELLOW, -1);
            data = "LEFT";
        }
        if (midAvg > 0d) {
            Imgproc.rectangle(input, midBox, YELLOW, -1);
            data = "MID";
        }
        Imgproc.putText(input,
                String.format("%3.0f", leftAvg),
                leftBox.tl(),
                Imgproc.FONT_HERSHEY_SIMPLEX,
                1,
                GREEN,
                2 );
        Imgproc.putText(input,
                String.format("%3.0f", midAvg),
                midBox.tl(),
                Imgproc.FONT_HERSHEY_SIMPLEX,
                1,
                GREEN,
                2 );
        return input;
    }
}
