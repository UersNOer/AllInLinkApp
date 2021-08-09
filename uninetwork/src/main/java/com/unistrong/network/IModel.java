package com.unistrong.network;

public interface IModel {


    /**
     * code>0时，返回true
     *
     * @return
     */
    boolean isError();

    /**
     * status是ssuccess
     *
     * @return
     */
    boolean isSuccess();

    /**
     * 后台返回的错误信息
     *
     * @return
     */
    String getErrorMsg();
}
