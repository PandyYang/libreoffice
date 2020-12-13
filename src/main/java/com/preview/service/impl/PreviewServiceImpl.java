package com.preview.service.impl;


import com.preview.constants.Constants;
import com.preview.dto.FileConvertResultDTO;
import com.preview.service.PreviewService;
import com.preview.util.FileUtil;
import java.io.File;
import java.io.InputStream;
import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.jodconverter.DocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PreviewServiceImpl implements PreviewService {

  @Value("${jodconverter.store.path}")
  private String storePath;

  @Autowired
  private DocumentConverter documentConverter;

  @Override
  public FileConvertResultDTO convertFile2pdf(File sourceFile, String fileExt) {
    FileConvertResultDTO fileConvertResultDTO = new FileConvertResultDTO();
    try {
      fileExt = fileExt.toLowerCase();
      String fileName = FileUtil.getWithoutExtension(sourceFile.getName());
      String targetFileExt = getTargetFileExt(fileExt);
      File targetFile = new File(storePath+ FileUtil.SLASH_ONE + fileName + FileUtil.DOT + targetFileExt);
      //jodconverter进行转换
      documentConverter.convert(sourceFile).as(DefaultDocumentFormatRegistry.getFormatByExtension(fileExt))
          .to(targetFile).as(DefaultDocumentFormatRegistry.getFormatByExtension(targetFileExt)).execute();
      fileConvertResultDTO.setStatus("success");
      fileConvertResultDTO.setTargetFileName(targetFile.getName());
    } catch (OfficeException e) {
      log.error("convertFile2pdf error : " + e.getMessage(),e);
      fileConvertResultDTO.setStatus("fail");
    }
    return fileConvertResultDTO;

  }

  @Override
  public FileConvertResultDTO convertInputStream2pdf(InputStream in, String fileName, String fileExt) {
    FileConvertResultDTO fileConvertResultDTO = new FileConvertResultDTO();
    try {
      fileExt = fileExt.toLowerCase();
      fileName = FileUtil.getWithoutExtension(fileName);
      String targetFileExt = getTargetFileExt(fileExt);
      System.out.println("libreOffice开始转换..............................");
      Long startTime = System.currentTimeMillis();
      File targetFile = new File(storePath+ FileUtil.SLASH_ONE + fileName + FileUtil.DOT + targetFileExt);
      documentConverter.convert(in).as(DefaultDocumentFormatRegistry.getFormatByExtension(fileExt))
          .to(targetFile).as(DefaultDocumentFormatRegistry.getFormatByExtension(targetFileExt)).execute();
      fileConvertResultDTO.setStatus("success");
      fileConvertResultDTO.setTargetFileName(targetFile.getName());
      System.out.println("转换结束。。。。。");
      //转换时间
      long endTime = System.currentTimeMillis();
      long time = endTime - startTime;
      System.out.println("libreOffice转换所用时间为：" + time);
    } catch (OfficeException e) {
      log.error("convertInputStream2pdf error : " + e.getMessage(),e);
      fileConvertResultDTO.setStatus("fail");
    }
    return fileConvertResultDTO;
  }

  /**
   * 获取想要转换的格式类型
   * @return
   */
  private String getTargetFileExt(String originFileExt){
     if(Constants.fileType2Htmls.contains(originFileExt)){
       return FileUtil.HTML;
     }
     return FileUtil.PDF;
  }

  @PostConstruct
  private void init(){
    File targetDir = new File(storePath);
    if(!targetDir.exists()){
      targetDir.mkdirs();
    }
  }
}
