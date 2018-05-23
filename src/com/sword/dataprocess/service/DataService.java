package com.sword.dataprocess.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.sword.dataprocess.pojo.DataProcess;

public interface DataService {

	int dataCount();

	void exportAls(FileInputStream fileInputStream, ServletOutputStream outputStream);

	Boolean importXls(File f, String myFileContentType);

	List<DataProcess> findAll();

}
