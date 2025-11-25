// ========== 模态框和 Toast 管理 ==========

// Toast 提示
function showToast(message, type = 'success', duration = 3000) {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    const icons = {
        success: '✅',
        error: '❌',
        warning: '⚠️',
        info: 'ℹ️'
    };
    
    const titles = {
        success: '成功',
        error: '错误',
        warning: '警告',
        info: '提示'
    };
    
    toast.innerHTML = `
        <div class="toast-icon">${icons[type]}</div>
        <div class="toast-content">
            <div class="toast-title">${titles[type]}</div>
            <div class="toast-message">${message}</div>
        </div>
        <button class="toast-close">×</button>
    `;
    
    container.appendChild(toast);
    
    // 关闭按钮事件
    toast.querySelector('.toast-close').addEventListener('click', () => {
        toast.style.animation = 'slideInRight 0.3s ease reverse';
        setTimeout(() => toast.remove(), 300);
    });
    
    // 自动关闭
    if (duration > 0) {
        setTimeout(() => {
            toast.style.animation = 'slideInRight 0.3s ease reverse';
            setTimeout(() => toast.remove(), 300);
        }, duration);
    }
}

// 模态框管理
const Modal = {
    overlay: null,
    title: null,
    body: null,
    footer: null,
    submitBtn: null,
    cancelBtn: null,
    closeBtn: null,
    currentCallback: null,
    
    init() {
        this.overlay = document.getElementById('modal-overlay');
        this.title = document.getElementById('modal-title');
        this.body = document.getElementById('modal-body');
        this.footer = document.getElementById('modal-footer');
        this.submitBtn = document.getElementById('modal-submit');
        this.cancelBtn = document.getElementById('modal-cancel');
        this.closeBtn = document.getElementById('modal-close');
        
        // 绑定关闭事件
        this.closeBtn.addEventListener('click', () => this.close());
        this.cancelBtn.addEventListener('click', () => this.close());
        this.overlay.addEventListener('click', (e) => {
            if (e.target === this.overlay) this.close();
        });
        
        // ESC 键关闭
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && this.overlay.classList.contains('active')) {
                this.close();
            }
        });
    },
    
    open(options) {
        this.title.textContent = options.title || '提示';
        this.body.innerHTML = options.content || '';
        
        // 显示/隐藏底部按钮
        if (options.showFooter === false) {
            this.footer.style.display = 'none';
        } else {
            this.footer.style.display = 'flex';
        }
        
        // 设置按钮文本
        this.submitBtn.textContent = options.submitText || '确定';
        this.cancelBtn.textContent = options.cancelText || '取消';
        
        // 设置按钮样式
        this.submitBtn.className = `btn ${options.submitClass || 'btn-accent'}`;
        
        // 保存回调
        this.currentCallback = options.onSubmit;
        
        // 绑定提交事件
        const submitHandler = () => {
            if (this.currentCallback) {
                const result = this.currentCallback();
                // 如果返回 false，不关闭模态框
                if (result !== false) {
                    this.close();
                }
            } else {
                this.close();
            }
        };
        
        // 移除旧的事件监听器
        const newSubmitBtn = this.submitBtn.cloneNode(true);
        this.submitBtn.parentNode.replaceChild(newSubmitBtn, this.submitBtn);
        this.submitBtn = newSubmitBtn;
        this.submitBtn.addEventListener('click', submitHandler);
        
        // 显示模态框
        this.overlay.classList.add('active');
        
        // 自动聚焦第一个输入框
        setTimeout(() => {
            const firstInput = this.body.querySelector('input, select, textarea');
            if (firstInput) firstInput.focus();
        }, 100);
    },
    
    close() {
        this.overlay.classList.remove('active');
        this.currentCallback = null;
    },
    
    // 表单模态框
    form(options) {
        const fields = options.fields || [];
        const formHTML = fields.map(field => {
            const required = field.required ? '<span class="required">*</span>' : '';
            const value = field.value || '';
            
            let inputHTML = '';
            if (field.type === 'select') {
                const optionsHTML = field.options.map(opt => 
                    `<option value="${opt.value}" ${opt.value === value ? 'selected' : ''}>${opt.label}</option>`
                ).join('');
                inputHTML = `<select id="${field.id}" name="${field.name}">${optionsHTML}</select>`;
            } else if (field.type === 'textarea') {
                inputHTML = `<textarea id="${field.id}" name="${field.name}" ${field.required ? 'required' : ''}>${value}</textarea>`;
            } else {
                inputHTML = `<input type="${field.type || 'text'}" id="${field.id}" name="${field.name}" value="${value}" ${field.required ? 'required' : ''}>`;
            }
            
            return `
                <div class="form-group">
                    <label for="${field.id}">${field.label}${required}</label>
                    ${inputHTML}
                    <div class="error-message" id="${field.id}-error"></div>
                </div>
            `;
        }).join('');
        
        this.open({
            title: options.title,
            content: formHTML,
            submitText: options.submitText || '提交',
            submitClass: options.submitClass || 'btn-accent',
            onSubmit: () => {
                // 收集表单数据
                const formData = {};
                let isValid = true;
                
                fields.forEach(field => {
                    const input = document.getElementById(field.id);
                    const value = input.value.trim();
                    
                    // 验证必填项
                    if (field.required && !value) {
                        isValid = false;
                        input.parentElement.classList.add('error');
                        const errorEl = document.getElementById(`${field.id}-error`);
                        errorEl.textContent = `${field.label}不能为空`;
                        errorEl.classList.add('show');
                    } else {
                        input.parentElement.classList.remove('error');
                        const errorEl = document.getElementById(`${field.id}-error`);
                        errorEl.classList.remove('show');
                    }
                    
                    formData[field.name] = value;
                });
                
                if (!isValid) {
                    return false; // 阻止关闭模态框
                }
                
                // 调用回调
                if (options.onSubmit) {
                    options.onSubmit(formData);
                }
            }
        });
    },
    
    // 确认对话框
    confirm(options) {
        this.open({
            title: options.title || '确认',
            content: `<p style="font-size: 1.1rem; line-height: 1.6;">${options.message}</p>`,
            submitText: options.submitText || '确定',
            cancelText: options.cancelText || '取消',
            submitClass: options.danger ? 'btn-danger' : 'btn-accent',
            onSubmit: options.onConfirm
        });
    }
};

