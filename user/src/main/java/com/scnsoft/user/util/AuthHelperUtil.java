package com.scnsoft.user.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class AuthHelperUtil {

    public static long calculateSecondsToUnblock(Date blockedSince) {
        long different = new Date().getTime() - blockedSince.getTime();
        long differentInSeconds = TimeUnit.MILLISECONDS.toSeconds(different);

        return 300 - differentInSeconds;
    }

    public static boolean isBruteForce(Date lastFail) {
        long different = new Date().getTime() - lastFail.getTime();
        long differentInSeconds = TimeUnit.MILLISECONDS.toSeconds(different);

        return differentInSeconds < 30;
    }

}
