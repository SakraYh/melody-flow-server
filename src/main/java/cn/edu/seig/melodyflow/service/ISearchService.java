package cn.edu.seig.melodyflow.service;

import cn.edu.seig.melodyflow.model.vo.SearchResultVO;
import cn.edu.seig.melodyflow.result.Result;

public interface ISearchService {
    Result<SearchResultVO> search(String keyword);
}