// 页面切换逻辑
document.addEventListener('DOMContentLoaded', function () {
    // 初始化模态框
    Modal.init();
    
    // 预加载高德地图API，提升后续地图初始化速度
    if (typeof initAMap === 'function') {
        initAMap(null, null).then((AMap) => {
            window.AMapInstance = AMap;
            console.log('高德地图API预加载完成');
        }).catch((e) => {
            console.warn('高德地图API预加载失败:', e);
        });
    }
    const menuItems = document.querySelectorAll('.menu-item');
    const submenuItems = document.querySelectorAll('.submenu-item');

    function hideAllPages() {
        document.querySelectorAll('.page.active').forEach(function (page) {
            page.classList.remove('active');
        });
    }

    function showPage(id, title) {
        let targetPage = document.getElementById(id);
        if (!targetPage) {
            targetPage = createPage(id, title);
        }
        hideAllPages();
        targetPage.classList.add('active');
        document.querySelector('.page-title').textContent = title;
    }

    // 菜单点击事件（统一处理：容器菜单只展开，不切页）
    menuItems.forEach(function (item) {
        item.addEventListener('click', function () {
            const target = this.getAttribute('data-target');
            const textEl = this.querySelector('.menu-text');
            const title = textEl ? textEl.textContent : '';

            // 如果是容器菜单（有子菜单），仅展开/折叠子菜单并返回
            if (this.matches('[data-target="basic-info"], [data-target="business"], [data-target="permission"]')) {
                // 切换菜单激活状态
                menuItems.forEach(function (mi) { mi.classList.remove('active'); });
                this.classList.add('active');

                const submenu = this.nextElementSibling;
                if (submenu && submenu.classList.contains('submenu')) {
                    submenu.classList.toggle('show');
                }
                return;
            }

            // 普通菜单项：切换页面
            menuItems.forEach(function (mi) { mi.classList.remove('active'); });
            this.classList.add('active');
            // 清除子菜单选中状态
            submenuItems.forEach(function (smi) { smi.classList.remove('active'); });
            showPage(target, title);
        });
    });

    // 子菜单点击事件
    submenuItems.forEach(function (item) {
        item.addEventListener('click', function () {
            const target = this.getAttribute('data-target');
            const title = this.textContent.trim();

            // 移除所有子菜单项的激活状态
            submenuItems.forEach(function (smi) { smi.classList.remove('active'); });
            this.classList.add('active');

            showPage(target, title);
        });
    });

    // 创建页面的函数（仅创建并返回，不负责显示/隐藏）
    function createPage(id, title) {
        // 创建新页面
        const newPage = document.createElement('div');
        newPage.id = id;
        newPage.className = 'page';

        // 根据ID设置页面内容
        let content = '';
        switch (id) {
            case 'course-management':
                content = `
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">课程信息管理</div>
                            <button class="btn btn-accent" onclick="addCourse()">添加课程</button>
                            <button class="btn" onclick="location.reload()">刷新</button>
                        </div>
                        <div class="card-body">
                            <div class="table-container">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>课程编号</th>
                                            <th>课程名称</th>
                                            <th>授课教师</th>
                                            <th>学分</th>
                                            <th>操作</th>
                                        </tr>
                                    </thead>
                                    <tbody id="course-table-body">
                                        <tr>
                                            <td>C001</td>
                                            <td>软件工程</td>
                                            <td>张老师</td>
                                            <td>3</td>
                                            <td>
                                                <div class="btn-group">
                                                    <button class="btn" onclick="editCourse('C001', '软件工程', '张老师', 3)">编辑</button>
                                                    <button class="btn btn-danger" onclick="deleteCourse('C001', '软件工程')">删除</button>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>C002</td>
                                            <td>数据结构</td>
                                            <td>李老师</td>
                                            <td>4</td>
                                            <td>
                                                <div class="btn-group">
                                                    <button class="btn" onclick="editCourse('C002', '数据结构', '李老师', 4)">编辑</button>
                                                    <button class="btn btn-danger" onclick="deleteCourse('C002', '数据结构')">删除</button>
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                `;
                break;
            case 'teacher-management':
                content = `
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">教师信息管理</div>
                            <button class="btn btn-accent" onclick="addTeacher()">添加教师</button>
                            <button class="btn" onclick="location.reload()">刷新</button>
                        </div>
                        <div class="card-body">
                            <div class="table-container">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>工号</th>
                                            <th>姓名</th>
                                            <th>职称</th>
                                            <th>所属院系</th>
                                            <th>操作</th>
                                        </tr>
                                    </thead>
                                    <tbody id="teacher-table-body">
                                        <tr>
                                            <td>T001</td>
                                            <td>张老师</td>
                                            <td>教授</td>
                                            <td>计算机学院</td>
                                            <td>
                                                <div class="btn-group">
                                                    <button class="btn" onclick="editTeacher('T001', '张老师', '教授', '计算机学院')">编辑</button>
                                                    <button class="btn btn-danger" onclick="deleteTeacher('T001', '张老师')">删除</button>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>T002</td>
                                            <td>李老师</td>
                                            <td>副教授</td>
                                            <td>计算机学院</td>
                                            <td>
                                                <div class="btn-group">
                                                    <button class="btn" onclick="editTeacher('T002', '李老师', '副教授', '计算机学院')">编辑</button>
                                                    <button class="btn btn-danger" onclick="deleteTeacher('T002', '李老师')">删除</button>
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                `;
                break;
            case 'student-management':
                content = `
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">学生信息管理</div>
                            <button class="btn btn-accent" onclick="addStudent()">添加学生</button>
                            <button class="btn" onclick="loadStudents()">刷新</button>
                        </div>
                        <div class="card-body">
                            <div class="table-container">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>学号</th>
                                            <th>姓名</th>
                                            <th>班级</th>
                                            <th>操作</th>
                                        </tr>
                                    </thead>
                                    <tbody id="student-table-body">
                                        <tr>
                                            <td colspan="4" style="text-align: center;">加载中...</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                `;
                // 加载学生数据
                setTimeout(loadStudents, 100);
                break;
            case 'user-management':
                content = `
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">用户信息管理</div>
                            <button class="btn btn-accent" onclick="addUser()">添加用户</button>
                            <button class="btn" onclick="loadUsersTable()">刷新</button>
                        </div>
                        <div class="card-body">
                            <div class="table-container">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>用户ID</th>
                                            <th>用户名</th>
                                            <th>真实姓名</th>
                                            <th>角色</th>
                                            <th>手机号</th>
                                            <th>邮箱</th>
                                            <th>创建时间</th>
                                            <th>操作</th>
                                        </tr>
                                    </thead>
                                    <tbody id="user-table-body">
                                        <tr>
                                            <td colspan="8" style="text-align: center;">加载中...</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                `;
                setTimeout(loadUsersTable, 100);
                break;
            case 'role-management':
                content = `
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">角色信息管理</div>
                            <button class="btn btn-accent" onclick="addRole()">添加角色</button>
                            <button class="btn" onclick="loadRolesTable()">刷新</button>
                        </div>
                        <div class="card-body">
                            <div class="table-container">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>角色ID</th>
                                            <th>角色名称</th>
                                            <th>角色描述</th>
                                            <th>创建时间</th>
                                            <th>操作</th>
                                        </tr>
                                    </thead>
                                    <tbody id="role-table-body">
                                        <tr>
                                            <td colspan="5" style="text-align: center;">加载中...</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                `;
                setTimeout(loadRolesTable, 100);
                break;
            case 'publish-task':
                content = `
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">发布考勤</div>
                        </div>
                        <div class="card-body">
                            <form id="publish-task-form">
                                <div class="form-group">
                                    <label for="task-class-select">选择班级 <span class="required">*</span></label>
                                    <select id="task-class-select" name="courseClassId" required>
                                        <option value="">正在加载班级...</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="task-name">任务名称 <span class="required">*</span></label>
                                    <input type="text" id="task-name" name="taskName" placeholder="例如：第1周 软件工程考勤" required>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="task-start-time">开始时间 <span class="required">*</span></label>
                                        <!-- 使用 time 类型，只显示时间 -->
                                        <input type="time" id="task-start-time" name="startTime" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="task-duration">持续时长(分钟) <span class="required">*</span></label>
                                        <div class="form-row" style="gap: 10px;">
                                            <select id="task-duration-select" style="flex: 1;" onchange="updateDurationInput(this.value)">
                                                <option value="5">5分钟</option>
                                                <option value="10" selected>10分钟</option>
                                                <option value="15">15分钟</option>
                                                <option value="30">30分钟</option>
                                                <option value="45">45分钟</option>
                                                <option value="custom">自定义</option>
                                            </select>
                                            <input type="number" id="task-duration-input" name="duration" value="10" min="1" style="flex: 1; display: none;" placeholder="输入分钟数">
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="form-group">
                                    <label>考勤地点设置 <span class="required">*</span></label>
                                    <div class="form-row" style="align-items: flex-end;">
                                        <div class="form-group" style="flex: 2;">
                                            <label for="location-range" style="font-size: 0.9em;">地点描述</label>
                                            <input type="text" id="location-range" name="locationRange" placeholder="例如：一教302" required>
                                        </div>
                                        <div class="form-group" style="flex: 1;">
                                            <button type="button" class="btn btn-secondary" id="btn-search-location" style="width: 100%; margin-bottom: 5px;">搜索</button>
                                        </div>
                                    </div>
                                    <!-- 地图容器 -->
                                    <div id="map-container" style="height: 300px; width: 100%; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; display: block;"></div>
                                    
                                    <div class="form-row">
                                        <div class="form-group" style="flex: 1;">
                                            <label for="radius" style="font-size: 0.9em;">有效半径(米)</label>
                                            <input type="number" id="radius" name="radius" value="30" required>
                                        </div>
                                    </div>
                                    <!-- 隐藏的经纬度输入框，用于表单提交到数据库 -->
                                    <div style="display: none;">
                                        <input type="number" id="latitude" name="latitude" step="0.0000001" required>
                                        <input type="number" id="longitude" name="longitude" step="0.0000001" required>
                                    </div>
                                </div>

                                <button type="submit" class="btn btn-accent" style="margin-top: 10px;">发布考勤</button>
                            </form>
                        </div>
                    </div>
                `;
                setTimeout(initPublishTaskPage, 100);
                break;
            case 'statistics':
                content = `
                    <div class="card" id="statistics">
                        <div class="card-header">
                            <div class="card-title">考勤统计</div>
                        </div>
                        <div class="card-body">
                            <div class="form-group">
                                <label>选择班级</label>
                                <select id="statistics-class-select">
                                    <option value="">正在加载...</option>
                                </select>
                            </div>
                            <button class="btn btn-accent">生成统计报告</button>

                            <div style="margin-top: 30px;">
                                <!-- 统计结果将在这里动态显示 -->
                            </div>
                        </div>
                    </div>
                `;
                setTimeout(initStatisticsPage, 100);
                break;
            case 'course-selection':
                content = `
                    <div class="card" id="course-selection">
                        <div class="card-header">
                            <div class="card-title">学生选课系统</div>
                        </div>
                        <div class="card-body">
                            <div class="course-selection-tabs">
                                <div class="tab-buttons">
                                    <button class="tab-btn active" data-tab="available-courses">
                                        <span class="tab-icon">📚</span>
                                        <span class="tab-text">可选课程</span>
                                    </button>
                                    <button class="tab-btn" data-tab="my-courses">
                                        <span class="tab-icon">📖</span>
                                        <span class="tab-text">我的课程</span>
                                    </button>
                                </div>

                                <div class="tab-content">
                                    <!-- 可选课程标签页 -->
                                    <div class="tab-pane active" id="available-courses">
                                        <div class="form-group" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                                            <div style="font-size: 14px; color: #7f8c8d;">找到 <span id="available-count" style="color: var(--primary-color); font-weight: 600;">0</span> 门可选课程</div>
                                            <button class="btn btn-accent" id="refresh-available-courses" style="display: flex; align-items: center; gap: 6px;">
                                                <span>🔄</span>
                                                <span>刷新可选课程</span>
                                            </button>
                                        </div>
                                        <div class="course-list" id="available-courses-list">
                                            <!-- 课程列表将在这里动态加载 -->
                                        </div>
                                    </div>

                                    <!-- 我的课程标签页 -->
                                    <div class="tab-pane" id="my-courses">
                                        <div class="form-group" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                                            <div style="font-size: 14px; color: #7f8c8d;">已选择 <span id="my-courses-count" style="color: var(--success-color); font-weight: 600;">0</span> 门课程</div>
                                            <button class="btn btn-secondary" id="refresh-my-courses" style="display: flex; align-items: center; gap: 6px;">
                                                <span>🔄</span>
                                                <span>刷新我的课程</span>
                                            </button>
                                        </div>
                                        <div class="course-list" id="my-courses-list">
                                            <!-- 已选课程列表将在这里动态加载 -->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
                setTimeout(initCourseSelectionPage, 100);
                break;
            case 'permission-assign':
                content = `
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">角色权限分配</div>
                            <button class="btn" onclick="loadPermissionAssignPage()">刷新</button>
                        </div>
                        <div class="card-body">
                            <div class="form-group">
                                <label for="permission-role-select">选择角色 <span class="required">*</span></label>
                                <select id="permission-role-select" onchange="onRoleSelected(this.value)">
                                    <option value="">请选择角色</option>
                                </select>
                            </div>
                            <div id="permission-content" style="display: none;">
                                <div style="margin: 20px 0; padding: 15px; background: #f5f5f5; border-radius: 4px;">
                                    <h3 id="selected-role-name" style="margin: 0 0 10px 0;"></h3>
                                    <p id="selected-role-desc" style="margin: 0; color: #666;"></p>
                                </div>
                                <div id="permission-tree" style="max-height: 600px; overflow-y: auto;">
                                    <!-- 权限树将在这里动态生成 -->
                                </div>
                                <div style="margin-top: 20px; text-align: right;">
                                    <button class="btn btn-secondary" onclick="selectAllPermissions()">全选</button>
                                    <button class="btn btn-secondary" onclick="deselectAllPermissions()">全不选</button>
                                    <button class="btn btn-accent" onclick="savePermissions()">保存权限</button>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
                setTimeout(initPermissionAssignPage, 100);
                break;
            default:
                content = `
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">${title}</div>
                        </div>
                        <div class="card-body">
                            <p>${title}功能正在开发中...</p>
                            <p>这里将实现${title}的相关功能。</p>
                        </div>
                    </div>
                `;
        }

        newPage.innerHTML = content;
        document.querySelector('.content-area').appendChild(newPage);
        return newPage;
    }
});

// ========== 学生管理功能 ==========

