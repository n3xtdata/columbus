/**
 * Copyright 2018 https://github.com/n3xtdata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.n3xtdata.columbus.data;

import com.n3xtdata.columbus.config.Properties;
import com.n3xtdata.columbus.connectors.jdbc.JdbcConnection;
import com.n3xtdata.columbus.connectors.ssh.SshConnection;
import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.loader.ColumbusYamlLoader;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class MetadataServiceImpl implements MetadataService,
    ApplicationListener<ContextRefreshedEvent> {

  private HashMap<String, Check> checks;
  private HashMap<String, JdbcConnection> jdbcConnections;
  private HashMap<String, SshConnection> sshConnections;


  @Autowired
  public MetadataServiceImpl(){
  }

  public Set<Check> getAllChecks() {
    return new HashSet<>(this.checks.values());
  }

  public Check getCheckByLabel(String label) throws Exception {

    if(this.checks.get(label) != null) {
      return this.checks.get(label);
    }
    else {
      throw new Exception("Check " + label + " does not exist!");
    }

  }

  public Set<JdbcConnection> getAllJdbcConnections() {

    return new HashSet<>(this.jdbcConnections.values());

  }

  public JdbcConnection getJdbcConnectionByLabel(String label) throws Exception {

    if(this.jdbcConnections.get(label) != null) {
      return this.jdbcConnections.get(label);
    }
    else {
      throw new Exception("Connection " + label + " does not exist!");
    }

  }

  public Set<SshConnection> getAllSshConnections() {

    return new HashSet<>(this.sshConnections.values());

  }

  public SshConnection getSshConnectionByLabel(String label) throws Exception {

    if(this.sshConnections.get(label) != null) {
      return this.sshConnections.get(label);
    }
    else {
      throw new Exception("Connection " + label + " does not exist!");
    }

  }

  @Override
  public void onApplicationEvent(final ContextRefreshedEvent event) {

    try {
      this.loadAll();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private void loadAll() throws InstantiationException, IllegalAccessException {

    ColumbusYamlLoader<Check> checkLoader = new ColumbusYamlLoader<>(Check.class);
    this.checks = checkLoader.load();

    ColumbusYamlLoader<JdbcConnection> jdbcLoader = new ColumbusYamlLoader<>(JdbcConnection.class);
    this.jdbcConnections = jdbcLoader.load();

    ColumbusYamlLoader<SshConnection> sshLoader = new ColumbusYamlLoader<>(SshConnection.class);
    this.sshConnections = sshLoader.load();

  }

}
