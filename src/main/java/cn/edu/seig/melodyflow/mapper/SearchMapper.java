package cn.edu.seig.melodyflow.mapper;

import cn.edu.seig.melodyflow.model.vo.SearchResultItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchMapper {

    List<SearchResultItemVO> fulltextSearchSongs(@Param("keyword") String keyword,
                                                  @Param("limit") int limit);

    List<SearchResultItemVO> fulltextSearchArtists(@Param("keyword") String keyword,
                                                    @Param("limit") int limit);

    List<SearchResultItemVO> fulltextSearchPlaylists(@Param("keyword") String keyword,
                                                      @Param("limit") int limit);
}
