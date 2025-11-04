package com.example.learningtracker.service;

import org.springframework.stereotype.Service;

@Service
public class PomodoroService {

    // ポモドーロ1セットの時間（分）
    private static final int POMODORO_UNIT_MINUTES = 25;
    
    // API応答に使用する共通のDTO
    public static class CalculationResult {
        private final Integer pureStudyMinutes;
        private final Integer pomodoro;

        public CalculationResult(Integer pureStudyMinutes, Integer pomodoro) {
            this.pureStudyMinutes = pureStudyMinutes;
            this.pomodoro = pomodoro;
        }

        public Integer getPureStudyMinutes() { return pureStudyMinutes; }
        public Integer getPomodoro() { return pomodoro; }
    }

    /**
     * 学習時間（分）からポモドーロ数を算出
     * @param pureStudyMinutes 純学習時間（分）
     * @return 計算結果（ポモドーロ数を含む）
     */
    public CalculationResult calculatePomodoroFromTime(Integer pureStudyMinutes) {
        if (pureStudyMinutes == null || pureStudyMinutes <= 0) {
            return new CalculationResult(0, 0);
        }
        
        // 学習時間25分を1ポモドーロとして計算（切り捨て）
        int pomodoroCount = pureStudyMinutes / POMODORO_UNIT_MINUTES;
        
        return new CalculationResult(pureStudyMinutes, pomodoroCount);
    }

    /**
     * ポモドーロ数から学習時間（分）を算出
     * @param pomodoroCount ポモドーロ数
     * @return 計算結果（学習時間（分）を含む）
     */
    public CalculationResult calculateTimeFromPomodoro(Integer pomodoroCount) {
        if (pomodoroCount == null || pomodoroCount <= 0) {
            return new CalculationResult(0, 0);
        }
        
        // ポモドーロ数 * 25分 で純学習時間（分）を算出
        int pureStudyMinutes = pomodoroCount * POMODORO_UNIT_MINUTES;
        
        return new CalculationResult(pureStudyMinutes, pomodoroCount);
    }
}
