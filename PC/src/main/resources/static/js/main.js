// 页面切换逻辑
document.addEventListener('DOMContentLoaded', function () {
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
                            <button class="btn" onclick="alert('课程管理功能已激活！\\n\\n您可以：\\n1. 点击【添加课程】按钮添加新课程\\n2. 点击【编辑】按钮修改课程\\n3. 点击【删除】按钮删除课程')">ℹ️ 功能说明</button>
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
                            <button class="btn" onclick="alert('教师管理功能已激活！\\n\\n您可以：\\n1. 点击【添加教师】按钮添加新教师\\n2. 点击【编辑】按钮修改教师信息\\n3. 点击【删除】按钮删除教师')">ℹ️ 功能说明</button>
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
                            <button class="btn btn-accent" onclick="addStudent()">添加学生</button>
                            <button class="btn" onclick="loadStudents()">刷新</button>
                        </div>
                        <div class="card-body">
                            <div class="table-container">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>学生ID</th>
                                            <th>学号</th>
                                            <th>班级</th>
                                            <th>用户ID</th>
                                            <th>操作</th>
                                        </tr>
                                    </thead>
                                    <tbody id="student-table-body">
                                        <tr>
                                            <td colspan="5" style="text-align: center;">加载中...</td>
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
    
    tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">加载中...</td></tr>';
    
    try {
        const students = await StudentAPI.getAll();
        
        if (students.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">暂无学生数据</td></tr>';
            return;
        }
        
        tbody.innerHTML = students.map(student => `
            <tr>
                <td>${student.studentId}</td>
                <td>${student.studentNumber}</td>
                <td>${student.className}</td>
                <td>${student.userId}</td>
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
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: red;">加载失败，请检查后端服务</td></tr>';
    }
}

// 添加学生
function addStudent() {
    const studentNumber = prompt('请输入学号:');
    if (!studentNumber) return;
    
    const className = prompt('请输入班级:');
    if (!className) return;
    
    const userId = prompt('请输入用户ID:');
    if (!userId) return;
    
    const student = {
        studentNumber: studentNumber,
        className: className,
        userId: parseInt(userId)
    };
    
    StudentAPI.create(student)
        .then(() => {
            alert('添加成功！');
            loadStudents();
        })
        .catch(error => {
            console.error('添加失败:', error);
            alert('添加失败，学号可能已存在');
        });
}

// 编辑学生
function editStudent(id) {
    StudentAPI.getById(id)
        .then(student => {
            const className = prompt('请输入新的班级:', student.className);
            if (className === null) return;
            
            const updatedStudent = {
                studentNumber: student.studentNumber,
                className: className,
                userId: student.userId
            };
            
            return StudentAPI.update(id, updatedStudent);
        })
        .then(() => {
            alert('更新成功！');
            loadStudents();
        })
        .catch(error => {
            console.error('更新失败:', error);
            alert('更新失败');
        });
}

// 删除学生
function deleteStudent(id) {
    if (!confirm('确定要删除这个学生吗？')) {
        return;
    }
    
    StudentAPI.delete(id)
        .then(() => {
            alert('删除成功！');
            loadStudents();
        })
        .catch(error => {
            console.error('删除失败:', error);
            alert('删除失败');
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
    const courseCode = prompt('请输入课程编号（例如：C003）:');
    if (!courseCode) return;
    
    const courseName = prompt('请输入课程名称:');
    if (!courseName) return;
    
    const teacher = prompt('请输入授课教师:');
    if (!teacher) return;
    
    const credits = prompt('请输入学分:');
    if (!credits) return;
    
    // 添加到表格
    const tbody = document.getElementById('course-table-body');
    if (!tbody) {
        alert('请先打开课程管理页面！');
        return;
    }
    
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td>${courseCode}</td>
        <td>${courseName}</td>
        <td>${teacher}</td>
        <td>${credits}</td>
        <td>
            <div class="btn-group">
                <button class="btn" onclick="editCourse('${courseCode}', '${courseName}', '${teacher}', ${credits})">✏️ 编辑</button>
                <button class="btn btn-danger" onclick="deleteCourse('${courseCode}', '${courseName}')">🗑️ 删除</button>
            </div>
        </td>
    `;
    tbody.appendChild(newRow);
    
    alert(`✅ 课程添加成功！\n\n课程编号：${courseCode}\n课程名称：${courseName}\n授课教师：${teacher}\n学分：${credits}`);
}

// 编辑课程
function editCourse(courseCode, courseName, teacher, credits) {
    const newCourseName = prompt('请输入新的课程名称:', courseName);
    if (newCourseName === null) return;
    
    const newTeacher = prompt('请输入新的授课教师:', teacher);
    if (newTeacher === null) return;
    
    const newCredits = prompt('请输入新的学分:', credits);
    if (newCredits === null) return;
    
    // 查找并更新对应的行
    const tbody = document.getElementById('course-table-body');
    const rows = tbody.getElementsByTagName('tr');
    
    for (let row of rows) {
        const cells = row.getElementsByTagName('td');
        if (cells[0].textContent === courseCode) {
            cells[1].textContent = newCourseName;
            cells[2].textContent = newTeacher;
            cells[3].textContent = newCredits;
            // 更新按钮的参数
            const editBtn = cells[4].querySelector('.btn');
            editBtn.onclick = function() { editCourse(courseCode, newCourseName, newTeacher, newCredits); };
            break;
        }
    }
    
    alert(`✅ 课程更新成功！\n\n课程编号：${courseCode}\n新课程名称：${newCourseName}\n新授课教师：${newTeacher}\n新学分：${newCredits}`);
}

// 删除课程
function deleteCourse(courseCode, courseName) {
    if (!confirm(`确定要删除课程吗？\n\n课程编号：${courseCode}\n课程名称：${courseName}`)) {
        return;
    }
    
    const tbody = document.getElementById('course-table-body');
    const rows = tbody.getElementsByTagName('tr');
    
    for (let i = 0; i < rows.length; i++) {
        const cells = rows[i].getElementsByTagName('td');
        if (cells[0].textContent === courseCode) {
            tbody.removeChild(rows[i]);
            alert(`✅ 课程删除成功！\n\n已删除课程：${courseName}（${courseCode}）`);
            return;
        }
    }
}

// ========== 教师管理功能 ==========

// 添加教师
function addTeacher() {
    const teacherId = prompt('请输入教师工号（例如：T003）:');
    if (!teacherId) return;
    
    const teacherName = prompt('请输入教师姓名:');
    if (!teacherName) return;
    
    const title = prompt('请输入职称（例如：讲师/副教授/教授）:');
    if (!title) return;
    
    const department = prompt('请输入所属院系:');
    if (!department) return;
    
    const tbody = document.getElementById('teacher-table-body');
    if (!tbody) {
        alert('请先打开教师管理页面！');
        return;
    }
    
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td>${teacherId}</td>
        <td>${teacherName}</td>
        <td>${title}</td>
        <td>${department}</td>
        <td>
            <div class="btn-group">
                <button class="btn" onclick="editTeacher('${teacherId}', '${teacherName}', '${title}', '${department}')">✏️ 编辑</button>
                <button class="btn btn-danger" onclick="deleteTeacher('${teacherId}', '${teacherName}')">🗑️ 删除</button>
            </div>
        </td>
    `;
    tbody.appendChild(newRow);
    
    alert(`✅ 教师添加成功！\n\n工号：${teacherId}\n姓名：${teacherName}\n职称：${title}\n院系：${department}`);
}

// 编辑教师
function editTeacher(teacherId, teacherName, title, department) {
    const newName = prompt('请输入新的教师姓名:', teacherName);
    if (newName === null) return;
    
    const newTitle = prompt('请输入新的职称:', title);
    if (newTitle === null) return;
    
    const newDepartment = prompt('请输入新的所属院系:', department);
    if (newDepartment === null) return;
    
    const tbody = document.getElementById('teacher-table-body');
    const rows = tbody.getElementsByTagName('tr');
    
    for (let row of rows) {
        const cells = row.getElementsByTagName('td');
        if (cells[0].textContent === teacherId) {
            cells[1].textContent = newName;
            cells[2].textContent = newTitle;
            cells[3].textContent = newDepartment;
            break;
        }
    }
    
    alert(`✅ 教师信息更新成功！\n\n工号：${teacherId}\n新姓名：${newName}\n新职称：${newTitle}\n新院系：${newDepartment}`);
}

// 删除教师
function deleteTeacher(teacherId, teacherName) {
    if (!confirm(`确定要删除教师吗？\n\n工号：${teacherId}\n姓名：${teacherName}`)) {
        return;
    }
    
    const tbody = document.getElementById('teacher-table-body');
    const rows = tbody.getElementsByTagName('tr');
    
    for (let i = 0; i < rows.length; i++) {
        const cells = rows[i].getElementsByTagName('td');
        if (cells[0].textContent === teacherId) {
            tbody.removeChild(rows[i]);
            alert(`✅ 教师删除成功！\n\n已删除教师：${teacherName}（${teacherId}）`);
            return;
        }
    }
}