package com.sale_system.mq.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import com.sale_system.po.Commodity;
import com.sale_system.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ItemMQHandler {

    @Autowired
    private CommodityService commodityService;

    public void execute(String msg){
        if(!StringUtils.isNullOrEmpty(msg)){
            Map<String,Object> msgMap=(Map<String,Object>) JSON.parse(msg);
            String jsonStr = (String)msgMap.get("itemObject");
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            Commodity commodity = convertMapToCommodity(jsonObject);
            String type = (String)msgMap.get("type");
            if(type.equals("insert"))
                commodityService.insert(commodity);
            else if(type.equals("update")){
                if(commodityService.selectById(commodity.getId()) != null){
                    commodityService.update(commodity);
                }else{
                    commodityService.insert(commodity);
                }
            }else if(type.equals("delete")){
                if(commodityService.selectById(commodity.getId())!= null){
                    commodityService.deleteById(commodity.getId());
                }
            }
        }
    }
    private Commodity convertMapToCommodity(JSONObject jsonObject) {
        Commodity commodity = new Commodity();
        if(jsonObject!=null){
            if(jsonObject.get("id")!=null){
                commodity.setId(Integer.parseInt(jsonObject.get("id").toString()));
            }
            if(jsonObject.get("name")!=null){
                commodity.setName(jsonObject.get("name").toString());
            }
            if(jsonObject.get("price")!=null){
                commodity.setPrice(Double.parseDouble(jsonObject.get("price").toString()));
            }
            if(jsonObject.get("desc")!=null){
                commodity.setDesc(jsonObject.get("desc").toString());
            }
            if(jsonObject.get("weight")!=null&&!StringUtils.isNullOrEmpty(jsonObject.get("weight").toString())){
                commodity.setWeight(Integer.parseInt(jsonObject.get("weight").toString()));
            }
            if(jsonObject.get("model")!=null){
                commodity.setModel(jsonObject.get("model").toString());
            }
        }
        return commodity;
    }
}
