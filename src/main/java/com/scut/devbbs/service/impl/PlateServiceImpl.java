package com.scut.devbbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.ParameterConstant;
import com.scut.devbbs.dao.PlateDao;
import com.scut.devbbs.dao.PlateRelateDao;
import com.scut.devbbs.service.PlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import com.scut.devbbs.util.CommonUtil;
import com.scut.devbbs.constants.ResponseEnum;

@Service
public class PlateServiceImpl implements PlateService {

    @Resource
    private PlateDao plateDao;

    @Resource
    private PlateRelateDao plateRelateDao;

    @Autowired
    private ParameterConstant parameterConstant;

    @Override
    public JSONObject listPlate(JSONObject jsonObject) {
        List<JSONObject> plateList = plateDao.listPlate(0);
        long userId = jsonObject.getLong("userId");
        List<JSONObject> resultJsons = addFollowInfo(plateList, userId);
        return CommonUtil.commonReturn(ResponseEnum.E_201, resultJsons);
    }

    @Override
    public JSONObject myListPlate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getMyPlateParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long userId = jsonObject.getLong("userId");
        int type = jsonObject.getIntValue("type");
        List<JSONObject> plateRelateList = plateRelateDao.relatePlate(userId, type);

        List<String> relatePlates = new ArrayList<String>();
        for (JSONObject plate : plateRelateList) {
            relatePlates.add(plate.getString("plate_id"));
        }
        List<JSONObject> plateList = plateDao.myListPlate(relatePlates);
        List<JSONObject> resultJsons = addFollowInfo(plateList, userId);
        return CommonUtil.commonReturn(ResponseEnum.E_201, resultJsons);
    }



    @Override
    public JSONObject addPlate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getAddPlateParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        String plateN = jsonObject.getString("plateName");
        if (plateDao.existPlate(plateN, 0) != 0) {
            info = "板块已存在，名字：" + plateN;
            return CommonUtil.commonReturn(ResponseEnum.E_205, info);
        }
        jsonObject.put("followNumber", 0);
        jsonObject.put("postNumber", 0);
        jsonObject.put("state", 0);
        plateDao.addPlate(jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_206, info);
    }

    @Override
    public List<JSONObject> addFollowInfo(List<JSONObject> jsonObjects, long userId) {
        if (userId > 0) {
            List<JSONObject> plateRelateList = plateRelateDao.relatePlate(userId, 0);
            Set set = new HashSet();
            for (JSONObject relate : plateRelateList) {
                set.add(relate.getLong("plate_id"));
            }
            for (JSONObject plate : jsonObjects) {
                if (set.contains(plate.getLong("plateId"))) {
                    plate.put("isFollow", true);
                } else {
                    plate.put("isFollow", false);
                }
            }
        } else {
            for (JSONObject plate : jsonObjects) {
                plate.put("isFollow", false);
            }
        }
        return jsonObjects;
    }

    @Override
    public JSONObject plateInfo(JSONObject jsonObject) {
        long plateId = jsonObject.getLong("plateId");
        System.out.println(plateId);
        if (plateId > 0) {
            JSONObject resultJson = plateDao.queryPlateById(plateId);
            jsonObject.put("type", 0);
            if (plateRelateExist(jsonObject)) {
                resultJson.put("isFollow", true);
            } else {
                resultJson.put("isFollow", false);
            }
            jsonObject.put("type", 1);
            if (plateRelateExist(jsonObject)) {
                resultJson.put("isManage", true);
            } else {
                resultJson.put("isManage", false);
            }
            return CommonUtil.commonReturn(ResponseEnum.E_207, resultJson);
        } else {
            String info = "板块Id不合法：" + plateId;
            return CommonUtil.errorJson(info);
        }
    }

    @Override
    public boolean plateRelateExist(JSONObject jsonObject) {
        JSONObject resultJson = plateRelateDao.existPlateRelate(jsonObject);
        if ( resultJson == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean userManagePlateExist(long userId, long plateId) {
        if (userId > 0) {
            JSONObject queryRelate = new JSONObject();
            queryRelate.put("userId", userId);
            queryRelate.put("plateId", plateId);
            queryRelate.put("type", 1);
            if (plateRelateExist(queryRelate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public JSONObject addPlateRelate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getPlateRelateParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        info = jsonObject.toString();
        if (plateRelateExist(jsonObject)) {
            return CommonUtil.commonReturn(ResponseEnum.E_305, info);
        } else {
            plateRelateDao.addPlateRelate(jsonObject);
            if (jsonObject.getIntValue("type") == 0) {
                plateFollowAdd(jsonObject);
            }
            return CommonUtil.commonReturn(ResponseEnum.E_302, info);
        }

    }

    @Override
    public JSONObject deletePlateRelate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getPlateRelateParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        info = jsonObject.toString();
        if (plateRelateExist(jsonObject)) {
            plateRelateDao.deletePlateRelate(jsonObject);
            if (jsonObject.getIntValue("type") == 0) {
                plateFollowReduce(jsonObject);
            }
            return CommonUtil.commonReturn(ResponseEnum.E_303, info);
        } else {
            return CommonUtil.commonReturn(ResponseEnum.E_304, info);
        }
    }

    @Override
    public void plateFollowAdd(JSONObject jsonObject) {
        long plateId = jsonObject.getLong("plateId");
        if (plateId != 0) {
            plateDao.plateFollowAdd(plateId);
        }
    }

    @Override
    public void plateFollowReduce(JSONObject jsonObject) {
        long plateId = jsonObject.getLong("plateId");
        if (plateId != 0) {
            plateDao.plateFollowReduce(plateId);
        }
    }

    @Override
    public boolean plateExist(JSONObject jsonObject) {
        JSONObject queryRelate = plateRelateDao.existPlateRelate(jsonObject);
        if (queryRelate != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public JSONObject allPlateList(JSONObject jsonObject) {
        if (jsonObject.get("type") == null) {
            return CommonUtil.errorJson("缺少type参数");
        } else {
            int type = jsonObject.getIntValue("type");
            List<JSONObject> plateList = plateDao.listPlate(type);
            return CommonUtil.commonReturn(ResponseEnum.E_201, plateList);
        }
    }

    @Override
    public boolean existPlateName(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getPlateNameExistParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return true;
        }
        long plateId = jsonObject.getLong("plateId");
        String plateName= jsonObject.getString("plateName");
        if (plateDao.existPlate(plateName, plateId) == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public JSONObject updatePlateInfo(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getAddPlateParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long userId = jsonObject.getLong("userId");
        if(userId > 0){
            if (!userManagePlateExist(userId, jsonObject.getLong("plateId"))) {
                return CommonUtil.commonReturn(ResponseEnum.E_209, jsonObject);
            }
        }
        if (existPlateName(jsonObject)) {
            return CommonUtil.commonReturn(ResponseEnum.E_202, jsonObject);
        }
        plateDao.updatePlateInfo(jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_203, jsonObject);
    }

    @Override
    public JSONObject updatePlateState(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getPlateUpdateStateParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        plateDao.updatePlateState(jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_204, jsonObject);
    }

    @Override
    public JSONObject plateAddManageList() {
        List<JSONObject> plateList = plateDao.listPlate(0);
        for (JSONObject plate: plateList) {
            long plateId = plate.getLong("plateId");
            plate.put("manageList", plateRelateDao.listPlateRelateUser(plateId, 1));
        }
        return CommonUtil.commonReturn(ResponseEnum.E_208, plateList);
    }
}
