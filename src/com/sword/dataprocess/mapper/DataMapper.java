package com.sword.dataprocess.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sword.dataprocess.pojo.DataProcess;
import com.sword.dataprocess.pojo.Tcaat;

public interface DataMapper {
	public int dataCount();

	public List<DataProcess> findAll();

	public void insertdata(@Param("dataProcess")DataProcess dataProcess);

	public void insertToAAT(@Param("tcaat")Tcaat tcaat);
}
