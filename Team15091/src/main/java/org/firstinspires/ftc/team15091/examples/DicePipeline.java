package org.firstinspires.ftc.team15091.examples;

import org.firstinspires.ftc.team15091.PipelineBase;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class DicePipeline extends PipelineBase {
    int threshold1 = 160, threshold2 = 40;
    long data;

    @Override
    public Mat processFrame(Mat input) {
        frameTemp = new Mat();
        contoursList.clear();
        Imgproc.cvtColor(input, frameTemp, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(frameTemp, frameTemp, new Size(15, 15), 0);
        Imgproc.threshold(frameTemp, frameTemp, threshold1, threshold2, Imgproc.THRESH_BINARY_INV);
        Imgproc.findContours(frameTemp, contoursList, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
        contoursList.removeIf((cont) -> {
            double area = Imgproc.contourArea(cont);
            if (area > 80d) {
                if (isInside(cont)) {
                    return false;
                }
            }
            return true;
        });
        data = contoursList.size();
//        Imgproc.cvtColor(frameTemp, input, Imgproc.COLOR_GRAY2BGR);
        Imgproc.drawContours(input, contoursList, -1, RED, 2, 8);
        Imgproc.rectangle(input, mask, GREEN, 2); // Draw rect
        Imgproc.putText(input,
                String.format("%d", data),
                mask.tl(),
                Imgproc.FONT_HERSHEY_SIMPLEX,
                0.7,
                GREEN,
                2);
        frameTemp.release();
        return input;
    }
}