// 加载所有学生
async function loadStudents() {
    const tbody = document.getElementById('student-table-body');
    if (!tbody) return;
    
    tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">加载中...</td></tr>';
    
    try {
        const students = await StudentAPI.getAll();
        
        if (students.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">暂无学生数据</td></tr>';
            return;
        }
        
        tbody.innerHTML = students.map(student => `
            <tr>
                <td>${student.studentNumber || '-'}</td>
                <td>${student.studentName}</td>
                <td>${student.className || '-'}</td>
                <td>
                    <div class="btn-group">
                        <button class="btn" onclick="editStudent(${student.studentId})">编辑</button>
                        <button class="btn btn-danger" onclick="deleteStudent(${student.studentId})">删除</button>
                    </div>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('加载学生失败:', error);
        tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: red;">加载失败，请检查后端服务</td></tr>';
    }
}

// 添加学生
function addStudent() {
    Modal.form({
        title: '➕ 添加学生',
        fields: [
            { id: 'studentName', name: 'studentName', label: '姓名', type: 'text', required: true },
            { id: 'className', name: 'className', label: '班级', type: 'text', required: false },
            { id: 'phoneNumber', name: 'phoneNumber', label: '手机号', type: 'tel', required: false },
            { id: 'email', name: 'email', label: '邮箱', type: 'email', required: false }
        ],
        submitText: '添加',
        onSubmit: (formData) => {
            // 自动生成学号、用户名、密码
            const studentNumber = Generator.generateStudentNumber();
            const username = Generator.generateUsername(studentNumber);
            const password = Generator.generatePassword();
            
            // 创建用户账号数据
            const userData = Generator.createUserData({
                username: username,
                password: password,
                realName: formData.studentName,
                phoneNumber: formData.phoneNumber,
                email: formData.email,
                roleId: 3 // 学生角色
            });
            
            // 先创建用户账号
            UserAPI.create(userData)
                .then(newUser => {
                    // 创建学生记录
                    const student = {
                        studentNumber: studentNumber,
                        studentName: formData.studentName,
                        className: formData.className,
                        userId: newUser.userId
                    };
                    
                    return StudentAPI.create(student);
                })
                .then(() => {
                    // 显示账号信息
                    const accountInfo = Generator.showAccountInfo('student', studentNumber, username, password);
                    Modal.open({
                        title: '✅ 学生添加成功',
                        content: accountInfo,
                        showFooter: true,
                        submitText: '知道了',
                        cancelText: '复制账号信息',
                        onSubmit: () => {
                            loadStudents();
                        }
                    });
                })
                .catch(error => {
                    console.error('添加失败:', error);
                    showToast('添加失败：' + (error.message || '未知错误'), 'error');
                });
        }
    });
}

// 编辑学生
function editStudent(id) {
    StudentAPI.getById(id)
        .then(student => {
            Modal.form({
                title: '✏️ 编辑学生信息',
                fields: [
                    { id: 'studentNumber', name: 'studentNumber', label: '学号', type: 'text', value: student.studentNumber, required: true },
                    { id: 'studentName', name: 'studentName', label: '姓名', type: 'text', value: student.studentName, required: true },
                    { id: 'className', name: 'className', label: '班级', type: 'text', value: student.className, required: false }
                ],
                submitText: '保存',
                onSubmit: (formData) => {
                    const updatedStudent = {
                        studentNumber: formData.studentNumber,
                        studentName: formData.studentName,
                        className: formData.className,
                        userId: student.userId // 保持原有的用户ID，不允许修改
                    };
                    
                    StudentAPI.update(id, updatedStudent)
                        .then(() => {
                            showToast('学生信息更新成功！', 'success');
                            loadStudents();
                        })
                        .catch(error => {
                            console.error('更新失败:', error);
                            showToast('更新失败，请重试', 'error');
                        });
                }
            });
        })
        .catch(error => {
            console.error('获取学生信息失败:', error);
            showToast('获取学生信息失败', 'error');
        });
}

// 删除学生
function deleteStudent(id) {
    // 验证ID是否有效
    if (!id || id === 'undefined' || id === 'null' || (typeof id === 'string' && isNaN(id))) {
        showToast('无效的学生ID', 'error');
        return;
    }
    
    // 确保ID是数字类型
    const studentId = parseInt(id);
    if (isNaN(studentId) || studentId <= 0) {
        showToast('无效的学生ID', 'error');
        return;
    }
    
    Modal.confirm({
        title: '⚠️ 确认删除',
        message: '确定要删除这个学生吗？<br><br>如果该学生有选课记录，删除操作将失败。',
        submitText: '删除',
        danger: true,
        onConfirm: () => {
            StudentAPI.delete(studentId)
                .then((response) => {
                    const message = response?.message || '学生删除成功！';
                    showToast(message, 'success');
                    loadStudents();
                })
                .catch(error => {
                    console.error('删除失败:', error);
                    // 显示详细的错误信息
                    let errorMessage = '删除失败，请重试';
                    if (error.message) {
                        errorMessage = error.message;
                    } else if (error.response?.message) {
                        errorMessage = error.response.message;
                    }
                    
                    // 如果是409冲突错误（有选课记录），提供更详细的提示
                    if (error.status === 409 || errorMessage.includes('选课记录')) {
                        errorMessage = errorMessage + '<br><br>提示：请先删除或处理该学生的选课记录后再删除学生。';
                    }
                    
                    showToast(errorMessage, 'error');
                });
        }
    });
}

// ========== 用户管理功能 ==========

// 加载所有用户到表格
async function loadUsersTable() {
    const tbody = document.getElementById('user-table-body');
    if (!tbody) return;
    
    tbody.innerHTML = '<tr><td colspan="8" style="text-align: center;">加载中...</td></tr>';
    
    try {
        const users = await UserAPI.getAll();
        
        if (users.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" style="text-align: center;">暂无用户数据</td></tr>';
            return;
        }
        
        // 按用户ID排序
        users.sort((a, b) => a.userId - b.userId);
        
        tbody.innerHTML = users.map(user => `
            <tr>
                <td>${user.userId}</td>
                <td>${user.username}</td>
                <td>${user.realName}</td>
                <td>${user.roleId === 1 ? '系统管理员' : user.roleId === 2 ? '教师' : user.roleId === 3 ? '学生' : '未知'}</td>
                <td>${user.phoneNumber || '-'}</td>
                <td>${user.email || '-'}</td>
                <td>${user.createdDate ? new Date(user.createdDate).toLocaleString('zh-CN') : '-'}</td>
                <td>
                    <div class="btn-group">
                        <button class="btn" onclick="editUser(${user.userId})">编辑</button>
                        <button class="btn btn-danger" onclick="deleteUser(${user.userId}, '${user.username}')">删除</button>
                    </div>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('加载用户失败:', error);
        tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; color: red;">加载失败，请检查后端服务</td></tr>';
    }
}

// 添加用户
function addUser() {
    Modal.form({
        title: '添加用户',
        fields: [
            { id: 'username', name: 'username', label: '用户名', type: 'text', required: true },
            { id: 'password', name: 'password', label: '密码', type: 'password', required: true },
            { id: 'realName', name: 'realName', label: '真实姓名', type: 'text', required: true },
            { id: 'roleId', name: 'roleId', label: '角色', type: 'select', required: true,
              options: [
                  { value: '', label: '请选择角色' },
                  { value: '1', label: '系统管理员' },
                  { value: '2', label: '教师' },
                  { value: '3', label: '学生' }
              ]
            },
            { id: 'phoneNumber', name: 'phoneNumber', label: '手机号', type: 'tel', required: false },
            { id: 'email', name: 'email', label: '邮箱', type: 'email', required: false }
        ],
        submitText: '添加',
        onSubmit: (formData) => {
            const userData = {
                ...formData,
                roleId: parseInt(formData.roleId),
                isActive: 1
            };
            
            UserAPI.create(userData)
                .then(() => {
                    showToast('用户添加成功！', 'success');
                    loadUsersTable();
                })
                .catch(error => {
                    console.error('添加失败:', error);
                    showToast('添加失败：' + (error.message || '未知错误'), 'error');
                });
        }
    });
}

// 编辑用户
function editUser(id) {
    UserAPI.getById(id)
        .then(user => {
            Modal.form({
                title: '编辑用户信息',
                fields: [
                    { id: 'username', name: 'username', label: '用户名', type: 'text', value: user.username, required: true },
                    { id: 'realName', name: 'realName', label: '真实姓名', type: 'text', value: user.realName, required: true },
                    { id: 'roleId', name: 'roleId', label: '角色', type: 'select', value: String(user.roleId), required: true,
                      options: [
                          { value: '1', label: '系统管理员' },
                          { value: '2', label: '教师' },
                          { value: '3', label: '学生' }
                      ]
                    },
                    { id: 'phoneNumber', name: 'phoneNumber', label: '手机号', type: 'tel', value: user.phoneNumber || '', required: false },
                    { id: 'email', name: 'email', label: '邮箱', type: 'email', value: user.email || '', required: false }
                ],
                submitText: '保存',
                onSubmit: (formData) => {
                    const updatedUser = {
                        ...formData,
                        roleId: parseInt(formData.roleId)
                    };
                    
                    UserAPI.update(id, updatedUser)
                        .then(() => {
                            showToast('用户信息更新成功！', 'success');
                            loadUsersTable();
                        })
                        .catch(error => {
                            console.error('更新失败:', error);
                            showToast('更新失败，请重试', 'error');
                        });
                }
            });
        })
        .catch(error => {
            console.error('获取用户信息失败:', error);
            showToast('获取用户信息失败', 'error');
        });
}

// 删除用户
function deleteUser(id, username) {
    // 验证ID是否有效
    if (!id || id === 'undefined' || id === 'null' || (typeof id === 'string' && isNaN(id))) {
        showToast('无效的用户ID', 'error');
        return;
    }
    
    // 确保ID是数字类型
    const userId = parseInt(id);
    if (isNaN(userId) || userId <= 0) {
        showToast('无效的用户ID', 'error');
        return;
    }
    
    Modal.confirm({
        title: '确认删除用户',
        message: `确定要删除用户"${username}"吗？<br><br>此操作不可撤销。`,
        submitText: '删除',
        danger: true,
        onConfirm: () => {
            UserAPI.delete(userId)
                .then((response) => {
                    const message = response?.message || `用户"${username}"删除成功！`;
                    showToast(message, 'success');
                    loadUsersTable();
                })
                .catch(error => {
                    console.error('删除失败:', error);
                    // 显示详细的错误信息
                    const errorMessage = error.message || error.response?.message || '删除失败，请重试';
                    showToast(errorMessage, 'error');
                });
        }
    });
}

// ========== 角色管理功能 ==========

// 加载所有角色到表格
async function loadRolesTable() {
    const tbody = document.getElementById('role-table-body');
    if (!tbody) return;
    
    tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">加载中...</td></tr>';
    
    try {
        const roles = await RoleAPI.getAll();
        
        if (roles.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">暂无角色数据</td></tr>';
            return;
        }
        
        tbody.innerHTML = roles.map(role => `
            <tr>
                <td>${role.roleId}</td>
                <td>${role.roleName}</td>
                <td>${role.description || '-'}</td>
                <td>${role.createdTime ? new Date(role.createdTime).toLocaleString('zh-CN') : '-'}</td>
                <td>
                    <div class="btn-group">
                        <button class="btn btn-success" onclick="assignPermissions(${role.roleId}, '${role.roleName}')">分配权限</button>
                        <button class="btn" onclick="editRole(${role.roleId})">编辑</button>
                        <button class="btn btn-danger" onclick="deleteRole(${role.roleId}, '${role.roleName}')">删除</button>
                    </div>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('加载角色失败:', error);
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: red;">加载失败，请检查后端服务</td></tr>';
    }
}

// 添加角色
function addRole() {
    Modal.form({
        title: '➕ 添加角色',
        fields: [
            { id: 'roleName', name: 'roleName', label: '角色名称', type: 'text', required: true },
            { id: 'roleDesc', name: 'description', label: '角色描述', type: 'textarea', required: false }
        ],
        submitText: '添加',
        onSubmit: (formData) => {
            RoleAPI.create(formData)
                .then(() => {
                    showToast('角色添加成功！', 'success');
                    loadRolesTable();
                })
                .catch(error => {
                    console.error('添加失败:', error);
                    showToast('添加失败：' + (error.message || '未知错误'), 'error');
                });
        }
    });
}

// 编辑角色
function editRole(id) {
    RoleAPI.getById(id)
        .then(role => {
            Modal.form({
                title: '✏️ 编辑角色信息',
                fields: [
                    { id: 'roleName', name: 'roleName', label: '角色名称', type: 'text', value: role.roleName, required: true },
                    { id: 'roleDesc', name: 'description', label: '角色描述', type: 'textarea', value: role.description || '', required: false }
                ],
                submitText: '保存',
                onSubmit: (formData) => {
                    RoleAPI.update(id, formData)
                        .then(() => {
                            showToast('角色信息更新成功！', 'success');
                            loadRolesTable();
                        })
                        .catch(error => {
                            console.error('更新失败:', error);
                            showToast('更新失败，请重试', 'error');
                        });
                }
            });
        })
        .catch(error => {
            console.error('获取角色信息失败:', error);
            showToast('获取角色信息失败', 'error');
        });
}

// 删除角色
function deleteRole(id, roleName) {
    Modal.confirm({
        title: '⚠️ 确认删除角色',
        message: `确定要删除角色"${roleName}"吗？<br><br>此操作不可撤销，且会影响该角色下的所有用户。`,
        submitText: '删除',
        danger: true,
        onConfirm: () => {
            RoleAPI.delete(id)
                .then(() => {
                    showToast(`角色"${roleName}"删除成功！`, 'success');
                    loadRolesTable();
                })
                .catch(error => {
                    console.error('删除失败:', error);
                    showToast('删除失败，请重试', 'error');
                });
        }
    });
}

// 分配权限
function assignPermissions(roleId, roleName) {
    pendingPermissionRoleId = roleId;
    
    // 确保权限管理子菜单展开
    const permissionMenu = document.querySelector('.menu-item[data-target="permission"]');
    if (permissionMenu) {
        const submenu = permissionMenu.nextElementSibling;
        if (submenu && !submenu.classList.contains('show')) {
            permissionMenu.click();
        }
    }
    
    // 切换到权限分配页面
    const permissionPageEntry = document.querySelector('.submenu-item[data-target="permission-assign"]');
    if (!permissionPageEntry) {
        showToast('未找到权限分配页面入口', 'error');
        return;
    }
    permissionPageEntry.click();
    
    // 重新加载页面数据并尝试选中目标角色
    loadPermissionAssignPage();
    setTimeout(applyPendingPermissionRoleSelection, 200);
    
    showToast(`已为角色"${roleName}"打开权限分配页面`, 'info');
}

// 加载所有角色（保留旧的方法名，用于其他地方调用）
async function loadRoles() {
    try {
        const roles = await RoleAPI.getAll();
        console.log('角色列表:', roles);
        return roles;
    } catch (error) {
        console.error('加载角色失败:', error);
        return [];
    }
}

// ========== 通用工具函数 ==========

// 显示消息提示
function showMessage(message, type = 'info') {
    // 可以后续实现更美观的消息提示组件
    alert(message);
}

// 确认对话框
function showConfirm(message) {
    return confirm(message);
}

// ========== 课程管理功能 ==========

// 添加课程
function addCourse() {
    const tbody = document.getElementById('course-table-body');
    if (!tbody) {
        showToast('请先打开课程管理页面！', 'warning');
        return;
    }
    
    Modal.form({
        title: '➕ 添加课程',
        fields: [
            { id: 'courseCode', name: 'courseCode', label: '课程编号', type: 'text', required: true },
            { id: 'courseName', name: 'courseName', label: '课程名称', type: 'text', required: true },
            { id: 'teacher', name: 'teacher', label: '授课教师', type: 'text', required: true },
            { id: 'credits', name: 'credits', label: '学分', type: 'number', required: true }
        ],
        submitText: '添加',
        onSubmit: (formData) => {
            const newRow = document.createElement('tr');
            newRow.innerHTML = `
                <td>${formData.courseCode}</td>
                <td>${formData.courseName}</td>
                <td>${formData.teacher}</td>
                <td>${formData.credits}</td>
                <td>
                    <div class="btn-group">
                        <button class="btn" onclick="editCourse('${formData.courseCode}', '${formData.courseName}', '${formData.teacher}', ${formData.credits})">编辑</button>
                        <button class="btn btn-danger" onclick="deleteCourse('${formData.courseCode}', '${formData.courseName}')">删除</button>
                    </div>
                </td>
            `;
            tbody.appendChild(newRow);
            showToast('课程添加成功！', 'success');
        }
    });
}

// 编辑课程
function editCourse(courseCode, courseName, teacher, credits) {
    Modal.form({
        title: '✏️ 编辑课程信息',
        fields: [
            { id: 'courseCode', name: 'courseCode', label: '课程编号', type: 'text', value: courseCode, required: true },
            { id: 'courseName', name: 'courseName', label: '课程名称', type: 'text', value: courseName, required: true },
            { id: 'teacher', name: 'teacher', label: '授课教师', type: 'text', value: teacher, required: true },
            { id: 'credits', name: 'credits', label: '学分', type: 'number', value: credits, required: true }
        ],
        submitText: '保存',
        onSubmit: (formData) => {
            // 查找并更新对应的行
            const tbody = document.getElementById('course-table-body');
            const rows = tbody.getElementsByTagName('tr');
            
            for (let row of rows) {
                const cells = row.getElementsByTagName('td');
                if (cells[0].textContent === courseCode) {
                    cells[0].textContent = formData.courseCode;
                    cells[1].textContent = formData.courseName;
                    cells[2].textContent = formData.teacher;
                    cells[3].textContent = formData.credits;
                    // 更新按钮的参数
                    const editBtn = cells[4].querySelector('.btn');
                    editBtn.onclick = function() { editCourse(formData.courseCode, formData.courseName, formData.teacher, formData.credits); };
                    break;
                }
            }
            
            showToast('课程信息更新成功！', 'success');
        }
    });
}

// 删除课程
function deleteCourse(courseCode, courseName) {
    Modal.confirm({
        title: '⚠️ 确认删除课程',
        message: `确定要删除课程吗？<br><br><strong>课程编号：</strong>${courseCode}<br><strong>课程名称：</strong>${courseName}<br><br>此操作不可撤销。`,
        submitText: '删除',
        danger: true,
        onConfirm: () => {
            const tbody = document.getElementById('course-table-body');
            const rows = tbody.getElementsByTagName('tr');
            
            for (let i = 0; i < rows.length; i++) {
                const cells = rows[i].getElementsByTagName('td');
                if (cells[0].textContent === courseCode) {
                    tbody.removeChild(rows[i]);
                    showToast(`课程 ${courseName} 删除成功！`, 'success');
                    return;
                }
            }
        }
    });
}

// ========== 教师管理功能 ==========

// 添加教师
function addTeacher() {
    const tbody = document.getElementById('teacher-table-body');
    if (!tbody) {
        showToast('请先打开教师管理页面！', 'warning');
        return;
    }
    
    Modal.form({
        title: '➕ 添加教师',
        fields: [
            { id: 'teacherName', name: 'teacherName', label: '教师姓名', type: 'text', required: true },
            { id: 'title', name: 'title', label: '职称', type: 'select', required: true, 
              options: [
                  { value: '', label: '请选择职称' },
                  { value: '讲师', label: '讲师' },
                  { value: '副教授', label: '副教授' },
                  { value: '教授', label: '教授' }
              ]
            },
            { id: 'department', name: 'department', label: '所属院系', type: 'text', required: true },
            { id: 'phoneNumber', name: 'phoneNumber', label: '手机号', type: 'tel', required: false },
            { id: 'email', name: 'email', label: '邮箱', type: 'email', required: false }
        ],
        submitText: '添加',
        onSubmit: (formData) => {
            // 自动生成工号、用户名、密码
            const teacherNumber = Generator.generateTeacherNumber();
            const username = Generator.generateUsername(teacherNumber);
            const password = Generator.generatePassword();
            
            // 创建用户账号数据
            const userData = Generator.createUserData({
                username: username,
                password: password,
                realName: formData.teacherName,
                phoneNumber: formData.phoneNumber,
                email: formData.email,
                roleId: 2 // 教师角色
            });
            
            // 先创建用户账号
            UserAPI.create(userData)
                .then(newUser => {
                    // 创建教师记录（这里使用静态表格，实际应该调用 TeacherAPI）
                    const newRow = document.createElement('tr');
                    newRow.innerHTML = `
                        <td>${teacherNumber}</td>
                        <td>${formData.teacherName}</td>
                        <td>${formData.title}</td>
                        <td>${formData.department}</td>
                        <td>
                            <div class="btn-group">
                                <button class="btn" onclick="editTeacher('${teacherNumber}', '${formData.teacherName}', '${formData.title}', '${formData.department}')">编辑</button>
                                <button class="btn btn-danger" onclick="deleteTeacher('${teacherNumber}', '${formData.teacherName}')">删除</button>
                            </div>
                        </td>
                    `;
                    tbody.appendChild(newRow);
                    
                    // 显示账号信息
                    const accountInfo = Generator.showAccountInfo('teacher', teacherNumber, username, password);
                    Modal.open({
                        title: '✅ 教师添加成功',
                        content: accountInfo,
                        showFooter: true,
                        submitText: '知道了',
                        cancelText: '复制账号信息',
                        onSubmit: () => {
                            // 刷新页面或重新加载数据
                        }
                    });
                })
                .catch(error => {
                    console.error('添加失败:', error);
                    showToast('添加失败：' + (error.message || '未知错误'), 'error');
                });
        }
    });
}

// 编辑教师
function editTeacher(teacherId, teacherName, title, department) {
    Modal.form({
        title: '✏️ 编辑教师信息',
        fields: [
            { id: 'teacherId', name: 'teacherId', label: '教师工号', type: 'text', value: teacherId, required: true },
            { id: 'teacherName', name: 'teacherName', label: '教师姓名', type: 'text', value: teacherName, required: true },
            { id: 'title', name: 'title', label: '职称', type: 'select', value: title, required: true,
              options: [
                  { value: '讲师', label: '讲师' },
                  { value: '副教授', label: '副教授' },
                  { value: '教授', label: '教授' }
              ]
            },
            { id: 'department', name: 'department', label: '所属院系', type: 'text', value: department, required: true }
        ],
        submitText: '保存',
        onSubmit: (formData) => {
            const tbody = document.getElementById('teacher-table-body');
            const rows = tbody.getElementsByTagName('tr');
            
            for (let row of rows) {
                const cells = row.getElementsByTagName('td');
                if (cells[0].textContent === teacherId) {
                    cells[0].textContent = formData.teacherId;
                    cells[1].textContent = formData.teacherName;
                    cells[2].textContent = formData.title;
                    cells[3].textContent = formData.department;
                    break;
                }
            }
            
            showToast('教师信息更新成功！', 'success');
        }
    });
}

// 删除教师
function deleteTeacher(teacherId, teacherName) {
    Modal.confirm({
        title: '⚠️ 确认删除教师',
        message: `确定要删除教师吗？<br><br><strong>工号：</strong>${teacherId}<br><strong>姓名：</strong>${teacherName}<br><br>此操作不可撤销。`,
        submitText: '删除',
        danger: true,
        onConfirm: () => {
            const tbody = document.getElementById('teacher-table-body');
            const rows = tbody.getElementsByTagName('tr');
            
            for (let i = 0; i < rows.length; i++) {
                const cells = rows[i].getElementsByTagName('td');
                if (cells[0].textContent === teacherId) {
                    tbody.removeChild(rows[i]);
                    showToast(`教师 ${teacherName} 删除成功！`, 'success');
                    return;
                }
            }
        }
    });
}

// ========== 考勤任务功能 ==========

// 初始化发布考勤任务页面
function initPublishTaskPage() {
    const form = document.getElementById('publish-task-form');
    const select = document.getElementById('task-class-select');
    const btnSearchLocation = document.getElementById('btn-search-location');
    
    // 辅助函数：更新隐藏的经纬度输入框（用于表单提交到数据库）
    function updateCoordinates(lat, lng) {
        const latHidden = document.getElementById('latitude');
        const lngHidden = document.getElementById('longitude');
        
        if (latHidden) latHidden.value = lat.toFixed(7);
        if (lngHidden) lngHidden.value = lng.toFixed(7);
    }
    
    // 设置默认时间
    const now = new Date();
    // 设置默认时间为当前时间 (HH:mm)
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const currentTimeStr = `${hours}:${minutes}`;
    
    const startTimeInput = document.getElementById('task-start-time');
    // const endTimeInput = document.getElementById('task-end-time'); // 已移除
    
    if (startTimeInput) startTimeInput.value = currentTimeStr;
    // if (endTimeInput) endTimeInput.value = end; // 已移除

    // 加载班级
    CourseClassAPI.getAll().then(classes => {
        if (!classes || classes.length === 0) {
            select.innerHTML = '<option value="">暂无班级数据</option>';
            return;
        }
        select.innerHTML = '<option value="">请选择班级</option>' + 
            classes.map(c => `<option value="${c.classId}">${c.className} (ID:${c.classId})</option>`).join('');
    }).catch(err => {
        console.error(err);
        select.innerHTML = '<option value="">加载失败</option>';
        showToast('加载班级失败，请检查网络或后端服务', 'error');
    });

    // 初始化地图函数
    function initMap(lat, lng) {
        const mapContainer = document.getElementById('map-container');
        if (!mapContainer) return;
        
        // 如果已经初始化过地图，先销毁
        if (window.currentMap) {
            window.currentMap.destroy();
            window.currentMap = null;
            window.currentMarker = null;
        }
        
        // 验证并转换坐标值
        const numLat = parseFloat(lat);
        const numLng = parseFloat(lng);
        const isValidLat = !isNaN(numLat) && numLat >= -90 && numLat <= 90;
        const isValidLng = !isNaN(numLng) && numLng >= -180 && numLng <= 180;
        
        // 默认位置：如果没有提供有效坐标，则默认为北京
        const defaultLat = isValidLat ? numLat : 39.9042;
        const defaultLng = isValidLng ? numLng : 116.4074;
        const zoomLevel = (isValidLat && isValidLng) ? 16 : 12;

        // 使用高德地图初始化
        // 如果 AMap 已经加载，直接创建地图；否则先加载 AMap
        if (window.AMapInstance) {
            return Promise.resolve(createAMapInstance(window.AMapInstance, defaultLng, defaultLat, zoomLevel, 
                isValidLat ? numLat : null, isValidLng ? numLng : null));
        } else {
            // 先加载 AMap 对象（不创建地图实例）
            return initAMap(null, null).then((AMap) => {
                window.AMapInstance = AMap;
                return createAMapInstance(AMap, defaultLng, defaultLat, zoomLevel, 
                    isValidLat ? numLat : null, isValidLng ? numLng : null);
            }).catch((e) => {
                console.error('高德地图加载失败:', e);
                mapContainer.innerHTML = '<div style="padding: 20px; text-align: center; color: #666;">地图加载失败，请检查网络连接</div>';
                throw e;
            });
        }
    }
    
    // 创建高德地图实例的辅助函数
    function createAMapInstance(AMap, defaultLng, defaultLat, zoomLevel, lat, lng) {
        // 确保坐标是有效的数字
        const validLng = typeof defaultLng === 'number' && !isNaN(defaultLng) ? defaultLng : 116.4074;
        const validLat = typeof defaultLat === 'number' && !isNaN(defaultLat) ? defaultLat : 39.9042;
        const validZoom = typeof zoomLevel === 'number' && !isNaN(zoomLevel) ? zoomLevel : 12;
        
        const map = new AMap.Map('map-container', {
            zoom: validZoom,
            center: [validLng, validLat],
            viewMode: '3D'
        });
        
        window.currentMap = map;
        
        // 添加比例尺
        map.addControl(new AMap.Scale());
        
        // 先删除旧的标记
        if (window.currentMarker) {
            window.currentMarker.setMap(null);
            window.currentMarker = null;
        }
        
        // 验证lat和lng是否有效
        const numLat = parseFloat(lat);
        const numLng = parseFloat(lng);
        const hasValidCoords = !isNaN(numLat) && !isNaN(numLng) && 
                               numLat >= -90 && numLat <= 90 && 
                               numLng >= -180 && numLng <= 180;
        
        // 如果有有效坐标，创建标记
        if (hasValidCoords) {
            window.currentMarker = new AMap.Marker({
                position: [numLng, numLat],
                draggable: true
            });
            window.currentMarker.setMap(map);
        }

        // 地图点击事件
        map.on('click', async function(e) {
                const clickedLng = e.lnglat.lng;
                const clickedLat = e.lnglat.lat;
                
                // 更新可见和隐藏的经纬度输入框
                updateCoordinates(clickedLat, clickedLng);
                
                // 更新或创建标记（确保只有一个标记）
                if (window.currentMarker) {
                    // 如果已存在标记，只更新位置
                    window.currentMarker.setPosition([clickedLng, clickedLat]);
                } else {
                    // 如果不存在标记，创建新标记
                    window.currentMarker = new AMap.Marker({
                        position: [clickedLng, clickedLat],
                        draggable: true
                    });
                    window.currentMarker.setMap(map);
                }
                
                // 高德地图逆地理编码获取地址
                try {
                    showToast('正在获取地址信息...', 'info', 1000);
                    const geocoder = new AMap.Geocoder();
                    geocoder.getAddress([clickedLng, clickedLat], (status, result) => {
                        if (status === 'complete' && result.info === 'OK') {
                            const address = result.regeocode.formattedAddress;
                            const locationRangeInput = document.getElementById('location-range');
                            if (locationRangeInput) {
                                // 简化地址显示（取前两个部分）
                                const addressParts = address.split(/省|市|区|县|街道|路|号/);
                                const simplifiedAddress = addressParts.slice(0, 3).join('') || address;
                                locationRangeInput.value = simplifiedAddress;
                                
                                // 设置标记信息窗口
                                if (window.currentMarker) {
                                    window.currentMarker.setTitle(simplifiedAddress);
                                    window.currentMarker.setLabel({
                                        content: simplifiedAddress,
                                        direction: 'right'
                                    });
                                }
                            }
                        }
                    });
                } catch (err) {
                    console.warn('逆地理编码失败:', err);
                }
        });
        
        // 标记拖拽事件
        if (window.currentMarker) {
            window.currentMarker.on('dragend', async function(e) {
                    const position = window.currentMarker.getPosition();
                    const lat = position.getLat();
                    const lng = position.getLng();
                    
                    // 更新可见和隐藏的经纬度输入框
                    updateCoordinates(lat, lng);
                    
                    // 拖拽后更新地址
                    try {
                        const geocoder = new AMap.Geocoder();
                        geocoder.getAddress([lng, lat], (status, result) => {
                            if (status === 'complete' && result.info === 'OK') {
                                const address = result.regeocode.formattedAddress;
                                const locationRangeInput = document.getElementById('location-range');
                                if (locationRangeInput) {
                                    const addressParts = address.split(/省|市|区|县|街道|路|号/);
                                    const simplifiedAddress = addressParts.slice(0, 3).join('') || address;
                                    locationRangeInput.value = simplifiedAddress;
                                    if (window.currentMarker) {
                                        window.currentMarker.setTitle(simplifiedAddress);
                                        window.currentMarker.setLabel({
                                            content: simplifiedAddress,
                                            direction: 'right'
                                        });
                                    }
                                }
                            }
                        });
            } catch (err) {
                console.warn('逆地理编码失败:', err);
            }
        });
        }
        
        return map;
    }
    
    // 处理地图加载错误的辅助函数
    function handleMapError(mapContainer, e) {
        console.error('高德地图加载失败:', e);
        mapContainer.innerHTML = '<div style="padding: 20px; text-align: center; color: #666;">地图加载失败，请检查网络连接</div>';
    }

    // 页面加载完成后初始化地图（尝试获取位置，如果失败则显示默认地图）
    setTimeout(() => {
        const mapContainer = document.getElementById('map-container');
        if (!mapContainer) return;
        
        // 先初始化地图，然后尝试获取定位
        initMap().then(map => {
            if (!map || !window.AMapInstance) {
                return;
            }
            
            // 使用高德地图定位插件获取当前位置
            window.AMapInstance.plugin('AMap.Geolocation', function() {
                const geolocation = new window.AMapInstance.Geolocation({
                    enableHighAccuracy: true,      // 进行浏览器原生定位的时候是否尝试获取较高精度
                    timeout: 20000,                 // 定位的超时时间，毫秒
                    maximumAge: 0,                  // 浏览器原生定位的缓存时间，毫秒
                    convert: true,                  // 是否将定位结果转换为高德坐标
                    showButton: false,              // 不显示定位按钮
                    showMarker: false,              // 不显示定位点
                    showCircle: false,              // 不显示定位精度圆
                    panToLocation: false,           // 定位成功后不自动移动到响应位置
                    zoomToAccuracy: false,          // 定位成功后不自动调整级别
                    GeoLocationFirst: true,          // 优先使用H5定位
                    getCityWhenFail: true,          // 定位失败之后返回基本城市定位信息
                    needAddress: true,              // 需要将定位结果进行逆地理编码操作
                    extensions: 'base'              // 返回基本的逆地理编码信息
                });
                
                geolocation.getCurrentPosition(function(status, result) {
                    if (status === 'complete' && result.info === 'SUCCESS') {
                        // 定位成功
                        const lat = result.position.getLat();
                        const lng = result.position.getLng();
                        
                        // 更新可见和隐藏的经纬度输入框
                        updateCoordinates(lat, lng);
                        
                        // 获取定位类型描述
                        const locationTypes = {
                            1: 'IP定位',
                            2: 'H5定位',
                            3: 'SDK定位',
                            4: 'IP城市定位'
                        };
                        const locationType = locationTypes[result.location_type] || '未知类型';
                        
                        // 更新地图中心点和标记
                        if (window.currentMap) {
                            // 根据定位类型和精度调整缩放级别
                            let zoomLevel = 16;
                            if (result.location_type === 1 || result.location_type === 4) {
                                // IP定位精度较低
                                zoomLevel = 12;
                            } else if (result.accuracy) {
                                // 根据精度调整缩放级别
                                if (result.accuracy > 1000) zoomLevel = 12;
                                else if (result.accuracy > 500) zoomLevel = 14;
                                else if (result.accuracy > 100) zoomLevel = 15;
                            }
                            
                            window.currentMap.setCenter([lng, lat]);
                            window.currentMap.setZoom(zoomLevel);
                            
                            // 如果已有标记，更新位置；否则创建新标记
                            if (window.currentMarker) {
                                window.currentMarker.setPosition([lng, lat]);
                            } else {
                                window.currentMarker = new window.AMapInstance.Marker({
                                    position: [lng, lat],
                                    draggable: true,
                                    map: window.currentMap
                                });
                            }
                            
                            // 处理地址信息（needAddress为true时，result中已包含地址信息）
                            let address = '';
                            if (result.formattedAddress) {
                                address = result.formattedAddress;
                            } else if (result.addressComponent) {
                                // 从结构化地址组件构建地址
                                const addr = result.addressComponent;
                                address = (addr.province || '') + (addr.city || '') + (addr.district || '') + (addr.street || '');
                            }
                            
                            if (address) {
                                const locationRangeInput = document.getElementById('location-range');
                                if (locationRangeInput) {
                                    const addressParts = address.split(/省|市|区|县|街道|路|号/);
                                    const simplifiedAddress = addressParts.slice(0, 3).join('') || address;
                                    locationRangeInput.value = simplifiedAddress;
                                    
                                    if (window.currentMarker) {
                                        const markerTitle = simplifiedAddress + (result.location_type === 1 || result.location_type === 4 ? ' (IP定位)' : '');
                                        window.currentMarker.setTitle(markerTitle);
                                        window.currentMarker.setLabel({
                                            content: simplifiedAddress,
                                            direction: 'right'
                                        });
                                    }
                                }
                            }
                            
                            console.log('定位成功:', {
                                type: locationType,
                                accuracy: result.accuracy ? result.accuracy + '米' : '未知',
                                address: address
                            });
                        }
                    } else {
                        // 定位失败
                        const errorMsg = '失败原因排查信息: ' + (result.message || '未知错误');
                        const originMsg = result.originMessage ? '<br>浏览器返回信息: ' + result.originMessage : '';
                        const infoMsg = result.info ? '<br>错误代码: ' + result.info : '';
                        console.error('定位失败详情:', errorMsg + originMsg + infoMsg);
                        
                        // 如果设置了getCityWhenFail，可能返回城市信息
                        if (result.formattedAddress) {
                            console.log('获取到城市信息:', result.formattedAddress);
                        }
                    }
                });
            });
        }).catch(e => {
            handleMapError(mapContainer, e);
        });
    }, 500);

    // 全局函数：处理时长选择
    window.updateDurationInput = function(value) {
        const input = document.getElementById('task-duration-input');
        if (value === 'custom') {
            input.style.display = 'block';
            input.value = '';
            input.focus();
        } else {
            input.style.display = 'none';
            input.value = value;
        }
    };

    // 搜索地点功能（使用高德地图搜索）
    if (btnSearchLocation) {
        btnSearchLocation.addEventListener('click', async () => {
            const locationInput = document.getElementById('location-range');
            const query = locationInput.value.trim();
            
            if (!query) {
                showToast('请输入要搜索的地点名称', 'warning');
                return;
            }
            
            btnSearchLocation.textContent = '搜索中...';
            btnSearchLocation.disabled = true;
            
            try {
                // 确保地图已初始化
                if (!window.currentMap || !window.AMapInstance) {
                    await initMap();
                }
                
                const AMap = window.AMapInstance;
                const map = window.currentMap;
                
                // 使用高德地图搜索
                const placeSearch = new AMap.PlaceSearch({
                    city: '全国', // 搜索范围
                    pageSize: 5,
                    pageIndex: 1
                });
                
                placeSearch.search(query, (status, result) => {
                    if (status === 'complete' && result.info === 'OK') {
                        if (result.poiList && result.poiList.pois && result.poiList.pois.length > 0) {
                            const poi = result.poiList.pois[0];
                            const lat = poi.location.lat;
                            const lng = poi.location.lng;
                            
                            // 更新可见和隐藏的经纬度输入框
                            updateCoordinates(lat, lng);
                            
                            // 更新地图中心点和标记
                            map.setCenter([lng, lat]);
                            map.setZoom(16);
                            
                            // 更新或创建标记（确保只有一个标记）
                            if (window.currentMarker) {
                                // 如果已存在标记，只更新位置和标签
                                window.currentMarker.setPosition([lng, lat]);
                                window.currentMarker.setTitle(poi.name);
                                window.currentMarker.setLabel({
                                    content: poi.name,
                                    direction: 'right'
                                });
                            } else {
                                // 如果不存在标记，创建新标记
                                window.currentMarker = new AMap.Marker({
                                    position: [lng, lat],
                                    draggable: true,
                                    title: poi.name
                                });
                                window.currentMarker.setMap(map);
                                window.currentMarker.setLabel({
                                    content: poi.name,
                                    direction: 'right'
                                });
                                
                                // 绑定拖拽事件
                                window.currentMarker.on('dragend', function(e) {
                                    const position = window.currentMarker.getPosition();
                                    const dragLat = position.getLat();
                                    const dragLng = position.getLng();
                                    updateCoordinates(dragLat, dragLng);
                                    
                                    // 拖拽后更新地址
                                    try {
                                        const geocoder = new AMap.Geocoder();
                                        geocoder.getAddress([dragLng, dragLat], (status, result) => {
                                            if (status === 'complete' && result.info === 'OK') {
                                                const address = result.regeocode.formattedAddress;
                                                const locationRangeInput = document.getElementById('location-range');
                                                if (locationRangeInput) {
                                                    const addressParts = address.split(/省|市|区|县|街道|路|号/);
                                                    const simplifiedAddress = addressParts.slice(0, 3).join('') || address;
                                                    locationRangeInput.value = simplifiedAddress;
                                                    if (window.currentMarker) {
                                                        window.currentMarker.setTitle(simplifiedAddress);
                                                        window.currentMarker.setLabel({
                                                            content: simplifiedAddress,
                                                            direction: 'right'
                                                        });
                                                    }
                                                }
                                            }
                                        });
                                    } catch (err) {
                                        console.warn('逆地理编码失败:', err);
                                    }
                                });
                            }
                            
                            showToast('已定位到搜索地点', 'success');
                        } else {
                            showToast('未找到相关地点，请尝试更详细的描述', 'warning');
                        }
                    } else {
                        showToast('搜索服务暂不可用', 'error');
                    }
                    
                    btnSearchLocation.textContent = '搜索';
                    btnSearchLocation.disabled = false;
                });
            } catch (e) {
                console.error('搜索失败:', e);
                showToast('搜索发生错误，请检查网络', 'error');
                btnSearchLocation.textContent = '搜索';
                btnSearchLocation.disabled = false;
            }
        });
    }

        // 处理提交
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const formData = new FormData(form);

        // 计算开始和结束时间
        const startTimeValue = formData.get('startTime'); // HH:mm
        const duration = parseInt(document.getElementById('task-duration-input').value) || 10;
        
        // 构造完整的开始时间 (今天日期 + 选择的时间)
        const now = new Date();
        const [hours, minutes] = startTimeValue.split(':').map(Number);
        const startDateTime = new Date(now.getFullYear(), now.getMonth(), now.getDate(), hours, minutes);
        
        // 如果选择的时间已经过去了（比如现在10点，选择了9点），是否默认为明天？
        // 暂时按当天处理，如果是补签或测试可能需要过去时间
        
        // 构造结束时间
        const endDateTime = new Date(startDateTime.getTime() + duration * 60000);
        
        // 处理时区偏移，转换为 ISO 字符串发送给后端
        // 注意：后端 AttendanceTask.java 使用 LocalDateTime，通常期望不带时区的 ISO 格式 (YYYY-MM-DDTHH:mm:ss)
        // 或者前端手动拼接格式
        
        const formatDate = (date) => {
            const pad = (n) => String(n).padStart(2, '0');
            return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
        };

        const task = {
            courseClassId: parseInt(formData.get('courseClassId')),
            taskName: formData.get('taskName'),
            startTime: formatDate(startDateTime),
            endTime: formatDate(endDateTime),
            locationRange: formData.get('locationRange'),
            latitude: parseFloat(formData.get('latitude')),
            longitude: parseFloat(formData.get('longitude')),
            radius: parseInt(formData.get('radius')),
            teacherId: 1 // 暂时硬编码教师ID
        };
        
        try {
            await AttendanceTaskAPI.create(task);
            showToast('考勤发布成功！', 'success');
            form.reset();
            // 重置时间为当前时间
            if (startTimeInput) {
                const now = new Date();
                const h = String(now.getHours()).padStart(2, '0');
                const m = String(now.getMinutes()).padStart(2, '0');
                startTimeInput.value = `${h}:${m}`;
            }
            // 重置时长选择
            document.getElementById('task-duration-select').value = '10';
            document.getElementById('task-duration-input').style.display = 'none';
            document.getElementById('task-duration-input').value = '10';
            
            // 隐藏地图
            const mapContainer = document.getElementById('map-container');
            if (mapContainer) {
                mapContainer.style.display = 'none';
            }
        } catch (err) {
            console.error(err);
            showToast('发布失败：' + (err.message || '未知错误'), 'error');
        }
    });
}

// ========== 权限分配功能 ==========

let currentRoleId = null;
let allFunctions = [];
let rolePermissions = new Set();
let pendingPermissionRoleId = null;

// 初始化权限分配页面
async function initPermissionAssignPage() {
    await loadRolesForPermission();
}

// 加载角色列表到下拉框
async function loadRolesForPermission() {
    const select = document.getElementById('permission-role-select');
    if (!select) return;
    
    try {
        const roles = await RoleAPI.getAll();
        select.innerHTML = '<option value="">请选择角色</option>' +
            roles.map(role => `<option value="${role.roleId}">${role.roleName}</option>`).join('');
        
        // 如果有待跳转的角色，尝试自动选中
        applyPendingPermissionRoleSelection();
    } catch (error) {
        console.error('加载角色失败:', error);
        showToast('加载角色失败', 'error');
    }
}

// 角色选择变化事件
async function onRoleSelected(roleId) {
    if (!roleId) {
        document.getElementById('permission-content').style.display = 'none';
        currentRoleId = null;
        return;
    }
    
    currentRoleId = parseInt(roleId);
    await loadPermissionData();
}

// 加载权限数据
async function loadPermissionData() {
    if (!currentRoleId) return;
    
    try {
        // 加载所有功能
        allFunctions = await FunctionAPI.getActive();
        
        // 加载角色已有的权限
        const permissions = await PermissionAPI.getRolePermissions(currentRoleId);
        rolePermissions = new Set(permissions.map(p => p.functionId));
        
        // 加载角色信息
        const role = await RoleAPI.getById(currentRoleId);
        document.getElementById('selected-role-name').textContent = `当前角色：${role.roleName}`;
        document.getElementById('selected-role-desc').textContent = role.description || '暂无描述';
        
        // 渲染权限树
        renderPermissionTree();
        
        // 显示权限内容
        document.getElementById('permission-content').style.display = 'block';
    } catch (error) {
        console.error('加载权限数据失败:', error);
        showToast('加载权限数据失败', 'error');
    }
}

// 渲染权限树（按照用户提供的权限设计组织）
function renderPermissionTree() {
    const container = document.getElementById('permission-tree');
    if (!container) return;
    
    // 按照模块分组权限
    const modules = groupFunctionsByModule(allFunctions);
    
    let html = '';
    
    // 根据当前选择的角色显示对应的权限分组
    if (currentRoleId === 3 || currentRoleId === 1) { // 学生或管理员
        html += renderPermissionSection('学生权限', [
            {
                title: '课程相关',
                permissions: [
                    { code: 'course.view', name: '查看课程', desc: '只能查看自己已选的课程列表' },
                    { code: 'course.view', name: '查看课程详情', desc: '只能查看自己课程的教学大纲、课件、作业等' },
                    { code: 'course.add', name: '选课操作', desc: '在选课期内选择未满员的课程' },
                    { code: 'course.delete', name: '退课操作', desc: '在规定时间内退选已选课程' }
                ]
            },
            {
                title: '个人信息',
                permissions: [
                    { code: 'user.edit', name: '修改个人信息', desc: '修改联系方式、密码等基础信息' },
                    { code: 'dashboard.view', name: '查看个人课表', desc: '查看自己的课程时间安排' }
                ]
            }
        ], 3);
    }
    
    if (currentRoleId === 2 || currentRoleId === 1) { // 教师或管理员
        html += renderPermissionSection('教师权限', [
            {
                title: '课程管理',
                permissions: [
                    { code: 'course.view', name: '查看所教课程', desc: '只能查看自己负责的课程' },
                    { code: 'course.edit', name: '课程内容管理', desc: '管理自己课程的教学资料、课件、视频等' },
                    { code: 'attendance_task.add', name: '发布通知', desc: '向自己课程的学生发布课程通知' },
                    { code: 'course.edit', name: '设置课程信息', desc: '修改自己课程的基本信息（除课程代码等核心信息）' }
                ]
            },
            {
                title: '学生管理',
                permissions: [
                    { code: 'student.view', name: '查看选课学生', desc: '查看选择自己课程的学生名单' },
                    { code: 'attendance.edit', name: '成绩管理', desc: '录入、修改、发布自己课程的学生成绩' },
                    { code: 'attendance.view_all', name: '考勤记录', desc: '记录和管理学生的出勤情况' }
                ]
            }
        ], 2);
    }
    
    if (currentRoleId === 1) { // 管理员
        html += renderPermissionSection('管理员权限', [
            {
                title: '用户管理',
                permissions: [
                    { code: 'user.view', name: '用户账户管理', desc: '创建、修改、禁用学生和教师账户' },
                    { code: 'user.assign_role', name: '角色分配', desc: '为用户分配学生、教师或管理员角色' },
                    { code: 'user.add', name: '批量操作', desc: '批量导入用户信息' }
                ]
            },
            {
                title: '课程体系管理',
                permissions: [
                    { code: 'course.add', name: '课程创建/删除', desc: '创建新课程或删除旧课程' },
                    { code: 'class.add', name: '课程分配', desc: '将课程分配给特定教师授课' },
                    { code: 'course.edit', name: '课程属性设置', desc: '设置课程代码、学分、容量等核心信息' }
                ]
            },
            {
                title: '系统管理',
                permissions: [
                    { code: 'permission.assign', name: '权限管理', desc: '定义和修改各角色的权限范围' },
                    { code: 'dashboard.view', name: '数据维护', desc: '备份和恢复系统数据' },
                    { code: 'dashboard.view', name: '系统监控', desc: '监控系统运行状态和用户活动' },
                    { code: 'dashboard.view', name: '全局设置', desc: '设置学期、选课时间等系统参数' }
                ]
            }
        ], 1);
    }
    
    // 其他权限（从数据库加载的实际权限，按模块显示）
    html += renderOtherPermissions(modules);
    
    container.innerHTML = html;
}

// 渲染权限分组
function renderPermissionSection(title, groups, roleId) {
    let html = `<div class="permission-section" style="margin-bottom: 30px;">
        <h3 style="margin-bottom: 15px; color: var(--primary-color);">${title}</h3>`;
    
    groups.forEach(group => {
        html += `<div class="permission-group" style="margin-bottom: 20px;">
            <h4 style="margin-bottom: 10px; font-size: 1.1em; color: #666;">${group.title}</h4>
            <div class="permission-list" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(350px, 1fr)); gap: 12px;">`;
        
        group.permissions.forEach(perm => {
            // 查找匹配的功能（可能多个功能有相同的代码，取第一个）
            const matchingFunctions = allFunctions.filter(f => f.functionCode === perm.code);
            const functionId = matchingFunctions.length > 0 ? matchingFunctions[0].functionId : null;
            const isChecked = functionId && rolePermissions.has(functionId);
            
            // 如果找不到对应的功能，使用功能代码作为标识
            const displayFunctionId = functionId || perm.code;
            
            html += `
                <label class="permission-item" style="display: flex; align-items: flex-start; padding: 14px; border: 1px solid #ddd; border-radius: 6px; cursor: pointer; background: ${isChecked ? '#e8f5e9' : '#fff'}; transition: all 0.2s; min-width: 0;">
                    <input type="checkbox" 
                           class="permission-checkbox" 
                           data-function-id="${displayFunctionId}" 
                           data-function-code="${perm.code}"
                           ${isChecked ? 'checked' : ''}
                           ${!functionId ? 'disabled title="该功能在系统中不存在"' : ''}
                           style="margin-right: 12px; margin-top: 2px; flex-shrink: 0; width: 18px; height: 18px; cursor: pointer;">
                    <div style="flex: 1; min-width: 0; word-wrap: break-word; word-break: break-word; white-space: normal; line-height: 1.5;">
                        <div style="font-weight: 500; margin-bottom: 6px; color: #2c3e50; font-size: 14px;">${perm.name}${!functionId ? ' <span style="color: #999; font-size: 0.85em;">(未配置)</span>' : ''}</div>
                        <div style="font-size: 0.85em; color: #666; line-height: 1.4;">${perm.desc}</div>
                    </div>
                </label>
            `;
        });
        
        html += `</div></div>`;
    });
    
    html += `</div>`;
    return html;
}

