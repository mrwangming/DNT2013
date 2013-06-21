#ifndef MACHINELEARNING_H
#define MACHINELEARNING_H

#include <ModuleFramework/Module.h>

#include "Learners/GA.h"
#include "LearnToWalk.h"
#include "Representations/Perception/CameraMatrix.h"
#include "Representations/Infrastructure/VirtualVision.h"
#include <Representations/Infrastructure/InertialSensorData.h>
#include "Representations/Infrastructure/FieldInfo.h"
#include "Representations/Infrastructure/FrameInfo.h"
#include "Representations/Infrastructure/ButtonData.h"
#include "Representations/Modeling/RobotPose.h"
#include "Representations/Modeling/BodyState.h"
#include "Representations/Modeling/BallModel.h"
#include "Representations/Motion/Request/MotionRequest.h"
#include "Representations/Motion/Request/HeadMotionRequest.h"

#include <DebugCommunication/DebugCommandManager.h>

#include <string>
#include <iomanip>
#include <iostream>

BEGIN_DECLARE_MODULE(MachineLearning)
  REQUIRE(FrameInfo)
  REQUIRE(FieldInfo)
  REQUIRE(VirtualVision)
  REQUIRE(RobotPose)
  REQUIRE(CameraMatrix)
  REQUIRE(BallModel)
  REQUIRE(InertialSensorData)
  REQUIRE(BodyState)
  REQUIRE(ButtonData)

  PROVIDE(MotionRequest)
  PROVIDE(HeadMotionRequest)
END_DECLARE_MODULE(MachineLearning)

class MachineLearning : public MachineLearningBase, public DebugCommandExecutor
{
public:
    MachineLearning();
    ~MachineLearning();

    virtual void execute();
    virtual void executeDebugCommand(
            const std::string& command,
            const std::map<std::string, std::string>& arguments,
            std::ostream &outstreclassesam);
private:
  void setTests(unsigned int runningtime);

    LearnToWalk *ltw;
    bool finished;
    std::map<std::string, LearnToWalk::Test > tests;
};

#endif // MACHINELEARNING_H