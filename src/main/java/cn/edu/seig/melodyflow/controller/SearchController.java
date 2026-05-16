package cn.edu.seig.melodyflow.controller;

import cn.edu.seig.melodyflow.model.vo.SearchResultVO;
import cn.edu.seig.melodyflow.result.Result;
import cn.edu.seig.melodyflow.service.ISearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "全文搜索", description = "跨表搜索歌曲、歌手、歌单")
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ISearchService searchService;

    @Operation(summary = "全文搜索", description = "基于 MySQL FULLTEXT INDEX 倒排索引，支持多关键词 AND 匹配，结果按类型分组返回（歌曲/歌手/歌单各最多20条），使用 Redis 缓存搜索结果")
    @GetMapping
    public Result<SearchResultVO> search(
            @Parameter(description = "搜索关键词，多词空格分隔（如：周杰伦 晴天）", required = true, example = "周杰伦 晴天")
            @RequestParam("keyword") String keyword) {
        return searchService.search(keyword);
    }
}