// 渲染其他权限（从数据库加载）
function renderOtherPermissions(modules) {
    let html = '<div class="permission-section" style="margin-top: 30px;"><h3 style="margin-bottom: 15px; color: var(--primary-color);">其他功能权限</h3>';
    
    Object.keys(modules).sort().forEach(moduleName => {
        html += `<div class="permission-group" style="margin-bottom: 20px;">
            <h4 style="margin-bottom: 10px; font-size: 1.1em; color: #666;">${moduleName}</h4>
            <div class="permission-list" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(350px, 1fr)); gap: 12px;">`;
        
        modules[moduleName].forEach(func => {
            const isChecked = rolePermissions.has(func.functionId);
            html += `
                <label class="permission-item" style="display: flex; align-items: flex-start; padding: 14px; border: 1px solid #ddd; border-radius: 6px; cursor: pointer; background: ${isChecked ? '#e8f5e9' : '#fff'}; transition: all 0.2s; min-width: 0;">
                    <input type="checkbox" 
                           class="permission-checkbox" 
                           data-function-id="${func.functionId}" 
                           data-function-code="${func.functionCode}"
                           ${isChecked ? 'checked' : ''}
                           style="margin-right: 12px; margin-top: 2px; flex-shrink: 0; width: 18px; height: 18px; cursor: pointer;">
                    <div style="flex: 1; min-width: 0; word-wrap: break-word; word-break: break-word; white-space: normal; line-height: 1.5;">
                        <div style="font-weight: 500; margin-bottom: 6px; color: #2c3e50; font-size: 14px;">${func.functionName}</div>
                        <div style="font-size: 0.85em; color: #666; line-height: 1.4;">${func.description || func.functionCode}</div>
                    </div>
                </label>
            `;
        });
        
        html += `</div></div>`;
    });
    
    html += '</div>';
    return html;
}

