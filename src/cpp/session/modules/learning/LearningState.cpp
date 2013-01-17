/*
 * LearningState.cpp
 *
 * Copyright (C) 2009-12 by RStudio, Inc.
 *
 * Unless you have received this program directly from RStudio pursuant
 * to the terms of a commercial license agreement with RStudio, then
 * this program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */


#include "LearningState.hpp"

#include <core/FilePath.hpp>
#include <core/Settings.hpp>

#include <session/SessionModuleContext.hpp>

using namespace core;

namespace session {
namespace modules { 
namespace learning {
namespace state {

namespace {
      
struct LearningState
{
   LearningState()
      : active(false)
   {
   }

   bool active;
   FilePath directory;
};

// write-through cache of learning state
LearningState s_learningState;

FilePath learningStatePath()
{
   FilePath path = module_context::userScratchPath().childPath("learning");
   Error error = path.ensureDirectory();
   if (error)
      LOG_ERROR(error);
   return path.childPath("learning-state");
}

void saveLearningState(const LearningState& state)
{
   // update write-through cache
   s_learningState = state;

   // save to disk
   Settings settings;
   Error error = settings.initialize(learningStatePath());
   if (error)
   {
      LOG_ERROR(error);
      return;
   }
   settings.beginUpdate();
   settings.set("active", state.active);
   settings.set("directory", state.directory.absolutePath());
   settings.endUpdate();
}

void loadLearningState()
{
   FilePath statePath = learningStatePath();
   if (statePath.exists())
   {
      Settings settings;
      Error error = settings.initialize(learningStatePath());
      if (error)
         LOG_ERROR(error);

      s_learningState.active = settings.getBool("active", false);
      s_learningState.directory = FilePath(settings.get("directory"));
   }
   else
   {
      s_learningState = LearningState();
   }
}

} // anonymous namespace


void init(const FilePath& directory)
{
   LearningState state;
   state.active = true;
   state.directory = directory;
   saveLearningState(state);
}

bool isActive()
{
   return s_learningState.active;
}

FilePath directory()
{
   return s_learningState.directory;
}

void clear()
{
   saveLearningState(LearningState());
}

json::Value asJson()
{
   json::Object stateJson;
   stateJson["active"] = s_learningState.active;
   stateJson["directory"] = s_learningState.directory.absolutePath();
   return stateJson;
}

Error initialize()
{
   loadLearningState();
   return Success();
}

} // namespace state
} // namespace learning
} // namespace modules
} // namesapce session
