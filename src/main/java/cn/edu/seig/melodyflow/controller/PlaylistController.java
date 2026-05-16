package cn.edu.seig.melodyflow.controller;


import cn.edu.seig.melodyflow.model.dto.PlaylistDTO;
import cn.edu.seig.melodyflow.model.vo.PlaylistDetailVO;
import cn.edu.seig.melodyflow.model.vo.PlaylistVO;
import cn.edu.seig.melodyflow.result.PageResult;
import cn.edu.seig.melodyflow.result.Result;
import cn.edu.seig.melodyflow.service.IPlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "歌单管理", description = "歌单查询、推荐、详情接口")
@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    private IPlaylistService playlistService;

    @Operation(summary = "获取歌单列表（分页）", description = "支持按标题、风格筛选")
    @PostMapping("/getAllPlaylists")
    public Result<PageResult<PlaylistVO>> getAllPlaylists(@RequestBody @Valid PlaylistDTO playlistDTO) {
        return playlistService.getAllPlaylists(playlistDTO);
    }

    @Operation(summary = "获取推荐歌单")
    @GetMapping("/getRecommendedPlaylists")
    public Result<List<PlaylistVO>> getRandomPlaylists(HttpServletRequest request) {
        return playlistService.getRecommendedPlaylists(request);
    }

    @Operation(summary = "获取歌单详情", description = "包含歌单内歌曲列表和评论")
    @GetMapping("/getPlaylistDetail/{id}")
    public Result<PlaylistDetailVO> getPlaylistDetail(
            @Parameter(description = "歌单ID") @PathVariable("id") Long playlistId,
            HttpServletRequest request) {
        return playlistService.getPlaylistDetail(playlistId, request);
    }


}
