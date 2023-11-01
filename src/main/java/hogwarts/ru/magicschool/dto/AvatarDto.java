package hogwarts.ru.magicschool.dto;

import java.util.Objects;

public class AvatarDto {

    private Long id;
    private long fileSize;
    private String mediaType;
    private String avatarUrl;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvatarDto avatarDto = (AvatarDto) o;
        return fileSize == avatarDto.fileSize && Objects.equals(id, avatarDto.id) && Objects.equals(mediaType, avatarDto.mediaType) && Objects.equals(avatarUrl, avatarDto.avatarUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileSize, mediaType, avatarUrl);
    }
}
