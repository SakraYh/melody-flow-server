package cn.edu.seig.vibemusic.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统一搜索结果项（歌曲/歌手/歌单共用）")
public class SearchResultItemVO {

    @Schema(description = "结果类型", example = "song", allowableValues = {"song", "artist", "playlist"})
    private String type;

    @Schema(description = "实体ID", example = "1")
    private Long id;

    @Schema(description = "标题（歌名/歌手名/歌单名）", example = "晴天")
    private String title;

    @Schema(description = "副标题（歌手名/地区/风格）", example = "周杰伦")
    private String subtitle;

    @Schema(description = "封面图URL", example = "http://localhost:9000/melody-flow-data/covers/1.jpg")
    private String coverUrl;

    @Schema(description = "额外信息（歌曲时长等）", example = "04:29")
    private String extra;
}
