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
package org.camunda.bpm.engine.impl.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Meyer
 *
 */
public class MetricsRegistry {

  protected Map<String, Meter> meters = new HashMap<String, Meter>();

  public Meter getMeterByName(String name) {
    return meters.get(name);
  }

  public Map<String, Meter> getMeters() {
    return meters;
  }

  public void markOccurrence(String name) {
    markOccurrence(name, 1);
  }

  public void markOccurrence(String name, long times) {
    Meter meter = meters.get(name);

    if (meter != null) {
      meter.markTimes(times);
    }
  }

  public Meter createMeter(String name) {
    Meter meter = new Meter(name);
    meters.put(name, meter);
    return meter;
  }

}
