package cn.edu.seig.melodyflow.controller;


import cn.edu.seig.melodyflow.model.dto.SongDTO;
import cn.edu.seig.melodyflow.model.vo.SongDetailVO;
import cn.edu.seig.melodyflow.model.vo.SongVO;
import cn.edu.seig.melodyflow.result.PageResult;
import cn.edu.seig.melodyflow.result.Result;
import cn.edu.seig.melodyflow.service.ISongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "歌曲管理", description = "歌曲查询、推荐、详情接口")
@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private ISongService songService;

    @Operation(summary = "获取歌曲列表（分页）", description = "支持按歌名、歌手、专辑筛选，OFFSET 分页")
    @PostMapping("/getAllSongs")
    public Result<PageResult<SongVO>> getAllSongs(@RequestBody @Valid SongDTO songDTO, HttpServletRequest request) {
        return songService.getAllSongs(songDTO, request);
    }

    @Operation(summary = "获取推荐歌曲", description = "根据用户收藏风格个性化推荐，未登录时随机返回 20 首")
    @GetMapping("/getRecommendedSongs")
    public Result<List<SongVO>> getRecommendedSongs(HttpServletRequest request) {
        return songService.getRecommendedSongs(request);
    }

    @Operation(summary = "获取歌曲详情")
    @GetMapping("/getSongDetail/{id}")
    public Result<SongDetailVO> getSongDetail(
            @Parameter(description = "歌曲ID") @PathVariable("id") Long songId,
            HttpServletRequest request) {
        return songService.getSongDetail(songId, request);
    }


}