// 按模块分组功能
function groupFunctionsByModule(functions) {
    const modules = {};
    functions.forEach(func => {
        const moduleName = func.moduleName || '其他';
        if (!modules[moduleName]) {
            modules[moduleName] = [];
        }
        modules[moduleName].push(func);
    });
    return modules;
}

// 根据功能代码查找功能ID
function findFunctionIdByCode(functionCode) {
    const func = allFunctions.find(f => f.functionCode === functionCode);
    return func ? func.functionId : null;
}

// 全选权限
function selectAllPermissions() {
    const checkboxes = document.querySelectorAll('.permission-checkbox');
    checkboxes.forEach(cb => {
        cb.checked = true;
        updatePermissionItemStyle(cb);
    });
}

// 全不选权限
function deselectAllPermissions() {
    const checkboxes = document.querySelectorAll('.permission-checkbox');
    checkboxes.forEach(cb => {
        cb.checked = false;
        updatePermissionItemStyle(cb);
    });
}

// 更新权限项样式
function updatePermissionItemStyle(checkbox) {
    const item = checkbox.closest('.permission-item');
    if (item) {
        item.style.background = checkbox.checked ? '#e8f5e9' : '#fff';
    }
}

// 保存权限
async function savePermissions() {
    if (!currentRoleId) {
        showToast('请先选择角色', 'warning');
        return;
    }
    
    const checkboxes = document.querySelectorAll('.permission-checkbox:not(:disabled)');
    const selectedFunctionIds = [];
    const unselectedFunctionIds = [];
    
    checkboxes.forEach(cb => {
        const functionId = parseInt(cb.dataset.functionId);
        if (!isNaN(functionId) && functionId > 0) {
            if (cb.checked) {
                selectedFunctionIds.push(functionId);
            } else {
                // 如果之前有权限但现在取消了，需要撤销
                if (rolePermissions.has(functionId)) {
                    unselectedFunctionIds.push(functionId);
                }
            }
        }
    });
    
    if (selectedFunctionIds.length === 0 && unselectedFunctionIds.length === 0) {
        showToast('没有需要保存的权限变更', 'info');
        return;
    }
    
    try {
        // 先撤销取消的权限
        if (unselectedFunctionIds.length > 0) {
            await PermissionAPI.revokePermissions(currentRoleId, unselectedFunctionIds);
        }
        
        // 再分配新选择的权限（只分配新增的，已存在的不会重复分配）
        if (selectedFunctionIds.length > 0) {
            // 过滤掉已经存在的权限
            const newFunctionIds = selectedFunctionIds.filter(id => !rolePermissions.has(id));
            if (newFunctionIds.length > 0) {
                await PermissionAPI.assignPermissions(currentRoleId, newFunctionIds);
            }
        }
        
        showToast('权限保存成功！', 'success');
        
        // 重新加载权限数据以更新界面
        await loadPermissionData();
    } catch (error) {
        console.error('保存权限失败:', error);
        showToast('保存权限失败：' + (error.message || '未知错误'), 'error');
    }
}

