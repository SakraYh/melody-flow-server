package cn.edu.seig.vibemusic.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "搜索结果分组：按类型返回歌曲、歌手、歌单")
public class SearchResultVO {

    @Schema(description = "歌曲列表（最多20条）")
    private List<SearchResultItemVO> songs;

    @Schema(description = "歌手列表（最多20条）")
    private List<SearchResultItemVO> artists;

    @Schema(description = "歌单列表（最多20条）")
    private List<SearchResultItemVO> playlists;

    @Schema(description = "命中总数（三路合计）")
    private int totalHits;
}
