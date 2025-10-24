package com.example.todolist.controller;

import java.util.List;

import javax.naming.Binding;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TodoListController {
    private final TodoRepository TodoRepository;
    private final TodoService todoService;

    @GetMapping("/")
    public String home(Model model) {
        System.out.println("--- HomeController: home()メソッドが実行されました ---");
        String message = "Hello, World!!";
        model.addAttribute("message", message);
        return "home";
    }

    // ToDO一覧表示
    @GetMapping("/todo")
    public ModelAndView showTodoList(ModelAndView mv) {
        mv.setViewName("todoList");
        List<Todo> todoList = TodoRepository.findAll();
        mv.addObject("todoList", todoList);
        return mv;
    }

    // ToDoフォーム表示
    // 処理1. ToDo一覧画面（todoList.html）で「新規追加」リンクがクリックされたとき
    @GetMapping("/todo/create")
    public ModelAndView createTodo(ModelAndView mv) {
        mv.setViewName("todoForm");                  // 1.
        mv.addObject("todoData", new TodoData());   // 2.
        return mv;
    }

    // ToDo追加処理
    // 処理2. ToDo入力画面（todoForm.html）で「登録」ボタンがクリックされたとき
    @PostMapping("/todo/create")
    public ModelAndView createTodo(@ModelAttribute @Validated TodoData todoData,  //3.
                                    BindingResult result, ModelAndView mv) {
        // エラーチェック
        boolean isValid = todoService.isValid(todoData, result);    // 4.
        if(!result.hasErrors() && isValid) {
            // エラーなし
            Todo todo = todoData.toEntity();        // 5.
            TodoRepository.saveAndFlush(todo);
            return showTodoList(mv);
        } else {
            // エラーアリ
            mv.setViewName("todoForm");             // 6.
            // mv.addObject("todoData", todoData);
            return mv;
        }
    }

    // ToDo一覧へ戻る
    // 処理3. ToDo入力画面で「キャンセル」ボタンがクリックされたとき
    @PostMapping("/todo/cancel")
    public String cancel() {
        return "redirect:/todo";
    }
}
