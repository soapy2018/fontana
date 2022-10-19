package com.fontana.xxljob.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fontana.base.constant.CommonConstants;
import com.fontana.xxljob.model.XxlJobGroup;
import com.fontana.xxljob.service.JobGroupService;
import com.fontana.xxljob.service.JobLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : cqf
 * @date: 2022/9/19 17:34
 * @version: 1.0
 */
@Service
public class JobGroupServiceImpl implements JobGroupService {

    @Value("${fontana.xxljob.adminAddresses}")
    private String adminAddresses;

    @Value("${fontana.xxljob.appname}")
    private String appName;

    @Value("${fontana.xxljob.title}")
    private String title;

    @Autowired
    private JobLoginService jobLoginService;

    @Override
    public List<XxlJobGroup> getJobGroup() {
        String url=adminAddresses+"/jobgroup/pageList";
        HttpResponse response = HttpRequest.post(url)
                .form("appname", appName)
                .form("title", title)
                .cookie(jobLoginService.getCookie())
                .execute();

        String body = response.body();
        JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);
        return array.stream()
                .map(o -> JSONUtil.toBean((JSONObject) o, XxlJobGroup.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean autoRegisterGroup() {
        String url = adminAddresses + "/jobgroup/save";
        HttpResponse response = HttpRequest.post(url)
                .form("appname", appName)
                .form("title", title)
                .cookie(jobLoginService.getCookie())
                .execute();
        Object code = JSONUtil.parse(response.body()).getByPath("code");
        return code.equals(200);
    }

    @Override
    public boolean preciselyCheck() {
        List<XxlJobGroup> jobGroup = getJobGroup();
        Optional<XxlJobGroup> has = jobGroup.stream()
                .filter(xxlJobGroup -> xxlJobGroup.getAppname().equals(appName)
                        && xxlJobGroup.getTitle().equals(title))
                .findAny();
        return has.isPresent();
    }

}