// 重新加载权限分配页面
function loadPermissionAssignPage() {
    if (currentRoleId) {
        loadPermissionData();
    } else {
        initPermissionAssignPage();
    }
}

// 尝试应用待分配角色选择
function applyPendingPermissionRoleSelection() {
    if (!pendingPermissionRoleId) return false;
    
    const select = document.getElementById('permission-role-select');
    if (!select) return false;
    
    const targetValue = String(pendingPermissionRoleId);
    const hasOption = Array.from(select.options).some(opt => opt.value === targetValue);
    if (!hasOption) {
        return false;
    }
    
    select.value = targetValue;
    const roleId = pendingPermissionRoleId;
    pendingPermissionRoleId = null;
    onRoleSelected(targetValue);
    return true;
}

// 绑定权限复选框变化事件
document.addEventListener('change', function(e) {
    if (e.target.classList.contains('permission-checkbox')) {
        updatePermissionItemStyle(e.target);
    }
});

// ========== 学生选课功能 ==========

// 初始化学生选课页面
function initCourseSelectionPage() {
    // 确保样式正确应用
    const tabButtonsContainer = document.querySelector('#course-selection .tab-buttons');
    if (tabButtonsContainer) {
        // 确保容器样式
        tabButtonsContainer.style.display = 'flex';
        tabButtonsContainer.style.gap = '16px';
        tabButtonsContainer.style.marginBottom = '30px';
        tabButtonsContainer.style.padding = '8px';
        tabButtonsContainer.style.background = '#f8f9fa';
        tabButtonsContainer.style.borderRadius = '12px';
        tabButtonsContainer.style.boxShadow = 'inset 0 2px 4px rgba(0, 0, 0, 0.06)';
    }
    
    // 标签页切换
    const tabButtons = document.querySelectorAll('#course-selection .tab-btn');
    const tabPanes = document.querySelectorAll('#course-selection .tab-pane');
    
    // 确保按钮样式
    tabButtons.forEach(btn => {
        btn.style.display = 'flex';
        btn.style.alignItems = 'center';
        btn.style.justifyContent = 'center';
        btn.style.gap = '8px';
        btn.style.flex = '1';
        btn.style.padding = '16px 24px';
        btn.style.borderRadius = '8px';
        btn.style.border = 'none';
        btn.style.cursor = 'pointer';
        btn.style.fontSize = '15px';
        btn.style.fontWeight = '600';
        btn.style.transition = 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)';
        btn.style.position = 'relative';
        
        if (btn.classList.contains('active')) {
            btn.style.color = 'white';
            btn.style.background = 'linear-gradient(135deg, #3498db 0%, #2980b9 50%, #1abc9c 100%)';
            btn.style.boxShadow = '0 4px 15px rgba(52, 152, 219, 0.4), 0 2px 8px rgba(52, 152, 219, 0.3)';
            btn.style.transform = 'translateY(-2px)';
        } else {
            btn.style.color = '#6c757d';
            btn.style.background = 'transparent';
        }
    });

    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            // 移除所有活动状态
            tabButtons.forEach(btn => {
                btn.classList.remove('active');
                btn.style.color = '#6c757d';
                btn.style.background = 'transparent';
                btn.style.boxShadow = 'none';
                btn.style.transform = 'translateY(0)';
            });
            tabPanes.forEach(pane => pane.classList.remove('active'));

            // 添加当前活动状态
            button.classList.add('active');
            button.style.color = 'white';
            button.style.background = 'linear-gradient(135deg, #3498db 0%, #2980b9 50%, #1abc9c 100%)';
            button.style.boxShadow = '0 4px 15px rgba(52, 152, 219, 0.4), 0 2px 8px rgba(52, 152, 219, 0.3)';
            button.style.transform = 'translateY(-2px)';
            
            const tabId = button.getAttribute('data-tab');
            document.getElementById(tabId).classList.add('active');

            // 加载对应标签页的数据
            loadTabData(tabId);
        });
    });

    // 绑定按钮事件
    document.getElementById('refresh-available-courses').addEventListener('click', () => loadAvailableCourses());
    document.getElementById('refresh-my-courses').addEventListener('click', () => loadMyCourses());

    // 初始化加载可选课程
    loadAvailableCourses();
}

