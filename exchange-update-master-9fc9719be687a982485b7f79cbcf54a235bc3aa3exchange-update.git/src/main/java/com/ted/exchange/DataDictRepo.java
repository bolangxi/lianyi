package com.ted.exchange;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//标注数据访问层，也可以说用于标注数据访问组件，即DAO组件
@Repository
public interface DataDictRepo extends JpaRepository<DataDict, Integer> {
    DataDict findByKey(String key);
}
