package hogwarts.ru.magicschool.service;

import hogwarts.ru.magicschool.entity.Avatar;
import hogwarts.ru.magicschool.entity.Student;
import hogwarts.ru.magicschool.repository.AvatarRepository;
import hogwarts.ru.magicschool.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final String avatarsDir;

    public AvatarService(AvatarRepository avatarRepository,
                         @Value("${path.to.avatars.folder}") String avatarsDir) {
        this.avatarRepository = avatarRepository;
        this.avatarsDir = avatarsDir;
    }


    public Avatar createAvatar(Student student, MultipartFile avatarFile) throws IOException {
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
        return avatarRepository.save(avatar);
    }

    public Avatar getAvatarByStudentID(Long studentId) {
        return avatarRepository.findByStudent_Id(studentId).orElse(new Avatar());
    }

    public Avatar findById(Long id) {
        return avatarRepository.findById(id).orElseThrow();
    }
}