// 加载标签页数据
function loadTabData(tabId) {
    switch(tabId) {
        case 'available-courses':
            loadAvailableCourses();
            break;
        case 'my-courses':
            loadMyCourses();
            break;
    }
}

// 加载可选课程
function loadAvailableCourses() {
    const container = document.getElementById('available-courses-list');
    container.innerHTML = '<div class="loading">正在加载...</div>';

    // 调用后端API获取可选课程
    StudentCourseAPI.getAvailableCourses(1) // 假设学生ID为1
        .then(courses => {
            if (!courses || courses.length === 0) {
                container.innerHTML = `
                    <div style="text-align: center; padding: 60px 20px; color: var(--text-light); background: white; border-radius: 8px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);">
                        <div style="font-size: 48px; margin-bottom: 16px;">📚</div>
                        <div style="font-size: 16px; margin-bottom: 8px; font-weight: 600;">暂无可选课程</div>
                        <div style="font-size: 14px; color: #95a5a6;">请稍后再试或联系管理员</div>
                    </div>
                `;
                // 更新计数
                const countElement = document.getElementById('available-count');
                if (countElement) {
                    countElement.textContent = '0';
                }
                return;
            }

            container.innerHTML = `
                <table class="course-table">
                    <thead>
                        <tr>
                            <th>课程名称</th>
                            <th>课程代码</th>
                            <th>班级</th>
                            <th>学分</th>
                            <th>上课时间</th>
                            <th>上课地点</th>
                            <th>学期</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${courses.map(course => `
                            <tr>
                                <td class="course-name-cell">${course.courseName || '未知'}</td>
                                <td class="course-info-cell">${course.courseCode || 'N/A'}</td>
                                <td class="course-info-cell">${course.className || '未知'}</td>
                                <td class="course-info-cell">${course.credits || '0'}</td>
                                <td class="course-info-cell">${course.classTime || '未设置'}</td>
                                <td class="course-info-cell">${course.classLocation || '未设置'}</td>
                                <td class="course-info-cell">${course.semester || '未知'}</td>
                                <td class="course-actions-cell">
                                    <button class="btn btn-sm btn-accent" onclick="enrollCourse(${course.classId})">选课</button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;

            // 更新计数
            const countElement = document.getElementById('available-count');
            if (countElement) {
                countElement.textContent = courses.length;
            }

            showToast('可选课程加载完成', 'success');
        })
        .catch(error => {
            console.error('加载可选课程失败:', error);
            container.innerHTML = '<div style="text-align: center; padding: 40px; color: var(--danger-color);">加载失败，请稍后重试</div>';
            showToast('加载可选课程失败', 'error');
        });
}

