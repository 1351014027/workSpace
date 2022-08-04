package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saving.metadata.dao.DwdxxbzdmysMapper;
import com.saving.metadata.pojo.Dwdxxbzdmys;
import com.saving.metadata.service.DwdxxbzdmysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 陈志强
 * @since 2020-05-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DwdxxbzdmysServiceImpl extends ServiceImpl<DwdxxbzdmysMapper, Dwdxxbzdmys> implements DwdxxbzdmysService {

    private final DwdxxbzdmysMapper dwdxxbzdmysMapper;

    @Autowired
    public DwdxxbzdmysServiceImpl(DwdxxbzdmysMapper dwdxxbzdmysMapper) {
        this.dwdxxbzdmysMapper = dwdxxbzdmysMapper;
    }


}
