package com.n3xtdata.columbus.api;

import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.core.connection.Connection;
import com.n3xtdata.columbus.data.MetadataService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiImpl implements RestApi {

  private final MetadataService metadataService;

  @Autowired
  public RestApiImpl(MetadataService metadataService) {

    this.metadataService = metadataService;
  }

  @Override
  public Set<Check> getAllChecks() {
    return this.metadataService.getAllChecks();
  }

  @Override
  public Check getCheckByLabel(@PathVariable String label) throws Exception {

    return this.metadataService.getCheckByLabel(label);
  }

  @Override
  public Set<Connection> getAllConnections() {
    return this.metadataService.getAllConnections();
  }

  @Override
  public Connection getConnectionByLabel(@PathVariable String label) throws Exception {
    return this.metadataService.getConnectionByLabel(label);
  }


}
