package org.zerock.ex01.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.PageResultDTO;
import org.zerock.ex01.dto.UploadResultDTO;
import org.zerock.ex01.entity.FoodImage;
import org.zerock.ex01.repository.FoodImageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class uploadImgServiceImpl implements UploadImgService {
    // @Value("${org.zerock.upload.path}")
    public String uploadPath;
    private final FoodImageRepository foodImageRepository;



    @Autowired
    AmazonS3Client amazonS3Client;

    @Override
    public void uploadFile(MultipartFile[] uploadFiles, Long num) {
        log.info(uploadFiles);
        UploadResultDTO uploadResultDTO = null;
        for (MultipartFile uploadFile : uploadFiles) {
            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("fileName: " + fileName);
            //날짜 폴더 생성
            String folderPath = makeFolder();

            //다수의 사용자가 같은 이름의 사진을 등록할 수 있기 떄문에 최종 이미지 파일이름을 고유번호(uuid)+실제파일이름(uploadFileName)으로 지정
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);//업로드 파일 저장
            } catch (IOException e) {
                e.printStackTrace();
            }
            //uploadResultDTO = new UploadResultDTO(fileName, uuid, folderPath);
            uploadResultDTO = UploadResultDTO.builder().fileName(fileName).uuid(uuid).folderPath(folderPath).build();
            log.info(uploadResultDTO);
            foodImageRepository.save(dtoToEntity(uploadResultDTO, num));
        }//end for
    }

    @Override
    public void uploadFileToAwsS3(MultipartFile[] uploadFiles, Long num) throws IOException {
        log.info(uploadFiles);
        UploadResultDTO uploadResultDTO = null;
        for (MultipartFile multipartFile : uploadFiles) {
            //다수의 사용자가 같은 이름의 사진을 등록할 수 있기 떄문에 최종 이미지 파일이름을 고유번호(uuid)+실제파일이름(uploadFileName)으로 지정
            String uuid = UUID.randomUUID().toString();
            String originalName = multipartFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
            //String originalName = uuid + "-" + filename; // 파일 이름
            long size = multipartFile.getSize(); // 파일 크기

            String s3Path = makeFolder() + "/" + uuid + "_" + fileName;

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(size);

            // S3에 업로드
//            amazonS3Client.putObject(
//                    new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
//                            .withCannedAcl(CannedAccessControlList.PublicRead)
//            );
            amazonS3Client.putObject(
                    S3Bucket, s3Path.replace(File.separatorChar, '/'), multipartFile.getInputStream(), objectMetaData);


            String imagePath = amazonS3Client.getUrl(S3Bucket, fileName).toString(); // 접근가능한 URL 가져오기
            uploadResultDTO = UploadResultDTO.builder().folderPath(s3Path).uuid(uuid).fileName(fileName).realImageUrl(imagePath).build();
            foodImageRepository.save(dtoToEntity(uploadResultDTO, num));
        }
    }

    @Transactional
    public void delUploadFileToAwsS3(Long num) {
        List<FoodImage> list = foodImageRepository.getImgList(num);
        for (FoodImage foodImage : list) {
            amazonS3Client.deleteObject(S3Bucket, foodImage.getPath() + "/" + foodImage.getUuid() + "_" + foodImage.getImgName());//s3의 이미지 파일들 삭제
        }
        foodImageRepository.delImgList(num);//db 삭제
    }

    @Override
    public String makeFolder() {//오늘날짜로 폴더를 생성

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("//", File.separator);//file.separator =>  프로그램이 실행 중인 OS에 해당하는 구분자를 리턴(경로구분자가 window는 \,Linux는 / 이기에 사용)

        // make folder --------
        File uploadPathFolder = new File(uploadPath, folderPath);// 폴더생성

        if (uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    @Override
    public PageResultDTO<CustomRecipeDTO, Object[]> getImageList(PageResultDTO<CustomRecipeDTO, Object[]> pageResultDTO) {
        for (Object obj : pageResultDTO.getDtoList()) {
            CustomRecipeDTO dto = (CustomRecipeDTO) obj;
            dto.setUploadImgResult(foodImageRepository.getImgList(dto.getCsRecipeId()).stream().map(entity -> entityToDto(entity)).collect(Collectors.toList()));
            //db에서 select한 이미지 데이터들은 entitu 객체이기에 dto로 변환
        }
        return pageResultDTO;
    }

    @Override
    public CustomRecipeDTO getImageList(CustomRecipeDTO dto) {
        dto.setUploadImgResult(foodImageRepository.getImgList(dto.getCsRecipeId()).stream().map(entity -> entityToDto(entity)).collect(Collectors.toList()));
        //db에서 select한 이미지 데이터들은 entitu 객체이기에 dto로 변환
        return dto;
    }

    @Override
    public void modify(MultipartFile[] uploadFiles, Long num) {
        foodImageRepository.delImgList(num);
        uploadFile(uploadFiles, num);
    }


//    public ResponseEntity<Boolean> removeFile(Long CsLong) {
//        log.info(fileName);
//        String srcFileName = null;
//        srcFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
//        File file = new File(uploadPath + File.separator + srcFileName);
//        boolean result = file.delete();
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//
//    }
}
