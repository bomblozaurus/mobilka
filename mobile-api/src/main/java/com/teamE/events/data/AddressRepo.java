package com.teamE.events.data;

import com.teamE.events.data.entity.Address;
import org.springframework.data.repository.CrudRepository;



public interface AddressRepo extends CrudRepository<Address,Long> {
}
