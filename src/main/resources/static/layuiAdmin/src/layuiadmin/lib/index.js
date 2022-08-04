layui.extend({
    setter: 'config' //配置模块
    , admin: 'lib/admin' //核心模块
    , view: 'lib/view' //视图渲染模块
    , tablePlug: 'lib/extend/tablePlug/tablePlug'//table插件
}).define(['setter', 'admin', 'tablePlug'], function (exports) {
    var setter = layui.setter
        , element = layui.element
        , admin = layui.admin
        , tabsPage = admin.tabsPage
        , view = layui.view

        , openTabsPage = function (url, text) {
        var matchTo
            , tabs = $('#LAY_app_tabsheader>li')
            , path = url.replace(/(^http(s*):)|(\?[\s\S]*$)/g, '');

        tabs.each(function (index) {
            var li = $(this)
                , layid = li.attr('lay-id');

            if (layid === url) {
                matchTo = true;
                tabsPage.index = index;
            }
        });

        text = text || '新标签页';

        if (setter.pageTabs) {
            if (!matchTo) {
                $(APP_BODY).append([
                    '<div class="layadmin-tabsbody-item layui-show">'
                    , '<iframe src="' + url + '" frameborder="0" class="layadmin-iframe"></iframe>'
                    , '</div>'
                ].join(''));
                tabsPage.index = tabs.length;
                element.tabAdd(FILTER_TAB_TBAS, {
                    title: '<span>' + text + '</span>'
                    , id: url
                    , attr: path
                });
            }
        } else {
            var iframe = admin.tabsBody(admin.tabsPage.index).find('.layadmin-iframe');
            iframe[0].contentWindow.location.href = url;
        }

        element.tabChange(FILTER_TAB_TBAS, url);
        admin.tabsBodyChange(tabsPage.index, {
            url: url
            , text: text
        });

    }

        , APP_BODY = '#LAY_app_body', FILTER_TAB_TBAS = 'layadmin-layout-tabs'
        , $ = layui.$, $win = $(window);

    layui.link(setter.base + "layui/font/layui-extend-data/iconfont.css");
    layui.link(setter.base + "layui/font/layui-extend-data/iconfont.js");
    if (admin.screen() < 2) admin.sideFlexible();

    layui.config({
        base: setter.base + 'modules/'
    });

    layui.each(setter.extend, function (index, item) {
        var mods = {};
        mods[item] = '{/}' + setter.base + 'lib/extend/' + item;
        layui.extend(mods);
    });

    view().autoRender();


    exports('index', {
        openTabsPage: openTabsPage
    });
});
