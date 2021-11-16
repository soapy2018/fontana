package com.bluetron.nb.common.base.dto.req;


import com.bluetron.nb.common.base.dto.LoginUserDTO;

import java.io.Serializable;

/**
 * Description:
 *
 * @author genx
 * @date 2021/4/16 13:48
 */
public interface IEntityUploadReq<E> extends Serializable {

    E toEntity(LoginUserDTO loginUserDTO);

}
