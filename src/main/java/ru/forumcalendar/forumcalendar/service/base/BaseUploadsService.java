package ru.forumcalendar.forumcalendar.service.base;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.forumcalendar.forumcalendar.service.UploadsService;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class BaseUploadsService implements UploadsService {

    private static String SEP = "/";

    @Value("${my.uploadDirectory}")
    private String uploadDirectory;

    @Override
    public Optional<File> upload(MultipartFile mFile, String name) {

        if (mFile == null)
            throw new IllegalArgumentException("mFile should not be null");

        if (mFile.isEmpty())
            return Optional.empty();

        File newFile = null;
        String mName = mFile.getOriginalFilename();

        try {
            byte[] nameBytes = name == null ? mFile.getBytes() : name.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(nameBytes);
            String hashString = new BigInteger(1, digest).toString(16);

            newFile = ResourceUtils.getFile(uploadDirectory + SEP + hashString + mName.substring(mName.lastIndexOf('.')));

            if (newFile.exists() || newFile.createNewFile()) {
                mFile.transferTo(newFile);
            } else {
                newFile = null;
            }

        } catch (IOException | NoSuchAlgorithmException ignore) {
            ignore.printStackTrace();
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
            byte[] nameBytes = name.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(nameBytes);
            String hashString = new BigInteger(1, digest).toString(16);

            newFile = ResourceUtils.getFile(uploadDirectory + SEP + hashString + url.substring(url.lastIndexOf('.')));

            if (newFile.exists() || newFile.createNewFile()) {
                FileUtils.copyURLToFile(new URL(url), newFile);
            } else {
                newFile = null;
            }

        } catch (IOException | NoSuchAlgorithmException ignore) {
            ignore.printStackTrace();
        }

        return Optional.ofNullable(newFile);
    }
}
