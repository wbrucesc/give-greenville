package com.will.givegreenville.repositories;

import com.will.givegreenville.models.Consideration;
import com.will.givegreenville.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsiderationRepository extends CrudRepository<Consideration, Long> {
}
