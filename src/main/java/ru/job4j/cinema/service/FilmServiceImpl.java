package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.*;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;

    private final FilmSessionRepository filmSessionRepository;

    private final TicketRepository ticketRepository;

    private final HallRepository hallRepository;

    private final GenreRepository genreRepository;

    private final FileRepository fileRepository;

    public FilmServiceImpl(FilmRepository filmRepository,
                           FilmSessionRepository filmSessionRepository,
                           TicketRepository ticketRepository,
                           HallRepository hallRepository,
                           GenreRepository genreRepository,
                           FileRepository fileRepository) {
        this.filmRepository = filmRepository;
        this.filmSessionRepository = filmSessionRepository;
        this.ticketRepository = ticketRepository;
        this.hallRepository = hallRepository;
        this.genreRepository = genreRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public Optional<FilmDto> findFilmById(int id) {
        Optional<Film> optionalFilm = filmRepository.findById(id);
        if (optionalFilm.isEmpty()) {
            return Optional.empty();
        }
        FilmDto filmDto = new FilmDto(optionalFilm.get().getName(),
                optionalFilm.get().getDescription(),
                optionalFilm.get().getYear(),
                genreRepository.findById(optionalFilm.get().getGenreId()).get().getName(),
                optionalFilm.get().getMinimalAge(),
                optionalFilm.get().getDurationInMinutes(),
                fileRepository.findById(id).get().getPath());
        filmDto.setId(optionalFilm.get().getId());
        return Optional.of(filmDto);
    }

    @Override
    public Collection<FilmDto> findAllFilms() {
        ConcurrentMap<Integer, FilmDto> filmsDto = new ConcurrentHashMap<>();
        FilmDto filmDto;
        for (Film film : filmRepository.findAll()) {
            int id = film.getId();
            filmDto = new FilmDto(film.getName(),
                    film.getDescription(),
                    film.getYear(),
                    genreRepository.findById(film.getGenreId()).get().getName(),
                    film.getMinimalAge(),
                    film.getDurationInMinutes(),
                    fileRepository.findById(id).get().getPath());
            filmDto.setId(id);
            filmsDto.put(id, filmDto);

        }
        return filmsDto.values();
    }
}
