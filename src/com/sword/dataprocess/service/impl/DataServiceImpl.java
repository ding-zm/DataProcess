package com.sword.dataprocess.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sword.dataprocess.mapper.DataMapper;
import com.sword.dataprocess.pojo.DataProcess;
import com.sword.dataprocess.pojo.Tcaat;
import com.sword.dataprocess.service.DataService;

@Service
public class DataServiceImpl implements DataService{
	@Autowired
	private DataMapper dataMapper;

	@Override
	public int dataCount() {
		return dataMapper.dataCount();
	}

	@Override 
	public void exportAls(FileInputStream fileInputStream, ServletOutputStream outputStream) {
		// Workbook工作簿
				XSSFWorkbook book = null;
				try {
					book = new XSSFWorkbook(fileInputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// 工作表 sheet
				XSSFSheet sheet = book.getSheetAt(0);   
				// 获取第二个sheet中的第一行第一列的样式 及边框
//				XSSFCellStyle cellStyle = book.getSheetAt(1).getRow(0).getCell(0).getCellStyle();
				List<DataProcess> list = dataMapper.findAll();
				System.out.println(list.size());
				int rowIndex = 1; // 让表格从第二行开始导入
				XSSFCell cell = null;
				for (DataProcess dataProcess : list) {
					// 新建一行
					XSSFRow row = sheet.createRow(rowIndex);
					cell = row.createCell(0); // 第一个单元格
//					设定已经准备好单元格的样式
//					cell.setCellStyle(cellStyle);
					String id = dataProcess.getP_id();
					if(id != null){
						cell.setCellValue(id);
					}

					cell = row.createCell(1); // 第一个单元格
					String name = dataProcess.getP_name();
					if(name != null){
						cell.setCellValue(name);
					}

					cell = row.createCell(2); // 第二个单元格
					String guige = dataProcess.getP_guige();
					if(guige != null){
						cell.setCellValue(guige);
					}

					cell = row.createCell(3); // 第三个单元格
					String xdata = dataProcess.getP_xdata();
					if(xdata != null){
						cell.setCellValue(xdata);
					}

					cell = row.createCell(4); // 第四个单元格
					String jdate = dataProcess.getP_jdate();
					if(jdate != null){
						cell.setCellValue(jdate);
					}

					/*cell = row.createCell(5); // 第五个单元格
					Integer sourceCount = dataProcess.getP_sourceCount();
					if(sourceCount != null){
						cell.setCellValue(sourceCount);
					}*/
					

					cell = row.createCell(6); // 第六个单元格
					Integer descCount = dataProcess.getP_descCount();
					if (descCount != null) {
						cell.setCellValue(descCount);
					}

					rowIndex++;
				}
				// 把工作簿放在输出流中
				try {
					book.write(outputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
	
	// 导入数据
	@Override
	public Boolean importXls(File myFile, String myFileContentType) {
		
		if ("application/vnd.ms-excel".equals(myFileContentType)) {
			try {
				// 获取workbook工作簿
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(myFile));
				// 获取sheet 工作表
				HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
				// 获取工作表的最后一行索引
				int lastRowNum = sheet.getLastRowNum();
//				一次导入产生一个随机数用于拼接版本号
				int random = new Random().nextInt(100);
				String valueOf = String.valueOf(random);
				
				for (int i = 1; i <= lastRowNum; i++) {
					DataProcess dataProcess = new DataProcess();
					HSSFRow row = sheet.getRow(i);
					// 料件编号  特征码（8个0）行动日期 交货日期  排产数量  版本号（一次导入只用设置一个相同的值就行） 已执行步骤为0
					
					// 料件编号
					if(row.getCell(0)!=null){
				          row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				          String p_id = row.getCell(0).getStringCellValue();
							dataProcess.setP_id(p_id);
				     }
					
					// 行动日期
					String p_xdata = row.getCell(3).getStringCellValue();
					dataProcess.setP_xdata(p_xdata);;
					// 交货日期 
					String p_jdate = row.getCell(4).getStringCellValue();
					dataProcess.setP_jdate(p_jdate);
/*//					需求数量
					Integer p_sourceCount =  (int) row.getCell(5).getNumericCellValue();
					dataProcess.setP_sourceCount(p_sourceCount);*/
					// 排产数量
					if(row.getCell(5)!=null){
				          row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
				          String p_descCount = row.getCell(5).getStringCellValue();
							dataProcess.setP_descCount(Integer.valueOf(p_descCount));
					}
					
//					优先级
					if(row.getCell(6)!=null){
				          row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
				          String p_priority = row.getCell(6).getStringCellValue();
							dataProcess.setP_priority(p_priority);
					}
//					版本号（一次导入只用设置一个相同的值就行）
					SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMdd");    
					String datetime = tempDate.format(new Date()); 
					String p_version = "MRPVERNO"+datetime;
					dataProcess.setP_version(p_version);
					// 向tc_aau_file表插入数据
					dataMapper.insertdata(dataProcess);
					// 向tc_aat_file表插入数据
					if(i==lastRowNum){
//						tc_aat03 tc_aat04 的日期
						 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
						 String format = sdf.format(new Date()); 
//						tc_aat02 的日期 
						 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");    
						 Calendar c = Calendar.getInstance();
						 c.setTime(new Date());
					        c.add(Calendar.MONTH, -1);
					        Date m = c.getTime();
					        String mon = sdf1.format(m);
					        Tcaat tcaat = new Tcaat();
					        tcaat.setT_version(p_version);
					        tcaat.setT_sdata(mon);
					        tcaat.setT_ddata(format);
						dataMapper.insertToAAT(tcaat);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		} else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(myFileContentType)) {
			try {
				// 获取workbook工作簿
				XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(myFile));
				// 获取sheet 工作表
				XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
				// 获取工作表的最后一行索引
				int lastRowNum = sheet.getLastRowNum();
//				一次导入产生一个随机数用于拼接版本号
				int random = new Random().nextInt(100);
				String valueOf = String.valueOf(random);
				
				for (int i = 1; i <= lastRowNum; i++) {
					DataProcess dataProcess = new DataProcess();
					XSSFRow row = sheet.getRow(i);
					// 料件编号  特征码（8个0）行动日期 交货日期  排产数量  版本号（一次导入只用设置一个相同的值就行） 已执行步骤为0
					
					// 料件编号
					if(row.getCell(0)!=null){
				          row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				          String p_id = row.getCell(0).getStringCellValue();
							dataProcess.setP_id(p_id);
					}
						// 行动日期
					if(row.getCell(3)!=null){
				          row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
				          String p_xdata = row.getCell(3).getStringCellValue();
							dataProcess.setP_xdata(p_xdata);
					}
						// 交货日期 
					if(row.getCell(4)!=null){
				          row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
				          String p_jdate = row.getCell(4).getStringCellValue();
							dataProcess.setP_jdate(p_jdate);
					}
					
/*//					需求数量
						Integer p_sourceCount =  (int) row.getCell(5).getNumericCellValue();
						dataProcess.setP_sourceCount(p_sourceCount);*/
					// 排产数量
						if(row.getCell(5)!=null){
					          row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
					          String p_descCount = row.getCell(5).getStringCellValue();
								dataProcess.setP_descCount(Integer.valueOf(p_descCount));
						}
						
//						优先级
						if(row.getCell(6)!=null){
					          row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
					          String p_priority = row.getCell(6).getStringCellValue();
								dataProcess.setP_priority(p_priority);
						}
						
//					版本号（一次导入只用设置一个相同的值就行）
					SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMdd");    
					String datetime = tempDate.format(new Date()); 
					String p_version = "MRPVERNO"+datetime+valueOf;
					dataProcess.setP_version(p_version);
					
					// 向tc_aau_file表插入数据
					dataMapper.insertdata(dataProcess);
					// 向tc_aat_file表插入数据
					if(i==lastRowNum){
//						tc_aat03 tc_aat04 的日期
						 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
						 String format = sdf.format(new Date()); 
//						tc_aat02 的日期 
						 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");    
						 Calendar c = Calendar.getInstance();
						 c.setTime(new Date());
					        c.add(Calendar.MONTH, -1);
					        Date m = c.getTime();
					        String mon = sdf1.format(m);
					        Tcaat tcaat = new Tcaat();
					        tcaat.setT_version(p_version);
					        tcaat.setT_sdata(mon);
					        tcaat.setT_ddata(format);
						dataMapper.insertToAAT(tcaat);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} // elseif 结束
		return true;
	}

	
//	查询所有数据
	@Override
	public List<DataProcess> findAll() {
		List<DataProcess> result = dataMapper.findAll();
		return result;
	}
}
