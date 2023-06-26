cmake_minimum_required(VERSION 3.22.1)
project("opencvnativedemo")

set(OpenCV_STATIC 1)
set(OpenCV_DIR /Users/kudashov/tools/OpenCV-android-sdk/sdk/native/jni)

find_package(OpenCV REQUIRED)

add_library(opencvnativedemo SHARED opencv_utils.cpp native-lib.cpp)

find_library(log-lib log)
find_library(jnigraphics-lib jnigraphics)

target_link_libraries(
        opencvnativedemo
        ${OpenCV_LIBS}
        ${jnigraphics-lib}
        ${log-lib})