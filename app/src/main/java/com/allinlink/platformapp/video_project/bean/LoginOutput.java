package com.allinlink.platformapp.video_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author ltd
 * @description: 登录返回实体
 * @date 2020-09-21
 **/
public class LoginOutput  implements Serializable {
    /**
     * gid : 2020103017142300000
     * userName : admin
     * password : 670B14728AD9902AECBA32E22FA4F6BD
     * realName : 系统管理员
     * depName : 2020102613335700001
     * zydx : null
     * roleList : [{"gid":"2020103017142300002","passportGid":"2020103017142300000","roleGid":"2020102819215200000"}]
     * menuList : [{"gid":"2020102311070400000","menuName":"系统配置","menuIcon":"system.png","menuUrl":"/System","orderSn":"0","menuModule":"系统配置","parentMenu":"0","isShow":1,"menuDescription":"系统配置一级菜单","children":null},{"gid":"2020102610373200000","menuName":"数据管理","menuIcon":"DataManage.png","menuUrl":"/System/Data","orderSn":"0","menuModule":null,"parentMenu":"2020102311070400000","isShow":1,"menuDescription":"数据管理二级菜单","children":null},{"gid":"2020102610405100001","menuName":"机构管理","menuIcon":"TenantManage.png","menuUrl":"/System/Data/Organization","orderSn":"1","menuModule":null,"parentMenu":"2020102610373200000","isShow":1,"menuDescription":"机构管理三级菜单","children":null},{"gid":"2020102610412400002","menuName":"用户管理","menuIcon":"UserManage.png","menuUrl":"/System/Data/User","orderSn":"2","menuModule":null,"parentMenu":"2020102610373200000","isShow":1,"menuDescription":"用户管理三级菜单","children":null},{"gid":"2020102610415100003","menuName":"角色管理","menuIcon":"RoleManage.png","menuUrl":"/System/Data/Role","orderSn":"3","menuModule":null,"parentMenu":"2020102610373200000","isShow":1,"menuDescription":"角色管理三级菜单","children":null},{"gid":"2020102610424900004","menuName":"菜单管理","menuIcon":"MenuManage.png","menuUrl":"/System/Data/Menu","orderSn":"4","menuModule":null,"parentMenu":"2020102610373200000","isShow":1,"menuDescription":"菜单管理三级菜单","children":null},{"gid":"2020102610580500005","menuName":"参数设置","menuIcon":"ParamentSetting.png","menuUrl":"/System/Parameter","orderSn":"1","menuModule":null,"parentMenu":"2020102311070400000","isShow":1,"menuDescription":"参数设置二级菜单","children":null},{"gid":"2020102611101200006","menuName":"系统参数设置","menuIcon":"ParamManage.png","menuUrl":"/System/Parameter/Parameter","orderSn":"1","menuModule":null,"parentMenu":"2020102610580500005","isShow":1,"menuDescription":"参数设置三级菜单","children":null},{"gid":"2020102611112500007","menuName":"字典管理","menuIcon":"ParamManage.png","menuUrl":"/System/Parameter/Enum","orderSn":"2","menuModule":null,"parentMenu":"2020102610580500005","isShow":1,"menuDescription":"字典管理三级菜单","children":null},{"gid":"2020102611120100008","menuName":"分类体系","menuIcon":"Fltx.png","menuUrl":"/System/Parameter/Classify","orderSn":"3","menuModule":null,"parentMenu":"2020102610580500005","isShow":1,"menuDescription":"分类体系三级菜单","children":null},{"gid":"2020102713481200002","menuName":"视频运维","menuIcon":"video.png","menuUrl":"/Video","orderSn":"","menuModule":null,"parentMenu":"0","isShow":1,"menuDescription":"","children":null},{"gid":"2020102713491300000","menuName":"网络设备管理","menuIcon":"SystemSetting.png","menuUrl":"/Video/Network","orderSn":"","menuModule":null,"parentMenu":"2020102713481200002","isShow":1,"menuDescription":"","children":null},{"gid":"2020102910023200003","menuName":"摄像机接入","menuIcon":"camera.png","menuUrl":"/Video/Network/Camera","orderSn":"","menuModule":null,"parentMenu":"2020102713491300000","isShow":1,"menuDescription":"","children":null},{"gid":"2020102910030000004","menuName":"服务器接入","menuIcon":"server.png","menuUrl":"/Video/Network/Server","orderSn":"","menuModule":null,"parentMenu":"2020102713491300000","isShow":1,"menuDescription":"","children":null},{"gid":"2020102910041800005","menuName":"视频管理","menuIcon":"videoManager.png","menuUrl":"/Video/Video","orderSn":"","menuModule":null,"parentMenu":"2020102713481200002","isShow":1,"menuDescription":"","children":null},{"gid":"2020102910055600007","menuName":"录像计划","menuIcon":"videoPlan.png","menuUrl":"/Video/Video/Plan","orderSn":"","menuModule":null,"parentMenu":"2020102910041800005","isShow":1,"menuDescription":"","children":null},{"gid":"2020102910062500008","menuName":"视频点播","menuIcon":"videoPlay.png","menuUrl":"/Video/Video/Play","orderSn":"","menuModule":null,"parentMenu":"2020102910041800005","isShow":1,"menuDescription":"","children":null},{"gid":"2020102910071200009","menuName":"录像回放","menuIcon":"videoReplay.png","menuUrl":"/Video/Video/Replay","orderSn":"","menuModule":null,"parentMenu":"2020102910041800005","isShow":1,"menuDescription":"","children":null}]
     */

