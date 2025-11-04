package com.example.learningtracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.learningtracker.service.PomodoroService;
import com.example.learningtracker.service.PomodoroService.CalculationResult;

@RestController
@RequestMapping("/api/calculator")
public class PomodoroApiController {

    private final PomodoroService pomodoroService;

    // コンストラクタインジェクション
    public PomodoroApiController(PomodoroService pomodoroService) {
        this.pomodoroService = pomodoroService;
    }

    // --- エンドポイント 1: 学習時間からポモドーロ数を計算 ---
    /**
     * URL: /api/calculator/time-to-pomodoro?pureStudyMinutes={分}
     */
    @GetMapping("/time-to-pomodoro")
    public CalculationResult calculatePomodoroFromTime(@RequestParam("pureStudyMinutes") Integer pureStudyMinutes) {
        // nullや不正な値が渡された場合のバリデーション
        if (pureStudyMinutes == null || pureStudyMinutes <= 0) {
            // 例外をスローせず、計算サービスに任せるか、最小の結果を返す
            return new CalculationResult(0, 0); 
        }

        // Serviceに計算を依頼
        return pomodoroService.calculatePomodoroFromTime(pureStudyMinutes);
    }

    // --- エンドポイント 2: ポモドーロ数から学習時間（分）を計算 ---
    /**
     * URL: /api/calculator/pomodoro-to-time?pomodoroCount={ポモドーロ数}
     */
    @GetMapping("/pomodoro-to-time")
    public CalculationResult calculateTimeFromPomodoro(@RequestParam("pomodoroCount") Integer pomodoroCount) {
        // nullや不正な値が渡された場合のバリデーション
        if (pomodoroCount == null || pomodoroCount <= 0) {
            return new CalculationResult(0, 0);
        }

        // Serviceに計算を依頼
        return pomodoroService.calculateTimeFromPomodoro(pomodoroCount);
    }
}