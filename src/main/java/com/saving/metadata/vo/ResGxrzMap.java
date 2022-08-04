package com.saving.metadata.vo;

import lombok.*;

import java.util.List;

/**
 * @program: practice
 * @description: 更新日志
 * @author: Mr.Chen
 * @create: 2020-08-28 10:39
 **/


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
@Builder
public class ResGxrzMap {

    private String topVersion;

    private List<ResGxrz> lists;
}
