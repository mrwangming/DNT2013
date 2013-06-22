/**
* @file VisualGridMapProvider.h
*
* @author <a href="mailto:giorgosmethe@gmail.com">Georgios Methenitis</a>
* Definition of class VisualGridMapProvider
*/

#ifndef VISUALGRIDMAPPROVIDER_H
#define VISUALGRIDMAPPROVIDER_H

#include <vector>
#include "Representations/Infrastructure/FieldInfo.h"
#include "VisualCompassParameters.h"
#include <time.h> 


class VisualGridMapProvider
{

public:
    VisualCompassFeature gridmap  [GridxLength][GridyLength][AngleBins];

    // takes a vector with features and store them in the grid
    void storeFeature(VisualCompassFeature vcf)
    {
        Vector2<int> grid_pos;
        fieldPosToGridPos(vcf.source_position, grid_pos);
		//TODO compare reliability of old and new feature, change if needed
		int angle_bin = (int) vcf.orientation/angle_size;
		VisualCompassFeature model_vcf = gridmap[grid_pos.x][grid_pos.y][angle_bin];
		if (!model_vcf.valid){
			gridmap[grid_pos.x][grid_pos.y][angle_bin] = vcf;
		}else{
			double model_cert; 
			double vcf_cert;
			time_t timer;
			time(&timer);
			vcf.getCertainty(timer, model_cert);
			model_vcf.getCertainty(timer, vcf_cert);
			if (vcf_cert > model_cert){
				gridmap[grid_pos.x][grid_pos.y][angle_bin] = vcf;
			}
		}
    }

    /*
     *translates the field position to the grid position
     *where we are going to store the feature.
     *Assumes: field pos in the center 0,0
     */
    static void fieldPosToGridPos(Vector2<double> fieldPos, Vector2<int> &gridPos)
    {
        gridPos.x = floor(( fieldPos.x + FieldInfo().xLength * 0.5 ) / GridxLength);
        gridPos.y = floor(( fieldPos.y + FieldInfo().yLength * 0.5 ) / GridyLength);
    }


private:
    void removeTheOldest(std::vector<VisualCompassFeature> &gridMapFeatureVector)
    {
        //unsigned int minTime = INT_MAX;
        int minPos = 0;
        for(unsigned int i=0; i < gridMapFeatureVector.size(); i++)
        {
//            if(gridMapFeatureVector.at(i).time < minTime)
//            {
//                minTime = gridMapFeatureVector.at(i).time;
//                minPos = i;
//            }
        }
        gridMapFeatureVector.erase(gridMapFeatureVector.begin() + minPos);
    }


};

#endif // VISUALGRIDPMAPROVIDER_H
