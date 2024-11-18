package com.example.kanban.service;

import com.example.kanban.model.Tarefa;
import com.example.kanban.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;

    public List<Tarefa> findAll() {
        return tarefaRepository.findAll(Sort.by(Sort.Order.asc("priority")));
    }

    public Optional<Tarefa> findById(Long id) {
        return tarefaRepository.findById(id);
    }

    public Tarefa save(Tarefa task) {
        task.setStatus("A fazer");
        return tarefaRepository.save(task);
    }

    public void deleteById(Long id) {
        tarefaRepository.deleteById(id);
    }

    public Tarefa moveTask(Long id) {
        Optional<Tarefa> optionalTask = tarefaRepository.findById(id);

        if (optionalTask.isPresent()) {
            Tarefa task = optionalTask.get();
            String currentStatus = task.getStatus();

            switch (currentStatus) {
                case "A fazer":
                    task.setStatus("Em processo");
                    break;
                case "Em proceso":
                    task.setStatus("Concluído");
                    break;
                case "Concluído":
                    throw new IllegalStateException("A tarefa já está no último status: Concluído");
                default:
                    throw new IllegalArgumentException("Status inválido: " + currentStatus);
            }

            return tarefaRepository.save(task);
        } else {
            throw new IllegalArgumentException("Tarefa não encontrada com ID: " + id);
        }
    }

    public Tarefa editarTarefa(Long id, Tarefa novosDados) {
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (novosDados.getTitulo() != null) {
            tarefaExistente.setTitulo(novosDados.getTitulo());
        }
        if (novosDados.getDescricao() != null) {
            tarefaExistente.setDescricao(novosDados.getDescricao());
        }
        if (novosDados.getPriority() != null) {
            tarefaExistente.setPriority(novosDados.getPriority());
        }
        if (novosDados.getStatus() != null) {
            tarefaExistente.setStatus(novosDados.getStatus());
        }

        return tarefaRepository.save(tarefaExistente);
    }
}
