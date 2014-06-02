package com.wd.basezk.dao;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.Cuser;

public interface CuserDAO {
    public void insertData(Cuser objNya);
    public void updateData(Cuser objNya);
    public void deleteData(String idNya);
    public Cuser getById(String idNya);
    public List<Cuser> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
    public int getMaxIdByRequest(Map<String, String> requestMap, int manyDigit);
}
