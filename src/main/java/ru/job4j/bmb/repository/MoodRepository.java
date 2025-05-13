package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.bmb.model.Mood;

import java.util.List;
import java.util.Optional;

public interface MoodRepository extends CrudRepository<Mood, Long> {
    List<Mood> findAll();

    Mood findMoodById(Long id);
}
