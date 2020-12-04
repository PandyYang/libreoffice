package com.file.controller;

import com.alibaba.fastjson.JSON;
import com.common.exception.BizException;
import com.common.model.Result;
import com.common.util.HttpClientUtil;
import com.file.constant.FileConstant;
import com.file.dto.FileConvertResultDTO;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.file.read.ReadFileService;
import lombok.val;
import org.jodconverter.DocumentConverter;
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
  public void go(@RequestParam String filePath) throws IOException {
    val readfile = readFileService.readfile(filePath);
    for (int i = 0; i< readfile.size(); i++) {
      File file = new File(readfile.get(i).toString());
      File targetFile = new File("C:\\Users\\Administrator\\OneDrive\\桌面\\321\\" + file.getName() + ".pdf");
      try {
        documentConverter.convert(file).to(targetFile).execute();
      } catch (OfficeException e) {
        e.printStackTrace();
      }
    }
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
}
