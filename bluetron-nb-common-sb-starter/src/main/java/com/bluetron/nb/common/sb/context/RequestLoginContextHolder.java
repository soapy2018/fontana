package com.bluetron.nb.common.sb.context;

import com.bluetron.nb.common.base.dto.ILoginAccountDTO;
import com.bluetron.nb.common.base.dto.LoginPersonnelDTO;
import com.bluetron.nb.common.base.dto.LoginUserDTO;
import com.bluetron.nb.common.base.exception.GeneralException;
import com.bluetron.nb.common.base.result.ResultCode;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * Description: 
 * @author genx
 * @date 2021/7/30 10:11
 */
public class RequestLoginContextHolder {

    private static final ThreadLocal<LoginUserDTO> REQUEST_LOGIN_HOLDER =
            new ThreadLocal();

    private static final ThreadLocal<LoginPersonnelDTO> REQUEST_LOGIN_PERSONNEL_HOLDER =
            new ThreadLocal();

    public static LoginUserDTO getCurrentLoginUser() {
        LoginUserDTO loginUserDTO = REQUEST_LOGIN_HOLDER.get();
        if (loginUserDTO != null && StringUtils.isNotBlank(loginUserDTO.getTenantId())) {
            return loginUserDTO;
        }
        //throw new AutoPartsNotLoginException();
        throw new GeneralException(ResultCode.USER_NOT_LOGGED_IN);
    }

    public static LoginPersonnelDTO getCurrentLoginPersonnel() {
        LoginPersonnelDTO loginPersonnelDTO = REQUEST_LOGIN_PERSONNEL_HOLDER.get();
        if (loginPersonnelDTO != null && StringUtils.isNotBlank(loginPersonnelDTO.getTenantId())) {
            return loginPersonnelDTO;
        }
        //throw new AutoPartsNotLoginException();
        throw new GeneralException(ResultCode.USER_NOT_LOGGED_IN);
    }

    public static ILoginAccountDTO getCurrentLoginAccount() {
        LoginUserDTO loginUserDTO = REQUEST_LOGIN_HOLDER.get();
        if (loginUserDTO != null && StringUtils.isNotBlank(loginUserDTO.getTenantId())) {
            return loginUserDTO;
        }
        LoginPersonnelDTO loginPersonnelDTO = REQUEST_LOGIN_PERSONNEL_HOLDER.get();
        if (loginPersonnelDTO != null && StringUtils.isNotBlank(loginPersonnelDTO.getTenantId())) {
            return loginPersonnelDTO;
        }
        //throw new AutoPartsNotLoginException();
        throw new GeneralException(ResultCode.USER_NOT_LOGGED_IN);
    }

    public static String getCurrentTenantId() {
        ILoginAccountDTO loginAccountDTO = getCurrentLoginAccount();
        return loginAccountDTO.getTenantId();
    }

    public static void setCurrentLoginUser(LoginUserDTO loginUserDTO) {
        REQUEST_LOGIN_HOLDER.set(loginUserDTO);
    }

    public static void setCurrentLoginPersonnel(LoginPersonnelDTO loginPersonnelDTO) {
        REQUEST_LOGIN_PERSONNEL_HOLDER.set(loginPersonnelDTO);
    }

}
