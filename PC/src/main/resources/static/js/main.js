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
                            <button class="btn btn-accent">添加课程</button>
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
                                    <tbody>
                                        <tr>
                                            <td>C001</td>
                                            <td>软件工程</td>
                                            <td>张老师</td>
                                            <td>3</td>
                                            <td>
                                                <div class="btn-group">
                                                    <button class="btn">编辑</button>
                                                    <button class="btn btn-danger">删除</button>
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
                                                    <button class="btn">编辑</button>
                                                    <button class="btn btn-danger">删除</button>
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
                            <button class="btn btn-accent">添加教师</button>
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
                                    <tbody>
                                        <tr>
                                            <td>T001</td>
                                            <td>张老师</td>
                                            <td>教授</td>
                                            <td>计算机学院</td>
                                            <td>
                                                <div class="btn-group">
                                                    <button class="btn">编辑</button>
                                                    <button class="btn btn-danger">删除</button>
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
                                                    <button class="btn">编辑</button>
                                                    <button class="btn btn-danger">删除</button>
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
                            <button class="btn btn-accent">添加学生</button>
                        </div>
                        <div class="card-body">
                            <div class="table-container">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>学号</th>
                                            <th>姓名</th>
                                            <th>班级</th>
                                            <th>专业</th>
                                            <th>操作</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>S2023001</td>
                                            <td>张三</td>
                                            <td>计算机2001</td>
                                            <td>计算机科学与技术</td>
                                            <td>
                                                <div class="btn-group">
                                                    <button class="btn">编辑</button>
                                                    <button class="btn btn-danger">删除</button>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>S2023002</td>
                                            <td>李四</td>
                                            <td>计算机2001</td>
                                            <td>计算机科学与技术</td>
                                            <td>
                                                <div class="btn-group">
                                                    <button class="btn">编辑</button>
                                                    <button class="btn btn-danger">删除</button>
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