// 加载我的课程
function loadMyCourses() {
    const container = document.getElementById('my-courses-list');
    container.innerHTML = '<div class="loading">正在加载...</div>';

    // 调用后端API获取已选课程
    StudentCourseAPI.getStudentCourses(1) // 假设学生ID为1
        .then(courses => {
            if (!courses || courses.length === 0) {
                container.innerHTML = `
                    <div style="text-align: center; padding: 60px 20px; color: var(--text-light); background: white; border-radius: 8px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);">
                        <div style="font-size: 48px; margin-bottom: 16px;">📖</div>
                        <div style="font-size: 16px; margin-bottom: 8px; font-weight: 600;">暂无已选课程</div>
                        <div style="font-size: 14px; color: #95a5a6;">快去选择你感兴趣的课程吧！</div>
                    </div>
                `;
                // 更新计数
                const countElement = document.getElementById('my-courses-count');
                if (countElement) {
                    countElement.textContent = '0';
                }
                return;
            }

            container.innerHTML = `
                <table class="course-table">
                    <thead>
                        <tr>
                            <th>课程名称</th>
                            <th>课程代码</th>
                            <th>班级</th>
                            <th>学分</th>
                            <th>上课时间</th>
                            <th>上课地点</th>
                            <th>学期</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${courses.map(course => `
                            <tr>
                                <td class="course-name-cell">${course.courseName || '未知'}</td>
                                <td class="course-info-cell">${course.courseCode || 'N/A'}</td>
                                <td class="course-info-cell">${course.className || '未知'}</td>
                                <td class="course-info-cell">${course.credits || '0'}</td>
                                <td class="course-info-cell">${course.classTime || '未设置'}</td>
                                <td class="course-info-cell">${course.classLocation || '未设置'}</td>
                                <td class="course-info-cell">${course.semester || '未知'}</td>
                                <td><span class="status-enrolled">${getStatusText('ENROLLED')}</span></td>
                                <td class="course-actions-cell">
                                    <button class="btn btn-sm btn-danger" onclick="dropCourse(${course.classId})">退课</button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;

            // 更新计数
            const countElement = document.getElementById('my-courses-count');
            if (countElement) {
                countElement.textContent = courses.length;
            }

            showToast('已选课程加载完成', 'success');
        })
        .catch(error => {
            console.error('加载已选课程失败:', error);
            container.innerHTML = '<div style="text-align: center; padding: 40px; color: var(--danger-color);">加载失败，请稍后重试</div>';
            showToast('加载已选课程失败', 'error');
        });
}

// 单个选课
function enrollCourse(classId) {
    if (!confirm('确认要选修这门课程吗？')) {
        return;
    }

    // 调用后端API进行选课
    StudentCourseAPI.enroll(1, classId) // 假设学生ID为1
        .then(result => {
            showToast(`课程 ${classId} 选修成功`, 'success');
            // 刷新数据
            loadAvailableCourses();
            loadMyCourses();
        })
        .catch(error => {
            console.error('选课失败:', error);
            showToast(`课程 ${classId} 选修失败`, 'error');
        });
}

// 退课
function dropCourse(classId) {
    if (!confirm('确认要退修这门课程吗？此操作不可撤销。')) {
        return;
    }

    // 调用后端API进行退课
    StudentCourseAPI.drop(1, classId) // 假设学生ID为1
        .then(result => {
            showToast(`课程 ${classId} 退修成功`, 'success');
            // 刷新数据
            loadAvailableCourses();
            loadMyCourses();
        })
        .catch(error => {
            console.error('退课失败:', error);
            showToast(`课程 ${classId} 退修失败`, 'error');
        });
}

// 查看课程详情
function checkCourseDetails(classId) {
    // 这里可以实现查看课程详细信息的弹窗
    showToast(`查看课程 ${classId} 详情`, 'info');
}

// 获取状态文本
function getStatusText(status) {
    switch(status) {
        case 'ENROLLED': return '已选修';
        case 'DROPPED': return '已退修';
        case 'COMPLETED': return '已完成';
        default: return status;
    }
}

// ========== 考勤统计功能 ==========

// 初始化考勤统计页面
function initStatisticsPage() {
    const generateBtn = document.querySelector('#statistics button.btn-accent');
    const classSelect = document.querySelector('#statistics select');

    // 加载班级选项
    loadClassOptions();

    // 生成统计报告按钮事件
    if (generateBtn) {
        generateBtn.addEventListener('click', generateStatisticsReport);
    }

    function loadClassOptions() {
        CourseClassAPI.getAll().then(classes => {
            if (!classes || classes.length === 0) {
                classSelect.innerHTML = '<option value="">暂无班级数据</option>';
                return;
            }
            classSelect.innerHTML = '<option value="">请选择班级</option>' +
                classes.map(c => `<option value="${c.classId}">${c.className} (ID:${c.classId})</option>`).join('');
        }).catch(err => {
            console.error('加载班级失败:', err);
            classSelect.innerHTML = '<option value="">加载失败</option>';
            showToast('加载班级失败，请检查网络或后端服务', 'error');
        });
    }

    function generateStatisticsReport() {
        const classId = classSelect.value;
        if (!classId) {
            showToast('请选择班级', 'warning');
            return;
        }

        // 显示加载状态
        generateBtn.disabled = true;
        generateBtn.textContent = '生成中...';

        // 调用考勤任务统计API
        fetch(`/api/attendance-tasks/class/${classId}/statistics`)
            .then(response => response.json())
            .then(data => {
                displayStatisticsReport(data, classId);
                showToast('统计报告生成成功', 'success');
            })
            .catch(error => {
                console.error('生成统计报告失败:', error);
                showToast('生成统计报告失败，请稍后重试', 'error');
            })
            .finally(() => {
                generateBtn.disabled = false;
                generateBtn.textContent = '生成统计报告';
            });
    }

    function displayStatisticsReport(statistics, classId) {
        const resultDiv = document.querySelector('#statistics .card-body > div:last-child');
        if (!resultDiv) return;

        const html = `
            <h3>考勤统计结果</h3>
            <div class="statistics-summary" style="margin: 20px 0; padding: 15px; background: #f8f9fa; border-radius: 8px;">
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 15px;">
                    <div class="stat-item">
                        <strong>总任务数:</strong> ${statistics.totalTasks || 0}
                    </div>
                    <div class="stat-item">
                        <strong>活跃任务:</strong> ${statistics.activeTasks || 0}
                    </div>
                    <div class="stat-item">
                        <strong>已过期任务:</strong> ${statistics.expiredTasks || 0}
                    </div>
                    <div class="stat-item">
                        <strong>即将开始:</strong> ${statistics.upcomingTasks || 0}
                    </div>
                </div>
            </div>

            <div class="table-container" style="margin-top: 15px;">
                <table>
                    <thead>
                        <tr>
                            <th>任务ID</th>
                            <th>开始时间</th>
                            <th>结束时间</th>
                            <th>状态</th>
                            <th>位置范围</th>
                        </tr>
                    </thead>
                    <tbody id="statistics-table-body">
                        <!-- 动态填充任务详情 -->
                    </tbody>
                </table>
            </div>
        `;

        resultDiv.innerHTML = html;

        // 加载并显示任务详情
        loadTaskDetails(classId);
    }

    function loadTaskDetails(classId) {
        AttendanceTaskAPI.getByCourseClassId(classId)
            .then(tasks => {
                const tbody = document.getElementById('statistics-table-body');
                if (!tbody) return;

                if (!tasks || tasks.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">暂无考勤任务数据</td></tr>';
                    return;
                }

                tbody.innerHTML = tasks.map(task => {
                    const status = getTaskStatus(task);
                    return `
                        <tr>
                            <td>${task.taskId}</td>
                            <td>${formatDateTime(task.startTime)}</td>
                            <td>${formatDateTime(task.endTime)}</td>
                            <td><span class="status-${status.toLowerCase()}">${status}</span></td>
                            <td>${task.locationRange || '未设置'}</td>
                        </tr>
                    `;
                }).join('');
            })
            .catch(error => {
                console.error('加载任务详情失败:', error);
                const tbody = document.getElementById('statistics-table-body');
                if (tbody) {
                    tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: red;">加载失败</td></tr>';
                }
            });
    }

    function getTaskStatus(task) {
        const now = new Date();
        const startTime = new Date(task.startTime);
        const endTime = new Date(task.endTime);

        if (now < startTime) return 'UPCOMING';
        if (now > endTime) return 'EXPIRED';
        return 'ACTIVE';
    }

    function formatDateTime(dateTimeStr) {
        const date = new Date(dateTimeStr);
        return date.toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    }
}
