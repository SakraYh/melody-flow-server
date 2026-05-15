package cn.edu.seig.vibemusic.controller;


import cn.edu.seig.vibemusic.model.dto.*;
import cn.edu.seig.vibemusic.model.entity.Artist;
import cn.edu.seig.vibemusic.model.entity.Playlist;
import cn.edu.seig.vibemusic.model.vo.ArtistNameVO;
import cn.edu.seig.vibemusic.model.vo.SongAdminVO;
import cn.edu.seig.vibemusic.model.vo.UserManagementVO;
import cn.edu.seig.vibemusic.result.PageResult;
import cn.edu.seig.vibemusic.result.Result;
import cn.edu.seig.vibemusic.service.*;
import cn.edu.seig.vibemusic.util.BindingResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "管理端", description = "管理员认证、用户/歌手/歌曲/歌单 CRUD")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IArtistService artistService;
    @Autowired
    private ISongService songService;
    @Autowired
    private IPlaylistService playlistService;
    @Autowired
    private MinioService minioService;

    // ==================== 管理员认证 ====================

    @Operation(summary = "管理员注册")
    @PostMapping("/register")
    public Result register(@RequestBody @Valid AdminDTO adminDTO, BindingResult bindingResult) {
        String errorMessage = BindingResultUtil.handleBindingResultErrors(bindingResult);
        if (errorMessage != null) {
            return Result.error(errorMessage);
        }
        return adminService.register(adminDTO);
    }

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public Result login(@RequestBody @Valid AdminDTO adminDTO, BindingResult bindingResult) {
        String errorMessage = BindingResultUtil.handleBindingResultErrors(bindingResult);
        if (errorMessage != null) {
            return Result.error(errorMessage);
        }
        return adminService.login(adminDTO);
    }

    @Operation(summary = "管理员登出")
    @PostMapping("/logout")
    public Result logout(@Parameter(description = "Bearer token") @RequestHeader("Authorization") String token) {
        return adminService.logout(token);
    }

    // ==================== 用户管理 ====================

    @Operation(summary = "获取用户总数")
    @GetMapping("/getAllUsersCount")
    public Result<Long> getAllUsersCount() {
        return userService.getAllUsersCount();
    }

    @Operation(summary = "获取用户列表（分页）")
    @PostMapping("/getAllUsers")
    public Result<PageResult<UserManagementVO>> getAllUsers(@RequestBody UserSearchDTO userSearchDTO) {
        return userService.getAllUsers(userSearchDTO);
    }

    @Operation(summary = "新增用户")
    @PostMapping("/addUser")
    public Result addUser(@RequestBody @Valid UserAddDTO userAddDTO, BindingResult bindingResult) {
        String errorMessage = BindingResultUtil.handleBindingResultErrors(bindingResult);
        if (errorMessage != null) {
            return Result.error(errorMessage);
        }
        return userService.addUser(userAddDTO);
    }

    @Operation(summary = "编辑用户信息")
    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        String errorMessage = BindingResultUtil.handleBindingResultErrors(bindingResult);
        if (errorMessage != null) {
            return Result.error(errorMessage);
        }
        return userService.updateUser(userDTO);
    }

    @Operation(summary = "启用/禁用用户")
    @PatchMapping("/updateUserStatus/{id}/{status}")
    public Result updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable("id") Long userId,
            @Parameter(description = "状态（0启用 1禁用）") @PathVariable("status") Integer userStatus) {
        return userService.updateUserStatus(userId, userStatus);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/deleteUser/{id}")
    public Result deleteUser(@Parameter(description = "用户ID") @PathVariable("id") Long userId) {
        return userService.deleteUser(userId);
    }

    @Operation(summary = "批量删除用户")
    @DeleteMapping("/deleteUsers")
    public Result deleteUsers(@Parameter(description = "用户ID列表") @RequestBody List<Long> userIds) {
        return userService.deleteUsers(userIds);
    }

    // ==================== 歌手管理 ====================

    @Operation(summary = "获取歌手总数")
    @GetMapping("/getAllArtistsCount")
    public Result<Long> getAllArtistsCount(
            @Parameter(description = "性别") @RequestParam(required = false) Integer gender,
            @Parameter(description = "地区") @RequestParam(required = false) String area) {
        return artistService.getAllArtistsCount(gender, area);
    }

    @Operation(summary = "获取歌手列表（分页）")
    @PostMapping("/getAllArtists")
    public Result<PageResult<Artist>> getAllArtists(@RequestBody ArtistDTO artistDTO) {
        return artistService.getAllArtistsAndDetail(artistDTO);
    }

    @Operation(summary = "新增歌手")
    @PostMapping("/addArtist")
    public Result addArtist(@RequestBody ArtistAddDTO artistAddDTO) {
        return artistService.addArtist(artistAddDTO);
    }

    @Operation(summary = "编辑歌手信息")
    @PutMapping("/updateArtist")
    public Result updateArtist(@RequestBody ArtistUpdateDTO artistUpdateDTO) {
        return artistService.updateArtist(artistUpdateDTO);
    }

    @Operation(summary = "更新歌手头像")
    @PatchMapping("/updateArtistAvatar/{id}")
    public Result updateArtistAvatar(
            @Parameter(description = "歌手ID") @PathVariable("id") Long artistId,
            @Parameter(description = "头像文件") @RequestParam("avatar") MultipartFile avatar) {
        String avatarUrl = minioService.uploadFile(avatar, "artists");
        return artistService.updateArtistAvatar(artistId, avatarUrl);
    }

    @Operation(summary = "删除歌手")
    @DeleteMapping("/deleteArtist/{id}")
    public Result deleteArtist(@Parameter(description = "歌手ID") @PathVariable("id") Long artistId) {
        return artistService.deleteArtist(artistId);
    }

    @Operation(summary = "批量删除歌手")
    @DeleteMapping("/deleteArtists")
    public Result deleteArtists(@Parameter(description = "歌手ID列表") @RequestBody List<Long> artistIds) {
        return artistService.deleteArtists(artistIds);
    }

    // ==================== 歌曲管理 ====================

    @Operation(summary = "获取歌曲总数")
    @GetMapping("/getAllSongsCount")
    public Result<Long> getAllSongsCount(@Parameter(description = "风格") @RequestParam(required = false) String style) {
        return songService.getAllSongsCount(style);
    }

    @Operation(summary = "获取所有歌手名称列表（下拉框用）")
    @GetMapping("/getAllArtistNames")
    public Result<List<ArtistNameVO>> getAllArtistNames() {
        return artistService.getAllArtistNames();
    }

    @Operation(summary = "按歌手查询歌曲列表")
    @PostMapping("/getAllSongsByArtist")
    public Result<PageResult<SongAdminVO>> getAllSongsByArtist(@RequestBody SongAndArtistDTO songDTO) {
        return songService.getAllSongsByArtist(songDTO);
    }

    @Operation(summary = "新增歌曲")
    @PostMapping("/addSong")
    public Result addSong(@RequestBody SongAddDTO songAddDTO) {
        return songService.addSong(songAddDTO);
    }

    @Operation(summary = "编辑歌曲信息")
    @PutMapping("/updateSong")
    public Result UpdateSong(@RequestBody SongUpdateDTO songUpdateDTO) {
        return songService.updateSong(songUpdateDTO);
    }

    @Operation(summary = "更新歌曲封面")
    @PatchMapping("/updateSongCover/{id}")
    public Result updateSongCover(
            @Parameter(description = "歌曲ID") @PathVariable("id") Long songId,
            @Parameter(description = "封面文件") @RequestParam("cover") MultipartFile cover) {
        String coverUrl = minioService.uploadFile(cover, "songCovers");
        return songService.updateSongCover(songId, coverUrl);
    }

    @Operation(summary = "更新歌曲音频")
    @PatchMapping("/updateSongAudio/{id}")
    public Result updateSongAudio(
            @Parameter(description = "歌曲ID") @PathVariable("id") Long songId,
            @Parameter(description = "音频文件") @RequestParam("audio") MultipartFile audio,
            @Parameter(description = "时长（秒）") @RequestParam("duration") String duration) {
        String audioUrl = minioService.uploadFile(audio, "songs");
        return songService.updateSongAudio(songId, audioUrl, duration);
    }

    @Operation(summary = "删除歌曲")
    @DeleteMapping("/deleteSong/{id}")
    public Result deleteSong(@Parameter(description = "歌曲ID") @PathVariable("id") Long songId) {
        return songService.deleteSong(songId);
    }

    @Operation(summary = "批量删除歌曲")
    @DeleteMapping("/deleteSongs")
    public Result deleteSongs(@Parameter(description = "歌曲ID列表") @RequestBody List<Long> songIds) {
        return songService.deleteSongs(songIds);
    }

    // ==================== 歌单管理 ====================

    @Operation(summary = "获取歌单总数")
    @GetMapping("/getAllPlaylistsCount")
    public Result<Long> getAllPlaylistsCount(@Parameter(description = "风格") @RequestParam(required = false) String style) {
        return playlistService.getAllPlaylistsCount(style);
    }

    @Operation(summary = "获取歌单列表（分页）")
    @PostMapping("/getAllPlaylists")
    public Result<PageResult<Playlist>> getAllPlaylists(@RequestBody PlaylistDTO playlistDTO) {
        return playlistService.getAllPlaylistsInfo(playlistDTO);
    }

    @Operation(summary = "新增歌单")
    @PostMapping("/addPlaylist")
    public Result addPlaylist(@RequestBody PlaylistAddDTO playlistAddDTO) {
        return playlistService.addPlaylist(playlistAddDTO);
    }

    @Operation(summary = "编辑歌单信息")
    @PutMapping("/updatePlaylist")
    public Result updatePlaylist(@RequestBody PlaylistUpdateDTO playlistUpdateDTO) {
        return playlistService.updatePlaylist(playlistUpdateDTO);
    }

    @Operation(summary = "更新歌单封面")
    @PatchMapping("/updatePlaylistCover/{id}")
    public Result updatePlaylistCover(
            @Parameter(description = "歌单ID") @PathVariable("id") Long playlistId,
            @Parameter(description = "封面文件") @RequestParam("cover") MultipartFile cover) {
        String coverUrl = minioService.uploadFile(cover, "playlists");
        return playlistService.updatePlaylistCover(playlistId, coverUrl);
    }

    @Operation(summary = "删除歌单")
    @DeleteMapping("/deletePlaylist/{id}")
    public Result deletePlaylist(@Parameter(description = "歌单ID") @PathVariable("id") Long playlistId) {
        return playlistService.deletePlaylist(playlistId);
    }

    @Operation(summary = "批量删除歌单")
    @DeleteMapping("/deletePlaylists")
    public Result deletePlaylists(@Parameter(description = "歌单ID列表") @RequestBody List<Long> playlistIds) {
        return playlistService.deletePlaylists(playlistIds);
    }

}
