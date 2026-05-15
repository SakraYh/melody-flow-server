-- ============================================================
-- Migration: 全文搜索索引
-- 为 tb_song、tb_artist、tb_playlist 添加 FULLTEXT INDEX
-- 支持在歌曲名、歌手名、专辑名、歌单标题上做中文全文搜索
-- ============================================================

-- tb_song: 歌名 + 专辑
ALTER TABLE tb_song
    ADD FULLTEXT INDEX ft_song_name_album (name, album);

-- tb_artist: 歌手姓名
ALTER TABLE tb_artist
    ADD FULLTEXT INDEX ft_artist_name (name);

-- tb_playlist: 歌单标题
ALTER TABLE tb_playlist
    ADD FULLTEXT INDEX ft_playlist_title (title);
