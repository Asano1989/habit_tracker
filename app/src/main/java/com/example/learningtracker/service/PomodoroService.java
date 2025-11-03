package com.example.learningtracker.service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class PomodoroService {
    private static final int POMODORO_UNIT_MINUTES = 25;

    // A. 純学習時間からポモドーロ数を計算する汎用メソッド
    public int calculatePomodoroFromMinutes(long pureStudyMinutes) {
        if (pureStudyMinutes <= 0) return 0;
        return (int) (pureStudyMinutes / POMODORO_UNIT_MINUTES);
    }
    
    // B. Start, Stop, Break から計算
    public Map<String, Long> calculateFromTimes(String startTimeStr, String stopTimeStr, String breakTimeStr) {
        // 1. "HH:mm" 形式を LocalTime オブジェクトに変換
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime stopTime = LocalTime.parse(stopTimeStr);
        LocalTime breakTime = LocalTime.parse(breakTimeStr);

        // 2. 総学習時間（分）を計算
        Duration totalDuration = Duration.between(startTime, stopTime);

        // stopTime < startTime の場合（日を跨ぐ場合）
        if (totalDuration.isNegative()) {
            totalDuration = totalDuration.plusDays(1);
        }

        // 3. 休憩時間（分）を計算：HoursとMinutesに分けて分に変換
        long breakMinutes = breakTime.getHour() * 60L + breakTime.getMinute();

        // 4. 純粋な学習時間（分）を計算
        // 総学習時間（分）
        long totalStudyMinutes = totalDuration.toMinutes();
        long pureStudyMinutes = totalStudyMinutes - breakMinutes;

        // 5. ポモドーロ数を計算 (切り捨て)
        int pomodoroCount = calculatePomodoroFromMinutes(pureStudyMinutes);

        return Map.of(
            "pureStudyMinutes", pureStudyMinutes,
            "pomodoroCount", (long)pomodoroCount
        );
    }

    // C. ポモドーロ数から学習時間（分）を逆算
    public long calculateMinutesFromPomodoro(int pomodoroCount) {
        return (long) pomodoroCount * POMODORO_UNIT_MINUTES;
    }
}

