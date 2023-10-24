package hogwarts.ru.magicschool.mapper;

import hogwarts.ru.magicschool.dto.AvatarDto;
import hogwarts.ru.magicschool.entity.Avatar;
import org.springframework.stereotype.Component;

@Component
public class AvatarMapper {

    public AvatarDto toDto(Avatar avatar) {
        AvatarDto avatarDto = new AvatarDto();
        avatarDto.setId(avatar.getId());
        avatarDto.setFileSize(avatar.getFileSize());
        avatarDto.setMediaType(avatar.getMediaType());
        avatarDto.setAvatarUrl("http://localhost:8080" + "/avatar/" + avatar.getId() + "/avatar-from-db");
        return avatarDto;
    }
}
