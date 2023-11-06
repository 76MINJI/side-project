package com.springboot.security.service.impl;

import com.springboot.security.data.dto.MediaDescriptorDto;
import com.springboot.security.service.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class LocalMediaServiceImpl implements MediaService {
    private static final Logger logger = LoggerFactory.getLogger(LocalMediaServiceImpl.class);
    private final String basePath = "./media";

    @Override
    public MediaDescriptorDto saveFile(MultipartFile file) {
        return this.saveToDir(file);
    }

    @Override
    public Collection<MediaDescriptorDto> saveFileBulk(MultipartFile[] files) {
        Collection<MediaDescriptorDto> resultList = new ArrayList<>();
        for (MultipartFile file: files) {
            resultList.add(this.saveToDir(file));
        }
        return resultList;
    }

    @Override
    public byte[] getFileAsBytes(String resourcePath) {
        try {
            return Files.readAllBytes(Path.of(basePath, resourcePath));
        } catch (IOException e) {
            logger.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

//    private MediaDescriptorDto saveToDir(MultipartFile file) {
//        MediaDescriptorDto descriptorDto = new MediaDescriptorDto();
//        descriptorDto.setStatus(200);
//        descriptorDto.setOriginalName(file.getOriginalFilename());
//        try {
//            LocalDateTime now = LocalDateTime.now();
//            String targetDir = Path.of(
//                    basePath,
//                    now.format(DateTimeFormatter.BASIC_ISO_DATE)).toString().replace("\\", "/");
//            String newFileName =
//                    now.format(DateTimeFormatter.ofPattern("HHmmss"))
//                            + "_"
//                            + file.getOriginalFilename();
//
//            File dirNow = new File(targetDir);
//            if (!dirNow.exists()) dirNow.mkdir();
//
//            file.transferTo(Path.of(
//                    targetDir,
//                    newFileName
//            ));
//            descriptorDto.setResourcePath("/" + Path.of(
//                    targetDir,
//                    newFileName).toString()
//            );
//            return descriptorDto;
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//            descriptorDto.setMessage("failed");
//            descriptorDto.setStatus(500);
//            return descriptorDto;
//        }
//    }
private MediaDescriptorDto saveToDir(MultipartFile file) {
    MediaDescriptorDto descriptorDto = new MediaDescriptorDto();
    descriptorDto.setStatus(200);
    descriptorDto.setOriginalName(file.getOriginalFilename());

       try {
        /*LocalDateTime now = LocalDateTime.now();
        String dateFolder = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String targetDir = Paths.get(basePath, dateFolder).toString(); // 사용자에게 보여줄 경로

        File dirNow = new File(targetDir);
        if (!dirNow.exists()) {
            dirNow.mkdirs(); // 디렉토리 생성
        }

        String newFileName = now.format(DateTimeFormatter.ofPattern("HHmmss"))
                + "_"
                + file.getOriginalFilename();

        File destFile = new File(dirNow, newFileName);*/
           Path newFilePath = Path.of(basePath, file.getOriginalFilename());

        file.transferTo(newFilePath); // 파일 저장

        // 저장된 파일의 상대 경로를 설정
        // descriptorDto.setResourcePath(Paths.get("media", dateFolder, newFileName).toString());*/


    } catch (IOException e) {
        logger.error(e.getMessage());
        descriptorDto.setMessage("failed");
        descriptorDto.setStatus(500);
        return descriptorDto;
    }
    return descriptorDto;
}

}