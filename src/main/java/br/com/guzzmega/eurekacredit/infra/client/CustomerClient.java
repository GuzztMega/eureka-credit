package br.com.guzzmega.eurekacredit.infra.client;

import br.com.guzzmega.eurekacredit.domain.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "eureka-client", path = "/customers")
public interface CustomerClient {

	@GetMapping("/{document}")
	ResponseEntity<Customer> getCustomer(@PathVariable(value="document") String document);
}
