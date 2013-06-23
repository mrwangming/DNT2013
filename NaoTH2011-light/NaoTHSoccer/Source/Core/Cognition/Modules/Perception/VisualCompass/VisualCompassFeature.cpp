#include "VisualCompassFeature.h"

VisualCompassFeature::VisualCompassFeature()
{
}

VisualCompassFeature::~VisualCompassFeature()
{
    //nothing here.
}

void VisualCompassFeature::createFeatureFromScanLine(vector<Pixel> scanLine)
{
    if(scanLine.size() == 0) return;
    for(unsigned int i = 1; i < scanLine.size(); i++)
    {
        // here the knn will decide the class of each pixel
        int class_0 = 1; //class of the previous pixel
        int class_1 = 2; //class of the current pixel
        this->table[class_0][class_1]++;
    }
}

void VisualCompassFeature::getCertainty(time_t current_time, double &certainty)
{
	certainty = this->measurement_certainty * exp(difftime(this->time, current_time));
}

void VisualCompassFeature::compare(VisualCompassFeature vcf, double &similarity_measure)
{
    similarity_measure = 1.0; 
	//M_PI is the maximum distance possible between 2 angles
	similarity_measure *= abs(this->orientation - vcf.orientation)/M_PI;
	//TODO factor in the certainties
	//TODO normalise distance?
	similarity_measure *= sqrt(pow(this->source_position.x - vcf.source_position.x, 2) + pow(this->source_position.y - vcf.source_position.y, 2));
}
