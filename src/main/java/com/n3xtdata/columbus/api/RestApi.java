package com.n3xtdata.columbus.api;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.core.connection.Connection;
import java.util.Set;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/api")
public interface RestApi {

  @RequestMapping(value = "/checks", method = GET)
  Set<Check> getAllChecks();

  @RequestMapping(value = "/check/{label}", method = GET)
  Check getCheckByLabel(@PathVariable String label) throws Exception;

  @RequestMapping(value = "/connections", method = GET)
  Set<Connection> getAllConnections();

  @RequestMapping(value = "/connection/{label}", method = GET)
  Connection getConnectionByLabel(@PathVariable String label) throws Exception;

}
