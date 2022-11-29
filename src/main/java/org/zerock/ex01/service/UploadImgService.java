package org.zerock.ex01.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.PageResultDTO;
import org.zerock.ex01.dto.UploadResultDTO;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.entity.FoodImage;

import java.io.IOException;
import java.net.InetAddress;

public interface UploadImgService {
    public static final String S3Bucket = "mkc-automeal-s3"; // Bucket 이름
    default FoodImage dtoToEntity(UploadResultDTO uploadResultDTO, Long num) {
        CustomRecipe customRecipe = CustomRecipe
                .builder()
                .csRecipeId(num).build();

        FoodImage entity = FoodImage.builder()
                .uuid(uploadResultDTO.getUuid())
                .imgName(uploadResultDTO.getFileName())
                .path(uploadResultDTO.getFolderPath())
                .real_path(uploadResultDTO.getRealImageUrl())
                .custom_recipe(customRecipe)
                .build();

        return entity;
    }

    default UploadResultDTO entityToDto(FoodImage image) {
        String ip = getServerIp();
        UploadResultDTO dto = UploadResultDTO
                .builder()
                .uuid(image.getUuid())
                .folderPath(image.getPath())
                .fileName(image.getImgName())
                .realImageUrl(image.getReal_path())
                .build();

        return dto;
    }

    default String getServerIp() {

        InetAddress local = null;
        try {
            local = InetAddress.getLocalHost();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (local == null) {
            return "";
        } else {
            String ip = local.getHostAddress();
            return ip;
        }

    }

    void uploadFile(MultipartFile[] uploadFiles, Long num);

    void uploadFileToAwsS3(MultipartFile[] uploadFiles, Long num) throws IOException;

    String makeFolder();

    PageResultDTO<CustomRecipeDTO, Object[]> getImageList(PageResultDTO<CustomRecipeDTO, Object[]> pageResultDTO);

    CustomRecipeDTO getImageList(CustomRecipeDTO customRecipeDTO);

    @Transactional
    void modify(MultipartFile[] uploadFiles, Long num);

    void modifyToAwsS3(MultipartFile[] uploadFiles, Long num) throws IOException;
}
