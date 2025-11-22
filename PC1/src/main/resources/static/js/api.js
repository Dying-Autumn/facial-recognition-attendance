// API基础配置
const API_BASE_URL = 'http://localhost:8080/api';

// API工具类
class API {
    // 通用请求方法
    static async request(url, options = {}) {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
            },
        };

        const config = { ...defaultOptions, ...options };

        try {
            const response = await fetch(`${API_BASE_URL}${url}`, config);
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            // 如果是204 No Content，直接返回
            if (response.status === 204) {
                return null;
            }
            
            return await response.json();
        } catch (error) {
            console.error('API请求失败:', error);
            throw error;
        }
    }

    // GET请求
    static get(url) {
        return this.request(url, { method: 'GET' });
    }

    // POST请求
    static post(url, data) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data),
        });
    }

    // PUT请求
    static put(url, data) {
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(data),
        });
    }

    // DELETE请求
    static delete(url) {
        return this.request(url, { method: 'DELETE' });
    }

    // PATCH请求
    static patch(url, data = null) {
        const options = { method: 'PATCH' };
        if (data) {
            options.body = JSON.stringify(data);
        }
        return this.request(url, options);
    }
}

// 学生API
class StudentAPI {
    // 获取所有学生
    static getAll() {
        return API.get('/students');
    }

    // 根据ID获取学生
    static getById(id) {
        return API.get(`/students/${id}`);
    }

    // 根据学号获取学生
    static getByNumber(studentNumber) {
        return API.get(`/students/number/${studentNumber}`);
    }

    // 根据班级获取学生
    static getByClass(className) {
        return API.get(`/students/class/${className}`);
    }

    // 搜索学生
    static search(className) {
        return API.get(`/students/search?className=${encodeURIComponent(className)}`);
    }

    // 创建学生
    static create(student) {
        return API.post('/students', student);
    }

    // 更新学生
    static update(id, student) {
        return API.put(`/students/${id}`, student);
    }

    // 删除学生
    static delete(id) {
        return API.delete(`/students/${id}`);
    }
}

// 角色API
class RoleAPI {
    // 获取所有角色
    static getAll() {
        return API.get('/roles');
    }

    // 根据ID获取角色
    static getById(id) {
        return API.get(`/roles/${id}`);
    }

    // 创建角色
    static create(role) {
        return API.post('/roles', role);
    }

    // 更新角色
    static update(id, role) {
        return API.put(`/roles/${id}`, role);
    }

    // 删除角色
    static delete(id) {
        return API.delete(`/roles/${id}`);
    }
}

// 用户API
class UserAPI {
    // 获取所有用户
    static getAll() {
        return API.get('/users');
    }

    // 根据ID获取用户
    static getById(id) {
        return API.get(`/users/${id}`);
    }

    // 创建用户
    static create(user) {
        return API.post('/users', user);
    }

    // 更新用户
    static update(id, user) {
        return API.put(`/users/${id}`, user);
    }

    // 删除用户
    static delete(id) {
        return API.delete(`/users/${id}`);
    }

    // 登录
    static login(username, password) {
        return API.post('/users/login', { username, password });
    }

    // 注册
    static register(user) {
        return API.post('/users/register', user);
    }

    // 为用户分配角色
    static assignRole(userId, roleId) {
        return API.patch(`/users/${userId}/role?roleId=${roleId}`);
    }

    // 根据角色ID获取用户列表
    static getByRoleId(roleId) {
        return API.get(`/users/role/${roleId}`);
    }
}

// 课程班级API
class CourseClassAPI {
    // 获取所有课程班级
    static getAll() {
        return API.get('/course-classes');
    }

    // 根据ID获取课程班级
    static getById(id) {
        return API.get(`/course-classes/${id}`);
    }

    // 创建课程班级
    static create(courseClass) {
        return API.post('/course-classes', courseClass);
    }

    // 更新课程班级
    static update(id, courseClass) {
        return API.put(`/course-classes/${id}`, courseClass);
    }

    // 删除课程班级
    static delete(id) {
        return API.delete(`/course-classes/${id}`);
    }
}

// 考勤任务API
class AttendanceTaskAPI {
    // 获取所有考勤任务
    static getAll() {
        return API.get('/attendance-tasks');
    }

    // 根据ID获取考勤任务
    static getById(id) {
        return API.get(`/attendance-tasks/${id}`);
    }

    // 创建考勤任务
    static create(task) {
        return API.post('/attendance-tasks', task);
    }

    // 更新考勤任务
    static update(id, task) {
        return API.put(`/attendance-tasks/${id}`, task);
    }

    // 删除考勤任务
    static delete(id) {
        return API.delete(`/attendance-tasks/${id}`);
    }
}

// 功能管理API
class FunctionAPI {
    // 获取所有功能
    static getAll() {
        return API.get('/functions');
    }

    // 根据ID获取功能
    static getById(id) {
        return API.get(`/functions/${id}`);
    }

    // 获取活跃功能
    static getActive() {
        return API.get('/functions/active');
    }

    // 根据子系统获取功能
    static getBySubsystem(subsystem) {
        return API.get(`/functions/subsystem/${subsystem}`);
    }

    // 创建功能
    static create(functionEntity) {
        return API.post('/functions', functionEntity);
    }

    // 更新功能
    static update(id, functionEntity) {
        return API.put(`/functions/${id}`, functionEntity);
    }

    // 更新功能状态
    static updateStatus(id, status) {
        return API.patch(`/functions/${id}/status?status=${status}`);
    }

    // 删除功能
    static delete(id) {
        return API.delete(`/functions/${id}`);
    }

    // 根据角色ID获取功能
    static getByRoleId(roleId) {
        return API.get(`/functions/role/${roleId}`);
    }
}

// 权限分配API
class PermissionAPI {
    // 根据角色ID获取所有权限
    static getByRoleId(roleId) {
        return API.get(`/role-function-permissions/role/${roleId}`);
    }

    // 根据功能ID获取所有权限
    static getByFunctionId(functionId) {
        return API.get(`/role-function-permissions/function/${functionId}`);
    }

    // 根据角色ID和功能ID获取权限
    static getByRoleAndFunction(roleId, functionId) {
        return API.get(`/role-function-permissions/role/${roleId}/function/${functionId}`);
    }

    // 创建权限
    static create(permission) {
        return API.post('/role-function-permissions', permission);
    }

    // 更新权限
    static update(roleId, functionId, permission) {
        return API.put(`/role-function-permissions/role/${roleId}/function/${functionId}`, permission);
    }

    // 更新权限授权状态
    static updateGranted(roleId, functionId, granted) {
        return API.patch(`/role-function-permissions/role/${roleId}/function/${functionId}/granted?granted=${granted}`);
    }

    // 删除权限
    static delete(roleId, functionId) {
        return API.delete(`/role-function-permissions/role/${roleId}/function/${functionId}`);
    }

    // 批量创建权限
    static batchCreate(permissions) {
        return API.post('/role-function-permissions/batch', permissions);
    }

    // 批量更新权限（为角色批量分配权限）
    static batchUpdate(roleId, permissions) {
        return API.put(`/role-function-permissions/role/${roleId}/batch`, permissions);
    }

    // 获取角色已授权的权限
    static getGrantedByRoleId(roleId) {
        return API.get(`/role-function-permissions/role/${roleId}/granted`);
    }
}

