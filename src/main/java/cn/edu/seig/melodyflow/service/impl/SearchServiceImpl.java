package cn.edu.seig.melodyflow.service.impl;

import cn.edu.seig.melodyflow.mapper.SearchMapper;
import cn.edu.seig.melodyflow.model.vo.SearchResultItemVO;
import cn.edu.seig.melodyflow.model.vo.SearchResultVO;
import cn.edu.seig.melodyflow.result.Result;
import cn.edu.seig.melodyflow.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "searchCache")
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private SearchMapper searchMapper;

    private static final int MAX_RESULTS_PER_TYPE = 20;

    @Override
    @Cacheable(key = "#keyword")
    public Result<SearchResultVO> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return Result.success(new SearchResultVO(List.of(), List.of(), List.of(), 0));
        }

        String ftKeyword = buildFulltextKeyword(keyword.trim());

        List<SearchResultItemVO> songs = searchMapper.fulltextSearchSongs(ftKeyword, MAX_RESULTS_PER_TYPE);
        List<SearchResultItemVO> artists = searchMapper.fulltextSearchArtists(ftKeyword, MAX_RESULTS_PER_TYPE);
        List<SearchResultItemVO> playlists = searchMapper.fulltextSearchPlaylists(ftKeyword, MAX_RESULTS_PER_TYPE);

        int totalHits = songs.size() + artists.size() + playlists.size();
        return Result.success(new SearchResultVO(songs, artists, playlists, totalHits));
    }

    /**
     * 将用户输入的关键词转换为 MySQL BOOLEAN MODE 全文搜索语法
     * 每个词前面加 + 表示必须包含，多个词之间空格分隔表示 AND 逻辑
     */
    private String buildFulltextKeyword(String raw) {
        String[] terms = raw.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String term : terms) {
            if (term.length() > 0) {
                sb.append("+").append(term).append(" ");
            }
        }
        return sb.toString().trim();
    }
}
