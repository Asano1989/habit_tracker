package com.example.todolist.controller;

import java.util.List;

import javax.naming.Binding;
import jakarta.servlet.http.HttpSession;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;
import com.example.todolist.dao.TodoDaoImpl;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TodoListController {
    // 8章で追加
    private final HttpSession session;

    private final TodoRepository todoRepository;
    private final TodoService todoService;

    // 10章で追加
    @PersistenceContext                         // 1.
    private EntityManager entityManager;
    TodoDaoImpl todoDaoImpl;

    @PostConstruct                              // 3.
    public void init() {
        todoDaoImpl = new TodoDaoImpl(entityManager);
    }

    @GetMapping("/")
    public String home(Model model) {
        String message = "Hello, World!!";
        model.addAttribute("message", message);
        return "home";
    }

    // ToDO一覧表示
    @GetMapping("/todo")
    public ModelAndView showTodoList(ModelAndView mv) {
        // 一覧を検索して表示する
        mv.setViewName("todoList");
        List<Todo> todoList = todoRepository.findAll();
        mv.addObject("todoList", todoList);
        mv.addObject("todoQuery", new TodoQuery());         // ※9章で追加
        return mv;
    }

    // フォームに入力された条件でToDoを検索：9章で追加、10章で変更
    @PostMapping("/todo/query")
    public ModelAndView queryTodo(@ModelAttribute TodoQuery todoQuery,
                                    BindingResult result,
                                    ModelAndView mv) {
        mv.setViewName("todoList");
        List<Todo> todoList = null;
        if (todoService.isValid(todoQuery, result)) {
            // エラーがなければ検索
            // todoList = todoQueryService.query(todoQuery);
            // ↓
            // JPQLによる検索
            todoList = todoDaoImpl.findByJPQL(todoQuery);               // 4.
        }
        //mv.addObject("todoQuery", todoQuery);
        mv.addObject("todoList", todoList);
        return mv;
    }


    // 8章で追加：ToDo一覧画面から更新・削除対象のToDoを選ぶ
    @GetMapping("/todo/{id}")
    public ModelAndView todoById(@PathVariable(name="id") int id, ModelAndView mv) {
            mv.setViewName("todoForm");
            Todo todo = todoRepository.findById(id).get();  // 1.
            mv.addObject("todoData", todo);                 // ※b
            session.setAttribute("mode", "update");         // 2.
            return mv;
    }

    // ToDoフォーム表示
    // 処理1. ToDo一覧画面（todoList.html）で「新規追加」リンクがクリックされたとき
    @GetMapping("/todo/create")
    public ModelAndView createTodo(ModelAndView mv) {
        mv.setViewName("todoForm");                  // 1.
        mv.addObject("todoData", new TodoData());   // ※a
        session.setAttribute("mode", "create");     // 3.
        return mv;
    }

    // ToDo追加処理
    // 処理2. ToDo入力画面（todoForm.html）で「登録」ボタンがクリックされたとき
    @PostMapping("/todo/create")
    public String createTodo(@ModelAttribute @Validated TodoData todoData,  // 3.
                                    BindingResult result, Model model) {    // 8章：ModelAndViewではなくModelに
        // エラーチェック
        boolean isValid = todoService.isValid(todoData, result);    // 4.
        if(!result.hasErrors() && isValid) {
            // エラーなし
            Todo todo = todoData.toEntity();        // 5.
            todoRepository.saveAndFlush(todo);
            return "redirect:/todo";
        } else {
            // エラーあり
            // mv.setViewName("todoForm");              6. ：8章で削除
            // model.addAttribute("todoData", todoData);
            return "todoForm";
        }
    }

    // 8章で追加：更新用のメソッド
    @PostMapping("/todo/update")
    public String updateTodo(@ModelAttribute @Validated TodoData todoData,
                                    BindingResult result, Model model) {
        // エラーチェック
        boolean isValid = todoService.isValid(todoData, result);
        if(!result.hasErrors() && isValid) {
            // エラーなし
            Todo todo = todoData.toEntity();
            todoRepository.saveAndFlush(todo);        // 1.
            return "redirect:/todo";
        } else {
            // エラーあり
            // model.addAttribute("todoData", todoData);
            return "todoForm";
        }
    }

    // 8章で追加：削除用のメソッド
    @PostMapping("/todo/delete")
    public String deleteTodo(@ModelAttribute TodoData todoData) {
        todoRepository.deleteById(todoData.getId());
        return "redirect:/todo";
    }

    // ToDo一覧へ戻る
    // 処理3. ToDo入力画面で「キャンセル」ボタンがクリックされたとき
    @PostMapping("/todo/cancel")
    public String cancel() {
        return "redirect:/todo";
    }
}
