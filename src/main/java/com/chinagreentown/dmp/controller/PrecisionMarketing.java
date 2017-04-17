package com.chinagreentown.dmp.controller;

import com.chinagreentown.dmp.pojo.PeopleDto;
import com.chinagreentown.dmp.pojo.precisionmarketing.BasicInfo;
import com.chinagreentown.dmp.pojo.precisionmarketing.CNetwork;
import com.chinagreentown.dmp.pojo.precisionmarketing.FixedNetwork;
import com.chinagreentown.dmp.pojo.precisionmarketing.Shopping;
import com.chinagreentown.dmp.pojo.precisionmarketing.UserPortrait;
import com.chinagreentown.dmp.pojo.precisionmarketing.Purchase;
import com.chinagreentown.dmp.service.QueryService;
import com.chinagreentown.dmp.util.FakeData;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by admin on 2017/3/28.
 * 精准营销相关接口
 */
@RestController
@RequestMapping("precisionmarketing")
public class PrecisionMarketing {

    @Autowired
    private QueryService queryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PrecisionMarketing.class);

    /**
     * 根据token以及电话号码查询用户微观画像
     *
     * @param phonenums
     * @param token
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserPortrait>> getUserPortrait(@RequestParam(value = "phonenums") List<String> phonenums,
                                                              @RequestParam(value = "token") String token) {
        try {
            if (token.equals("XYSh346UIdha") && !(null == phonenums)) {
                UserPortrait up = new UserPortrait();
                // 设置基础信息
                up.setBasicInfo(new BasicInfo("wanggao", "female", "345261876678779987", "22-30", "100-300w"));
                // 设置C网
                CNetwork cNetwork = new CNetwork();
                Map<String, Integer> category = new HashMap<>();
                Map<String, Integer> app = new HashMap<>();
                category.put("娱乐", 22);
                app.put("链家", 80);
                cNetwork.setCategory(category);
                cNetwork.setApp(app);
                up.setcNetwork(cNetwork);
                // 设置固网
                FixedNetwork fixedNetwork = new FixedNetwork();
                Map<String, Integer> category2 = new HashMap<>();
                Map<String, Integer> app2 = new HashMap<>();
                category2.put("金融", 33);
                app2.put("我爱我家", 222);
                fixedNetwork.setCategory(category2);
                fixedNetwork.setApp(app2);
                up.setFixedNetwork(fixedNetwork);
                // 设置购房需求
                up.setPurchase(new Purchase("养老", "强烈", "2w", "250w", "闲林", "杭州"));
                // 设置购物
                up.setShopping(new Shopping("3000", "15"));
                List<UserPortrait> userPortraits = new ArrayList<>();
                for (int i = 0; i < phonenums.size(); i++) {
                    userPortraits.add(up);
                }
                // 查询成功，响应200
                return ResponseEntity.ok(userPortraits);
            } else {
                // 资源不存在，响应404
                LOGGER.info("资源不存在：" + phonenums);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 查询出错，响应500
        LOGGER.info("查询出错");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "/test")
    public String test() {
        List<PeopleDto> list = queryService.query("liming5", "liming6");
        for (PeopleDto p : list) {
            System.out.println(p);
        }
        return list.toString();
    }


    @RequestMapping(value = "/v1.0")
    public ResponseEntity<Map<String, Object>> getUser() {
        List<PeopleDto> list = queryService.query("liming5", "liming6");
        for (PeopleDto p : list) {
            System.out.println(p);
        }
        return ResponseEntity.ok(Maps.newHashMap());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/workplacehot/v1.0")
    public ResponseEntity<Object> getWorkssPlace(@RequestParam(value = "estatecode") String estatecode,
                                                 @RequestParam(value = "token") String token,
                                                 @RequestParam(value = "date") String date) {
        try {
            if (null != estatecode && token.equals("test") && null != date) {
                if (FakeData.isNum(estatecode)) {
                    Map<String, String> liveMaps = queryService.getworkMaps(Integer.parseInt(estatecode));
                    return ResponseEntity.ok(liveMaps);
                }
            }
            HashMap<String, String> Map = Maps.newHashMap();
            Map.put("Reason", FakeData.HttpStr.PARAMETERERROR.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @RequestMapping(method = RequestMethod.GET, value = "/liveplacehot/v1.0")
    public ResponseEntity<Object> getlivePlace(@RequestParam(value = "estatecode") String estatecode,
                                               @RequestParam(value = "token") String token,
                                               @RequestParam(value = "date") String date) {
        try {
            if (null != estatecode && token.equals("test") && null != date) {
                if (FakeData.isNum(estatecode)) {
                    Map<String, String> liveMaps = queryService.getLiveMaps(Integer.parseInt(estatecode));
                    return ResponseEntity.ok(liveMaps);
                }
            }
            HashMap<String, String> Map = Maps.newHashMap();
            Map.put("Reason", FakeData.HttpStr.PARAMETERERROR.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @RequestMapping(value = "/label/v1.0", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getUserLabel(@RequestParam(value = "phonenum") String phonenum,
                                                            @RequestParam(value = "date") String date,
                                                            @RequestParam(value = "token") String token) {
        try {
            HashMap<String, Object> Map = Maps.newHashMap();
            if (null != phonenum && token.equals("test") && null != date) {
                String[] split = phonenum.split(",");
                for (String str : split) {
                    if (!FakeData.getMa5Phone(str).isEmpty()) {
                        Map.put(FakeData.getMa5Phone(str), queryService.getlabelUserinfo(str));
                    } else {
                        Map.put(str, FakeData.HttpStr.PHONEERROR.toString());
                    }
                }
                return ResponseEntity.ok(Map);
            }
            Map.put("Reason", FakeData.HttpStr.PARAMETERERROR.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @RequestMapping(value = "/estatemicro/v1.0")
    public ResponseEntity<Map<String, Object>> getEstateUserLabel(@RequestParam(value = "estatecode") String estatecode,
                                                                  @RequestParam(value = "date") String date,
                                                                  @RequestParam(value = "token") String token) {
        try {
            HashMap<String, Object> Map = Maps.newHashMap();
            if (null != estatecode && token.equals("test") && null != date) {
                if (FakeData.isNum(estatecode)) {
                    Map<String, Object> labelmap = queryService.getEsatUserLabelMaps(Integer.parseInt(estatecode));
                    return ResponseEntity.ok(labelmap);
                }
            }
            Map.put("Reason", FakeData.HttpStr.PARAMETERERROR.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "/estate/v1.0")
    public ResponseEntity<Map<String, Object>> getEstate(@RequestParam(value = "estatecode") String estatecode,
                                                         @RequestParam(value = "date") String date,
                                                         @RequestParam(value = "token") String token) {
        try {
            HashMap<String, Object> Map = Maps.newHashMap();
            if (null != estatecode && token.equals("test") && null != date) {
                if (FakeData.isNum(estatecode)) {
                    java.util.Map<String, Object> stringObjectMap = queryService.houseMacrography(Integer.parseInt(estatecode));
                    return ResponseEntity.ok(stringObjectMap);
                }
            }
            Map.put("Reason", FakeData.HttpStr.PARAMETERERROR.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


}
