package com.file.controller;

import com.alibaba.fastjson.JSON;
import com.common.exception.BizException;
import com.common.model.Result;
import com.common.util.HttpClientUtil;
import com.file.constant.FileConstant;
import com.file.dto.FileConvertResultDTO;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.file.read.ReadFileService;
import com.preview.util.FileUtil;
import lombok.val;
import org.jodconverter.DocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value="")
public class FileController {

  @Autowired
  private ReadFileService readFileService;

  @Autowired
  private DocumentConverter documentConverter;

  @GetMapping(value = "/go")
  public String go(@RequestParam String filePath) throws IOException {
    val readfile = readFileService.readfile(filePath);
    //FileUtil.reName("I:\\BaiduNetdiskDownload\\123", ".epub", ".rtf");
    for (int i = 0; i< readfile.size(); i++) {
      File file = new File(readfile.get(i).toString());
      String uuid = UUID.randomUUID().toString();
      File targetFile = new File("I:\\BaiduNetdiskDownload\\书籍pdf\\" + file.getName().replaceAll("[.][^.]+$", "") + ".pdf");
      try {
        documentConverter.convert(file).to(targetFile).execute();
      } catch (OfficeException e) {
        e.printStackTrace();
      }
    }
    return "";
  }

  @GetMapping(value="/")
  public String gotoUpload(){
   return "upload";
  }


  @PostMapping(value="/upload")
  @ResponseBody
  public Result<String> upload(@RequestParam("file")MultipartFile file)throws IOException {
    Map<String,Object> params = new HashMap<>();
    params.put(FileConstant.FILE_PARAM_KEY,file.getBytes());
    String response = HttpClientUtil.INSTANCE.post(FileConstant.PREVIEW_REMOTE_URL,params,file.getOriginalFilename());
    FileConvertResultDTO fileConvertResultDTO = JSON.parseObject(response,FileConvertResultDTO.class);
    if(fileConvertResultDTO != null && FileConstant.SUCCESS.equals(fileConvertResultDTO.getStatus())){
      return new Result<String>().setData(fileConvertResultDTO.getTargetFileName());
    }
    throw new BizException("文件上传解析预览失败",406);
  }

  @GetMapping(value="toPreview")
  public String gotoPreview(String fileName, Model model){
    model.addAttribute("fileName",fileName);
    return "preview";
  }

  @PostMapping(value = "/upload2")
  @ResponseBody
  public String test(@RequestParam("file")MultipartFile file) throws IOException, OfficeException {
    String uuid = UUID.randomUUID().toString();
    File targetFile = new File("F:\\IdeaProjects\\libre-office\\src\\main\\resources\\test"+ FileUtil.SLASH_ONE + uuid +  file.getName() + FileUtil.DOT + "pdf");
    //jodconverter进行转换
    String fileExt = FileUtil.getExtension(FileUtil.getFileName(file.getOriginalFilename()));
//    byte [] byteArr = file.getBytes();
//    InputStream inputStream =new ByteArrayInputStream(byteArr);
    System.out.println(uuid + "开始转换...");
    long startTime = System.currentTimeMillis();
    try {
      InputStream inputStream =  new BufferedInputStream(file.getInputStream());
      documentConverter.convert(inputStream).as(DefaultDocumentFormatRegistry.getFormatByExtension(fileExt)).to(targetFile).as(DefaultDocumentFormatRegistry.PDF).execute();
    }catch (Exception e) {
      System.out.println("转换失败");
      e.printStackTrace();
      return "fail";
    }
    long endTime = System.currentTimeMillis();
    long time = endTime - startTime;
    System.out.println(uuid + "转换完成,用时:" + time + "毫秒" );
    return "success";
  }
}
