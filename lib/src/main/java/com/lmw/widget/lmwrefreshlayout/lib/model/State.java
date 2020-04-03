package com.lmw.widget.lmwrefreshlayout.lib.model;

public enum State {
    IDLE,//空闲状态
    RELEASE_TO_REFRESH,//释放刷新
    REFRESHING,//正在刷新
    RELEASE_TO_LOAD,//释放加载
    LOADING,//正在加载
    PREPARE_TO_REFRESH,//准备刷新
    PREPARE_TO_LOAD,//准备状态
    NEXT_FLOOR,//下一层
    PRE_FLOOR,//上一层
    RELEASE_NEXT_FLOOR,//释放进入下一层
    RELEASE_PRE_FLOOR,//释放进入上一层
}
