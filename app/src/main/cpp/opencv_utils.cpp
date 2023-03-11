#include "opencv_utils.h"
#include "opencv2/imgproc.hpp"

using namespace cv;
using namespace std;

void meanShift(Mat src) {
    int spatialRadius = 30;
    int colorRadius = 50;
    int pyramidLevels = 3;
    pyrMeanShiftFiltering(
            src,
            src,
            spatialRadius,
            colorRadius,
            pyramidLevels
    );
}