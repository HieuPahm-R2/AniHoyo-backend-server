package com.HieuPahm.AniHoyo.services.implement;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.HieuPahm.AniHoyo.model.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.model.dtos.SeasonDTO;
import com.HieuPahm.AniHoyo.model.entities.Episode;
import com.HieuPahm.AniHoyo.model.entities.Film;
import com.HieuPahm.AniHoyo.model.entities.Permission;
import com.HieuPahm.AniHoyo.model.entities.Season;
import com.HieuPahm.AniHoyo.repository.EpisodeRepository;
import com.HieuPahm.AniHoyo.repository.FilmRepository;
import com.HieuPahm.AniHoyo.repository.SeasonRepository;
import com.HieuPahm.AniHoyo.services.ISeasonService;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

@Service
public class SeasonService implements ISeasonService {
    @Autowired
    FilterBuilder fb;
    @Autowired
    private FilterParser filterParser;
    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    private Set<String> sessionViewCache = Collections.synchronizedSet(new HashSet<>());

    private final ModelMapper modelMapper;
    private final SeasonRepository seasonRepository;
    private final FilmRepository filmRepository;
    private final EpisodeRepository episodeRepository;

    public SeasonService(ModelMapper modelMapper, SeasonRepository seasonRepository,
            FilmRepository filmRepository, EpisodeRepository episodeRepository) {
        this.modelMapper = modelMapper;
        this.seasonRepository = seasonRepository;
        this.filmRepository = filmRepository;
        this.episodeRepository = episodeRepository;
    }

    @Override
    public SeasonDTO insert(SeasonDTO dto) {
        if (dto.getFilm() != null) {
            Optional<Film> check = this.filmRepository.findById(dto.getFilm().getId());
            if (check.isPresent()) {
                dto.setFilm(check.get());
            }
        }
        return modelMapper.map(
                this.seasonRepository.save(modelMapper.map(dto, Season.class)), SeasonDTO.class);
    }

    @Override
    public SeasonDTO getById(Long id) {
        return this.modelMapper.map(
                this.seasonRepository.findById(id).orElseThrow(
                        () -> new NoSuchElementException("Not Found")),
                SeasonDTO.class);
    }

    @Override
    public SeasonDTO update(SeasonDTO dto) throws BadActionException {
        Optional<Season> check = this.seasonRepository.findById(dto.getId());
        if (check.isEmpty()) {
            throw new BadActionException("Not Found");
        }
        Season season = check.get();
        season.setSeasonName(dto.getSeasonName());
        season.setReleaseYear(dto.getReleaseYear());
        return modelMapper.map(
                this.seasonRepository.save(season), SeasonDTO.class);
    }

    @Override
    public void delete(Long id) {
        this.seasonRepository.deleteById(id);
    }

    public PaginationResultDTO fetchSeasonsByFilm(Long filmId, Pageable pageable) {
        // Tạo filter để lấy season theo filmId
        FilterNode node = filterParser.parse("film.id=" + filmId);
        FilterSpecification<Season> spec = filterSpecificationConverter.convert(node);
        Page<Season> pageCheck = this.seasonRepository.findAll(spec, pageable);

        PaginationResultDTO ans = new PaginationResultDTO();
        PaginationResultDTO.Meta mt = new PaginationResultDTO.Meta();

        mt.setPage(pageCheck.getNumber() + 1);
        mt.setPageSize(pageCheck.getSize());
        mt.setTotal(pageCheck.getTotalElements());
        mt.setPages(pageCheck.getTotalPages());
        ans.setMeta(mt);

        List<SeasonDTO> res = pageCheck.getContent().stream()
                .map(item -> modelMapper.map(item, SeasonDTO.class))
                .collect(Collectors.toList());
        ans.setResult(res);
        return ans;
    }

    public PaginationResultDTO fetchAll(Specification<Season> spec, Pageable pageable) {
        Page<Season> page = this.seasonRepository.findAll(spec, pageable);
        PaginationResultDTO rs = new PaginationResultDTO();
        PaginationResultDTO.Meta mt = new PaginationResultDTO.Meta();

        mt.setPage(page.getNumber() + 1);
        mt.setPageSize(page.getSize());
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(page.getContent());
        return rs;
    }

    public List<SeasonDTO> getTop5SeasonsByViews() {
        List<Season> topSeasons = seasonRepository.findTop5ByOrderByViewCountDesc();
        return topSeasons.stream()
                .map(season -> modelMapper.map(season, SeasonDTO.class))
                .collect(Collectors.toList());
    }

    public void increaseViewOnce(Long ssId, String sessionId) {
        String key = ssId + "-" + sessionId;
        if (sessionViewCache.contains(key))
            return; // đã tăng``

        Episode ep = episodeRepository.findById(ssId)
                .orElseThrow(() -> new NoSuchElementException("Not found"));
        Season season = ep.getSeason();
        season.setViewCount(season.getViewCount() + 1);
        seasonRepository.save(season);
        sessionViewCache.add(key);
        // (Redis thì ngon hơn)
    }
}
