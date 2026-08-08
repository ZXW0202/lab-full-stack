package com.stbu.labmanager.controller;

import com.stbu.labmanager.entity.Borrow;
import com.stbu.labmanager.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 借用信息管理 控制器
 * 处理前端关于借用记录的增删改查请求
 *
 * @author: 自动生成
 * @date: 2025
 */
@RestController
@RequestMapping("/borrow")
@RequiredArgsConstructor
public class BorrowController {

    /**
     * 借用信息 业务层服务
     * 由 @RequiredArgsConstructor 自动注入，无需 @Autowired
     */
    private final BorrowService borrowService;

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
        r.setData(null);
        return r;
    }

    // ====================== 借用信息 CRUD 接口 ======================

    /**
     * 查询所有借用记录列表
     * @return 所有借用记录集合
     */
    @GetMapping("/list")
    public RestResponse list() {
        List<Borrow> list = borrowService.list();
        return success(list);
    }

    /**
     * 新增借用记录
     * @param borrow 前端传入的借用信息实体
     * @return 新增结果
     */
    @PostMapping("/add")
    public RestResponse add(@RequestBody Borrow borrow) {
        boolean save = borrowService.save(borrow);
        return save ? success(null) : fail("添加失败");
    }

    /**
     * 根据ID修改借用记录
     * @param borrow 前端传入的修改后借用信息（必须包含id）
     * @return 修改结果
     */
    @PutMapping("/update")
    public RestResponse update(@RequestBody Borrow borrow) {
        boolean update = borrowService.updateById(borrow);
        return update ? success(null) : fail("修改失败");
    }

    /**
     * 根据ID删除借用记录
     * @param id 借用记录ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public RestResponse delete(@PathVariable Integer id) {
        boolean remove = borrowService.removeById(id);
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