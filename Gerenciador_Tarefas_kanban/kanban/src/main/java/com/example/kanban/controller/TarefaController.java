package com.example.kanban.controller;

import com.example.kanban.model.Tarefa;
import com.example.kanban.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping
    public List<Tarefa> getAllTasks() {
        return tarefaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> getTaskById(@PathVariable Long id) {
        return tarefaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Tarefa createTask(@RequestBody Tarefa task) {
        return tarefaService.save(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        tarefaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/move")
    public ResponseEntity<Tarefa> moveTask(@PathVariable Long id) {
        try {
            Tarefa updatedTask = tarefaService.moveTask(id);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> updateTask(@PathVariable Long id, @RequestBody Tarefa novosDados) {
        try {
            Tarefa updatedTask = tarefaService.editarTarefa(id, novosDados);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
