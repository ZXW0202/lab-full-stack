package com.stbu.labmanager.controller;

import com.stbu.labmanager.entity.Lab;
import com.stbu.labmanager.service.LabService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实验室信息管理 控制器
 * 处理前端关于实验室的增删改查请求
 */
@RestController
@RequestMapping("/lab")
@RequiredArgsConstructor
public class LabController {

    /**
     * 实验室业务层服务
     * 处理实验室相关业务逻辑
     */
    private final LabService labService;

    // ====================== 统一响应结果封装工具方法 ======================

    /**
     * 成功响应封装
     * @param data 响应返回的数据
     * @return 统一格式的成功响应
     */
    private RestResponse success(Object data) {
        RestResponse r = new RestResponse();
        r.setCode(200);
        r.setMsg("操作成功");
        r.setData(data);
        return r;
    }

    /**
     * 失败响应封装
     * @param msg 失败提示信息
     * @return 统一格式的失败响应
     */
    private RestResponse fail(String msg) {
        RestResponse r = new RestResponse();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    // ====================== 实验室 CRUD 接口 ======================

    /**
     * 查询所有实验室列表
     * @return 所有实验室数据集合
     */
    @GetMapping("/list")
    public RestResponse list() {
        List<Lab> list = labService.list();
        return success(list);
    }

    /**
     * 新增实验室信息
     * @param lab 前端传入的实验室实体数据
     * @return 新增操作结果
     */
    @PostMapping("/add")
    public RestResponse add(@RequestBody Lab lab) {
        boolean save = labService.save(lab);
        return save ? success(null) : fail("添加失败");
    }

    /**
     * 根据ID修改实验室信息
     * @param lab 前端传入的修改后实验室数据（必须包含id）
     * @return 修改操作结果
     */
    @PutMapping("/update")
    public RestResponse update(@RequestBody Lab lab) {
        boolean update = labService.updateById(lab);
        return update ? success(null) : fail("修改失败");
    }

    /**
     * 根据ID删除实验室
     * @param id 实验室ID
     * @return 删除操作结果
     */
    @DeleteMapping("/delete/{id}")
    public RestResponse delete(@PathVariable Integer id) {
        boolean remove = labService.removeById(id);
        return remove ? success(null) : fail("删除失败");
    }

    /**
     * 统一接口响应结果类
     * 用于给前端返回标准化格式：code(状态码) + msg(提示信息) + data(数据)
     */
    static class RestResponse {
        // 响应状态码：200成功 500失败
        private Integer code;
        // 响应提示信息
        private String msg;
        // 响应数据
        private Object data;

        // getter & setter
        public Integer getCode() { return code; }
        public void setCode(Integer code) { this.code = code; }
        public String getMsg() { return msg; }
        public void setMsg(String msg) { this.msg = msg; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
}