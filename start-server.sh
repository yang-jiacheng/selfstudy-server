#!/bin/bash

# ==================== 基础配置 ====================

PORTS=(8071 8072)

MODULES=('selfstudy-admin' 'selfstudy-mobile-app')

MODULE_NAMES=('后台管理系统' 'APP服务端')

JARS=('selfstudy-admin.jar' 'selfstudy-mobile-app.jar')

JAR_PATHS=('/java/selfstudy-admin/' '/java/selfstudy-mobile-app/')

JAVA_OPTIONS=(
'-Xms512m -Xmx512m -XX:+UseG1GC -XX:CICompilerCount=2 -XX:MaxDirectMemorySize=128m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Xss256k -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=2m -XX:+UseStringDeduplication -XX:+ParallelRefProcEnabled -Xlog:gc:/java/logs/gc/gc-selfstudy-admin.log:time:filecount=5,filesize=50M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/java/logs/heap-dumps/selfstudy-admin.hprof'
'-Xms384m -Xmx384m -XX:+UseG1GC -XX:CICompilerCount=2 -XX:MaxDirectMemorySize=96m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=192m -Xss256k -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=2m -XX:+UseStringDeduplication -XX:+ParallelRefProcEnabled -Xlog:gc:/java/logs/gc/gc-selfstudy-mobile-app.log:time:filecount=5,filesize=50M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/java/logs/heap-dumps/selfstudy-mobile-app.hprof'
)

JDK_OPENS="--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED"

## ==================== JVM参数配置 ====================
## 参数说明：
# -Xms: 堆内存初始大小
# -Xmx: 堆内存最大大小
# -XX:+UseG1GC: 使用G1垃圾收集器（适合低延迟场景）
# -XX:CICompilerCount: JIT编译器线程数
# -XX:MaxDirectMemorySize: 直接内存最大大小（限制堆外内存）
# -XX:MetaspaceSize: 元空间初始大小
# -XX:MaxMetaspaceSize: 元空间最大大小（存储类元数据）
# -Xss: 线程栈大小（每个线程的栈空间）
# -XX:MaxGCPauseMillis: GC停顿时间目标（毫秒）
# -XX:G1HeapRegionSize: G1堆区域大小
# -XX:+UseStringDeduplication: 启用字符串去重（节省内存）
# -XX:+ParallelRefProcEnabled: 启用并行引用处理（提高GC效率）
# -Xlog:gc: GC日志配置（记录GC详细信息）
# -XX:+HeapDumpOnOutOfMemoryError: 发生OOM时自动dump堆内存
# -XX:HeapDumpPath: 堆dump文件保存路径

## 示例：
# -Xms384m -Xmx384m -XX:+UseG1GC -XX:CICompilerCount=2 -XX:MaxDirectMemorySize=96m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=192m -Xss256k -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=2m
# -Xms512m -Xmx512m -XX:+UseG1GC -XX:CICompilerCount=2 -XX:MaxDirectMemorySize=128m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Xss256k -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=2m
# -Xms1g -Xmx1g -XX:+UseG1GC -XX:CICompilerCount=2 -XX:MaxDirectMemorySize=256m -XX:MetaspaceSize=160m -XX:MaxMetaspaceSize=320m -Xss384k -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=4m
# -Xms2g -Xmx2g -XX:+UseG1GC -XX:CICompilerCount=2 -XX:MaxDirectMemorySize=512m -XX:MetaspaceSize=192m -XX:MaxMetaspaceSize=384m -Xss384k -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=8m
# -Xms3g -Xmx3g -XX:+UseG1GC -XX:CICompilerCount=2 -XX:MaxDirectMemorySize=768m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -Xss512k -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=8m
# -Xms4g -Xmx4g -XX:+UseG1GC -XX:CICompilerCount=2 -XX:MaxDirectMemorySize=768m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -Xss512k -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=16m


# ==================== 启动函数 ====================

start() {

    local command="$1"
    local commandOk=0
    local okCount=0

    for ((i=0;i<${#MODULES[@]};i++)); do

        MODULE=${MODULES[$i]}
        MODULE_NAME=${MODULE_NAMES[$i]}
        JAR_NAME=${JARS[$i]}
        JAR_PATH=${JAR_PATHS[$i]}
        JAVA_OPTION=${JAVA_OPTIONS[$i]}
        PORT=${PORTS[$i]}

        if [ "$command" = "all" ] || [ "$command" = "$MODULE" ]; then

            commandOk=1
            PID=$(pgrep -f "$JAR_NAME")

            if [ -n "$PID" ]; then
                echo "$MODULE---$MODULE_NAME 已运行, PID=$PID"
            else
                echo "正在启动 $MODULE---$MODULE_NAME (端口:$PORT) ..."

                nohup java $JAVA_OPTION $JDK_OPENS -jar ${JAR_PATH}${JAR_NAME} --server.port=$PORT --spring.config.additional-location=${JAR_PATH}application-prod.yaml --spring.profiles.active=prod > ${JAR_PATH}nohup.out 2>&1 &

                count=0
                sleep 2

                while true; do
                    PID=$(pgrep -f "$JAR_NAME")
                    if [ -n "$PID" ]; then
                        echo "$MODULE---$MODULE_NAME 启动成功, PID=$PID"
                        echo "日志文件: ${JAR_PATH}console.log"
                        okCount=$((okCount+1))
                        break
                    fi

                    if [ $count -ge 30 ]; then
                        echo "$MODULE---$MODULE_NAME 30秒内未启动成功，请检查日志"
                        break
                    fi

                    sleep 1
                    count=$((count+1))
                done
            fi
        fi
    done

    if [ $commandOk -eq 0 ]; then
        echo "第二个参数输入错误"
    else
        echo "本次共启动: $okCount 个服务"
    fi
}

# ==================== 停止函数 ====================

stop() {

    local command="$1"
    local commandOk=0
    local okCount=0

    for ((i=0;i<${#MODULES[@]};i++)); do

        MODULE=${MODULES[$i]}
        MODULE_NAME=${MODULE_NAMES[$i]}
        JAR_NAME=${JARS[$i]}

        if [ "$command" = "all" ] || [ "$command" = "$MODULE" ]; then

            commandOk=1
            PID=$(pgrep -f "$JAR_NAME")

            if [ -n "$PID" ]; then

                echo "$MODULE---$MODULE_NAME 正在优雅关闭, PID=$PID"
                kill -15 $PID

                WAIT_TIME=0

                while [ -n "$PID" ] && [ $WAIT_TIME -lt 40 ]; do
                    sleep 1
                    PID=$(pgrep -f "$JAR_NAME")
                    WAIT_TIME=$((WAIT_TIME+1))
                done

                if [ -n "$PID" ]; then
                    echo "$MODULE---$MODULE_NAME 超时未结束，强制终止 PID=$PID"
                    kill -9 $PID
                else
                    echo "$MODULE---$MODULE_NAME 已成功关闭"
                fi

                okCount=$((okCount+1))

            else
                echo "$MODULE---$MODULE_NAME 未运行"
            fi
        fi
    done

    if [ $commandOk -eq 0 ]; then
        echo "第二个参数输入错误"
    else
        echo "本次共停止: $okCount 个服务"
    fi
}

# ==================== 主入口 ====================

case "$1" in
    start)
        start "$2"
        ;;
    stop)
        stop "$2"
        ;;
    restart)
        stop "$2"
        sleep 3
        start "$2"
        ;;
    *)
        echo "用法: ./start.sh start|stop|restart 模块名|all"
        exit 1
        ;;
esac
