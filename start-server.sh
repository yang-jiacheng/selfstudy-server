#! /bin/sh
# 端口号
PORTS=(8071 8072)
# 模块
MODULES=('selfstudy-admin' 'selfstudy-mobile-app')
# 模块名称
MODULE_NAMES=(后台管理系统 APP服务端)
# jar包数组
JARS=('selfstudy-admin.jar' 'selfstudy-mobile-app.jar')
# jar包路径数组
JAR_PATHS=('/java/selfstudy-admin/' '/java/selfstudy-mobile-app/')
# jvm参数
JAVA_OPTIONS=('-Xms650m -Xmx650m' '-Xms256m -Xmx512m')

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
                nohup java -jar $JAVA_OPTION $JAR_PATH$JAR_NAME --spring.profiles.active=prod > /dev/null 2>&1 &
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
            PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
            if [ -n "$PID" ]; then
                echo "$MODULE---$MODULE_NAME:准备结束,PID=$PID"
                kill -9 $PID
                PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
                while [ -n "$PID" ]; do
                    sleep 3s
                    PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
                done
                echo "$MODULE---$MODULE_NAME:成功结束"
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

