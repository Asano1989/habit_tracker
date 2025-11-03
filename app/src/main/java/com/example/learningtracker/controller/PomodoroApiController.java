package com.example.learningtracker.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.learningtracker.service.PomodoroService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/calculator") // より汎用的なパスに変更することを推奨
public class PomodoroApiController {

    @Autowired
    private PomodoroService pomodoroService;

    // 複数の計算パターンに対応できるよう、計算に必要な全データを受け取る
    @GetMapping("/calculate")
    public Map<String, Long> calculate(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String stopTime,
            @RequestParam(required = false) String breakTime,
            @RequestParam(required = false) Long pureStudyMinutes, // 純学習時間（分）
            @RequestParam(required = false) Integer pomodoroCount) { // ポモドーロ数

        // サービス層に処理を委譲し、どの値が変更されたかに応じた結果を取得する
        // 戻り値は { "pureStudyMinutes": 120, "pomodoroCount": 4 } のような形式とする

        if (pomodoroCount != null) {
            // 要件3: ポモドーロ数から学習時間を算出
            long minutes = pomodoroService.calculateMinutesFromPomodoro(pomodoroCount);
            return Map.of("pureStudyMinutes", minutes, "pomodoroCount", (long)pomodoroCount);

        } else if (startTime != null && stopTime != null) {
            // 要件1: Start/Stop/Breakから学習時間とポモドーロ数を算出
            return pomodoroService.calculateFromTimes(startTime, stopTime, breakTime);

        } else if (pureStudyMinutes != null) {
            // 要件2: 学習時間のみからポモドーロ数を算出
            int count = pomodoroService.calculatePomodoroFromMinutes(pureStudyMinutes);
            return Map.of("pureStudyMinutes", pureStudyMinutes, "pomodoroCount", (long)count);
            
        } 
        
        // いずれのパターンにも当てはまらない場合は空の結果を返す
        return Map.of();
    }
}