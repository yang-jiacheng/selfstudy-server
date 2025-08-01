#! /bin/sh
# 端口号
PORTS=(8071 8072)
# 模块
MODULES=('selfstudy-admin' 'selfstudy-mobile-app')
# 模块名称
MODULE_NAMES=('后台管理系统' 'APP服务端')
# jar包数组
JARS=('selfstudy-admin.jar' 'selfstudy-mobile-app.jar')
# jar包路径数组
JAR_PATHS=('/java/selfstudy-admin/' '/java/selfstudy-mobile-app/')
# jvm参数 (JDK 17兼容)
JAVA_OPTIONS=(
'-Xms512m -Xmx512m -XX:+UseG1GC -XX:CICompilerCount=2 -XX:MaxDirectMemorySize=128m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Xss256k -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=2m -XX:+UseStringDeduplication -XX:+ParallelRefProcEnabled -Xlog:gc:/java/logs/gc/gc-selfstudy-admin.log:time:filecount=5,filesize=50M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/java/logs/heap-dumps/selfstudy-admin.hprof'
'-Xms384m -Xmx384m -XX:+UseG1GC -XX:CICompilerCount=2 -XX:MaxDirectMemorySize=96m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=192m -Xss256k -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=2m -XX:+UseStringDeduplication -XX:+ParallelRefProcEnabled -Xlog:gc:/java/logs/gc/gc-selfstudy-mobile-app.log:time:filecount=5,filesize=50M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/java/logs/heap-dumps/selfstudy-mobile-app.hprof'
)


# ==================== JVM参数配置 ====================
# 参数说明：
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

start() {
    local MODULE=
    local MODULE_NAME=
    local JAR_NAME=
    local JAR_PATH=
    local JAVA_OPTION=
    local command="$1"
    local commandOk=0
    local count=0
    local okCount=0
    local port=0
    for((i=0;i<${#MODULES[@]};i++)); do
        MODULE=${MODULES[$i]}
        MODULE_NAME=${MODULE_NAMES[$i]}
        JAR_NAME=${JARS[$i]}
        PORT=${PORTS[$i]}
        JAR_PATH=${JAR_PATHS[$i]}
        JAVA_OPTION=${JAVA_OPTIONS[$i]}

        if [ "$command" == "all" ] || [ "$command" == "$MODULE" ]; then
            commandOk=1
            count=0
            PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
            if [ -n "$PID" ]; then
                echo "$MODULE---$MODULE_NAME:已经运行,PID=$PID"
            else
                nohup java $JAVA_OPTION -jar $JAR_PATH$JAR_NAME --spring.profiles.active=prod > /dev/null 2>&1 &
                PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
                while [ -z "$PID" ]; do
                    if (($count == 30)); then
                        echo "$MODULE---$MODULE_NAME:$(expr $count \* 10)秒内未启动,请检查!"
                        break
                    fi
                    count=$(($count+1))
                    echo "$MODULE_NAME启动中.................."
                    sleep 10s
                    PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
                done
                okCount=$(($okCount+1))
                echo "$MODULE---$MODULE_NAME:已经启动成功,PID=$PID"
            fi
        fi
    done
    if(($commandOk == 0)); then
        echo "第二个参数输入错误"
    else
        echo "............本次共启动:$okCount个服务..........."
    fi
}

stop() {
    local MODULE=
    local MODULE_NAME=
    local JAR_NAME=
    local command="$1"
    local commandOk=0
    local okCount=0
    for((i=0;i<${#MODULES[@]};i++)); do
        MODULE=${MODULES[$i]}
        MODULE_NAME=${MODULE_NAMES[$i]}
        JAR_NAME=${JARS[$i]}

        if [ "$command" = "all" ] || [ "$command" = "$MODULE" ]; then
            commandOk=1
            # 获取 PID
            PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
            if [ -n "$PID" ]; then
                echo "$MODULE---$MODULE_NAME:准备结束,PID=$PID"
                # 发送 SIGTERM（kill -15）请求优雅关闭
                kill -15 $PID
                # 等待进程退出，最多等待 40 秒
                WAIT_TIME=0
                while [ -n "$PID" ] && [ $WAIT_TIME -lt 40 ]; do
                    sleep 1s
                    PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
                    WAIT_TIME=$((WAIT_TIME+1))
                done

                if [ -n "$PID" ]; then
                    # 如果超过40秒进程还没有退出，则强制杀死
                    echo "$MODULE---$MODULE_NAME:超时未结束，强制终止, PID=$PID"
                    kill -9 $PID
                else
                    echo "$MODULE---$MODULE_NAME:成功结束"
                fi
                okCount=$(($okCount+1))
            else
                echo "$MODULE---$MODULE_NAME:未运行"
            fi
        fi
    done
    if (($commandOk == 0)); then
        echo "第二个参数输入错误"
    else
        echo "............本次共停止:$okCount个服务............"
    fi
}


case "$1" in
    start)
        start "$2"
        ;;
    stop)
        stop "$2"
        ;;
    restart)
        stop "$2"
        sleep 3s
        start "$2"
        ;;
    *)
        echo "第一个参数请输入:start|stop|restart"
        exit 1
        ;;
esac

