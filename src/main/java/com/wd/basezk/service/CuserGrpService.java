package com.wd.basezk.service;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.CuserGrp;

public interface CuserGrpService {
    public void insertData(CuserGrp objNya);
    public void updateData(CuserGrp objNya);
    public void deleteData(String idNya);
    public CuserGrp getById(String idNya);
    public List<CuserGrp> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
}
