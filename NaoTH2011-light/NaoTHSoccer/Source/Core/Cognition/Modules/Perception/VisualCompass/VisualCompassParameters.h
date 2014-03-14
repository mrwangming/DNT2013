/**
* @file VisualCompassFeature.h
*
* @author <a href="mailto:giorgosmethe@gmail.com">Georgios Methenitis</a>
*/

#ifndef VISUALCOMPASSPARAMETERS_H
#define VISUALCOMPASSPARAMETERS_H
#define _USE_MATH_DEFINES

// VisualCompass
#define COMPASS_WIDTH_MIN 0.49
#define COMPASS_WIDTH_MAX 0.80
#define COMPASS_FEATURE_NUMBER 10
#define COMPASS_DATA_FILE "compass.dat"
#define COMPASS_PARTICLE_UPDATE true

// VisualCompassFeature
// ...

// ColorDiscretizer
#define NUM_OF_COLORS 8
#define CLUSTER_DATA_FILE "clusters.dat"

// VisualGridMapProvider
#define GRID_X_LENGTH 5
#define GRID_Y_LENGTH 3
#define NUM_ANGLE_BINS 180
#define ANGLE_SIZE 360.0 / NUM_ANGLE_BINS

// WeightedExperts
#define SMOOTHING_FACTOR 0.1 //TODO test this value later
#define GRID_CELL_CONFIDENCE 0.51

#endif // VISUALCOMPASSPARAMETERS_H