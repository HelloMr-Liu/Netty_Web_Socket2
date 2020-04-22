var app = new Vue({
    el: "#app",
    data: {
        // 顶部左边的数据
        topLeft: [
            {
                icnoClass: "layui-icon layui-icon-shrink-right tl-icno",
                title: "隐藏菜单栏",
                clickName: "0",
                id: "uNav"
            }
        ],
        // 顶部右边的数据
        topRight: [
            {
                icnoClass: "layui-icon layui-icon-logout tl-icno",
                title: "退出系统",
                clickName: '1'
            }

        ],
        // 系统的名称
        systemName: "上海中乙宏信企业管理有限公司",
        // 树形导航栏数据
        leftNavs: [
            {
            parentName: "控制台",
            icnoClass: "layui-icon layui-icon-home nav-icon-class",
            childData: [],
            href: "/Users/sam/luqiqi/codes/hrms/hrms_html/ConsoleManage/index.html"
            },
            {
                parentName: "简历管理",
                icnoClass: "layui-icon layui-icon-form nav-icon-class",
                childData: [],
                href: "/Users/sam/luqiqi/codes/hrms/hrms_html/ResumeManage/index.html"
            },
            {
                parentName: "职位管理",
                icnoClass: "layui-icon layui-icon-dialogue nav-icon-class",
                token: "zwgl",
                childData: [{
                    childName: "职位管理",
                    href: "/Users/sam/luqiqi/codes/hrms/hrms_html/PositionManage/PositionManage/index.html"
                },
                    {
                        childName: "指标管理",
                        href: "/Users/sam/luqiqi/codes/hrms/hrms_html/PositionManage/IndicatorManage/index.html"
                    }
                ]
            },
            {
                parentName: "求职管理",
                icnoClass: "layui-icon layui-icon-group nav-icon-class",
                childData: [],
                href: "/Users/sam/luqiqi/codes/hrms/hrms_html/JobManage/index.html"
            }, {
                parentName: "数据报表",
                icnoClass: "layui-icon layui-icon-chart-screen nav-icon-class",
                childData: [{
                    childName: "门店业绩报表",
                    href: ""
                },
                    {
                        childName: "个人业绩报表",
                        href: ""
                    },
                    {
                        childName: "每月在职报表",
                        href: ""
                    }
                ]
            },
            {
                parentName: "系统管理",
                icnoClass: "layui-icon layui-icon-set nav-icon-class",
                childData: [{
                    childName: "用户管理",
                    href: "/Users/sam/luqiqi/codes/hrms/hrms_html/SystemManage/UserManage/index.html"
                },
                    {
                        childName: "公司管理",
                        href: "/Users/sam/luqiqi/codes/hrms/hrms_html/SystemManage/CompanyManage/index.html"
                    },
                    {
                        childName: "门店管理",
                        href: "/Users/sam/luqiqi/codes/hrms/hrms_html/SystemManage/StoreManage/index.html"
                    }
                ]
            },
            {
                parentName: "系统超级管理员",
                icnoClass: "layui-icon layui-icon-ios nav-icon-class",
                childData: [],
                href: "/Users/sam/luqiqi/codes/hrms/hrms_html/AdminManage/index.html"
            }
        ],
        // 当前做菜单栏是否显示
        leftNavFlag: true,
        fullscreenLoading: false,
        loading: true,
        // 当前leftNav的token
        currentLeftNavToken: "zwgl",
        currentClientToken: "zwgl"
    },
    methods: {
        tlClick: function (index, id) {
            if (index == 0) {
                this.updateNav(index, id);
            } else if (index == 1) {
                window.open("http://www.baidu.com");
            } else if (index == 2) {
                location.reload();
            }
        },
        // 退出系统
        logout: function () {
            layer.confirm('是否退出当前账户？', {
                btn: ['确定', '取消'] //按钮
            }, function (index) {
                layer.close(index);
                //app.fullscreenLoading = true;
                app.$message({
                    message: '恭喜你，这是一条成功消息',
                    type: 'success'
                });
            });
        },
        // 修改密码
        newEditPass: function () {
            // 计算出坐标
            var content = $(".body_content").width();
            var left = (content - (content * 0.4)) / 2;
            if (this.leftNavFlag) {
                left += 240;
            }
            var top = ($("html").height() - 300) / 2;

            layer.open({
                type: 2,
                area: [(content * 0.4) + 'px', '400px'],
                fix: false, //不固定
                maxmin: true,
                offset: [top, left],
                shadeClose: true,
                shade: 0.4,
                title: "修改密码",
                content: "./EditPass.html",
                success: function () {
                    $(':focus').blur();
                },
                cancel: function () {
                    //关闭窗口之后刷新frame
                    // location.replace(location.href);
                },
                end: function () {
                    //窗口销毁之后刷新frame
                }
            });
        },
        // 修改菜单栏
        updateNav: function (index, id) {
            if (this.leftNavFlag) {
                // 显示的话需要将他进行隐藏
                this.leftNavFlag = false;
                // 切换icon
                this.topLeft[index].icnoClass = "layui-icon tl-icno layui-icon-spread-left";
                this.topLeft[index].title = "显示菜单栏";
                $(".layui-body").animate({
                    left: '0px',
                    "padding-left": '0px'
                }, 200);
                $(".layui-layout-left").animate({
                    left: '0px'
                }, 200);

            } else {
                this.leftNavFlag = true
                this.topLeft[index].icnoClass = "layui-icon layui-icon-shrink-right tl-icno";
                this.topLeft[index].title = "隐藏菜单栏";
                $(".layui-body").animate({
                    left: '200px',
                    "padding-left": '20px'
                }, 200);
                $(".layui-layout-left").animate({
                    left: '200px'
                }, 200);
            }
        }
    }
});


//JavaScript代码区域
layui.use('layer', function () {
    var layer = layui.layer;

});

layui.use('element', function(){
    var element = layui.element;

});

$(function () {
    setTimeout(function () {
        app.loading = false;
    }, 500)
});
layui.use('tree', function () {
    let tree = layui.tree;

    //基本演示
    tree.render({
        elem: '#test12'
        , data: [{
            title: '早餐'
            , id: 1
            , children: [{
                title: '油条'
                , id: 5
            }, {
                title: '包子'
                , id: 6
            }, {
                title: '豆浆'
                , id: 7
            }]
        }, {
            title: '午餐'
            , id: 2
            , checked: true
            , children: [{
                title: '藜蒿炒腊肉'
                , id: 8
            }, {
                title: '西湖醋鱼'
                , id: 9
            }, {
                title: '小白菜'
                , id: 10
            }, {
                title: '海带排骨汤'
                , id: 11
            }]
        }, {
            title: '晚餐'
            , id: 3
            , children: [{
                title: '红烧肉'
                , id: 12
                , fixed: true
            }, {
                title: '番茄炒蛋'
                , id: 13
            }]
        }, {
            title: '夜宵'
            , id: 4
            , children: [{
                title: '小龙虾'
                , id: 14
                , checked: true
            }, {
                title: '香辣蟹'
                , id: 15
                , disabled: true
            }, {
                title: '烤鱿鱼'
                , id: 16
            }]
        }]
        , showCheckbox: true  //是否显示复选框
        , id: 'demoId1'
        , isJump: true //是否允许点击节点时弹出新窗口跳转
        , click: function (obj) {
            var data = obj.data;  //获取当前点击的节点数据
            layer.msg('状态：' + obj.state + '<br>节点数据：' + JSON.stringify(data));
        }
    });

});


