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
                            <button class="btn btn-accent" onclick="addCourse()">➕ 添加课程</button>
                            <button class="btn" onclick="location.reload()">🔄 刷新</button>
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
                                                    <button class="btn" onclick="editCourse('C001', '软件工程', '张老师', 3)">✏️ 编辑</button>
                                                    <button class="btn btn-danger" onclick="deleteCourse('C001', '软件工程')">🗑️ 删除</button>
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
                                                    <button class="btn" onclick="editCourse('C002', '数据结构', '李老师', 4)">✏️ 编辑</button>
                                                    <button class="btn btn-danger" onclick="deleteCourse('C002', '数据结构')">🗑️ 删除</button>
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
                            <button class="btn btn-accent" onclick="addTeacher()">➕ 添加教师</button>
                            <button class="btn" onclick="location.reload()">🔄 刷新</button>
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
                                                    <button class="btn" onclick="editTeacher('T001', '张老师', '教授', '计算机学院')">✏️ 编辑</button>
                                                    <button class="btn btn-danger" onclick="deleteTeacher('T001', '张老师')">🗑️ 删除</button>
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
                                                    <button class="btn" onclick="editTeacher('T002', '李老师', '副教授', '计算机学院')">✏️ 编辑</button>
                                                    <button class="btn btn-danger" onclick="deleteTeacher('T002', '李老师')">🗑️ 删除</button>
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
                            <button class="btn btn-accent" onclick="addStudent()">➕ 添加学生</button>
                            <button class="btn" onclick="loadStudents()">🔄 刷新</button>
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
                                        <input type="datetime-local" id="task-start-time" name="startTime" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="task-end-time">结束时间 <span class="required">*</span></label>
                                        <input type="datetime-local" id="task-end-time" name="endTime" required>
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
                                            <button type="button" class="btn btn-secondary" id="btn-search-location" style="width: 100%; margin-bottom: 5px;">🔍 搜索</button>
                                        </div>
                                        <div class="form-group" style="flex: 1;">
                                            <button type="button" class="btn btn-secondary" id="btn-get-location" style="width: 100%; margin-bottom: 5px;">📍 获取当前位置</button>
                                        </div>
                                    </div>
                                    <!-- 地图容器 -->
                                    <div id="map-container" style="height: 300px; width: 100%; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; display: block;"></div>
                                    
                                    <div class="form-row">
                                        <div class="form-group" style="display: none;">
                                            <label for="latitude">纬度</label>
                                            <input type="number" id="latitude" name="latitude" step="0.0000001" required>
                                        </div>
                                        <div class="form-group" style="display: none;">
                                            <label for="longitude">经度</label>
                                            <input type="number" id="longitude" name="longitude" step="0.0000001" required>
                                        </div>
                                        <div class="form-group">
                                            <label for="radius" style="font-size: 0.9em;">有效半径(米)</label>
                                            <input type="number" id="radius" name="radius" value="100" required>
                                        </div>
                                    </div>
                                </div>

                                <!-- 移除强制人脸识别选项，默认为1 -->
                                <input type="hidden" id="is-face-required" name="isFaceRequired" value="1">

                                <div class="form-group">
                                    <label for="task-desc">任务描述</label>
                                    <textarea id="task-desc" name="description" rows="3"></textarea>
                                </div>
                                <button type="submit" class="btn btn-accent" style="margin-top: 10px;">发布考勤</button>
                            </form>
                        </div>
                    </div>
                `;
                setTimeout(initPublishTaskPage, 100);
                break;
            case 'attendance':
                content = `
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">人脸识别考勤</div>
                        </div>
                        <div class="card-body">
                            <div class="face-recognition-area">
                                <div class="camera-preview">
                                    <div class="camera-icon">📷</div>
                                </div>
                                <button class="btn btn-accent" style="width: 200px;">开始识别</button>
                                <div class="recognition-result">
                                    <p>请点击"开始识别"按钮进行人脸识别考勤</p>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
                break;
            case 'statistics':
                content = `
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">考勤统计</div>
                        </div>
                        <div class="card-body">
                            <div class="form-group">
                                <label>选择课程</label>
                                <select>
                                    <option>软件工程</option>
                                    <option>数据结构</option>
                                    <option>数据库原理</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>选择时间范围</label>
                                <div class="form-row">
                                    <div class="form-group">
                                        <input type="date" value="2023-10-01">
                                    </div>
                                    <div class="form-group">
                                        <input type="date" value="2023-10-31">
                                    </div>
                                </div>
                            </div>
                            <button class="btn btn-accent">生成统计报告</button>
                            
                            <div style="margin-top: 30px;">
                                <h3>考勤统计结果</h3>
                                <div class="table-container" style="margin-top: 15px;">
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>学生姓名</th>
                                                <th>学号</th>
                                                <th>出勤次数</th>
                                                <th>缺勤次数</th>
                                                <th>出勤率</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>张三</td>
                                                <td>S2023001</td>
                                                <td>15</td>
                                                <td>1</td>
                                                <td>93.8%</td>
                                            </tr>
                                            <tr>
                                                <td>李四</td>
                                                <td>S2023002</td>
                                                <td>14</td>
                                                <td>2</td>
                                                <td>87.5%</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
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
                        <button class="btn" onclick="editStudent(${student.studentId})">✏️ 编辑</button>
                        <button class="btn btn-danger" onclick="deleteStudent(${student.studentId})">🗑️ 删除</button>
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
    Modal.confirm({
        title: '⚠️ 确认删除',
        message: '确定要删除这个学生吗？此操作不可撤销。',
        submitText: '删除',
        danger: true,
        onConfirm: () => {
            StudentAPI.delete(id)
                .then(() => {
                    showToast('学生删除成功！', 'success');
                    loadStudents();
                })
                .catch(error => {
                    console.error('删除失败:', error);
                    showToast('删除失败，请重试', 'error');
                });
        }
    });
}

// ========== 角色管理功能 ==========

// 加载所有角色
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
                        <button class="btn" onclick="editCourse('${formData.courseCode}', '${formData.courseName}', '${formData.teacher}', ${formData.credits})">✏️ 编辑</button>
                        <button class="btn btn-danger" onclick="deleteCourse('${formData.courseCode}', '${formData.courseName}')">🗑️ 删除</button>
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
                                <button class="btn" onclick="editTeacher('${teacherNumber}', '${formData.teacherName}', '${formData.title}', '${formData.department}')">✏️ 编辑</button>
                                <button class="btn btn-danger" onclick="deleteTeacher('${teacherNumber}', '${formData.teacherName}')">🗑️ 删除</button>
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
    const btnGetLocation = document.getElementById('btn-get-location');
    const btnSearchLocation = document.getElementById('btn-search-location');
    
    // 设置默认时间
    const now = new Date();
    const offset = now.getTimezoneOffset() * 60000;
    const start = new Date(now.getTime() - offset).toISOString().slice(0, 16);
    const end = new Date(now.getTime() + 2 * 60 * 60 * 1000 - offset).toISOString().slice(0, 16);
    
    const startTimeInput = document.getElementById('task-start-time');
    const endTimeInput = document.getElementById('task-end-time');
    
    if (startTimeInput) startTimeInput.value = start;
    if (endTimeInput) endTimeInput.value = end;

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
        
        if (typeof L === 'undefined') {
            mapContainer.innerHTML = '<div style="padding: 20px; text-align: center; color: #666;">地图加载失败，请检查网络连接</div>';
            return;
        }

        // 如果已经初始化过地图，先移除
        if (window.currentMap) {
            window.currentMap.remove();
        }
        
        // 默认位置：如果没有提供坐标，则默认为北京
        const defaultLat = lat || 39.9042;
        const defaultLng = lng || 116.4074;
        const zoomLevel = lat ? 16 : 12;

        const map = L.map('map-container').setView([defaultLat, defaultLng], zoomLevel);
        window.currentMap = map;
        
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        }).addTo(map);
        
        let marker;
        if (lat && lng) {
            marker = L.marker([lat, lng]).addTo(map);
        }

        // 地图点击事件
        map.on('click', async function(e) {
            const clickedLat = e.latlng.lat;
            const clickedLng = e.latlng.lng;
            
            // 更新隐藏的经纬度输入框
            document.getElementById('latitude').value = clickedLat.toFixed(7);
            document.getElementById('longitude').value = clickedLng.toFixed(7);
            
            // 更新或创建标记
            if (marker) {
                marker.setLatLng(e.latlng);
            } else {
                marker = L.marker(e.latlng).addTo(map);
            }
            
            // 逆地理编码获取地址
            try {
                showToast('正在获取地址信息...', 'info', 1000);
                const response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${clickedLat}&lon=${clickedLng}&zoom=18&addressdetails=1`);
                if (response.ok) {
                    const data = await response.json();
                    if (data && data.display_name) {
                        const locationRangeInput = document.getElementById('location-range');
                        if (locationRangeInput) {
                            // 简化地址显示
                            let address = '';
                            if (data.address) {
                                // 优先显示更有意义的名称
                                const parts = [];
                                if (data.address.amenity) parts.push(data.address.amenity); // 设施名
                                else if (data.address.building) parts.push(data.address.building); // 建筑名
                                
                                if (data.address.road) parts.push(data.address.road); // 道路
                                if (data.address.house_number) parts.push(data.address.house_number); // 门牌
                                
                                if (parts.length > 0) {
                                    address = parts.join(' ');
                                } else {
                                    address = data.display_name.split(',')[0]; // 回退到显示名称的第一部分
                                }
                            } else {
                                address = data.display_name;
                            }
                            locationRangeInput.value = address;
                            marker.bindPopup(address).openPopup();
                        }
                    }
                }
            } catch (err) {
                console.warn('逆地理编码失败:', err);
            }
        });
        
        return map;
    }

    // 页面加载完成后初始化地图（尝试获取位置，如果失败则显示默认地图）
    setTimeout(() => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const lat = position.coords.latitude;
                    const lng = position.coords.longitude;
                    document.getElementById('latitude').value = lat.toFixed(7);
                    document.getElementById('longitude').value = lng.toFixed(7);
                    initMap(lat, lng);
                },
                (error) => {
                    console.log('无法自动获取位置，加载默认地图');
                    initMap(); 
                },
                { timeout: 5000 }
            );
        } else {
            initMap();
        }
    }, 500);

    // 搜索地点功能
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
                const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}&limit=1`);
                if (response.ok) {
                    const data = await response.json();
                    if (data && data.length > 0) {
                        const result = data[0];
                        const lat = parseFloat(result.lat);
                        const lng = parseFloat(result.lon);
                        
                        document.getElementById('latitude').value = lat.toFixed(7);
                        document.getElementById('longitude').value = lng.toFixed(7);
                        
                        // 初始化或更新地图
                        const map = initMap(lat, lng);
                        if (map && window.currentMap) {
                            window.currentMap.setView([lat, lng], 16);
                            
                            // 查找并更新标记
                            let markerFound = false;
                            map.eachLayer((layer) => {
                                if (layer instanceof L.Marker) {
                                    layer.setLatLng([lat, lng]);
                                    layer.bindPopup(result.display_name).openPopup();
                                    markerFound = true;
                                }
                            });
                            
                            // 如果没有找到标记（理论上 initMap 会创建，但为了保险），这里可以补一个
                            if (!markerFound) {
                                const marker = L.marker([lat, lng], {draggable: true}).addTo(map);
                                marker.bindPopup(result.display_name).openPopup();
                                
                                // 绑定拖拽事件
                                marker.on('dragend', function(e) {
                                    const position = marker.getLatLng();
                                    document.getElementById('latitude').value = position.lat.toFixed(7);
                                    document.getElementById('longitude').value = position.lng.toFixed(7);
                                });
                            }
                        }
                        
                        showToast('已定位到搜索地点', 'success');
                    } else {
                        showToast('未找到相关地点，请尝试更详细的描述', 'warning');
                    }
                } else {
                    showToast('搜索服务暂不可用', 'error');
                }
            } catch (e) {
                console.error('搜索失败:', e);
                showToast('搜索发生错误，请检查网络', 'error');
            } finally {
                btnSearchLocation.textContent = '🔍 搜索';
                btnSearchLocation.disabled = false;
            }
        });
    }

    // 获取位置按钮逻辑（定位到当前位置）
    if (btnGetLocation) {
        btnGetLocation.addEventListener('click', () => {
            if (!navigator.geolocation) {
                showToast('您的浏览器不支持地理位置功能', 'error');
                return;
            }
            
            btnGetLocation.textContent = '正在获取...';
            btnGetLocation.disabled = true;
            
            navigator.geolocation.getCurrentPosition(
                async (position) => {
                    const lat = position.coords.latitude;
                    const lng = position.coords.longitude;
                    
                    document.getElementById('latitude').value = lat.toFixed(7);
                    document.getElementById('longitude').value = lng.toFixed(7);
                    
                    // 重新初始化地图并定位
                    initMap(lat, lng);
                    
                    // 自动获取地址
                    try {
                        const response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}&zoom=18&addressdetails=1`);
                        if (response.ok) {
                            const data = await response.json();
                            if (data && data.display_name) {
                                const locationRangeInput = document.getElementById('location-range');
                                if (locationRangeInput) {
                                    locationRangeInput.value = data.display_name.split(',')[0];
                                }
                            }
                        }
                    } catch(e) {}

                    btnGetLocation.textContent = '✅ 定位成功';
                    btnGetLocation.classList.remove('btn-secondary');
                    btnGetLocation.classList.add('btn-success');
                    setTimeout(() => {
                        btnGetLocation.textContent = '📍 获取当前位置';
                        btnGetLocation.disabled = false;
                        btnGetLocation.classList.remove('btn-success');
                        btnGetLocation.classList.add('btn-secondary');
                    }, 2000);
                },
                (error) => {
                    showToast('获取位置失败', 'error');
                    btnGetLocation.textContent = '📍 获取当前位置';
                    btnGetLocation.disabled = false;
                }
            );
        });
    }

    // 处理提交
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const formData = new FormData(form);
        const isFaceRequiredInput = document.getElementById('is-face-required');
        const isFaceRequired = isFaceRequiredInput ? parseInt(isFaceRequiredInput.value) : 1;

        const task = {
            courseClassId: parseInt(formData.get('courseClassId')),
            taskName: formData.get('taskName'),
            startTime: formData.get('startTime'),
            endTime: formData.get('endTime'),
            description: formData.get('description'),
            locationRange: formData.get('locationRange'),
            latitude: parseFloat(formData.get('latitude')),
            longitude: parseFloat(formData.get('longitude')),
            radius: parseInt(formData.get('radius')),
            isFaceRequired: isFaceRequired,
            teacherId: 1 // 暂时硬编码教师ID
        };
        
        try {
            await AttendanceTaskAPI.create(task);
            showToast('考勤发布成功！', 'success');
            form.reset();
            // 重置时间
            if (startTimeInput) startTimeInput.value = start;
            if (endTimeInput) endTimeInput.value = end;
            // 重置按钮状态
            if (btnGetLocation) {
                btnGetLocation.textContent = '📍 获取当前位置';
                btnGetLocation.classList.remove('btn-success');
                btnGetLocation.classList.add('btn-secondary');
            }
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
