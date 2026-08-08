package com.stbu.labmanager.controller;

import com.stbu.labmanager.entity.Equipment;
import com.stbu.labmanager.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备信息管理 控制器
 * 处理前端关于实验室设备的增删改查请求
 */
@RestController
@RequestMapping("/equipment") // 统一接口前缀：/equipment
@RequiredArgsConstructor // 构造器注入，自动注入final修饰的服务
public class EquipmentController {

    /**
     * 设备业务层服务
     * 处理设备相关的业务逻辑
     */
    private final EquipmentService equipmentService;

    // ====================== 统一响应结果封装 ======================

    /**
     * 成功响应工具方法
     * @param data 要返回给前端的数据
     * @return 统一格式的成功响应
     */
    private RestResponse success(Object data) {
        RestResponse r = new RestResponse();
        r.setCode(200);       // 200=成功状态码
        r.setMsg("操作成功"); // 成功提示信息
        r.setData(data);      // 响应数据
        return r;
    }

    /**
     * 失败响应工具方法
     * @param msg 失败提示信息
     * @return 统一格式的失败响应
     */
    private RestResponse fail(String msg) {
        RestResponse r = new RestResponse();
        r.setCode(500);  // 500=失败状态码
        r.setMsg(msg);   // 失败提示信息
        return r;
    }

    // ====================== 设备 CRUD 接口 ======================

    /**
     * 查询所有设备列表
     * @return 所有设备数据集合
     */
    @GetMapping("/list")
    public RestResponse list() {
        List<Equipment> list = equipmentService.list();
        return success(list);
    }

    /**
     * 新增设备信息
     * @param equipment 前端传入的设备实体数据
     * @return 新增操作结果
     */
    @PostMapping("/add")
    public RestResponse add(@RequestBody Equipment equipment) {
        boolean save = equipmentService.save(equipment);
        return save ? success(null) : fail("添加失败");
    }

    /**
     * 根据ID修改设备信息
     * @param equipment 前端传入的修改后设备数据（必须包含id）
     * @return 修改操作结果
     */
    @PutMapping("/update")
    public RestResponse update(@RequestBody Equipment equipment) {
        boolean update = equipmentService.updateById(equipment);
        return update ? success(null) : fail("修改失败");
    }

    /**
     * 根据ID删除设备
     * @param id 设备ID
     * @return 删除操作结果
     */
    @DeleteMapping("/delete/{id}")
    public RestResponse delete(@PathVariable Integer id) {
        boolean remove = equipmentService.removeById(id);
        return remove ? success(null) : fail("删除失败");
    }

    /**
     * 统一响应结果内部类
     * 给前端返回固定格式：状态码+提示信息+数据
     */
    static class RestResponse {
        private Integer code;  // 响应状态码
        private String msg;    // 响应提示信息
        private Object data;   // 响应数据

        // getter & setter
        public Integer getCode() { return code; }
        public void setCode(Integer code) { this.code = code; }
        public String getMsg() { return msg; }
        public void setMsg(String msg) { this.msg = msg; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
}