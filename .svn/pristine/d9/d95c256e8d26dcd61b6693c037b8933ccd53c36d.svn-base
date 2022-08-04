/**
 * SPFunction
 * 函数库
 * @package SPFunction
 * @author SunP<sunp@aowsp.com>
 * @copyright Copyright © Since 2012 SunP All Rights Reserved.
 **/
layui.define(['view', 'table', 'layer', 'tablePlug', 'form'], function (exports) {
    let $ = layui.$,
        setter = layui.setter,
        table = layui.table,
        form = layui.form,
        tablePlug = layui.tablePlug,
        view = layui.view,
        layer = layui.layer,
        SavingFunction = {
            v: '1.0 dms',
            //Ajax请求
            req: function (options) {
                let success = options.success
                    , error = options.error
                    , request = setter.request
                    , response = setter.response
                    , debug = function () {
                    return setter.debug ? '<br><cite>URL：</cite>' + options.url : '';
                };
                //记录done参数
                let done = options.done;
                //清理对象done参数
                delete options.done;
                options.data = options.data || {};
                if (request.tokenName) {
                    let sendData = typeof options.data === 'string' ? JSON.parse(options.data) : options.data;
                    //自动给参数传入默认 token
                    options.data[request.tokenName] = request.tokenName in sendData
                        ? options.data[request.tokenName]
                        : (layui.data(setter.tableName)[request.tokenName] || '');
                    //自动给 Request Headers 传入 token
                    options.headers[request.tokenName] = request.tokenName in options.headers
                        ? options.headers[request.tokenName]
                        : (layui.data(setter.tableName)[request.tokenName] || '');
                }
                delete options.success;
                delete options.error;
                return $.ajax($.extend({
                    type: 'post'
                    , dataType: 'json'
                    , success: function (res) {
                        let statusCode = response.statusCode;
                        // 只有 response 的 code符合定义的标准 一切正常才执行 done
                        if (res[response.statusName] === statusCode.ok) {
                            typeof done === 'function' && done(res.data);
                        }
                        //登录状态失效，清除本地 access_token，并强制跳转到登入页
                        else if (res[response.statusName] === statusCode.logout) {
                            view.exit();
                        } else if (res[response.statusName] === statusCode.err ||
                            res[response.statusName] === statusCode.errParam) {
                            typeof error === 'function' ? error(res.msg) : SavingFunction.errorMsg(res.msg);
                        }
                        // 其它异常
                        else {
                            let errorMsgs = [
                                '<cite>Error：</cite> ' + (res[response.msgName] || '返回状态码异常')
                                , debug()
                            ].join('');
                            SavingFunction.errorMsg(errorMsgs);
                        }
                        // 只要 http 状态码正常，无论 response 的 code 是否正常都执行 success
                        typeof success === 'function' && success(res);
                    }
                    , error: function (e, code) {
                        if (error) {
                            error(e, code);
                            return;
                        }
                        let errorMsgs = [
                            '请求异常，请重试<br><cite>错误信息：</cite>' + code
                            , debug()
                        ].join('');
                        SavingFunction.errorMsg(errorMsgs);
                    }
                }, options));
            },
            errorMsg: function (content, options) {
                return top.layer.msg(content, {offset: '300px', icon: 2, time: 1500}, options);
            },
            warnMsg: function (content, options) {
                return top.layer.msg(content, {offset: '300px', icon: 3, time: 1500}, options);
            },
            successMsg: function (content, options) {
                return top.layer.msg(content, {offset: '300px', icon: 1, time: 1500}, options);
            },
            loading: function () {
                return top.layer.msg('加载中,请稍等', {
                    icon: 16
                    , shade: 0.01
                    , time: 10 * 1000
                });
            },
            intoTable: function (option) {

                option.loading = option.loading !== false;
                option.method = option.method || 'post';
                option.even = option.even !== false;
                if (option.page !== false) {
                    option.page = option.page !== false;
                }
                tablePlug.smartReload.enable(true);
                option.limit = option.limit || 100;
                option.limits = [10, 50, 100, 200, 500, 2000];
                option.toolbar = option.toolbar !== false ? option.toolbar : false;
                option.defaultToolbar = option.defaultToolbar || ['filter', 'print'];
                option.elem = '#' + option.id;
                option.colFilterRecord = option.colFilterRecord !== false;
                option.request = option.request || {pageName: 'pageNum', limitName: 'pageSize'};
                option.parseData = option.parseData || function (res) {
                    return {
                        "code": res.status,
                        "msg": res.msg,
                        "count": res.data !== undefined ? res.data.total : 0,
                        "data": res.data.records
                    };
                };
                let savTableOption = $.extend({
                    smartReloadModel: true
                    , text: {
                        none: '暂无相关数据(或者更换过滤条件后重试)'
                    }
                }, option);
                let tables = table.render(savTableOption);
                table.on('sort(' + option.id + ')', function (obj) {
                    table.reload(option.id, {
                        initSort: obj
                        , where: {
                            field: obj.field
                            , order: obj.type
                        }
                    });
                });
                return tables;
            },
            getCookie: function (name) {
                let arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
                if (arr = document.cookie.match(reg)) {
                    return unescape(arr[2]);
                } else {
                    return null;
                }
            },
            getListToMaps: (event, idName) => {
                let maps = new Map();
                if (!idName) {
                    console.error('idName is not defined');
                }
                if (event instanceof Array) {
                    event.forEach(obj => maps.set(obj[idName], obj))
                }
                return maps;
            },
            /**
             * 设置cookie
             * @param {Object} name： cookie名称
             * @param {Object} value: cookie值 ,每个值之间用“,”隔开,如：“123,456,123” 三个值
             * @param {Object} time: s20是代表20秒 h是指小时，如12小时则是：h12 d是天数，30天则：d30
             */
            setCookie: function (name, value, time) {
                let strsec = '';
                let str1 = time.substring(1, time.length) * 1;
                let str2 = time.substring(0, 1);
                if (str2 == "s") {
                    strsec = str1 * 1000;
                } else if (str2 == "h") {
                    strsec = str1 * 60 * 60 * 1000;
                } else if (str2 == "d") {
                    strsec = str1 * 24 * 60 * 60 * 1000;
                }
                let exp = new Date();
                exp.setTime(exp.getTime() + strsec * 1);
                document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
            },
            delCookie: function (name) {
                let exp = new Date();
                exp.setTime(exp.getTime() - 1);
                let cval = SavingFunction.getCookie(name);
                if (cval != null) {
                    document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
                }
            },
            renderSize: function (filesize) {
                if (null == filesize || filesize == '') {
                    return "0 Bytes";
                }
                let unitArr = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
                let index = 0;
                let srcsize = parseFloat(filesize);
                index = Math.floor(Math.log(srcsize) / Math.log(1024));
                let size = srcsize / Math.pow(1024, index);
                size = size.toFixed(2);//保留的小数位数
                return size + unitArr[index];
            },
            filterParams: function (obj) {
                let _newPar = {};
                for (let key in obj) {
                    if ((obj[key] === 0 || obj[key] === false || obj[key]) && obj[key].toString().replace(/(^\s*)|(\s*$)/g, '') !== '') {
                        if (obj[key] !== []) {
                            _newPar[key] = obj[key];
                        }
                    }
                }
                return _newPar;
            },
            //数组转为treelist
            toTree: function (data, y, isXmSelect) {
                let val = [];
                if (data.constructor === Object) {
                    data = Object.keys(data).map(function (e) {
                        return data [e]
                    });
                }
                if (Object.keys(data).length > 0) {
                    data.forEach(function (item) {
                        delete item.children;
                    });
                    let map = {};
                    data.forEach(function (item) {
                        if (!item.value && isXmSelect) {
                            item.value = item.id;
                        }
                        map[item[y]] = item;
                    });
                    data.forEach(function (item) {
                        // 以当前遍历项，的pid,去map对象中找到索引的id
                        let parent = map[item.parent];
                        // 好绕啊，如果找到索引，那么说明此项不在顶级当中,那么需要把此项添加到，他对应的父级中
                        if (parent && item.parent !== 0) {

                            (parent.children || (parent.children = [])).push(item);
                        } else {
                            //如果没有在map中找到对应的索引ID,那么直接把 当前的item添加到 val结果集中，作为顶级
                            val.push(item);
                        }
                    });
                    val = JSON.parse(JSON.stringify(val).replace(/sortId/g, "id").replace(/typeName/g, "name"));
                }
                return val;
            },
            /**模拟表单提交下载**/
            exportExcel: function (url, data) {
                let exportForm = document.createElement("form");
                exportForm.action = url;
                exportForm.method = "post";
                exportForm.style.display = "none";
                try {
                    if (data) {
                        Object.keys(data).forEach(function (key) {
                            let hideInput = document.createElement("input");
                            hideInput.type = "hidden";
                            hideInput.name = key;
                            hideInput.value = typeof (data[key]) === 'string' ? data[key] : JSON.stringify(data[key]);
                            exportForm.appendChild(hideInput);
                        });
                    }
                    $(document.body).append(exportForm);
                    exportForm.submit();
                } catch (e) {
                    SavingFunction.errorMsg(e.message);
                } finally {
                    exportForm.remove();
                }
            },
            textToImg: function (str) {
                let name, fsize;
                if (str.length < 2) {
                    name = str;
                    fsize = 60
                } else {
                    if (str.length === 2) {
                        name = str.substring(0, str.length);
                        fsize = 45
                    } else {
                        if (str.length === 3) {
                            name = str.substring(0, str.length);
                            fsize = 30
                        } else {
                            if (str.length === 4) {
                                name = str.substring(0, str.length);
                                fsize = 25
                            } else {
                                if (str.length > 4) {
                                    name = str.substring(0, 2);
                                    fsize = 45
                                }
                            }
                        }
                    }
                }
                let fontSize = 80, fontWeight = "bold", canvas = document.createElement("canvas"),
                    img1 = document.createElement("img");
                canvas.style.display = "none";
                img1.style.display = "none";
                canvas.width = 160;
                canvas.height = 160;
                let context = canvas.getContext("2d");
                context.fillStyle = this.getBG();
                context.fillRect(0, 0, canvas.width, canvas.height);
                context.fillStyle = "#FFF";
                context.font = fontWeight + " " + fsize + "px sans-serif";
                context.textAlign = "center";
                context.textBaseline = "middle";
                context.fillText(name, fontSize, fontSize);
                return canvas.toDataURL("image/png")
            },
            getBG: function () {
                let bgArray = ["#1abc9c", "#2ecc71", "#3498db", "#9b59b6", "#34495e",
                    "#16a085", "#27ae60", "#2980b9", "#8e44ad", "#2c3e50", "#f1c40f",
                    "#e67e22", "#e74c3c", "#eca0f1", "#95a5a6", "#f39c12", "#d35400",
                    "#c0392b", "#bdc3c7", "#7f8c8d"];
                let color = bgArray[Math.floor(Math.random() * bgArray.length)];
                return color
            }
        }
    ;
    form.verify({
        nickname: function (value, item) { //value：表单的值、item：表单的DOM对象
            if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                return '用户名不能有特殊字符';
            }
            if (/(^\_)|(\__)|(\_+$)/.test(value)) {
                return '用户名首尾不能出现下划线\'_\'';
            }
            // if (/^\d+\d+\d$/.test(value)) {
            //     return '用户名不能全为数字';
            // }
        }
        , pass: [
            /^[\S]{6,12}$/
            , '密码必须6到12位，且不能出现空格'
        ]
    });

    exports('SavingFunction', SavingFunction);
});
