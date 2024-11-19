package com.example.pratica2bimestre.service;

import com.example.pratica2bimestre.model.Tarefa;
import com.example.pratica2bimestre.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public Tarefa findById(int id){
        return tarefaRepository.findById(id).orElse(null);
    }

    public Tarefa addTarefa(Tarefa tarefa) {
        tarefaRepository.save(tarefa);
        return tarefa;
    }

    public List<List<Tarefa>> getAllTarefas() {
        List<List<Tarefa>> listaTarefasPorColuna = new ArrayList<>();
        listaTarefasPorColuna.add(tarefaRepository.findByStatusOrderByPrioridadeAsc("A fazer"));
        listaTarefasPorColuna.add(tarefaRepository.findByStatusOrderByPrioridadeAsc("Em progresso"));
        listaTarefasPorColuna.add(tarefaRepository.findByStatusOrderByPrioridadeAsc("Concluida"));
        return listaTarefasPorColuna;
    }

    public List<Tarefa> getTarefasByPrioridade(String prioridade) {
        return tarefaRepository.findByPrioridade(prioridade);
    }

    public List<Tarefa> getTarefasByDataLimite(Date dataFinal) {
        return tarefaRepository.findByDataFinal(dataFinal);
    }

    public List<List<Tarefa>> getRelatorio() {
        List<List<Tarefa>> listaTarefasPorColuna = new ArrayList<>();

        List<Tarefa> tarefasAtrasadas = tarefaRepository.findTarefasAtrasadas();

        List<Tarefa> tarefasAFazer = tarefasAtrasadas.stream()
                .filter(tarefa -> "A fazer".equals(tarefa.getStatus()))
                .collect(Collectors.toList());

        List<Tarefa> tarefasEmProgresso = tarefasAtrasadas.stream()
                .filter(tarefa -> "Em progresso".equals(tarefa.getStatus()))
                .collect(Collectors.toList());

        listaTarefasPorColuna.add(tarefasAFazer);
        listaTarefasPorColuna.add(tarefasEmProgresso);

        return listaTarefasPorColuna;
    }

    public Tarefa mudarStatus(int id) {
        Tarefa tarefa = this.findById(id);
        if(tarefa.getStatus().equals("A fazer")){
            tarefa.setStatus("Em progresso");
            tarefaRepository.save(tarefa);
        }else if (tarefa.getStatus().equals("Em progresso")){
            tarefa.setStatus("Concluido");
            tarefaRepository.save(tarefa);
        }
        return tarefa;
    }

    public Tarefa editarTarefa(int id, Tarefa tarefa) {
        Tarefa tarefaAnt = this.findById(id);
        tarefaAnt.setTitulo(tarefa.getTitulo());
        tarefaAnt.setDescricao(tarefa.getDescricao());
        tarefaAnt.setPrioridade(tarefa.getPrioridade());
        tarefaAnt.setDataFinal(tarefa.getDataFinal());
        tarefaRepository.save(tarefaAnt);
        return tarefaAnt;
    }

    public void deletarTarefa(int id) {
        tarefaRepository.deleteById(id);
    }
}
