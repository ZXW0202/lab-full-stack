package com.stbu.labmanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stbu.labmanager.entity.User;
import com.stbu.labmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/**
 * 用户信息管理 控制器
 * 处理用户登录、注册、增删改查、密码修改等操作
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    /**
     * 用户业务层服务
     * 处理用户相关业务逻辑
     */
    private final UserService userService;

    // ====================== 统一响应结果封装 ======================

    /**
     * 成功响应工具方法
     * @param data 返回给前端的数据
     * @return 统一格式成功响应
     */
    private RestResponse success(Object data) {
        RestResponse r = new RestResponse();
        r.setCode(200);
        r.setMsg("操作成功");
        r.setData(data);
        return r;
    }

    /**
     * 失败响应工具方法
     * @param msg 失败提示信息
     * @return 统一格式失败响应
     */
    private RestResponse fail(String msg) {
        RestResponse r = new RestResponse();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    // ====================== 用户核心接口 ======================

    /**
     * 用户登录接口
     * @param user 登录用户信息（用户名+密码）
     * @return 登录结果：成功返回用户信息，失败返回提示
     */
    @PostMapping("/login")
    public RestResponse login(@Valid @RequestBody User user) {
        // 构建查询条件：用户名和密码同时匹配
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername())
                .eq(User::getPassword, user.getPassword());

        // 查询用户
        User loginUser = userService.getOne(wrapper);

        // 判断是否登录成功
        if (loginUser != null) {
            return success(loginUser);
        } else {
            return fail("用户名或密码错误");
        }
    }

    /**
     * 修改用户密码
     * @param params 包含用户id、旧密码、新密码
     * @return 修改结果
     */
    @PostMapping("/changePwd")
    public RestResponse changePwd(@RequestBody Map<String, String> params) {
        // 获取请求参数
        String id = params.get("id");
        String oldPwd = params.get("oldPwd");
        String newPwd = params.get("newPwd");

        // 非空校验
        if (id == null || oldPwd == null || newPwd == null) {
            return fail("参数不能为空");
        }

        // 根据ID查询用户是否存在
        User user = userService.getById(Long.parseLong(id));
        if (user == null) {
            return fail("用户不存在");
        }

        // 校验旧密码是否正确
        if (!user.getPassword().equals(oldPwd)) {
            return fail("旧密码错误");
        }

        // 设置新密码并更新
        user.setPassword(newPwd);
        boolean success = userService.updateById(user);

        return success ? success("密码修改成功") : fail("密码修改失败");
    }

    // ====================== 用户 CRUD 接口 ======================

    /**
     * 查询所有用户列表
     * @return 用户列表数据
     */
    @GetMapping("/list")
    public RestResponse list() {
        List<User> list = userService.list();
        return success(list);
    }

    /**
     * 新增用户（用户注册）
     * @param user 用户信息
     * @return 新增结果
     */
    @PostMapping("/add")
    public RestResponse add(@Valid @RequestBody User user) {
        boolean save = userService.save(user);
        return save ? success(null) : fail("注册失败");
    }

    /**
     * 根据ID查询单个用户信息
     * @param id 用户ID
     * @return 用户详情
     */
    @GetMapping("/{id}")
    public RestResponse getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return success(user);
    }

    /**
     * 修改用户信息
     * @param user 修改后的用户信息
     * @return 修改结果
     */
    @PutMapping("/update")
    public RestResponse update(@RequestBody User user) {
        boolean update = userService.updateById(user);
        return update ? success(null) : fail("修改失败");
    }

    /**
     * 根据ID删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public RestResponse delete(@PathVariable Long id) {
        boolean remove = userService.removeById(id);
        return remove ? success(null) : fail("删除失败");
    }

    /**
     * 统一响应结果内部类
     * 固定返回格式：code(状态码) + msg(提示信息) + data(数据)
     */
    static class RestResponse {
        private Integer code;   // 状态码：200成功 500失败
        private String msg;     // 提示信息
        private Object data;    // 响应数据

        // getter & setter
        public Integer getCode() { return code; }
        public void setCode(Integer code) { this.code = code; }
        public String getMsg() { return msg; }
        public void setMsg(String msg) { this.msg = msg; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
}