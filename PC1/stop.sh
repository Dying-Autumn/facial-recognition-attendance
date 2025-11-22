#!/bin/bash

# 停止占用8080端口的进程
echo "正在查找占用8080端口的进程..."

# 查找占用8080端口的进程ID
PID=$(lsof -ti:8080)

if [ -z "$PID" ]; then
    echo "8080端口未被占用，无需停止"
    exit 0
fi

echo "找到占用8080端口的进程: $PID"
echo "正在停止进程..."

# 停止进程
kill -9 $PID

# 等待一下确保进程已停止
sleep 2

# 再次检查
if lsof -ti:8080 > /dev/null 2>&1; then
    echo "警告: 进程可能仍在运行，请手动检查"
    exit 1
else
    echo "✓ 8080端口已释放"
    exit 0
fi

