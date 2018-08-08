package ru.forumcalendar.forumcalendar.service.base;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.forumcalendar.forumcalendar.service.UploadsService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class BaseUploadsService implements UploadsService {

    private static final String SEP = "/";

    @Value("${my.uploadDirectory}")
    private String uploadDirectory;

    @Override
    public Optional<File> upload(MultipartFile mFile) {
        return upload(mFile, null);
    }

    @Override
    public Optional<File> upload(MultipartFile mFile, String name) {

        if (mFile == null)
            throw new IllegalArgumentException("mFile should not be null");

        if (mFile.isEmpty())
            return Optional.empty();

        File newFile = null;

        try {
            String hashString = DigestUtils.md5Hex(name == null ? mFile.getBytes() : name.getBytes());
            String ext = '.' + FilenameUtils.getExtension(mFile.getOriginalFilename());

            newFile = ResourceUtils.getFile(uploadDirectory + SEP + hashString + ext);

            if (newFile.exists() || newFile.createNewFile()) {
                FileUtils.writeByteArrayToFile(newFile, mFile.getBytes());
            } else {
                newFile = null;
            }
        } catch (IOException ignore) {
        }

        return Optional.ofNullable(newFile);
    }

    @Override
    public Optional<File> upload(String url, String name) {

        if (name == null)
            throw new IllegalArgumentException("name should not be null");

        if (url == null)
            return Optional.empty();

        File newFile = null;

        try {
            String hashString = DigestUtils.md5Hex(name.getBytes());
            String ext = '.' + FilenameUtils.getExtension(url);

            newFile = ResourceUtils.getFile(uploadDirectory + SEP + hashString + ext);

            if (newFile.exists() || newFile.createNewFile()) {
                FileUtils.copyURLToFile(new URL(url), newFile);
            } else {
                newFile = null;
            }
        } catch (IOException ignore) {
        }

        return Optional.ofNullable(newFile);
    }

    @Override
    public boolean delete(String name) {
        try {
            return Files.deleteIfExists(new File(uploadDirectory + SEP + name).toPath());
        } catch (IOException ignore) {
        }
        return false;
    }
}
