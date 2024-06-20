package com.eric.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.demo.domain.Todos;
import com.eric.demo.repository.TodoRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/todos")
    public List<Todos> getAll(@RequestParam(required = false, defaultValue = "") String param) {
        return todoRepository.findAll();
    }
    
    @GetMapping("/todos/{id}")
    public Optional<Todos> getTodo(@PathVariable Integer id) {
        return todoRepository.findById(id);
    }

    @PostMapping("/todos")
    public Todos createTodo(@Valid @RequestBody Todos todo) {
        return todoRepository.save(todo);
    }

    @PutMapping("/todos/{id}")
    public Todos updateTodo(@PathVariable Integer id, @Valid @RequestBody Todos newTodo) {
        return todoRepository.findById(id)
                .<Todos>map(todo -> {
                    todo.setTitle(newTodo.getTitle());
                    todo.setCompleted(newTodo.getCompleted());
                    todo.setUsers(newTodo.getUsers());
                    todo.setUpdatedAt(newTodo.getUpdatedAt());
                    return todoRepository.save(todo);
                })
                .orElseGet(() -> {
                    newTodo.setId(id);
                    return todoRepository.save(newTodo);
                });
    }

    @DeleteMapping("/todos/{id}")
    public void delete(@PathVariable Integer id) {
        todoRepository.deleteById(id);
    }

}
