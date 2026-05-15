package cn.edu.seig.vibemusic.service;

import cn.edu.seig.vibemusic.model.vo.SearchResultVO;
import cn.edu.seig.vibemusic.result.Result;

public interface ISearchService {
    Result<SearchResultVO> search(String keyword);
}
