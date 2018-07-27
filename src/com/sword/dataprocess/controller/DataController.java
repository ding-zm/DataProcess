package com.sword.dataprocess.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sword.dataprocess.pojo.DataProcess;
import com.sword.dataprocess.service.DataService;
import com.sword.dataprocess.utils.FileUtils;

@Controller
public class DataController {
	
	@Autowired
	private DataService dataService;
	
	@RequestMapping(value={"/index","/index.html","/index.htm"})
	public String index(){
		return "index";
	}
	
	@RequestMapping("/show")
	@ResponseBody
	public List<DataProcess> show(Model model){
		List<DataProcess> list =  dataService.findAll();
		model.addAttribute("list", list);
        return list;  
    }   
	
// 文件导出的实现
	@RequestMapping("/export")
	public void exportXls(HttpServletRequest request,HttpServletResponse response) throws Exception{
		// 一个流
		// 两个头
		// 下载文件的mime类型
		response.setContentType("application/vnd.ms-excel"); // 常见的文件  可以省略

		// 文件的打开方式   inline在线打开   attachment
		String agent = request.getHeader("User-Agent");
		String filename = FileUtils.encodeDownloadFilename("data.xlsx", agent);
		response.setHeader("content-disposition", "attachment;fileName="+filename);
		ServletOutputStream outputStream = response.getOutputStream();
		
		// 获取模板   在当前项目
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		String templatePath =  request.getServletContext().getRealPath(File.separator)+"temp"+File.separator+"data.xlsx";
		FileInputStream fileInputStream = new FileInputStream(templatePath);
	
		dataService.exportAls(fileInputStream, outputStream);
	}
	
	
//	文件导入
	//接收页面传来的文件
	@RequestMapping("/import")
	@ResponseBody
	public String importXlsx(HttpServletRequest request){
		MultipartHttpServletRequest multipartRequest =     (MultipartHttpServletRequest) request;
	    MultipartFile myFile = multipartRequest.getFile("myFile"); // 通过参数名获取指定文件 文件本身   变量名和文件上传时的名称保持一致
	    String myFileFileName = myFile.getOriginalFilename();//文件的名字
	    String myFileContentType = myFile.getContentType(); //文件的mime类型
	    
        CommonsMultipartFile cf= (CommonsMultipartFile)myFile; 
        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 

        File f = fi.getStoreLocation();
       String msg = null;
       
		Boolean	flag = dataService.importXls(f,myFileContentType);
		if(flag){
			msg = "success";
		}else{
			msg = "error";
		}
				return msg;		
	}
	
	
}
