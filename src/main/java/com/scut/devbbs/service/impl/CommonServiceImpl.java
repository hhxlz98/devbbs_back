package com.scut.devbbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.ParameterConstant;
import com.scut.devbbs.constants.ResponseEnum;
import com.scut.devbbs.dao.NewsDao;
import com.scut.devbbs.service.CommonService;
import com.scut.devbbs.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

    @Resource
    private NewsDao newsDao;

    @Autowired
    private ParameterConstant parameterConstant;

    @Override
    public ResponseEnum uploadImg(MultipartFile file, String path, String fileName) {
        if (file == null) {
            log.error("图片为空:{}", file);
            return ResponseEnum.E_504;
        }
        System.out.println("新图片名：" + fileName);
        String re = path + fileName;
        try {
            file.transferTo(new File(re));
            return ResponseEnum.E_506;
        } catch (IOException e) {
            log.error("上传图片失败:{}", e.getMessage());
            return ResponseEnum.E_505;
        }
    }

    @Override
    public void imageToSquare(String path, String suf) throws Exception{
        Image src=Toolkit.getDefaultToolkit().getImage(path);
        BufferedImage image = toBufferedImage(src);
//判断宽高最大的一个值
        int max=Math.max(image.getHeight(), image.getWidth());
        BufferedImage outImage =  new BufferedImage(max, max, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = outImage.createGraphics();
        outImage = graphics2D.getDeviceConfiguration().createCompatibleImage(max, max, Transparency.TRANSLUCENT);
        graphics2D.dispose();
        graphics2D = outImage.createGraphics();
//原图高度
        int oldheight = image.getHeight();
//原图宽度
        int oldwidth = image.getWidth();
// 设置图片居中显示
        graphics2D.drawImage(image, (max - oldwidth) / 2,(max - oldheight) / 2, null);
//        graphics2D.drawImage(image.getScaledInstance(max, max, Image.SCALE_SMOOTH), (max - oldwidth) / 2,(max - oldheight) / 2, null);
        graphics2D.dispose();
//生成新的图片
        ImageIO.write(outImage, suf, new File(path));
    }

    @Override
    public JSONObject addInfoForPaging(JSONObject jsonObject, int listSize, JSONObject resultJson) {
        resultJson.put("noPost", false);
        int currentPage = jsonObject.getIntValue("currentPage");
        int pageSize = jsonObject.getIntValue("pageSize");
        if (listSize == 0) {
            if (currentPage == 1) {
                resultJson.put("noPost", true);
            }
            resultJson.put("noMore", true);
        } else {
            if (listSize < pageSize) {
                resultJson.put("noMore", true);
            } else {
                resultJson.put("noMore", false);
            }
        }
        return resultJson;
    }

    @Override
    public JSONObject addNews(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getAddNewsParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        jsonObject.put("updateTime", System.currentTimeMillis());
        newsDao.addNews(jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_1201, jsonObject);
    }

    @Override
    public JSONObject allNewsList() {
        List<JSONObject> newsList = newsDao.selectNewsList();
        return CommonUtil.commonReturn(ResponseEnum.E_1204, newsList);
    }

    @Override
    public JSONObject homeNewsList() {
        List<JSONObject> newsList = newsDao.selectHomeNewsList();
        return CommonUtil.commonReturn(ResponseEnum.E_1205, newsList);
    }

    @Override
    public JSONObject updateNews(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUpdateNewsParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        jsonObject.put("updateTime", System.currentTimeMillis());
        newsDao.updateNews(jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_1202, jsonObject);
    }

    @Override
    public JSONObject updateNewsValid(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUpdateNewsValidParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        newsDao.updateNewsValid(jsonObject.getLong("newsId"), jsonObject.getBoolean("valid"));
        return CommonUtil.commonReturn(ResponseEnum.E_1203, jsonObject);
    }

    @Override
    public JSONObject deleteNews(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getDeleteNewsParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        newsDao.deleteNews(jsonObject.getLong("newsId"));
        return CommonUtil.commonReturn(ResponseEnum.E_1206, jsonObject);
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null),
                    image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }
}

