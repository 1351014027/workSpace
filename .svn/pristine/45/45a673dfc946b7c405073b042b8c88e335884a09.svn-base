package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.dao.UserMapper;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.UserService;
import com.saving.metadata.utils.DateUtil;
import com.saving.metadata.utils.MD5Util;
import com.saving.metadata.utils.MapUtil;
import com.saving.metadata.vo.ResGxrzMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author saving
 * @since 2019-12-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    @Value("${application.ticketKey}")
    private String ticketKey;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Map<Object, Object> getDepMaps() {
        return MapUtil.listMapTransformMap(userMapper.getDepMaps());
    }

    @Override
    public String findDomainName() {
        return userMapper.findDomainName();
    }

    @Override
    public String ssoXxhx() {
        String ip2 = selJwIp();
        String http = userMapper.selHttp();
        String str = DateUtil.format(new Date(), "yyyyMd") + RequestHolder.getCurrenSysUser().getUsername() + DateUtil.format(new Date(), "H:mm").substring(0, 2) + ticketKey;
        String md5Str = MD5Util.encrypt(str);
        return http + "://" + ip2 + "/sav/mis/login/loginSso.do?url=/mis/sys/index.page&userid=" + RequestHolder.getCurrenSysUser().getUsername() + "&ticket=" + md5Str;
    }

    @Override
    public String ssoGljsc() {
        String ip = selCsxy();
        String http = userMapper.selHttp();
        String str = DateUtil.format(new Date(), "yyyyMd") + RequestHolder.getCurrenSysUser().getUsername() + DateUtil.format(new Date(), "H:mm").substring(0, 2) + ticketKey;
        MD5Util.encrypt(str);
        return http + "://" + ip + "/oa/ilogin/klogin.aspx?link=http://" + ip + "/ufo/login_io.aspx?id=1&U=main/Index.aspx";
    }

    @Override
    public String findShcooleName() {
        return userMapper.findSchoolName();
    }

    @Override
    public List<ResGxrzMap> getShowVersion(String sysName) {

        return userMapper.getShowVersion(sysName);
    }


    private String selJwIp() {
        return userMapper.selJwIp();
    }


    private String selCsxy() {
        return userMapper.selCsxy();
    }
}
