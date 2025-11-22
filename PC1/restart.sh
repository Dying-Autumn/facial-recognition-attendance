#!/bin/bash

# 重启Spring Boot项目脚本

echo "=========================================="
echo "正在重启项目..."
echo "=========================================="

# 获取脚本所在目录
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# 1. 停止当前运行的项目
echo ""
echo "步骤1: 停止当前运行的项目"
echo "----------------------------------------"
./stop.sh

# 等待端口完全释放
sleep 2

# 2. 启动项目
echo ""
echo "步骤2: 启动项目"
echo "----------------------------------------"
echo "正在使用 Maven 启动项目..."
echo ""

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到 Maven，请先安装 Maven"
    exit 1
fi

# 启动项目
mvn spring-boot:run

