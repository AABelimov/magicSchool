package hogwarts.ru.magicschool.service;

import hogwarts.ru.magicschool.dto.AvatarDto;
import hogwarts.ru.magicschool.entity.Avatar;
import hogwarts.ru.magicschool.entity.Student;
import hogwarts.ru.magicschool.mapper.AvatarMapper;
import hogwarts.ru.magicschool.repository.AvatarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final AvatarMapper avatarMapper;
    private final String avatarsDir;
    private static final Logger LOG = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(AvatarRepository avatarRepository,
                         AvatarMapper avatarMapper, @Value("${path.to.avatars.folder}") String avatarsDir) {
        this.avatarRepository = avatarRepository;
        this.avatarMapper = avatarMapper;
        this.avatarsDir = avatarsDir;
    }


    public void createAvatar(Student student, MultipartFile avatarFile) throws IOException {
        LOG.info("Was invoked method createAvatar");
        Path filePath = Path.of(avatarsDir, student + "." + StringUtils.getFilenameExtension(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = getAvatarByStudentID(student.getId());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    private Avatar getAvatarByStudentID(Long studentId) {
        LOG.info("Was invoked method getAvatarByStudentID");
        return avatarRepository.findByStudent_Id(studentId).orElse(new Avatar());
    }

    public Avatar getAvatarById(Long id) {
        LOG.info("Was invoked method getAvatarById");
        return avatarRepository.findById(id).orElseThrow();
    }

    public Collection<AvatarDto> getAvatars(Integer page, Integer pageSize) {
        LOG.info("Was invoked method getAvatars");
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent().stream()
                .map(avatarMapper::toDto)
                .toList();
    }
}
