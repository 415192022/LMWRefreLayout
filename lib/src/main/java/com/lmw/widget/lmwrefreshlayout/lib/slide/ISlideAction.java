package com.lmw.widget.lmwrefreshlayout.lib.slide;

public interface ISlideAction {

    /**
     * 打开菜单
     */
    void openMenu();

    /**
     * 关闭菜单
     */
    void closeMenu();


    /**
     * 打开状态
     * @return
     */
    Boolean isOpened();
}
