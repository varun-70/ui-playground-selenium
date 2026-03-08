package com.uiplayground.listeners;

import com.uiplayground.utils.VideoRecorderUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private static final int MAX_RETRY_COUNT = 2;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (count < MAX_RETRY_COUNT) {
                count++;
                return true;
            }
        }
        return false;
    }

    public int getCount() {
        return count;
    }
}

//public class RetryAnalyzer implements IRetryAnalyzer {
//    private int count = 0;
//    private static final int MAX_RETRY_COUNT = 2; // Test will run 1 + 2 = 3 times total
//
//    @Override
//    public boolean retry(ITestResult iTestResult) {
//        // 1. If the previous attempt was a retry and had a recording running, stop it now.
//        // This ensures the video from the FAILED retry is saved.
//        VideoRecorderUtils.stopRecording();
//
//        if (!iTestResult.isSuccess()) {
//            if (count < MAX_RETRY_COUNT) {
//                count++;
//
//                String testName = iTestResult.getName();
//                System.out.println("Retrying test: " + testName + " | Attempt: " + (count + 1));
//
//                // 2. Start recording for the UPCOMING retry attempt.
//                // We append the retry count to the filename for clarity.
//                VideoRecorderUtils.startRecording(testName + "_Retry_Attempt_" + count);
//
//                return true;
//            }
//        }
//
//        // If we are here, either the test passed or we reached max retries.
//        // The recording is stopped either by the next call to retry()
//        // or you should call VideoRecorderUtils.stopRecording() in your @AfterMethod
//        // to catch the very last attempt's video.
//        return false;
//    }
//
//    public int getCount() {
//        return count;
//    }
//}
