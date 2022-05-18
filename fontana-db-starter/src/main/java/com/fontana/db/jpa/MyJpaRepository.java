package com.fontana.db.jpa;

import com.fontana.base.query.BasePageQueryDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @className: MyJpaRepository
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/2/22 10:02
 */
@NoRepositoryBean
public interface MyJpaRepository<T,S> extends JpaRepository<T, S>, JpaSpecificationExecutor<T>{

    default Pageable queryByCondition(BasePageQueryDTO basePageQueryDTO){

        List<Sort.Order> sortList = null;
        basePageQueryDTO.getOrderParam().forEach(s->
                sortList.add(new Sort.Order(s.getOrder().equalsIgnoreCase(Sort.Direction.DESC.name())?Sort.Direction.DESC:Sort.Direction.ASC,s.getFieldName())));

        return PageRequest.of(basePageQueryDTO.getPageParam().getPageNum(), basePageQueryDTO.getPageParam().getPageSize(), Sort.by(sortList));
    };
}


