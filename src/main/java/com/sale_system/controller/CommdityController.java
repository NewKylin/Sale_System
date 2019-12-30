package com.sale_system.controller;

import com.sale_system.po.Commodity;
import com.sale_system.service.CommodityService;
import com.sun.javafx.image.PixelUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CommdityController {
    @Autowired
    private CommodityService commodityService;

    Log log = LogFactory.getLog(this.getClass());

    @RequestMapping("/commodity/home.action")
    public String list(Model model){
        List<Commodity> commodityList = commodityService.selectAll(null);
        model.addAttribute("commodityList",commodityList);
        return "/commodity/home.jsp";
    }
    @RequestMapping("/commodity/toAdd.action")
    public String toAdd(Model model){
        return "/commodity/add.jsp";
    }

    @RequestMapping("/commodity/add.action")
    public String add(Model model,Commodity commodity){
        commodityService.insert(commodity);
        return list(model);
}

    @RequestMapping("/commodity/toEdit.action")
    public String toEdit(Model model,Integer id){
        if(id!=null){
            model.addAttribute("commodity", commodityService.selectById(id));
        }
        return "/commodity/edit.jsp";
    }

    @RequestMapping("/commodity/edit.action")
    public String edit(Model model,Commodity commodity){
        commodityService.update(commodity);
        return list(model);
    }

    @RequestMapping("/commodity/delete.action")
    public String delete(Model model,Integer id){
        commodityService.deleteById(id);
        return list(model);
    }
}
