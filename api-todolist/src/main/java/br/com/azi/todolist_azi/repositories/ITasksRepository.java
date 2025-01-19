package br.com.azi.todolist_azi.repositories;

import br.com.azi.todolist_azi.domain.tasks.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITasksRepository extends JpaRepository<Tasks, Long> {
}