    private String gid;
    private String userName;
    private String password;
    private String realName;
    private String depName;
    private Object zydx;
    private String email;
    private String telephone;
    private List<RoleListBean> roleList;
    private List<MenuListBean> menuList;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public Object getZydx() {
        return zydx;
    }

    public void setZydx(Object zydx) {
        this.zydx = zydx;
    }

    public List<RoleListBean> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleListBean> roleList) {
        this.roleList = roleList;
    }

    public List<MenuListBean> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuListBean> menuList) {
        this.menuList = menuList;
    }

    public static class RoleListBean  implements Serializable{
        /**
         * gid : 2020103017142300002
         * passportGid : 2020103017142300000
         * roleGid : 2020102819215200000
         */

        private String gid;
        private String passportGid;
        private String roleGid;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getPassportGid() {
            return passportGid;
        }

        public void setPassportGid(String passportGid) {
            this.passportGid = passportGid;
        }

        public String getRoleGid() {
            return roleGid;
        }

        public void setRoleGid(String roleGid) {
            this.roleGid = roleGid;
        }
    }

    public static class MenuListBean  implements Serializable {
        /**
         * gid : 2020102311070400000
         * menuName : 系统配置
         * menuIcon : system.png
         * menuUrl : /System
         * orderSn : 0
         * menuModule : 系统配置
         * parentMenu : 0
         * isShow : 1
         * menuDescription : 系统配置一级菜单
         * children : null
         */

        private String gid;
        private String menuName;
        private String menuIcon;
        private String menuUrl;
        private String orderSn;
        private String menuModule;
        private String parentMenu;
        private int isShow;
        private String menuDescription;
        private Object children;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public String getMenuIcon() {
            return menuIcon;
        }

        public void setMenuIcon(String menuIcon) {
            this.menuIcon = menuIcon;
        }

        public String getMenuUrl() {
            return menuUrl;
        }

        public void setMenuUrl(String menuUrl) {
            this.menuUrl = menuUrl;
        }

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public String getMenuModule() {
            return menuModule;
        }

        public void setMenuModule(String menuModule) {
            this.menuModule = menuModule;
        }

        public String getParentMenu() {
            return parentMenu;
        }

        public void setParentMenu(String parentMenu) {
            this.parentMenu = parentMenu;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }

        public String getMenuDescription() {
            return menuDescription;
        }

        public void setMenuDescription(String menuDescription) {
            this.menuDescription = menuDescription;
        }

        public Object getChildren() {
            return children;
        }

        public void setChildren(Object children) {
            this.children = children;
        }
    }


    /**


     {
     "suf": true,
     "code": 200,
     "result": {
     "gid": "2020103017142300000",
     "userName": "admin",
     "password": "670B14728AD9902AECBA32E22FA4F6BD",
     "realName": "系统管理员",
     "depName": "2020102613335700001",
     "zydx": null,
     "roleList": [
     {
     "gid": "2020103017142300002",
     "passportGid": "2020103017142300000",
     "roleGid": "2020102819215200000"
     }
     ],
     "menuList": [
     {
     "gid": "2020102311070400000",
     "menuName": "系统配置",
     "menuIcon": "system.png",
     "menuUrl": "/System",
     "orderSn": "0",
     "menuModule": "系统配置",
     "parentMenu": "0",
     "isShow": 1,
     "menuDescription": "系统配置一级菜单",
     "children": null
     },
     {
     "gid": "2020102610373200000",
     "menuName": "数据管理",
     "menuIcon": "DataManage.png",
     "menuUrl": "/System/Data",
     "orderSn": "0",
     "menuModule": null,
     "parentMenu": "2020102311070400000",
     "isShow": 1,
     "menuDescription": "数据管理二级菜单",
     "children": null
     },
     {
     "gid": "2020102610405100001",
     "menuName": "机构管理",
     "menuIcon": "TenantManage.png",
     "menuUrl": "/System/Data/Organization",
     "orderSn": "1",
     "menuModule": null,
     "parentMenu": "2020102610373200000",
     "isShow": 1,
     "menuDescription": "机构管理三级菜单",
     "children": null
     },
     {
     "gid": "2020102610412400002",
     "menuName": "用户管理",
     "menuIcon": "UserManage.png",
     "menuUrl": "/System/Data/User",
     "orderSn": "2",
     "menuModule": null,
     "parentMenu": "2020102610373200000",
     "isShow": 1,
     "menuDescription": "用户管理三级菜单",
     "children": null
     },
     {
     "gid": "2020102610415100003",
     "menuName": "角色管理",
     "menuIcon": "RoleManage.png",
     "menuUrl": "/System/Data/Role",
     "orderSn": "3",
     "menuModule": null,
     "parentMenu": "2020102610373200000",
     "isShow": 1,
     "menuDescription": "角色管理三级菜单",
     "children": null
     },
     {
     "gid": "2020102610424900004",
     "menuName": "菜单管理",
     "menuIcon": "MenuManage.png",
     "menuUrl": "/System/Data/Menu",
     "orderSn": "4",
     "menuModule": null,
     "parentMenu": "2020102610373200000",
     "isShow": 1,
     "menuDescription": "菜单管理三级菜单",
     "children": null
     },
     {
     "gid": "2020102610580500005",
     "menuName": "参数设置",
     "menuIcon": "ParamentSetting.png",
     "menuUrl": "/System/Parameter",
     "orderSn": "1",
     "menuModule": null,
     "parentMenu": "2020102311070400000",
     "isShow": 1,
     "menuDescription": "参数设置二级菜单",
     "children": null
     },
     {
     "gid": "2020102611101200006",
     "menuName": "系统参数设置",
     "menuIcon": "ParamManage.png",
     "menuUrl": "/System/Parameter/Parameter",
     "orderSn": "1",
     "menuModule": null,
     "parentMenu": "2020102610580500005",
     "isShow": 1,
     "menuDescription": "参数设置三级菜单",
     "children": null
     },{
     "gid": "2020102611112500007",
     "menuName": "字典管理",
     "menuIcon": "ParamManage.png",
     "menuUrl": "/System/Parameter/Enum",
     "orderSn": "2",
     "menuModule": null,
     "parentMenu": "2020102610580500005",
     "isShow": 1,
     "menuDescription": "字典管理三级菜单",
     "children": null
     },
     {
     "gid": "2020102611120100008",
     "menuName": "分类体系",
     "menuIcon": "Fltx.png",
     "menuUrl": "/System/Parameter/Classify",
     "orderSn": "3",
     "menuModule": null,
     "parentMenu": "2020102610580500005",
     "isShow": 1,
     "menuDescription": "分类体系三级菜单",
     "children": null
     },
     {
     "gid": "2020102713481200002",
     "menuName": "视频运维",
     "menuIcon": "video.png",
     "menuUrl": "/Video",
     "orderSn": "",
     "menuModule": null,
     "parentMenu": "0",
     "isShow": 1,
     "menuDescription": "",
     "children": null
     },
     {
     "gid": "2020102713491300000",
     "menuName": "网络设备管理",
     "menuIcon": "SystemSetting.png",
     "menuUrl": "/Video/Network",
     "orderSn": "",
     "menuModule": null,
     "parentMenu": "2020102713481200002",
     "isShow": 1,
     "menuDescription": "",
     "children": null
     },
     {
     "gid": "2020102910023200003",
     "menuName": "摄像机接入",
     "menuIcon": "camera.png",
     "menuUrl": "/Video/Network/Camera",
     "orderSn": "",
     "menuModule": null,
     "parentMenu": "2020102713491300000",
     "isShow": 1,
     "menuDescription": "",
     "children": null
     },
     {
     "gid": "2020102910030000004",
     "menuName": "服务器接入",
     "menuIcon": "server.png",
     "menuUrl": "/Video/Network/Server",
     "orderSn": "",
     "menuModule": null,
     "parentMenu": "2020102713491300000",
     "isShow": 1,
     "menuDescription": "",
     "children": null
     },
     {
     "gid": "2020102910041800005",
     "menuName": "视频管理",
     "menuIcon": "videoManager.png",
     "menuUrl": "/Video/Video",
     "orderSn": "",
     "menuModule": null,
     "parentMenu": "2020102713481200002",
     "isShow": 1,
     "menuDescription": "",
     "children": null
     },
     {
     "gid": "2020102910055600007",
     "menuName": "录像计划",
     "menuIcon": "videoPlan.png",
     "menuUrl": "/Video/Video/Plan",
     "orderSn": "",
     "menuModule": null,
     "parentMenu": "2020102910041800005",
     "isShow": 1,
     "menuDescription": "",
     "children": null
     },
     {
     "gid": "2020102910062500008",
     "menuName": "视频点播",
     "menuIcon": "videoPlay.png",
     "menuUrl": "/Video/Video/Play",
     "orderSn": "",
     "menuModule": null,
     "parentMenu": "2020102910041800005",
     "isShow": 1,
     "menuDescription": "",
     "children": null
     },
     {
     "gid": "2020102910071200009",
     "menuName": "录像回放",
     "menuIcon": "videoReplay.png",
     "menuUrl": "/Video/Video/Replay",
     "orderSn": "",
     "menuModule": null,
     "parentMenu": "2020102910041800005",
     "isShow": 1,
     "menuDescription": "",
     "children": null
     }
     ]
     },
     "token": "2020111711060700000",
     "message": "查询成功"
     }

     */


}
