package com.example.todolist.dao;

import java.util.ArrayList;
import java.util.List;
import com.example.todolist.common.Utils;
import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TodoDaoImpl implements TodoDao {
    private final EntityManager entityManager;

    // JPQLによる検索
    @Override
    public List<Todo> findByJPQL(TodoQuery todoQuery) {
        // ここを"todo"にすると実行時エラーになる
        StringBuilder sb = new StringBuilder("select t from Todo t where 1 = 1");
        List<Object> params = new ArrayList<>();
        int pos = 0;
        
        // 実行するJPQLの組み立て
        // 件名
        if (todoQuery.getTitle().length() > 0) {
            sb.append("and t.title like ?" + (++pos));         // 1.
            params.add("%" + todoQuery.getTitle() + "%");       // 2.
        }

        // 重要度
        if (todoQuery.getImportance() != -1) {
            sb.append("and t.importance = ?" + (++pos));       // 1.
            params.add(todoQuery.getImportance());             // 2.
        }

        // 緊急度
        if (todoQuery.getUrgency() != -1) {
            sb.append("and t.urgency = ?" + (++pos));          // 1.
            params.add(todoQuery.getUrgency());                // 2.
        }

        // 期限：開始～
        if (!todoQuery.getDeadlineFrom().equals("")) {
            sb.append("and t.deadline >= ?" + (++pos));                // 1.
            params.add(Utils. str2date(todoQuery.getDeadlineFrom()));  // 2.
        }
        
        // ～期限：終了で検索
        if (!todoQuery.getDeadlineTo().equals("")) {
            sb.append("and t.deadline <= ?" + (++pos));                // 1.
            params.add(Utils. str2date(todoQuery.getDeadlineTo()));    // 2.
        }
        
        // 完了
        if (todoQuery.getDone() != null && todoQuery.getDone().equals("Y")) {
            sb.append("and t.done = ?" + (++pos));                     // 1.
            params.add(todoQuery.getDone());                           // 2.
        }
        
        // order
        sb.append("order by id");

        Query query = entityManager.createQuery( sb.toString());        // 3.
        for (int i = 0; i < params.size(); ++ i) {                      // 4.
            query = query.setParameter(i + 1, params.get(i));
        }
        
        @SuppressWarnings("unchecked")
        List<Todo> list = query.getResultList();                        // 5.
        return list;
    }
    
    // Criteria APIによる検索
    @Override
    public List<Todo> findByCriteria(TodoQuery todoQuery) {
      return null;
    }
}
