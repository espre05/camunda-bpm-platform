/*
 * Copyright © 2012 - 2018 camunda services GmbH and various authors (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.test.api.cfg;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.junit.After;
import org.junit.Test;

public class HistoryLevelTest {

  protected ProcessEngine processEngine;

  @Test
  public void shouldInitHistoryLevelByObject() throws Exception {
    ProcessEngineConfigurationImpl config = createConfig();
    config.setHistoryLevel(HistoryLevel.HISTORY_LEVEL_FULL);

    ProcessEngineConfigurationImpl processEngineConfiguration = buildProcessEngine(config);

    assertThat(processEngineConfiguration.getHistoryLevels().size(), is(4));
    assertThat(processEngineConfiguration.getHistoryLevel(), is(HistoryLevel.HISTORY_LEVEL_FULL));
    assertThat(processEngineConfiguration.getHistory(), is(HistoryLevel.HISTORY_LEVEL_FULL.getName()));
  }

  @Test
  public void shouldInitHistoryLevelByString() throws Exception {
    ProcessEngineConfigurationImpl config = createConfig();
    config.setHistory(HistoryLevel.HISTORY_LEVEL_FULL.getName());

    ProcessEngineConfigurationImpl processEngineConfiguration = buildProcessEngine(config);

    assertThat(processEngineConfiguration.getHistoryLevels().size(), is(4));
    assertThat(processEngineConfiguration.getHistoryLevel(), is(HistoryLevel.HISTORY_LEVEL_FULL));
    assertThat(processEngineConfiguration.getHistory(), is(HistoryLevel.HISTORY_LEVEL_FULL.getName()));
  }

  protected ProcessEngineConfigurationImpl createConfig() {
    StandaloneInMemProcessEngineConfiguration configuration = new StandaloneInMemProcessEngineConfiguration();
    configuration.setProcessEngineName("process-engine-HistoryTest");
    configuration.setDbMetricsReporterActivate(false);
    configuration.setJdbcUrl("jdbc:h2:mem:HistoryTest");
    return configuration;
  }

  protected ProcessEngineConfigurationImpl buildProcessEngine(ProcessEngineConfigurationImpl config) {
    processEngine = config.buildProcessEngine();

    return (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
  }

  @After
  public void closeEngine() {
    processEngine.close();
  }

}