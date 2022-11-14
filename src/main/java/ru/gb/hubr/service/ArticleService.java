package ru.gb.hubr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gb.hubr.api.dto.ArticleDto;
import ru.gb.hubr.api.mapper.ArticleMapper;
import ru.gb.hubr.dao.AccountUserDao;
import ru.gb.hubr.dao.ArticleDao;
import ru.gb.hubr.entity.Article;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleDao articleDao;
    private final AccountUserDao accountUserDao;
    private final ArticleMapper articleMapper;

    public ArticleDto getArticleById(Long id) {
        return articleMapper.toArticleDto(articleDao.findById(id).orElse(null), accountUserDao);
    }

    public List<ArticleDto> getAllArticles() {
        return articleDao.findAll().stream()
                .map(article -> articleMapper.toArticleDto(article, accountUserDao))
                .collect(Collectors.toList());
    }

    public ArticleDto saveArticle(ArticleDto articleDto) {
        Article article = articleMapper.toArticle(articleDto, accountUserDao);
        return articleMapper.toArticleDto(articleDao.save(article), accountUserDao);
    }


}
