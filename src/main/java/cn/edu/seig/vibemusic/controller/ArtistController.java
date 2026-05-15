package cn.edu.seig.vibemusic.controller;


import cn.edu.seig.vibemusic.model.dto.ArtistDTO;
import cn.edu.seig.vibemusic.model.vo.ArtistDetailVO;
import cn.edu.seig.vibemusic.model.vo.ArtistVO;
import cn.edu.seig.vibemusic.result.PageResult;
import cn.edu.seig.vibemusic.result.Result;
import cn.edu.seig.vibemusic.service.IArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "歌手管理", description = "歌手查询、详情接口")
@RestController
@RequestMapping("/artist")
public class ArtistController {

    @Autowired
    private IArtistService artistService;

    @Operation(summary = "获取歌手列表（分页）", description = "支持按姓名、性别、地区筛选")
    @PostMapping("/getAllArtists")
    public Result<PageResult<ArtistVO>> getAllArtists(@RequestBody @Valid ArtistDTO artistDTO) {
        return artistService.getAllArtists(artistDTO);
    }

    @Operation(summary = "获取随机歌手", description = "随机返回 10 位歌手")
    @GetMapping("/getRandomArtists")
    public Result<List<ArtistVO>> getRandomArtists() {
        return artistService.getRandomArtists();
    }

    @Operation(summary = "获取歌手详情", description = "包含歌手作品列表")
    @GetMapping("/getArtistDetail/{id}")
    public Result<ArtistDetailVO> getArtistDetail(
            @Parameter(description = "歌手ID") @PathVariable("id") Long artistId,
            HttpServletRequest request) {
        return artistService.getArtistDetail(artistId, request);
    }

}
