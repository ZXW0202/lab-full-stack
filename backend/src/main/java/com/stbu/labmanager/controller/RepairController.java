package com.stbu.labmanager.controller;

import com.stbu.labmanager.entity.Repair;
import com.stbu.labmanager.service.RepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 维修信息管理 控制器
 * 处理前端关于设备维修记录的增删改查请求
 */
@RestController
@RequestMapping("/repair")
@RequiredArgsConstructor
public class RepairController {

    /**
     * 维修业务层服务
     * 处理维修相关的业务逻辑
     */
    private final RepairService repairService;

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

    // ====================== 维修信息 CRUD 接口 ======================

    /**
     * 查询所有维修记录列表
     * @return 所有维修数据集合
     */
    @GetMapping("/list")
    public RestResponse list() {
        List<Repair> list = repairService.list();
        return success(list);
    }

    /**
     * 新增维修记录
     * @param repair 前端传入的维修实体数据
     * @return 新增操作结果
     */
    @PostMapping("/add")
    public RestResponse add(@RequestBody Repair repair) {
        boolean save = repairService.save(repair);
        return save ? success(null) : fail("添加失败");
    }

    /**
     * 根据ID修改维修记录
     * @param repair 前端传入的修改后维修数据（必须包含id）
     * @return 修改操作结果
     */
    @PutMapping("/update")
    public RestResponse update(@RequestBody Repair repair) {
        boolean update = repairService.updateById(repair);
        return update ? success(null) : fail("修改失败");
    }

    /**
     * 根据ID删除维修记录
     * @param id 维修记录ID
     * @return 删除操作结果
     */
    @DeleteMapping("/delete/{id}")
    public RestResponse delete(@PathVariable Integer id) {
        boolean remove = repairService.removeById(id);
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