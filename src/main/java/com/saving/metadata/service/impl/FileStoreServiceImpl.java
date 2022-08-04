package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saving.metadata.dao.FileStoreMapper;
import com.saving.metadata.pojo.FileStore;
import com.saving.metadata.service.FileStoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 陈志强
 * @since 2019-12-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FileStoreServiceImpl extends ServiceImpl<FileStoreMapper, FileStore> implements FileStoreService {

}
