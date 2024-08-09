package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaRaringService {
    private final MpaRatingStorage mpaRatingRepository;


    public List<Mpa> getAll() {
        return mpaRatingRepository.getAllMpas();
    }

    public Mpa getMpaById(int id) {
        return mpaRatingRepository.getMpaById(id);
    }

}
